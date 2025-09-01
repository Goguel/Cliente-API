package br.com.miguelXavier.clienteApi.service;

import br.com.miguelXavier.clienteApi.dto.ClienteRequestDTO;
import br.com.miguelXavier.clienteApi.dto.ClienteResponseDTO;
import br.com.miguelXavier.clienteApi.model.Cliente;
import br.com.miguelXavier.clienteApi.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveRetornarDTOComIdadeCorretaAoBuscarClientePorId() {
        Long clienteId = 1L;
        Cliente clienteMock = new Cliente();
        clienteMock.setId(clienteId);
        clienteMock.setNome("Teste");
        clienteMock.setCpf("12345678901");
        clienteMock.setDataNascimento(LocalDate.now().minusYears(30)); // Cliente de 30 anos

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteMock));

        ClienteResponseDTO resultadoDTO = clienteService.buscarClientePorId(clienteId);

        assertNotNull(resultadoDTO);
        assertEquals(30, resultadoDTO.idade());
        assertEquals("Teste", resultadoDTO.nome());
    }

    @Test
    void deveLancarExcecaoAoCriarClienteComCpfDuplicado() {
        ClienteRequestDTO requestDTO = new ClienteRequestDTO("Novo Cliente", "12345678901", LocalDate.now().minusYears(25));
        when(clienteRepository.findByCpf(requestDTO.cpf())).thenReturn(Optional.of(new Cliente()));

        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.criarCliente(requestDTO);
        });

        verify(clienteRepository, never()).save(any(Cliente.class));
    }
}