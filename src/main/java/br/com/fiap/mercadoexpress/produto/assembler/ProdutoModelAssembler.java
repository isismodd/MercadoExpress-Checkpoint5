package br.com.fiap.mercadoexpress.produto.assembler;

import br.com.fiap.mercadoexpress.produto.controller.ProdutoController;
import br.com.fiap.mercadoexpress.produto.dto.ProdutoResponse;
import br.com.fiap.mercadoexpress.produto.entity.Produto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProdutoModelAssembler implements RepresentationModelAssembler<Produto, EntityModel<ProdutoResponse>> {

    @Override
    public EntityModel<ProdutoResponse> toModel(Produto produto) {
        ProdutoResponse response = new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getTipo(),
                produto.getSetor(),
                produto.getTamanho(),
                produto.getPreco(),
                produto.getCusto(),
                produto.getEstoque()
        );

        return EntityModel.of(response,
                linkTo(methodOn(ProdutoController.class).buscarPorId(produto.getId())).withSelfRel(),
                linkTo(methodOn(ProdutoController.class).listar()).withRel("mercado"),
                linkTo(methodOn(ProdutoController.class).atualizar(produto.getId(), null)).withRel("atualizar"),
                linkTo(methodOn(ProdutoController.class).atualizarParcial(produto.getId(), null)).withRel("atualizar-parcial"),
                linkTo(methodOn(ProdutoController.class).deletar(produto.getId())).withRel("deletar")
        );
    }
}
