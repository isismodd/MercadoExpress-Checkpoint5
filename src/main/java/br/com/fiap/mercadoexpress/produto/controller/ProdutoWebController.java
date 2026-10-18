package br.com.fiap.mercadoexpress.produto.controller;

import br.com.fiap.mercadoexpress.produto.dto.ProdutoRequest;
import br.com.fiap.mercadoexpress.produto.entity.Produto;
import br.com.fiap.mercadoexpress.produto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/produtos")
@RequiredArgsConstructor
public class ProdutoWebController {

    private final ProdutoService produtoService;

    // LISTAR todos os produtos
    @GetMapping
    public String listarProdutos(@RequestParam(required = false) String busca, Model model) {
        boolean semBusca = busca == null || busca.isBlank();
        model.addAttribute("produtos", semBusca ? produtoService.findAll() : produtoService.search(busca));
        model.addAttribute("busca", busca);
        return "produtos/lista";
    }

    // FORMULÁRIO para criar
    @GetMapping("/novo")
    public String mostrarFormularioCriacao(Model model) {
        // Criar um ProdutoRequest vazio usando o Record
        model.addAttribute("produto", new ProdutoRequest("", "", "", "", null, null, 0));
        return "produtos/cadastro";
    }

    // CREATE - salvar novo produto
    @PostMapping
    public String criarProduto(@ModelAttribute ProdutoRequest request,
                               RedirectAttributes redirectAttributes) {
        produtoService.create(request);
        redirectAttributes.addFlashAttribute("mensagem", "✅ Produto criado com sucesso!");
        return "redirect:/web/produtos";
    }

    // READ - detalhes de um produto
    @GetMapping("/{id}")
    public String detalharProduto(@PathVariable Long id, Model model) {
        Produto produto = produtoService.findById(id);
        model.addAttribute("produto", produto);
        return "produtos/detalhes";
    }

    // FORMULÁRIO para editar
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Produto produto = produtoService.findById(id);

        // Converter Produto para ProdutoRequest (Record)
        ProdutoRequest request = new ProdutoRequest(
                produto.getNome(),
                produto.getTipo(),
                produto.getSetor(),
                produto.getTamanho(),
                produto.getPreco(),
                produto.getCusto(),
                produto.getEstoque()
        );

        model.addAttribute("produto", request);
        model.addAttribute("id", id);
        return "produtos/editar";
    }

    // UPDATE - atualizar produto
    @PostMapping("/{id}")
    public String atualizarProduto(@PathVariable Long id,
                                   @ModelAttribute ProdutoRequest request,
                                   RedirectAttributes redirectAttributes) {
        produtoService.update(id, request);
        redirectAttributes.addFlashAttribute("mensagem", "✅ Produto atualizado com sucesso!");
        return "redirect:/web/produtos";
    }

    // DELETE - remover produto
    @PostMapping("/{id}/delete")
    public String deletarProduto(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        produtoService.delete(id);
        redirectAttributes.addFlashAttribute("mensagem", "✅ Produto deletado com sucesso!");
        return "redirect:/web/produtos";
    }
}