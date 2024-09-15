package com.LucasH.park_api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Slf4j
public class JwtUtils {
    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "6875231489-2469874563-3489653178";
    public static final long EXPIRE_DAYS = 0;
    public static final long EXPIRE_HOURS = 0;
    public static final long EXPIRE_MINUTES = 5;

    private JwtUtils() {

    }

    private static javax.crypto.SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        //Este método gera a chave secreta usada para assinar o token JWT usando o algoritmo HMAC-SHA (neste caso, HMAC-SHA256).
        // Ele converte a chave secreta de uma string para um objeto javax.crypto.SecretKey.
    }

    private static Date toExpireDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
        // esse método calcula a data de expiração do token. Ele usa a data de emissão (passada como parâmetro)
        // e adiciona o tempo definido nas constantes
    }

    public static JwtToken createJwtToken(String username, String role) {
        Date issuedAt = new Date();
        // Define a data de emissão
        Date limit = toExpireDate(issuedAt);
        // Calcula a data de expiração
        Key key = generateKey();

        String token = Jwts.builder()
                .header().add("typ","JWT")// Adiciona o tipo do token no cabeçalho (JWT)
                .and()
                .subject(username) // Define o nome de usuário (subject) no payload
                .issuedAt(issuedAt) // Define a data de emissão
                .expiration(limit) // Define a data de expiração
                .signWith(generateKey()) // Assina o token usando a chave gerada
                .claim("role", role) // Adiciona uma claim personalizada, no caso, o papel (role) do usuário
                .compact();

        return new JwtToken(token);
    }

    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(generateKey()) //  usa a chave secreta para verificar a autenticidade do token
                    .build()
                    .parseSignedClaims(refactorToken(token)).getPayload(); //  o método retorna todas as claims do token, que incluem o payload inteiro
        } catch (JwtException ex){
            log.error("Token invalido " + ex.getMessage());
        }
        return null;
    }

    private static String refactorToken(String token) {
        if (token.contains(JWT_BEARER)){
            return token.substring(JWT_BEARER.length());
            // Remove o prefixo "Bearer " do token, caso ele esteja presente. Esse método é útil porque,
            // muitas vezes, os tokens são enviados no formato "Bearer <token>" no cabeçalho HTTP
            // e é necessário removê-lo antes de realizar qualquer processamento.
        }
        return token;
    }


    public static String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
        // Extrai o nome de usuário (subject) das claims contidas no token JWT, usando o metódo GetClaimsFromToken()
    }

    public static boolean isTokenValid(String token) {
        try {
             Jwts.parser()
                    .verifyWith(generateKey())
                     .build()
                    .parseSignedClaims(refactorToken(token));
             return true;
             //Verifica se o token JWT é válido. Ele tenta fazer o parsing do token e
            // , se for bem-sucedido (ou seja, o token não está expirado e foi assinado corretamente),
            // retorna true
            // caso contrario retorna false
        } catch (JwtException ex){
            log.error("Token invalido " + ex.getMessage());
        }
        return false;
    }



}
