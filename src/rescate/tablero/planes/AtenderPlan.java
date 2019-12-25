package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class AtenderPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe peticion de atender victima");

    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    AtenderHerido accion = (AtenderHerido) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Fichas atención medica
    int primerosAuxilios = (int) getBeliefbase().getBelief("primerosAuxilios").getFact();

    if (c.getPuntoInteres() != 2) {
      System.out.println("[FALLO] El jugador con id " + idJugador + " no esta en una casilla con victima");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = peticion.createReply("Failure_Atender", accion);
      sendMessage(respuesta);
    }
    else if (jugador.getRol() != 1) {
      System.out.println("[FALLO] El jugador con id " + idJugador + " no tiene el rol necesario para atender (sanitario)");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = peticion.createReply("Failure_Atender", accion);
      sendMessage(respuesta);
    }
    else {
      // Si tiene PA suficientes y hay suficientes fichas de primeros auxilios...
      if (jugador.getPuntosAccion() > 0 && primerosAuxilios > 0) {
        System.out.println("[INFO] El jugador con id " + idJugador + " atiende a la victima en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        // Se actualiza el PDI
        c.setPuntoInteres(3);
         // Se reduce los puntos de acción del jugador 
        jugador.setPuntosAccion(jugador.getPuntosAccion()-1);
        // Se actualiza la vista
        ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
        viewUpdater.updateTablero(t);
        getBeliefbase().getBelief("view").setFact(viewUpdater);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se reduce en uno el número de fichas de atención médica
        getBeliefbase().getBelief("primerosAuxilios").setFact(primerosAuxilios - 1);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = peticion.createReply("Inform_Victima_Atendida", new VictimaAtendida());
        sendMessage(respuesta);
      }
      // En caso contrario...
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA o no quedan fichas de primeros auxilios");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = peticion.createReply("Refuse_Atender", accion);
        sendMessage(respuesta);
      }
    }

  }

}
