package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.conceptos.Jugador;
import rescate.ontologia.conceptos.Tablero;

class TableroUnirJugadorPlan extends Plan {

  @Override
  public void body() {

    ArrayList<Jugador> jugadores = ((Tablero) getBeliefbase().getBelief("tablero")).getJugadores();
    

  }

}
