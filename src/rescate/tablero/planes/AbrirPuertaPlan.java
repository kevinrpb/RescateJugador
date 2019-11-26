package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class AbrirPuertaPlan extends Plan {

	@Override
	public void body() {

    System.out.println("[PLAN] El tablero recibe petición de abrir puerta");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();
    // Parámetros de la peticion
		AgentIdentifier jugador = (AgentIdentifier) peticion.getParameter("agente").getValue();
    Casilla c = (Casilla) peticion.getParameter("casilla").getValue();
    int pos = (int) peticion.getParameter("conexion").getValue();

		if (c.getConexiones()[pos] != Casilla.Conexion.PUERTA_CERRADA) {
			IMessageEvent respuesta = createMessageEvent("ABRIR_PUERTA_RECHAZADO");
			System.out.println("[ERROR] No hay una puerta cerrada en la conexión de la casilla indicada en la petición");
    } 
    else {
      IMessageEvent msg = createMessageEvent("ABRIR_PUERTA_COMPLETADO");
      c.setConexiones(pos, Casilla.Conexion.PUERTA_ABIERTA);
      
			
			Casilla[][] mapa = (Casilla[][]) getBeliefbase().getBeliefSet("casillas").getFacts();
			getBeliefbase().getBeliefSet("casillas").removeFact(mapa);
			mapa[puertaSolicitada.getPosicion()[1]][puertaSolicitada.getPosicion()[0]] = puertaSolicitada;
			getBeliefbase().getBeliefSet("casillas").addFact(mapa);
			System.out.println("tablero informa que la puerta ha sido abierta correctamente");

		}
	}

}
