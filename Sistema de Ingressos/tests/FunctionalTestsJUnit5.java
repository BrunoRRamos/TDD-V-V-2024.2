import Anotations.FunctionalTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FunctionalTestsJUnit5 {
    private Show show;

    @BeforeEach
    public void setUp() throws Exception {
        Date dataDoShow = new Date();
        Double totalDespesaInfra = 5000.00;
        boolean showEmDataEspecial = true;
        int totalIngressos = 100;
        double valorIngresso = 200.00;
        Artista artista = new Artista("Blinding Azeitona", 3000);

        this.show = new Show(dataDoShow, totalDespesaInfra, showEmDataEspecial, totalIngressos, valorIngresso, artista);
    }

    @AfterEach
    public void tearDown() throws Exception {
        this.show.getLotes().get(this.show.getLotes().size()-1).resetCounter();
        this.show = null;
    }

    private void compraTodosOsIngressosDeUmTipo(TipoIngresso tipoIngresso) {
        Lote lote = show.getLoteById(1);
        List<Ingresso> ingressos = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == tipoIngresso &&
                        e.getStatus() == StatusIngresso.DISPONIVEL)
                .toList();

        ingressos.forEach(ingresso -> ingresso.setStatus(StatusIngresso.VENDIDO));
    }

    @FunctionalTest
    public void TestaQuantidadeDeIngressosVip() {
        Lote lote = show.getLoteById(1);
        List<Ingresso> ingressosVip = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.VIP)
                .toList().stream().toList();
        int totalIngressosVip = ingressosVip.size();
        int totalIngressos = show.getTotalIngressos();
        int minVip = (int) Math.ceil(totalIngressos * 0.20);
        int maxVip = (int) Math.floor(totalIngressos * 0.30);

        assertAll(() -> {
            assertTrue(totalIngressosVip >= minVip);
            assertTrue(totalIngressosVip <= maxVip);
        });
    }

    @FunctionalTest
    public void TestaQuantidadeDeIngressosMeia() {
        Lote lote = show.getLoteById(1);
        List<Ingresso> ingressosMeia = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.MEIA_ENTRADA)
                .toList().stream().toList();
        int totalIngressosMeia = ingressosMeia.size();
        int totalIngressos = show.getTotalIngressos();
        int qntMeiaExpected = (int) Math.ceil(totalIngressos * 0.10);
        assertEquals(totalIngressosMeia, qntMeiaExpected);
    }

    @FunctionalTest
    public void TestaQuantidadeDeIngressosNormal() {
        Lote lote = show.getLoteById(1);
        List<Ingresso> ingressosNormal = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.NORMAL)
                .toList().stream().toList();
        int totalIngressosNormal = ingressosNormal.size();
        int totalIngressos = show.getTotalIngressos();
        int qntVipExpected = (int) Math.floor(totalIngressos * 0.25);
        int qntMeiaExpected = (int) Math.ceil(totalIngressos * 0.10);
        int qntNormalExpected = totalIngressos - qntVipExpected - qntMeiaExpected;
        assertEquals(totalIngressosNormal, qntNormalExpected);
    }

    @FunctionalTest
    public void TestaValorDoIngressoVip() {
        Lote lote = show.getLoteById(1);
        Ingresso ingressoVip = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.VIP &&
                        e.getStatus() == StatusIngresso.DISPONIVEL)
                .findFirst()
                .orElse(null);

        assertAll(() -> {
            assertNotNull(ingressoVip);
            assertEquals(ingressoVip.getValorIngresso(), show.getLoteById(1).getValorIngresso() * 2, 0.0);
        });
    }

    @FunctionalTest
    public void TestaValorDoIngressoMeia() {
        Lote lote = show.getLoteById(1);
        Ingresso ingressoMeia = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.MEIA_ENTRADA &&
                        e.getStatus() == StatusIngresso.DISPONIVEL)
                .findFirst()
                .orElse(null);

        assertAll(() -> {
            assertNotNull(ingressoMeia);
            assertEquals(ingressoMeia.getValorIngresso(), show.getLoteById(1).getValorIngresso() / 2, 0.0);
        });
    }

    @FunctionalTest
    public void TestaValorDoIngressoNormal() {
        Lote lote = show.getLoteById(1);
        Ingresso ingressoNormal = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.NORMAL &&
                        e.getStatus() == StatusIngresso.DISPONIVEL)
                .findFirst()
                .orElse(null);

        assertAll(() -> {
            assertNotNull(ingressoNormal);
            assertEquals(ingressoNormal.getValorIngresso(), show.getLoteById(1).getValorIngresso(), 0.0);
        });
    }

    @FunctionalTest
    public void TestaCriacaoDeNovoLote() {
        int numeroDeIngressos = 100;
        double desconto = 0.10;
        double valoringresso = 300.0;
        Lote lote = this.show.criarNovoLote(numeroDeIngressos, desconto, valoringresso);

        assertEquals(lote.getTotalIngressos(), numeroDeIngressos);
    }

    @FunctionalTest
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

    @FunctionalTest
    public void TestaCriarLoteComDescontoIgualA25() {
        int numeroDeIngressos = 100;
        double desconto = 0.25;
        double valoringresso = 300.0;
        Lote lote = this.show.criarNovoLote(numeroDeIngressos, desconto, valoringresso);

        assertEquals(lote.getTotalIngressos(), numeroDeIngressos);
    }

    @FunctionalTest
    public void TestaCriarLoteComDescontoMaiorQue25() {
        int numeroDeIngressos = 100;
        double desconto = 0.26;
        double valoringresso = 300.0;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Lote lote = this.show.criarNovoLote(numeroDeIngressos, desconto, valoringresso);
        });

        assertEquals("Desconto deve ser menor ou igual a 25%", exception.getMessage());
    }

    @FunctionalTest
    public void TestaCriarLoteComDescontoNegativo() {
        int numeroDeIngressos = 100;
        double desconto = -0.10;
        double valoringresso = 300.0;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Lote lote = this.show.criarNovoLote(numeroDeIngressos, desconto, valoringresso);
        });

        assertEquals("Desconto deve ser maior que zero", exception.getMessage());
    }

    @FunctionalTest
    public void TestaComprarIngresssoVipComDesconto() {
        int totalIngressosAntesCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.VIP);
        double valor = show.comprarIngressoComDesconto(1, TipoIngresso.VIP);
        int totalIngressosDepoisCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.VIP);

        assertAll(() -> {
            assertTrue(totalIngressosDepoisCompra == totalIngressosAntesCompra - 1);
            assertEquals((400.00 * 0.25), valor, 0.0);
        });
    }

    @FunctionalTest
    public void TestaComprarIngresssoNormalComDesconto() {
        int totalIngressosAntesCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.NORMAL);
        double valor = show.comprarIngressoComDesconto(1, TipoIngresso.NORMAL);
        int totalIngressosDepoisCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.NORMAL);
        assertAll(() -> {
            assertTrue(totalIngressosDepoisCompra == totalIngressosAntesCompra - 1);
            assertEquals((200.00 * 0.25), valor, 0.0);
        });
    }

    @FunctionalTest
    public void TestaComprarIngresssoMeialComDesconto() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            show.comprarIngressoComDesconto(1, TipoIngresso.MEIA_ENTRADA);
        });
        assertEquals("Desconto não aplicavel a ingresso do tipo: MEIA_ENTRADA", exception.getMessage());
    }

    @FunctionalTest
    public void TestaComprarIngresssoVipSemDesconto() {
        int totalIngressosAntesCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.VIP);
        show.comprarIngresso(1, TipoIngresso.VIP);
        int totalIngressosDepoisCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.VIP);
        assertEquals(totalIngressosDepoisCompra, totalIngressosAntesCompra - 1);
    }

    @FunctionalTest
    public void TestaComprarIngresssoNormalSemDesconto() {
        int totalIngressosAntesCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.NORMAL);
        show.comprarIngresso(1, TipoIngresso.NORMAL);
        int totalIngressosDepoisCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.NORMAL);
        assertEquals(totalIngressosDepoisCompra, totalIngressosAntesCompra - 1);
    }

    @FunctionalTest
    public void TestaComprarIngresssoMeialSemDesconto() {
        int totalIngressosAntesCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.MEIA_ENTRADA);
        show.comprarIngresso(1, TipoIngresso.MEIA_ENTRADA);
        int totalIngressosDepoisCompra = show.contaTotalIngressosDisponiveis(1, TipoIngresso.MEIA_ENTRADA);
        assertEquals(totalIngressosDepoisCompra, totalIngressosAntesCompra - 1);
    }

    @FunctionalTest
    public void TestaGerarRelatorioShowComPrejuizo() {
        String relatorioExpected = "Relatório de Vendas:\n" +
                "Vendas VIP: 1\n" +
                "Vendas Meia Entrada: 0\n" +
                "Vendas Normais: 0\n" +
                "Receita liquida: -3450.0\n" +
                "Status financeiro: PREJUIZO";
        show.comprarIngressoComDesconto(1, TipoIngresso.VIP);
        String relatorio = show.gerarRelatorio();
        assertEquals(relatorioExpected, relatorio);
    }

    @FunctionalTest
    public void TestaGerarRelatorioShowComLucro() {
        String relatorioExpected = "Relatório de Vendas:\n" +
                "Vendas VIP: 25\n" +
                "Vendas Meia Entrada: 10\n" +
                "Vendas Normais: 65\n" +
                "Receita liquida: 14500.0\n" +
                "Status financeiro: LUCRO";

        this.compraTodosOsIngressosDeUmTipo(TipoIngresso.VIP);
        this.compraTodosOsIngressosDeUmTipo(TipoIngresso.MEIA_ENTRADA);
        this.compraTodosOsIngressosDeUmTipo(TipoIngresso.NORMAL);

        String relatorio = show.gerarRelatorio();
        assertEquals(relatorioExpected, relatorio);
    }
}
