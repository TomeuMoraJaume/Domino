import java.util.ArrayList;

public class jugador {
    ArrayList<Peses> mano;

    public jugador() {
        mano = new ArrayList<>();
    }


    public void insertarPeses(Peses p) {
        mano.add(p);
    }

}
