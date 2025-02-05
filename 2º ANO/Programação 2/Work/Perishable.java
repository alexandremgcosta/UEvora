import java.util.*;

/*
 * Class Perishable que é uma subclass de Product
 */

public class Perishable extends Product implements Freshness {

    protected Date limitDate;

    public Perishable(String name, double cost, Date datalimite) {
        super(name, cost);
        limitDate = datalimite;
    }

    public boolean isOutDated() {
        Date dataatual = new Date();
        if (limitDate.compareTo(dataatual) < 0) {
            return true;
        }
        return false;
    }

    public boolean isFromToday() {
        Date dataatual = new Date();
        return limitDate.equals(dataatual);
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }
}
