package rescate.ontologia.conceptos;

public class Casilla extends Concepto {

  /*** Constructor ***/
  public Casilla() {
  }

  /*** Atributos ***/
  private int[] posicion; // [x, y]
  private int[] conexiones; // 0: arriba, 1: derecha, 2: abajo, 3: izquierda

  private int tieneFuego;
  private boolean tieneMateriaPeligrosa;
  private boolean tieneFocoCalor;

  private int puntoInteres;

  private int flecha;

  private boolean camionBomberos;
  private boolean ambulancia;
  private boolean esAparcamientoCamion;
  private boolean esAparcamientoAmbulancia;

  private int habitacion;

  /*** Getters & Setters ***/
  public int[] getPosicion() {
    return posicion;
  }

  public void setPosicion(int[] posicion) {
    this.posicion = posicion;
  }

  public int[] getConexiones() {
    return conexiones;
  }

  public void setConexiones(int[] conexiones) {
    this.conexiones = conexiones;
  }

  public int getTieneFuego() {
    return tieneFuego;
  }

  public void setTieneFuego(int tieneFuego) {
    this.tieneFuego = tieneFuego;
  }

  public boolean getTieneMateriaPeligrosa() {
    return tieneMateriaPeligrosa;
  }

  public void setTieneMateriaPeligrosa(boolean tieneMateriaPeligrosa) {
    this.tieneMateriaPeligrosa = tieneMateriaPeligrosa;
  }

  public boolean getTieneFocoCalor() {
    return tieneFocoCalor;
  }

  public void setTieneFocoCalor(boolean tieneFocoCalor) {
    this.tieneFocoCalor = tieneFocoCalor;
  }

  public int getPuntoInteres() {
    return puntoInteres;
  }

  public void setPuntoInteres(int puntoInteres) {
    this.puntoInteres = puntoInteres;
  }

  public int getFlecha() {
    return flecha;
  }

  public void setFlecha(int flecha) {
    this.flecha = flecha;
  }

  public boolean getCamionBomberos() {
    return camionBomberos;
  }

  public void setCamionBomberos(boolean camionBomberos) {
    this.camionBomberos = camionBomberos;
  }

  public boolean getAmbulancia() {
    return ambulancia;
  }

  public void setAmbulancia(boolean ambulancia) {
    this.ambulancia = ambulancia;
  }

  public boolean getEsAparcamientoCamion() {
    return esAparcamientoCamion;
  }

  public void setEsAparcamientoCamion(boolean esAparcamientoCamion) {
    this.esAparcamientoCamion = esAparcamientoCamion;
  }

  public boolean getEsAparcamientoAmbulancia() {
    return esAparcamientoAmbulancia;
  }

  public void setEsAparcamientoAmbulancia(boolean esAparcamientoAmbulancia) {
    this.esAparcamientoAmbulancia = esAparcamientoAmbulancia;
  }

  public int getHabitacion() {
    return habitacion;
  }

  public void setHabitacion(int habitacion) {
    this.habitacion = habitacion;
  }

  public boolean esColindante(Casilla c) {
    return (getPosicion()[0] == c.getPosicion()[0] && getPosicion()[1] == c.getPosicion()[1] - 1)
        || (getPosicion()[0] == c.getPosicion()[0] && getPosicion()[1] == c.getPosicion()[1] + 1)
        || (getPosicion()[0] == c.getPosicion()[0] - 1 && getPosicion()[1] == c.getPosicion()[1])
        || (getPosicion()[0] == c.getPosicion()[0] + 1 && getPosicion()[1] == c.getPosicion()[1]);
  }

  public boolean mismaPosicion(Casilla c) {
    return (getPosicion()[0] == c.getPosicion()[0] && getPosicion()[1] == c.getPosicion()[1]);
  }

  public boolean mismaPosicion(Jugador j) {
    return (getPosicion()[0] == j.getPosicion()[0] && getPosicion()[1] == j.getPosicion()[1]);
  }
  
  public void dannarConexion(int c) {
    switch (conexiones[c]) {
      case 3:
        conexiones[c] = 4;
        break;
      case 4:
        conexiones[c] = 5;
        break;
      case 1:
        conexiones[c] = 0;
        break;
      case 2:
        conexiones[c] = 0;
        break;
      default:
        break;
    }
  }

}
