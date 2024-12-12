import java.io.ObjectInputFilter;

public class Ingresso {
    private String id;
    private TipoIngresso tipo;
    private StatusIngresso status;

    public Ingresso(String id, TipoIngresso tipo, StatusIngresso status) {
        this.id = id;
        this.tipo = tipo;
        this.status = status;
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
}
