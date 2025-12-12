package com.mycompany.ruanramos2024005365_trabalhopratico;


public class AgenteVenda implements Runnable {
    private Onibus onibus;
    private String nome;

    public AgenteVenda(Onibus onibus, String nome) {
        this.onibus = onibus;
        this.nome = nome;
    }

    @Override
    public void run() {
        // Tenta reservar um assento
        onibus.reservarAssento(this.nome);
    }
}