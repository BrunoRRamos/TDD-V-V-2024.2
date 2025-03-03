package processador;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Fatura {
    private Date data;
    private BigDecimal valorTotal = BigDecimal.ZERO;
    private String nomeCliente;

    private List<Conta> contas;
    private BigDecimal valor;

    private StatusFatura statusFatura = StatusFatura.PENDENTE;

    public Fatura(Date data, String nomeCliente, List<Conta> contas) {
        this.data = data;
        this.nomeCliente = nomeCliente;
        this.contas = contas;
        this.valor = contas.stream().map(Conta::getValorPago).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addValorPagamento(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Valor não pode ser negativo");
        }

        if (valor.compareTo(BigDecimal.ZERO) == 0) {
            throw new RuntimeException("Valor não pode ser 0");
        }
        this.valorTotal = this.valorTotal.add(valor);
    }

    public void pagarFatura() {
        this.statusFatura = StatusFatura.PAGA;
    }

    public Date getData() {
        return data;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public List<Conta> getContas() {
        return contas;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public StatusFatura getStatusFatura() {
        return statusFatura;
    }

    public void setStatusFatura(StatusFatura statusFatura) {
        this.statusFatura = statusFatura;
    }
}
