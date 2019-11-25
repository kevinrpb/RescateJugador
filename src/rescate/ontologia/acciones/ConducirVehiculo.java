package rescate.ontologia.acciones;

public class ConducirVehiculo extends Accion {

  private int[] destino;

  public ConducirVehiculo() {
  }

  public int[] getDestino() {
    return destino;
  }

  public void setDestino(int[] destino) {
    this.destino = destino;
  }

}