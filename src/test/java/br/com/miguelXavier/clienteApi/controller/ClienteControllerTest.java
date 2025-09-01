package br.com.miguelXavier.clienteApi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void deveRetornarStatus201AoCadastrarClienteComDadosValidos() throws Exception {
        String clienteJson = """
                {
                    "nome": "Cliente Miguel2",
                    "cpf": "62492922090",
                    "dataNascimento": "2000-01-01"
                }
                """;

        mockMvc.perform(
                post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteJson)
        ).andExpect(status().isCreated());
    }

    @Test
    void deveRetornarStatus403AoTentarAcessarSemAutenticacao() throws Exception {
        String clienteJson = "{}";

        mockMvc.perform(
                post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteJson)
        ).andExpect(status().isForbidden());
    }
}