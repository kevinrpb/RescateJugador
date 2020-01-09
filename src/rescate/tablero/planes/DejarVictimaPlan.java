package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class DejarVictimaPlan extends Plan {

	@Override
	public void body() {

    System.out.println("[PLAN] El tablero recibe peticion de dejar a una victima");
    
    // Peticion
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    DejarVictima accion = (DejarVictima) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la peticion
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Si no hay un PDI en la casilla
    if (c.getPuntoInteres() == 0) {
      // Si el jugador lleva a una victima
      if (jugador.getLlevandoVictima() != 0) {
        System.out.println("[INFO] El jugador con id " + idJugador + " ha dejado una victima en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        // Si deja la victima en una ambulancia
        if (c.getAmbulancia()) {
          // Se reduce en uno los PDI en el tablero
          getBeliefbase().getBelief("PDITablero").setFact((int) getBeliefbase().getBelief("PDITablero").getFact() - 1);
          // Se aumenta en uno las victimas salvadas
          getBeliefbase().getBelief("salvados").setFact((int) getBeliefbase().getBelief("salvados").getFact() + 1);
        } else {
          // Se añade el PDI a la casilla
          c.setPuntoInteres((jugador.getLlevandoVictima() == 1) ? 2 : 3);       
        }
        // El jugador deja a la victima
        jugador.setLlevandoVictima(0);
        // Se actualiza la vista
        ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
        viewUpdater.updateTablero(t);
        getBeliefbase().getBelief("view").setFact(viewUpdater);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la accion ha sido llevada a cabo
        IMessageEvent respuesta = peticion.createReply("Inform_Victima_Dejada", new VictimaDejada());
        sendMessage(respuesta);
      }
      // El jugador no lleva nada
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no lleva una victima");
        // Se rechaza la peticion de accion del jugador
        IMessageEvent respuesta = peticion.createReply("Refuse_Dejar_Victima", accion);
        sendMessage(respuesta);
      }
    }
    // Si ya hay un PDI en la casilla, no se puede dejar
    else {
      System.out.println("[FALLO] En la casilla del jugador ya hay un PDI");
      // Se rechaza la peticion de accion del jugador
      IMessageEvent respuesta = peticion.createReply("Failure_Dejar_Victima", accion);
      sendMessage(respuesta);
    }


  }

}