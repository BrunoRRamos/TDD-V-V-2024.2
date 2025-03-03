import Anotations.UnitTest;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
class LoteTestJUnit5 {

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
    @DisplayName("Testa o retorno do id de um lote")
    void getId() {
        assertEquals(1, lote.getId());
    }

    @UnitTest
    @DisplayName("Testa o retorno da coleção de ingressos de um lote")
    void getIngressos() {
        assertAll(() -> {
            assertNotNull(lote.getIngressos());
            assertFalse(lote.getIngressos().isEmpty());
        });
    }

    @UnitTest
    @DisplayName("Testa o retorno do numero de ingressos de um lote")
    void getTotalIngressos() {
        assertEquals(100, lote.getTotalIngressos());
    }

    @UnitTest
    @DisplayName("Testa o retorno do desconto de um lote")
    void getDesconto() {
        assertEquals(0.10, lote.getDesconto());
    }

    @UnitTest
    @DisplayName("Testa o retorno do valor base do ingresso de um lote")
    void getValorIngresso() {
        assertEquals(100.0, lote.getValorIngresso());
    }

    @UnitTest
    @DisplayName("Testa o retorno dos descontos aplicados de um lote")
    void getValorIngressoComDesconto() {
        assertAll(() -> {
            assertEquals(180.0, lote.getValorIngressoComDesconto(vip));
            assertEquals(50.0, lote.getValorIngressoComDesconto(meia));
            assertEquals(90.0, lote.getValorIngressoComDesconto(normal));
        });
    }
}