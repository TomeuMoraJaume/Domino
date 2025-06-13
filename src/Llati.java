import java.util.*;

public class Llati extends Joc {

    private final int PUNTAJE_OBJETIVO = 100;

    public Llati() {
        super();
    }

    @Override
    public void start() {
        generarJugadors();      // Paso 1: crear jugadores
        jugarVariasPartidas();  // Paso 2: iniciar bucle de juego
    }

    @Override
    public void generarMans() {
        boolean repartirDeNou;
        do {
            totalPeses.clear();
            generarPeses();

            for (jugador jugadorActual : listaJugadors) {
                jugadorActual.getMano().clear();
            }

            ArrayList<Peses> copiaPeses = new ArrayList<>(totalPeses);
            Random r = new Random();

            for (jugador jugadorActual : listaJugadors) {
                for (int y = 0; y < 7; y++) {
                    int rPosicio = r.nextInt(copiaPeses.size());
                    Peses pesaActual = copiaPeses.remove(rPosicio);
                    jugadorActual.setMano(pesaActual);
                }
            }

            boolean hayDobleSeis = false;
            for (jugador j : listaJugadors) {
                for (Peses p : j.getMano()) {
                    if (p.getValor1() == 6 && p.getValor2() == 6) {
                        hayDobleSeis = true;
                        break;
                    }
                }
                if (hayDobleSeis) break;
            }

            if (!hayDobleSeis) {
                sort.imprimirTexte("Cap jugador té el 6|6. Vols repartir de nou? (s/n):");
                String resposta = sc.nextLine();
                repartirDeNou = resposta.equalsIgnoreCase("s");
            } else {
                repartirDeNou = false;
            }

        } while (repartirDeNou);
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
