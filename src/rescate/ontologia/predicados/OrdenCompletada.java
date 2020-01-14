package rescate.ontologia.predicados;

import rescate.ontologia.conceptos.Jugador;

public class OrdenCompletada extends Predicado {

  private int puntosAccion;
  private Jugador jugador;
  
  public OrdenCompletada() {
  } 

  public void setPuntosAccion(int puntosAccion) {
    this.puntosAccion = puntosAccion;
  }

  public int getPuntosAccion() {
    return this.puntosAccion;
  }

  public Jugador getJugador() {
    return jugador;
  }

  public void setJugador(Jugador jugador) {
    this.jugador = jugador;
  }

}