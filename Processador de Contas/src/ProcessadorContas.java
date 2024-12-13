import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProcessadorContas {

    private Fatura fatura;

    public ProcessadorContas(Fatura fatura) {
        this.fatura = fatura;
    }

    public List<Conta> getContas() {
        return fatura.getContas();
    }

    public StatusFatura getStatusFatura() {
        return fatura.getStatusFatura();
    }

    public BigDecimal getValorPago() {
        return fatura.getValorTotal();
    }

    private void verificaFaturaPaga() {
        if (fatura.getValorTotal().compareTo(fatura.getValor()) >= 0) {
            fatura.pagarFatura();
        }
    }

    private void validarConta(Optional<Conta> contaOptional) {
        if (contaOptional.isEmpty()) {
            throw new RuntimeException("O código informado é inválido ou inexistente");
        }
    }

    private void validarPagamentoBoleto(Conta conta) {
        if (conta.getValorPago().compareTo(BigDecimal.valueOf(5000)) > 0) {
            throw new RuntimeException("Valor da conta deve ser menor que 5000 para ser paga com boleto");
        }

        if (conta.getValorPago().compareTo(BigDecimal.valueOf(0.01)) < 0) {
            throw new RuntimeException("Valor da conta deve ser maior que 0.01 para ser paga com boleto");
        }
    }

    private void processarPagamentoBoleto(Conta conta, Date data) {
        if (data.before(fatura.getData())) {
            if (data.after(conta.getData())) {
                fatura.addValorPagamento(conta.getValorPago().multiply(BigDecimal.valueOf(1.1)));
            } else {
                fatura.addValorPagamento(conta.getValorPago());
            }
        }
        verificaFaturaPaga();
    }

    private void processarPagamentoCartaoCredito(Conta conta, Date data) {
        LocalDateTime ldt = LocalDateTime.ofInstant(fatura.getData().toInstant(), ZoneId.systemDefault());
        LocalDateTime minusDays = ldt.minusDays(15);
        Date faturaMenos15Dias = Date.from(minusDays.atZone(ZoneId.systemDefault()).toInstant());

        if (data.before(faturaMenos15Dias)) {
            fatura.addValorPagamento(conta.getValorPago());
            fatura.setStatusFatura(StatusFatura.PAGA);
        }
        verificaFaturaPaga();
    }

    public void pagarConta(Long codConta, TiposPagamento tipo, Date data) {
        Optional<Conta> contaOptional = this.getContas().stream()
                .filter(conta -> conta.getCodConta().equals(codConta))
                .findFirst();

        validarConta(contaOptional);

        Conta conta = contaOptional.get();

        if (conta.getData().after(fatura.getData())) {
            return;
        }

        if (tipo == TiposPagamento.BOLETO) {
            validarPagamentoBoleto(conta);
            processarPagamentoBoleto(conta, data);
        } else if (tipo == TiposPagamento.CARTAO_CREDITO) {
            processarPagamentoCartaoCredito(conta, data);
        } else {
            fatura.addValorPagamento(conta.getValorPago());

        }
        verificaFaturaPaga();
    }
    }
