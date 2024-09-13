#include "list.h"
#include <stdbool.h>
#include <stdlib.h>
#include "fatal.h"


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
    L=(List)malloc(sizeof(struct Node));
    L->Element = Header;
    L->Next = NULL;
    return L;
}

int IsEmpty( List L ){
    return L->Next == NULL;
}

int IsLast( Position P, List L ){
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
    return L->Next;   //primeiro nó
}


Position Advance( Position P ) {
    return P->Next;
}


ElementType Retrieve( Position P ) {
    return P->Element;
}

List q3(List L, int n, int m, int k){
    List X = CreateList(NULL);
    Position Px = Header(X);
    Position Pl = First(L);
    int i;
    for(i=0; i<n; i++){
        if(Pl==NULL)
            return X;
        else Pl=Advance(Pl);
    }

    while(Pl != NULL && i<m){
        Insert(Pl->Element,X,Px);
        Px= Advance(Px);

        for(int p=0; p<k; p++){
            if(Pl!= NULL){
                Pl= Advance(Pl);
                i++;
            } else{
                return X;
            }
        }
    }
    return X;
}

int main(){
    List L =CreateList(NULL);

    for(int i=9;i>=1;i--){
        Insert(i, L, Header(L));
    } 

    Position P;
    P = L->Next;

    while(P != NULL ){
        printf(" - %d - ", P->Element);
        P = P->Next;
    }

    printf("\n");
    
    List X=  q3(L, 2, 7, 1);

    Position Q;
    Q = X->Next;

    while(Q != NULL ){
        printf(" * %d * ", Q->Element);
        Q = Q->Next;
    }

}