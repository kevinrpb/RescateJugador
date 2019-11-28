package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class DannarPared extends Accion {

  private int conexion;

  public DannarPared() {
  }

  public int getConexion() {
    return conexion;
  }

  public void setConexion(int conexion) {
    this.conexion = conexion;
  }

}