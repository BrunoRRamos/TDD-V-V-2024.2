package junit5Tests;

import Anotations.UnitTest;
import core.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ShowTestJUnit5 {
    private Show show;

    @BeforeEach
    public void setUp() throws Exception {
        Date dataDoShow = new Date();
        Double totalDespesaInfra = 50000.00;
        boolean showEmDataEspecial = true;
        int totalIngressos = 100;
        double valorIngresso = 249.99;
        Artista artista = new Artista("Sorriso Ronaldo", 30000);

        this.show = new Show(dataDoShow, totalDespesaInfra, showEmDataEspecial, totalIngressos, valorIngresso, artista);
    }

    @AfterEach
    public void tearDown() throws Exception {
        this.show.getLotes().get(this.show.getLotes().size()-1).resetCounter();
        this.show = null;
    }

    @UnitTest
    @DisplayName("Testa o retorno da quantidade de ingressos VIP")
    public void TestaQuantidadeDeIngressosVip() {
        Lote lote = show.getLoteById(1);
        List<Ingresso> ingressosVip = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.VIP)
                .collect(Collectors.toList());
        int totalIngressosVip = ingressosVip.size();
        int totalIngressos = show.getTotalIngressos();
        int minVip = (int) Math.ceil(totalIngressos * 0.20);
        int maxVip = (int) Math.floor(totalIngressos * 0.30);

        assertAll(() -> {
            assertTrue(totalIngressosVip >= minVip);
            assertTrue(totalIngressosVip <= maxVip);
        });
    }

    @UnitTest
    @DisplayName("Testa o retorno da quantidade de ingressos MEIA_ENTRADA")
    public void TestaQuantidadeDeIngressosMeia() {
        Lote lote = show.getLoteById(1);
        List<Ingresso> ingressosMeia = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.MEIA_ENTRADA)
                .collect(Collectors.toList());
        int totalIngressosMeia = ingressosMeia.size();
        int totalIngressos = show.getTotalIngressos();
        int qntMeiaExpected = (int) Math.ceil(totalIngressos * 0.10);
        assertEquals(totalIngressosMeia, qntMeiaExpected);
    }

    @UnitTest
    @DisplayName("Testa o retorno da quantidade de ingressos NORMAL")
    public void TestaQuantidadeDeIngressosNormal() {
        Lote lote = show.getLoteById(1);
        List<Ingresso> ingressosNormal = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.NORMAL)
                .collect(Collectors.toList());
        int totalIngressosNormal = ingressosNormal.size();
        int totalIngressos = show.getTotalIngressos();
        int qntVipExpected = (int) Math.floor(totalIngressos * 0.25);
        int qntMeiaExpected = (int) Math.ceil(totalIngressos * 0.10);
        int qntNormalExpected = totalIngressos - qntVipExpected - qntMeiaExpected;
        assertEquals(totalIngressosNormal, qntNormalExpected);
    }

    @UnitTest
    @DisplayName("Testa o retorno do valor de ingressos VIP")
    public void TestaValorDoIngressoVip() {
        Lote lote = show.getLoteById(1);
        Ingresso ingressoVip = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.VIP)
                .findFirst()
                .orElse(null);

        assertAll(() -> {
            assertNotNull(ingressoVip);
            assertEquals(ingressoVip.getValorIngresso(), show.getLoteById(1).getValorIngresso() * 2, 0.0);
        });
    }

    @UnitTest
    @DisplayName("Testa o retorno do valor de ingressos MEIA_ENTRADA")
    public void TestaValorDoIngressoMeia() {
        Lote lote = show.getLoteById(1);
        Ingresso ingressoMeia = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.MEIA_ENTRADA)
                .findFirst()
                .orElse(null);

        assertAll(() -> {
            assertNotNull(ingressoMeia);
            assertEquals(ingressoMeia.getValorIngresso(), show.getLoteById(1).getValorIngresso() / 2, 0.0);
        });
    }

    @UnitTest
    @DisplayName("Testa o retorno do valor de ingressos NORMAL")
    public void TestaValorDoIngressoNormal() {
        Lote lote = show.getLoteById(1);
        Ingresso ingressoNormal = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.NORMAL)
                .findFirst()
                .orElse(null);

        assertAll(() -> {
            assertNotNull(ingressoNormal);
            assertEquals(ingressoNormal.getValorIngresso(), show.getLoteById(1).getValorIngresso(), 0.0);
        });
    }

    @UnitTest
    @DisplayName("Testa o comprar ingressos VIP")
    public void TestaCompraDeIngressoVip() {
        Ingresso ingressoComprado = this.show.comprarIngresso(1, TipoIngresso.VIP);
        assertAll(() -> {
            assertNotNull(ingressoComprado);
            assertSame(ingressoComprado.getStatus(), StatusIngresso.VENDIDO);
        });
    }

    @UnitTest
    @DisplayName("Testa o comprar ingressos MEIA_ENTRADA")
    public void TestaCompraDeIngressoMeia() {
        Ingresso ingressoComprado = this.show.comprarIngresso(1, TipoIngresso.MEIA_ENTRADA);
        assertAll(() -> {
            assertNotNull(ingressoComprado);
            assertSame(ingressoComprado.getStatus(), StatusIngresso.VENDIDO);
        });
    }

    @UnitTest
    @DisplayName("Testa o comprar ingressos NORMAL")
    public void TestaCompraDeIngressoNormal() {
        Ingresso ingressoComprado = this.show.comprarIngresso(1, TipoIngresso.NORMAL);
        assertAll(() -> {
            assertNotNull(ingressoComprado);
            assertSame(ingressoComprado.getStatus(), StatusIngresso.VENDIDO);
        });
    }

    @UnitTest
    @DisplayName("Testa criar novo lote")
    public void TestaCriacaoDeNovoLote() {
        int numeroDeIngressos = 100;
        double desconto = 0.10;
        double valoringresso = 300.0;
        Lote lote = this.show.criarNovoLote(numeroDeIngressos, desconto, valoringresso);

        assertEquals(lote.getTotalIngressos(), numeroDeIngressos);
    }

    @UnitTest
    @DisplayName("Testa criar novo lote sem desconto")
    public void TestaCriarLoteSemDesconto() {
        int numeroDeIngressos = 100;
        double desconto = 0;
        double valoringresso = 300.0;
        Lote lote = this.show.criarNovoLote(numeroDeIngressos, desconto, valoringresso);

        assertAll(() -> {
            assertEquals(lote.getTotalIngressos(), numeroDeIngressos);
            assertEquals(1.0, lote.getDesconto(), 0.0);
        });
    }

    @UnitTest
    @DisplayName("Testa criar novo lote com desconto maio que 0.25")
    public void TestaCriarLoteComDescontoMaiorQue25() {
        int numeroDeIngressos = 100;
        double desconto = 0.26;
        double valoringresso = 300.0;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Lote lote = this.show.criarNovoLote(numeroDeIngressos, desconto, valoringresso);
        });

        assertEquals("Desconto deve ser menor ou igual a 25%", exception.getMessage());
    }

    @UnitTest
    @DisplayName("Testa criar novo lote com desconto negativo")
    public void TestaCriarLoteComDescontoNegativo() {
        int numeroDeIngressos = 100;
        double desconto = -0.10;
        double valoringresso = 300.0;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Lote lote = this.show.criarNovoLote(numeroDeIngressos, desconto, valoringresso);
        });

        assertEquals("Desconto deve ser maior que zero", exception.getMessage());
    }

    @UnitTest
    @DisplayName("Testa comprar ingresso VIP com desconto")
    public void TestaComprarIngresssoVipComDesconto() {
        int totalIngressosAntesCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.VIP);
        double valor = show.comprarIngressoComDesconto(1, TipoIngresso.VIP);
        int totalIngressosDepoisCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.VIP);

        assertAll(() -> {
            assertEquals(totalIngressosDepoisCompra, totalIngressosAntesCompra - 1);
            assertEquals((499.98 * 0.25), valor, 0.0);
        });
    }

    @UnitTest
    @DisplayName("Testa comprar ingresso NORMAL com desconto")
    public void TestaComprarIngresssoNormalComDesconto() {
        int totalIngressosAntesCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.NORMAL);
        double valor = show.comprarIngressoComDesconto(1, TipoIngresso.NORMAL);
        int totalIngressosDepoisCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.NORMAL);

        assertAll(() -> {
            assertEquals(totalIngressosDepoisCompra, totalIngressosAntesCompra - 1);
            assertEquals((249.99 * 0.25), valor, 0.0);
        });
    }

    @UnitTest
    @DisplayName("Testa comprar ingresso MEIA_ENTRADA com desconto")
    public void TestaComprarIngresssoMeiaComDesconto() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            show.comprarIngressoComDesconto(1, TipoIngresso.MEIA_ENTRADA);
        });
        assertEquals("Desconto não aplicavel a ingresso do tipo: MEIA_ENTRADA", exception.getMessage());
    }

    @UnitTest
    @DisplayName("Testa gerar relatorio do show")
    public void TestaGerarRelatorioShow() {
        String relatorioExpected = "Relatório de Vendas:\n" +
                "Vendas VIP: 1\n" +
                "Vendas Meia Entrada: 0\n" +
                "Vendas Normais: 0\n" +
                "Receita liquida: -37125.015\n" +
                "Status financeiro: PREJUIZO";
        show.comprarIngressoComDesconto(1, TipoIngresso.VIP);
        String relatorio = show.gerarRelatorio();
        assertEquals(relatorioExpected, relatorio);
    }

    @UnitTest
    @DisplayName("Testa comprar ingresso sem ingressos diponiveis")
    public void TestaComprarIngresssoSemIngressosDisponiveis() {
        Date dataDoShow = new Date();
        Double totalDespesaInfra = 50000.00;
        boolean showEmDataEspecial = true;
        int totalIngressos = 1;
        double valorIngresso = 249.99;
        Artista artista = new Artista("Sorriso Ronaldo", 30000);

        Show novoShow = new Show(dataDoShow, totalDespesaInfra, showEmDataEspecial, totalIngressos, valorIngresso, artista);

        assertThrows(RuntimeException.class, () -> {
            novoShow.comprarIngressoComDesconto(1, TipoIngresso.VIP);
            novoShow.comprarIngressoComDesconto(1, TipoIngresso.NORMAL);
        });
    }
}
