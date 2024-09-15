package com.LucasH.park_api.jwt;

import com.LucasH.park_api.entity.Usuario;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

//A classe User armazena as informações necessárias para
// autenticar um usuário, como nome de usuário, senha e as autoridades (roles)
public class JwtUserDetails extends User {

    private Usuario usuario;

    public JwtUserDetails(Usuario usuario) {
        super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        // Super: Chama o construtor da classe User do Spring, que espera três parâmetros:
        this.usuario = usuario;
    }

    public Long getId() {
        return this.usuario.getId();
    }

    public String getRole() {
        return this.usuario.getRole().name();
    }


}
