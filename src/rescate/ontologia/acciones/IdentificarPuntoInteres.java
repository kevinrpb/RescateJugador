package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class IdentificarPuntoInteres extends Accion {

  private Casilla casilla;

  public IdentificarPuntoInteres() {
  }

  public Casilla getCasilla() {
    return casilla;
  }

  public void setCasilla(Casilla casilla) {
    this.casilla = casilla;
  }

}