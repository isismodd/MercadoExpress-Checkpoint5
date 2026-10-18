package br.com.fiap.mercadoexpress.email.service;

import br.com.fiap.mercadoexpress.compra.entity.Compra;
import br.com.fiap.mercadoexpress.email.entity.EmailLog;
import br.com.fiap.mercadoexpress.email.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    @Value("${app.mail.from}")
    private String from;

    /** Envia o aviso de pagamento confirmado e registra o envio (ou a falha) no banco. */
    public void enviarConfirmacaoPagamento(Compra compra) {
        String assunto = "Mercado Express - Pagamento confirmado (compra #" + compra.getId() + ")";
        String itens = compra.getItens().stream()
                .map(i -> "- " + i.getQuantidade() + "x " + i.getProdutoNome() + " (R$ " + i.getPrecoUnitario() + ")")
                .collect(Collectors.joining("\n"));
        String corpo = "Olá, " + compra.getClienteNome() + "!\n\n"
                + "O Banco Tranquilo confirmou o pagamento da sua compra #" + compra.getId() + ".\n\n"
                + itens + "\n\n"
                + "Total: R$ " + compra.getTotal() + "\n"
                + "Transação: " + compra.getTransacaoId() + "\n\n"
                + "Obrigado por comprar no Mercado Express!";

        EmailLog registro = EmailLog.builder()
                .compraId(compra.getId())
                .destinatario(compra.getClienteEmail())
                .assunto(assunto)
                .corpo(corpo)
                .dataEnvio(LocalDateTime.now())
                .status("ENVIADO")
                .build();
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(from);
            msg.setTo(compra.getClienteEmail());
            msg.setSubject(assunto);
            msg.setText(corpo);
            mailSender.send(msg);
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail da compra {}: {}", compra.getId(), e.getMessage());
            registro.setStatus("FALHA");
            String erro = String.valueOf(e.getMessage());
            registro.setErro(erro.length() > 480 ? erro.substring(0, 480) : erro);
        }
        emailLogRepository.save(registro);
    }
}
