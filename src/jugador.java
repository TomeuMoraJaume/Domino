import java.util.ArrayList;

public class jugador {
    private ArrayList<Peses> mano = new ArrayList<>();
    private int id;
    private String nombre;

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
}
