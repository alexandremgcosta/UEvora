#ifndef PROCESSOS_H
#define PROCESSOS_H

/*
Define os estados de um processo
*/
#define NAOCRIADO -1
#define NEW 0
#define READY 1
#define RUNNING 2
#define BLOCKED 3
#define EXIT 4
#define TERMINADO 5

typedef struct processo
{
    int id;               
    int estado;       
    int instante_chegada;
    int instrucoes[7];
    int tempo_estado_atual;
    int tempo_para_saida;
    int instrucao_atual;
    int instante_de_saida;
} Processo;

int existe_processo_running(Processo lista_processos[], int num_processos);
void verificar_se_exit(Processo lista_processos[], int num_processos, int instante);
int Existem_Processos(Processo lista_processos[], int num_processos);

#endif