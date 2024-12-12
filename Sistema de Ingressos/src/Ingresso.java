import java.io.ObjectInputFilter;

public class Ingresso {
    private String id;
    private TipoIngresso tipo;
    private StatusIngresso status;
    private double valorIngresso;

    public Ingresso(String id, TipoIngresso tipo, StatusIngresso status, double valorIngresso) {
        this.id = id;
        this.tipo = tipo;
        this.status = status;

        if (tipo == TipoIngresso.VIP) {
            this.valorIngresso = valorIngresso * 2;
        } else if (tipo == TipoIngresso.MEIA_ENTRADA) {
            this.valorIngresso = valorIngresso / 2;
        } else {
            this.valorIngresso = valorIngresso;
        }
    }

    public String getId() {
        return id;
    }

    public TipoIngresso getTipo() {
        return tipo;
    }

    public StatusIngresso getStatus() {
        return status;
    }

    public double getValorIngresso() {
        return valorIngresso;
    }

    public void setStatus(StatusIngresso status) {
        this.status = status;
    }
}
