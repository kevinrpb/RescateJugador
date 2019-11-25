package rescate.ontologia.conceptos;

import java.util.ArrayList;

public class Tablero extends Concepto {

  /*** Constructor ***/
  public Tablero() {
  }

  /*** Atributos ***/
  private Casilla[][] habitaciones;
  private Casilla[][] mapa;

  private int salvados;
  private int victimas;

  private int cubosDaño;
  private int focosCalor;
  private int puntoInteres;
  private int primerosAuxilios;

  private ArrayList<Jugador> jugadores;

  /*** Getters & Setters ***/
  public Casilla[][] getHabitaciones() {
    return habitaciones;
  }

  public void setHabitaciones(Casilla[][] habitaciones) {
    this.habitaciones = habitaciones;
  }

  public int getSalvados() {
    return salvados;
  }

  public void setSalvados(int salvados) {
    this.salvados = salvados;
  }

  public int getVictimas() {
    return victimas;
  }

  public void setVictimas(int victimas) {
    this.victimas = victimas;
  }

  public int getCubosDaño() {
    return cubosDaño;
  }

  public void setCubosDaño(int cubosDaño) {
    this.cubosDaño = cubosDaño;
  }

  public int getFocosCalor() {
    return focosCalor;
  }

  public void setFocosCalor(int focosCalor) {
    this.focosCalor = focosCalor;
  }

  public int getPuntoInteres() {
    return puntoInteres;
  }

  public void setPuntoInteres(int puntoInteres) {
    this.puntoInteres = puntoInteres;
  }

  public int getPrimerosAuxilios() {
    return primerosAuxilios;
  }

  public void setPrimerosAuxilios(int primerosAuxilios) {
    this.primerosAuxilios = primerosAuxilios;
  }

  public ArrayList<Jugador> getJugadores() {
    return jugadores;
  }

  public void setJugadores(ArrayList<Jugador> jugadores) {
    this.jugadores = jugadores;
  }

  public Casilla[][] getMapa() {
    return mapa;
  }

  public void setMapa(Casilla[][] mapa) {
    this.mapa = mapa;
  }

}
