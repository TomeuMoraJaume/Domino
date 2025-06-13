import java.util.*;

public class Mexica extends Joc {


    private ArrayList<ArrayList<jugador>> parejas = new ArrayList<>();
    private final int PUNTAJE_OBJETIVO = 200;

    public Mexica() {
        super();
    }

    @Override
    public void start() {
        generarJugadors();
        formarParejas(true);
        jugarVariasPartidasMexica();
    }

    public void formarParejas(boolean parejasFijas) {
        parejas.clear();
        if (parejasFijas) {
            ArrayList<jugador> pareja1 = new ArrayList<>();
            ArrayList<jugador> pareja2 = new ArrayList<>();
            pareja1.add(listaJugadors.get(0));
            pareja1.add(listaJugadors.get(2));
            pareja2.add(listaJugadors.get(1));
            pareja2.add(listaJugadors.get(3));
            parejas.add(pareja1);
            parejas.add(pareja2);
        }
    }

    private int puntosPareja(ArrayList<jugador> pareja) {
        int suma = 0;
        for (jugador j : pareja) {
            suma += j.puntosEnMano();
        }
        return suma;
    }

    private int contarFichasPareja(ArrayList<jugador> pareja) {
        int total = 0;
        for (jugador j : pareja) {
            total += j.getMano().size();
        }
        return total;
    }

    private ArrayList<jugador> parejaGanadoraTranca() {
        int puntos1 = puntosPareja(parejas.get(0));
        int puntos2 = puntosPareja(parejas.get(1));

        if (puntos1 < puntos2) {
            return parejas.get(0);
        } else if (puntos2 < puntos1) {
            return parejas.get(1);
        } else {
            int fichas1 = contarFichasPareja(parejas.get(0));
            int fichas2 = contarFichasPareja(parejas.get(1));
            if (fichas1 < fichas2) {
                return parejas.get(0);
            } else if (fichas2 < fichas1) {
                return parejas.get(1);
            } else {
                return null; // empate total
            }
        }
    }

    private void actualizarPuntuacionPorPareja(ArrayList<jugador> parejaGanadora) {
        ArrayList<jugador> parejaPerdedora = parejas.stream()
                .filter(p -> p != parejaGanadora)
                .findFirst()
                .orElse(new ArrayList<>());

        int puntosSumados = parejaPerdedora.stream()
                .mapToInt(j -> j.puntosEnMano())
                .sum();

        parejaGanadora.forEach(j -> j.sumarPuntuacion(puntosSumados));

        System.out.println("La pareja ganadora suma " + puntosSumados + " puntos.");
    }

    public void resetearPuntuaciones() {
        for (jugador j : listaJugadors) {
            j.setPuntuacion(0);
        }
    }

    public void jugarVariasPartidasMexica() {
        boolean hayGanador = false;
        resetearPuntuaciones();

        while (!hayGanador) {
            totalPeses.clear();
            tablero.clear();
            listaJugadors.forEach(j -> j.getMano().clear());

            generarPeses();
            generarMans();
            iniciarPartida();

            jugador ganador = jugar();  // Este puede ser null si hay tranca
            ArrayList<jugador> parejaGanadora = null;

            if (ganador == null) {
                System.out.println("La ronda terminó en tranca.");
                parejaGanadora = parejaGanadoraTranca();
            } else {
                parejaGanadora = parejas.stream()
                        .filter(p -> p.contains(ganador))
                        .findFirst()
                        .orElse(null);
            }

            if (parejaGanadora != null) {
                actualizarPuntuacionPorPareja(parejaGanadora);
            } else {
                System.out.println("Empate total, no se suman puntos.");
            }

            hayGanador = listaJugadors.stream()
                    .anyMatch(j -> j.getPuntuacion() >= PUNTAJE_OBJETIVO);

            if (hayGanador) {
                jugador finalWinner = listaJugadors.stream()
                        .filter(j -> j.getPuntuacion() >= PUNTAJE_OBJETIVO)
                        .findFirst()
                        .get();
                System.out.println("\n🎉 ¡" + finalWinner.getNombre() + " ha ganado el juego con " + finalWinner.getPuntuacion() + " puntos!");
            } else {
                System.out.println("\n--- Nueva partida comenzará ---\n");
            }
        }
    }
}
