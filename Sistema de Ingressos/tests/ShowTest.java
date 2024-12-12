import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

public class ShowTest {
    private Show show;

    @Before
    public void setUp() throws Exception {
        Date dataDoShow = new Date();
        Double totalDespesaInfra = 50000.00;
        boolean showEmDataEspecial = true;
        int totalIngressos = 100;
        double valorIngresso = 249.99;

        this.show = new Show(dataDoShow, totalDespesaInfra, showEmDataEspecial, totalIngressos, valorIngresso);
    }
    
    @After
    public void tearDown() throws Exception {
        this.show.getLotes().get(this.show.getLotes().size()-1).resetCounter();
        this.show = null;
    }

    @Test
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

        assertTrue(totalIngressosVip >= minVip && totalIngressosVip <= maxVip);
    }

    @Test
    public void TestaQuantidadeDeIngressosMeia() {
        Lote lote = show.getLoteById(1);
        List<Ingresso> ingressosMeia = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.MEIA_ENTRADA)
                .toList().stream().toList();
        int totalIngressosMeia = ingressosMeia.size();
        int totalIngressos = show.getTotalIngressos();
        int qntMeiaExpected = (int) Math.ceil(totalIngressos * 0.10);
        assertTrue(totalIngressosMeia == qntMeiaExpected);
    }

    @Test
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
        assertTrue(totalIngressosNormal == qntNormalExpected);
    }

    @Test
    public void TestaValorDoIngressoVip() {
        Lote lote = show.getLoteById(1);
        Ingresso ingressoVip = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.VIP)
                .findFirst()
                .orElse(null);

        assertNotNull(ingressoVip);
        assertTrue(ingressoVip.getValorIngresso() == show.getValorIngresso() * 2);
    }

    @Test
    public void TestaValorDoIngressoMeia() {
        Lote lote = show.getLoteById(1);
        Ingresso ingressoMeia = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.MEIA_ENTRADA)
                .findFirst()
                .orElse(null);

        assertNotNull(ingressoMeia);
        assertTrue(ingressoMeia.getValorIngresso() == show.getValorIngresso() / 2);
    }

    @Test
    public void TestaValorDoIngressoNormal() {
        Lote lote = show.getLoteById(1);
        Ingresso ingressoNormal = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.NORMAL)
                .findFirst()
                .orElse(null);

        assertNotNull(ingressoNormal);
        assertTrue(ingressoNormal.getValorIngresso() == show.getValorIngresso());
    }

    @Test
    public void TestaCompraDeIngressoVip() {
        Ingresso ingressoComprado = this.show.comprarIngresso(1, TipoIngresso.VIP);
        assertNotNull(ingressoComprado);
        assertTrue(ingressoComprado.getStatus() == StatusIngresso.VENDIDO);
    }

    @Test
    public void TestaCompraDeIngressoMeia() {
        Ingresso ingressoComprado = this.show.comprarIngresso(1, TipoIngresso.MEIA_ENTRADA);
        assertNotNull(ingressoComprado);
        assertTrue(ingressoComprado.getStatus() == StatusIngresso.VENDIDO);
    }

    @Test
    public void TestaCompraDeIngressoNormal() {
        Ingresso ingressoComprado = this.show.comprarIngresso(1, TipoIngresso.NORMAL);
        assertNotNull(ingressoComprado);
        assertTrue(ingressoComprado.getStatus() == StatusIngresso.VENDIDO);
    }

    @Test
    public void TestaCriacaoDeNovoLote() {
        int numeroDeIngressos = 100;
        double desconto = 0.10;
        double valoringresso = 300.0;
        Lote lote = this.show.criarNovoLote(numeroDeIngressos, desconto, valoringresso);

        assertEquals(lote.getTotalIngressos(), numeroDeIngressos);
    }

    @Test
    public void TestaCriarLoteSemDesconto() {
        int numeroDeIngressos = 100;
        double desconto = 0;
        double valoringresso = 300.0;
        Lote lote = this.show.criarNovoLote(numeroDeIngressos, desconto, valoringresso);

        assertEquals(lote.getTotalIngressos(), numeroDeIngressos);
        assertEquals(1.0, lote.getDesconto(), 0.0);
    }
}