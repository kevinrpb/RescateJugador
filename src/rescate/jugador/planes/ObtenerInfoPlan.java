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
public class ObtenerInfoPlan extends Plan {

  public void body() {

    // Obtenemos al jugador y los jugadores en la habitación
    Jugador jugador = (Jugador) getBeliefbase().getBelief("jugador").getFact();
    Jugador[] jugadores = (Jugador[]) getBeliefbase().getBeliefSet("jugadoresHabitacion").getFacts();

    // Ejecutamos la estrategia
    Info nuevaInfo = ObtenerInfoEstrategia.ejecutar(this, jugador, jugadores);

    // Tenemos info
    getBeliefbase().getBelief("info").setFact(nuevaInfo);
    getBeliefbase().getBelief("tieneInfo").setFact(true);

  }

}
