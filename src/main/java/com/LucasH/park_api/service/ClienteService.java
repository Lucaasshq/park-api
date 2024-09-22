package com.LucasH.park_api.service;

import com.LucasH.park_api.entity.Cliente;
import com.LucasH.park_api.exeception.CpfUniqueViolationExeception;
import com.LucasH.park_api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        try {
            return  clienteRepository.save(cliente);

        } catch (DataIntegrityViolationException e) {
            throw  new CpfUniqueViolationExeception("CPF " + cliente.getCpf() + " não pode ser cadastrado, já existe no sistema" );
        }
    }
}
