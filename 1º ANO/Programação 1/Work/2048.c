#include <stdio.h>
#include <stdlib.h> // Usar a rand()
#include <conio.h>  // Para uso da função kbhit
#include <time.h>   // Necessário para a função rand()
#include <ctype.h>  // Para poder fazer uso da função toupper
#include <math.h>   // Para usar a função do expoente
#include "2048.h"

int InstrucoesDoJogo_TamanhoDaGrelha()
{
    int sz=0;

    system("cls");
    printf("\n\n\n");
    printf("\t Instrucoes do modo iterativo: \n\n\n");
    printf("\t\t - A grelha e preenchidax em duas posicoes aleatorias com o numero 2 ou 4. \n\n");
    printf("\t\t - Mova as pecas de forma a criar uma peca com o numero 2048. \n\n");
    printf("\t\t - As pecas podem ser movidas em 4 direcoes: Baixo, Cima, Direita, Esquerda. \n\n\n");

    printf("\t\t\t Pressione qualquer tecla para jogar!");

    getch();
    system("cls");

    printf("\n\n\n");

    printf("\t Indique o tamanho da grelha: ");
    scanf("%d", &sz);

    while (sz<2 || sz>10){
        printf("\t Tamanho invalido!\n");
        printf("\t Tamanho minimo 2 e maximo 10.\n");
        printf("\t Indique o tamanho da grelha: ");
        scanf("%d", &sz);
    }

    system("cls");
    printf("\n\n\n");
    printf("\t\t Grelha %d por %d selecionado. \n\n", sz, sz);

    return sz;
}

void Inicia_Grelha(int sz,int grelha[sz][sz])       //Função para iniciar a grelha e colocar os dois numeros em posições aleatorias
{
    int colunaescolhida, linhaescolhida, escolha;

    for (int c=0; c < sz; c++)
    {
        for (int l=0; l<sz; l++)
        {
            grelha[l][c] = 0;
        }
    }

    escolha = (rand() % 9 == 4) ? 4 : 2;        //Coloca na variavel escolha o numero 2 ou 4

    for (int i=0; i<2; i++)
    {
        colunaescolhida = rand() % sz;

        linhaescolhida = rand() %sz;

        if(grelha[linhaescolhida][colunaescolhida]== 0)
        {
            grelha[linhaescolhida][colunaescolhida]= escolha;
        } else {
            i--;
        }
    }
}

int baixo(int sz, int grelha[sz][sz])       //Executa o movimento para baixo
{
    int c, l, aux, pecascombinadas=0;
    for (l=sz-2; l>=0; l--)
    {
        for (c=0; c<sz; c++)
        {
            if (grelha[l][c]!=0)
            {
                aux = l;
                while((grelha[aux+1][c]== 0 || grelha[aux][c]==grelha[aux+1][c]) && (aux < sz))
                {
                    if ( grelha[aux][c]==grelha[aux+1][c] )
                    {
                        grelha[aux+1][c]+=grelha[aux][c];
                        grelha[aux][c]=0;
                        pecascombinadas++;
                    } else
                    {
                        grelha[aux+1][c] = grelha[aux][c];
                        grelha[aux][c]=0;
                        aux++;
                    }
                }
            }
        }
    }
    return (pecascombinadas);
}

int cima(int sz, int grelha[sz][sz])        //Executa o movimento para cima
{
    int c, l, aux, pecascombinadas=0;
    for (l=1; l<sz; l++)
    {
        for (c=0; c<sz; c++)
        {
            if (grelha[l][c]!=0)
            {
                aux = l;
                while((grelha[aux-1][c]== 0 || grelha[aux][c]==grelha[aux-1][c]) && aux >0)
                {
                    if (grelha[aux][c]==grelha[aux-1][c])
                    {
                        grelha[aux-1][c]+=grelha[aux][c];
                        grelha[aux][c]=0;
                        pecascombinadas++;
                    } else
                    {
                        grelha[aux-1][c] = grelha[aux][c];
                        grelha[aux][c]=0;
                        aux--;
                    }
                }
            }
        }
    }
    return (pecascombinadas);
}

int direita(int sz, int grelha[sz][sz])     //Executa o movimento para a direita
{
    int c, l, aux, pecascombinadas=0;
    for (l=0; l<sz; l++)
    {
        for (c=sz-2; c>=0; c--)
        {
            if (grelha[l][c]!=0)
            {
                aux = c;
                while((grelha[l][aux+1]== 0 || grelha[l][aux]==grelha[l][aux+1]) && aux<sz-1 )
                {
                    if (grelha[l][aux]==grelha[l][aux+1])
                    {
                        grelha[l][aux+1]+=grelha[l][aux];
                        grelha[l][aux]=0;
                        pecascombinadas++;
                    } else
                    {
                        grelha[l][aux+1] = grelha[l][aux];
                        grelha[l][aux]=0;
                        aux++;
                    }
                }
            }
        }
    }
    return (pecascombinadas);
}

int esquerda(int sz, int grelha[sz][sz])        //Executa o movimento para a esquerda
{
    int c, l, aux, pecascombinadas=0;
    for (l=0; l<sz; l++)
    {
        for (c=1; c<sz; c++)
        {
            if(grelha[l][c]!=0)
            {
                aux = c;
                while((grelha[l][aux-1] == 0 || grelha[l][aux]==grelha[l][aux-1] )&& aux > 0)
                {
                    if(grelha[l][aux]==grelha[l][aux-1])
                    {
                        grelha[l][aux-1]+=grelha[l][aux];
                        grelha[l][aux]=0;
                        pecascombinadas++;
                    } else
                    {
                        grelha[l][aux-1] = grelha[l][aux];
                        grelha[l][aux] = 0;
                        aux--;
                    }
                }
            }
        }
    }
    return (pecascombinadas);
}

