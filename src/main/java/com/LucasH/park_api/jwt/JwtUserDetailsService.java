package com.LucasH.park_api.jwt;

import com.LucasH.park_api.entity.Usuario;
import com.LucasH.park_api.repository.UsuarioRepository;
import com.LucasH.park_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;


    //Verifica se o usuário esta presente no banco de dados aparti do nome dele
    // Caso não esteja retorna uma UsernameNotFoundExeception
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(username);

        return new JwtUserDetails(usuario);
    }

    //Verifica se o usuário esta presente no banco de dados e apartir disso cria seu token

    public JwtToken getTokenAuthenticated(String username) {
     Usuario.Role role  = usuarioService.buscarRolePorUsername(username);
         return JwtUtils.createJwtToken(username, role.name() );

    }
}
