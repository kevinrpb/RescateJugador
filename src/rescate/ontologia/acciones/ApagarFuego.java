package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class ApagarFuego extends Accion {

  private Casilla casilla;

  public ApagarFuego() {
  }

  public Casilla getCasilla() {
    return casilla;
  }

  public void setCasilla(Casilla casilla) {
    this.casilla = casilla;
  }

}