void Mostrar(int sz,int grelha[sz][sz])     //Mostra a grelha ao utilizador
{
    int c, l;
    for (l=0; l<sz; l++)
    {
        printf("\t\t\t");
        for (c=0; c<sz; c++)
        {
            if (grelha[l][c]==0)        //Substitui os valores iguais a 0 por " - "
            {
                printf("  -   ");
            } else
            {
                printf(" %4d ", grelha[l][c]);
            }
        }
        printf("\n");
    }
    printf("\n");
}

int verifica_espacos_vazios(int sz, int grelha[sz][sz])         //Indica quantos espacos na grelha existem com o valor 0
{
    int l,c, espacosvazios=0;

    for (l=0; l<sz; l++)
    {
        for (c=0; c<sz; c++)
        {
            if (grelha[l][c]==0)
                espacosvazios++;
        }
    }
    return espacosvazios;
}

void gera_novo_numero(int sz, int grelha[sz][sz])
{
    int l, c, numeroaleatorio, numeropossivel=0;        //Variavel numeroaleatorio guarda o valor 2 ou 4. Variavel numeropossivel usada para parar o ciclo while

    numeroaleatorio = (rand() % 9 == 4) ? 4 : 2;

        while (numeropossivel == 0)
        {
            l = rand() % sz-1;
            c = rand() % sz-1;
            if (grelha[l][c]==0)
            {
                grelha[l][c]= numeroaleatorio;
                numeropossivel = 1;
            }
        }
}

int jogada(int sz, int grelha[sz][sz], char sentido)        // Função para efetuar uma jogada e faz as verificações necessárias para saber se é possivel jogar
{

    int pecascombinadas=0, existeespacovazio=0;

    existeespacovazio = verifica_espacos_vazios(sz,grelha);

    switch(sentido)
    {
        case 'B':
        case 'b':
            pecascombinadas = baixo(sz,grelha);
            if (existeespacovazio != 0)             //Apenas gera novo numero caso exista algum espaço vazio na grelha
            {
                gera_novo_numero(sz,grelha);
            }
            Mostrar(sz,grelha);

            break;
        case 'C':
        case 'c':
            pecascombinadas = cima(sz,grelha);
            if (existeespacovazio != 0)             //Apenas gera novo numero caso exista algum espaço vazio na grelha
            {
                gera_novo_numero(sz,grelha);
            }
            Mostrar(sz,grelha);

            break;
        case 'D':
        case 'd':
            pecascombinadas = direita(sz,grelha);
            if (existeespacovazio != 0)             //Apenas gera novo numero caso exista algum espaço vazio na grelha
            {
                gera_novo_numero(sz,grelha);
            }
            Mostrar(sz,grelha);

            break;
        case 'E':
        case 'e':
            pecascombinadas = esquerda(sz,grelha);
            if (existeespacovazio != 0)             //Apenas gera novo numero caso exista algum espaço vazio na grelha
            {
                gera_novo_numero(sz,grelha);
            }
            Mostrar(sz,grelha);

            break;
        case 'F':
        case 'f':

            break;
        default:
            printf("Valor invalido!\n");
            break;
    }
    return (pecascombinadas);
}

int Verifica_jogada_possivel(int sz, int grelha[sz][sz])
{
    int l, c, copiagrelha[sz][sz];

    for (l=0; l<sz; l++)            //Cria uma copia da grelha na variavel copiagrelha
    {
        for (c=0; c<sz; c++)
        {
            copiagrelha[l][c] = grelha[l][c];
        }
    }

    if ( (verifica_espacos_vazios(sz,copiagrelha)==0) && ((baixo(sz,copiagrelha)==0) && (cima(sz,copiagrelha)==0) && (direita(sz,copiagrelha)==0) && (esquerda(sz,copiagrelha)==0)) )
        {
            return 0;           //Nenhum movimento é possivel
        }
    return 1;
}

void Fim_do_Jogo(int sz, int grelha[sz][sz], int combinadas)
{
    int l, c, maiornumero, num=1, contarepetidos;

    maiornumero=0;
    for (l=0; l<sz; l++)        //Coloca o maior numero da grelha na variavel maiornumero
    {
        for (c=0; c<sz; c++)
        {
            if (grelha[l][c]>=maiornumero)
                maiornumero = grelha[l][c];
        }
    }

    // Print do fim do jogo mais o numero de combinaçoes feitas

    printf("\n\n\n");
    printf("\t\t\t\tJogo terminado!\n\n");
    printf("\t\t\t Pecas combinadas: %d\n\n", combinadas);
    printf("\t\t\t Contagem:");

    // Print da Contagem do numero de peças na grelha

    while ( pow(2,num) <= maiornumero )
    {
        contarepetidos=0;
        for (l=0; l<sz; l++)        //Contar o numero de vezes que o numero repete
        {
            for (c=0; c<sz; c++)
            {
            if (grelha[l][c]== pow(2,num))
                contarepetidos++;
            }
        }
        num++;
        printf(" %d ", contarepetidos);
    }
    printf("\n\n\n");
}
