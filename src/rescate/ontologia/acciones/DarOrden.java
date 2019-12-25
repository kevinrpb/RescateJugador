package rescate.ontologia.acciones;
import jadex.adapter.fipa.*;


public class DarOrden extends Accion {


  private int accion; 
  private int conexion;
  private AgentIdentifier idJugador;

  public DarOrden(){
  }

  public int getAccion() {
    return accion;
  }

  public void setAccion(int accion) {
    this.accion = accion;
  }

  public int getConexion() {
    return conexion;
  }

  public void setConexion(int conexion) {
    this.conexion = conexion;
  }

  public AgentIdentifier getIdJugador() {
    return idJugador;
  }

  public void setIdJugador(AgentIdentifier idJugador) {
    this.idJugador = idJugador;
  }

}
