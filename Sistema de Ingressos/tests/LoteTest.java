import Anotations.UnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class LoteTest {

    Lote lote = new Lote(100, 0.10, 100.0);

    Ingresso vip = lote.getIngressos()
            .stream()
            .filter(ingressos -> ingressos.getTipo().equals(TipoIngresso.VIP))
            .findFirst().get();

    Ingresso meia = lote.getIngressos()
            .stream()
            .filter(ingressos -> ingressos.getTipo().equals(TipoIngresso.MEIA_ENTRADA))
            .findFirst().get();

    Ingresso normal = lote.getIngressos()
            .stream()
            .filter(ingressos -> ingressos.getTipo().equals(TipoIngresso.NORMAL))
            .findFirst().get();

    @UnitTest
    void getId() {
        assertEquals(1, lote.getId());
    }

    @UnitTest
    void getIngressos() {
        assertAll(() -> {
            assertNotNull(lote.getIngressos());
            assertFalse(lote.getIngressos().isEmpty());
        });
    }

    @UnitTest
    void getTotalIngressos() {
        assertEquals(100, lote.getTotalIngressos());
    }

    @UnitTest
    void getDesconto() {
        assertEquals(0.10, lote.getDesconto());
    }

    @UnitTest
    void getValorIngresso() {
        assertEquals(100.0, lote.getValorIngresso());
    }

    @UnitTest
    void getValorIngressoComDesconto() {
        assertAll(() -> {
            assertEquals(180.0, lote.getValorIngressoComDesconto(vip));
            assertEquals(50.0, lote.getValorIngressoComDesconto(meia));
            assertEquals(90.0, lote.getValorIngressoComDesconto(normal));
        });
    }
}