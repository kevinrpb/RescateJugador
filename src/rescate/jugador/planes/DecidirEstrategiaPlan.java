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
    Info info = (Info) getBeliefbase().getBelief("info").getFact();

    int[] datosEstrategia = DecidirEstrategiaEstrategia.ejecutar(this, jugador, info);

    int estrategia = datosEstrategia[0];
    int casillaX = datosEstrategia[1];
    int casillaY = datosEstrategia[2];

    System.out.println("[Plan] El jugador con id " + jugador.getIdAgente() + " decide la estrategia " + estrategia);

    getBeliefbase().getBelief("estrategia").setFact(estrategia);
    getBeliefbase().getBelief("casillaEstrategia").setFact(new int[] { casillaX, casillaY });
  }

}
