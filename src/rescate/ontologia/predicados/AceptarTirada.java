package rescate.ontologia.predicados;

public class AceptarTirada {

  private boolean tiradaAceptada; 
  private int[] tirada;

  public AceptarTirada(){
  }

  public boolean tiradaAceptada() {
    return tiradaAceptada;
  }

  public void setTiradaAceptada(boolean tiradaAceptada) {
    this.tiradaAceptada = tiradaAceptada;
  }

  public int[] getTirada() {
    return tirada;
  }

  public void setTirada(int[] tirada) {
    this.tirada = tirada;
  }
}
