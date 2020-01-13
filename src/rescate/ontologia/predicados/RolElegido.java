package rescate.ontologia.predicados;

import rescate.ontologia.conceptos.Jugador;

public class RolElegido extends Predicado {

  Jugador jugador;

  public RolElegido() {
  }

  public void setJugador(Jugador jugador) {
    this.jugador = jugador;
  }

  public Jugador getJugador() {
    return jugador;
  }

}
