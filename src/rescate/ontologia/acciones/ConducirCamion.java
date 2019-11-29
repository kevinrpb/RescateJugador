package rescate.ontologia.acciones;

public class ConducirCamion extends Accion {

  public enum Aparcamiento {
    ARRIBA, DERECHA, ABAJO, IZQUIERDA
  }

  private Aparcamiento destino;

  public ConducirCamion() {
  }

  public Aparcamiento getDestino() {
    return destino;
  }

  public void setDestino(Aparcamiento destino) {
    this.destino = destino;
  }

}