package rescate.ontologia.predicados;

import rescate.ontologia.conceptos.Tablero;

public class Informacion extends Predicado {

  private Tablero mapa;
  private int salvados;

  public Informacion() {
  }

  public Tablero getMapa() {
    return mapa;
  }

  public void setMapa(Tablero mapa) {
    this.mapa = mapa;
  }

  public int getSalvados() {
    return salvados;
  }

  public void setSalvados(int salvados) {
    this.salvados = salvados;
  }

}