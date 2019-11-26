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
    Jugador jugador = null;
    int indice = -1;
    for (int i = 0; i < t.getJugadores().size(); i++) {
      if (t.getJugadores().get(i).getIdAgente() == idJugador) {
        jugador = t.getJugadores().get(i);
        indice = i;
        break;
      }
    }

    // Fichas atención medica
    int primerosAuxilios = (int) getBeliefbase().getBelief("primerosAuxilios").getFact();

    // Si el jugador está llevando una víctima y tiene el rol SANITARIO...
    if (jugador.llevandoVictima() == Jugador.LlevandoVictima.SI && jugador.getRol() == Jugador.Rol.SANITARIO) {
      // Si tiene PA suficientes y hay suficientes fichas de primeros auxilios...
      if (jugador.getPuntosAccion() > 0 && primerosAuxilios > 0) {
        System.out.println("[INFO] El jugador con id " + idJugador + " atiende a la víctima que actualmente lleva");
        // Se actualiza el estado de llevando víctima del jugador
        jugador.setLlevandoVictima(Jugador.LlevandoVictima.CURADA);
        t.setJugador(indice, jugador);
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
        System.out.println("[ERROR] El jugador con id " + idJugador + " no tiene suficientes PA o no quedan fichas de primeros auxilios");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Atender");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    }
    // En caso contrario no puede atender a la víctima
    else {
      System.out.println("[ERROR] El jugador con id " + idJugador + " no tiene el rol necesario o no está llevando a una víctima");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Atender");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }

  }

}
