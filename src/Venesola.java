import java.util.ArrayList;

public class Venesola extends Joc {

    private final int PUNTAJE_OBJETIVO = 75; // o 100 según quieras configurar

    // Parejas: 0 y 2, 1 y 3
    private ArrayList<ArrayList<jugador>> parejas = new ArrayList<>();

    // Para saber quién salió en la ronda anterior
    private jugador jugadorSalidaAnterior = null;

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

            jugador jugadorInicio = encontrarJugadorConDobleSeis();
            if (jugadorInicio == null) {
                System.out.println("Error: No se encontró el doble seis. Reiniciando reparto...");
                continue;
            }

            jugadorSalidaAnterior = jugadorInicio;
            System.out.println("El jugador que inicia es: " + jugadorInicio.getNombre() + " (tiene el doble seis)");

            // Rotar la lista de turnos para que empiece el jugador que tiene el doble seis
            prepararTurnos(jugadorInicio);

            // Aquí lanzamos la partida
            jugarRonda();

            // Calculamos la puntuación
            int parejaGanadoraIndex = evaluarGanadorYPuntuacion();

            // Mostrar puntuaciones acumuladas
            mostrarPuntuaciones();

            // Verificamos si alguna pareja llegó a la meta
            if (parejas.get(parejaGanadoraIndex).get(0).getPuntuacion() >= PUNTAJE_OBJETIVO) {
                System.out.println("🎉 La pareja ganadora es: " +
                        parejas.get(parejaGanadoraIndex).get(0).getNombre() + " y " +
                        parejas.get(parejaGanadoraIndex).get(1).getNombre() +
                        " con " + parejas.get(parejaGanadoraIndex).get(0).getPuntuacion() + " puntos.");
                juegoTerminado = true;
            } else {
                System.out.println("\n--- Nueva ronda comenzará. Sale a la derecha del último salidor ---\n");
                // Para la siguiente ronda, el jugador que inicia es el de la derecha
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

    private jugador encontrarJugadorConDobleSeis() {
        for (jugador j : listaJugadors) {
            for (Peses p : j.getMano()) {
                if (p.getValor1() == 6 && p.getValor2() == 6) {
                    return j;
                }
            }
        }
        return null;
    }

    private void prepararTurnos(jugador jugadorInicio) {
        torns.clear();
        // Empezar desde jugadorInicio y continuar en orden (listaJugadors es fija)
        int inicioIndex = listaJugadors.indexOf(jugadorInicio);
        for (int i = 0; i < listaJugadors.size(); i++) {
            int idx = (inicioIndex + i) % listaJugadors.size();
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
                    continue; // Pasa el turno
                }

                // Para simplicidad, dejamos que el jugador elija ficha a jugar con input
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

    // Retorna índice de pareja ganadora tras la ronda y actualiza puntos
    private int evaluarGanadorYPuntuacion() {
        // Sumar puntos no jugados por cada pareja
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
                return 0; // Retornamos 0 para seguir juego, ningún puntaje sumado
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

    private jugador jugadorALaDerecha(jugador j) {
        int idx = listaJugadors.indexOf(j);
        int idxDerecha = (idx - 1 + listaJugadors.size()) % listaJugadors.size();
        return listaJugadors.get(idxDerecha);
    }
}
