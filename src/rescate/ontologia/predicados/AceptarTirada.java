package rescate.ontologia.predicados;

public class AceptarTirada {

  public Boolean tirada_aceptada; 
  public int[] tirada;


  public AceptarTirada(int [] tirada){
    this.tirada = tirada;
    tirada_aceptada = false;

  }
}
