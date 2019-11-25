package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class ExtinguirFuego extends Accion {

  private Casilla casilla;

  public ExtinguirFuego() {
  }

  public Casilla getCasilla() {
    return casilla;
  }

  public void setCasilla(Casilla casilla) {
    this.casilla = casilla;
  }

}