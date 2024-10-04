package com.LucasH.park_api.service;

import com.LucasH.park_api.entity.ClienteVaga;
import com.LucasH.park_api.exeception.EntityNotFoundException;
import com.LucasH.park_api.repository.ClienteVagaRepository;
import com.LucasH.park_api.repository.projection.ClienteVagaProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class ClienteVagaService{

    private final ClienteVagaRepository vagaRepository;

    public ClienteVagaService(ClienteVagaRepository vagaRepository) {
        this.vagaRepository = vagaRepository;
    }


    @Transactional
    public ClienteVaga salvar(ClienteVaga vaga) {
        return vagaRepository.save(vaga);
    }

    public ClienteVaga buscarPorRecibo(String recibo) {
        return vagaRepository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
                () -> new EntityNotFoundException(
                        "Recibo " + recibo + " Não encontrado no sistema ou check-out já realizado"
                )
        );
    }

    @Transactional(readOnly = true)
    public long getTotalDevezesEstacionamentoCompleto(String cpf) {
        return vagaRepository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosProClienteCpf(String cpf, Pageable pageable) {
        return vagaRepository.findAllByClienteCpf(cpf, pageable);
    }


    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosPorUsuarioId(Long id, Pageable pageable) {
        return vagaRepository.findAllByClienteUsuarioId(id, pageable);
    }
}
