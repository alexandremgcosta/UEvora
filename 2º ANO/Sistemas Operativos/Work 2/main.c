#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <limits.h>
#include <stdbool.h>
#include "processos.h"
#include "filas.h"

#define MAX_PROCESSOS 8
#define MAX_INSTRUCOES 7
// É necessário alterar o "NUM_PROCESSOS" dependendo do numero de programas[][]
#define NUM_PROCESSOS 3

int main()
{
    // Criação das filas para READY e BLOCKED
    Queue Q_READY;
    Queue Q_BLOCKED;
    cria_fila(&Q_READY);
    cria_fila(&Q_BLOCKED);

    // Definição dos programas a serem executados pelos processos
    int programas[3][8] = {
        {1, 3, 1, 2, 2, 4, 0, 0},
        {1, 4, 2, 4, 1, 3, 0, 0},
        {3, 2, 1, 6, 1, 3, 0, 0}};

    int id_processos = 0;
    Processo lista_processos[NUM_PROCESSOS];

    // Criação dos processos
    for (int i = 0; i < NUM_PROCESSOS; i++)
    {
        int soma_instrucoes = 0;

        // Atribuição dos valores de cada processo
        lista_processos[i].id = id_processos;
        lista_processos[i].estado = -1;
        lista_processos[i].instante_chegada = programas[i][0];

        // Definição das instruções de cada processo
        for (int j = 0; j < MAX_INSTRUCOES; j++)
        {
            lista_processos[i].instrucoes[j] = programas[i][j + 1];
            soma_instrucoes = soma_instrucoes + programas[i][j + 1];
        }

        // Atribuição dos valores restantes de cada processo
        lista_processos[i].tempo_estado_atual = lista_processos[i].instrucoes[0];
        lista_processos[i].tempo_para_saida = soma_instrucoes;
        lista_processos[i].instrucao_atual = 0;
        lista_processos[i].instante_de_saida = -2;

        id_processos++;
    }

    // Impressão do cabeçalho da tabela
    printf("instante |");
    for (int i = 0; i < NUM_PROCESSOS; i++)
    {
        printf("  proc%d  |", i + 1);
    }
    printf("\n");

    int instante = 0;
    bool terminados = false;

    // Loop principal do programa
    while (!terminados)
    {

        // Inicializa um processo para new
        for (int i = 0; i < NUM_PROCESSOS; i++)
        {
            if (lista_processos[i].instante_chegada == instante)
            {
                lista_processos[i].estado = NEW;
            }
        }

        // Mexe nos processos "BLOCKED"
        processo_blocked(&Q_READY, lista_processos, NUM_PROCESSOS, instante);

        // Mexe nos processos "RUNNING"
        Running_Para_Blocked(&Q_BLOCKED, lista_processos, NUM_PROCESSOS, instante);

        // Verificação se há processo na fila READY e CPU ocioso
        if (!isEmpty(&Q_READY) && existe_processo_running(lista_processos, NUM_PROCESSOS) == 0)
        {
            Processo *processo_removido = dequeue(&Q_READY);
            processo_removido->estado = RUNNING;
            processo_removido->instante_chegada = instante;
            processo_removido->tempo_estado_atual = processo_removido->instrucoes[processo_removido->instrucao_atual];
            Running_Para_Blocked(&Q_BLOCKED, lista_processos, NUM_PROCESSOS, instante);
        }

        // Passa o processo de "NEW" para "READY" e verifica se o CPU está ocioso
        for (int i = 0; i < NUM_PROCESSOS; i++)
        {
            if (lista_processos[i].estado == NEW && lista_processos[i].instante_chegada + 1 == instante)
            {
                lista_processos[i].estado = READY;
                enqueue(&Q_READY, &lista_processos[i]);
            }

            if (isEmpty(&Q_BLOCKED) && existe_processo_running(lista_processos, NUM_PROCESSOS) == 0 && !isEmpty(&Q_READY))
            {
                Processo *processo_removido = dequeue(&Q_READY);
                processo_removido->estado = RUNNING;
                Running_Para_Blocked(&Q_BLOCKED, lista_processos, NUM_PROCESSOS, instante);
            }
        }

        verificar_se_exit(lista_processos, NUM_PROCESSOS, instante);

        // Imprime cada instante
        printf("%8d |", instante);
        for (int i = 0; i < NUM_PROCESSOS; i++)
        {
            if (lista_processos[i].instante_chegada <= instante && lista_processos[i].instante_de_saida <= instante)
            {
                switch (lista_processos[i].estado)
                {
                case NEW:
                    printf("   NEW   |");
                    break;
                case READY:
                    printf("  READY  |");
                    break;
                case RUNNING:
                    printf(" RUNNING |");
                    break;
                case BLOCKED:
                    printf("  BLOCK  |");
                    break;
                case EXIT:
                    printf("   EXIT  |");
                    break;
                case TERMINADO:
                    printf("         |");
                    break;
                }
            }
            else
            {
                printf("         |");
            }
        }
        printf("\n");

        // Verifica se todos os processos terminaram para que o loop principal seja parado
        terminados = true;
        for (int i = 0; i < NUM_PROCESSOS; i++)
        {
            if (lista_processos[i].estado != EXIT && lista_processos[i].estado != TERMINADO)
            {
                terminados = false;
                break;
            }
        }

        // Incrementa o tempo
        instante++;
    }

    return 0;
}