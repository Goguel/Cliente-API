package br.com.miguelXavier.clienteApi.controller;

import br.com.miguelXavier.clienteApi.dto.ClienteRequestDTO;
import br.com.miguelXavier.clienteApi.dto.ClienteResponseDTO;
import br.com.miguelXavier.clienteApi.model.Cliente;
import br.com.miguelXavier.clienteApi.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Lista todos os clientes de forma paginada")
    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(clienteService.listarClientes(pageable));
    }

    @Operation(summary = "Busca clientes por nome ou CPF")
    @GetMapping("/buscar")
    public ResponseEntity<Page<ClienteResponseDTO>> buscarPorCriterio(
            @And({
                    @Spec(path = "nome", params = "nome", spec = LikeIgnoreCase.class),
                    @Spec(path = "cpf", params = "cpf", spec = LikeIgnoreCase.class)
            }) Specification<Cliente> spec,
            Pageable pageable) {
        return ResponseEntity.ok(clienteService.buscarClientesPorCriterio(spec, pageable));
    }

    @Operation(summary = "Busca um cliente específico por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarClientePorId(id));
    }

    @Operation(summary = "Cadastra um novo cliente")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(@RequestBody @Valid ClienteRequestDTO dto, UriComponentsBuilder uriBuilder) {
        ClienteResponseDTO clienteSalvo = clienteService.criarCliente(dto);
        URI uri = uriBuilder.path("/clientes/{id}").buildAndExpand(clienteSalvo.id()).toUri();
        return ResponseEntity.created(uri).body(clienteSalvo);
    }

    @Operation(summary = "Atualiza os dados de um cliente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO dto) {
        return ResponseEntity.ok(clienteService.atualizarCliente(id, dto));
    }

    @Operation(summary = "Exclui um cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        clienteService.excluirCliente(id);
        return ResponseEntity.noContent().build();
    }
}