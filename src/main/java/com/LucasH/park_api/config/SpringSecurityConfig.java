package com.LucasH.park_api.config;

import com.LucasH.park_api.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
//Ativa as configurações padrão do Spring MVC, como suporte a JSON, XML
@EnableMethodSecurity
// Habilita a segurança baseada em métodos, permitindo o uso de anotações como @PreAuthorize ou @Secured
// para definir regras de acesso nos métodos
public class SpringSecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception  {
        // Este método configura a cadeia de filtros de segurança do Spring Security.
        return http
                .csrf(csrf -> csrf.disable()) // Desabilita a proteção CSRF, uma vez que a aplicação provavelmente não usa sessões
                .formLogin(form -> form.disable()) // Desabilita o login via formulário e o HTTP Basic authentication, pois será usada autenticação via JWT.
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "api/v1/usuarios").permitAll()
                        //Permite que qualquer um acesse o endpoint de criação de usuários (api/v1/usuarios)
                        .requestMatchers(HttpMethod.POST, "api/v1/auth").permitAll()
                        //Permite que qualquer um acesse o endpoint de autenticação (api/v1/auth)
                        .anyRequest().authenticated() // Todas as outras requisições precisam estar autenticadas
                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //Define que a política de criação de sessão será stateless, o que significa que o servidor não mantém sessão entre as requisições (ideal para autenticação com JWT)
                ).addFilterBefore(
                        jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class
                // Adiciona o filtro de altorização JWT antes do filtro padrão de autenticação por nomes de usuário e senha
                ).build();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
        // Retorna uma instância do filtro de autorização JWT. Esse filtro
        //intercepta as requisições e verifica a presença e validade do token JWT no cabeçalho da requisição.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //Define um bean para o codificador de senhas BCryptPasswordEncoder, usado para
        // armazenar senhas de forma segura, já que BCrypt é um algoritmo de hashing forte.
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
        //Define um bean para o gerenciador de autenticação. Ele usa a configuração
        // de autenticação (que pode ser, por exemplo, UserDetailsService
        // ou outro provedor de autenticação configurado) para retornar o AuthenticationManager
    }
}
