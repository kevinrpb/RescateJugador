package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class DesplazarPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe peticion de mover a un jugador");
    
    // Peticion
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    Desplazar accion = (Desplazar) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la peticion
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Casilla de destino
    Casilla destino = null;
    // Obstaculo entre origen y destino
    boolean obstaculo = false;

    switch(accion.getDireccion()) {
      case 0:
        obstaculo = hayObstaculo(c.getConexiones()[0]);
        destino = t.getMapa()[c.getPosicion()[1] - 1][c.getPosicion()[0]];
        break;
      case 1:
        obstaculo = hayObstaculo(c.getConexiones()[1]);
        destino = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] + 1];
        break;
      case 2:
        obstaculo = hayObstaculo(c.getConexiones()[2]);
        destino = t.getMapa()[c.getPosicion()[1] + 1][c.getPosicion()[0]];
        break;
      case 3:
        obstaculo = hayObstaculo(c.getConexiones()[3]);
        destino = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] - 1];
        break;
    }

    // Si hay un obstaculo que impide el desplazamiento
    if (obstaculo) {
      System.out.println("[FALLO] Desde la casilla del jugador con id " + idJugador + " en direccion " + accion.getDireccion() + " hay un obstáculo");
      // Se rechaza la peticion de accion del jugador
      IMessageEvent respuesta = peticion.createReply("Failure_Desplazar", accion);
      sendMessage(respuesta);
    }
    // El movimiento está permitido
    else {
      // PA suficientes
      int PA = puntosAccionNecesarios(destino, jugador);
      if (jugador.getPuntosAccion() + jugador.getPuntosAccionMovimiento() >= PA) {
        // La casilla destino tiene FUEGO y se está llevando una víctima
        if (destino.getTieneFuego() == 2 && (jugador.getLlevandoVictima() != 0 || jugador.getLlevandoMateriaPeligrosa())) {
          System.out.println("[RECHAZADO] Desde la casilla del jugador con id " + idJugador + " en direccion " + accion.getDireccion() + " hay fuego y el jugador lleva una victima/mat. peligrosa");
          // Se rechaza la peticion de accion del jugador
          IMessageEvent respuesta = peticion.createReply("Refuse_Desplazar", accion);
          sendMessage(respuesta);
        }
        // En caso contrario, se permite el movimiento
        else {
          // Se baja de los vehiculos si esta subido
          jugador.setSubidoAmbulancia(false);
          jugador.setSubidoCamion(false);
          // Se actualiza la posicion
          jugador.setPosicion(destino.getPosicion());
          jugador.setHabitacion(destino.getHabitacion());
          // Se consumen los PA
          if (jugador.getPuntosAccionMovimiento() >= PA) {
            jugador.setPuntosAccionMovimiento(jugador.getPuntosAccionMovimiento() - PA);
          }
          else {
            PA -= jugador.getPuntosAccionMovimiento();
            jugador.setPuntosAccionMovimiento(0);
            jugador.setPuntosAccion(jugador.getPuntosAccion() - PA);
          }
          // Se identifica PDI si lo hay
          int PDITablero = (int) getBeliefbase().getBelief("PDITablero").getFact();
          int PDIVictima = (int) getBeliefbase().getBelief("PDIVictima").getFact();
          int PDIFalsaAlarma = (int) getBeliefbase().getBelief("PDIFalsaAlarma").getFact();
          if (destino.getPuntoInteres() == 1) {
            // Si no queda de un tipo, se coloca del otro...
            if (PDIVictima == 0) {
              destino.setPuntoInteres(0);
              PDIFalsaAlarma--;
              PDITablero--;
            } else if (PDIFalsaAlarma == 0) {
              destino.setPuntoInteres(2);
              PDIVictima--;
            }
            // Si quedan de los dos tipos, de manera aleatoria...
            else if (Math.random() < 0.5) {
              destino.setPuntoInteres(0);
              PDIFalsaAlarma--;
              PDITablero--;
            } else {
              destino.setPuntoInteres(2);
              PDIVictima--;
            }
          }
          // Se actualizan los hechos
          getBeliefbase().getBelief("PDITablero").setFact(PDITablero);
          getBeliefbase().getBelief("PDIVictima").setFact(PDIVictima);
          getBeliefbase().getBelief("PDIFalsaAlarma").setFact(PDIFalsaAlarma);
          // Se actualiza la vista
          ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
          viewUpdater.updateTablero(t);
          getBeliefbase().getBelief("view").setFact(viewUpdater);
          // Se actualiza en la base de creencias el hecho tablero
          getBeliefbase().getBelief("tablero").setFact(t);
          // Se informa al jugador de que la accion ha sido llevada a cabo y se le envia la habitacion en la que esta
          Desplazado predicado = new Desplazado();
          predicado.setHabitacion(t.getHabitacion(jugador.getHabitacion()));
          predicado.setJugadores(t.getJugadoresEnHabitacion(jugador.getHabitacion()));
          IMessageEvent respuesta = peticion.createReply("Inform_Desplazado", predicado);
          sendMessage(respuesta);
        }
      }
      // No PA suficientes
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA");
        // Se rechaza la peticion de accion del jugador
        IMessageEvent respuesta = peticion.createReply("Refuse_Desplazar", accion);
        sendMessage(respuesta);
      }
    }

  }

  public static boolean hayObstaculo(int con) {
    return con == 2 || con == 3 || con == 4;
  }

  public static int puntosAccionNecesarios(Casilla destino, Jugador j) {
    if (destino.getTieneFuego() == 2 || j.getLlevandoVictima() == 1 || j.getLlevandoMateriaPeligrosa()) {
      return 2;
    }
    return 1;
  }

}
