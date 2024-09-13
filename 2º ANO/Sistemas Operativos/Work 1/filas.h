#ifndef FILAS_H
#define FILAS_H

typedef struct node
{
    Processo *process;
    struct node *proximo;
} Node;

typedef struct
{
    Node *inicio;
    Node *fim;
} Queue;

void cria_fila(Queue *q);
void enqueue(Queue *fila, Processo *processo);
Processo *dequeue(Queue *fila);
int isEmpty(Queue *fila);
Processo *top(Queue *fila);

void Running_Para_Blocked(Queue *b, Processo lista_processos[], int NUM_PROCESSOS, int instante);

void processo_blocked(Queue *Q_READY, Processo lista_processos[], int NUM_PROCESSOS, int instante);

#endif