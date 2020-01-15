package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class DarOrdenPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe peticion de mando");
    
    // Peticion
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    DarOrden accion = (DarOrden) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la peticion
    Jugador jugadorJefe = t.getJugador(idJugador);
    // Se encuentra el jugador sobre el que se hace el mando
    Jugador jugadorEsclavo = t.getJugador(accion.getIdJugador());
    
    // Casilla del jugador esclavo
    Casilla c = t.getMapa()[jugadorEsclavo.getPosicion()[1]][jugadorEsclavo.getPosicion()[0]];
    int conexion = c.getConexiones()[accion.getConexion()];

    // El jugador es jefe
    if (jugadorJefe.getRol() == 2) {
      // Comprobamos si los jugadores estan en la misma habitacion y si la accion sobre la conexion recibida es posible
      if (jugadorJefe.getHabitacion() == jugadorEsclavo.getHabitacion() &&
          (accion.getAccion() == 1 && conexion == 2 || 
          accion.getAccion() == 2 && conexion == 1 || 
          accion.getAccion() == 0 && !DesplazarPlan.hayObstaculo(conexion))) {

          boolean accionRealizada = false;
          int PA = 0;

          // Casilla + conexion --> colindante
          Casilla colindante = null;
          switch(accion.getConexion()) {
            // Arriba
            case 0:
              colindante = t.getMapa()[c.getPosicion()[1] - 1][c.getPosicion()[0]];
              break;
            // Derecha
            case 1:
              colindante = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] + 1];
              break;
            // Abajo
            case 2:
              colindante = t.getMapa()[c.getPosicion()[1] + 1][c.getPosicion()[0]];
              break;
            // Izquierda
            case 3:
              colindante = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] - 1];
              break;
          }

          // La accion es sobre una puerta (abrir o cerrar)
          if ((accion.getAccion() == 1 || accion.getAccion() == 2) && jugadorJefe.getPuntosAccion() + jugadorJefe.getPuntosAccionMando() > 1){
            System.out.println("[INFO] El jugador con id " + accion.getIdJugador() + " ha abierto una puerta en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "] debido a la orden del jugador "+ idJugador);
            // Abierta o cerrada
            int nuevoEstado = ((accion.getAccion() == 1) ? 1 : 2);
            c.getConexiones()[accion.getConexion()] = nuevoEstado;
            // Casilla colindante (donde también esta la referencia a la puerta cerrada y hay que abrirla)
            switch (accion.getConexion()) {
              // Arriba
              case 0:
                colindante.getConexiones()[2] = nuevoEstado;
                break;
              // Derecha
              case 1:
                colindante.getConexiones()[3] = nuevoEstado;
                break;
              // Abajo
              case 2:
                colindante.getConexiones()[0] = nuevoEstado;
                break;
              // Izquierda
              case 3:
                colindante.getConexiones()[1] = nuevoEstado;
                break;
              // ...
              default:
                break;
            }
            // Accion realizada y PA consumidos
            accionRealizada = true;
            PA = 1;
          }
          // La accion es moverse
          else if (accion.getAccion() == 0 && jugadorJefe.getPuntosAccion() + ((jugadorEsclavo.getRol() != 4) ? jugadorJefe.getPuntosAccionMando() : ((jugadorJefe.getPuntosAccionMando() > 0) ? 1 : 0)) > DesplazarPlan.puntosAccionNecesarios(colindante, jugadorEsclavo)){
            // Se baja de los vehiculos si esta subido
            jugadorEsclavo.setSubidoAmbulancia(false);
            jugadorEsclavo.setSubidoCamion(false);
            // Se actualiza la posicion
            jugadorEsclavo.setPosicion(colindante.getPosicion());
            jugadorEsclavo.setHabitacion(colindante.getHabitacion());
            // Se identifica PDI si lo hay
            int PDITablero = (int) getBeliefbase().getBelief("PDITablero").getFact();
            int PDIVictima = (int) getBeliefbase().getBelief("PDIVictima").getFact();
            int PDIFalsaAlarma = (int) getBeliefbase().getBelief("PDIFalsaAlarma").getFact();
            if (colindante.getPuntoInteres() == 1) {
              // Si no queda de un tipo, se coloca del otro...
              if (PDIVictima == 0) {
                colindante.setPuntoInteres(0);
                PDIFalsaAlarma--;
                PDITablero--;
              } else if (PDIFalsaAlarma == 0) {
                colindante.setPuntoInteres(2);
                PDIVictima--;
              }
              // Si quedan de los dos tipos, de manera aleatoria...
              else if (Math.random() < 0.5) {
                colindante.setPuntoInteres(0);
                PDIFalsaAlarma--;
                PDITablero--;
              } else {
                colindante.setPuntoInteres(2);
                PDIVictima--;
              }
            }
            // Se actualizan los hechos
            getBeliefbase().getBelief("PDITablero").setFact(PDITablero);
            getBeliefbase().getBelief("PDIVictima").setFact(PDIVictima);
            getBeliefbase().getBelief("PDIFalsaAlarma").setFact(PDIFalsaAlarma);
            // Accion realizada y PA consumidos
            accionRealizada = true;
            PA = DesplazarPlan.puntosAccionNecesarios(colindante, jugadorEsclavo);
          }

          // Si la accion se ha realizado...
          if (accionRealizada) {
            // Reducimos los puntos de accion o puntos de mando del jefe 
            if (jugadorJefe.getPuntosAccionMando() >= PA) {
              jugadorJefe.setPuntosAccionMando(jugadorJefe.getPuntosAccionMando() - PA);
            }
            else {
              PA -= jugadorJefe.getPuntosAccionMando();
              jugadorJefe.setPuntosAccionMando(0);
              jugadorJefe.setPuntosAccion(jugadorJefe.getPuntosAccion() - PA);
            }
            // Se actualiza la vista
            ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
            viewUpdater.updateTablero(t);
            getBeliefbase().getBelief("view").setFact(viewUpdater);
            // Se actualiza en la base de creencias el hecho tablero
            getBeliefbase().getBelief("tablero").setFact(t);
            // Se informa al jugador de que la accion ha sido llevada a cabo
            OrdenCompletada predicado = new OrdenCompletada();
            predicado.setPuntosAccion(PA);
            predicado.setJugador(jugadorEsclavo);
            predicado.setHabitacion(t.getHabitacion(jugadorJefe.getHabitacion()));
            predicado.setJugadores(t.getJugadoresEnHabitacion(jugadorJefe.getHabitacion()));
            IMessageEvent respuesta = peticion.createReply("Inform_Orden_Completada", predicado);
            sendMessage(respuesta);
          }
          // Si la accion no ha sido realizada por falta de PA
          else {
            System.out.println("[RECHAZAR] El jefe no dispone de PA necesarios para realizar la accion");
            // Se rechaza la peticion de accion del jugador
            IMessageEvent respuesta = peticion.createReply("Refuse_Dar_Orden", accion);
            sendMessage(respuesta);
          }
      } 
      // Accion imposible
      else {
        System.out.println("[FALLO] No se puede realizar dicha orden");
        // Se rechaza la peticion de accion del jugador
        IMessageEvent respuesta = peticion.createReply("Failure_Dar_Orden", accion);
        sendMessage(respuesta);
      }
    }
    // El jugador no es jefe
    else {
      System.out.println("[FALLO] El jugador con id " + jugadorJefe.getIdAgente() + " no tiene rol jefe");
      // Se rechaza la peticion de accion del jugador
      IMessageEvent respuesta = peticion.createReply("Failure_Dar_Orden", accion);
      sendMessage(respuesta);
    }

  }

}
