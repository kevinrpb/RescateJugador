package rescate.jugador.util;

import java.util.HashMap;

import rescate.ontologia.conceptos.*;

public class Info {

  private final int[][][] modeloTablero = new int[][][] {
    { { 0, 0, 0, 0, 8, 0, 0, 0 }, { 0, 0, 3, 0, 8, 0, 0, 0 }, { 0, 0, 3, 0, 8, 0, 0, 0 }, { 0, 0, 3, 0, 8, 0, 0, 0 }, { 0, 0, 3, 0, 8, 0, 0, 0 }, { 0, 0, 3, 0, 8, 0, 1, 0 }, { 0, 0, 1, 0, 8, 0, 1, 0 },{ 0, 0, 3, 0, 8, 1, 0, 0 }, { 0, 0, 3, 0, 8, 1, 0, 0 }, { 0, 0, 0, 0, 8, 0, 0, 0 } },
    { { 0, 3, 0, 0, 8, 1, 0, 0 }, { 3, 0, 0, 3, 3, 0, 0, 1 }, { 3, 0, 0, 0, 4, 0, 0, 1 }, { 3, 2, 0, 0, 4, 0, 0, 1 }, { 3, 0, 0, 2, 4, 0, 0, 2 }, { 3, 3, 0, 0, 4, 0, 0, 2 }, { 1, 0, 0, 3, 4, 0, 0, 3 },{ 3, 0, 0, 0, 4, 0, 0, 3 }, { 3, 3, 0, 0, 5, 0, 0, 3 }, { 0, 0, 0, 3, 8, 0, 0, 0 } },
    { { 0, 3, 0, 0, 8, 1, 0, 0 }, { 0, 0, 0, 3, 2, 0, 0, 1 }, { 0, 0, 0, 0, 3, 0, 0, 1 }, { 0, 3, 3, 0, 6, 0, 0, 1 },{ 0, 0, 3, 3, 4, 0, 0, 2 }, { 0, 2, 3, 0, 4, 0, 0, 2 }, { 0, 0, 3, 2, 2, 0, 0, 3 },{ 0, 0, 3, 0, 5, 0, 0, 3 }, { 0, 3, 2, 0, 6, 0, 0, 3 }, { 0, 0, 0, 3, 8, 0, 0, 0 } },
    { { 0, 1, 0, 0, 8, 0, 1, 0 }, { 0, 0, 0, 1, 2, 0, 0, 1 }, { 0, 2, 0, 0, 0, 0, 0, 1 }, { 3, 0, 0, 2, 4, 0, 0, 4 },{ 3, 0, 0, 0, 6, 0, 0, 4 }, { 3, 0, 0, 0, 6, 0, 0, 4 }, { 3, 3, 0, 0, 6, 0, 0, 4 },{ 3, 0, 0, 3, 0, 0, 0, 5 }, { 2, 3, 0, 0, 6, 0, 0, 5 }, { 0, 0, 0, 3, 8, 0, 1, 0 } },
    { { 0, 3, 0, 0, 8, 0, 1, 0 }, { 0, 0, 3, 3, 2, 0, 0, 1 }, { 0, 3, 3, 0, 4, 0, 0, 1 }, { 0, 0, 3, 3, 2, 0, 0, 4 },{ 0, 0, 2, 0, 2, 0, 0, 4 }, { 0, 0, 3, 0, 2, 0, 0, 4 }, { 0, 2, 3, 0, 0, 0, 0, 4 },{ 0, 0, 3, 2, 4, 0, 0, 5 }, { 0, 1, 3, 0, 6, 0, 0, 5 }, { 0, 0, 0, 1, 8, 0, 1, 0 } },
    { { 0, 3, 0, 0, 8, 0, 0, 0 }, { 3, 0, 0, 3, 2, 0, 0, 6 }, { 3, 0, 0, 0, 1, 0, 0, 6 }, { 3, 0, 0, 0, 6, 0, 0, 6 },{ 2, 0, 0, 0, 0, 0, 0, 6 }, { 3, 3, 0, 0, 0, 0, 0, 6 }, { 3, 0, 0, 3, 2, 0, 0, 7 },{ 3, 3, 0, 0, 7, 0, 0, 7 }, { 3, 3, 0, 3, 6, 0, 0, 8 }, { 0, 0, 0, 3, 8, 1, 0, 0 } },
    { { 0, 3, 0, 0, 8, 0, 0, 0 }, { 0, 0, 3, 3, 1, 0, 0, 6 }, { 0, 0, 3, 0, 0, 0, 0, 6 }, { 0, 0, 1, 0, 0, 0, 0, 6 },{ 0, 0, 3, 0, 0, 0, 0, 6 }, { 0, 2, 3, 0, 0, 0, 0, 6 }, { 0, 0, 3, 2, 0, 0, 0, 7 },{ 0, 2, 3, 0, 0, 0, 0, 7 }, { 0, 3, 3, 2, 7, 0, 0, 8 }, { 0, 0, 0, 3, 8, 1, 0, 0 } },
    { { 0, 0, 0, 0, 8, 0, 0, 0 }, { 3, 0, 0, 0, 8, 1, 0, 0 }, { 3, 0, 0, 0, 8, 1, 0, 0 }, { 1, 0, 0, 0, 8, 0, 1, 0 },{ 3, 0, 0, 0, 8, 0, 1, 0 }, { 3, 0, 0, 0, 8, 0, 0, 0 }, { 3, 0, 0, 0, 8, 0, 0, 0 },{ 3, 0, 0, 0, 8, 0, 0, 0 }, { 3, 0, 0, 0, 8, 0, 0, 0 }, { 0, 0, 0, 0, 8, 0, 0, 0 } },
  };

