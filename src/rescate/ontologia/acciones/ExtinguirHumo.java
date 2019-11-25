package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class ExtinguirHumo extends Accion {

  private Casilla casilla;

  public ExtinguirHumo() {
  }

  public Casilla getCasilla() {
    return casilla;
  }

  public void setCasilla(Casilla casilla) {
    this.casilla = casilla;
  }

}