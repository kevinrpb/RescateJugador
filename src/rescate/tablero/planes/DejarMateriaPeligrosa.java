package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class DejarMateriaPeligrosa extends Plan {

	@Override
	public void body() {

    System.out.println("[PLAN] El tablero recibe petición de dejar una materia peligrosa");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    DejarMateriaPeligrosa accion = (DejarMateriaPeligrosa) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Si no hay materia peligrosa en la casilla
    if (!c.tieneMateriaPeligrosa()) {
      // Si el jugador lleva una materia peligrosa
      if (jugador.llevandoMateriaPeligrosa()) {
        System.out.println("[INFO] El jugador con id " + idJugador + " ha dejado una materia peligrosa en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        // Si se deja en el exterior
        if (c.getHabitacion() == 0) { 
          System.out.println("[INFO] El jugador con id " + idJugador + " ha eliminado una materia peligrosa");
        }
        // Si no se deja en el exterior
        else {
          // Se añade el PDI a la casilla
          c.setTieneMateriaPeligrosa(true); 
        }
        // El jugador deja la materia peligrosa
        jugador.setLlevandoMateriaPeligrosa(false);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = createMessageEvent("Inform_Materia_Peligrosa_Dejada");
        respuesta.setContent(new MateriaPeligrosaDejada());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // El jugador no lleva nada
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no lleva una materia peligrosa");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Dejar_Materia_Peligrosa");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    }
    // Si ya hay una materia peligrosa en la casilla, no se puede dejar
    else {
      System.out.println("[FALLO] En la casilla del jugador ya hay una materia peligrosa");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Dejar_Materia_Peligrosa");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }


  }

}