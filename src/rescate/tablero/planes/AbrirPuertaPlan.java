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

    System.out.println("[PLAN] El tablero recibe petición de abrir puerta");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    AbrirPuerta accion = (AbrirPuerta) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = null;
    int indice = -1;
    for (int i = 0; i < t.getJugadores().size(); i++) {
      if (t.getJugadores().get(i).getIdAgente() == idJugador) {
        jugador = t.getJugadores().get(i);
        indice = i;
        break;
      }
    }

    // Si la conexión no es una puerta cerrada...
		if (accion.getCasilla().getConexiones()[accion.getConexion()] != Casilla.Conexion.PUERTA_CERRADA) {
      System.out.println("[ERROR] No hay una puerta cerrada en la conexión de la casilla indicada en la petición");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Abrir_Puerta");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    } 
    // Si la conexión es una puerta cerrada...
    else {
      if (jugador.getPuntosAccion() < 1) {
        System.out.println("[ERROR] El jugador con id " + idJugador + " no tiene suficientes PA para abrir una puerta");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Abrir_Puerta");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      else {
        System.out.println("[INFO] Se ha abierto una puerta en la casilla[" + accion.getCasilla().getPosicion()[0] + ", " + accion.getCasilla().getPosicion()[1] + "]");
        // Se modifica la conexion a puerta abierta
        accion.getCasilla().setConexiones(accion.getConexion(), Casilla.Conexion.PUERTA_ABIERTA);
        // Casilla colindante (donde también esta la referencia a la puerta cerrada y hay que abrirla)
        Casilla colindante = null;
        switch (accion.getConexion()) {
          // Arriba
          case 0:
            colindante = t.getMapa()[accion.getCasilla().getPosicion()[1] - 1][accion.getCasilla().getPosicion()[0]];
            colindante.setConexiones(2, Casilla.Conexion.PUERTA_ABIERTA);
            break;
          // Derecha
          case 1:
            colindante = t.getMapa()[accion.getCasilla().getPosicion()[1]][accion.getCasilla().getPosicion()[0] + 1];
            colindante.setConexiones(3, Casilla.Conexion.PUERTA_ABIERTA);
            break;
          // Abajo
          case 2:
            colindante = t.getMapa()[accion.getCasilla().getPosicion()[1] + 1][accion.getCasilla().getPosicion()[0]];
            colindante.setConexiones(0, Casilla.Conexion.PUERTA_ABIERTA);
            break;
          // Izquierda
          case 3:
            colindante = t.getMapa()[accion.getCasilla().getPosicion()[1]][accion.getCasilla().getPosicion()[0] - 1];
            colindante.setConexiones(1, Casilla.Conexion.PUERTA_ABIERTA);
            break;
          // ...
          default:
            break;
        }
        // Se actualiza el jugador (consumo de PA)
        jugador.setPuntosAccion(jugador.getPuntosAccion() - 1);
        t.setJugador(indice, jugador);
        // Se actualiza la casilla sobre la que se ha realizado la apertura de la puerta
        t.setCasilla(accion.getCasilla().getPosicion()[0], accion.getCasilla().getPosicion()[1], accion.getCasilla());
        // Se actualiza la casilla colindante
        t.setCasilla(colindante.getPosicion()[0], colindante.getPosicion()[1], colindante);
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
