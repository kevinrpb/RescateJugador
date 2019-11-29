package rescate.ontologia.acciones;

public class Desplazar extends Accion {

  public enum Direccion {
    ARRIBA, DERECHA, ABAJO, IZQUIERDA
  }

  private Direccion direccion;

  public Desplazar() {
  }

  public Direccion getDireccion() {
    return direccion;
  }

  public void setDireccion(Direccion direccion) {
    this.direccion = direccion;
  }

}