package br.com.fiap.mercadoexpress.compra.controller;

import br.com.fiap.mercadoexpress.compra.dto.CompraRequest;
import br.com.fiap.mercadoexpress.compra.dto.CompraResponse;
import br.com.fiap.mercadoexpress.compra.service.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** API de compras (para Postman/Insomnia). Autenticação: HTTP Basic. */
@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraApiController {

    private final CompraService service;

    /** Cria a compra (PENDENTE) e dispara o fluxo de pagamento assíncrono. Retorna 202 Accepted. */
    @PostMapping
    public ResponseEntity<CompraResponse> criar(@Valid @RequestBody CompraRequest request) {
        return ResponseEntity.accepted().body(CompraResponse.from(service.criar(request)));
    }

    @GetMapping("/{id}")
    public CompraResponse buscar(@PathVariable Long id) {
        return CompraResponse.from(service.buscar(id));
    }

    @GetMapping
    public List<CompraResponse> listar() {
        return service.listar().stream().map(CompraResponse::from).toList();
    }
}
