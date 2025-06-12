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
    ArrayList<jugador> torns = new ArrayList<>();
    public Joc(){
    }
    public void start(){

    }
    public void iniciarPartida() {
        int primer = cercarJugadorambMajorDoblePessa();

        if (primer != -1) {
            jugador jugadorInicial = listaJugadors.get(primer);

            // Buscar el doble más alto y colocarlo
            ArrayList<Peses> mano = jugadorInicial.getMano();
            for (Peses p : mano) {
                if (p.getValor1() == p.getValor2()) {
                    tablero.add(p); // colocar al centro
                    jugadorInicial.removePesa(p);
                    System.out.println(jugadorInicial.getNombre() + " comença i col·loca automàticament el " + p);
                    break;
                }
            }

            // Generar el orden de turnos (jugador que inició va al final)
            torns(); // ya coloca el jugador con doble primero

            // Mover el jugador que colocó la ficha al final de la lista de turnos
            jugador iniciador = torns.remove(0);
            torns.add(iniciador);
        }

        // Empieza la partida
        jugar();
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

    private void cDerrere(Peses p) {
        int valid = esPesaValidaD(p);
        if (valid == 1) {
            tablero.add(p);
        } else if (valid == 2) {
            p.setGirada(true);
            tablero.add(p);
        } else {
            System.out.println("La fitxa no es pot col·locar al final.");
        }
    }

    private void cDevant(Peses p) {
        int valid = esPesaValidaE(p);
        if (valid == 1) {
            tablero.add(0, p);
        } else if (valid == 2) {
            p.setGirada(true);
            tablero.add(0, p);
        } else {
            System.out.println("La fitxa no es pot col·locar al davant.");
        }
    }

    private int esPesaValidaE(Peses p) {
        if (tablero.isEmpty()) {
            return 1;
        }

        Peses primera = tablero.get(0);

        if (p.getValor2() == primera.getValor1()) {
            return 1;
        } else if (p.getValor1() == primera.getValor1()) {
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
            return 0; // se puede colocar en cualquier lado
        }

        int dreta = esPesaValidaD(p);
        int esquerra = esPesaValidaE(p);

        if (dreta > 0 && esquerra > 0) return 3; // se puede colocar en ambos
        else if (dreta > 0) return 1; // derecha
        else if (esquerra > 0) return 2; // izquierda
        return 0; // no se puede colocar
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
                torns();
                iniciarPartida();
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
        sort.imprimirTexte("Cuants de grups sareu (Maxim 2 ):");
        int grups = sc.nextInt();
        sc.nextLine();
        if (grups > 2 ){
            System.out.println("Per Respectar les normes nomes podeu ser 2 grups");
        } else if (listaJugadors.size()/grups !=0 ) {
            sort.imprimirTexte("Imposible generar aquest numero de grups , tens menys participants dels que necesites per formar  " + grups + " grups. ");
        } else {
            for (int i = 0; i < listaJugadors.size(); i++) {
                Random r = new Random(listaJugadors.size());
                lisytaGrups.add(listaJugadors.get(i));
                listaJugadors.remove(i);
            }
        }
    }

    public void torns() {
        int pesadoblle6 = cercarJugadorambMajorDoblePessa(); // índice del jugador con el doble más alto
        if (pesadoblle6 != -1) {
            // Añadir primero el jugador con el doble más alto
            torns.add(listaJugadors.get(pesadoblle6));
            listaJugadors.remove(pesadoblle6);
        }

        // Añadir el resto de jugadores en el orden que quedaron
        for (jugador j : listaJugadors) {
            torns.add(j);
        }

//        // Mostrar el orden de turnos
//        for (jugador jugador : torns) {
//            jugador.mostrar();
//        }
    }


    private int cercarJugadorambMajorDoblePessa() {
        int valorMaxTrobat = -1;
        int indexJugador = -1;

        for (int i = 0; i < listaJugadors.size(); i++) {
            jugador jActual = listaJugadors.get(i);
            ArrayList<Peses> manoJA = jActual.getMano();
            for (Peses p : manoJA) {
                if (p.getValor1() == p.getValor2() && p.getValor1() > valorMaxTrobat) {
                    valorMaxTrobat = p.getValor1();
                    indexJugador = i;
                }
            }
        }

        return indexJugador;
    }
    public void jugar() {
        boolean jocAcabat = false;

        while (!jocAcabat) {
            for (jugador j : torns) {
                System.out.println("\nTorn de: " + j.getNombre());
                System.out.println("Tauler: " + tablero);
                j.mostrar();

                boolean haJugat = false;

                System.out.println("Escriu l'índex de la peça que vols jugar:");
                int index = sc.nextInt();
                sc.nextLine();

                if (index < j.getMano().size()) {
                    Peses pesaSeleccionada = j.getMano().get(index);
                    int potColocar = esPotColocar(pesaSeleccionada);

                    switch (potColocar) {
                        case 1:
                            cDerrere(pesaSeleccionada);
                            j.removePesa(pesaSeleccionada);
                            haJugat = true;
                            System.out.println(j.getNombre() + " ha col·locat: " + pesaSeleccionada);
                            break;
                        case 2:
                            cDevant(pesaSeleccionada);
                            j.removePesa(pesaSeleccionada);
                            haJugat = true;
                            System.out.println(j.getNombre() + " ha col·locat: " + pesaSeleccionada);
                            break;
                        case 3:
                            System.out.println("Pots col·locar la fitxa a esquerra o dreta. Escriu 'E' o 'D':");
                            String costat = sc.nextLine();
                            if (costat.equalsIgnoreCase("E")) {
                                cDevant(pesaSeleccionada);
                            } else {
                                cDerrere(pesaSeleccionada);
                            }
                            j.removePesa(pesaSeleccionada);
                            haJugat = true;
                            System.out.println(j.getNombre() + " ha col·locat: " + pesaSeleccionada);
                            break;
                        default:
                            System.out.println("La fitxa no es pot col·locar.");
                            break;
                    }
                }

                // Si no ha pogut jugar
                while (!haJugat) {
                    if (totalPeses.isEmpty()) {
                        System.out.println("No hi ha fitxes per robar. " + j.getNombre() + " passa.");
                        break;
                    } else {
                        System.out.println(j.getNombre() + " roba una fitxa.");
                        Peses nova = totalPeses.remove(0);
                        j.setMano(nova);
                        System.out.println(j.getNombre() + " ha robat: " + nova + ". No pot jugar-la aquest torn.");
                        break;
                    }
                }

                // Verifica si el jugador ha ganado
                if (j.getMano().isEmpty()) {
                    System.out.println("\n🏆 El jugador " + j.getNombre() + " ha guanyat!");
                    jocAcabat = true;
                    break;
                }
            }
        }
    }

}
