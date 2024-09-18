package com.LucasH.park_api;

import com.LucasH.park_api.jwt.JwtToken;
import com.LucasH.park_api.web.dto.UsuarioLoginDto;
import com.LucasH.park_api.web.dto.UsuarioResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;
// A classe JwtAuthentication tem como objetivo fornecer uma maneira conveniente de gerar um token JWT (JSON Web Token) ao
// realizar uma requisição de autenticação na API e depois usar esse token como parte dos cabeçalhos de autorização para chamadas
// subsequentes de testes ou requisições protegidas. Vamos detalhar o que cada parte dessa classe faz.
public class JwtAuthentication {

    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient client, String username, String password) {
         String token = client
                 .post()  // Faz uma requisição POST ao servidor.
                 .uri("/api/v1/auth") // Define o endpoint para autenticação, no caso /api/v1/auth.
                 .bodyValue(new UsuarioLoginDto(username, password)) // Envia o corpo da requisição com as credenciais do usuário encapsuladas em um objeto UsuarioLoginDto (que contém username e password).
                 .exchange()// Executa a requisição.
                 .expectStatus().isOk() // Verifica se a resposta HTTP tem o status 200 OK
                 .expectBody(JwtToken.class) // : Especifica que o corpo da resposta será do tipo JwtToken.
                 .returnResult().getResponseBody().getToken(); //  Obtém o corpo da resposta, que contém o token
                  //  Extrai o token JWT do objeto JwtToken.
         return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
         //Esta linha cria um consumidor que modifica os cabeçalhos HTTP, adicionando o token JWT
        // como um valor do cabeçalho Authorization no formato Bearer {token}. Isso permite que futuras
        // requisições que exigem autenticação incluam automaticamente o token nos cabeçalhos.
    }
}
