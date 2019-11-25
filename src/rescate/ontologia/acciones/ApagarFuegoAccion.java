package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class ApagarFuegoAccion extends Accion {

  private Casilla casilla;

  public ApagarFuegoAccion() {
  }

  public Casilla getCasilla() {
    return casilla;
  }

  public void setCasilla(Casilla casilla) {
    this.casilla = casilla;
  }

}