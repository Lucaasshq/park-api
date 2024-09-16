package com.LucasH.park_api.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


@Slf4j

//  filtro de autorização JWT que intercepta todas as requisições HTTP
//  e verifica se um token JWT válido está presente no cabeçalho da requisição.
//  Ela estende a classe OncePerRequestFilter, o que significa que esse filtro
//  será executado uma vez por requisição
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    //Este é o método principal que executa o filtro. Ele é chamado para cada
    // requisição HTTP que passa pelo sistema

    @Autowired
    private JwtUserDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);
        if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)) {
            log.info("JWT Token está nulo, vazio ou não iniciado com 'Bearer '.");
            filterChain.doFilter(request, response);
            return;
            //O token JWT é extraído do cabeçalho da requisição HTTP usando
            // request.getHeader(JwtUtils.JWT_AUTHORIZATION).
            // Ele busca o token no campo Authorization do cabeçalho HTTP.
            //Se o token for nulo, vazio, ou não começar com "Bearer ",
            // o filtro não faz nada e a requisição continua
            // (chama filterChain.doFilter(request, response)).
        }

        if (!JwtUtils.isTokenValid(token)) {
            log.warn("JWT Token está inválido ou expirado.");
            filterChain.doFilter(request, response);
            return;
            //Após verificar a presença do token, o método JwtUtils.isTokenValid(token)
            // é chamado para verificar se o token é
            // válido (se está assinado corretamente e não expirou).
            //Se o token não for válido, a requisição é encerrada (a requisição continua
            // sem modificar o contexto de segurança).
        }

        String username = JwtUtils.getUsernameFromToken(token);
        //Se o token for válido, o nome de usuário associado ao token é extraído através
        // do método JwtUtils.getUsernameFromToken(token).


        toAuthentication(request, username);
        //Após obter o nome de usuário, o método toAuthentication(request, username)
        // é chamado para autenticar o usuário. Aqui está o que acontece nesse método:


        filterChain.doFilter(request, response);

    }

    private void toAuthentication(HttpServletRequest request, String username) {
        UserDetails userDetails = detailsService.loadUserByUsername(username);
        //é chamado para carregar as informações do usuário com base no nome de usuário.

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());


        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //As informações de autenticação são associadas à requisição HTTP

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //A autenticação é armazenada no contexto de segurança
        // do Spring (SecurityContextHolder.getContext().setAuthentication(authenticationToken)),
        // o que significa que o usuário agora está autenticado para essa requisição.
    }
}
