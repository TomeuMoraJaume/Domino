public class Peses {

    private int valor1;
    private int valor2;
    private boolean girada = false;

    public Peses(int valor1, int valor2) {
         this.valor1 = valor1;
         this.valor2 = valor2;
    }

    @Override
    public String toString() {
        if (girada) {
            return "[" + valor1 + " | " + valor2 + "]";
        } else {
            return "[" + valor1 + " | " + valor2 + "]";}
    }

    public int getValor1() {
        return valor1;
    }
    public int getValor2() {
        return valor2;
    }

    public void setGirada(boolean girada) {
        if (girada) {
            int temp = valor1;
            valor1 = valor2;
            valor2 = temp;
        }
        this.girada = girada;
    }

}