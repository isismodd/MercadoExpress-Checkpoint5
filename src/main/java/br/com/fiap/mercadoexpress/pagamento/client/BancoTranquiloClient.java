package br.com.fiap.mercadoexpress.pagamento.client;

import br.com.fiap.mercadoexpress.pagamento.dto.BancoPagamentoRequest;
import br.com.fiap.mercadoexpress.pagamento.dto.BancoPagamentoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Conexão com a API de Integração de Pagamentos do Banco Tranquilo (URL configurável no application.yml). */
@Component
public class BancoTranquiloClient {

    private final RestClient restClient;
    private final String path;

    public BancoTranquiloClient(@Value("${banco.tranquilo.url}") String baseUrl,
                                @Value("${banco.tranquilo.path}") String path) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.path = path;
    }

    public BancoPagamentoResponse cobrar(BancoPagamentoRequest request) {
        return restClient.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(BancoPagamentoResponse.class);
    }
}
