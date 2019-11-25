package rescate.jugador.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;

import rescate.util.*;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;

public class UnirsePartidaPlan extends Plan {

  public void body() {

    // Registra al jugador
    AgentIdentifier agentId = new AgentIdentifier();
    Jugador jugador = new Jugador();

    jugador.setIdAgente(agentId);

    ServiceDescription sd = new ServiceDescription();
    sd.setName("tablero");
    sd.setType("agente");

    AgentDescription dfaDesc = new AgentDescription();
    dfaDesc.addService(sd);

    SearchConstraints sc = new SearchConstraints();
    sc.setMaxResults(-1);

    // Busca el tablero
    IGoal ft = createGoal("df_search");
    ft.getParameter("description").setValue(dfaDesc);
    ft.getParameter("constraints").setValue(sc);
    dispatchSubgoalAndWait(ft);

    Log.debug("Jugador busca tablero...");

    AgentDescription[] result = (AgentDescription[]) ft.getParameterSet("result").getValues();

    if (result.length > 0) {
      Log.debug("Jugador pide unirse a la partida");

      AgentIdentifier tablero = result[0].getName();

      // Solicitud unirse
      UnirsePartidaAccion accion = new UnirsePartidaAccion();
      accion.setJugador(jugador);

      IMessageEvent msgsend = createMessageEvent("RequestUnirsePartidaMsg");
      msgsend.setContent(accion);
      msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);

      sendMessage(msgsend);

      Log.debug("Jugador envía mensaje para unirse a tablero");
    } else {
      Log.warning("Tablero no encontrado");
    }
  }

}
