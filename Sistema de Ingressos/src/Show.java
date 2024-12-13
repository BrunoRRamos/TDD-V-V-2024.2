import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Show {
    private Artista artista;
    private Date data;
    private Double totalDespesaInfra;
    private boolean showEmDataEspecial;
    private int totalIngressos;
    private List<Lote> lotes;
    private double valorIngresso;


    public Show(Date data, Double totalDespesaInfra, boolean showEmDataEspecial, int totalIngressos, double valorIngresso, Artista artista) {
        this.artista = artista;
        this.data = data;
        this.showEmDataEspecial = showEmDataEspecial;
        this.totalDespesaInfra = totalDespesaInfra;
        this.totalIngressos = totalIngressos;
        this.valorIngresso = valorIngresso;
        this.lotes = new ArrayList<>();
        this.criarNovoLote(this.totalIngressos, 0.25, this.valorIngresso);

        if(this.showEmDataEspecial){
            this.totalDespesaInfra = totalDespesaInfra * 0.15;
        }
    }

    public int getTotalIngressos() {
        return totalIngressos;
    }

    public List<Lote> getLotes() {
        return lotes;
    }

    private double calculaReceitaIngressos(TipoIngresso tipoIngresso, Lote lote) {
        double total = lote.getIngressos()
                .stream()
                .filter(e -> e.getTipo() == tipoIngresso &&
                        e.getStatus() == StatusIngresso.VENDIDO)
                .mapToDouble(lote::getValorIngressoComDesconto)
                .sum();

        return total;
    }

    private int getNumTotalIngressosVendidos(TipoIngresso tipoIngresso) {
        return this.getLotes()
                .stream()
                .mapToInt(lote -> (int) lote.getIngressos()
                        .stream()
                        .filter(e -> e.getStatus() == StatusIngresso.VENDIDO && e.getTipo() == tipoIngresso)
                        .count())
                .sum();
    }

    private double calculaTotalReceitaIngressos(TipoIngresso tipoIngresso) {
        return this.lotes
                .stream()
                .mapToDouble(lote -> this.calculaReceitaIngressos(tipoIngresso, lote))
                .sum();
    }

    private double calculaReceita() {
        double totalVendasVip = this.calculaTotalReceitaIngressos(TipoIngresso.VIP);
        double totalVendasMeia = this.calculaTotalReceitaIngressos(TipoIngresso.MEIA_ENTRADA);
        double totalVendasNormal = this.calculaTotalReceitaIngressos(TipoIngresso.NORMAL);

        return  totalVendasVip + totalVendasMeia + totalVendasNormal;
    }

    private void validaTipoDeIngresso(TipoIngresso tipoIngresso) {
        if (tipoIngresso == TipoIngresso.MEIA_ENTRADA) {
            throw new IllegalArgumentException("Desconto não aplicavel a ingresso do tipo: MEIA_ENTRADA");
        }
    }

    private void validaDesconto(double desconto) {
        if (desconto < 0) {
            throw new IllegalArgumentException("Desconto deve ser maior que zero");
        } else if (desconto > 0.25) {
            throw new IllegalArgumentException("Desconto deve ser menor ou igual a 25%");
        }
    }

    private Ingresso getIngressosDisponiveis(int loteId, TipoIngresso tipoIngresso) {
        return this.getLoteById(loteId)
                .getIngressos()
                .stream()
                .filter(e ->
                        e.getStatus() == StatusIngresso.DISPONIVEL &&
                                e.getTipo() == tipoIngresso)
                .findFirst().orElse(null);
    }

    private StatusFinaceiro getStatusFinaceiro(double total) {
        if(total > 0) {
            return StatusFinaceiro.LUCRO;
        } else if(total == 0) {
            return StatusFinaceiro.ESTAVEL;
        }
        return StatusFinaceiro.PREJUIZO;
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

    public Lote criarNovoLote(int numeroDeIngressos, double desconto, double valorIngresso) {
        this.validaDesconto(desconto);
        if (desconto == 0) {
            desconto = 1.0;
        }
        this.lotes.add(new Lote(numeroDeIngressos, desconto, valorIngresso));

        return this.lotes.get(this.lotes.size() - 1);
    }

    public int contaTotalIngressosDisponiveis(int loteId, TipoIngresso tipoIngresso) {
        int numDeingressosDisponiveis = (int) this.getLoteById(loteId)
                .getIngressos()
                .stream()
                .filter(e ->
                        e.getStatus() == StatusIngresso.DISPONIVEL &&
                                e.getTipo() == tipoIngresso)
                .count();

        return numDeingressosDisponiveis;
    }

    public double comprarIngressoComDesconto(int loteId, TipoIngresso tipoIngresso) {
        this.validaTipoDeIngresso(tipoIngresso);
        int ingressosDisponiveis = this.contaTotalIngressosDisponiveis(loteId, tipoIngresso);
        double valorDaCompra = 0;

        if (ingressosDisponiveis > 0) {
            Ingresso ingresso = this.getIngressosDisponiveis(loteId, tipoIngresso);
            valorDaCompra = ingresso.getValorIngresso() * this.getLoteById(loteId).getDesconto();
            this.comprarIngresso(loteId, tipoIngresso);
        } else {
            throw new RuntimeException("Não há ingressos disponiveis deste tipo");
        }

        return valorDaCompra;
    }

    public String gerarRelatorio() {
        int vendasVip = this.getNumTotalIngressosVendidos(TipoIngresso.VIP);
        int vendasMeia = this.getNumTotalIngressosVendidos(TipoIngresso.MEIA_ENTRADA);
        int vendasNormal = this.getNumTotalIngressosVendidos(TipoIngresso.NORMAL);
        double receitaTotal = this.calculaReceita();
        double valorFinal = receitaTotal - this.totalDespesaInfra - this.artista.getCache();

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Relatório de Vendas:\n")
                .append("Vendas VIP: ").append(vendasVip).append("\n")
                .append("Vendas Meia Entrada: ").append(vendasMeia).append("\n")
                .append("Vendas Normais: ").append(vendasNormal).append("\n")
                .append("Receita liquida: ").append(valorFinal).append("\n")
                .append("Status financeiro: ").append(this.getStatusFinaceiro(valorFinal));

        return relatorio.toString();
    }

}
