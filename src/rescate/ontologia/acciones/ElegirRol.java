package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Jugador.Rol;

public class ElegirRol extends Accion {

  private Rol rol;

  public ElegirRol() {
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }

}