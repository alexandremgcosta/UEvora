#include "abp.h"
#include <stdlib.h>
#include "fatal.h"

#define COUNT 10

struct TreeNode{
    ElementType Element;
    SearchTree  Left;
    SearchTree  Right;
};


SearchTree CreateTree(ElementType element)
{
    SearchTree T = malloc(sizeof(struct TreeNode));
    T->Element = element;
    T->Left = NULL;
    T->Right = NULL;
    return T;
}


SearchTree MakeEmpty( SearchTree T ){
    if( T != NULL ){
        MakeEmpty( T->Left );
        MakeEmpty( T->Right );
        free( T );
    }
    return NULL; 
}


Position Find( ElementType X, SearchTree T ){
    if( T == NULL )
        return NULL;

    if( X < T->Element )
        return Find( X, T->Left );

    else
        if( X > T->Element )
            return Find( X, T->Right );
        else
            return T;
}


Position FindMin( SearchTree T ){
    if( T == NULL )
        return NULL;
    else
        if( T->Left == NULL )
            return T;
        else
            return FindMin(T->Left); 
}


Position FindMax( SearchTree T ){
    if( T == NULL )
        return NULL;
    else
        if( T->Right == NULL )
            return T;
        else
            return FindMax( T->Right ); 
}


SearchTree Insert( ElementType X, SearchTree T ){
    if( T == NULL ){
        T = malloc( sizeof( struct TreeNode ) );
        if( T == NULL )
            FatalError( "Out of space!!!" );
        else{
            T->Element = X;
            T->Left = T->Right = NULL;
        }
    }
    else
        if( X < T->Element )
            T->Left = Insert( X, T->Left );
        else
            if( X > T->Element )
                T->Right = Insert( X, T->Right );

    return T; /* Do not forget this line!!*/
}


SearchTree Delete( ElementType X, SearchTree T ){
    Position TmpCell;
    if( T == NULL )
        Error( "Element not found" );
    else
        if( X < T->Element ) /* Go left */
            T->Left = Delete( X, T->Left );
        else
        {
            if( X > T->Element ) /* Go right */
                T->Right = Delete( X, T->Right );
            else /* Found element to be deleted */
            {
                if( (T->Left!=NULL) && (T->Right!=NULL) ) 
                { /* Two children */
                    /* Replace with smallest in right subtree */
                    TmpCell = FindMin( T->Right );
                    T->Element = TmpCell->Element;
                    T->Right = Delete( T->Element, T->Right );
                }
                else
                { /* One or zero children */
                    TmpCell = T;
                    if( T->Left == NULL ) /* Also handles 0 children */
                        T = T->Right;
                    else if( T->Right == NULL )
                        T = T->Left;
                }
            }
        free( TmpCell );
        }
    return T; 
}


ElementType Retrieve( Position P ){
    return P->Element;
}



void PrintInOrder(SearchTree T)
{
    if (T != NULL) {
        PrintInOrder(T->Left);
        printf("%d ", T->Element);
        PrintInOrder(T->Right);
    }
}

void PrintPreOrder( SearchTree T ){
    if(T != NULL){
        printf("%d ", T->Element);
        PrintPreOrder(T->Left);
        PrintPreOrder(T->Right);
    }
}

void PrintPosOrder( SearchTree T ){
    if(T != NULL){
        PrintPosOrder(T->Left);
        PrintPosOrder(T->Right);
        printf("%d ", T->Element);
    }
}

SearchTree q1(SearchTree T){
    if(T!=NULL){
        if (T->Left!=NULL && T->Right==NULL)
            return NULL;
        T->Left = q1(T->Left);
        T->Right = q1(T->Right);
        return T;
    }
    return T;
}

int q3(SearchTree T, int l, int x){
    if(l==x && T!=NULL)
        return 1;
    else
        if(T!=NULL){
            return q3(T->Left, l+1,x) + q3(T->Right,l+1,x);
            }
        else
            return 0;
}

int main()
{
    SearchTree T;
    int toBeInserted[10] = {3,7,4,12,2,20,9,6,10,11};

    for(int i=0 ; i<10 ; i++)
    {
        if(i==0)
            T = CreateTree(toBeInserted[0]);
        else
            Insert(toBeInserted[i], T);
    }

    printf("\n# PRINTS #\n");

    PrintInOrder(T);
    printf("\n");

    PrintPosOrder(T);
    printf("\n");

    PrintPreOrder(T);
    printf("\n");

    printf("\n# PRINTS APOS ALTERACAO #\n");

    printf("\n");

    printf("%d ", q3(T,0,6));


    return 0;
}