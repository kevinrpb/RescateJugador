package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class ExtinguirHumoAccion extends Accion {

  private Casilla casilla;

  public ExtinguirHumoAccion() {
  }

  public Casilla getCasilla() {
    return casilla;
  }

  public void setCasilla(Casilla casilla) {
    this.casilla = casilla;
  }

}