package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class EliminarMateriaPeligrosaPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de atender víctima");

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


    if (!c.tieneMateriaPeligrosa()) {
      System.out.println("[FALLO] La casilla sobre la que esta el jugador " + idJugador + " no tiene materia peligrosa");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Eliminar_Materia_Peligrosa");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }
    else if (jugador.getRol() != Jugador.Rol.MATERIAS_PELIGROSAS) {
      System.out.println("[FALLO] El jugador con id " + idJugador + " no tiene el rol necesario para eliminar materia peligrosa (Experto en Materias Peligrosas)");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Eliminar_Materia_Peligrosa");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }
    else {
      // Si tiene PA suficientes y hay suficientes fichas de primeros auxilios...
      if (jugador.getPuntosAccion() > 2) {
        System.out.println("[INFO] El jugador con id " + idJugador + " eliminar una materia peligrosa en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        // Se actualiza el PDI
        c.setTieneMateriaPeligrosa(false);
        // Se reduce los puntos de acción del jugador 
        jugador.setPuntosAccion(jugador.getPuntosAccion()-2);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = createMessageEvent("Inform_Materia_Peligrosa_Eliminada");
        respuesta.setContent(new MateriaPeligrosaEliminada());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // En caso contrario...
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA para eliminar materia peligrosa");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Eliminar_Materia_Peligrosa");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    }

    

  }

}
