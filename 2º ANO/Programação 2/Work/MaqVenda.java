public class MaqVenda {

    static int numMaquinas;
    static final int MAXPRODS = 100;

    //metodos de classe
    public static void incNumMaquinas(){
        numMaquinas+=1;
    }

    //acede a constante de numero maximo de produtos
    public static int maxProds(){
        return MAXPRODS;
    }

    //consulta o numero de maquinas criadas
    public static int qtMaquinas(){
        return numMaquinas;
    }

    //variaveis de instancia
    private String tipoprod;
    private int totalDinheiro;
    private int numCompras;
    private String estado;
    private TriploMaq[] tab;
    private int numprods;
    
    //construtores
    public MaqVenda(String tprod, TriploMaq tabinic[], int cash, int nprods){
        this.incNumMaquinas();
        tipoprod = tprod;
        totalDinheiro= cash;
        tab=tabinic;
        numprods = (nprods <= MaqVenda.maxProds() ? nprods : MaqVenda.maxProds());
        numCompras=0;
        estado= new String ("OK");
    }

    //metodos de instancia privados

    //devolve o triplo na posicao dada por index
    private TriploMaq daTriplo(int index){
        TriploMaq triplo= (TriploMaq) tab[index].clone();
        return triplo;
    }

    //procura no array o indice do produto
    private int procuraProduto(String prod){
        boolean enc=false;
        int ind = -1;
        while (!enc && ind < numprods) {
            ind++;
            enc = ((this.daTriplo(ind).prod()).equals(prod) ? true : false);
        }
        return (enc ? ind : 0);
    }

    //metodos de instancia
    public String queProduto(){
        return tipoprod;
    }

    public int cash(){
        return totalDinheiro;
    }

    public int ncompras(){
        return numCompras;
    }

    public int numProds(){
        return numprods;
    }

        	            //codigo não concluido pag. 125
}
