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
        Integer lotesIngressos = 3;
        boolean showEmDataEspecial = true;

        // Criar o objeto Show
        Show show = new Show(dataDoShow, totalDespesaInfra, lotesIngressos, showEmDataEspecial);
    }

    @Test
    public void TestaQuantidadeDeIngressosVip() {
        List<Ingresso> ingressosVip = show.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == TipoIngresso.VIP)
                .toList().stream().toList();
        int totalIngressosVip = ingressosVip.size();
        int totalIngressos = show.getIngressos().size();
        int minVip = (int) Math.ceil(totalIngressos * 0.20);
        int maxVip = (int) Math.floor(totalIngressos * 0.30);

        assertTrue(totalIngressosVip >= minVip && totalIngressosVip <= maxVip);
    }

    @Test
    public void TestaQuantidadeDeIngressosMeia() {

    }

    @Test
    public void TestaQuantidadeDeIngressosNormal() {

    }
}