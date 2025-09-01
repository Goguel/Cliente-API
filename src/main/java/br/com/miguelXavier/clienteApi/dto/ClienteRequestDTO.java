package br.com.miguelXavier.clienteApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record ClienteRequestDTO(
        @NotBlank(message = "O campo nome não pode ser vazio.")
        String nome,

        @NotBlank(message = "O campo CPF não pode ser vazio.")
        @CPF(message = "CPF inválido.")
        String cpf,

        @NotNull(message = "O campo data de nascimento não pode ser nulo.")
        @Past(message = "A data de nascimento deve ser uma data no passado.")
        LocalDate dataNascimento
) {}