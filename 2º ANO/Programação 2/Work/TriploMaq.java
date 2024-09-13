public class TriploMaq {

    //variaveis de instancia
    private String produto;
    private int preco;
    private int quant;
    //

    //constructores
    public TriploMaq(){
        produto = new String("");
        preco=0; quant=0;
    }

    public TriploMaq(String prod, int pr, int qt){
        produto=prod;
        preco=pr; quant=qt;
    }
    // 


    //metodos da instancia
    public String prod(){
        return produto;
    }

    public int preco(){
        return preco;
    }

    public int quant(){
        return quant;
    }

    public Object clone(){
        return new TriploMaq(this.produto, this.quant, this.preco);
    }

    public String toString(){
        return new String("Triplo = "+produto+", "+preco+", "+quant);
    }
    //
}