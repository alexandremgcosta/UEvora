import java.io.*;

/*
 * Class VendingMachine que implementa Serializable
 * para que nesta class uma VendingMachine possa ser
 * guardada e lida de um ficheiro.
 * Para alem dos métodos pedidos pelo professor, está 
 * a ser feito aqui o método de compra de Products.
 */

public class VendingMachine implements Serializable {

    private ProductMachine pm;
    private MoneyMachine mm;

    public VendingMachine(ProductMachine pm, MoneyMachine mm) {
        this.pm = pm;
        this.mm = mm;
    }

    public MoneyMachine getMoneyMachine() {
        return mm;
    }

    public void setMoneyMachine(MoneyMachine mm) {
        this.mm = mm;
    }

    public ProductMachine getProductMachine() {
        return pm;
    }

    public void setProductMachine(ProductMachine pm) {
        this.pm = pm;
    }

    public static void saveMachine(VendingMachine vm, String Filename) throws IOException {
        File f = new File(Filename);
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream outObject = new ObjectOutputStream(fos);
        outObject.writeObject(vm);
        outObject.close();
    }

    public static VendingMachine restoreMachine(String Filename) throws IOException, ClassNotFoundException {
        File f = new File(Filename);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream inObject = new ObjectInputStream(fis);
        VendingMachine vmachine = (VendingMachine) inObject.readObject();
        inObject.close();
        return vmachine;
    }

    // Comprar
    public boolean comprarProduct(Product x, Float dinheiro, ProductMachine pm, MoneyMachine mm) {
        float precoproduto = (float) x.getCost();

        if (pm.hasProduct(x)) {
            if (dinheiro >= precoproduto) {
                if (dinheiro == precoproduto) {
                    mm.efetuaPagamento(dinheiro);
                    pm.removeOneThing(x);
                } else {
                    mm.efetuaPagamento(dinheiro);
                    pm.removeOneThing(x);
                    mm.calculaTroco(dinheiro - precoproduto);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
