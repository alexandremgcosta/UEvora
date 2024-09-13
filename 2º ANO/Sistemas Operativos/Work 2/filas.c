#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "processos.h"
#include "filas.h"

/*
Função para criar uma fila vazia.
*/
void cria_fila(Queue *q)
{
    q->inicio = NULL;
    q->fim = NULL;
}

/*
Função para inserir um processo na fila.
*/
void enqueue(Queue *fila, Processo *processo)
{
    Node *novo_no = malloc(sizeof(Node));
    if (novo_no == NULL)
    {
        fprintf(stderr, "Erro de alocação de memória\n");
        exit(EXIT_FAILURE);
    }
    novo_no->process = processo;
    novo_no->proximo = NULL;

    if (fila->inicio == NULL)
    {
        fila->inicio = novo_no;
    }
    else
    {
        fila->fim->proximo = novo_no;
    }
    fila->fim = novo_no;
}

/*
Função para remover o primeiro processo da fila e retorná-lo.
*/
Processo *dequeue(Queue *fila)
{
    if (fila->inicio == NULL)
    {
        return NULL;
    }
    else
    {
        Node *no_removido = fila->inicio;
        Processo *processo = no_removido->process;
        fila->inicio = no_removido->proximo;
        free(no_removido);
        if (fila->inicio == NULL)
        {
            fila->fim = NULL;
        }
        return processo;
    }
}

/*
Verifica se a fila está vazia.
*/
int isEmpty(Queue *fila)
{
    return (fila->inicio == NULL);
}

/*
Função para ter acesso ao primeiro processo da fila sem que seja removido.
*/
Processo *top(Queue *fila)
{
    if (fila->inicio == NULL)
    {
        return NULL;
    }
    else
    {
        return fila->inicio->process;
    }
}

/*
É percorrida a lista de processos e verifica se algum está no estado "RUNNING". Se estiver, é analisado e decidido se passar a "BLOCKED", a "EXIT" ou continua "RUNNING".
*/
void Running_Para_Blocked(Queue *b, Processo lista_processos[], int NUM_PROCESSOS, int instante)
{
    for (int i = 0; i < NUM_PROCESSOS; i++)
    {
        if (lista_processos[i].estado == RUNNING)
        {
            if (lista_processos[i].tempo_estado_atual > 0)
            {
                lista_processos[i].tempo_estado_atual--;
                lista_processos[i].tempo_para_saida--;
            }
            else
            {
                if (lista_processos[i].tempo_para_saida > 0)
                {
                    lista_processos[i].estado = BLOCKED;
                    lista_processos[i].instrucao_atual++;
                    lista_processos[i].tempo_estado_atual = lista_processos[i].instrucoes[lista_processos[i].instrucao_atual];
                    lista_processos[i].tempo_estado_atual--;
                    lista_processos[i].tempo_para_saida--;
                    enqueue(b, &lista_processos[i]);
                }
                else
                {
                    lista_processos[i].estado = EXIT;
                    // lista_processos[i].instante_chegada=instante;
                    lista_processos[i].instante_de_saida = instante;
                }
            }
        }
    }
}

/*
Percorre a lista e verifica se algum processo se encontra no estado "BLOCKED". Se estiver, é então verificado se está em condições de passar a "READY" ou não.
*/
void processo_blocked(Queue *Q_READY, Processo lista_processos[], int NUM_PROCESSOS, int instante)
{
    for (int i = 0; i < NUM_PROCESSOS; i++)
    {
        if (lista_processos[i].estado == BLOCKED)
        {
            if (lista_processos[i].tempo_estado_atual > 0)
            {
                lista_processos[i].tempo_estado_atual--;
                lista_processos[i].tempo_para_saida--;
            }
            else
            {
                lista_processos[i].estado = READY;
                lista_processos[i].instrucao_atual++;
                enqueue(Q_READY, &lista_processos[i]);
            }
        }
    }
}