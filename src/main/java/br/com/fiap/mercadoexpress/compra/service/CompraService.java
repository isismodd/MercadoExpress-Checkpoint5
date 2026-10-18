package br.com.fiap.mercadoexpress.compra.service;

import br.com.fiap.mercadoexpress.common.ResourceNotFoundException;
import br.com.fiap.mercadoexpress.compra.dto.CompraRequest;
import br.com.fiap.mercadoexpress.compra.entity.Compra;
import br.com.fiap.mercadoexpress.compra.entity.ItemCompra;
import br.com.fiap.mercadoexpress.compra.repository.CompraRepository;
import br.com.fiap.mercadoexpress.email.entity.EmailLog;
import br.com.fiap.mercadoexpress.email.repository.EmailLogRepository;
import br.com.fiap.mercadoexpress.email.service.EmailService;
import br.com.fiap.mercadoexpress.pagamento.dto.PagamentoRequest;
import br.com.fiap.mercadoexpress.pagamento.dto.PagamentoResponse;
import br.com.fiap.mercadoexpress.pagamento.messaging.PagamentoPublisher;
import br.com.fiap.mercadoexpress.produto.entity.Produto;
import br.com.fiap.mercadoexpress.produto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProdutoRepository produtoRepository;
    private final EmailLogRepository emailLogRepository;
    private final PagamentoPublisher publisher;
    private final EmailService emailService;

    /** Registra a compra como PENDENTE e, após o commit, publica a requisição de pagamento no RabbitMQ. */
    @Transactional
    public Compra criar(CompraRequest request) {
        Compra compra = Compra.builder()
                .clienteNome(request.clienteNome())
                .clienteEmail(request.clienteEmail())
                .status("PENDENTE")
                .dataCompra(LocalDateTime.now())
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (CompraRequest.ItemRequest item : request.itens()) {
            Produto produto = produtoRepository.findById(item.produtoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + item.produtoId()));
            int estoque = produto.getEstoque() == null ? 0 : produto.getEstoque();
            if (estoque < item.quantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para " + produto.getNome()
                        + " (disponível: " + estoque + ")");
            }
            compra.getItens().add(ItemCompra.builder()
                    .compra(compra)
                    .produtoId(produto.getId())
                    .produtoNome(produto.getNome())
                    .quantidade(item.quantidade())
                    .precoUnitario(produto.getPreco())
                    .custoUnitario(produto.getCusto())
                    .build());
            total = total.add(produto.getPreco().multiply(BigDecimal.valueOf(item.quantidade())));
        }
        compra.setTotal(total);
        Compra salva = compraRepository.save(compra);

        PagamentoRequest mensagem = new PagamentoRequest(
                salva.getId(), salva.getClienteNome(), salva.getClienteEmail(), salva.getTotal(), request.numeroCartao());
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publisher.solicitarPagamento(mensagem);
            }
        });
        return salva;
    }

    /** Trata a resposta do Banco Tranquilo: atualiza a compra, baixa o estoque e envia o e-mail de confirmação. */
    @Transactional
    public void processarResposta(PagamentoResponse resposta) {
        Compra compra = compraRepository.findById(resposta.compraId()).orElse(null);
        if (compra == null) {
            log.warn("Compra {} não encontrada para a resposta de pagamento", resposta.compraId());
            return;
        }
        if (!"PENDENTE".equals(compra.getStatus())) {
            return; // mensagem duplicada
        }
        compra.setTransacaoId(resposta.transacaoId());
        compra.setMensagemBanco(resposta.mensagem());

        if ("APROVADO".equals(resposta.status())) {
            compra.setStatus("APROVADA");
            for (ItemCompra item : compra.getItens()) {
                produtoRepository.findById(item.getProdutoId()).ifPresent(p -> {
                    int atual = p.getEstoque() == null ? 0 : p.getEstoque();
                    p.setEstoque(Math.max(0, atual - item.getQuantidade()));
                });
            }
            compraRepository.save(compra);
            emailService.enviarConfirmacaoPagamento(compra);
        } else {
            compra.setStatus("RECUSADA");
            compraRepository.save(compra);
        }
    }

    @Transactional(readOnly = true)
    public Compra buscar(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada com id " + id));
    }

    @Transactional(readOnly = true)
    public List<Compra> listar() {
        return compraRepository.findAll();
    }

    public List<EmailLog> emailsDaCompra(Long compraId) {
        return emailLogRepository.findByCompraIdOrderByDataEnvioDesc(compraId);
    }
}
