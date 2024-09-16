package com.LucasH.park_api.service;

import com.LucasH.park_api.entity.Usuario;
import com.LucasH.park_api.exeception.EntityNotFoundException;
import com.LucasH.park_api.exeception.PasswordInvalidException;
import com.LucasH.park_api.repository.UsuarioRepository;
import com.LucasH.park_api.exeception.UsernameUniqueViolationExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationExeception(String.format("Username {%s} já cadastrado", usuario.getUsername()));
        }

    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        // método findyById tenta buscar um usuario no banco de dados
        // caso esse id não exista no banco ele lanca uma RuntimeException
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Usuário " + id + " não encontrado")
        );
    }

    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        if (!novaSenha.equals(confirmaSenha)) {
            throw new PasswordInvalidException("Nova senha não confere com confirmação de senha.");
        }
        Usuario user = buscarPorId(id);

        if (!passwordEncoder.matches(senhaAtual, user.getPassword())) {
            throw  new PasswordInvalidException("Sua senha não confere");
        }
        user.setPassword(passwordEncoder.encode(novaSenha));
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }



    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(username + " não encontrado")
        );
    }

    @Transactional
    public Usuario.Role buscarRolePorUsername(String username) {
        return usuarioRepository.findRoleByUsername(username);
    }
}
