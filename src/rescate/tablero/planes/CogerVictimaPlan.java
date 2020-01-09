package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class CogerVictimaPlan extends Plan {

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
    if (c.getPuntoInteres() == 2 || c.getPuntoInteres() == 3) {
      // Si el jugador ya esta llevando algo
      if (jugador.getLlevandoVictima() != 0 || jugador.getLlevandoMateriaPeligrosa()) {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " ya esta llevando algo");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = peticion.createReply("Refuse_Coger_Victima", accion);
        sendMessage(respuesta);
      }
      // El jugador no lleva nada
      else {
        System.out.println("[INFO] El jugador con id " + idJugador + " ahora lleva una victima");
        // El jugador coge a la victima
        jugador.setLlevandoVictima((c.getPuntoInteres() == 2) ? 1 : 2);
        // Se elimina el PDI de la casilla (aunque no se colocara ninguno hasta que se deje la victima en el exterior)
        c.setPuntoInteres(0);
        // Se actualiza la vista
        ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
        viewUpdater.updateTablero(t);
        getBeliefbase().getBelief("view").setFact(viewUpdater);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = peticion.createReply("Inform_Victima_Cogida", new VictimaCogida());
        sendMessage(respuesta);
      }
    }
    // Si no hay una víctima, no se puede coger
    else {
      System.out.println("[FALLO] En la casilla del jugador no hay una victima que coger");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = peticion.createReply("Failure_Coger_Victima", accion);
      sendMessage(respuesta);
    }


  }

}