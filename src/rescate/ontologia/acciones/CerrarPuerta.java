package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Casilla;

public class CerrarPuerta extends Accion {

  private int conexion;

  public CerrarPuerta() {
  }

  public int getConexion() {
    return conexion;
  }

  public void setConexion(int conexion) {
    this.conexion = conexion;
  }

}