package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class AbrirPuertaPlan extends Plan {

	@Override
	public void body() {

    System.out.println("[PLAN] El tablero recibe petición de abrir una puerta");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    AbrirPuerta accion = (AbrirPuerta) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Si la conexión no es una puerta cerrada...
		if (c.getConexiones()[accion.getConexion()] != Casilla.Conexion.PUERTA_CERRADA) {
      System.out.println("[FALLO] No hay una puerta cerrada en la conexión indicada de la casilla del jugador");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Abrir_Puerta");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    } 
    // Si la conexión es una puerta cerrada...
    else {
      // No tiene PA suficientes...
      if (jugador.getPuntosAccion() < 1) {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA para abrir una puerta");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Abrir_Puerta");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // PA suficientes...
      else {
        System.out.println("[INFO] Se ha abierto una puerta en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        // Se modifica la conexion a puerta abierta
        c.getConexiones()[accion.getConexion()] = Casilla.Conexion.PUERTA_ABIERTA;
        // Casilla colindante (donde también esta la referencia a la puerta cerrada y hay que abrirla)
        Casilla colindante = null;
        switch (accion.getConexion()) {
          // Arriba
          case 0:
            colindante = t.getMapa()[c.getPosicion()[1] - 1][c.getPosicion()[0]];
            colindante.getConexiones()[2] = Casilla.Conexion.PUERTA_ABIERTA;
            break;
          // Derecha
          case 1:
            colindante = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] + 1];
            colindante.getConexiones()[3] = Casilla.Conexion.PUERTA_ABIERTA;
            break;
          // Abajo
          case 2:
            colindante = t.getMapa()[c.getPosicion()[1] + 1][c.getPosicion()[0]];
            colindante.getConexiones()[0] = Casilla.Conexion.PUERTA_ABIERTA;
            break;
          // Izquierda
          case 3:
            colindante = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] - 1];
            colindante.getConexiones()[1] = Casilla.Conexion.PUERTA_ABIERTA;
            break;
          // ...
          default:
            break;
        }
        // Se actualiza el jugador (consumo de PA)
        jugador.setPuntosAccion(jugador.getPuntosAccion() - 1);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = createMessageEvent("Inform_Puerta_Abierta");
        respuesta.setContent(new PuertaAbierta());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
		}
	}

}
