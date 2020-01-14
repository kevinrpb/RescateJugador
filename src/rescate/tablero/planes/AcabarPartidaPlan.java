package rescate.tablero.planes;

import java.util.ArrayList;

import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.IMessageEvent;
import jadex.runtime.IPlan;
import jadex.runtime.Plan;

import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class AcabarPartidaPlan extends Plan {


	@Override
	public void body() {

    int cubosDanno = (int) getBeliefbase().getBelief("cubosDanno").getFact();
    int victimas = (int) getBeliefbase().getBelief("victimas").getFact();
    int salvados = (int) getBeliefbase().getBelief("salvados").getFact();

    System.out.println("[PLAN] El tablero acaba la partida porque" + ((cubosDanno < 1) ? " se ha derruido la casa" : ((victimas > 3) ? " hay mas de 4 victimas" : " han salvado 7 posibles victimas")));

    // Tablero y jugadores
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();
    ArrayList<Jugador> jugadores = t.getJugadores();

    // Se informa a todos los jugadores de que la partida ha acabado y del resultado
    IMessageEvent respuesta = createMessageEvent("Inform_Partida_Acabada");
    ResultadoPartida predicado = new ResultadoPartida();
    predicado.setResultado((salvados > 6) ? 0 : 1);
    respuesta.setContent(predicado);

    // Como destinatarios, todos los jugadores
    for (Jugador j: jugadores) {
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(j.getIdAgente());
    }

    // Se envia la respuesta
    sendMessage(respuesta);
      
    // Se deja de jugar
    getBeliefbase().getBelief("jugando").setFact(false);

  }

}