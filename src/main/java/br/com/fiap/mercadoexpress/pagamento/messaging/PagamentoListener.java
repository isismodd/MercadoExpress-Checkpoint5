package br.com.fiap.mercadoexpress.pagamento.messaging;

import br.com.fiap.mercadoexpress.compra.service.CompraService;
import br.com.fiap.mercadoexpress.pagamento.client.BancoTranquiloClient;
import br.com.fiap.mercadoexpress.pagamento.config.RabbitConfig;
import br.com.fiap.mercadoexpress.pagamento.dto.BancoPagamentoRequest;
import br.com.fiap.mercadoexpress.pagamento.dto.BancoPagamentoResponse;
import br.com.fiap.mercadoexpress.pagamento.dto.PagamentoRequest;
import br.com.fiap.mercadoexpress.pagamento.dto.PagamentoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagamentoListener {

    private final BancoTranquiloClient bancoClient;
    private final PagamentoPublisher publisher;
    private final CompraService compraService;

    /** Lado "Banco": consome a requisição, chama a API do Banco Tranquilo e publica a resposta. */
    @RabbitListener(queues = RabbitConfig.QUEUE_REQUISICAO)
    public void aoReceberRequisicao(PagamentoRequest request) {
        log.info("[RabbitMQ] Requisição recebida para a compra {}", request.compraId());
        PagamentoResponse resposta;
        try {
            BancoPagamentoResponse banco = bancoClient.cobrar(new BancoPagamentoRequest(
                    request.compraId(), request.clienteNome(), request.numeroCartao(), request.valor()));
            resposta = new PagamentoResponse(request.compraId(), banco.transacaoId(), banco.status(), banco.mensagem());
        } catch (Exception e) {
            log.error("Banco Tranquilo indisponível: {}", e.getMessage());
            resposta = new PagamentoResponse(request.compraId(), null, "RECUSADO",
                    "Banco Tranquilo indisponível");
        }
        publisher.responderPagamento(resposta);
    }

    /** Lado "Mercado": consome a resposta, atualiza a compra, baixa o estoque e envia o e-mail. */
    @RabbitListener(queues = RabbitConfig.QUEUE_RESPOSTA)
    public void aoReceberResposta(PagamentoResponse resposta) {
        log.info("[RabbitMQ] Resposta recebida para a compra {}: {}", resposta.compraId(), resposta.status());
        compraService.processarResposta(resposta);
    }
}
