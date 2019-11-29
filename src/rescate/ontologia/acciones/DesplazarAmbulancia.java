package rescate.ontologia.acciones;

public class DesplazarAmbulancia extends Accion {

  public enum Aparcamiento {
    ARRIBA, DERECHA, ABAJO, IZQUIERDA
  }

  private Aparcamiento destino;

  public DesplazarAmbulancia() {
  }

  public Aparcamiento getDestino() {
    return destino;
  }

  public void setDestino(Aparcamiento destino) {
    this.destino = destino;
  }

}