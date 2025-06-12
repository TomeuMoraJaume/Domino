import java.util.ArrayList;

public class jugador {
    private ArrayList<Peses> mano = new ArrayList<>();
    private int id;
    private String nombre;
    private int puntuacio = 0 ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setMano(Peses p){
        this.mano.add(p);
    }

    public void mostrar() {
        System.out.println(this.mano);
    }
    public ArrayList<Peses> getMano(){
        return mano;
    }

    public void removePesa(Peses p) {
        this.mano.remove(p);
    }
    public int getPuntuacion() {
        return puntuacio;
    }
    public void sumarPuntuacion(int puntos) {
        this.puntuacio += puntos;
    }
    public void setPuntuacion(int puntos) {
        this.puntuacio = puntos;
    }
    public int puntosEnMano() {
        int total = 0;
        for (Peses p : mano) {
            total += p.getValor1() + p.getValor2();
        }
        return total;
    }
}
