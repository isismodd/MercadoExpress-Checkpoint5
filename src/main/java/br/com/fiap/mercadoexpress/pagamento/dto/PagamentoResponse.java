package br.com.fiap.mercadoexpress.pagamento.dto;

/** Mensagem publicada na fila de resposta de pagamento (status: APROVADO ou RECUSADO). */
public record PagamentoResponse(
        Long compraId,
        String transacaoId,
        String status,
        String mensagem
) {
}
