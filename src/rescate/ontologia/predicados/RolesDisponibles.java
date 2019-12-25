package rescate.ontologia.predicados;

import java.util.ArrayList;


public class RolesDisponibles extends Predicado {

  private ArrayList<Integer> rolesDisponibles;

  public RolesDisponibles() {
  }

  public ArrayList<Integer> getRolesDisponibles() {
    return rolesDisponibles;
  }

  public void setRolesDisponibles(ArrayList<Integer> rolesDisponibles) {
    this.rolesDisponibles = rolesDisponibles;
  }

}