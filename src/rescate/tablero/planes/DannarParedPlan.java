package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class DannarParedPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de derribar una pared");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    DannarPared accion = (DannarPared) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Si la conexión no es una pared o es una pared ya rota...
		if (c.getConexiones()[accion.getConexion()] != Casilla.Conexion.PARED && c.getConexiones()[accion.getConexion()] != Casilla.Conexion.PARED_SEMIRROTA) {
      System.out.println("[FALLO] No es una pared o es una pared que no puede recibir danno");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Dannar_Pared");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    } 
    // Si la conexión es una pared que puede ser dannada..
    else {
      // No tiene PA suficientes en funcion de ...
      if (jugador.getPuntosAccion() < ((jugador.getRol() == Jugador.Rol.RESCATES) ? 1 : 2))  {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA para abrir una puerta");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Dannar_Pared");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // PA suficientes...
      else {
        System.out.println("[INFO] Se ha dañado la pared en la posicion[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        // Se modifica la pared dannana
        Casilla.Conexion nuevoEstado = Casilla.Conexion.values()[c.getConexiones()[accion.getConexion()].ordinal() + 1];
        c.getConexiones()[accion.getConexion()] = nuevoEstado;
        // Casilla colindante (donde también esta la referencia a la puerta cerrada y hay que abrirla)
        Casilla colindante = null;
        switch (accion.getConexion()) {
          // Arriba
          case 0:
            colindante = t.getMapa()[c.getPosicion()[1] - 1][c.getPosicion()[0]];
            colindante.getConexiones()[2] = nuevoEstado;
            break;
          // Derecha
          case 1:
            colindante = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] + 1];
            colindante.getConexiones()[3] = nuevoEstado;
            break;
          // Abajo
          case 2:
            colindante = t.getMapa()[c.getPosicion()[1] + 1][c.getPosicion()[0]];
            colindante.getConexiones()[0] = nuevoEstado;
            break;
          // Izquierda
          case 3:
            colindante = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] - 1];
            colindante.getConexiones()[1] = nuevoEstado;
            break;
          // ...
          default:
            break;
        }
        // Se actualiza el jugador (consumo de PA) en funcionn de su rol
        jugador.setPuntosAccion(jugador.getPuntosAccion() - ((jugador.getRol() == Jugador.Rol.RESCATES) ? 1 : 2));
        // Se reduce en uno los cubos de daño
        getBeliefbase().getBelief("cubosDanno").setFact((int) getBeliefbase().getBelief("cubosDanno").getFact() - 1);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = createMessageEvent("Inform_Dannar_Pared");
        respuesta.setContent(new ParedDannada());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
		}

  }

}
