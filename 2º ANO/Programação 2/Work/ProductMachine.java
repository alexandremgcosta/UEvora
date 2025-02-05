import java.util.*;

/*
 * Class ProductMachine que é subclass de ElementarMachine<T>
 * deixando a list de ser do tipo generico e passa a ser do
 * tipo Product.
 * 
 */

public class ProductMachine extends ElementarMachine<Product> {

    protected List<Element<Product>> elementos;

    public ProductMachine() {
        super();
    }

    public void addProduct(int n, Product x) {
        addThings(n, x);
    }

    public boolean hasProduct(Product x) {
        elementos = getList();
        boolean existe = false;

        for (Element<Product> e : elementos) {
            if (thingExists(e.toString(), x.toString())) {
                existe = true;
            }
        }
        return existe;
    }

    public void listAllOrdered() {
        elementos = getList();

        Collections.sort(elementos, new Comparator<Element<Product>>() {

            @Override
            public int compare(Element<Product> arg0, Element<Product> arg1) {

                return (int) (arg0.getThing().getCost() - arg1.getThing().getCost());
            }

        });
        for (Element<Product> e : elementos) {
            System.out.println(e.toString());
        }
    }
}
