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

    private void VerificaFaturaPaga() {
        if (this.fatura.getValorTotal().compareTo(this.fatura.getValor()) >= 0) {
            this.fatura.pagarFatura();
        }
    }

    public void pagarConta(Long codConta, TiposPagamento tipo, Date data) {
        Optional<Conta> contaOptional = this.getContas().stream().filter(conta -> conta.getCodConta().equals(codConta)).findFirst();

        if (contaOptional.isEmpty()) {
            throw new RuntimeException("O codigo informado eh invalido ou inexistete");
        }

        Conta conta = contaOptional.get();

        if (conta.getData().after(fatura.getData())) {
            return;
        }

        if (tipo.equals(TiposPagamento.BOLETO)) {

            if (conta.getValorPago().compareTo(BigDecimal.valueOf(5000)) > 0) {
                throw new RuntimeException("Valor da conta deve ser menor que 5000 para ser paga com boleto");
            }

            if (conta.getValorPago().compareTo(BigDecimal.valueOf(0.01)) < 0) {
                throw new RuntimeException("Valor da conta deve ser maior que 0.01 para ser paga com boleto");
            }

            if (data.before(fatura.getData())) {
                if (data.after(conta.getData())) {
                    fatura.addValorPagamento(conta.getValorPago().multiply(BigDecimal.valueOf(1.1)));
                } else {
                    fatura.addValorPagamento(conta.getValorPago());
                }
            }

            VerificaFaturaPaga();
            return;
        }

        if (tipo.equals(TiposPagamento.CARTAO_CREDITO)) {
            LocalDateTime ldt = LocalDateTime.ofInstant(fatura.getData().toInstant(), ZoneId.systemDefault());
            LocalDateTime minusDays = ldt.minusDays(15);
            Date faturaMenos15Dias = Date.from(minusDays.atZone(ZoneId.systemDefault()).toInstant());

            if (data.before(faturaMenos15Dias)) {
                fatura.addValorPagamento(conta.getValorPago());
                fatura.setStatusFatura(StatusFatura.PAGA);
                VerificaFaturaPaga();
            }
            return;
        }

        fatura.addValorPagamento(conta.getValorPago());
        VerificaFaturaPaga();

    }


}
