package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class FinTurnoPlan extends Plan {

	@Override
	public void body() {

    System.out.println("[PLAN] El tablero recibe peticion de finalizar un turno");
    
    // Peticion
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Turno
    int turno = (int) getBeliefbase().getBelief("turno").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    CambiarTurno accion = (CambiarTurno) peticion.getContent();
    
    // Se comprueba que sea el turno del jugador
    if (t.getIndiceJugador(idJugador) != (turno % t.getJugadores().size())) {
      System.out.println("[RECHAZADO] No es el turno del jugador con id " + idJugador);
      // Se rechaza la peticion de accion del jugador
      IMessageEvent respuesta = peticion.createReply("Refuse_Cambiar_Turno", accion);
      sendMessage(respuesta);
      return;
    }
    
    System.out.println("[INFO] Siguiente turno");
    
    // Se informa al jugador que el turno se ha cambiado correctamente
    IMessageEvent respuesta = peticion.createReply("Inform_Turno_Cambiado", new TurnoCambiado());
    sendMessage(respuesta);

    turno++;

    // Se actualiza la vista
    ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
    viewUpdater.updateTablero(t);
    viewUpdater.cambiarTurno(turno);
    getBeliefbase().getBelief("view").setFact(viewUpdater);
    
    // Se actualizan los hechos realicionados con el turno
    getBeliefbase().getBelief("turno").setFact(turno);
    //getBeliefbase().getBelief("siguienteTurno").setFact(true);
    getBeliefbase().getBelief("propagarFuego").setFact(true);
  }
  
}
