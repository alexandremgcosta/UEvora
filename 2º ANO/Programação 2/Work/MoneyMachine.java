import java.math.*;
import java.util.*;

/*
 * Class MoneyMachine que é subclass de ElementarMachine<T>
 * deixando a list de ser do tipo generico e passa a ser do
 * tipo Float
 * Para além dos métodos solicitados pelo professor foram
 * criados os métodos:
 *  - public void efetuaPagamento(Float m)
 *  - public void calculaTroco(Float t)
 */

public class MoneyMachine extends ElementarMachine<Float> {

    protected List<Element<Float>> elementos;

    public MoneyMachine() {
        super();
    }

    public float getTotalValue() {
        elementos = getList();
        Float total = 0f;
        for (Element<Float> e : elementos) {
            total = total + (e.getCount() * e.getThing());
        }
        return total;
    }

    public void addMoney(int n, Float x) {
        addThings(n, x);
    }

    public void efetuaPagamento(Float m) {
        BigDecimal bg = new BigDecimal(m * 100);
        BigDecimal tipo_moedas[] = { new BigDecimal(200), new BigDecimal(100), new BigDecimal(50), new BigDecimal(20),
                new BigDecimal(10), new BigDecimal(5), new BigDecimal(2), new BigDecimal(1) };
        int indice = 0;
        BigDecimal quant = BigDecimal.valueOf(0);

        while (bg.intValue() != 0) {
            quant = bg.divide(tipo_moedas[indice]);
            if (quant.intValue() > 0) {
                addMoney(quant.intValue(), tipo_moedas[indice].floatValue() / 100);
                bg = bg.remainder(tipo_moedas[indice]);
            }
            indice++;
        }
    }

    public void calculaTroco(Float troco) {

        elementos = ordenarMoedas();
        ElementarMachine<Float> moedas_devolvidas = new ElementarMachine<Float>();
        List<Integer> n_moedas = new ArrayList<Integer>();
        List<BigDecimal> moedas = new ArrayList<BigDecimal>();
        int indice = 0;
        BigDecimal t = new BigDecimal(troco * 100);

        t = t.setScale(0, RoundingMode.HALF_UP);

        for (Element<Float> e : elementos) {
            moedas.add(new BigDecimal(e.getThing() * 100));
            n_moedas.add(e.getCount());
        }

        while (t.intValue() > 0 && getTotalValue() > 0) {
            if (t.intValue() >= moedas.get(indice).intValue()) {
                n_moedas.set(indice, n_moedas.get(indice) - 1);
                moedas_devolvidas.addThings(1, moedas.get(indice).floatValue() / 100);
                removeOneThing(moedas.get(indice).floatValue() / 100);
                t = t.subtract(moedas.get(indice));
                if (n_moedas.get(indice) == 0) {
                    moedas.remove(indice);
                    n_moedas.remove(indice);
                }
            } else {
                indice++;
            }
            if (getTotalValue() == 0) {
                System.out.println("Não existem moedas suficientes");
                System.out.println("para devolver todo o troco!");
                break;
            }
        }
        System.out.println();
        System.out.println("******* Moedas devolvidas *******");
        moedas_devolvidas.listAll();
        System.out.println("        *****************        ");
        System.out.println();
    }

    public List<Element<Float>> ordenarMoedas() {
        elementos = getList();
        Collections.sort(elementos, new Comparator<Element<Float>>() {
            @Override
            public int compare(Element<Float> arg0, Element<Float> arg1) {
                Float f1 = Float.valueOf(arg0.getThing());
                Float f2 = Float.valueOf(arg1.getThing());
                return f2.compareTo(f1);
            }
        });
        return elementos;
    }
}
