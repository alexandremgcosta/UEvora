
#include <stdio.h>
#include <stdlib.h> // Usar a rand()
#include <conio.h>
#include <time.h>   // Necessário para a função rand()
#include <ctype.h>  // Para poder fazer uso da função toupper
#include <math.h>   // Para usar a função do expoente

//Variavel constante com o valor 10
#define TAM_MAX 10

#include "2048.h"

int main()
{
    int sz=0, pecascombinadas=0, combinadas=0, jogadapossivel;
    int grelha[TAM_MAX][TAM_MAX];
    char sentido;

    srand(time(NULL));       //Funcao necessaria para que gere sempre numeros aleatorios diferentes

    sz = InstrucoesDoJogo_TamanhoDaGrelha();

    Inicia_Grelha(sz,grelha);

    Mostrar(sz,grelha);

    while (sentido != 'F')
    {
        printf("Menu: [B]aixo  [C]ima  [D]ireita  [E]squerda  [F]im\n");
        fflush(stdin);      //Limpa o que fica no buffer do teclado da função stdin
        scanf("%c", &sentido);
        sentido = toupper(sentido);     //Função utilizada para colocar o caracter em letra maiuscula
        printf("\n");

        jogadapossivel = Verifica_jogada_possivel(sz,grelha);
        if ( jogadapossivel == 1 )
        {
            pecascombinadas = jogada(sz,grelha,sentido);
            combinadas+=pecascombinadas;
        }
        else
        {
            sentido = 'F';
        }
    }

    Fim_do_Jogo(sz,grelha,combinadas);

    return 0;
}

