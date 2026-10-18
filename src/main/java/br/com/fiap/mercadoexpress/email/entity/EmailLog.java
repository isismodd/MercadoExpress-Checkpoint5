package br.com.fiap.mercadoexpress.email.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/** Controle de envio de e-mails aos clientes. */
@Entity
@Table(name = "TDS_Email_Log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_log_seq")
    @SequenceGenerator(name = "email_log_seq", sequenceName = "EMAIL_LOG_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMPRA_ID")
    private Long compraId;

    @Column(name = "DESTINATARIO", nullable = false, length = 150)
    private String destinatario;

    @Column(name = "ASSUNTO", nullable = false, length = 200)
    private String assunto;

    @Column(name = "CORPO", length = 2000)
    private String corpo;

    /** ENVIADO ou FALHA */
    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    @Column(name = "ERRO", length = 500)
    private String erro;

    @Column(name = "DATA_ENVIO", nullable = false)
    private LocalDateTime dataEnvio;
}
