package rescate.ontologia.conceptos;

public class Casilla extends Concepto {

  /*** Enums ***/
  public enum Conexion {
    NADA, PUERTA_ABIERTA, PUERTA_CERRADA, PARED, PARED_SEMIRROTA, PARED_ROTA
  }

	public enum PuntoInteres {
		NADA, OCULTO, VICTIMA, VICTIMA_CURADA
	}

  public enum Direccion {
    ARRIBA, ARRIBA_DERECHA, DERECHA, ABAJO_DERECHA, ABAJO, ABAJO_IZQUIERDA, IZQUIERDA, ARRIBA_IZQUIERDA, NADA
  }

  public enum Fuego {
    NADA, HUMO, FUEGO
  }

  /*** Constructor ***/
  public Casilla() {
  }

  /*** Atributos ***/
  private int[] posicion; // [x, y]
  private Conexion[] conexiones; // 0: arriba, 1: derecha, 2: abajo, 3: izquierda

  private Fuego tieneFuego;
  private boolean tieneMateriaPeligrosa;
  private boolean tieneFocoCalor;

  private PuntoInteres puntoInteres;

  private Direccion flecha;

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

  public Conexion[] getConexiones() {
    return conexiones;
  }

  public void setConexiones(Conexion[] conexiones) {
    this.conexiones = conexiones;
  }

  public Boolean tieneAdyacencia(int c) {
    return conexiones[c] == Conexion.PUERTA_ABIERTA ||
        conexiones[c] == Conexion.PARED_ROTA ||
        conexiones[c] == Conexion.NADA;
  }

  public Boolean conexionEsPuerta(int c) {
    return conexiones[c] == Conexion.PUERTA_CERRADA ||
        conexiones[c] == Conexion.PUERTA_ABIERTA;
  }

  // Este método no devuelve si la pared está rota ya que a efectos prácticos es como si no hubiese nada
  // Este método se usa principalmente para calcular efectos de epxlosiones
  public Boolean conexionEsPared(int c) {
    return conexiones[c] == Conexion.PARED ||
        conexiones[c] == Conexion.PARED_SEMIRROTA;
  }

  public Conexion dannarConexion(int c) {
    switch (conexiones[c]) {
      case PARED:
        conexiones[c] = Conexion.PARED_SEMIRROTA;
        return Conexion.PARED_SEMIRROTA;
      case PARED_SEMIRROTA:
        conexiones[c] = Conexion.PARED_ROTA;
        return Conexion.PARED_ROTA;
      case PUERTA_ABIERTA:
        conexiones[c] = Conexion.NADA;
        return Conexion.NADA;
      case PUERTA_CERRADA:
        conexiones[c] = Conexion.NADA;
        return Conexion.NADA;
      default:
        return Conexion.NADA;
    }
  }

  public Fuego tieneFuego() {
    return tieneFuego;
  }

  public void setTieneFuego(Fuego tieneFuego) {
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

  public Direccion getFlecha() {
    return flecha;
  }

  public void setFlecha(Direccion flecha) {
    this.flecha = flecha;
  }

  public boolean esCamionBomberos() {
    return camionBomberos;
  }

  public void setCamionBomberos(boolean camionBomberos) {
    this.camionBomberos = camionBomberos;
  }

  public boolean esAmbulancia() {
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

}
