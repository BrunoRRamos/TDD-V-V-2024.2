import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Lote {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private int id;
    private List<Ingresso> ingressos;
    private int totalIngressos;
    private double desconto;
    private double valorIngresso;

    public Lote(int totalIngressos, double desconto, double valorIngresso) {
        this.id = this.counter.incrementAndGet();
        this.ingressos = new ArrayList<>();
        this.totalIngressos = totalIngressos;
        this.desconto = desconto;
        this.valorIngresso = valorIngresso;
        this.createIngressos();
    }

    public void resetCounter() {
        this.counter.set(0);
    }

    public int getId() {
        return id;
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    public int getTotalIngressos() {
        return totalIngressos;
    }

    public double getDesconto() {
        return desconto;
    }

    private void geraIngressos(int qnt, TipoIngresso tipo) {
        for (int i = 0; i < qnt; i++) {
            String id = UUID.randomUUID().toString();
            Ingresso newIngresso = new Ingresso(id, tipo, StatusIngresso.DISPONIVEL, this.valorIngresso);
            this.ingressos.add(newIngresso);
        }
    }

    private void createIngressos() {
        if (this.totalIngressos <= 0) {
            throw new RuntimeException("Número de ingressos inválido");
        }

        int qntVip = (int) Math.ceil(this.totalIngressos * 0.25);
        int qntMeia = (int) Math.ceil(this.totalIngressos * 0.10);
        int qntNormal = this.totalIngressos - qntVip - qntMeia;

        this.geraIngressos(qntVip, TipoIngresso.VIP);
        this.geraIngressos(qntMeia, TipoIngresso.MEIA_ENTRADA);
        this.geraIngressos(qntNormal, TipoIngresso.NORMAL);
    }
}
