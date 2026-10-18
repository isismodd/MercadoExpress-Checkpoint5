package br.com.fiap.mercadoexpress.email.repository;

import br.com.fiap.mercadoexpress.email.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    List<EmailLog> findTop10ByOrderByDataEnvioDesc();
    List<EmailLog> findByCompraIdOrderByDataEnvioDesc(Long compraId);
}
