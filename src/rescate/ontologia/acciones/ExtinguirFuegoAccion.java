package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class ExtinguirFuegoAccion extends Accion {

  private Casilla casilla;

  public ExtinguirFuegoAccion() {
  }

  public Casilla getCasilla() {
    return casilla;
  }

  public void setCasilla(Casilla casilla) {
    this.casilla = casilla;
  }

}