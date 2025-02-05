import java.io.Serializable;
import java.util.*;

/**
 * Class ElementarMachine<T> com implementação de
 * Serializable para que os objetos possam ser guardados.
 * 
 * Para alem dos métodos pedidos pelo professor foram
 * criados os seguintes métodos:
 * - public boolean thingExists(String elem, String thing)
 * - public List<Element<T>> getList()
 */

public class ElementarMachine<T> implements Serializable {

    protected List<Element<T>> elementos;

    public ElementarMachine() {
        elementos = new ArrayList<Element<T>>();
    }

    public void addThings(int n, T thing) {
        boolean thing_alterada = false;
        Element<T> element_aux;

        for (Element<T> e : elementos) {

            if (thingExists(e.toString(), thing.toString())) {
                int aux = e.getCount();
                aux += n;
                e.setCount(aux);
                thing_alterada = true;
            }
        }
        if (thing_alterada == false) {
            element_aux = new Element<T>(n, thing);
            elementos.add(element_aux);
        }
    }

    public boolean removeOneThing(T thing) {
        for (Element<T> ex : elementos) {
            if (thingExists(ex.toString(), thing.toString())) {
                int aux = ex.getCount();
                aux -= 1;
                if (aux == 0) {
                    elementos.remove(ex);
                } else {
                    ex.setCount(aux);
                }
                return true;
            }
        }
        return false;
    }

    public void listAll() {
        for (Element<T> e : elementos) {
            System.out.println(e.toString());
        }
    }

    /*
     * Metodo auxiliar criado para ajudar a saber se uma "thing" 
     * já existe na lista
     */
    public boolean thingExists(String elem, String thing) {
        char colchete = '[';
        int indexcolchete = 0;
        for (int i = 0; i < elem.length(); i++) {
            if (colchete == elem.charAt(i)) {
                indexcolchete = i;
            }
        }
        elem = elem.substring(indexcolchete, elem.length());
        elem = elem.substring(elem.indexOf("=") + 1, elem.indexOf(",")).toLowerCase();

        if (thing.contains("[") && thing.contains("=") && thing.contains(",")) {
            for (int i = 0; i < thing.length(); i++) {
                if (colchete == thing.charAt(i)) {
                    indexcolchete = i;
                }
            }
            thing = thing.substring(indexcolchete, thing.length());
            thing = thing.substring(thing.indexOf("=") + 1, thing.indexOf(",")).toLowerCase();
        }
        if (elem.equals(thing)) {
            return true;
        }
        return false;
    }

    public List<Element<T>> getList() {
        return elementos;
    }
}