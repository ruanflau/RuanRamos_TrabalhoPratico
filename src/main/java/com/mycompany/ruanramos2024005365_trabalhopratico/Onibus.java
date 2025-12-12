package com.mycompany.ruanramos2024005365_trabalhopratico;


import java.util.concurrent.Semaphore;

public class Onibus {
    private int assentosDisponiveis = 5; // 
    
    // Semáforo para controlar o número máximo de conexões simultâneas (Parte IV)
    private final Semaphore semaforo = new Semaphore(3); // 

    // Método para tentar comprar passagem
    public void reservarAssento(String agente) {
        try {
            System.out.println(agente + " está tentando se conectar ao servidor...");
            
            // Parte IV: Adquire permissão (Segurança da Balada)
            semaforo.acquire(); // 
            System.out.println(agente + " conectou-se! Tentando reservar...");

            // Parte II: Bloco Sincronizado (Mutex) para evitar Race Condition
            synchronized (this) { // 
                
                // Parte III: Lógica de Wait (Produtor-Consumidor)
                while (assentosDisponiveis == 0) { // Enquanto não houver vaga
                    System.out.println("LOG: Ônibus lotado. " + agente + " entrou em espera (WAIT).");
                    try {
                        wait(); // Thread dorme e libera o lock
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println(agente + " foi interrompido.");
                        return; // Sai se for interrompido
                    }
                }

                // Parte I e II: Verificação e Venda
                if (assentosDisponiveis > 0) { // 
                    // Simula latência da rede
                    try {
                        Thread.sleep(100); // 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    assentosDisponiveis--; // Decrementa a vaga
                    System.out.println("SUCESSO: " + agente + " comprou uma passagem. Vagas restantes: " + assentosDisponiveis);
                }
            } // Fim do synchronized

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Parte IV: Libera a permissão do semáforo independente do resultado
            semaforo.release(); // 
            System.out.println(agente + " desconectou do servidor.");
        }
    }

    // Parte III: Método para Cancelamento (Produtor)
    public void cancelarPassagem(int quantidade) {
        synchronized (this) { // 
            assentosDisponiveis += quantidade;
            System.out.println("\n--- ADMIN: " + quantidade + " passagem(ns) cancelada(s/s). Novas vagas: " + assentosDisponiveis + " ---");
            notifyAll(); // Avisa as threads que estão em wait()
        }
    }
    
    // Método auxiliar para ver saldo (para debug)
    public int getAssentosDisponiveis() {
        return assentosDisponiveis;
    }
}
