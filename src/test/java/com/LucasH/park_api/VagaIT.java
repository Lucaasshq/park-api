package com.LucasH.park_api;

import com.LucasH.park_api.web.dto.VagaCreateDto;
import com.LucasH.park_api.web.exeception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vagas/vagas-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vagas/vagas-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagaIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void criarVaga_ComDadosValidos_RetornarLocationStatus200() {
           testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"admin@gmail.com","123456" ))
                .bodyValue(new VagaCreateDto("A-05", "LIVRE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void criarVaga_ComDadosJaCadastrados_RetornarErrorMensageComStatus409() {
      ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"admin@gmail.com","123456" ))
                .bodyValue(new VagaCreateDto("A-04", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
        Assertions.assertThat(responseBody.getMethod()).isEqualTo("POST");
        Assertions.assertThat(responseBody.getPath()).isEqualTo("/api/v1/vagas");

    }

    @Test
    public void criarVaga_ComDadosInvalidos_RetornarErrorMensageComStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"admin@gmail.com","123456" ))
                .bodyValue(new VagaCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        Assertions.assertThat(responseBody.getMethod()).isEqualTo("POST");
        Assertions.assertThat(responseBody.getPath()).isEqualTo("/api/v1/vagas");


        testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"admin@gmail.com","123456" ))
                .bodyValue(new VagaCreateDto("A-5011", "DESOCUPADO"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        Assertions.assertThat(responseBody.getMethod()).isEqualTo("POST");
        Assertions.assertThat(responseBody.getPath()).isEqualTo("/api/v1/vagas");
    }
}
