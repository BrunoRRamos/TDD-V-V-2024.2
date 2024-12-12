

import java.math.BigDecimal;
import java.util.*;


import java.util.Date;
import java.util.List;


import org.junit.Test;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

public class ProcessadorContasTest {

    private final Fatura fatura1 = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Suelen",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2023 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(500)),
                    new Conta(2L, new Date(2023 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(400)),
                    new Conta(3L, new Date(2023 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(600))
            )));
    private final Fatura fatura2 = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Bruno",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2023 - 1900, Calendar.FEBRUARY, 5), BigDecimal.valueOf(700)),
                    new Conta(2L, new Date(2023 - 1900, Calendar.FEBRUARY, 17), BigDecimal.valueOf(800))
            )));
    private final Fatura fatura3 = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Fulano",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2023 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(700)),
                    new Conta(2L, new Date(2023 - 1900, Calendar.FEBRUARY, 17), BigDecimal.valueOf(800))
            )));

    private final Fatura faturaBoleto = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Everton",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2023 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(0.001)),
                    new Conta(2L, new Date(2023 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(5001)),
                    new Conta(3L, new Date(2023 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(100))
            )));

    private final Fatura faturaCartao = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Everton",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2023 - 1900, Calendar.FEBRUARY, 4), BigDecimal.valueOf(100)),
                    new Conta(2L, new Date(2023 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(100))
            )));

    private final Fatura faturaContaAposFatura = new Fatura(
            new Date(2023 - 1900, Calendar.FEBRUARY, 20),
            "Everton",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2023 - 1900, Calendar.FEBRUARY, 24), BigDecimal.valueOf(100))
            )));


    @Test
    public void testContaInexistente() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(999999L, TiposPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
        assertEquals("O codigo informado eh invalido ou inexistete", e.getMessage());
    }

    @Test
    public void testBoletoMenorQue1Centavo() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
        assertEquals("Valor da conta deve ser maior que 0.01 para ser paga com boleto", e.getMessage());
    }

    @Test
    public void testBoletoMaiorQue5000() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(2L, TiposPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 6)));
        assertEquals("Valor da conta deve ser menor que 5000 para ser paga com boleto", e.getMessage());
    }

    @Test
    public void testBoletoAtrasado() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        processadorContas.pagarConta(3L, TiposPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 19));
        assertEquals(BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(1.1)), processadorContas.getValorPago());

    }

    @Test
    public void testPagamentoCartaoConsiderado() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaCartao);
        processadorContas.pagarConta(1L, TiposPagamento.CARTAO_CREDITO, new Date(2023 - 1900, Calendar.FEBRUARY, 4));
        assertEquals(BigDecimal.valueOf(100), processadorContas.getValorPago());
    }

    @Test
    public void testPagamentoCartaoDesconsiderado() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaCartao);
        processadorContas.pagarConta(2L, TiposPagamento.CARTAO_CREDITO, new Date(2023 - 1900, Calendar.FEBRUARY, 10));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());
    }

    @Test
    public void testContaAposFatura() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaContaAposFatura);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 10));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());

        processadorContas.pagarConta(1L, TiposPagamento.CARTAO_CREDITO, new Date(2023 - 1900, Calendar.FEBRUARY, 10));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());

        processadorContas.pagarConta(1L, TiposPagamento.TRANSFERENCIA_BANCARIA, new Date(2023 - 1900, Calendar.FEBRUARY, 10));
        assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago());
    }

    @Test
    public void testFaturaPaga() {
        ProcessadorContas processadorContas = new ProcessadorContas(fatura1);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 19));
        processadorContas.pagarConta(2L, TiposPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 19));
        processadorContas.pagarConta(3L, TiposPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 19));

        assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura());

    }

    @Test
    public void testFaturaNaoPaga() {
        ProcessadorContas processadorContas = new ProcessadorContas(fatura1);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 20));
        processadorContas.pagarConta(2L, TiposPagamento.BOLETO, new Date(2023 - 1900, Calendar.FEBRUARY, 20));

        assertEquals(StatusFatura.PENDENTE, processadorContas.getStatusFatura());

    }
}