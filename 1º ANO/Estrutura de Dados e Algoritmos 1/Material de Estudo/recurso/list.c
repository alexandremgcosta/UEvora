#include "list.h"
#include <stdlib.h>
#include "fatal.h"
#include <stdbool.h>


struct Node{
    ElementType Element;
    Position    Next;
};


List CreateList( List L )
{
    if( L != NULL )
        DeleteList( L );
    L = malloc( sizeof( struct Node ) );
    if( L == NULL )
        FatalError( "Out of memory!" );
    L->Next = NULL;
    return L;
}

List MakeEmpty( List L ){
    Position p = Header(L);

    while(p != NULL){
        p = p->Next;
        free(p);
    }
    return L;
}

bool IsEmpty( List L ){
    return L->Next == NULL;
}

bool IsLast( Position P, List L ){
    return P->Next == NULL;
}

Position Find( ElementType X, List L ){
    Position P;

    P = L->Next;  /*Primeiro elemento*/
    while(P != NULL && P->Element != X){
        P = P->Next;
    }
    return P;
}


Position FindPrevious( ElementType X, List L ) {
    /* devolve posicao do elemento anterior
    se nao existir devolve o ultimo elemento
    */
    Position P;
    P = L;

    while(P->Next != NULL && P->Next->Element != X){
        P=P->Next;
    }
    return P;
}


void Insert( ElementType X, List L, Position P ) {
    Position Tmp = malloc(sizeof(struct Node));

    if (Tmp == NULL)
        FatalError("out of space!!!");

    Tmp->Element = X;
    Tmp->Next = P->Next;
    P->Next = Tmp;
}

void Delete( ElementType X, List L ){
    Position P,tmp;

    P = FindPrevious(X,L);

    if(!IsLast(P,L)){  //se elemento existe
        tmp = P->Next;
        P->Next = tmp->Next;
        free(tmp);
    }
}


void DeleteList( List L ) {
    Position P, tmp;

    P = L->Next;
    L->Next = NULL;  /*primeiro elemento*/
    while( P != NULL){
        tmp = P->Next;  /*Proximo elemento*/
        free(P);
        P = tmp;
    }
}


Position Header( List L ) {
     return L;
}


Position First( List L ) {
    return L->Next;
}


Position Advance( Position P ) {
    return P->Next;
}


ElementType Retrieve( Position P ) {
    return P->Element;
}

void PrintList(List L)
{
    Position node = First(L);

    putchar('[');

    while(node != NULL)
    {
        printf(" %d ", node->Element);
        node = node->Next;
    }

    printf("]\n");
}


Position Last( List L )
{
    Position currNode = First(L);

    while(currNode != NULL)
    {
        if(currNode->Next == NULL)
            return currNode;
        else
            currNode = currNode->Next;
    }
    return NULL;
}

List q3(List L, int n, int m, int k){
    List X= CreateList(NULL);
    Position Px = Header(X);
    Position Pl = Header(L);
    int i;
    for(i=0; i<n; i++){
        if(Pl==NULL)
            return X;
        else Pl= Advance(Pl);
    }

    while(Pl!=NULL && i<m){
        Insert(Pl->Element,X, Px);
        Px=Advance(Px);

        for(int p=0; p<k;p++){
            if(Pl!=NULL){
                Pl = Advance(Pl);
                i++;
            }
            else
                return X;
        }
    }

    return X;
}

void q2(List L, int k){
    Position H= Header(L);
    Position X= H;
    for(int i=0; i<k; i++){
        X=Advance(X);
    }
    H->Next = X;
}

int main(){
    List L = CreateList(NULL);
    Insert(10,L,Header(L));
    Insert(2,L,Last(L));
    Insert(7,L,Last(L));
    Insert(33,L,Last(L));
    Insert(19,L,Last(L));
    /*Insert(6,L,Last(L));
    Insert(7,L,Last(L));
    Insert(8,L,Last(L));
    Insert(9,L,Last(L));
    Insert(10,L,Last(L));*/

    PrintList(L);

    q2(L,1);
    PrintList(L);


    return 0;
}