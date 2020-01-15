package rescate.ontologia.predicados;

import java.util.ArrayList;

import rescate.ontologia.conceptos.Casilla;
import rescate.ontologia.conceptos.Jugador;

public class OrdenCompletada extends Predicado {

  private int puntosAccion;
  private Jugador jugador;
  private ArrayList<Casilla> habitacion;
  private ArrayList<Jugador> jugadores;
  
  public OrdenCompletada() {
  } 

  public void setPuntosAccion(int puntosAccion) {
    this.puntosAccion = puntosAccion;
  }

  public int getPuntosAccion() {
    return this.puntosAccion;
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

  public Jugador getJugador() {
    return jugador;
  }

  public void setJugador(Jugador jugador) {
    this.jugador = jugador;
  }
  

}