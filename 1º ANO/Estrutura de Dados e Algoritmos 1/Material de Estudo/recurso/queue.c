#include <stdlib.h>
#include <stdbool.h>
#include "queue.h"
#include "fatal.h"


#define MinQueueSize ( 5 )

struct QueueRecord{
    int Capacity;
    int Front;
    int Rear;
    ElementType *Array;
};


/* FUNCOES AUXILIARES */
/* numero de elementos na fila */
int size( Queue Q ){
    return ((Q->Capacity - Q->Front + Q->Rear) % Q->Capacity);
}


/* indice do proximo elemento  */
int successor( int i, Queue Q ){
    if(i == Q->Capacity-1){
        return 0;
    } else {
        return i+1;
    }

    return (i+1)%Q->Capacity;
}



/* FUNCOES DE MANIPULACAO DE QUEUES */
Queue CreateQueue( int MaxElements ){
    Queue Q;

    if( MaxElements < MinQueueSize )
        Error( "Queue size is too small" );

    Q = malloc( sizeof( struct QueueRecord ) );
    if( Q == NULL )
        FatalError( "Out of space!!!" );

    Q->Array = malloc( sizeof( ElementType ) * MaxElements );
    if( Q->Array == NULL )
        FatalError( "Out of space!!!" );

    Q->Capacity = MaxElements+1;
    MakeEmptyQueue( Q );

    return Q;
}


void DisposeQueue( Queue Q ){
    if( Q != NULL ){
        free( Q->Array );
        free( Q );
    }
}


bool IsEmptyQueue( Queue Q ){
    return Q->Front == Q->Rear;
}


bool IsFullQueue( Queue Q ){
    return size(Q) == Q->Capacity - 1;
}


void MakeEmptyQueue( Queue Q ){
    Q->Front = 0;
    Q->Rear = 0;
}


void Enqueue( ElementType X, Queue Q ){
    if(IsFullQueue(Q)){
        printf("Queue is already full.");
        return;
    }

    Q->Array[Q->Rear] = X;
    printf("%d\n",Q->Array[Q->Rear]);
    Q->Array[Q->Rear+=1];
}


ElementType Front( Queue Q ){
    return Q->Array[Q->Front];
}


ElementType Dequeue( Queue Q ){
    if (IsEmptyQueue(Q)) Error("Empty Queue");
    ElementType X = Q->Array[Q->Front];
    Q->Front = successor(Q->Front, Q);
    return X;
}

void q2(int n){
    Queue Q= CreateQueue(5);
    Enqueue(0,Q);
    Enqueue(1,Q);
    int a;
    for(int i=0; i<n; i++){
        a=Dequeue(Q);
        printf("F(%d)=%d \n", i, a);
        Enqueue(Front(Q)+a,Q);
    }
    printf("F(%d)=%d\n", n, Front(Q));
}

int main(){

    q2(5);

    return 0;
}