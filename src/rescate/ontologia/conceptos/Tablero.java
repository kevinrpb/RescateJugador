package rescate.ontologia.conceptos;

import java.util.ArrayList;

public class Tablero extends Concepto {

  /*** Constructor ***/
  public Tablero() {
  }

  /*** Atributos ***/
  private Casilla[][] mapa;
  private ArrayList<Jugador> jugadores;

  /*** Funciones auxiliares ***/
  public ArrayList<Casilla> getHabitacion(int i) {
    ArrayList<Casilla> habitacion = new ArrayList<>();
    for (Casilla[] fila: mapa) {
      for (Casilla c: fila) {
        if (c.getHabitacion() == i) {
          habitacion.add(c);
        }
      }
    }
    return habitacion;
  }

  /*** Getters & Setters ***/
  public ArrayList<Jugador> getJugadores() {
    return jugadores;
  }

  public void setJugador(int indice, Jugador j) {
    this.jugadores.set(indice, j);
  }

  public Casilla[][] getMapa() {
    return mapa;
  }

  public void setCasilla(int X, int Y, Casilla c) {
    this.mapa[Y][X] = c;
  }

}
