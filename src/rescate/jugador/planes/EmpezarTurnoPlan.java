package rescate.jugador.planes;

import java.util.ArrayList;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;
import rescate.jugador.estrategias.*;

@SuppressWarnings("serial")
public class EmpezarTurnoPlan extends Plan {

  public void body() {

    // Mensaje y predicado, tablero
    IMessageEvent mensaje = (IMessageEvent) getInitialEvent();
    TurnoAsignado turno = (TurnoAsignado) mensaje.getContent();

    AgentIdentifier idTablero = (AgentIdentifier) mensaje.getParameter("sender").getValue();

    // Jugador (según lo tiene el tablero)
    Jugador jugador = turno.getJugador();

    System.out.println("[PLAN] El jugador con id " + jugador.getIdAgente() + " empieza su turno");

    // Si no tiene rol asignado, hacerlo y actualizar el jugador
    if (jugador.getRol() == 0) {
      RolElegido rol = ElegirRolEstrategia.ejecutar(this, idTablero);

      jugador = rol.getJugador();
    }

    getBeliefbase().getBelief("jugador").setFact(jugador);

    // Preguntamos al resto de jugadores en esta habitación
    ArrayList<Jugador> jugadores = turno.getJugadores();

    // ObtenerInfoEstrategia.ejecutar(this, jugadores);

  }

}
