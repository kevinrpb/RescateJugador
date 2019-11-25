package rescate.ontologia.acciones;

import rescate.ontologia.conceptos.Jugador;

public class DarOrden extends Accion {

  private Jugador recibeOrden;
  private Accion accion;

  public DarOrden() {
  }

  public Jugador getRecibeOrden() {
    return recibeOrden;
  }

  public void setRecibeOrden(Jugador recibeOrden) {
    this.recibeOrden = recibeOrden;
  }

  public Accion getAccion() {
    return accion;
  }

  public void setAccion(Accion accion) {
    this.accion = accion;
  }

}