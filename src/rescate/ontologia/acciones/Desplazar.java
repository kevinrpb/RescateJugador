package rescate.ontologia.acciones;

public class Desplazar extends Accion {

  private int[] destino;

  public Desplazar() {
  }

  public int[] getDestino() {
    return destino;
  }

  public void setDestino(int[] destino) {
    this.destino = destino;
  }

}