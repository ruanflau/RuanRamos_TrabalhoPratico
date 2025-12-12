package com.mycompany.ruanramos2024005365_trabalhopratico;


public class Cancelamento implements Runnable {
    private Onibus onibus;

    public Cancelamento(Onibus onibus) {
        this.onibus = onibus;
    }

    @Override
    public void run() {
        try {
            // Espera um pouco antes de cancelar para dar tempo de lotar o ônibus
            Thread.sleep(2000); // 2 segundos
            onibus.cancelarPassagem(2); // Libera 2 vagas
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}