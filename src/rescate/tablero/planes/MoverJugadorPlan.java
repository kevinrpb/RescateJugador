package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class MoverJugadorPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de mover a un jugador");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    Desplazar accion = (Desplazar) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // Casilla de destino
    Casilla destino = null;
    // Obstaculo entre origen y destino
    boolean obstaculo = false;

    switch(accion.getDireccion()) {
      case ARRIBA:
        obstaculo = hayObstaculo(c.getConexiones()[0]);
        destino = t.getMapa()[c.getPosicion()[1] - 1][c.getPosicion()[0]];
        break;
      case DERECHA:
        obstaculo = hayObstaculo(c.getConexiones()[1]);
        destino = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] + 1];
        break;
      case ABAJO:
        obstaculo = hayObstaculo(c.getConexiones()[2]);
        destino = t.getMapa()[c.getPosicion()[1] + 1][c.getPosicion()[0]];
        break;
      case IZQUIERDA:
        obstaculo = hayObstaculo(c.getConexiones()[3]);
        destino = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] - 1];
        break;
    }

    // Si hay un obstaculo que impide el desplazamiento
    if (obstaculo) {
      System.out.println("[FALLO] Desde la casilla del jugador con id " + idJugador + " en dirección " + accion.getDireccion() + " hay un obstáculo");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Desplazar");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }
    // El movimiento está permitido
    else {
      // PA suficientes
      int PA = puntosAccionNecesarios(destino, jugador);
      if (jugador.getPuntosAccion() + jugador.getPuntosAccionMovimiento() >= PA) {
        // La casilla destino tiene FUEGO y se está llevando una víctima
        if (destino.tieneFuego() == Casilla.Fuego.FUEGO && (jugador.llevandoVictima() != Jugador.LlevandoVictima.NO || jugador.llevandoMateriaPeligrosa())) {
          System.out.println("[RECHAZADO] Desde la casilla del jugador con id " + idJugador + " en dirección " + accion.getDireccion() + " hay fuego y el jugador lleva una victima/mat. peligrosa");
          // Se rechaza la petición de acción del jugador
          IMessageEvent respuesta = createMessageEvent("Refuse_Desplazar");
          respuesta.setContent(accion);
          respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
          sendMessage(respuesta);
        }
        // En caso contrario, se permite el movimiento
        else {
          // Se baja de los vehiculos si esta subido
          jugador.setSubidoAmbulancia(false);
          jugador.setSubidoCamion(false);
          // Se actualiza la posicion
          jugador.setPosicion(destino.getPosicion());
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
          if (destino.getPuntoInteres() == Casilla.PuntoInteres.OCULTO) {
            // Si no queda de un tipo, se coloca del otro...
            if (PDIVictima == 0) {
              destino.setPuntoInteres(Casilla.PuntoInteres.NADA);
              PDIFalsaAlarma--;
              PDITablero--;
            } else if (PDIFalsaAlarma == 0) {
              destino.setPuntoInteres(Casilla.PuntoInteres.VICTIMA);
              PDIVictima--;
            }
            // Si quedan de los dos tipos, de manera aleatoria...
            else if (Math.random() < 0.5) {
              destino.setPuntoInteres(Casilla.PuntoInteres.NADA);
              PDIFalsaAlarma--;
              PDITablero--;
            } else {
              destino.setPuntoInteres(Casilla.PuntoInteres.VICTIMA);
              PDIVictima--;
            }
          }
          // Se actualizan los hechos
          getBeliefbase().getBelief("PDITablero").setFact(PDITablero);
          getBeliefbase().getBelief("PDIVictima").setFact(PDIVictima);
          getBeliefbase().getBelief("PDIFalsaAlarma").setFact(PDIFalsaAlarma);
          // Se actualiza en la base de creencias el hecho tablero
          getBeliefbase().getBelief("tablero").setFact(t);
          // Se informa al jugador de que la acción ha sido llevada a cabo
          // TODO: Enviar habitacion
          IMessageEvent respuesta = createMessageEvent("Inform_Desplazado");
          respuesta.setContent(new Desplazado());
          respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
          sendMessage(respuesta);
        }
      }
      // No PA suficientes
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Desplazar");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    }

  }

  public boolean hayObstaculo(Casilla.Conexion con) {
    return con == Casilla.Conexion.PUERTA_CERRADA || con == Casilla.Conexion.PARED || con == Casilla.Conexion.PARED_SEMIRROTA;
  }

  public int puntosAccionNecesarios(Casilla destino, Jugador j) {
    if (destino.tieneFuego() == Casilla.Fuego.FUEGO || j.llevandoVictima() == Jugador.LlevandoVictima.SI || j.llevandoMateriaPeligrosa()) {
      return 2;
    }
    return 1;
  }

}
