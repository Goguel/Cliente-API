package br.com.miguelXavier.clienteApi.service;

import br.com.miguelXavier.clienteApi.dto.ClienteRequestDTO;
import br.com.miguelXavier.clienteApi.dto.ClienteResponseDTO;
import br.com.miguelXavier.clienteApi.model.Cliente;
import br.com.miguelXavier.clienteApi.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> listarClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> buscarClientesPorCriterio(Specification<Cliente> spec, Pageable pageable) {
        return clienteRepository.findAll(spec, pageable).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));
    }

    @Transactional
    public ClienteResponseDTO criarCliente(ClienteRequestDTO dto) {
        if (clienteRepository.findByCpf(dto.cpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema.");
        }
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setDataNascimento(dto.dataNascimento());
        return toResponseDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));

        clienteRepository.findByCpf(dto.cpf()).ifPresent(clienteExistente -> {
            if (!clienteExistente.getId().equals(id)) {
                throw new IllegalArgumentException("CPF já cadastrado para outro cliente.");
            }
        });

        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setDataNascimento(dto.dataNascimento());
        return toResponseDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public void excluirCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getDataNascimento(),
                Period.between(cliente.getDataNascimento(), LocalDate.now()).getYears()
        );
    }
}