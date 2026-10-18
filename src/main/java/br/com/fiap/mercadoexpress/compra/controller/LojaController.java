package br.com.fiap.mercadoexpress.compra.controller;

import br.com.fiap.mercadoexpress.common.ResourceNotFoundException;
import br.com.fiap.mercadoexpress.compra.dto.CompraRequest;
import br.com.fiap.mercadoexpress.compra.entity.Compra;
import br.com.fiap.mercadoexpress.compra.service.CompraService;
import br.com.fiap.mercadoexpress.produto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Tela de carrinho: escolhe produtos, finaliza a compra e acompanha o pagamento. */
@Controller
@RequestMapping("/loja")
@RequiredArgsConstructor
public class LojaController {

    private final ProdutoService produtoService;
    private final CompraService compraService;

    @GetMapping
    public String loja(Model model) {
        model.addAttribute("produtos", produtoService.findAll());
        return "loja/index";
    }

    @PostMapping("/comprar")
    public String comprar(@RequestParam String clienteNome,
                          @RequestParam String clienteEmail,
                          @RequestParam String numeroCartao,
                          @RequestParam Map<String, String> params,
                          RedirectAttributes redirect) {
        List<CompraRequest.ItemRequest> itens = new ArrayList<>();
        params.forEach((chave, valor) -> {
            if (chave.startsWith("qtd_") && valor != null && !valor.isBlank()) {
                try {
                    int quantidade = Integer.parseInt(valor.trim());
                    if (quantidade > 0) {
                        itens.add(new CompraRequest.ItemRequest(Long.valueOf(chave.substring(4)), quantidade));
                    }
                } catch (NumberFormatException ignorado) {
                    // ignora valores inválidos
                }
            }
        });
        if (itens.isEmpty()) {
            redirect.addFlashAttribute("erro", "Selecione a quantidade de pelo menos um produto.");
            return "redirect:/loja";
        }
        try {
            Compra compra = compraService.criar(new CompraRequest(clienteNome, clienteEmail, numeroCartao, itens));
            return "redirect:/loja/compra/" + compra.getId();
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            return "redirect:/loja";
        }
    }

    @GetMapping("/compra/{id}")
    public String compra(@PathVariable Long id, Model model) {
        model.addAttribute("compra", compraService.buscar(id));
        model.addAttribute("emails", compraService.emailsDaCompra(id));
        return "loja/compra";
    }
}
