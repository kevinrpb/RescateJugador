package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class ApagarFuegoPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de apagar fuego o humo");

    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    ApagarFuego accion = (ApagarFuego) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Si la casilla no tiene fuego...
    if (accion.getCasilla().tieneFuego() == Casilla.Fuego.FUEGO || accion.getCasilla().tieneFuego() == Casilla.Fuego.HUMO) {
      System.out.println("[FALLO] La casilla no tiene un fuego activo");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Apagar_Fuego");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    } 
    // Si el bombero no está cerca...
    else if (!c.esColindante(accion.getCasilla()) && !c.mismaPosicion(accion.getCasilla())) {
      System.out.println("[FALLO] El jugador no está cerca del fuego");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Apagar_Fuego");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    } 
    // Si las condiciones permiten extinguir el fuego...
    else {
      // No hay PA suficientes para realizar la acción (en función del rol)
      if (jugador.getPuntosAccion() + jugador.getPuntosAccionExtincion() < ((jugador.getRol() == Jugador.Rol.SANITARIO) ? 2 : 1)) {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA para apagar un fuego");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Apagar_Fuego");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
      // PA suficientes
      else {
        System.out.println("[INFO] Se ha apagado un fuego o humo en la casilla[" + accion.getCasilla().getPosicion()[0] + ", " + accion.getCasilla().getPosicion()[1] + "]");
        // Se apaga el fuego (pasa a humo)
        accion.getCasilla().setTieneFuego(Casilla.Fuego.values()[c.tieneFuego().ordinal()-1]);
        // Se actualiza el jugador (consumo de PA)
        if (jugador.getPuntosAccionExtincion() > 0) {
          jugador.setPuntosAccionExtincion(jugador.getPuntosAccionExtincion() - 1);
        }
        else if (jugador.getPuntosAccion() > ((jugador.getRol() == Jugador.Rol.SANITARIO) ? 1 : 0)) {
          jugador.setPuntosAccion(jugador.getPuntosAccion() - ((jugador.getRol() == Jugador.Rol.SANITARIO) ? 2 : 1));
        }
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
