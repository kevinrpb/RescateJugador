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
import rescate.jugador.util.*;

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

      // Para el primer turno, no pedimos info
      getBeliefbase().getBelief("tieneInfo").setFact(true);
    }

    getBeliefbase().getBelief("jugador").setFact(jugador);

    // Guardamos los jugadores de la habitación
    ArrayList<Jugador> jugadoresList = turno.getJugadores();
    Jugador[] jugadores = jugadoresList.toArray(new Jugador[jugadoresList.size()]);

    getBeliefbase().getBeliefSet("jugadoresHabitacion").removeFacts();
    getBeliefbase().getBeliefSet("jugadoresHabitacion").addFacts(jugadores);

    // Actualizamos la info con las habitaciones de este turno
    Info info = (Info) getBeliefbase().getBelief("info").getFact();

    ArrayList<Casilla> casillas = turno.getHabitacion();
    Info nuevaInfo = ActualizarHabitacionEstrategia.ejecutar(this, info, casillas, true);

    getBeliefbase().getBelief("info").setFact(nuevaInfo);

    // Guardamos los PA para este turno
    getBeliefbase().getBelief("PA").setFact(jugador.getPuntosAccion());
    getBeliefbase().getBelief("PAMando").setFact(jugador.getPuntosAccionMando());
    getBeliefbase().getBelief("PAExtincion").setFact(jugador.getPuntosAccionExtincion());
    getBeliefbase().getBelief("PAMovimiento").setFact(jugador.getPuntosAccionMovimiento());

    // El turno ha empezado
    getBeliefbase().getBelief("esTurno").setFact(true);

  }

}
