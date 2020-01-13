package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.RolElegido;
import rescate.ontologia.predicados.RolesDisponibles;

public class AsignarRolPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe peticion de eleccion de rol");

    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    ElegirRol accion = (ElegirRol) peticion.getContent();

    // Se identifican los roles disponibles y...
    ArrayList<Integer> roles = new ArrayList<Integer>();
    Collections.addAll(roles, 1, 2, 3, 4, 5, 6, 7, 8);
    // ...se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = null;
    for (int i = 0; i < t.getJugadores().size(); i++) {
      roles.remove((Integer) t.getJugadores().get(i).getRol());
      if (t.getJugadores().get(i).getIdAgente().equals(idJugador)) {
        jugador = t.getJugadores().get(i);
      }
    }

    // PA suficientes (no necesario si no tiene ningun rol previamente)
    if (jugador.getRol() == 0 || (jugador.getRol() != 0 && jugador.getPuntosAccion() > 1)) {
      // Si dentro de los roles disponibles está el que quiere el jugador...
      if (roles.contains((Integer) accion.getRol())) {
        System.out.println("[INFO] El jugador con id " + idJugador + " cambia al rol " + accion.getRol());
        // Se reducen los PA si es necesario
        if (jugador.getRol() != 0) {
          jugador.setPuntosAccion(jugador.getPuntosAccion() - 2);
        }else{          
          // Se actualizan los PA del jugador
          jugador.setPuntosAccion(4);
          jugador.setPuntosAccionMando(0);
          jugador.setPuntosAccionExtincion(0);
          jugador.setPuntosAccionMovimiento(0);
  
          switch (accion.getRol()) {
            case 2: // Jefe
              jugador.setPuntosAccionMando(2);
              break;
            case 4: // Espuma
              jugador.setPuntosAccion(3);
              jugador.setPuntosAccionExtincion(3);
              break;
            case 6: // Generalista
              jugador.setPuntosAccion(5);
              break;
            case 7: // Rescates
              jugador.setPuntosAccionMovimiento(3);
              break;
          }
        }
        // Se actualiza el rol del jugador
        jugador.setRol(accion.getRol());
        // Se actualiza la vista
        ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
        viewUpdater.updateTablero(t);
        getBeliefbase().getBelief("view").setFact(viewUpdater);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);

        // Se confirma al jugador la asignación del rol elegido
        RolElegido predicado = new RolElegido();
        predicado.setJugador(jugador);

        IMessageEvent respuesta = peticion.createReply("Inform_Rol_Elegido", predicado);
        sendMessage(respuesta);
      }
      // Si no...
      else {
        System.out.println("[RECHAZADO] El rol " + accion.getRol() + " solicitado por el jugador con id " + idJugador + " no esta disponible");
        // Se contesta al jugador rechazando la elección de rol y con la lista de roles disponibles
        RolesDisponibles predicado = new RolesDisponibles();
        predicado.setRolesDisponibles(roles);
        IMessageEvent respuesta = peticion.createReply("Refuse_Elegir_Rol", predicado);
        sendMessage(respuesta);
      }
    }
    // No PA suficientes
    else {
      System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene PA suficientes");
      // Se contesta al jugador rechazando la elección de rol y con la lista de roles disponibles
      IMessageEvent respuesta = peticion.createReply("Refuse_Elegir_Rol", accion);
      sendMessage(respuesta);
    }

  }

}
