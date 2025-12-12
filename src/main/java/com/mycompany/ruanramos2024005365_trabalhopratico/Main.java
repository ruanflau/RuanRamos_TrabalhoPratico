package com.mycompany.ruanramos2024005365_trabalhopratico;


public class Main {
    public static void main(String[] args) {
        System.out.println("--- Iniciando Vendas da AMATUR (Rota Boa Vista-Manaus) ---");
        
        Onibus onibus = new Onibus(); // Inicia com 5 assentos 

        // Parte I: Criar 7 threads de agentes 
        Thread[] agentes = new Thread[7];
        for (int i = 0; i < 7; i++) {
            agentes[i] = new Thread(new AgenteVenda(onibus, "Agente-" + (i + 1)));
            agentes[i].start();
        }

        // Parte III: Thread de Cancelamento 
        Thread cancelamento = new Thread(new Cancelamento(onibus));
        cancelamento.start();
    }
}