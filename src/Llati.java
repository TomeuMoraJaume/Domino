public class Llati extends Joc {

    private final int PUNTAJE_OBJETIVO = 100; // o 200, según prefieras
    private final int PUNTOS_PASO_CORRIDO = 25; // puntos por paso corrido

    public Llati() {
        super();
    }

    @Override
    public void start() {
        super.start();
        iniciarPartida();
    }
    private void determinarJugadorSalida() {
        Peses seisDoble = new Peses(6, 6);
        for (jugador j : listaJugadors) {
            if (j.getMano().contains(seisDoble)) {
                // El jugador con seis doble inicia
                // Se reorganiza la lista torns para que empiece por este jugador
                while (!torns.get(0).equals(j)) {
                    jugador first = torns.remove(0);
                    torns.add(first);
                }
                System.out.println("Jugador que inicia: " + j.getNombre());
                return;
            }
        }
        // Si nadie tiene seis doble, queda el turno normal (o implementar otra regla)
        System.out.println("Nadie tiene seis doble, turno normal.");
    }

    private int puntosEnMano(jugador j) {
        int suma = 0;
        for (Peses p : j.getMano()) {
            suma += p.getValor1() + p.getValor2();
        }
        return suma;
    }


    private int puntosPareja(jugador j1, jugador j2) {
        return puntosEnMano(j1) + puntosEnMano(j2);
    }


    public void finalizarMano() {
        jugador j0 = listaJugadors.get(0);
        jugador j1 = listaJugadors.get(1);
        jugador j2 = listaJugadors.get(2);
        jugador j3 = listaJugadors.get(3);

        int puntosPareja1 = puntosPareja(j0, j2);
        int puntosPareja2 = puntosPareja(j1, j3);

        // Determinar pareja ganadora y perdedora según reglas
        jugador parejaGanadoraRepresentante;
        jugador parejaPerdedoraRepresentante;

        if (puntosPareja1 == 0) {
            // Pareja 1 ganó por quedarse sin fichas
            parejaGanadoraRepresentante = j0;
            parejaPerdedoraRepresentante = j1;
        } else if (puntosPareja2 == 0) {
            parejaGanadoraRepresentante = j1;
            parejaPerdedoraRepresentante = j0;
        } else if (puntosPareja1 < puntosPareja2) {
            // Tranca: gana pareja con menos puntos
            parejaGanadoraRepresentante = j0;
            parejaPerdedoraRepresentante = j1;
        } else if (puntosPareja2 < puntosPareja1) {
            parejaGanadoraRepresentante = j1;
            parejaPerdedoraRepresentante = j0;
        } else {
            // Empate en puntos: gana pareja que salió en la mano
            jugador jugadorSalida = torns.get(0);
            if (jugadorSalida.equals(j0) || jugadorSalida.equals(j2)) {
                parejaGanadoraRepresentante = j0;
                parejaPerdedoraRepresentante = j1;
            } else {
                parejaGanadoraRepresentante = j1;
                parejaPerdedoraRepresentante = j0;
            }
        }

        // Sumar puntos de la pareja perdedora a la ganadora
        int puntosSumados = puntosPareja(parejaPerdedoraRepresentante, parejaPerdedoraRepresentante.equals(j1) ? j3 : j2);

        parejaGanadoraRepresentante.sumarPuntuacion(puntosSumados);
        parejaPerdedoraRepresentante.sumarPuntuacion(0); // No suma puntos

        System.out.println("La pareja ganadora suma " + puntosSumados + " puntos.");

        for (jugador j : listaJugadors) {
            if (j.getPuntuacion() >= PUNTAJE_OBJETIVO) {
                System.out.println("🎉 ¡Jugador " + j.getNombre() + " ha ganado la partida!");
                break;
            }
        }
    }




}
