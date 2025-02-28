import java.math.BigDecimal;
import java.time.LocalDate;
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
            throw new RuntimeException("Valor da conta deve ser até 5000 para ser paga com boleto");
        }

        if (conta.getValorPago().compareTo(BigDecimal.valueOf(0.01)) < 0) {
            throw new RuntimeException("Valor da conta deve ser maior que 0.01 para ser paga com boleto");
        }
    }

    private void processarPagamentoBoleto(Conta conta, Date data, BigDecimal valorPagamento) {
        if (data.before(fatura.getData())) {
            if (data.compareTo(conta.getData()) > 0) {
                BigDecimal valorComJuros = conta.getValorPago().multiply(BigDecimal.valueOf(1.1));
                if (valorPagamento.compareTo(valorComJuros) >= 0) {
                    fatura.addValorPagamento(valorComJuros);
                } else {
                    throw new RuntimeException("O valor do pagamento com juros é de: R$ " + valorComJuros + ", o valor do seu pagamento é de: R$ " + valorPagamento +
                            ", portanto não é suficiente para pagar a conta com juros");
                }
            } else {
                fatura.addValorPagamento(conta.getValorPago());
            }
            verificaFaturaPaga();
        }
    }

    private void processarPagamentoCartaoCredito(Conta conta, Date data) {
        LocalDate dataFatura = fatura.getData().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate faturaMenos15Dias = dataFatura.minusDays(15);

        Date dataLimite = Date.from(faturaMenos15Dias.atStartOfDay(ZoneId.systemDefault()).toInstant());


        if (data.compareTo(dataLimite) <= 0) {
            fatura.addValorPagamento(conta.getValorPago());
            fatura.setStatusFatura(StatusFatura.PAGA);
            verificaFaturaPaga();
        }else{
            throw new RuntimeException("A tentativa de pagamento por cartão deve ser feita com pelo menos 15 dias de antecedência");
        }
    }

    private void processarPagamentoTranferencia(Conta conta, Date data) {
        if (conta.getData().compareTo(data) >= 0) {
            fatura.addValorPagamento(conta.getValorPago());
            fatura.setStatusFatura(StatusFatura.PAGA);
            verificaFaturaPaga();
        } else {
            fatura.setStatusFatura(StatusFatura.PENDENTE);
        }
    }

    public void pagarConta(Long codConta, TiposPagamento tipo, Date data, BigDecimal valorDoPagamento) {
        Optional<Conta> contaOptional = this.getContas().stream()
                .filter(conta -> conta.getCodConta().equals(codConta))
                .findFirst();

        validarConta(contaOptional);

        Conta conta = contaOptional.get();

        if (conta.getData().after(fatura.getData())) {
            return;
        }
        if (valorDoPagamento.compareTo(conta.getValorPago()) < 0) {
            fatura.setStatusFatura(StatusFatura.PENDENTE);
            return;
        }

        if (tipo == TiposPagamento.BOLETO) {
            validarPagamentoBoleto(conta);
            processarPagamentoBoleto(conta, data, valorDoPagamento);
        } else if (tipo == TiposPagamento.CARTAO_CREDITO) {
            processarPagamentoCartaoCredito(conta, data);
        } else {
            processarPagamentoTranferencia(conta, data);
        }
    }
}
