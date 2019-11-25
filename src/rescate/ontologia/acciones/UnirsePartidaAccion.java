package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.*;

public class UnirsePartidaAccion extends Accion {

  private Jugador jugador;

  public UnirsePartidaAccion() {
  }

  public Jugador getJugador() {
    return jugador;
  }

  public void setJugador(Jugador jugador) {
    this.jugador = jugador;
  }

}
