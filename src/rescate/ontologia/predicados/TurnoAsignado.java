package rescate.ontologia.predicados;

import java.util.ArrayList;

import rescate.ontologia.conceptos.Casilla;
import rescate.ontologia.conceptos.Jugador;

public class TurnoAsignado extends Predicado {

  private Jugador jugador;
  private ArrayList<Casilla> habitacion;

  public TurnoAsignado() {
  }

  public Jugador getJugador() {
    return jugador;
  }

  public void setJugador(Jugador jugador) {
    this.jugador = jugador;
  }

  public ArrayList<Casilla> getHabitacion() {
    return habitacion;
  }

  public void setHabitacion(ArrayList<Casilla> habitacion) {
    this.habitacion = habitacion;
  }

}