package rescate.tablero.planes;

import rescate.ontologia.conceptos.Casilla;

import java.util.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class PropagarFuegoPlan extends Plan {

  private int propagaciones = 1;
  private Casilla[][] mapa;

  @Override
  public void body() {

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();
    mapa = t.getMapa();

    // Propagar el fuego mientras se deba (1 vez siempre, y es posible que otras si ocurren fogonazos)
    do {
      propagaciones--;
      propagar();
    } while (propagaciones > 0);

    // Guardar tablero
    getBeliefbase().getBelief("tablero").setFact(t);

  } 

  private void propagar() {

  }

}
