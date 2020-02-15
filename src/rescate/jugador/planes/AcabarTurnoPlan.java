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
public class AcabarTurnoPlan extends Plan {

  public void body() {

    AgentIdentifier idTablero = (AgentIdentifier) getBeliefbase().getBelief("tablero").getFact();
    Jugador jugador = (Jugador) getBeliefbase().getBelief("jugador").getFact();

    System.out.println("[Plan] El jugador con id " + jugador.getIdAgente() + " decide acabar su turno");

    // Mandamos Petición
    CambiarTurno accion = new CambiarTurno();

    IMessageEvent peticion = createMessageEvent(Mensajes.Turno.RequestCambiar);
    peticion.setContent(accion);
    peticion.getParameterSet(SFipa.RECEIVERS).addValue(idTablero);

    IMessageEvent respuesta = sendMessageAndWait(peticion);

    // Comprobamos la respuesta
    assert respuesta.getName().contains(Mensajes.Turno.InformCambiado);
  }

  @Override
  public void passed() {
    super.passed();

    // Actualizamos la base
    getBeliefbase().getBelief("esTurno").setFact(false);
    getBeliefbase().getBelief("tieneInfo").setFact(false);
    getBeliefbase().getBelief("estrategia").setFact(Estrategias.Ninguna);
  }

}
