import Exceptions.InvalidPosicion;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Joc {

    private ArrayList<Peses> totalPeses = new ArrayList<>();
    private ArrayList<Peses> tablero = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    protected Sortides sort = new Sortides();
    private ArrayList<jugador> listaJugadors = new ArrayList<>();
    private ArrayList<jugador> lisytaGrups = new ArrayList<>();
    public Joc(){
    }
    public  void start(){
        sort.imprimirTexte("hola");
    }

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

        for (int i = 0; i < listaJugadors.size(); i++) {
            jugador jugadorActual = listaJugadors.get(i);
            for (int y = 0; y < 7 ; y++){
                Random r = new Random();
                int lon = totalPeses.size();
                int rPosicio = r.nextInt(lon);
                Peses pesaActual = totalPeses.get(rPosicio);
                jugadorActual.setMano(pesaActual);
                totalPeses.remove(rPosicio);
            }
        }
    }


    public void imprimirPersones(){
        for (int i = 0; i < listaJugadors.size(); i++) {
            listaJugadors.get(i).mostrar();
        }
    }

    private void cDerrere(Peses p){
        if (esPesaValidaD(p) == 1) {
        tablero.add( 0 ,p); } else if (esPesaValidaD(p) == 2) {
            tablero.add( 0 ,p.setGirada(true));
        } else if (esPesaValidaD(p) == 0) {
            new InvalidPosicion("La Fitca no es pot colocar alla ");
        }
    }


    private void cDevant(Peses p){
        if (esPesaValidaE(p) == 1) {
            tablero.add(p); } else if (esPesaValidaE(p) == 2) {
            tablero.add(p.setGirada(true));
        } else if (esPesaValidaE(p) == 0) {
            new InvalidPosicion("La Fitca no es pot colocar alla ");
        }
    }

    private int esPesaValidaE(Peses p) {
        if (tablero.isEmpty()) {
            return 1;
        }

        Peses primera = tablero.get(0);

        if (p.getValor1() == primera.getValor1()) {
                return 1;
                } else if (p.getValor2() == primera.getValor1()) {
                return 2;
        }
        return 0;
    }

    private int esPesaValidaD(Peses p) {
        if (tablero.isEmpty()) {
            return 1;
        }

        Peses ultima = tablero.get(tablero.size() - 1);

        if (p.getValor1() == ultima.getValor2()) {
            return 1;
        } else if (p.getValor2() == ultima.getValor2()) {
            return 2;
        }
        return 0;
    }

    private int esPotColocar(Peses p) {
        if (tablero.isEmpty()) {
            return 0;
        }
        if(esPesaValidaD(p) == 1 || esPesaValidaE(p) == 1) {
            return 1;
        } else if (esPesaValidaD(p) == 2 || esPesaValidaE(p) == 2) {
            return 2;
        }
        return 0;
    }

    public void startJoc(){
    sort.imprimirTexte(" Benvolguts al Joc del Domino \n En aquest joc temim 7 modalitats de joc , heuras de triarne una per començar a jugar.");
    sort.imprimirTexte(" 1. Domino Standart \n 2. Domino Mexicà \n 3. Domino Llatí \n 4. Domino Colombiá \n 5. Domino Xilè \n 6. Domino Veneçolà \n 7. Domino Ponce");
        switch (sc.nextLine()){
            case "1":
                    start();
                    generarPeses();
                    generarJugadors();
                for (int i = 0; i < listaJugadors.size(); i++) {
                    System.out.println(listaJugadors.get(i).getNombre());
                }
                    generarMans();
                imprimirPersones();
                break;
            case "2":
                sort.imprimirTexte("Has elejit Domino Mexicà are començarem el joc");
                new Mexica().start();
                break;
            case "3":
                sort.imprimirTexte("Has elejit Domino Llatí are començarem el joc");
                new Llati().start();
                break;
            case "4":
                sort.imprimirTexte("Has elejit Domino Colombià are començarem el joc");
                new Colombia().start();
                break;
            case "5":
                sort.imprimirTexte("Has elejit Domino Xilè are començarem el joc");
                new Xile().start();
                break;
            case "6":
                sort.imprimirTexte("Has elejit Domino Veneçolà are començarem el joc");
                new Venesola();
                break;
            case "7":
                sort.imprimirTexte("Has elejit Domino Ponce are començarem el joc");
                new Ponce();
                break;
            default:
                sort.imprimirTexte("Has elejit Un valor no valid");
                break;
        }
    }


    public void generarJugadors(){
        sort.imprimirTexte("Inseresqui cuants de Jugadors sereu (Maxim 4): ");
        int jugadors = sc.nextInt();
        sc.nextLine();
        if (jugadors > 4 ){
            System.out.println("Per Respectar les normes del joc nomes podeu jugar 4 persones");

        } else {
        for (int i = 0; i < jugadors; i++) {
            sort.imprimirTexte("Ingresi el nom del jugador " + i + ": ");
            String nombre = sc.nextLine();

            jugador idJugador = new jugador();
            idJugador.setId(i);
            idJugador.setNombre(nombre);

            listaJugadors.add(idJugador);
        }}
    }

    public void generarParelles(){
        sort.imprimirTexte("Cuants de grups sareu");
        int grups = sc.nextInt();
        if (listaJugadors.size()/grups !=0 ) {
            sort.imprimirTexte("Imposible generar aquest numero de grups , tens menys participants dels que necesites per formar  " + grups + " grups. ");

        } else {
            for (int i = 0; i < listaJugadors.size(); i++) {
                Random r = new Random(listaJugadors.size());
                lisytaGrups.add(listaJugadors.get(i));
                listaJugadors.remove(i);
            }
        }
    }

    public void torns(){
        boolean acabat = false;
        while (!acabat) {



        }
    }

}