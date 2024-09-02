package com.LucasH.park_api.web.dto.mapper;

import com.LucasH.park_api.entity.Usuario;
import com.LucasH.park_api.web.dto.UsuarioCreateDto;
import com.LucasH.park_api.web.dto.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

public class UsuarioMapper {

    public static Usuario toUsuario(UsuarioCreateDto createDto){
        return new ModelMapper().map(createDto, Usuario.class);
    }

    public static UsuarioResponseDto toDto(Usuario usuario) {
        Usuario.Role role = usuario.getRole();
        ModelMapper mapperMain = new ModelMapper();
        TypeMap<Usuario, UsuarioResponseDto> propertyMapper = mapperMain.createTypeMap(Usuario.class, UsuarioResponseDto.class);
        propertyMapper.addMappings(
                mapper -> mapper.map(_ -> role, UsuarioResponseDto::setRole)
        );
        return mapperMain.map(usuario, UsuarioResponseDto.class);
    }
}
