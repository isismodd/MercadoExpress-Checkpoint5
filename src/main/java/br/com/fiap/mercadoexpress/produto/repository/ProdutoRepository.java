package br.com.fiap.mercadoexpress.produto.repository;

import br.com.fiap.mercadoexpress.produto.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
