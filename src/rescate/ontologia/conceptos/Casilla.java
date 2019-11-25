package rescate.ontologia.conceptos;

public class Casilla extends Concepto {

  /*** Enums ***/
  public enum Conexion {
    NADA, PUERTA_ABIERTA, PUERTA_CERRADA, PARED, PARED_SEMIRROTA, PARED_ROTA
  }

  public enum PuntoInteres {
    OCULTO, FALSA_ALARMA, VICTIMA
  }

  /*** Constructor ***/
  public Casilla() {
  }

  /*** Atributos ***/
  private int[] posicion; // [x, y]
  private Conexion[] conexiones;

  private int tieneFuego; // 0: nada, 1: humo, 2: fuego
  private boolean tieneMateriaPeligrosa;
  private boolean tieneFocoCalor;
  private PuntoInteres puntoInteres;

  private boolean[] flecha; // 0: arriba, 1: derecha, 2: abajo, 3: izquierda

  private boolean camionBomberos;
  private boolean ambulancia;
  private boolean esAparcamientoCamion;
  private boolean esAparcamientoAmbulancia;

  /*** Getters & Setters ***/
  public int[] getPosicion() {
    return posicion;
  }

  public void setPosicion(int[] posicion) {
    this.posicion = posicion;
  }

  public Conexion[] getConexiones() {
    return conexiones;
  }

  public void setConexiones(Conexion[] conexiones) {
    this.conexiones = conexiones;
  }

  public int tieneFuego() {
    return tieneFuego;
  }

  public void setTieneFuego(int tieneFuego) {
    this.tieneFuego = tieneFuego;
  }

  public boolean tieneMateriaPeligrosa() {
    return tieneMateriaPeligrosa;
  }

  public void setTieneMateriaPeligrosa(boolean tieneMateriaPeligrosa) {
    this.tieneMateriaPeligrosa = tieneMateriaPeligrosa;
  }

  public boolean tieneFocoCalor() {
    return tieneFocoCalor;
  }

  public void setTieneFocoCalor(boolean tieneFocoCalor) {
    this.tieneFocoCalor = tieneFocoCalor;
  }

  public PuntoInteres getPuntoInteres() {
    return puntoInteres;
  }

  public void setPuntoInteres(PuntoInteres puntoInteres) {
    this.puntoInteres = puntoInteres;
  }

  public boolean[] getFlecha() {
    return flecha;
  }

  public void setFlecha(boolean[] flecha) {
    this.flecha = flecha;
  }

  public boolean isCamionBomberos() {
    return camionBomberos;
  }

  public void setCamionBomberos(boolean camionBomberos) {
    this.camionBomberos = camionBomberos;
  }

  public boolean isAmbulancia() {
    return ambulancia;
  }

  public void setAmbulancia(boolean ambulancia) {
    this.ambulancia = ambulancia;
  }

  public boolean esAparcamientoCamion() {
    return esAparcamientoCamion;
  }

  public void setEsAparcamientoCamion(boolean esAparcamientoCamion) {
    this.esAparcamientoCamion = esAparcamientoCamion;
  }

  public boolean esAparcamientoAmbulancia() {
    return esAparcamientoAmbulancia;
  }

  public void setEsAparcamientoAmbulancia(boolean esAparcamientoAmbulancia) {
    this.esAparcamientoAmbulancia = esAparcamientoAmbulancia;
  }

}
