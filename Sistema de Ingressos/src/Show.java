import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Show {
    private Date data;
    private Double totalDespesaInfra;
    private boolean showEmDataEspecial;
    private int totalIngressos;
    private List<Lote> lotes;
    private double valorIngresso;


    public Show(Date data, Double totalDespesaInfra, boolean showEmDataEspecial, int totalIngressos, double valorIngresso) {
        this.data = data;
        this.totalDespesaInfra = totalDespesaInfra;
        this.showEmDataEspecial = showEmDataEspecial;
        this.totalIngressos = totalIngressos;
        this.valorIngresso = valorIngresso;
        this.lotes = new ArrayList<>();
        this.criarNovoLote(this.totalIngressos, 0.25, this.valorIngresso);
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

    public List<Lote> getLotes() {
        return lotes;
    }

    public double getValorIngresso() {
        return valorIngresso;
    }

    private void validaDesconto(double desconto) {
        if (desconto < 0) {
            throw new IllegalArgumentException("Desconto deve ser maior que zero");
        } else if (desconto > 0.25) {
            throw new IllegalArgumentException("Desconto deve ser menor ou igual a 25%");
        }
    }

    public Lote getLoteById(int id) {
        return this.lotes
                .stream()
                .filter(lote -> lote.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Ingresso comprarIngresso(int loteId, TipoIngresso tipoIngresso) {
        Lote lote = this.getLoteById(loteId);
        Ingresso ingressoComprado = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == tipoIngresso)
                .findFirst()
                .orElse(null);

        ingressoComprado.setStatus(StatusIngresso.VENDIDO);

        return ingressoComprado;
    }

    protected Lote criarNovoLote(int numeroDeIngressos, double desconto, double valorIngresso) {
        this.validaDesconto(desconto);
        if (desconto == 0) {
            desconto = 1.0;
        }
        this.lotes.add(new Lote(numeroDeIngressos, desconto, valorIngresso));

        return this.lotes.get(this.lotes.size() - 1);
    }
}
