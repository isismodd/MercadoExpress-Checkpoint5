package br.com.fiap.mercadoexpress.pagamento.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exchanges, queues e bindings do fluxo de pagamento.
 *
 * mercado.pagamentos.exchange --pagamento.requisicao--> pagamento.requisicao.queue  (Mercado -> Banco)
 * mercado.pagamentos.exchange --pagamento.resposta----> pagamento.resposta.queue    (Banco -> Mercado)
 * Mensagens que falham vão para mercado.pagamentos.dlx -> pagamento.dlq
 */
@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "mercado.pagamentos.exchange";
    public static final String DLX = "mercado.pagamentos.dlx";

    public static final String QUEUE_REQUISICAO = "pagamento.requisicao.queue";
    public static final String QUEUE_RESPOSTA = "pagamento.resposta.queue";
    public static final String QUEUE_DLQ = "pagamento.dlq";

    public static final String RK_REQUISICAO = "pagamento.requisicao";
    public static final String RK_RESPOSTA = "pagamento.resposta";
    public static final String RK_DLQ = "pagamento.dlq";

    @Bean
    public DirectExchange pagamentosExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange pagamentosDlx() {
        return new DirectExchange(DLX, true, false);
    }

    @Bean
    public Queue filaRequisicao() {
        return QueueBuilder.durable(QUEUE_REQUISICAO)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", RK_DLQ)
                .build();
    }

    @Bean
    public Queue filaResposta() {
        return QueueBuilder.durable(QUEUE_RESPOSTA)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", RK_DLQ)
                .build();
    }

    @Bean
    public Queue filaDlq() {
        return QueueBuilder.durable(QUEUE_DLQ).build();
    }

    @Bean
    public Binding bindingRequisicao() {
        return BindingBuilder.bind(filaRequisicao()).to(pagamentosExchange()).with(RK_REQUISICAO);
    }

    @Bean
    public Binding bindingResposta() {
        return BindingBuilder.bind(filaResposta()).to(pagamentosExchange()).with(RK_RESPOSTA);
    }

    @Bean
    public Binding bindingDlq() {
        return BindingBuilder.bind(filaDlq()).to(pagamentosDlx()).with(RK_DLQ);
    }

    /** Mensagens trafegam em JSON (visíveis no dashboard do RabbitMQ). */
    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
