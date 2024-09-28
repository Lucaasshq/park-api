package com.LucasH.park_api.service;

import com.LucasH.park_api.entity.Vaga;
import com.LucasH.park_api.exeception.CodigoUniqueViolationExeception;
import com.LucasH.park_api.exeception.EntityNotFoundException;
import com.LucasH.park_api.repository.VagaRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

public class VagaService {

    private final VagaRepository vagaRepository;

    public Vaga salvar(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        }catch (DataIntegrityViolationException ex) {
            throw new CodigoUniqueViolationExeception("Vaga com código " + vaga.getCodigo() + " já cadastrada");
        }
    }

    public Vaga buscarPorCodigo(String codigo) {
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new EntityNotFoundException("Vaga " + codigo " não foi encontrada")
        );
    }
}
