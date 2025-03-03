package core;

public class Artista {
    private String nome;
    private double cache;

    public Artista(String nome, double cache) {
        this.nome = nome;
        this.cache = cache;
    }

    public double getCache() {
        return cache;
    }
}
