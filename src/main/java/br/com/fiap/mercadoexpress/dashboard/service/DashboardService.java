package br.com.fiap.mercadoexpress.dashboard.service;

import br.com.fiap.mercadoexpress.compra.entity.Compra;
import br.com.fiap.mercadoexpress.compra.entity.ItemCompra;
import br.com.fiap.mercadoexpress.compra.repository.CompraRepository;
import br.com.fiap.mercadoexpress.email.repository.EmailLogRepository;
import br.com.fiap.mercadoexpress.produto.entity.Produto;
import br.com.fiap.mercadoexpress.produto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CompraRepository compraRepository;
    private final ProdutoRepository produtoRepository;
    private final EmailLogRepository emailLogRepository;

    @Transactional(readOnly = true)
    public DashboardView montar() {
        List<Compra> todas = compraRepository.findAll();
        List<Compra> aprovadas = todas.stream().filter(c -> "APROVADA".equals(c.getStatus())).toList();

        BigDecimal receita = BigDecimal.ZERO;
        BigDecimal lucro = BigDecimal.ZERO;
        Map<String, Integer> qtdPorProduto = new HashMap<>();
        Map<String, BigDecimal> lucroPorProduto = new HashMap<>();
        Map<LocalDate, BigDecimal> vendasPorDia = new TreeMap<>();

        for (Compra c : aprovadas) {
            receita = receita.add(c.getTotal());
            vendasPorDia.merge(c.getDataCompra().toLocalDate(), c.getTotal(), BigDecimal::add);
            for (ItemCompra i : c.getItens()) {
                BigDecimal custo = i.getCustoUnitario() == null ? BigDecimal.ZERO : i.getCustoUnitario();
                BigDecimal l = i.getPrecoUnitario().subtract(custo).multiply(BigDecimal.valueOf(i.getQuantidade()));
                lucro = lucro.add(l);
                qtdPorProduto.merge(i.getProdutoNome(), i.getQuantidade(), Integer::sum);
                lucroPorProduto.merge(i.getProdutoNome(), l, BigDecimal::add);
            }
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        List<String> diasLabels = new ArrayList<>();
        List<Double> diasValores = new ArrayList<>();
        vendasPorDia.forEach((dia, valor) -> {
            diasLabels.add(dia.format(fmt));
            diasValores.add(valor.doubleValue());
        });

        List<String> topLabels = new ArrayList<>();
        List<Integer> topQuantidades = new ArrayList<>();
        qtdPorProduto.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> {
                    topLabels.add(e.getKey());
                    topQuantidades.add(e.getValue());
                });

        List<String> lucroLabels = new ArrayList<>();
        List<Double> lucroValores = new ArrayList<>();
        lucroPorProduto.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(8)
                .forEach(e -> {
                    lucroLabels.add(e.getKey());
                    lucroValores.add(e.getValue().doubleValue());
                });

        List<String> estoqueLabels = new ArrayList<>();
        List<Integer> estoqueValores = new ArrayList<>();
        for (Produto p : produtoRepository.findAll()) {
            estoqueLabels.add(p.getNome());
            estoqueValores.add(p.getEstoque() == null ? 0 : p.getEstoque());
        }

        return DashboardView.builder()
                .receita(receita)
                .lucro(lucro)
                .aprovadas(aprovadas.size())
                .recusadas(todas.stream().filter(c -> "RECUSADA".equals(c.getStatus())).count())
                .pendentes(todas.stream().filter(c -> "PENDENTE".equals(c.getStatus())).count())
                .diasLabels(diasLabels).diasValores(diasValores)
                .topLabels(topLabels).topQuantidades(topQuantidades)
                .lucroLabels(lucroLabels).lucroValores(lucroValores)
                .estoqueLabels(estoqueLabels).estoqueValores(estoqueValores)
                .ultimasCompras(compraRepository.findTop10ByOrderByDataCompraDesc())
                .emails(emailLogRepository.findTop10ByOrderByDataEnvioDesc())
                .build();
    }
}
