package rescate.ontologia.acciones;
import jadex.adapter.fipa.*;


public class DarOrden extends Accion {

  public enum Mandato {
    MOVER,ABRIR,CERRAR;
 }

 private Mandato accion; 
 private int conexion;
 AgentIdentifier idJugador;

 public DarOrden(){
 }

  public Mandato getAccion() {
    return accion;
  }

  public void setAccion(Mandato accion) {
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
