package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class DejarVictimaPlan extends Plan {

	@Override
	public void body() {

    System.out.println("[PLAN] El tablero recibe petición de dejar a una victima");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    DejarVictima accion = (DejarVictima) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Si no hay un PDI en la casilla
    if (c.getPuntoInteres() == Casilla.PuntoInteres.NADA) {
      // Si el jugador lleva a una victima
      if (jugador.llevandoVictima() != Jugador.LlevandoVictima.NO) {
        System.out.println("[INFO] El jugador con id " + idJugador + " ha dejado una victima en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        // Si deja la victima en una ambulancia
        if (c.esAmbulancia()) {
          // Se reduce en uno los PDI en el tablero
          getBeliefbase().getBelief("PDITablero").setFact((int) getBeliefbase().getBelief("PDITablero").getFact() - 1);
          // Se aumenta en uno las victimas salvadas
          getBeliefbase().getBelief("salvados").setFact((int) getBeliefbase().getBelief("salvados").getFact() + 1);
        } else {
          // Se añade el PDI a la casilla
          c.setPuntoInteres((jugador.llevandoVictima() == Jugador.LlevandoVictima.SI) ? Casilla.PuntoInteres.VICTIMA : Casilla.PuntoInteres.VICTIMA_CURADA);       
        }
        // El jugador deja a la victima
        jugador.setLlevandoVictima(Jugador.LlevandoVictima.NO);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = createMessageEvent("Inform_Victima_Dejada");
        respuesta.setContent(new VictimaDejada());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // El jugador no lleva nada
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no lleva una victima");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Dejar_Victima");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    }
    // Si ya hay un PDI en la casilla, no se puede dejar
    else {
      System.out.println("[FALLO] En la casilla del jugador ya hay un PDI");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Dejar_Victima");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }


  }

}