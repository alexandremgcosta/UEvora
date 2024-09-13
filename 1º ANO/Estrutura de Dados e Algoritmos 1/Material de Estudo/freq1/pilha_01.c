#include <stdio.h>
#include <stdlib.h>
#define TAMANHO_PILHA 100

struct pilha
{
    int topo;
    int itens[TAMANHO_PILHA];
};

int empty(struct pilha*p){
    if(p->topo == -1){
        return 1;
    } else {
        return 0;
    }
}

int pop(struct pilha *p){
    if(empty(p)){
        printf("\nPilha vazia");
        exit(1);
    }
    return (p->itens[p->topo--]);
}

void push(struct pilha *p, int e ){
    if (p->topo == (TAMANHO_PILHA - 1)){
        printf("\nEstouro da pilha");
        exit(1);
    }
    p->itens[++(p->topo)] = e;

    return;
}

int size(struct pilha *p){
    return p->topo+1;
}

int stackpop(struct pilha *p){
    return p->itens[p->topo];
}

int main(void){

    struct pilha x;
    x.topo = -1;



    printf("\nTamanho da pilha: %d", size(&x));
    printf("\nElemento do topo da pilha: %d", stackpop(&x));

    printf("\n%d", pop(&x));
    printf("\n%d", pop(&x));
    printf("\n%d", pop(&x));
    printf("\n%d", pop(&x));

    return 0;
}

