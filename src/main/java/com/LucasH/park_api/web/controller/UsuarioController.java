package com.LucasH.park_api.web.controller;

import com.LucasH.park_api.entity.Usuario;
import com.LucasH.park_api.service.UsuarioService;
import com.LucasH.park_api.web.dto.UsuarioCreateDto;
import com.LucasH.park_api.web.dto.UsuarioResponseDto;
import com.LucasH.park_api.web.dto.UsuarioSenhaDto;
import com.LucasH.park_api.web.dto.mapper.UsuarioMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Usuarios", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Cria um novo usuário", description = "Recurso para criar um novo usuário",
    responses = {@ApiResponse(responseCode = "201", description = "Recurso criando com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                 @ApiResponse})
    @PostMapping()
    public ResponseEntity<UsuarioResponseDto> createUsuario(@Valid @RequestBody UsuarioCreateDto createDto) {
       Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
       return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toDto(user));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody  UsuarioSenhaDto dto) {
        Usuario user = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmarSenha());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>>  getAll() {
        List<Usuario> users = usuarioService.ListarTodosUsuarios();
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toListDto(users));
    }
}
