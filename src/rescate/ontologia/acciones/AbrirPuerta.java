package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class AbrirPuerta extends Accion {

  private int conexion;

  public AbrirPuerta() {
  }

  public int getConexion() {
    return conexion;
  }

  public void setConexion(int conexion) {
    this.conexion = conexion;
  }

}