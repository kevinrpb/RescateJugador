package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class AtenderPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de atender víctima");

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

    if (c.getPuntoInteres() != Casilla.PuntoInteres.VICTIMA) {
      System.out.println("[FALLO] El jugador con id " + idJugador + " no está en una casilla con víctima");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Atender");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }
    else if (jugador.getRol() != Jugador.Rol.SANITARIO) {
      System.out.println("[FALLO] El jugador con id " + idJugador + " no tiene el rol necesario para atender (sanitario)");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Atender");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }
    else {
      // Si tiene PA suficientes y hay suficientes fichas de primeros auxilios...
      if (jugador.getPuntosAccion() > 0 && primerosAuxilios > 0) {
        System.out.println("[INFO] El jugador con id " + idJugador + " atiende a la víctima en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        // Se actualiza el PDI
        c.setPuntoInteres(Casilla.PuntoInteres.VICTIMA_CURADA);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se reduce en uno el número de fichas de atención médica
        getBeliefbase().getBelief("primerosAuxilios").setFact(primerosAuxilios - 1);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = createMessageEvent("Inform_Victima_Atendida");
        respuesta.setContent(new VictimaAtendida());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // En caso contrario...
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA o no quedan fichas de primeros auxilios");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Atender");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    }

  }

}
