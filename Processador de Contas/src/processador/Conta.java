package processador;

import java.math.BigDecimal;
import java.util.Date;

public class Conta {
    private Long codConta;
    private Date data;
    private BigDecimal valorPago;

    public Conta(Long cod, Date data, BigDecimal valor) {
        this.codConta = cod;
        this.data = data;
        this.valorPago = valor;

    }

    public Long getCodConta() {
        return codConta;
    }

    public Date getData() {
        return data;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }
}
