package com.LucasH.park_api.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //JwtAuthenticationEntryPoint é responsável por lidar com tentativas de acesso a recursos protegidos
    // sem autenticação válida, registrando o erro e retornando uma resposta HTTP 401 Unauthorized, sinalizando
    // para o cliente que ele deve fornecer um token JWT válido para acessar o recurso.

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("Http Status {}", authException.getMessage());
        response.setHeader("www-authenticate", "Bearer realm='/api/v1/auth'" );
        response.sendError(401);
    }
}
