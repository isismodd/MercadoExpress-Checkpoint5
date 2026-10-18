package br.com.fiap.mercadoexpress.dashboard.service;

import br.com.fiap.mercadoexpress.compra.entity.Compra;
import br.com.fiap.mercadoexpress.email.entity.EmailLog;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class DashboardView {
    private BigDecimal receita;
    private BigDecimal lucro;
    private long aprovadas;
    private long recusadas;
    private long pendentes;

    private List<String> diasLabels;
    private List<Double> diasValores;

    private List<String> topLabels;
    private List<Integer> topQuantidades;

    private List<String> lucroLabels;
    private List<Double> lucroValores;

    private List<String> estoqueLabels;
    private List<Integer> estoqueValores;

    private List<Compra> ultimasCompras;
    private List<EmailLog> emails;
}
