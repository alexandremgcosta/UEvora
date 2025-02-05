
/*
 * Class NonPerishable que é uma subclass de Product
 */

public class NonPerishable extends Product {

    protected double volume;

    public NonPerishable(String name, double cost, double volume) {
        super(name, cost);
        this.volume = volume;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
