package rescate.ontologia.predicados;

import java.util.ArrayList;

import rescate.ontologia.conceptos.Jugador;

public class RolesDisponibles extends Predicado {

  private ArrayList<Jugador.Rol> rolesDisponibles;

  public RolesDisponibles() {
  }

  public ArrayList<Jugador.Rol> getRolesDisponibles() {
    return rolesDisponibles;
  }

  public void setRolesDisponibles(ArrayList<Jugador.Rol> rolesDisponibles) {
    this.rolesDisponibles = rolesDisponibles;
  }

}