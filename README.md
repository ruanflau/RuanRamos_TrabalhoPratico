# 1. Introdução e Contextualização

O presente trabalho prático tem como objetivo simular e solucionar problemas críticos de concorrência no sistema de vendas da empresa **AMATUR**. O cenário proposto envolve a rota **Boa Vista–Manaus**, onde múltiplos agentes de venda tentam reservar simultaneamente assentos limitados.

O sistema original, desprovido de controle de concorrência, apresentava falhas graves de integridade de dados, resultando em **overbooking** (venda de passagens excedentes). Este relatório descreve as etapas de implementação para mitigar esses problemas utilizando mecanismos de sincronização da linguagem **Java**, como `synchronized`, `wait/notify` e `Semaphore`.

---

# 2. Desenvolvimento e Metodologia

O projeto foi dividido em quatro etapas incrementais de complexidade, partindo da demonstração do problema até a implementação de uma arquitetura robusta e segura.

## 2.1. Simulação do Caos (Race Condition)

Inicialmente, foi implementada a classe `Onibus` contendo um atributo de assentos disponíveis e um método de reserva com latência simulada (`Thread.sleep`). Sete threads (agentes) foram instanciadas para competir por 5 assentos simultaneamente.

**Observação:** Sem controle, múltiplas threads acessaram a verificação `if (assentos > 0)` ao mesmo tempo, gerando vendas duplicadas e saldo negativo de assentos.

## 2.2. Implementação de Mutex (Synchronized)

Para corrigir a inconsistência, aplicou-se o controle de concorrência via palavra-chave `synchronized`. Optou-se pela sincronização de bloco (`synchronized(this)`) em vez da sincronização do método inteiro, visando isolar apenas a seção crítica (leitura e escrita da variável de assentos) e otimizar a performance.

## 2.3. Padrão Produtor–Consumidor (Wait/Notify)

Foi implementada a funcionalidade de cancelamento de passagens. Caso o ônibus esteja lotado, a thread do agente entra em estado de espera (`wait()`). Uma thread separada, simulando o cancelamento, libera vagas e notifica as threads aguardando (`notifyAll()`), permitindo que tentem a compra novamente.

## 2.4. Controle de Fluxo (Semáforos)

Para simular a limitação de recursos do servidor, utilizou-se a classe `java.util.concurrent.Semaphore`. Foi definido um limite de **3 conexões simultâneas**. O agente deve adquirir uma permissão (`acquire()`) antes de processar a compra e liberá-la (`release()`) ao final.

---

# 3. Análise dos Resultados

Esta seção detalha as justificativas técnicas solicitadas nos critérios de avaliação.
![Image Alt](https://github.com/ruanflau/RuanRamos_TrabalhoPratico/blob/master/Captura%20de%20tela%202025-12-12%20104404.png?raw=true)

## 3.1. Evidência do Erro de Overbooking (Parte I)

Durante a execução da Parte I, o console exibiu mensagens confirmando a venda de passagens mesmo após o esgotamento do estoque. O saldo final da variável `assentosDisponiveis` resultou em valores negativos (ex: **-2**), comprovando a ocorrência de uma **Race Condition**.

Isso ocorre porque a operação de **ler → verificar → decrementar** não era atômica.

## 3.2. Solução da Inconsistência com Synchronized (Parte II)

O uso do bloco `synchronized` resolveu o problema ao garantir a **Exclusão Mútua**.

**Funcionamento:** O bloco sincronizado cria um *lock* no objeto `Onibus`. Apenas uma thread por vez consegue entrar na seção crítica.

**Resultado:** A atomicidade da operação é forçada. Se um agente entra no bloco, os demais devem aguardar sua finalização. Isso impede leituras de valores desatualizados e elimina o overbooking.

## 3.3. Eficiência de CPU: Wait/Notify vs Busy-Waiting (Parte III)

A implementação do padrão Produtor–Consumidor utilizou `wait()` e `notify()` para gerenciar a espera por vagas, evitando o custo computacional do *busy-waiting*.

### Busy-Waiting (Ineficiente)

Mantém a CPU em uso total verificando repetidamente uma condição:

```
while (lotado) { }
```

### Wait/Notify (Eficiente)

`wait()` suspende a thread e libera o processador. A thread só volta a ser executada quando é acordada via `notify()`. Isso economiza recursos e evita travamentos.

---

# 4. Conclusão

O desenvolvimento deste trabalho permitiu a aplicação prática de conceitos de programação concorrente. A evolução do código demonstrou como a falta de sincronização compromete a integridade dos dados e como mecanismos como `synchronized`, `wait/notify` e `Semaphore` são essenciais para garantir **robustez**, **consistência** e **eficiência** em sistemas multi-thread, como o sistema de vendas da AMATUR.
