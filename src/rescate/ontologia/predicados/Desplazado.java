package rescate.ontologia.predicados;

import java.util.ArrayList;

import rescate.ontologia.conceptos.Casilla;
import rescate.ontologia.conceptos.Jugador;

public class Desplazado extends Predicado {

  private ArrayList<Casilla> habitacion;
  private ArrayList<Jugador> jugadores;

  public Desplazado() {
  }

  public ArrayList<Casilla> getHabitacion() {
    return habitacion;
  }

  public void setHabitacion(ArrayList<Casilla> habitacion) {
    this.habitacion = habitacion;
  }

  public ArrayList<Jugador> getJugadores() {
    return jugadores;
  }

  public void setJugadores(ArrayList<Jugador> jugadores) {
    this.jugadores = jugadores;
  }

}