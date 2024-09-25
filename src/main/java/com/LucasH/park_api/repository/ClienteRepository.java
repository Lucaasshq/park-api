package com.LucasH.park_api.repository;

import com.LucasH.park_api.entity.Cliente;
import com.LucasH.park_api.repository.projection.ClienteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c")
    Page<ClienteProjection> findAllPageable(Pageable pageable);
}
