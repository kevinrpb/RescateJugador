package rescate.ontologia.conceptos;

import jadex.adapter.fipa.AgentIdentifier;

public class Jugador extends Concepto {

  /*** Constructor ***/
  public Jugador() {
  }

  /*** Atributos ***/
  private AgentIdentifier idAgente;

  private int rol;

  private int[] posicion;
  private int habitacion;

  private int puntosAccion;
  private int puntosAccionMando;
  private int puntosAccionExtincion;
  private int puntosAccionMovimiento;

  private boolean subidoCamion;
  private boolean subidoAmbulancia;

  private int llevandoVictima;
  private boolean llevandoMateriaPeligrosa;

  /*** Getters & Setters ***/
  public AgentIdentifier getIdAgente() {
    return idAgente;
  }

  public void setIdAgente(AgentIdentifier idAgente) {
    this.idAgente = idAgente;
  }

  public int getRol() {
    return rol;
  }

  public void setRol(int rol) {
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

  public boolean getSubidoCamion() {
    return subidoCamion;
  }

  public void setSubidoCamion(boolean subidoCamion) {
    this.subidoCamion = subidoCamion;
  }
  
  public boolean getSubidoAmbulancia() {
    return subidoAmbulancia;
  }

  public void setSubidoAmbulancia(boolean subidoAmbulancia) {
    this.subidoAmbulancia = subidoAmbulancia;
  }

  public int getLlevandoVictima() {
    return llevandoVictima;
  }

  public void setLlevandoVictima(int llevandoVictima) {
    this.llevandoVictima = llevandoVictima;
  }

  public boolean getLlevandoMateriaPeligrosa() {
    return llevandoMateriaPeligrosa;
  }

  public void setLlevandoMateriaPeligrosa(boolean llevandoMateriaPeligrosa) {
    this.llevandoMateriaPeligrosa = llevandoMateriaPeligrosa;
  }

  public int getHabitacion() {
    return habitacion;
  }

  public void setHabitacion(int habitacion) {
    this.habitacion = habitacion;
  }

}
