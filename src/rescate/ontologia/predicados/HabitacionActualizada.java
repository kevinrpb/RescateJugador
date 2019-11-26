package rescate.ontologia.predicados;

import java.util.ArrayList;
import rescate.ontologia.conceptos.*;

public class HabitacionActualizada extends Predicado {

  private ArrayList<Casilla> casillas;

  public HabitacionActualizada() {
  }

  public ArrayList<Casilla> getCasillas() {
    return casillas;
  }

  public void setCasillas(ArrayList<Casilla> casillas) {
    this.casillas = casillas;
  }

}