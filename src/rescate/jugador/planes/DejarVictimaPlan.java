package rescate.jugador.planes;

import java.util.ArrayList;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

import rescate.jugador.estrategias.*;
import rescate.jugador.util.*;

@SuppressWarnings("serial")
public class DejarVictimaPlan extends Plan {

  public void body() {

    AgentIdentifier idTablero = (AgentIdentifier) getBeliefbase().getBelief("tablero").getFact();

    // Cargamos la info del jugador
    Jugador jugador = (Jugador) getBeliefbase().getBelief("jugador").getFact();

    // Hacemos la petición
    CogerVictima accion = new CogerVictima();

    IMessageEvent peticion = createMessageEvent(Mensajes.Victima.RequestDejar);
    peticion.getParameterSet(SFipa.RECEIVERS).addValue(idTablero);
    peticion.setContent(accion);

    // Obtenemos respuesta
    IMessageEvent respuesta = sendMessageAndWait(peticion);

    if (respuesta.getType().equals(Mensajes.Victima.InformDejada)) {
      jugador.setLlevandoVictima(0);

      getBeliefbase().getBelief("jugador").setFact(jugador);
    }
  }

  @Override
  public void passed() {
    super.passed();

    // Quitamos la strat
    getBeliefbase().getBelief("estrategia").setFact(Estrategias.Ninguna);
  }


}
