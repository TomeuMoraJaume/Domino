import java.util.*;

public class Venesola extends Joc {

    private final int PUNTAJE_OBJETIVO = 75;

    private ArrayList<ArrayList<jugador>> parejas = new ArrayList<>();

    private int jugadorSalidaAnterior = -1;

    public Venesola() {
        super();
    }

    @Override
    public void start() {
        generarJugadors();
        if (listaJugadors.size() != 4) {
            System.out.println("El dominó venezolano se juega con 4 jugadores en dos parejas.");
            return;
        }
        formarParejas();

        boolean juegoTerminado = false;

        while (!juegoTerminado) {
            resetearManosYTablero();
            repartirFichas();

            int jugadorInicio = encontrarJugadorConDobleSeis();
            if (jugadorInicio == -1) {
                System.out.println("Error: No se encontró el doble seis. Reiniciando reparto...");
                continue;
            }

            jugadorSalidaAnterior = jugadorInicio;
            System.out.println("El jugador que inicia es: " + listaJugadors.get(jugadorInicio).getNombre() + " (tiene el doble seis)");

            prepararTurnos(jugadorInicio);
            tablero.add(cercar6(jugadorInicio));

            jugarRonda();


            int parejaGanadoraIndex = evaluarGanadorYPuntuacion();


            mostrarPuntuaciones();

            if (parejas.get(parejaGanadoraIndex).get(0).getPuntuacion() >= PUNTAJE_OBJETIVO) {
                System.out.println("🎉 La pareja ganadora es: " +
                        parejas.get(parejaGanadoraIndex).get(0).getNombre() + " y " +
                        parejas.get(parejaGanadoraIndex).get(1).getNombre() +
                        " con " + parejas.get(parejaGanadoraIndex).get(0).getPuntuacion() + " puntos.");
                juegoTerminado = true;
            } else {
                System.out.println("\n--- Nueva ronda comenzará. Sale a la derecha del último salidor ---\n");
                jugadorSalidaAnterior = jugadorALaDerecha(jugadorSalidaAnterior);
            }
        }
    }

    private void formarParejas() {
        parejas.clear();
        ArrayList<jugador> pareja1 = new ArrayList<>();
        pareja1.add(listaJugadors.get(0));
        pareja1.add(listaJugadors.get(2));
        ArrayList<jugador> pareja2 = new ArrayList<>();
        pareja2.add(listaJugadors.get(1));
        pareja2.add(listaJugadors.get(3));
        parejas.add(pareja1);
        parejas.add(pareja2);
    }

    private void resetearManosYTablero() {
        totalPeses.clear();
        tablero.clear();
        for (jugador j : listaJugadors) {
            j.getMano().clear();
        }
        generarPeses();
    }

    private void repartirFichas() {
        generarMans();
    }

    private int encontrarJugadorConDobleSeis() {
        for (jugador i : listaJugadors) {
            for (Peses p : i.getMano()) {
                if (p.getValor1() == 6 && p.getValor2() == 6) {
                    int idJugador = listaJugadors.indexOf(i);
                    return idJugador;
                }
            }
        }
        return -1;
    }

    private void prepararTurnos(int jugadorInicio) {
        torns.clear();
        for (int i = 0; i < listaJugadors.size(); i++) {
            int idx = (jugadorInicio + i) % listaJugadors.size();
            torns.add(listaJugadors.get(idx));
        }
    }

