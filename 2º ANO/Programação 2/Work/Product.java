import java.io.Serializable;

/**
 * Class Product com implementação de Serializable
 * para que os objetos possam ser guardados num ficheiro.
 * 
 */

public abstract class Product implements Serializable {

    protected String name;
    protected double cost;

    public Product(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", cost=" + cost + "]";
    }
}