  private Integer turno;
  private HashMap<Integer, Casilla[][]> historialTablero;

  public Info() {
    // Turno inicial a cero
    turno = 0;

    // Para el historial, se pone en turno cero el mapa vacío para usar como base
    Casilla[][] mapaBase = new Casilla[modeloTablero.length][modeloTablero[0].length];

    for (int i = 0; i < modeloTablero.length; i++) {
      for (int j = 0; j < modeloTablero[i].length; j++) {
        mapaBase[i][j] = new Casilla();
        mapaBase[i][j].setPosicion(new int[] { j, i });
        mapaBase[i][j].setConexiones(new int[] { modeloTablero[i][j][0], modeloTablero[i][j][1], modeloTablero[i][j][2], modeloTablero[i][j][3] });
        mapaBase[i][j].setFlecha(modeloTablero[i][j][4]);
        mapaBase[i][j].setEsAparcamientoCamion(modeloTablero[i][j][5] == 1);
        mapaBase[i][j].setEsAparcamientoAmbulancia(modeloTablero[i][j][6] == 1);
        mapaBase[i][j].setHabitacion(modeloTablero[i][j][7]);
        mapaBase[i][j].setTieneFuego(0);
        mapaBase[i][j].setPuntoInteres(0);
      }
    }

    historialTablero = new HashMap<Integer, Casilla[][]>();
    historialTablero.put(0, mapaBase);

  }

  public Integer getTurno() {
    return turno;
  }

  public void setTurno(Integer turno) {
    this.turno = turno;
  }

  public HashMap<Integer, Casilla[][]> getHistorial() {
    return historialTablero;
  }

  public Casilla[][] getHistorial(Integer turno) {
    return historialTablero.get(turno);
  }

  public Casilla[][] getMapaBase() {
    return historialTablero.get(0);
  }

  public void setHistorial(Integer turno, Casilla[][] historial) {
    // Si ya tenemos este turno guardado, lo reemplazamos. Sino lo añadimos
    if (historialTablero.keySet().contains(turno))
      historialTablero.replace(turno, historial);
    else
      historialTablero.put(turno, historial);
  }

}
