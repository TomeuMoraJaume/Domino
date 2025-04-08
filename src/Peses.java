import java.util.ArrayList;

public class Peses {

    private int valor1;
    private int valor2;
    private boolean adalt = true;
    private boolean abaix = true;
    public Peses(int valor1, int valor2) {
         this.valor1 = valor1;
         this.valor2 = valor2;
    }

    @Override
    public String toString() {
        return "[" + valor1 + " | " + valor2 + "]";
    }
    public String toStringGirat() {
        return "[" + valor2 + " | " + valor1 + "]";
    }

    public int getValor1() {
        return valor1;
    }


    public int getValor2() {
        return valor2;
    }

}
