package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class ApagarFuegoPlan extends Plan {

  @Override
  public void body() {
    FuegoApagadoPredicado fa = new FuegoApagadoPredicado();
    System.out.println("tablero recibe peticion de apagar fuego...");
		IMessageEvent request = (IMessageEvent) getInitialEvent();
		AgentIdentifier jugador = (AgentIdentifier) request.getParameter("emisor").getValue();
		Casilla casillaSolicitada = (Casilla) request.getParameter("casilla").getValue();
		boolean ok = false;

    if (casillaSolicitada.tieneFuego() == 2){ //tiene fuego
      ok=true;
    }

    IMessageEvent msg = createMessageEvent("Agree_Apagar_Fuego");

		if (!ok) {
			msg = createMessageEvent("Refuse_Apagar_Fuego");
			System.out.println("tablero deniega peticion de apagar fuego: la casilla no tiene fuego");
		} else {

      Tablero tablero = (Tablero) getBeliefbase().getBelief("tablero").getFact();
      Casilla[][] mapa = tablero.getMapa();

      for(int i=0; i< mapa.length; i++){
        for(int j=0; j< mapa[0].length; j++){
          if(mapa[i][j].equals(casillaSolicitada)){
            mapa[i][j].setTieneFuego(1);
          }
        }
      }

      tablero.setMapa(mapa);

      getBeliefbase().getBelief("tablero").setFact(tablero);
      System.out.println("tablero informa que el fuego ha sido apagado (se transforma en humo)");

    }
    msg.setContent(fa);
    msg.getParameterSet(SFipa.RECEIVERS).addValue(jugador);
    sendMessage(msg);
  }

}
