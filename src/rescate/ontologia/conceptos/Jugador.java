package rescate.ontologia.conceptos;

import jadex.adapter.fipa.AgentIdentifier;

public class Jugador extends Concepto {

    /*** Constructor ***/
    public Jugador() {
    }

    /*** Atributos ***/
    private AgentIdentifier idAgente;

    private int rol;
    // 0: sanitario, 1: jefe, 2: experto en imgs, 3: espuma ignífuga,
    // 4: materias peligrosas, 5: generalista, 6: rescates, 7: conductor

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