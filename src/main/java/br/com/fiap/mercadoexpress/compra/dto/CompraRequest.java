package br.com.fiap.mercadoexpress.compra.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CompraRequest(

        @NotBlank(message = "clienteNome é obrigatório")
        String clienteNome,

        @NotBlank(message = "clienteEmail é obrigatório")
        @Email(message = "clienteEmail inválido")
        String clienteEmail,

        @NotBlank(message = "numeroCartao é obrigatório")
        String numeroCartao,

        @NotEmpty(message = "informe ao menos um item")
        @Valid
        List<ItemRequest> itens
) {
    public record ItemRequest(
            @NotNull(message = "produtoId é obrigatório")
            Long produtoId,

            @NotNull(message = "quantidade é obrigatória")
            @Positive(message = "quantidade deve ser maior que zero")
            Integer quantidade
    ) {
    }
}
