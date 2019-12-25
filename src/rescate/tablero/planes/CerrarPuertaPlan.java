package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class CerrarPuertaPlan extends Plan {

	@Override
	public void body() {

    System.out.println("[PLAN] El tablero recibe peticion de cerrar una puerta");
    
    // Peticion
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    CerrarPuerta accion = (CerrarPuerta) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la peticion
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Si la conexion no es una puerta abierta...
		if (c.getConexiones()[accion.getConexion()] != 1) {
      System.out.println("[FALLO] No hay una puerta abierta en la conexion indicada de la casilla del jugador");
      // Se rechaza la peticion de accion del jugador
      IMessageEvent respuesta = peticion.createReply("Failure_Cerrar_Puerta", accion);
      sendMessage(respuesta);
    } 
    // Si la conexion es una puerta abierta...
    else {
      // No tiene PA suficientes...
      if (jugador.getPuntosAccion() < 1) {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA para cerrar una puerta");
        // Se rechaza la peticion de accion del jugador
        IMessageEvent respuesta = peticion.createReply("Refuse_Cerrar_Puerta", accion);
        sendMessage(respuesta);
      }
      // PA suficientes...
      else {
        System.out.println("[INFO] El jugador con id " + idJugador + " ha cerrado una puerta en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        // Se modifica la conexion a puerta abierta
        c.getConexiones()[accion.getConexion()] = 2;
        // Casilla colindante (donde también esta la referencia a la puerta abierta y hay que cerrarla)
        Casilla colindante = null;
        switch (accion.getConexion()) {
          // Arriba
          case 0:
            colindante = t.getMapa()[c.getPosicion()[1] - 1][c.getPosicion()[0]];
            colindante.getConexiones()[2] = 2;
            break;
          // Derecha
          case 1:
            colindante = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] + 1];
            colindante.getConexiones()[3] = 2;
            break;
          // Abajo
          case 2:
            colindante = t.getMapa()[c.getPosicion()[1] + 1][c.getPosicion()[0]];
            colindante.getConexiones()[0] = 2;
            break;
          // Izquierda
          case 3:
            colindante = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] - 1];
            colindante.getConexiones()[1] = 2;
            break;
          // ...
          default:
            break;
        }
        // Se actualiza el jugador (consumo de PA)
        jugador.setPuntosAccion(jugador.getPuntosAccion() - 1);
        // Se actualiza la vista
        ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
        viewUpdater.updateTablero(t);
        getBeliefbase().getBelief("view").setFact(viewUpdater);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la accion ha sido llevada a cabo
        IMessageEvent respuesta = peticion.createReply("Inform_Puerta_Cerrada", new PuertaCerrada());
        sendMessage(respuesta);
      }
		}
	}

}
