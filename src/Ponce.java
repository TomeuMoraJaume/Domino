import java.util.*;

public class Ponce extends Joc {

    private final int PUNTAJE_OBJETIVO_PONCE = 20;
    private int equipoAPuntos = 0;
    private int equipoBPuntos = 0;

    private int indiceJugadorSalidaAnterior = -1;
    private boolean primerPaseAnotado = false;

    private ArrayList<jugador> equipoA = new ArrayList<>();
    private ArrayList<jugador> equipoB = new ArrayList<>();

    // Para controlar el orden del turno según rotación anti-horaria (a la derecha)
    // En Ponce el orden gira en contra de las manecillas del reloj (a la derecha)
    // Esto significa que el siguiente jugador es (actual -1 + n) % n
    private ArrayList<jugador> ordenTurnos = new ArrayList<>();

    public Ponce() {
        super();
    }

    @Override
    public void start() {
        // Preparamos equipos (suponemos 4 jugadores)
        if (listaJugadors.size() != 4) {
            System.out.println("Dominó Ponce requiere exactamente 4 jugadores.");
            return;
        }

        // Asignamos equipos: Jugadores 0 y 2 en equipo A, 1 y 3 en equipo B
        equipoA.clear();
        equipoB.clear();
        equipoA.add(listaJugadors.get(0));
        equipoA.add(listaJugadors.get(2));
        equipoB.add(listaJugadors.get(1));
        equipoB.add(listaJugadors.get(3));

        // Empezamos las partidas
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

            // Limpiar manos jugadores
            for (jugador j : listaJugadors) {
                j.getMano().clear();
            }

            generarPeses();
            generarMans();

            // Si no es la primera mano, el jugador que empezó la anterior debe "fregar" (revolver fichas)
            if (indiceJugadorSalidaAnterior != -1) {
                jugador fregador = listaJugadors.get(indiceJugadorSalidaAnterior);
                System.out.println(fregador.getNombre() + " frega las fichas para la nueva mano.");
                // Barajear las manos de todos mezclando sus fichas
                ArrayList<Peses> todasFichas = new ArrayList<>();
                for (jugador j : listaJugadors) {
                    todasFichas.addAll(j.getMano());
                    j.getMano().clear();
                }
                Collections.shuffle(todasFichas);
                // Repartir las fichas de nuevo
                int idx = 0;
                for (Peses p : todasFichas) {
                    listaJugadors.get(idx % listaJugadors.size()).setMano(p);
                    idx++;
                }
            }

            iniciarPartida();

            jugador ganadorMano = jugarMano();

            if (ganadorMano != null) {
                // Calcular puntos de la mano para equipos y sumar
                int puntosManoA = calcularPuntosEquipo(equipoA);
                int puntosManoB = calcularPuntosEquipo(equipoB);

                System.out.println("Puntos esta mano - Equipo A: " + puntosManoA + " | Equipo B: " + puntosManoB);

                equipoAPuntos += puntosManoA;
                equipoBPuntos += puntosManoB;

                System.out.println("Puntajes acumulados - Equipo A: " + equipoAPuntos + " | Equipo B: " + equipoBPuntos);

                // Revisar si algún equipo llegó al objetivo
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
        // Determinar jugador que inicia la mano

        int jugadorSalida;

        if (indiceJugadorSalidaAnterior == -1) {
            // Primera mano: jugador que tiene doble seis
            jugadorSalida = buscarJugadorDobleSeis();
            if (jugadorSalida == -1) {
                System.out.println("Ningún jugador tiene el doble seis. Se elige jugador 0 para iniciar.");
                jugadorSalida = 0;
            }
        } else {
            // Manos siguientes: jugador a la derecha (rotación anti-horaria)
            jugadorSalida = (indiceJugadorSalidaAnterior - 1 + listaJugadors.size()) % listaJugadors.size();
        }

        indiceJugadorSalidaAnterior = jugadorSalida;

        // Ordenar turnos rotando hacia la derecha (anti-horario)
        ordenTurnos.clear();
        for (int i = 0; i < listaJugadors.size(); i++) {
            ordenTurnos.add(listaJugadors.get((jugadorSalida - i + listaJugadors.size()) % listaJugadors.size()));
        }

        System.out.println("Comienza la mano el jugador: " + listaJugadors.get(jugadorSalida).getNombre());

        // Poner la ficha de salida en el tablero

        jugador jugadorInicio = listaJugadors.get(jugadorSalida);
        Peses fichaSalida = null;

        if (indiceJugadorSalidaAnterior == jugadorSalida && primerPaseAnotado == false) {
            // Primera mano, debe salir con doble seis
            for (Peses p : jugadorInicio.getMano()) {
                if (p.getValor1() == 6 && p.getValor2() == 6) {
                    fichaSalida = p;
                    break;
                }
            }
        }

        if (fichaSalida == null) {
            // No es la primera mano o no tiene doble seis (permite salir con cualquier ficha)
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

        // Controlar turnos usando ordenTurnos
        int indiceTurno = 0;

        // Para controlar pases y puntos
        // Guardamos quién pasó este turno para verificar pase gratis o no
        List<jugador> jugadoresPasados = new ArrayList<>();

        while (!manoFinalizada) {
            jugador jugadorActual = ordenTurnos.get(indiceTurno);

            System.out.println("\nTurno de: " + jugadorActual.getNombre());
            System.out.println("Tablero: " + tablero);
            jugadorActual.mostrar();

            boolean haJugado = false;
            boolean pasoJugador = false;

            // Preguntar qué ficha quiere jugar (simulación o real input)
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
                    // El jugador decide pasar
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

            // Controlar puntuación por pase

            if (pasoJugador) {
                System.out.println(jugadorActual.getNombre() + " pasa.");

                if (!jugadoresPasados.contains(jugadorActual)) {
                    // Calculamos puntos por pase
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
                // Si jugó ficha, resetear lista de pases para próximos turnos
                jugadoresPasados.clear();
            }

            // Comprobar si jugador actual ganó (no tiene fichas)
            if (jugadorActual.getMano().isEmpty()) {
                System.out.println(jugadorActual.getNombre() + " ha ganado la mano!");
                ganador = jugadorActual;
                manoFinalizada = true;
                break;
            }

            // Comprobar tranque (ningún jugador puede poner ficha)
            if (esTranque()) {
                System.out.println("¡Tranque detectado!");
                // Lógica de tranque
                manoFinalizada = true;
                ganador = null; // No hay ganador en tranque
                break;
            }

            // Pasar turno al siguiente jugador rotando a la derecha (anti-horario)
            indiceTurno = (indiceTurno + 1) % ordenTurnos.size();
        }

        return ganador;
    }

    private int puntosPorPase(jugador jugadorActual, List<jugador> jugadoresPasados) {
        // Si es primer pase del partido vale 2 puntos
        // Luego, 1 punto por pase
        // Pase a compañero no vale puntos
        // Pase triple (pase que hace que el mismo jugador juegue de nuevo): 2 puntos
        // Aquí simplificado: si pasa un jugador y es la primera vez que pasa, suma 1 punto
        // Si pasa compañero, no suma

        // Detectar si el pase hace que el turno vuelva al mismo jugador: no implementado complejo
        // Para ahora solo sumar 2 puntos en primer pase y 1 para demás pases no a compañeros

        if (!primerPaseAnotado) {
            return 2;
        }

        // Si el pase hace que pase un compañero, no suma
        jugador compañero = obtenerCompanero(jugadorActual);

        // El jugador al que le toca turno si pasa el jugador actual:
        int idxActual = ordenTurnos.indexOf(jugadorActual);
        int idxSiguiente = (idxActual + 1) % ordenTurnos.size();
        jugador siguiente = ordenTurnos.get(idxSiguiente);

        if (siguiente == compañero) {
            // Pase "gratis"
            return 0;
        }

        // Para pase triple (cuando turno vuelve al mismo jugador) se necesitaría lógica más compleja,
        // para esta versión básica dejamos 1 punto.

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
        // Verificar si ningún jugador puede colocar ficha (simplificado)

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
        // Sumar puntos de las fichas que quedan en mano redondeando
        int totalPuntos = 0;
        for (jugador j : equipo) {
            int puntosJugador = 0;
            for (Peses p : j.getMano()) {
                puntosJugador += p.getValor1() + p.getValor2();
            }
            totalPuntos += puntosJugador;
        }

        // Aplicar redondeo según especificación
        return redondearPuntos(totalPuntos);
    }

    private int redondearPuntos(int puntos) {
        // Por ejemplo, 6=1 punto, 15=1 punto, 16=2 puntos, 26=3 puntos, etc.
        // Se redondea al decimal según: (puntos / 5)
        return puntos / 5;
    }

    public void resetearPuntuaciones() {
        equipoAPuntos = 0;
        equipoBPuntos = 0;
    }


}
