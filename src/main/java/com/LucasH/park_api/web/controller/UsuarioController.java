package com.LucasH.park_api.web.controller;

import com.LucasH.park_api.entity.Usuario;
import com.LucasH.park_api.service.UsuarioService;
import com.LucasH.park_api.web.dto.UsuarioCreateDto;
import com.LucasH.park_api.web.dto.UsuarioResponseDto;
import com.LucasH.park_api.web.dto.UsuarioSenhaDto;
import com.LucasH.park_api.web.dto.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping()
    public ResponseEntity<UsuarioResponseDto> createUsuario(@RequestBody UsuarioCreateDto createDto) {
       Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
       return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toDto(user));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UsuarioSenhaDto dto) {
        Usuario user = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>>  getAll() {
        List<Usuario> users = usuarioService.ListarTodosUsuarios();
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toListDto(users));
    }
}
