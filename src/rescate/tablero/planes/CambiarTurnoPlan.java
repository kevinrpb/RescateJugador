package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class CambiarTurnoPlan extends Plan {

	@Override
	public void body() {
    System.out.println("[PLAN] El tablero recibe petición de cambiar turno");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Turno
    int turno = (int) getBeliefbase().getBelief("turno").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    CambiarTurno accion = (CambiarTurno) peticion.getContent();

    
    // Se comprueba que sea el turno del jugador
    if (t.getIndexJugador(idJugador) != turno%t.getJugadores().size()) {
      System.out.println("[RECHAZADO] No es el turno del jugador con id " + idJugador);
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Refuse_Cambiar_Turno");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
      return;
    }
    // Se suma el turno y se pone el belief
    turno++;
    getBeliefbase().getBelief("turno").setFact(turno);
    
    // Se encuentra en la lista de jugadores del tablero el jugador al que le toca jugar en el siguiente turno
    int indexNextJugador = turno%t.getJugadores().size();
    Jugador jugador = t.getJugadores().get(indexNextJugador);

    //Se añaden los PA genéricos y se restauran los de clase
    if (jugador.getRol() == Jugador.Rol.ESPUMA_IGNIFUGA) {
      jugador.setPuntosAccion(jugador.getPuntosAccion()+3);
      jugador.setPuntosAccionExtincion(3);
    }else if (jugador.getRol() == Jugador.Rol.GENERALISTA) {
      jugador.setPuntosAccion(jugador.getPuntosAccion()+5);
    }else{
      jugador.setPuntosAccion(jugador.getPuntosAccion()+4);
      if (jugador.getRol() == Jugador.Rol.JEFE) {
        jugador.setPuntosAccionMando(2);
      }else if (jugador.getRol() == Jugador.Rol.RESCATES) {
        jugador.setPuntosAccionMovimiento(3);
      }
    }
    // Se informa al jugador que el turno se ha cambiado correctamente
    IMessageEvent respuesta = createMessageEvent("Inform_Turno_Cambiado");
    respuesta.setContent(new TurnoCambiado());
    respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
    sendMessage(respuesta);

    // Se informa también al jugador al que le toca ahora jugar
    respuesta = createMessageEvent("Inform_Turno_Asignado");
    TurnoAsignado predicado = new TurnoAsignado();
    predicado.setHabitacion(t.getHabitacion(jugador.getHabitacion()));
    respuesta.setContent(predicado);
    respuesta.getParameterSet(SFipa.RECEIVERS).addValue(jugador.getIdAgente());
    sendMessage(respuesta);
	}
}