    private void jugarRonda() {
        boolean rondaTerminada = false;

        while (!rondaTerminada) {
            boolean algunJugadorJugo = false;

            for (jugador j : torns) {
                System.out.println("\nTurno de " + j.getNombre());
                System.out.println("Tablero actual: " + tablero);
                j.mostrar();

                ArrayList<Peses> fichasJugables = getFichasJugables(j);
                if (fichasJugables.isEmpty()) {
                    System.out.println(j.getNombre() + " no puede jugar, pasa.");
                    continue;
                }

                int indiceFicha = -1;
                do {
                    System.out.println("Elige índice de ficha para jugar (o -1 para pasar): ");
                    indiceFicha = sc.nextInt();
                    sc.nextLine();
                    if (indiceFicha == -1) break;
                } while (indiceFicha < 0 || indiceFicha >= j.getMano().size() || !fichasJugables.contains(j.getMano().get(indiceFicha)));

                if (indiceFicha == -1) {
                    System.out.println(j.getNombre() + " decide pasar.");
                    continue;
                }

                Peses fichaElegida = j.getMano().get(indiceFicha);

                int puedeColocar = esPotColocar(fichaElegida);
                if (puedeColocar == 0) {
                    System.out.println("No puedes colocar esa ficha, turno perdido.");
                    continue;
                }

                if (puedeColocar == 3) {
                    System.out.println("Puedes colocar la ficha en izquierda (E) o derecha (D). Elige:");
                    String lado = sc.nextLine().toUpperCase();
                    if (lado.equals("E")) {
                        cDevant(fichaElegida);
                    } else {
                        cDerrere(fichaElegida);
                    }
                } else if (puedeColocar == 1) {
                    cDerrere(fichaElegida);
                } else {
                    cDevant(fichaElegida);
                }

                j.removePesa(fichaElegida);
                System.out.println(j.getNombre() + " colocó " + fichaElegida);
                algunJugadorJugo = true;

                if (j.getMano().isEmpty()) {
                    System.out.println("\n🏆 El jugador " + j.getNombre() + " ha terminado sus fichas!");
                    rondaTerminada = true;
                    break;
                }
            }

            if (!algunJugadorJugo) {
                System.out.println("Nadie pudo jugar más, se cierra la ronda (tranca).");
                rondaTerminada = true;
            }
        }
    }

    private ArrayList<Peses> getFichasJugables(jugador j) {
        ArrayList<Peses> jugables = new ArrayList<>();
        for (Peses p : j.getMano()) {
            if (esPotColocar(p) > 0) {
                jugables.add(p);
            }
        }
        return jugables;
    }

    private int evaluarGanadorYPuntuacion() {
        int puntosPareja1 = parejas.get(0).get(0).puntosEnMano() + parejas.get(0).get(1).puntosEnMano();
        int puntosPareja2 = parejas.get(1).get(0).puntosEnMano() + parejas.get(1).get(1).puntosEnMano();

        int parejaGanadora = -1;
        int puntosASumar = 0;

        if (puntosPareja1 == 0 || puntosPareja2 == 0) {
            // Algún jugador terminó fichas, gana su pareja y suma puntos de la otra pareja
            if (puntosPareja1 == 0) {
                parejaGanadora = 0;
                puntosASumar = puntosPareja2;
            } else {
                parejaGanadora = 1;
                puntosASumar = puntosPareja1;
            }
        } else {
            // Tranca o cierre: gana pareja con menos puntos en mano
            if (puntosPareja1 < puntosPareja2) {
                parejaGanadora = 0;
                puntosASumar = puntosPareja2;
            } else if (puntosPareja2 < puntosPareja1) {
                parejaGanadora = 1;
                puntosASumar = puntosPareja1;
            } else {
                // Empate, nadie suma puntos
                System.out.println("Empate en la tranca, nadie suma puntos esta ronda.");
                return 0;
            }
        }

        // Sumar puntos a ambos jugadores de la pareja ganadora
        for (jugador j : parejas.get(parejaGanadora)) {
            j.sumarPuntuacion(puntosASumar);
        }

        System.out.println("\nPAREJA GANADORA: " +
                parejas.get(parejaGanadora).get(0).getNombre() + " y " +
                parejas.get(parejaGanadora).get(1).getNombre() +
                " suman " + puntosASumar + " puntos.");

        return parejaGanadora;
    }

    private void mostrarPuntuaciones() {
        System.out.println("\nPuntuaciones actuales:");
        for (int i = 0; i < parejas.size(); i++) {
            int puntajePareja = parejas.get(i).get(0).getPuntuacion(); // Ambos tienen mismo puntaje
            System.out.println("Pareja " + (i+1) + ": " + parejas.get(i).get(0).getNombre() + " y " +
                    parejas.get(i).get(1).getNombre() + " -> " + puntajePareja);
        }
    }

    private int jugadorALaDerecha(int j) {
        int idxDerecha = (j - 1 + listaJugadors.size()) % listaJugadors.size();
        return listaJugadors.indexOf(idxDerecha);
    }

    private Peses cercar6(int jInici){
        ArrayList<Peses> mano = listaJugadors.get(jInici).getMano();
        for (int i = 0; i < mano.size(); i++) {
            if (mano.get(i).getValor1() == 6 && mano.get(i).getValor2() == 6) {
                return mano.get(i);
            }
        }
        return null;
    }
}
