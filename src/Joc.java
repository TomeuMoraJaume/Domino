import Exceptions.InvalidPosicion;

import java.util.ArrayList;
import java.util.Random;

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
            Random r = new Random();
            int rPosicio = r.nextInt(totalPeses.size());

        }
    }


    private void cDevant(Peses p){
        if (esPesaValidaD(p) == 1) {
        tablero.add( 0 ,p); } else if (esPesaValidaD(p) == 2) {
            tablero.add( 0 ,p.setGirada(true));
        } else if (esPesaValidaD(p) == 0) {
            new InvalidPosicion("La Fitca no es pot colocar alla ");
        }
    }


    private void cDerrere(Peses p){
        if (esPesaValidaE(p) == 1) {
            tablero.add( 0 ,p); } else if (esPesaValidaE(p) == 2) {
            tablero.add( 0 ,p.setGirada(true));
        } else if (esPesaValidaE(p) == 0) {
            new InvalidPosicion("La Fitca no es pot colocar alla ");
        }
    }

    private int esPesaValidaD(Peses p) {
        if (tablero.isEmpty()) {
            return 1; // Si el tablero está vacío, puedes colocar cualquier pieza
        }

        Peses primera = tablero.get(0);

        // Compara los extremos del tablero con los lados de la pieza
        if (p.getValor1() == primera.getValor1()) {
                return 1;
                } else if (p.getValor2() == primera.getValor1()) {
                return 2;
        }
        return 0;
    }

    private int esPesaValidaE(Peses p) {
        if (tablero.isEmpty()) {
            return 1; // Si el tablero está vacío, puedes colocar cualquier pieza
        }

        Peses ultima = tablero.get(tablero.size() - 1);

        // Compara los extremos del tablero con los lados de la pieza
        if (p.getValor1() == ultima.getValor2()) {
            return 1;
        } else if (p.getValor2() == ultima.getValor2()) {
            return 2;
        }
        return 0;
    }

}
