package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class EliminarMateriaPeligrosaPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de eliminar materia peligrosa");

    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    EliminarMateriaPeligrosa accion = (EliminarMateriaPeligrosa) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];


    if (!c.getTieneMateriaPeligrosa()) {
      System.out.println("[FALLO] La casilla sobre la que esta el jugador " + idJugador + " no tiene materia peligrosa");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = peticion.createReply("Failure_Eliminar_Materia_Peligrosa", accion);
      sendMessage(respuesta);
    }
    else if (jugador.getRol() != 5) {
      System.out.println("[FALLO] El jugador con id " + idJugador + " no tiene el rol necesario para eliminar materia peligrosa (Experto en Materias Peligrosas)");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = peticion.createReply("Failure_Eliminar_Materia_Peligrosa", accion);
      sendMessage(respuesta);
    }
    else {
      // Si tiene PA suficientes y hay suficientes fichas de primeros auxilios...
      if (jugador.getPuntosAccion() > 1) {
        System.out.println("[INFO] El jugador con id " + idJugador + " eliminar una materia peligrosa en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        // Se actualiza el PDI
        c.setTieneMateriaPeligrosa(false);
        // Se reduce los puntos de acción del jugador 
        jugador.setPuntosAccion(jugador.getPuntosAccion() - 2);
        // Se actualiza la vista
        ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
        viewUpdater.updateTablero(t);
        getBeliefbase().getBelief("view").setFact(viewUpdater);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = peticion.createReply("Inform_Materia_Peligrosa_Eliminada", new MateriaPeligrosaEliminada());
        sendMessage(respuesta);
      }
      // En caso contrario...
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA para eliminar materia peligrosa");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = peticion.createReply("Refuse_Eliminar_Materia_Peligrosa", accion);
        sendMessage(respuesta);
      }
    }

    

  }

}
