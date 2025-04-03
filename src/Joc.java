import java.util.ArrayList;

public class Joc {

    private ArrayList<Peses> totalPeses = new ArrayList<>();

    public void generarPeses(){
        for (int i = 0; i <= 6; i++){
            for (int j = 0; j <= 6; j++){
                totalPeses.add(new Peses(i,j));
            }
        }
    }

    public void imprimirTablero(){
        System.out.println(totalPeses);
    }


}
