package rescate.tablero.planes;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class ConducirCamionPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de conducir el camión de bomberos");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    ConducirCamion accion = (ConducirCamion) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    // El jugador está subido al camión
    if (jugador.subidoCamion()) {
      // El jugador tiene PA suficientes
      if (jugador.getPuntosAccion() > 1) {
        // Hay otro jugador montado
        Jugador montado = null;
        for (int i = 0; i < t.getJugadores().size(); i++) {
          if (t.getJugadores().get(i).getIdAgente() != idJugador && t.getJugadores().get(i).subidoCamion()) {
            montado = t.getJugadores().get(i);
            break;
          }
        }
        boolean desplazado = false;
        // Dependiendo del destino...
        switch(accion.getDestino()) {
          case ARRIBA:
            // No puede conducir desde ABAJO
            if (jugador.getPosicion()[1] != t.getMapa().length - 1) {
              // Vienen de la IZQUIERDA
              if (jugador.getPosicion()[0] == 0) {
                // El camión abandona el aparcamiento actual
                t.getMapa()[1][0].setCamionBomberos(false);
                t.getMapa()[2][0].setCamionBomberos(false);
              }
              // Vienen de la DERECHA
              else {
                // El camión abandona el aparcamiento actual
                t.getMapa()[5][9].setCamionBomberos(false);
                t.getMapa()[6][9].setCamionBomberos(false);
              }
              // Se desplaza al aparcamiento ARRIBA
              t.getMapa()[0][7].setCamionBomberos(true);
              t.getMapa()[0][8].setCamionBomberos(true);
              // Los jugadores se desplazan
              jugador.setPosicion(new int[]{0, 7});
              if (montado != null) {
                montado.setPosicion(new int[]{0, 8});
              }
              desplazado = true;
            }
            break;
          case DERECHA:
            // No puede conducir desde IZQUIERDA
            if (jugador.getPosicion()[0] != 0) {
              // Vienen de ARRIBA
              if (jugador.getPosicion()[1] == 0) {
                // El camión abandona el aparcamiento actual
                t.getMapa()[0][7].setCamionBomberos(false);
                t.getMapa()[0][8].setCamionBomberos(false);
              }
              // Vienen de ABAJO
              else {
                // El camión abandona el aparcamiento actual
                t.getMapa()[7][1].setCamionBomberos(false);
                t.getMapa()[7][2].setCamionBomberos(false);
              }
              // Se desplaza al aparcamiento DERECHA
              t.getMapa()[5][9].setCamionBomberos(true);
              t.getMapa()[6][9].setCamionBomberos(true);
              // Los jugadores se desplazan
              jugador.setPosicion(new int[]{5, 9});
              if (montado != null) {
                montado.setPosicion(new int[]{6, 9});
              }
              desplazado = true;
            }
            break;
          case ABAJO:
            // No puede conducir desde ARRIBA
            if (jugador.getPosicion()[1] != 0) {
              // Vienen de la IZQUIERDA
              if (jugador.getPosicion()[0] == 0) {
                // El camión abandona el aparcamiento actual
                t.getMapa()[1][0].setCamionBomberos(false);
                t.getMapa()[2][0].setCamionBomberos(false);
              }
              // Vienen de la DERECHA
              else {
                // El camión abandona el aparcamiento actual
                t.getMapa()[5][9].setCamionBomberos(false);
                t.getMapa()[6][9].setCamionBomberos(false);
              }
              // Se desplaza al aparcamiento ABAJO
              t.getMapa()[7][1].setCamionBomberos(true);
              t.getMapa()[7][2].setCamionBomberos(true);
              // Los jugadores se desplazan
              jugador.setPosicion(new int[]{7, 1});
              if (montado != null) {
                montado.setPosicion(new int[]{7, 2});
              }
              desplazado = true;
            }
            break;
          case IZQUIERDA:
            // No puede conducir desde DERECHA
            if (jugador.getPosicion()[0] != t.getMapa().length - 1) {
              // Vienen de ARRIBA
              if (jugador.getPosicion()[1] == 0) {
                // El camión abandona el aparcamiento actual
                t.getMapa()[0][7].setCamionBomberos(false);
                t.getMapa()[0][8].setCamionBomberos(false);
              }
              // Vienen de ABAJO
              else {
                // El camión abandona el aparcamiento actual
                t.getMapa()[7][1].setCamionBomberos(false);
                t.getMapa()[7][2].setCamionBomberos(false);
              }
              // Se desplaza al aparcamiento IZQUIERDA
              t.getMapa()[1][0].setCamionBomberos(true);
              t.getMapa()[2][0].setCamionBomberos(true);
              // Los jugadores se desplazan
              jugador.setPosicion(new int[]{1, 0});
              if (montado != null) {
                montado.setPosicion(new int[]{2, 0});
              }
              desplazado = true;
            }
            break;
        } 
        // Si finalmente se ha desplazado...
        if (desplazado) {
          System.out.println("[INFO] El camión y los jugadores en él se han desplazado al aparcamiento: " + accion.getDestino());
          // Se actualiza el jugador (consumo de PA)
          jugador.setPuntosAccion(jugador.getPuntosAccion() - 2);
          // Se actualiza en la base de creencias el hecho tablero
          getBeliefbase().getBelief("tablero").setFact(t);
          // Se informa al jugador de que la acción ha sido llevada a cabo
          IMessageEvent respuesta = createMessageEvent("Inform_Camion_Conducido");
          respuesta.setContent(new CamionConducido());
          respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
          sendMessage(respuesta);
        }
        else {
          System.out.println("[RECHAZADO] El aparcamiento de destino es inalcanzable desde la posición actual");
          // Se rechaza la petición de acción del jugador
          IMessageEvent respuesta = createMessageEvent("Refuse_Conducir_Camion");
          respuesta.setContent(accion);
          respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
          sendMessage(respuesta);
        }
      }
      // No PA suficientes
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene PA suficientes para conducir el camión");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Conducir_Camion");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    }
    // Tiene que estar montado en el camión
    else {
      System.out.println("[FALLO] El jugador con id " + idJugador + " debe estar subido al camión para conducirlo");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Conducir_Camion");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }
    
  }

}
