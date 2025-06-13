import java.util.*;


public class Ponce extends Joc {

    private final int PUNTAJE_OBJETIVO_PONCE = 20;
    private int equipoAPuntos = 0;
    private int equipoBPuntos = 0;

    private int indiceJugadorSalidaAnterior = -1;
    private boolean primerPaseAnotado = false;

    private ArrayList<jugador> equipoA = new ArrayList<>();
    private ArrayList<jugador> equipoB = new ArrayList<>();

    private ArrayList<jugador> ordenTurnos = new ArrayList<>();

    public Ponce() {
        super();
    }

    @Override
    public void start() {
        generarJugadors();
        if (listaJugadors.size() != 4) {
            System.out.println("Dominó Ponce requiere exactamente 4 jugadores.");
            return;
        }

        equipoA.clear();
        equipoB.clear();
        equipoA.add(listaJugadors.get(0));
        equipoA.add(listaJugadors.get(2));
        equipoB.add(listaJugadors.get(1));
        equipoB.add(listaJugadors.get(3));

        jugarVariasPartidas();
    }

    @Override
    public void jugarVariasPartidas() {
        boolean partidoTerminado = false;

        resetearPuntuaciones();
        primerPaseAnotado = false;
        indiceJugadorSalidaAnterior = -1;

        while (!partidoTerminado) {

            totalPeses.clear();
            tablero.clear();


            for (jugador j : listaJugadors) {
                j.getMano().clear();
            }

            generarPeses();
            generarMans();


            if (indiceJugadorSalidaAnterior != -1) {
                jugador fregador = listaJugadors.get(indiceJugadorSalidaAnterior);
                System.out.println(fregador.getNombre() + " frega las fichas para la nueva mano.");

                ArrayList<Peses> todasFichas = new ArrayList<>();
                for (jugador j : listaJugadors) {
                    todasFichas.addAll(j.getMano());
                    j.getMano().clear();
                }
                Collections.shuffle(todasFichas);

                int idx = 0;
                for (Peses p : todasFichas) {
                    listaJugadors.get(idx % listaJugadors.size()).setMano(p);
                    idx++;
                }
            }

            iniciarPartida();

            jugador ganadorMano = jugarMano();

            if (ganadorMano != null) {

                int puntosManoA = calcularPuntosEquipo(equipoA);
                int puntosManoB = calcularPuntosEquipo(equipoB);

                System.out.println("Puntos esta mano - Equipo A: " + puntosManoA + " | Equipo B: " + puntosManoB);

                equipoAPuntos += puntosManoA;
                equipoBPuntos += puntosManoB;

                System.out.println("Puntajes acumulados - Equipo A: " + equipoAPuntos + " | Equipo B: " + equipoBPuntos);


                if (equipoAPuntos >= PUNTAJE_OBJETIVO_PONCE) {
                    System.out.println("\n🎉 ¡Equipo A ha ganado el partido con " + equipoAPuntos + " puntos!");
                    partidoTerminado = true;
                } else if (equipoBPuntos >= PUNTAJE_OBJETIVO_PONCE) {
                    System.out.println("\n🎉 ¡Equipo B ha ganado el partido con " + equipoBPuntos + " puntos!");
                    partidoTerminado = true;
                }
            } else {
                System.out.println("Mano finalizada sin ganador (tranque sin ganador).");
            }

            if (!partidoTerminado) {
                System.out.println("\n--- Nueva mano comenzará ---\n");
            }
        }
    }

    @Override
    public void iniciarPartida() {


        int jugadorSalida;

        if (indiceJugadorSalidaAnterior == -1) {

            jugadorSalida = buscarJugadorDobleSeis();
            if (jugadorSalida == -1) {
                System.out.println("Ningún jugador tiene el doble seis. Se elige jugador 0 para iniciar.");
                jugadorSalida = 0;
            }
        } else {

            jugadorSalida = (indiceJugadorSalidaAnterior - 1 + listaJugadors.size()) % listaJugadors.size();
        }

        indiceJugadorSalidaAnterior = jugadorSalida;

        ordenTurnos.clear();
        for (int i = 0; i < listaJugadors.size(); i++) {
            ordenTurnos.add(listaJugadors.get((jugadorSalida - i + listaJugadors.size()) % listaJugadors.size()));
        }

        System.out.println("Comienza la mano el jugador: " + listaJugadors.get(jugadorSalida).getNombre());



        jugador jugadorInicio = listaJugadors.get(jugadorSalida);
        Peses fichaSalida = null;

        if (indiceJugadorSalidaAnterior == jugadorSalida && primerPaseAnotado == false) {

            for (Peses p : jugadorInicio.getMano()) {
                if (p.getValor1() == 6 && p.getValor2() == 6) {
                    fichaSalida = p;
                    break;
                }
            }
        }

        if (fichaSalida == null) {

            fichaSalida = jugadorInicio.getMano().get(0);
        }

        tablero.add(fichaSalida);
        jugadorInicio.removePesa(fichaSalida);

        System.out.println(jugadorInicio.getNombre() + " coloca la ficha inicial: " + fichaSalida);
    }

