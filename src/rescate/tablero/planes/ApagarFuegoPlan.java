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

    System.out.println("[PLAN] El tablero recibe petición de apagar fuego");

    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    ApagarFuego accion = (ApagarFuego) peticion.getContent();

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

    // Si la casilla no tiene fuego...
		if (accion.getCasilla().tieneFuego() == Casilla.Fuego.FUEGO) {
      System.out.println("[ERROR] La casilla no tiene un fuego activo");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Apagar_Fuego");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    } 
    // Si la casilla tiene fuego...
    else {
      // No hay PA suficientes para realizar la acción (en función del rol)
      if (jugador.getPuntosAccion() + jugador.getPuntosAccionExtincion() < ((jugador.getRol() == Jugador.Rol.SANITARIO) ? 2 : 1)) {
        System.out.println("[ERROR] El jugador con id " + idJugador + " no tiene suficientes PA para apagar un fuego");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Apagar_Fuego");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // PA suficientes
      else {
        System.out.println("[INFO] Se ha apagado un fuego en la casilla[" + accion.getCasilla().getPosicion()[0] + ", " + accion.getCasilla().getPosicion()[1] + "]");
        // Se apaga el fuego (pasa a humo)
        accion.getCasilla().setTieneFuego(Casilla.Fuego.HUMO);
        // Se actualiza el jugador (consumo de PA)
        if (jugador.getPuntosAccionExtincion() > 0) {
          jugador.setPuntosAccionExtincion(jugador.getPuntosAccionExtincion() - 1);
        }
        else if (jugador.getPuntosAccion() > ((jugador.getRol() == Jugador.Rol.SANITARIO) ? 1 : 0)) {
          jugador.setPuntosAccion(jugador.getPuntosAccion() - ((jugador.getRol() == Jugador.Rol.SANITARIO) ? 2 : 1));
        }
        t.setJugador(indice, jugador);
        // Se actualiza la casilla sobre la que se ha realizado la apertura de la puerta
        t.setCasilla(accion.getCasilla().getPosicion()[0], accion.getCasilla().getPosicion()[1], accion.getCasilla());
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = createMessageEvent("Inform_Fuego_Apagado");
        respuesta.setContent(new FuegoApagado());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
		}
  }
  
}
