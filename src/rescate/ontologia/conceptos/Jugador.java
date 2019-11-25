package rescate.ontologia.conceptos;

import jadex.adapter.fipa.AgentIdentifier;

public class Jugador extends Concepto {

  /*** Enums ***/
  public enum Rol {
    SANITARIO, JEFE, EXPERTO_EN_IMAGENES, ESPUMA_IGNIFUGA, MATERIAS_PELIGROSAS, GENERALISTA, RESCATES, CONDUCTOR
  }

  /*** Constructor ***/
  public Jugador() {
  }

  /*** Atributos ***/
  private AgentIdentifier idAgente;

  private Rol rol;

  private int[] posicion;

  private int puntosAccion;
  private int puntosAccionMando;
  private int puntosAccionExtincion;
  private int puntosAccionMovimiento;

  private boolean subidoCamion;

  private boolean llevandoVictima;
  private boolean llevandoMateriaPeligrosa;

  /*** Getters & Setters ***/
  public AgentIdentifier getIdAgente() {
    return idAgente;
  }

  public void setIdAgente(AgentIdentifier idAgente) {
    this.idAgente = idAgente;
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }

  public int[] getPosicion() {
    return posicion;
  }

  public void setPosicion(int[] posicion) {
    this.posicion = posicion;
  }

  public int getPuntosAccion() {
    return puntosAccion;
  }

  public void setPuntosAccion(int puntosAccion) {
    this.puntosAccion = puntosAccion;
  }

  public int getPuntosAccionMando() {
    return puntosAccionMando;
  }

  public void setPuntosAccionMando(int puntosAccionMando) {
    this.puntosAccionMando = puntosAccionMando;
  }

  public int getPuntosAccionExtincion() {
    return puntosAccionExtincion;
  }

  public void setPuntosAccionExtincion(int puntosAccionExtincion) {
    this.puntosAccionExtincion = puntosAccionExtincion;
  }

  public int getPuntosAccionMovimiento() {
    return puntosAccionMovimiento;
  }

  public void setPuntosAccionMovimiento(int puntosAccionMovimiento) {
    this.puntosAccionMovimiento = puntosAccionMovimiento;
  }

  public boolean subidoCamion() {
    return subidoCamion;
  }

  public void setSubidoCamion(boolean subidoCamion) {
    this.subidoCamion = subidoCamion;
  }

  public boolean llevandoVictima() {
    return llevandoVictima;
  }

  public void setLlevandoVictima(boolean llevandoVictima) {
    this.llevandoVictima = llevandoVictima;
  }

  public boolean llevandoMateriaPeligrosa() {
    return llevandoMateriaPeligrosa;
  }

  public void setLlevandoMateriaPeligrosa(boolean llevandoMateriaPeligrosa) {
    this.llevandoMateriaPeligrosa = llevandoMateriaPeligrosa;
  }

}
