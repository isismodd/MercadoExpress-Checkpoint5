package br.com.fiap.mercadoexpress.pagamento.dto;

/** Resposta da API do Banco Tranquilo. */
public record BancoPagamentoResponse(
        String transacaoId,
        String status,
        String mensagem
) {
}
