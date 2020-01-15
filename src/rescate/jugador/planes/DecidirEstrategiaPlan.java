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
public class DecidirEstrategiaPlan extends Plan {

  public void body() {

    Jugador jugador = (Jugador) getBeliefbase().getBelief("jugador").getFact();

    int estrategia = DecidirEstrategiaEstrategia.ejecutar(this, jugador);

    System.out.println("[Plan] El jugador con id " + jugador.getIdAgente() + " decide la estrategia " + estrategia);

    getBeliefbase().getBelief("estrategia").setFact(estrategia);
  }

}
