package rescate.jugador.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;

public class UnirsePartidaPlan extends Plan {

  public void body() {

    // Registra al jugador
    AgentIdentifier agentId = new AgentIdentifier();
    Jugador jugador = new Jugador();

    System.out.println("[PLAN] El jugador con id " + agentId + " quiere unirse a la partida");

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

    System.out.println("[INFO] Jugador busca tablero...");

    AgentDescription[] result = (AgentDescription[]) ft.getParameterSet("result").getValues();

    if (result.length > 0) {
      AgentIdentifier tablero = result[0].getName();

      // Solicitud unirse
      UnirsePartida accion = new UnirsePartida();

      IMessageEvent msgsend = createMessageEvent("Request_Unirse_Partida");
      msgsend.setContent(accion);
      msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);

      sendMessage(msgsend);

      System.out.println("[INFO] Jugador envía mensaje para unirse a tablero");
    } else {
      System.out.println("[FALLO] Tablero no encontrado");
    }
  }

}
