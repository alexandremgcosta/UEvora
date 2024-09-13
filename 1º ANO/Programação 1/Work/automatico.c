
#include <stdio.h>
#include <stdlib.h>
#include <string.h>     //Include necessário para manipular strings

#include "2048.h"

int main()
{
    FILE *fch;      //Nome da variavel ponteiro para o ficheiro

    char tamgrelhas, c;
    int sz, lin, col, convert, pecascombinadas, totalcombinadas;

    fch = fopen("ficheiro.txt", "r");  //Abertura do ficheiro somento para leitura

    if (fch == NULL)
    {
        printf("O arquivo não foi aberto.\n");
    }

    //Guarda o primeiro caracter do ficheiro (Tamanho da grelha)
    tamgrelhas = getc(fch);
    sz = tamgrelhas-'0';

    //Grelha de tabuleira declarada
    int grelha[sz][sz];

    //Le o proximo caracter em branco
    c=getc(fch);

    //Coloca os numeros do ficheiro nas posições da grelha
    for (lin=0; lin<sz; lin++)
    {
        for(col=0; col<sz; col++)
        {
            while (c != '4' && c != '2')
            {
                c= getc(fch);
            }
                convert = c- '0';
                grelha[lin][col]= convert;
                c= getc(fch);
        }
    }

    pecascombinadas = 0;    //Guarda o return das funções baixo, cima, direita, esquerda
    totalcombinadas = 0;    //Guarda a soma das peças combinadas
    while (c != EOF)
    {
        if (c!= ' ' && c!= '\n')
        {
            switch(c)
            {
                case 'B':
                case 'b':
                    pecascombinadas = baixo(sz,grelha);
                    break;
                case 'C':
                case 'c':
                    pecascombinadas = cima(sz,grelha);
                    break;
                case 'D':
                case 'd':
                    pecascombinadas = direita(sz,grelha);
                    break;
                case 'E':
                case 'e':
                    pecascombinadas = esquerda(sz,grelha);
                    break;
                default:
                    break;

            }
                totalcombinadas += pecascombinadas;
        }
            c= getc(fch); // Le proximo caractere
    }

    Fim_do_Jogo(sz,grelha,totalcombinadas);     //Função faz print das peças combinadas e da contagem das peças

    fclose(fch);        //Fecha o arquivo

    return 0;
}

