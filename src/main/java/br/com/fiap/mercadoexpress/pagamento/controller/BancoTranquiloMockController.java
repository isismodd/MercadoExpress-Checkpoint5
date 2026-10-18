package br.com.fiap.mercadoexpress.pagamento.controller;

import br.com.fiap.mercadoexpress.pagamento.dto.BancoPagamentoRequest;
import br.com.fiap.mercadoexpress.pagamento.dto.BancoPagamentoResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Simulador da API do Banco Tranquilo (para testes e demonstração).
 * Regras: cartão terminado em 0000 é recusado; valor acima de R$ 5.000,00 é recusado; demais são aprovados.
 * Para usar a API real, basta trocar banco.tranquilo.url / banco.tranquilo.path no application.yml.
 */
@RestController
@RequestMapping("/banco-tranquilo/api")
public class BancoTranquiloMockController {

    private static final BigDecimal LIMITE = new BigDecimal("5000.00");

    @PostMapping("/pagamentos")
    public BancoPagamentoResponse pagar(@RequestBody BancoPagamentoRequest request) {
        try {
            Thread.sleep(1500); // simula a latência do banco (útil para mostrar o status PENDENTE na demo)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String transacao = "BT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String cartao = request.numeroCartao() == null ? "" : request.numeroCartao().replaceAll("\\D", "");

        if (cartao.isEmpty() || cartao.endsWith("0000")) {
            return new BancoPagamentoResponse(transacao, "RECUSADO", "Cartão recusado pelo Banco Tranquilo");
        }
        if (request.valor() != null && request.valor().compareTo(LIMITE) > 0) {
            return new BancoPagamentoResponse(transacao, "RECUSADO", "Limite excedido");
        }
        return new BancoPagamentoResponse(transacao, "APROVADO", "Pagamento aprovado");
    }
}
