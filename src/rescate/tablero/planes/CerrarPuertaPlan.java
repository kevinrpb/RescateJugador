package rescate.tablero.planes;

import java.util.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class CerrarPuertaPlan extends Plan {

	@Override
	public void body() {
		System.out.println("tablero recibe peticion de cerrar puerta...");
		IMessageEvent request = (IMessageEvent) getInitialEvent();
		AgentIdentifier jugador = (AgentIdentifier) request.getParameter("emisor").getValue();
		Casilla puertaSolicitada = (Casilla) request.getParameter("casilla").getValue();
		boolean ok = false;
		int posicionConex = 0;
		for (int i = 0; i < 4 && ok != true; i++) {
			if (puertaSolicitada.getConexiones()[i] == Casilla.Conexion.PUERTA_ABIERTA) {
				ok = true;
				posicionConex = i;
			}
		}

		if (!ok) {
			IMessageEvent msg = createMessageEvent("refused Cerrar");
			System.out.println("tablero deniega peticion de cerrar puerta: no hay conexiones de puertas abiertas");
		} else {
			IMessageEvent msg = createMessageEvent("agreed Cerrar");
			Casilla.Conexion[] conexiones = puertaSolicitada.getConexiones();
			conexiones[posicionConex] = Casilla.Conexion.PUERTA_CERRADA;
			puertaSolicitada.setConexiones(conexiones);
			Casilla[][] mapa = (Casilla[][]) getBeliefbase().getBeliefSet("casillas").getFacts();
			getBeliefbase().getBeliefSet("casillas").removeFact(mapa);
			mapa[puertaSolicitada.getPosicion()[1]][puertaSolicitada.getPosicion()[0]] = puertaSolicitada;
			getBeliefbase().getBeliefSet("casillas").addFact(mapa);
			System.out.println("tablero informa que la puerta ha sido cerrada correctamente");
		}
	}

}
