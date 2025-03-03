package junit5Tests;

import org.testng.annotations.Test;
import org.junit.jupiter.api.*;
import processador.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Classe de testes para o Sistema de processador de contas usanodo JUnit5")
public class ProcessadorContasJunit5Test {

    public final Fatura fatura1 = new Fatura(
            new Date(2024 - 1900, Calendar.FEBRUARY, 20),
            "Suelen",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2024 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(500)),
                    new Conta(2L, new Date(2024 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(400)),
                    new Conta(3L, new Date(2024 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(600))
            )));

    private final Fatura faturaBoleto = new Fatura(
            new Date(2024 - 1900, Calendar.FEBRUARY, 20),
            "Everton",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(0.001)),
                    new Conta(2L, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(5001)),
                    new Conta(3L, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(100))
            )));

    private final Fatura faturaBoleto5000 = new Fatura(
            new Date(2024 - 1900, Calendar.FEBRUARY, 20),
            "Everton",
            new ArrayList<>(List.of(
                    new Conta(4L, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(5000))
            )));

    private final Fatura faturaBoletoValorAleatorio = new Fatura(
            new Date(2024 - 1900, Calendar.FEBRUARY, 20),
            "Suelen",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(51))
            )));

    private final Fatura faturaBoletoUmCentavo = new Fatura(
            new Date(2024 - 1900, Calendar.FEBRUARY, 20),
            "Bruno",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(0.01))
            )));

    private final Fatura faturaCartao = new Fatura(
            new Date(2024 - 1900, Calendar.FEBRUARY, 20),
            "Bruno",
            new ArrayList<>(List.of(
                    new Conta(2L, new Date(2024 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(100))
            )));

    private final Fatura faturaTranferencia = new Fatura(
            new Date(2024 - 1900, Calendar.FEBRUARY, 20),
            "Bruno",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2024 - 1900, Calendar.FEBRUARY, 4), BigDecimal.valueOf(100)),
                    new Conta(2L, new Date(2024 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(100))
            )));


    private final Fatura faturaContaAposFatura = new Fatura(
            new Date(2024 - 1900, Calendar.FEBRUARY, 20),
            "Suelen",
            new ArrayList<>(List.of(
                    new Conta(1L, new Date(2024 - 1900, Calendar.FEBRUARY, 24), BigDecimal.valueOf(100))
            )));

    private final Fatura faturaContaComValor0 = new Fatura(
            new Date(2024 - 1900, Calendar.FEBRUARY, 20),
            "Fulano",
            new ArrayList<>(List.of(
                    new Conta(5L, new Date(2024 - 1900, Calendar.FEBRUARY, 19), BigDecimal.valueOf(0))
            )));


    @Test
    @DisplayName("Verifica Exception quando a conta é inexistente")
    public void testContaInexistente() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(999999L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(10)));
        assertEquals("O código informado é inválido ou inexistente", e.getMessage());
    }

    @Test
    @DisplayName("Verifica Exception quando o boleto tem valor menor que 1 centavo")
    public void testBoletoMenorQue1Centavo() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(0.01)));
        assertEquals("Valor da conta deve ser maior que 0.01 para ser paga com boleto", e.getMessage());
    }

    @Test
    @DisplayName("Verifica se Status da fatura está igual à paga quando o boleto tem valor igual à 1 centavo")
    public void testBoletoIgual1Centavo() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoletoUmCentavo);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(0.01));
        assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura());
    }

    @Test
    @DisplayName("Verifica Exception quando o boleto tem valor maior que 5000 reais")
    public void testBoletoMaiorQue5000() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(2L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(5001)));
        assertEquals("Valor da conta deve ser até 5000 para ser paga com boleto", e.getMessage());
    }

    @Test
    @DisplayName("Verifica se Status da fatura está igual à paga quando o boleto tem valor igual à 5000 reais")
    public void testBoletoIgual5000() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoleto5000);
        processadorContas.pagarConta(4L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(5001));
        assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura());
    }


    @Test
    @DisplayName("Verifica se Status da fatura está igual à paga quando o boleto tem valor aleatório")
    public void testBoletoValorAleatoria() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoletoValorAleatorio);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(51));
        assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura());
    }

    @Test
    @DisplayName("Verifica Valor pago e Status da fatura ao ter o boleto pago com atraso")
    public void testBoletoAtrasado() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoletoValorAleatorio);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 19), BigDecimal.valueOf(57));
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(51).multiply(BigDecimal.valueOf(1.1)), processadorContas.getValorPago()),
                () -> assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura())
        );
    }

    @Test
    @DisplayName("Verifica Exception quando o boleto está atrasado e tem valor insuficiente")
    public void testBoletoAtrasadoEValorInsuficiente() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoletoValorAleatorio);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 25), BigDecimal.valueOf(51)));
        assertEquals(e.getMessage(), "O valor do pagamento com juros é de: R$ 56.1, o valor do seu pagamento é de: R$ 51, portanto não é suficiente para pagar a conta com juros");
    }

    @Test
    @DisplayName("Verifica Exception quando o boleto tem valor insuficiente")
    public void testBoletoComValorInsuficiente() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoletoValorAleatorio);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(20)));
        assertEquals(e.getMessage(), "O valor do pagamento não é suficiente");
    }


    @Test
    @DisplayName("Verifica juros quando boleto é pago um dia depois")
    public void testBoletoUmDiaDepois() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoletoValorAleatorio);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 7), BigDecimal.valueOf(100));
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(51).multiply(BigDecimal.valueOf(1.1)), processadorContas.getValorPago()),
                () -> assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura()));
    }

    @Test
    @DisplayName("Verifica condições quando boleto é pago em dia")
    public void testBoletoEmDia() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoletoValorAleatorio);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 6), BigDecimal.valueOf(100));
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(51), processadorContas.getValorPago()),
                () -> assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura()));

    }

    @Test
    @DisplayName("Verifica condições quando boleto é pago um dia antes")
    public void testBoletoUmDiaAntes() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaBoletoValorAleatorio);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 5), BigDecimal.valueOf(52));
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(51), processadorContas.getValorPago()),
                () -> assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura()));
    }

    @Test
    @DisplayName("Verifica condições quando fatura paga com Cartão é feita 15 dias antes")
    public void testPagamentoCartaoComMenosDe15Dias() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaCartao);
        processadorContas.pagarConta(2L, TiposPagamento.CARTAO_CREDITO, new Date(2024 - 1900, Calendar.FEBRUARY, 4), BigDecimal.valueOf(100));
        assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura());
    }

    @Test
    @DisplayName("Verifica condições quando fatura paga com Cartão é feita com mais de 15 dias depois")
    public void testPagamentoCartaoComMaisDe15Dias() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaCartao);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(2L, TiposPagamento.CARTAO_CREDITO, new Date(2024 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(100)));
        assertEquals(e.getMessage(), "A tentativa de pagamento por cartão deve ser feita com pelo menos 15 dias de antecedência");
    }

    @Test
    @DisplayName("Verifica condições quando fatura paga com Cartão é feita com 15 dias")
    public void testPagamentoCartaoCom15Dias() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaCartao);
        processadorContas.pagarConta(2L, TiposPagamento.CARTAO_CREDITO, new Date(2024 - 1900, Calendar.FEBRUARY, 5), BigDecimal.valueOf(100));
        assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura());
    }

    @Test
    @DisplayName("Verifica condições quando fatura paga com Tranferência antes do prazo")
    public void testPagamentoTransferenciaNoPrazo() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaTranferencia);
        processadorContas.pagarConta(1L, TiposPagamento.TRANSFERENCIA_BANCARIA, new Date(2024 - 1900, Calendar.JANUARY, 25), BigDecimal.valueOf(100));
        processadorContas.pagarConta(2L, TiposPagamento.TRANSFERENCIA_BANCARIA, new Date(2024 - 1900, Calendar.JANUARY, 25), BigDecimal.valueOf(100));
        assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura());
    }

    @Test
    @DisplayName("Verifica condições quando fatura paga com Tranferência é feita no prazo")
    public void testPagamentoTransferenciaNoDia() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaTranferencia);
        processadorContas.pagarConta(1L, TiposPagamento.TRANSFERENCIA_BANCARIA, new Date(2024 - 1900, Calendar.JANUARY, 25), BigDecimal.valueOf(100));
        processadorContas.pagarConta(2L, TiposPagamento.TRANSFERENCIA_BANCARIA, new Date(2024 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(100));
        assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura());
    }

    @Test
    @DisplayName("Verifica condições quando fatura paga com Tranferência é depois do prazo")
    public void testPagamentoTransferenciaAtrasada() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaTranferencia);
        processadorContas.pagarConta(1L, TiposPagamento.TRANSFERENCIA_BANCARIA, new Date(2024 - 1900, Calendar.JANUARY, 25), BigDecimal.valueOf(100));
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(2L, TiposPagamento.TRANSFERENCIA_BANCARIA, new Date(2024 - 1900, Calendar.FEBRUARY, 28), BigDecimal.valueOf(100)));
        assertAll(
                () -> assertEquals(e.getMessage(), "A data do pagamento deve ser anterior à data da fatura"),
                () -> assertEquals(StatusFatura.PENDENTE, processadorContas.getStatusFatura()));
    }

    @Test
    @DisplayName("Verifica condições quando a conta tem data posterior à fatura paga com Boleto")
    public void testContaAposFaturaBoleto() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaContaAposFatura);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(100));
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago()),
                () -> assertEquals(StatusFatura.PENDENTE, processadorContas.getStatusFatura()));

    }

    @Test
    @DisplayName("Verifica condições quando a conta tem data posterior à fatura paga com Cartão")
    public void testContaAposFaturaCartao() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaContaAposFatura);
        processadorContas.pagarConta(1L, TiposPagamento.CARTAO_CREDITO, new Date(2024 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(100));
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago()),
                () -> assertEquals(StatusFatura.PENDENTE, processadorContas.getStatusFatura()));

    }

    @Test
    @DisplayName("Verifica condições quando a conta tem data posterior à fatura paga com Transferencia")
    public void testContaAposFaturaTranferencia() {
        ProcessadorContas processadorContas = new ProcessadorContas(faturaContaAposFatura);
        processadorContas.pagarConta(1L, TiposPagamento.TRANSFERENCIA_BANCARIA, new Date(2024 - 1900, Calendar.FEBRUARY, 10), BigDecimal.valueOf(100));
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(0), processadorContas.getValorPago()),
                () -> assertEquals(StatusFatura.PENDENTE, processadorContas.getStatusFatura()));
    }

    @Test
    @DisplayName("Verifica fatura com todas as contas pagas com boleto")
    public void testFaturaPaga() {
        ProcessadorContas processadorContas = new ProcessadorContas(fatura1);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 19), BigDecimal.valueOf(500));
        processadorContas.pagarConta(2L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 19), BigDecimal.valueOf(400));
        processadorContas.pagarConta(3L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 19), BigDecimal.valueOf(600));

        assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura());

    }

    @Test
    @DisplayName("Verifica fatura que recebe valor do pagamento menor do que o esperado")
    public void testFaturaComValorMenor() {
        ProcessadorContas processadorContas = new ProcessadorContas(fatura1);
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 19), BigDecimal.valueOf(0)));
        processadorContas.pagarConta(2L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 19), BigDecimal.valueOf(400));
        processadorContas.pagarConta(3L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 19), BigDecimal.valueOf(600));
        assertAll(
                () -> assertEquals(e.getMessage(), "O valor do pagamento não é suficiente"),
                () -> assertEquals(StatusFatura.PENDENTE, processadorContas.getStatusFatura()));
    }

    @Test
    @DisplayName("Verifica fatura que não teve todas as contas pagas")
    public void testFaturaNaoPaga() {
        ProcessadorContas processadorContas = new ProcessadorContas(fatura1);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(500));
        processadorContas.pagarConta(2L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(400));

        assertEquals(StatusFatura.PENDENTE, processadorContas.getStatusFatura());

    }

    @Test
    @DisplayName("Verifica fatura que possui todos os métodos de pagamento")
    public void mixDePagamentos() {
        ProcessadorContas processadorContas = new ProcessadorContas(fatura1);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(500));
        processadorContas.pagarConta(2L, TiposPagamento.CARTAO_CREDITO, new Date(2024 - 1900, Calendar.FEBRUARY, 1), BigDecimal.valueOf(400));
        processadorContas.pagarConta(3L, TiposPagamento.TRANSFERENCIA_BANCARIA, new Date(2024 - 1900, Calendar.FEBRUARY, 19), BigDecimal.valueOf(600));

        assertEquals(StatusFatura.PAGA, processadorContas.getStatusFatura());

    }

    @Test
    @DisplayName("Verifica fatura que possui todos os métodos de pagamento, mas com cartão fora do prazo")
    public void mixDePagamentosComCartaoForaDoPrazo() {
        ProcessadorContas processadorContas = new ProcessadorContas(fatura1);
        processadorContas.pagarConta(1L, TiposPagamento.BOLETO, new Date(2024 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(500));
        Exception e = assertThrows(RuntimeException.class, () -> processadorContas.pagarConta(2L, TiposPagamento.CARTAO_CREDITO, new Date(2024 - 1900, Calendar.FEBRUARY, 18), BigDecimal.valueOf(400)));
        processadorContas.pagarConta(3L, TiposPagamento.TRANSFERENCIA_BANCARIA, new Date(2024 - 1900, Calendar.FEBRUARY, 20), BigDecimal.valueOf(600));

        assertEquals(e.getMessage(), "A tentativa de pagamento por cartão deve ser feita com pelo menos 15 dias de antecedência");
    }

    @Test
    @DisplayName("Verifica fatura recebendo pagamento com valor 0")
    public void testFaturaComValor0() {
        Exception e = assertThrows(RuntimeException.class, () -> faturaContaComValor0.addValorPagamento(BigDecimal.valueOf(0)));
        assertEquals(e.getMessage(), "Valor não pode ser 0");

    }

    @Test
    @DisplayName("Verifica fatura recebendo pagamento com valor negativo")
    public void testFaturaComValorNegativo() {
        Exception e = assertThrows(RuntimeException.class, () -> fatura1.addValorPagamento(BigDecimal.valueOf(-50)));
        assertEquals(e.getMessage(), "Valor não pode ser negativo");
    }
}