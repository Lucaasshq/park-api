package com.LucasH.park_api.repository;

import com.LucasH.park_api.entity.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VagaRepository extends JpaRepository<Vaga, Long> {
    Optional<Vaga> findByCodigo(String codigo);

    Optional<Vaga> findFirstByStatus(com.LucasH.park_api.entity.Vaga.StatusVaga statusVaga);
}
