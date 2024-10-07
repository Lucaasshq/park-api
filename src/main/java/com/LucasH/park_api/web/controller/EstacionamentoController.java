package com.LucasH.park_api.web.controller;

import com.LucasH.park_api.entity.ClienteVaga;
import com.LucasH.park_api.jwt.JwtUserDetails;
import com.LucasH.park_api.repository.projection.ClienteVagaProjection;
import com.LucasH.park_api.service.ClienteService;
import com.LucasH.park_api.service.ClienteVagaService;
import com.LucasH.park_api.service.EstacionamentoService;
import com.LucasH.park_api.web.dto.EstacionamentoCreateDto;
import com.LucasH.park_api.web.dto.EstacionamentoResponseDto;
import com.LucasH.park_api.web.dto.PageableDto;
import com.LucasH.park_api.web.dto.mapper.ClienteVagaMapper;
import com.LucasH.park_api.web.dto.mapper.PageableMapper;
import com.LucasH.park_api.web.exeception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Estacionamentos", description = "Operação de registro de entrada e saida de um veiculo do estacionamento.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/estacionamentos")
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;


    @Operation(summary = "Operação de check-in", description = "Recurso para dar entradde um veiculo no estacionamento",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso e retorno de um bearer token",
                            headers = @Header(name = HttpHeaders.LOCATION,
                            description = "URL de acesso ao recurso criado"),
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Causas possiveis: <br/>" +
                            "- CPF do cliente não cadastrado no sistema <br/>" +
                            "- Nenhuma vaga livre foi localizada;",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "403", description = "Recurso não permitdo ao ROLE= USER",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping("/check-in")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkIn (@RequestBody @Valid EstacionamentoCreateDto dto) {
        ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.checkIn(clienteVaga);

        EstacionamentoResponseDto responseDto = ClienteVagaMapper.toDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/{check-in}/{recibo}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<EstacionamentoResponseDto> getByRecibo(@PathVariable String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Operação de check-out", description = "Recurso para dar saida um veiculo no estacionamento",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Número de recibo não existe ou veiculo já passou pelo check-out",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitdo ao ROLE= USER",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping("/check-out/{recibo}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkOut(@PathVariable String recibo) {
        ClienteVaga clienteVaga = estacionamentoService.checkOut(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }


    @Operation(summary = "Localizar os registros de estacionamnetos do cliente por CPF", description = "Localizar os registros de estacionamnetos do cliente por CPF." +
            "Requisição exige bearer toke, ROLE=ADMIN",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "cpf", description = "N° do CPF referente ao cliente a ser consultado", required = true),
                    @Parameter(in = ParameterIn.QUERY, name = "page", description = "Representa a página a ser retornada",
                        content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),

                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Representa o total de elementos por pagina",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5"))),

                    @Parameter(in = ParameterIn.QUERY, name = "sort", description = "Representa o padrão de ordenação 'dataEntrada,asc'",
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dataEntrada,asc")),
                            hidden = true),


            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitdo ao ROLE= USER",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageableDto> getAllEstacionamentoPorCpf(@PathVariable String cpf,
                                                                  @PageableDefault(size = 5, sort = "dataEntrada",
                                                                  direction = Sort.Direction.ASC) Pageable pageable) {
       Page<ClienteVagaProjection> projectionPage = clienteVagaService.buscarTodosProClienteCpf(cpf, pageable);
       PageableDto dto = PageableMapper.toDto(projectionPage);
       return ResponseEntity.ok(dto);
    }


    @Operation(summary = "Localizar os registros de estacionamnetos do cliente por CPF", description = "Localizar os registros de estacionamnetos do cliente por CPF." +
            "Requisição exige bearer toke, ROLE=ADMIN",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "cpf", description = "N° do CPF referente ao cliente a ser consultado", required = true),
                    @Parameter(in = ParameterIn.QUERY, name = "page", description = "Representa a página a ser retornada",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),

                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Representa o total de elementos por pagina",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5"))),

                    @Parameter(in = ParameterIn.QUERY, name = "sort", description = "Representa o padrão de ordenação 'dataEntrada,asc'",
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dataEntrada,asc")),
                            hidden = true),


            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitdo ao ROLE= USER",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PageableDto> getAllEstacionamentosDoCliente(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                                                    @PageableDefault(size = 5, sort = "dataEntrada",
                                                                                    direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClienteVagaProjection> projectionPage = clienteVagaService.buscarTodosPorUsuarioId(userDetails.getId(), pageable);
        PageableDto dto = PageableMapper.toDto(projectionPage);
        return ResponseEntity.ok(dto);
    }
}
