import Exceptions.InvalidPosicion;

import java.util.ArrayList;

public class Joc {

    private ArrayList<Peses> totalPeses = new ArrayList<>();
    private ArrayList<Peses> tablero = new ArrayList<>();

    public void generarPeses(){
        for (int i = 0; i <= 6; i++){
            for (int j = i; j <= 6; j++){
                totalPeses.add(new Peses(i,j));
            }
        }
    }

    public void imprimirTablero(){
        System.out.println(totalPeses);
    }

    public void generarMans(){
        for (int i = 0; i <totalPeses.size(); i++){

        }
    }


    private void cDevant(Peses p){
        if (esPesaValida(p)) {
        tablero.add( 0 ,p); } else {
            new InvalidPosicion("La Fitxa no es pot colocar alla ");
        }
    }


    private void cDerrere(Peses p){
        if (esPesaValida(p)) {
            tablero.add(p);
        } else {
            new InvalidPosicion("La Fitca no es pot colocar alla ");
        }
    }

    private boolean esPesaValida(Peses p) {
        if (tablero.isEmpty()) {
            return true; // Si el tablero está vacío, puedes colocar cualquier pieza
        }

        Peses primera = tablero.get(0);
        Peses ultima = tablero.get(tablero.size() - 1);

        // Compara los extremos del tablero con los lados de la pieza
        return p.getValor1() == primera.getValor1() ||
                p.getValor2() == primera.getValor1() ||
                p.getValor1() == ultima.getValor2() ||
                p.getValor2() == ultima.getValor2();
    }

}
