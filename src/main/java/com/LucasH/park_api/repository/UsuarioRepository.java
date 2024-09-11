package com.LucasH.park_api.repository;

import com.LucasH.park_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
   Optional<Usuario> findByUsername(String username);
}
