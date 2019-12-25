package rescate.ontologia.predicados;

public class OrdenCompletada extends Predicado {

  private int puntosAccion;

  public OrdenCompletada() {
  } 

  public void setPuntosAccion(int puntosAccion) {
    this.puntosAccion = puntosAccion;
  }

  public int getPuntosAccion() {
    return this.puntosAccion;
  }

}