    private int buscarJugadorDobleSeis() {
        for (int i = 0; i < listaJugadors.size(); i++) {
            jugador j = listaJugadors.get(i);
            for (Peses p : j.getMano()) {
                if (p.getValor1() == 6 && p.getValor2() == 6) {
                    return i;
                }
            }
        }
        return -1;
    }

    private jugador jugarMano() {
        boolean manoFinalizada = false;
        jugador ganador = null;


        int indiceTurno = 0;


        List<jugador> jugadoresPasados = new ArrayList<>();

        while (!manoFinalizada) {
            jugador jugadorActual = ordenTurnos.get(indiceTurno);

            System.out.println("\nTurno de: " + jugadorActual.getNombre());
            System.out.println("Tablero: " + tablero);
            jugadorActual.mostrar();

            boolean haJugado = false;
            boolean pasoJugador = false;


            int indexFicha = -1;

            do {
                System.out.println("Escribe el índice de la ficha para jugar, o -1 para pasar:");
                try {
                    indexFicha = sc.nextInt();
                    sc.nextLine();
                } catch (Exception e) {
                    System.out.println("Entrada no válida. Intenta de nuevo.");
                    sc.nextLine();
                    continue;
                }

                if (indexFicha == -1) {

                    pasoJugador = true;
                    haJugado = true;
                } else if (indexFicha >= 0 && indexFicha < jugadorActual.getMano().size()) {
                    Peses fichaSeleccionada = jugadorActual.getMano().get(indexFicha);
                    int lugar = esPotColocar(fichaSeleccionada);

                    if (lugar == 0) {
                        System.out.println("No puedes colocar esa ficha. Intenta otra.");
                    } else {
                        if (lugar == 3) {
                            System.out.println("Puedes colocar la ficha a la izquierda o derecha (E/D):");
                            String lado = sc.nextLine();
                            if (lado.equalsIgnoreCase("E")) {
                                tablero.add(0, fichaSeleccionada);
                            } else {
                                tablero.add(fichaSeleccionada);
                            }
                        } else if (lugar == 1) {
                            tablero.add(0, fichaSeleccionada);
                        } else if (lugar == 2) {
                            tablero.add(fichaSeleccionada);
                        }

                        jugadorActual.removePesa(fichaSeleccionada);
                        haJugado = true;
                        pasoJugador = false;
                    }
                } else {
                    System.out.println("Índice fuera de rango, intenta de nuevo.");
                }

            } while (!haJugado);



            if (pasoJugador) {
                System.out.println(jugadorActual.getNombre() + " pasa.");

                if (!jugadoresPasados.contains(jugadorActual)) {

                    int puntosPara = puntosPorPase(jugadorActual, jugadoresPasados);
                    if (puntosPara > 0) {
                        System.out.println("Se suman " + puntosPara + " puntos al equipo contrario.");
                        if (equipoA.contains(jugadorActual)) {
                            equipoBPuntos += puntosPara;
                        } else {
                            equipoAPuntos += puntosPara;
                        }
                        if (!primerPaseAnotado) {
                            primerPaseAnotado = true;
                        }
                    }
                    jugadoresPasados.add(jugadorActual);
                }
            } else {

                jugadoresPasados.clear();
            }


            if (jugadorActual.getMano().isEmpty()) {
                System.out.println(jugadorActual.getNombre() + " ha ganado la mano!");
                ganador = jugadorActual;
                manoFinalizada = true;
                break;
            }


            if (esTranque()) {
                System.out.println("¡Tranque detectado!");
                manoFinalizada = true;
                ganador = null;
                break;
            }

            indiceTurno = (indiceTurno + 1) % ordenTurnos.size();
        }

        return ganador;
    }

    private int puntosPorPase(jugador jugadorActual, List<jugador> jugadoresPasados) {


        if (!primerPaseAnotado) {
            return 2;
        }

        jugador compañero = obtenerCompanero(jugadorActual);


        int idxActual = ordenTurnos.indexOf(jugadorActual);
        int idxSiguiente = (idxActual + 1) % ordenTurnos.size();
        jugador siguiente = ordenTurnos.get(idxSiguiente);

        if (siguiente == compañero) {

            return 0;
        }


        return 1;
    }

    private jugador obtenerCompanero(jugador jugadorActual) {
        if (equipoA.contains(jugadorActual)) {
            for (jugador j : equipoA) {
                if (j != jugadorActual) return j;
            }
        } else {
            for (jugador j : equipoB) {
                if (j != jugadorActual) return j;
            }
        }
        return null;
    }

    private boolean esTranque() {

        for (jugador j : listaJugadors) {
            for (Peses p : j.getMano()) {
                if (esPotColocar(p) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private int calcularPuntosEquipo(List<jugador> equipo) {

        int totalPuntos = 0;
        for (jugador j : equipo) {
            int puntosJugador = 0;
            for (Peses p : j.getMano()) {
                puntosJugador += p.getValor1() + p.getValor2();
            }
            totalPuntos += puntosJugador;
        }


        return redondearPuntos(totalPuntos);
    }

    private int redondearPuntos(int puntos) {

        return puntos / 5;
    }

    public void resetearPuntuaciones() {
        equipoAPuntos = 0;
        equipoBPuntos = 0;
    }


}
