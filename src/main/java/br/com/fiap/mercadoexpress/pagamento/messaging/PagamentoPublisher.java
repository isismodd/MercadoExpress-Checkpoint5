package br.com.fiap.mercadoexpress.pagamento.messaging;

import br.com.fiap.mercadoexpress.pagamento.config.RabbitConfig;
import br.com.fiap.mercadoexpress.pagamento.dto.PagamentoRequest;
import br.com.fiap.mercadoexpress.pagamento.dto.PagamentoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagamentoPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void solicitarPagamento(PagamentoRequest request) {
        log.info("[RabbitMQ] Publicando requisição de pagamento da compra {}", request.compraId());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.RK_REQUISICAO, request);
    }

    public void responderPagamento(PagamentoResponse response) {
        log.info("[RabbitMQ] Publicando resposta de pagamento da compra {}: {}", response.compraId(), response.status());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.RK_RESPOSTA, response);
    }
}
