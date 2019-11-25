package rescate.tablero.planes;

import rescate.ontologia.conceptos.Casilla;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class PropagarFuego {

	
	@Override
	public void body() {
	
	
	Casilla[][] mapa = (Casilla[][]) getBeliefbase().getBeliefSet("casillas").getFacts();
	getBeliefbase().getBeliefSet("casillas").removeFact(mapa);
	
	}
}
