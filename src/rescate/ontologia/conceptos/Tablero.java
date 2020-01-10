package rescate.ontologia.conceptos;

import java.util.ArrayList;

import jadex.adapter.fipa.AgentIdentifier;

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
        if (c != null && c.getHabitacion() == i) {
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

  public ArrayList<Jugador> getJugadoresEnHabitacion(int i){
    ArrayList<Jugador> jugadores = new ArrayList<>();
    for (Jugador jugador : this.jugadores) {
      if (jugador.getHabitacion() == i){
        jugadores.add(jugador);
      }
    }
    return jugadores;
  }

  public Jugador getJugador(AgentIdentifier idJugador) {
    for (int i = 0; i < getJugadores().size(); i++) {
      if (getJugadores().get(i).getIdAgente().equals(idJugador)) {
        return getJugadores().get(i);
      }
    }
    return null;
  }

  public int getIndiceJugador(AgentIdentifier idJugador) {
    for (int i = 0; i < getJugadores().size(); i++) {
      if (getJugadores().get(i).getIdAgente().equals(idJugador)) {
        return i;
      }
    }
    return -1;
  }

  public void setJugadores(ArrayList<Jugador> jugadores) {
    this.jugadores = jugadores;
  }

  public Casilla[][] getMapa() {
    return mapa;
  }

  public void setMapa(Casilla[][] mapa) {
    this.mapa = mapa;
  }

  public void setCasilla(int X, int Y, Casilla c) {
    this.mapa[Y][X] = c;
  }

}
