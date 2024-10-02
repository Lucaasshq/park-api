package com.LucasH.park_api.service;

import com.LucasH.park_api.entity.ClienteVaga;
import com.LucasH.park_api.exeception.EntityNotFoundException;
import com.LucasH.park_api.repository.projection.ClienteVagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class ClienteVagaService{

    private final ClienteVagaRepository vagaRepository;

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



}
