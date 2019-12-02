package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class CogerVictimaPlan extends Plan {

	@Override
	public void body() {

    System.out.println("[PLAN] El tablero recibe petición de coger a una victima");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    CogerVictima accion = (CogerVictima) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Si hay una víctima en la casilla
    if (c.getPuntoInteres() == Casilla.PuntoInteres.VICTIMA || c.getPuntoInteres() == Casilla.PuntoInteres.VICTIMA_CURADA) {
      // Si el jugador ya esta llevando algo
      if (jugador.llevandoVictima() != Jugador.LlevandoVictima.NO || jugador.llevandoMateriaPeligrosa()) {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " ya esta llevando algo");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Coger_Victima");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // El jugador no lleva nada
      else {
        System.out.println("[INFO] El jugador con id " + idJugador + " ahora lleva una victima");
        // El jugador coge a la victima
        jugador.setLlevandoVictima((c.getPuntoInteres() == Casilla.PuntoInteres.VICTIMA) ? Jugador.LlevandoVictima.SI : Jugador.LlevandoVictima.CURADA);
        // Se elimina el PDI de la casilla (aunque no se colocara ninguno hasta que se deje la victima en el exterior)
        c.setPuntoInteres(Casilla.PuntoInteres.NADA);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = createMessageEvent("Inform_Victima_Cogida");
        respuesta.setContent(new VictimaCogida());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    }
    // Si no hay una víctima, no se puede coger
    else {
      System.out.println("[FALLO] En la casilla del jugador no hay una victima que coger");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Coger_Victima");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }


  }

}