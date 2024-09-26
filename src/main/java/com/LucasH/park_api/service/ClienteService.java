package com.LucasH.park_api.service;

import com.LucasH.park_api.entity.Cliente;
import com.LucasH.park_api.exeception.CpfUniqueViolationExeception;
import com.LucasH.park_api.exeception.EntityNotFoundException;
import com.LucasH.park_api.repository.ClienteRepository;
import com.LucasH.park_api.repository.projection.ClienteProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
     @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
         return clienteRepository.findById(id).orElseThrow(
                 () -> new EntityNotFoundException("Cliente de id " + id + " não encontrado no sistema")
         );
     }

     @Transactional(readOnly = true)
    public Page<ClienteProjection> buscarTodos(Pageable pageable) {
        return clienteRepository.findAllPageable(pageable);
    }
    @Transactional(readOnly = true)
    public Cliente buscarPorUsuarioId(Long id) {
        return clienteRepository.findByUsuarioId(id);
    }
}
