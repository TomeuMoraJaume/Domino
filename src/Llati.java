public class Llati extends Joc {

    private final int PUNTAJE_OBJETIVO = 100; // puedes ajustar a 200 si prefieres
    private final int PUNTOS_PASO_CORRIDO = 25; // si deseas usarlo más adelante

    public Llati() {
        super();
    }

    @Override
    public void start() {
        super.start();
        iniciarPartida();
        determinarJugadorSalida();
    }


    private void determinarJugadorSalida() {
        Peses seisDoble = new Peses(6, 6);
        for (jugador j : listaJugadors) {
            if (j.getMano().contains(seisDoble)) {
                while (!torns.get(0).equals(j)) {
                    jugador first = torns.remove(0);
                    torns.add(first);
                }
                System.out.println("Jugador que inicia: " + j.getNombre());
                return;
            }
        }
        System.out.println("Nadie tiene seis doble, turno normal.");
    }



    private int puntosPareja(jugador j1, jugador j2) {
        return j1.puntosEnMano() + j2.puntosEnMano();
    }

    public void finalizarMano() {
        jugador j0 = listaJugadors.get(0);
        jugador j1 = listaJugadors.get(1);
        jugador j2 = listaJugadors.get(2);
        jugador j3 = listaJugadors.get(3);

        int puntosPareja1 = puntosPareja(j0, j2);
        int puntosPareja2 = puntosPareja(j1, j3);

        jugador parejaGanadoraRepresentante;
        jugador parejaPerdedoraRepresentante;

        if (puntosPareja1 == 0) {
            parejaGanadoraRepresentante = j0;
            parejaPerdedoraRepresentante = j1;
        } else if (puntosPareja2 == 0) {
            parejaGanadoraRepresentante = j1;
            parejaPerdedoraRepresentante = j0;
        } else if (puntosPareja1 < puntosPareja2) {
            parejaGanadoraRepresentante = j0;
            parejaPerdedoraRepresentante = j1;
        } else if (puntosPareja2 < puntosPareja1) {
            parejaGanadoraRepresentante = j1;
            parejaPerdedoraRepresentante = j0;
        } else {
            jugador jugadorSalida = torns.get(0);
            if (jugadorSalida.equals(j0) || jugadorSalida.equals(j2)) {
                parejaGanadoraRepresentante = j0;
                parejaPerdedoraRepresentante = j1;
            } else {
                parejaGanadoraRepresentante = j1;
                parejaPerdedoraRepresentante = j0;
            }
        }

        jugador compañeroPerdedor = (parejaPerdedoraRepresentante.equals(j0) || parejaPerdedoraRepresentante.equals(j2)) ?
                (parejaPerdedoraRepresentante.equals(j0) ? j2 : j0) :
                (parejaPerdedoraRepresentante.equals(j1) ? j3 : j1);

        int puntosSumados = puntosPareja(parejaPerdedoraRepresentante, compañeroPerdedor);

        parejaGanadoraRepresentante.sumarPuntuacion(puntosSumados);
        parejaPerdedoraRepresentante.sumarPuntuacion(0);

        System.out.println("La pareja ganadora suma " + puntosSumados + " puntos.");

        for (jugador j : listaJugadors) {
            if (j.getPuntuacion() >= PUNTAJE_OBJETIVO) {
                System.out.println("🎉 ¡Jugador " + j.getNombre() + " ha ganado la partida!");
                break;
            }
        }
    }
}
