import Anotations.UnitTest;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class IngressoTestJUnit5 {
    private final Ingresso ingressoNormal =  new Ingresso("1", TipoIngresso.NORMAL, StatusIngresso.DISPONIVEL, 200);
    private final Ingresso ingressoVip =  new Ingresso("2", TipoIngresso.VIP, StatusIngresso.DISPONIVEL, 200);
    private final Ingresso ingressoMeia =  new Ingresso("3", TipoIngresso.MEIA_ENTRADA, StatusIngresso.DISPONIVEL, 200);

    @UnitTest
    @DisplayName("Testa o retorno do id de um ingresso")
    void getId() {
        assertAll(() -> {
            assertEquals("1", ingressoNormal.getId());
            assertEquals("2", ingressoVip.getId());
            assertEquals("3", ingressoMeia.getId());
        });
    }

    @UnitTest
    @DisplayName("Testa o retorno do tipo de um ingresso")
    void getTipo() {
        assertAll(() -> {
            assertEquals(TipoIngresso.NORMAL, ingressoNormal.getTipo());
            assertEquals(TipoIngresso.VIP, ingressoVip.getTipo());
            assertEquals(TipoIngresso.MEIA_ENTRADA, ingressoMeia.getTipo());
        });
    }

    @UnitTest
    @DisplayName("Testa o retorno do status de um ingresso")
    void getStatus() {
        assertAll(() -> {
            assertEquals(StatusIngresso.DISPONIVEL, ingressoNormal.getStatus());
            assertEquals(StatusIngresso.DISPONIVEL, ingressoVip.getStatus());
            assertEquals(StatusIngresso.DISPONIVEL, ingressoMeia.getStatus());
        });
    }

    @UnitTest
    @DisplayName("Testa o retorno do valor de um ingresso")
    void getValorIngresso() {
        assertAll(() -> {
            assertEquals(200, ingressoNormal.getValorIngresso());
            assertEquals(400, ingressoVip.getValorIngresso());
            assertEquals(100, ingressoMeia.getValorIngresso());
        });
    }

    @UnitTest
    @DisplayName("Testa o set de um status de um ingresso")
    void setStatus() {
        this.getStatus();
        this.ingressoNormal.setStatus(StatusIngresso.VENDIDO);
        this.ingressoVip.setStatus(StatusIngresso.VENDIDO);
        this.ingressoMeia.setStatus(StatusIngresso.VENDIDO);

        assertAll(() -> {
            assertEquals(StatusIngresso.VENDIDO, ingressoNormal.getStatus());
            assertEquals(StatusIngresso.VENDIDO, ingressoVip.getStatus());
            assertEquals(StatusIngresso.VENDIDO, ingressoMeia.getStatus());
        });
    }
}