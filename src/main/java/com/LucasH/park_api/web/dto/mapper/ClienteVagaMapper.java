package com.LucasH.park_api.web.dto.mapper;

import com.LucasH.park_api.entity.ClienteVaga;
import com.LucasH.park_api.web.dto.EstacionamentoCreateDto;
import com.LucasH.park_api.web.dto.EstacionamentoResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public ClienteVaga toClienteVaga (EstacionamentoCreateDto dto) {
        return new ModelMapper().map(dto, ClienteVaga.class);
    }

    public EstacionamentoResponseDto toDto (ClienteVaga clienteVaga) {
        return new ModelMapper().map(clienteVaga, EstacionamentoResponseDto.class);
    }
}