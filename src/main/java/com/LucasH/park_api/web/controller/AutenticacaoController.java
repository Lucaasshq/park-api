package com.LucasH.park_api.web.controller;

import com.LucasH.park_api.jwt.JwtToken;
import com.LucasH.park_api.jwt.JwtUserDetailsService;
import com.LucasH.park_api.web.dto.UsuarioLoginDto;
import com.LucasH.park_api.web.exeception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
//Indica que essa classe é um controller REST do Spring. Ele
// expõe endpoints HTTP para serem consumidos via requisições HTTP, como POST ou GET
public class AutenticacaoController {

    private final JwtUserDetailsService detailsService;
    //Este serviço é usado para buscar os detalhes do usuário e
    // gerar o token JWT após a autenticação bem-sucedida.

    private final AuthenticationManager authenticationManager;
    // do Spring Security é responsável por autenticar o
    // usuário com base em suas credenciais (nome de usuário e senha).

    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto dto, HttpServletRequest request) {
        log.info("Processo de autenticacao do usuario: {}", dto.getUsername());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            //O objeto UsernamePasswordAuthenticationToken é criado com as credenciais do usuário
            // (nome de usuário e senha), fornecidas no UsuarioLoginDto

            authenticationManager.authenticate(authenticationToken);
            //Este token é enviado para o authenticationManager.authenticate()
            // para verificar as credenciais.

            JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());
            return ResponseEntity.ok(token);

        } catch (AuthenticationException ex) {
            log.warn("Bad Credentials from username: '{}'", dto.getUsername());
        }
        return ResponseEntity.badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais Inválidas"));
    }
}
