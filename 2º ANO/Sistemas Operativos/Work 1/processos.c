#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "processos.h"
#include "filas.h"

/*
Percorre a lista e verifica se há algum processo com o estado "RUNNING". Se sim, dá return de 1, caso contrário return 0.
*/
int existe_processo_running(Processo lista_processos[], int num_processos)
{
    for (int i = 0; i < num_processos; i++)
    {
        if (lista_processos[i].estado == RUNNING)
        {
            return 1;
        }
    }
    return 0;
}

/*
Percorre a lista de processos e verifica se algum processo está no estado "EXIT" e se já teve um instante no estádo "EXIT". Se verdade, o estado do processo passa para "TERMINADO".*/
void verificar_se_exit(Processo lista_processos[], int num_processos, int instante)
{
    for (int i = 0; i < num_processos; i++)
    {
        if (lista_processos[i].estado == EXIT && (lista_processos[i].instante_de_saida + 1) == instante)
        {
            lista_processos[i].estado = TERMINADO;
        }
    }
}
