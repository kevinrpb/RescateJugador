package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class DerribarPared extends Accion {

  private Casilla casilla;
  private int conexion;

  public DerribarPared() {
  }

  public Casilla getCasilla() {
    return casilla;
  }

  public void setCasilla(Casilla casilla) {
    this.casilla = casilla;
  }

  public int getConexion() {
    return conexion;
  }

  public void setConexion(int conexion) {
    this.conexion = conexion;
  }

}