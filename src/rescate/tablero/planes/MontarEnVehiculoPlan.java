package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class MontarEnVehiculoPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de montar en un vehículo");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    MontarVehiculo accion = (MontarVehiculo) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // En qué vehículo quiere montar
    boolean ambulancia = c.esAmbulancia();
    boolean camion = c.esCamionBomberos();

    // Montar en el vehiculo
    if (ambulancia || camion) {
      // Si hay un jugador subido a la ambulancia en esa posicion
      for (int i = 0; i < t.getJugadores().size(); i++) {
        if (t.getJugadores().get(i).getPosicion()[0] == jugador.getPosicion()[0] && t.getJugadores().get(i).getPosicion()[1] == jugador.getPosicion()[1]) {
          if (t.getJugadores().get(i).subidoAmbulancia() || t.getJugadores().get(i).subidoCamion()) {
            System.out.println("[RECHAZADO] El jugador con id " + t.getJugadores().get(i).getIdAgente() + " ya está subido " + ((ambulancia) ? "a la ambulancia" : "al camion") + " en esa posición");
            // Se rechaza la petición de acción del jugador
            IMessageEvent respuesta = createMessageEvent("Refuse_Montar_Vehiculo");
            respuesta.setContent(accion);
            respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
            sendMessage(respuesta);
            return;
          }
        }
      } 
      System.out.println("[INFO] El jugador con id " + idJugador + " se ha subido " + ((ambulancia) ? "a la ambulancia" : "al camion")); 
      // Si no hay nadie subido en esa posicion
      if (ambulancia) {
        jugador.setSubidoAmbulancia(true);
      } else {
        jugador.setSubidoCamion(true);
      }
      // Se actualiza en la base de creencias el hecho tablero
      getBeliefbase().getBelief("tablero").setFact(t);
      // Se informa al jugador de que la acción ha sido llevada a cabo
      IMessageEvent respuesta = createMessageEvent("Inform_Montado_Vehiculo");
      respuesta.setContent(new MontadoVehiculo());
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }
    // No hay ningun vehículo en el que montarse en la casilla del jugador
    else {
      System.out.println("[FALLO] No hay ningun vehiculo en el que montarse en la casilla del jugador con id " + idJugador);
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Montar_Vehiculo");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }
  }

}
