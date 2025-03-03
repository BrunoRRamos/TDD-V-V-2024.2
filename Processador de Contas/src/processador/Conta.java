package processador;

import java.math.BigDecimal;
import java.util.Date;

public class Conta {
    private Long codConta;
    private Date data;
    private BigDecimal valorPago;

    private TiposPagamento tiposPagamento;

    public Conta(Long cod, Date data, BigDecimal valor) {
        this.codConta = cod;
        this.data = data;
        this.valorPago = valor;

    }

    public Long getCodConta() {
        return codConta;
    }

    public void setCodConta(Long codConta) {
        this.codConta = codConta;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public TiposPagamento getTiposPagamento() {
        return tiposPagamento;
    }

    public void setTiposPagamento(TiposPagamento tiposPagamento) {
        this.tiposPagamento = tiposPagamento;
    }
}
