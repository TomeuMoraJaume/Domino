public class Xile extends Joc {


    private boolean jugarConSacar = true; // true = se puede sacar, false = solo pasar

    public Xile() {
        sort.imprimirTexte("Has elegido la modalidad de dominó Xilè.");
        configurarModoDeSacar();
        generarJugadors();
        jugarVariasPartidas();
    }

    private void configurarModoDeSacar() {
        sort.imprimirTexte("¿Deseas jugar con la regla de 'sacar' al no poder jugar? (s/n):");
        String respuesta = sc.nextLine();
        jugarConSacar = respuesta.equalsIgnoreCase("s");
    }

    @Override
    public void iniciarPartida() {
        int index = buscarDobleMasAltoDisponible();
        if (index != -1) {
            jugador jugadorInicial = listaJugadors.get(index);
            Peses doble = obtenerDobleMasAlto(jugadorInicial);
            tablero.add(doble);
            jugadorInicial.removePesa(doble);

            sort.imprimirTexte(jugadorInicial.getNombre() + " inicia la partida con el " + doble);

            torns.clear();
            torns.addAll(listaJugadors);
            while (!torns.get(0).equals(jugadorInicial)) {
                jugador j = torns.remove(0);
                torns.add(j);
            }
        }
    }

    private int buscarDobleMasAltoDisponible() {
        for (int v = 6; v >= 0; v--) {
            for (int i = 0; i < listaJugadors.size(); i++) {
                for (Peses p : listaJugadors.get(i).getMano()) {
                    if (p.getValor1() == v && p.getValor1() == p.getValor2()) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private Peses obtenerDobleMasAlto(jugador j) {
        for (int v = 6; v >= 0; v--) {
            for (Peses p : j.getMano()) {
                if (p.getValor1() == v && p.getValor1() == p.getValor2()) {
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public jugador jugar() {
        boolean jocAcabat = false;
        jugador ganador = null;

        while (!jocAcabat) {
            boolean todosPasaron = true;

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

                    if (potColocar > 0) {
                        if (potColocar == 1) cDerrere(pesaSeleccionada);
                        else if (potColocar == 2) cDevant(pesaSeleccionada);
                        else {
                            System.out.println("Pots col·locar a esquerra o dreta. Escriu 'E' o 'D':");
                            String costat = sc.nextLine();
                            if (costat.equalsIgnoreCase("E")) cDevant(pesaSeleccionada);
                            else cDerrere(pesaSeleccionada);
                        }
                        j.removePesa(pesaSeleccionada);
                        System.out.println(j.getNombre() + " ha jugat: " + pesaSeleccionada);
                        haJugat = true;
                        todosPasaron = false;
                    }
                }

                if (!haJugat) {
                    if (jugarConSacar && !totalPeses.isEmpty()) {
                        Peses nova = totalPeses.remove(0);
                        j.setMano(nova);
                        System.out.println(j.getNombre() + " roba una fitxa: " + nova);
                    } else {
                        System.out.println(j.getNombre() + " passa.");
                    }
                }

                if (j.getMano().isEmpty()) {
                    ganador = j;
                    jocAcabat = true;
                    break;
                }
            }

            // Si todos pasaron, cierre
            if (todosPasaron) {
                jocAcabat = true;
                ganador = calcularGanadorCierre();
            }
        }

        return ganador;
    }

    private jugador calcularGanadorCierre() {
        jugador maxPuntos = null;
        int puntosMaximos = -1;

        for (jugador j : listaJugadors) {
            int puntos = j.puntosEnMano();
            if (puntos > puntosMaximos) {
                puntosMaximos = puntos;
                maxPuntos = j;
            }
        }

        int suma = 0;
        for (jugador j : listaJugadors) {
            suma += j.puntosEnMano();
        }
        if (maxPuntos != null) {
            maxPuntos.sumarPuntuacion(suma);
            System.out.println(maxPuntos.getNombre() + " gana por cierre y suma " + suma + " puntos.");
        }

        return maxPuntos;
    }

    @Override
    public void jugarVariasPartidas() {
        resetearPuntuaciones();
        boolean partidaFinal = false;

        while (!partidaFinal) {
            totalPeses.clear();
            tablero.clear();
            for (jugador j : listaJugadors) j.getMano().clear();
            generarPeses();
            generarMans();
            iniciarPartida();
            jugador ganador = jugar();

            if (ganador != null) {
                System.out.println("Fin de la partida. Puntajes:");
                for (jugador j : listaJugadors) {
                    System.out.println(j.getNombre() + ": " + j.getPuntuacion());
                }

                for (jugador j : listaJugadors) {
                    if (j.getPuntuacion() >= 121) {
                        System.out.println("🎉 El jugador " + encontrarPerdedor().getNombre() + " gana la partida por tener la menor puntuación.");
                        partidaFinal = true;
                        break;
                    }
                }
            }
        }
    }

    private jugador encontrarPerdedor() {
        jugador perdedor = null;
        int min = Integer.MAX_VALUE;
        for (jugador j : listaJugadors) {
            if (j.getPuntuacion() < min) {
                min = j.getPuntuacion();
                perdedor = j;
            }
        }
        return perdedor;
    }
}
