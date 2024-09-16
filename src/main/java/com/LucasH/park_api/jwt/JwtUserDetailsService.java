package com.LucasH.park_api.jwt;

import com.LucasH.park_api.entity.Usuario;
import com.LucasH.park_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implementa o método da interface UserDetailsService, que é usado pelo Spring Security para
        // carregar os detalhes de um usuário com base no nome de usuário.
        Usuario usuario = usuarioService.buscarPorUsername(username);
        // usa o usarioService para buscar um usuário no banco de dados
        // a partir do username

        return new JwtUserDetails(usuario);
        //Se o usuário for encontrado, é retornada uma instância de JwtUserDetails,
        // que é uma classe que estende User e implementa a interface UserDetails.
        // O Spring Security utiliza este objeto UserDetails para autenticar o usuário
    }



    public JwtToken getTokenAuthenticated(String username) {
     Usuario.Role role  = usuarioService.buscarRolePorUsername(username);
         return JwtUtils.createJwtToken(username, role.name() );
         //Gera um token JWT para um usuário autenticado, baseado no seu nome de usuário e papel (role).

    }
}
