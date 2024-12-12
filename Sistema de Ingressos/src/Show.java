import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Show {
    private Date data;
    private Double totalDespesaInfra;
    private boolean showEmDataEspecial;
    private int totalIngressos;
    private List<Ingresso> ingressos;

    public Show(Date data, Double totalDespesaInfra, boolean showEmDataEspecial, int totalIngressos) {
        this.data = data;
        this.totalDespesaInfra = totalDespesaInfra;
        this.showEmDataEspecial = showEmDataEspecial;
        this.totalIngressos = totalIngressos;
        this.ingressos = new ArrayList<>();
        this.createIngressos();
    }

    public Date getData() {
        return data;
    }

    public Double getTotalDespesaInfra() {
        return totalDespesaInfra;
    }

    public int getTotalIngressos() {
        return totalIngressos;
    }

    public boolean isShowEmDataEspecial() {
        return showEmDataEspecial;
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    private void geraIngressos(int qnt, TipoIngresso tipo) {
        for (int i = 0; i < qnt; i++) {
            String id = UUID.randomUUID().toString();
            Ingresso newIngresso = new Ingresso(id, tipo, StatusIngresso.DISPONIVEL);
            this.ingressos.add(newIngresso);
        }
    }

    private void createIngressos() {
        int qntVip = (int) Math.ceil(this.totalIngressos * 0.25);

        this.geraIngressos(qntVip, TipoIngresso.VIP);

    }
}
