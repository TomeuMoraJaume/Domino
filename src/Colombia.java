public class Colombia extends Joc{

        private jugador equipo1Jugador1, equipo1Jugador2;
        private jugador equipo2Jugador1, equipo2Jugador2;
        private jugador jugadorQueCierra;
        private jugador jugadorQueInicio;

        @Override
        public void start() {
            generarJugadorsColombiano();
            jugarVariasPartidas();
        }

        private void generarJugadorsColombiano() {
            sort.imprimirTexte("Mode Colombia: 2 equips de 2 jugadors.");
            for (int i = 0; i < 4; i++) {
                sort.imprimirTexte("Nom del jugador " + (i + 1) + ": ");
                String nom = sc.nextLine();
                jugador j = new jugador();
                j.setId(i);
                j.setNombre(nom);
                listaJugadors.add(j);
            }

            equipo1Jugador1 = listaJugadors.get(0);
            equipo1Jugador2 = listaJugadors.get(2);
            equipo2Jugador1 = listaJugadors.get(1);
            equipo2Jugador2 = listaJugadors.get(3);
        }

        @Override
        public void iniciarPartida() {
            jugador queInicia = determinarJugadorQueInicia();
            jugadorQueInicio = queInicia;

            Peses millorDoble = null;
            for (Peses p : queInicia.getMano()) {
                if (p.getValor1() == p.getValor2()) {
                    if (millorDoble == null || p.getValor1() > millorDoble.getValor1()) {
                        millorDoble = p;
                    }
                }
            }

            if (millorDoble != null) {
                tablero.add(millorDoble);
                queInicia.removePesa(millorDoble);
                sort.imprimirTexte(queInicia.getNombre() + " inicia amb el doble " + millorDoble);
            }

            torns.clear();
            int idx = listaJugadors.indexOf(queInicia);
            for (int i = 0; i < listaJugadors.size(); i++) {
                torns.add(listaJugadors.get((idx + i) % listaJugadors.size()));
            }
        }

        private jugador determinarJugadorQueInicia() {
            if (jugadorQueCierra != null) {
                return jugadorQueCierra;
            }
            return listaJugadors.get(cercarJugadorambMajorDoblePessa());
        }

        @Override
        public jugador jugar() {
            jugador ganadorRonda = null;
            jugadorQueCierra = null;
            boolean bloquejat = false;

            while (!bloquejat) {
                boolean alguienPudoJugar = false;
                for (jugador j : torns) {
                    System.out.println("\nTorn de: " + j.getNombre());
                    System.out.println("Tauler: " + tablero);
                    j.mostrar();

                    boolean haJugat = false;

                    System.out.println("Índex de la fitxa a jugar:");
                    int index = sc.nextInt();
                    sc.nextLine();

                    if (index < j.getMano().size()) {
                        Peses p = j.getMano().get(index);
                        int potColocar = esPotColocar(p);

                        if (potColocar > 0) {
                            if (potColocar == 3) {
                                System.out.println("Escriu 'E' per esquerra o 'D' per dreta:");
                                String costat = sc.nextLine();
                                if (costat.equalsIgnoreCase("E")) cDevant(p);
                                else cDerrere(p);
                            } else if (potColocar == 2) cDevant(p);
                            else cDerrere(p);

                            j.removePesa(p);
                            jugadorQueCierra = j;
                            haJugat = true;
                            alguienPudoJugar = true;
                            System.out.println(j.getNombre() + " juga " + p);
                        }
                    }

                    if (!haJugat) {
                        if (!totalPeses.isEmpty()) {
                            Peses nova = totalPeses.remove(0);
                            j.setMano(nova);
                            System.out.println(j.getNombre() + " roba: " + nova);
                        } else {
                            System.out.println(j.getNombre() + " passa.");
                        }
                    }

                    if (j.getMano().isEmpty()) {
                        sort.imprimirTexte("🏆 " + j.getNombre() + " ha guanyat aquesta ronda!");
                        ganadorRonda = j;
                        return j;
                    }
                }

                if (!alguienPudoJugar) {
                    bloquejat = true;
                    ganadorRonda = determinarGuanyadorTrancament();
                    if (ganadorRonda != null) {
                        sort.imprimirTexte("🏆 Ronda guanyada per " + ganadorRonda.getNombre() + " per trancament.");
                    }
                }
            }
            jugadorQueCierra = ganadorRonda;
            return ganadorRonda;
        }

        private jugador determinarGuanyadorTrancament() {
            int pEquipo1 = equipo1Jugador1.puntosEnMano() + equipo1Jugador2.puntosEnMano();
            int pEquipo2 = equipo2Jugador1.puntosEnMano() + equipo2Jugador2.puntosEnMano();

            return pEquipo1 <= pEquipo2 ? equipo1Jugador1 : equipo2Jugador1;
        }
    }


