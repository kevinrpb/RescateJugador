package rescate.ontologia.predicados;

public class PuntoInteresIdentificado extends Predicado {

  private boolean victima;

  public PuntoInteresIdentificado() {
  }

  public boolean esVictima() {
    return victima;
  }

  public void setVictima(boolean victima) {
    this.victima = victima;
  }

}