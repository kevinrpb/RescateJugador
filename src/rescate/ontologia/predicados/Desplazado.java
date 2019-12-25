package rescate.ontologia.predicados;

import java.util.ArrayList;

import rescate.ontologia.conceptos.Casilla;

public class Desplazado extends Predicado {

  private ArrayList<Casilla> habitacion;

  public Desplazado() {
  }

  public ArrayList<Casilla> getHabitacion() {
    return habitacion;
  }

  public void setHabitacion(ArrayList<Casilla> habitacion) {
    this.habitacion = habitacion;
  }

}