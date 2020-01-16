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
public class CogerVictimaPlan extends Plan {

  public void body() {

    AgentIdentifier idTablero = (AgentIdentifier) getBeliefbase().getBelief("tablero").getFact();

    // Cargamos la info del jugador
    Jugador jugador = (Jugador) getBeliefbase().getBelief("jugador").getFact();
    int X = jugador.getPosicion()[0];
    int Y = jugador.getPosicion()[1];

    int PA = (int) getBeliefbase().getBelief("PA").getFact();

    // Hacemos la petición
    CogerVictima accion = new CogerVictima();

    IMessageEvent peticion = createMessageEvent(Mensajes.Victima.RequestCoger);
    peticion.getParameterSet(SFipa.RECEIVERS).addValue(idTablero);
    peticion.setContent(accion);

    // Obtenemos respuesta
    IMessageEvent respuesta = sendMessageAndWait(peticion);

    if (respuesta.getType().equals(Mensajes.Victima.InformCogida)) {
      // Actualizamos habitación
      Info info = (Info) getBeliefbase().getBelief("info").getFact();

      // Actualizamos jugador
      Casilla[][] mapa = info.getHistorial(info.getTurno());
      Casilla casilla = mapa[Y][X];

      PA = PA - 2;

      jugador.setPuntosAccion(PA);
      jugador.setLlevandoVictima(casilla.getPuntoInteres());

      getBeliefbase().getBelief("PA").setFact(PA);

      getBeliefbase().getBelief("jugador").setFact(jugador);
    } else {
      getBeliefbase().getBelief("PA").setFact(0);

      jugador.setPuntosAccion(0);

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
