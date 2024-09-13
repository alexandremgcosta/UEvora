int q1(long n){
    long c=n;
    Stack S=CreateStack(20);
    while(n>0){
        Push(n%10,S);
        n=n/10;
    }

    while (!IsEmpty(S)){
        if (Pop(S) != c%10){
            return 0;
        }
        c=c/10;
    }
    return 1;
}