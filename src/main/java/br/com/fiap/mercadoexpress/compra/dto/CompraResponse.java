package br.com.fiap.mercadoexpress.compra.dto;

import br.com.fiap.mercadoexpress.compra.entity.Compra;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CompraResponse(
        Long id,
        String clienteNome,
        String clienteEmail,
        BigDecimal total,
        String status,
        String transacaoId,
        String mensagemBanco,
        LocalDateTime dataCompra,
        List<ItemResponse> itens
) {
    public record ItemResponse(Long produtoId, String produtoNome, Integer quantidade, BigDecimal precoUnitario) {
    }

    public static CompraResponse from(Compra c) {
        return new CompraResponse(
                c.getId(), c.getClienteNome(), c.getClienteEmail(), c.getTotal(), c.getStatus(),
                c.getTransacaoId(), c.getMensagemBanco(), c.getDataCompra(),
                c.getItens().stream()
                        .map(i -> new ItemResponse(i.getProdutoId(), i.getProdutoNome(), i.getQuantidade(), i.getPrecoUnitario()))
                        .toList());
    }
}
