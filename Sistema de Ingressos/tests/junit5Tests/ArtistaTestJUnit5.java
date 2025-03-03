package junit5Tests;

import Anotations.UnitTest;
import core.Artista;
import org.junit.jupiter.api.*;

class ArtistaTestJUnit5 {
    private final Artista artista = new Artista("Tim Maia", 30000);

    @UnitTest
    @DisplayName("Testa o retorno do cache de um artista")
    void getCache() {
        Assertions.assertEquals(30000, artista.getCache());
    }
}