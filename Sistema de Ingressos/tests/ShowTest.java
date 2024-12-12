import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ShowTest {
    private Show show;

    @Before
    public void setUp() throws Exception {
        Date dataDoShow = new Date();
        Double totalDespesaInfra = 50000.00;
        boolean showEmDataEspecial = true;
        int totalIngressos = 100;

        this.show = new Show(dataDoShow, totalDespesaInfra, showEmDataEspecial, totalIngressos);
    }
    
    @After
    public void tearDown() throws Exception {
        this.show = null;
    }

    @Test
    public void TestaQuantidadeDeIngressosVip() {
        List<Ingresso> ingressosVip = show.getIngressos()
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
        List<Ingresso> ingressosMeia = show.getIngressos()
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

    }
}