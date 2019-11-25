package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.*;
import rescate.ontologia.conceptos.Casilla;
import rescate.ontologia.conceptos.Casilla.Conexion;

class AbrirPuertaPlan extends Plan {

	@Override
	public void body() {
		System.out.println("tablero recibe peticion de abrir puerta...");
		IMessageEvent request = (IMessageEvent) getInitialEvent();
		AgentIdentifier jugador = (AgentIdentifier) request.getParameter("emisor").getValue();
		Casilla puertaSolicitada = (Casilla) request.getParameter("casilla").getValue();
		boolean ok = false;
		int posicionConex = 0;
		for (int i = 0; i < 4 && ok != true; i++) {
			if (puertaSolicitada.getConexiones()[i] == Conexion.PUERTA_CERRADA) {
				ok = true;
				posicionConex = i;
			}
		}

		if (!ok) {
			IMessageEvent msg = createMessageEvent("refused Abrir");
			System.out.println("tablero deniega peticion de abrir puerta: no hay conexiones de puertas cerradas");
		} else {
			IMessageEvent msg = createMessageEvent("agreed Abrir");
			Conexion[] conexiones = puertaSolicitada.getConexiones();
			conexiones[posicionConex] = Conexion.PUERTA_ABIERTA;
			puertaSolicitada.setConexiones(conexiones);
			Casilla[][] mapa = (Casilla[][]) getBeliefbase().getBeliefSet("casillas").getFacts();
			getBeliefbase().getBeliefSet("casillas").removeFact(mapa);
			mapa[puertaSolicitada.getPosicion()[1]][puertaSolicitada.getPosicion()[0]] = puertaSolicitada;
			getBeliefbase().getBeliefSet("casillas").addFact(mapa);
			System.out.println("tablero informa que la puerta ha sido abierta correctamente");

		}
	}

}
