package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class DesplazarAmbulanciaPlan extends Plan {

  public enum Aparcamiento {
    ARRIBA, DERECHA, ABAJO, IZQUIERDA
  }


  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de desplazar la ambulancia");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    DesplazarAmbulancia accion = (DesplazarAmbulancia) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    // Casilla en la que está el jugador
    Casilla c = t.getMapa()[jugador.getPosicion()[1]][jugador.getPosicion()[0]];

    Aparcamiento posicionAmbulancia; 

    if(t.getMapa()[0][3].isAmbulancia()){
      posicionAmbulancia = Aparcamiento.IZQUIERDA;
    }else if (t.getMapa()[5][0].isAmbulancia()){
      posicionAmbulancia = Aparcamiento.ARRIBA;
    }else if (t.getMapa()[9][3].isAmbulancia()){
      posicionAmbulancia = Aparcamiento.DERECHA;
    }else{
      posicionAmbulancia = Aparcamiento.ABAJO;
    }

    
      // El jugador tiene PA suficientes
      if (jugador.getPuntosAccion() > 2) {
        // Hay otro jugador jugadoresMontados
        ArrayList<Jugador> jugadoresMontados = new ArrayList<Jugador>();
        for (int i = 0; i < t.getJugadores().size(); i++) {
          if (t.getJugadores().get(i).subidoCamion()) {
            jugadoresMontados.add(t.getJugadores().get(i));
          }
        }
        boolean desplazado = false;
        // Dependiendo del destino...
        switch(accion.getDestino()) {
          case ARRIBA:
            // No puede conducir desde ABAJO
            if (posicionAmbulancia != Aparcamiento.ABAJO) {
              // Vienen de la IZQUIERDA
              if (posicionAmbulancia == Aparcamiento.IZQUIERDA) {
                // El camión abandona el aparcamiento actual
                t.getMapa()[0][3].setCamionBomberos(false);
                t.getMapa()[0][4].setCamionBomberos(false);
              }
              // Vienen de la DERECHA
              else {
                // El camión abandona el aparcamiento actual
                t.getMapa()[9][3].setCamionBomberos(false);
                t.getMapa()[9][4].setCamionBomberos(false);
              }
              // Se desplaza al aparcamiento ARRIBA
              t.getMapa()[5][0].setCamionBomberos(true);
              t.getMapa()[6][0].setCamionBomberos(true);

              // Los jugadores se desplazan
              for(int i=0; i<2 && !jugadoresMontados.isEmpty();i++){
                jugadoresMontados.get(0).setPosicion(new int[]{0,2+i});
                jugadoresMontados.remove(0);
              }
              desplazado = true;
            }
            break;
          case DERECHA:
            // No puede conducir desde IZQUIERDA
            if (posicionAmbulancia != Aparcamiento.IZQUIERDA) {
              // Vienen de ARRIBA
              if (posicionAmbulancia == Aparcamiento.ARRIBA) {
                // El camión abandona el aparcamiento actual
                t.getMapa()[5][0].setCamionBomberos(false);
                t.getMapa()[6][0].setCamionBomberos(false);
              }
              // Vienen de ABAJO
              else {
                // El camión abandona el aparcamiento actual
                t.getMapa()[3][7].setCamionBomberos(false);
                t.getMapa()[4][7].setCamionBomberos(false);
              }
              // Se desplaza al aparcamiento DERECHA
              t.getMapa()[9][3].setCamionBomberos(true);
              t.getMapa()[9][4].setCamionBomberos(true);
              // Los jugadores se desplazan
              for(int i=0; i<2 && !jugadoresMontados.isEmpty();i++){
                jugadoresMontados.get(0).setPosicion(new int[]{9,3+i});
                jugadoresMontados.remove(0);
              }
              
              desplazado = true;
            }
            break;
          case ABAJO:
            // No puede conducir desde ARRIBA
            if (posicionAmbulancia != Aparcamiento.ARRIBA) {
              // Vienen de la IZQUIERDA
              if (posicionAmbulancia == Aparcamiento.IZQUIERDA) {
                // El camión abandona el aparcamiento actual
                t.getMapa()[0][3].setCamionBomberos(false);
                t.getMapa()[0][4].setCamionBomberos(false);
              }
              // Vienen de la DERECHA
              else {
                // El camión abandona el aparcamiento actual
                t.getMapa()[9][3].setCamionBomberos(false);
                t.getMapa()[9][4].setCamionBomberos(false);
              }
              // Se desplaza al aparcamiento ABAJO
              t.getMapa()[3][7].setCamionBomberos(true);
              t.getMapa()[4][7].setCamionBomberos(true);
              // Los jugadores se desplazan
              for(int i=0; i<2 && !jugadoresMontados.isEmpty();i++){
                jugadoresMontados.get(0).setPosicion(new int[]{3+i,7});
                jugadoresMontados.remove(0);
              }
              desplazado = true;
            }
            break;
          case IZQUIERDA:
            // No puede conducir desde DERECHA
            if (posicionAmbulancia != Aparcamiento.DERECHA) {
              // Vienen de ARRIBA
              if (posicionAmbulancia == Aparcamiento.ARRIBA) {
                // El camión abandona el aparcamiento actual
                t.getMapa()[5][0].setCamionBomberos(false);
                t.getMapa()[6][0].setCamionBomberos(false);
              }
              // Vienen de ABAJO
              else {
                // El camión abandona el aparcamiento actual
                t.getMapa()[3][7].setCamionBomberos(false);
                t.getMapa()[4][7].setCamionBomberos(false);
              }
              // Se desplaza al aparcamiento IZQUIERDA
              t.getMapa()[0][3].setCamionBomberos(true);
              t.getMapa()[0][4].setCamionBomberos(true);
              // Los jugadores se desplazan
              for(int i=0; i<2 && !jugadoresMontados.isEmpty();i++){
                jugadoresMontados.get(0).setPosicion(new int[]{0,2+i});
                jugadoresMontados.remove(0);
              }
              desplazado = true;
            }
            break;
        } 
        // Si finalmente se ha desplazado...
        if (desplazado) {
          System.out.println("[INFO] El camión  y los jugadores en él se han desplazado al aparcamiento: " + accion.getDestino());
          // Se actualiza el jugador (consumo de PA)
          jugador.setPuntosAccion(jugador.getPuntosAccion() - 2);
          // Se actualiza en la base de creencias el hecho tablero
          getBeliefbase().getBelief("tablero").setFact(t);
          // Se informa al jugador de que la acción ha sido llevada a cabo
          IMessageEvent respuesta = createMessageEvent("Inform_Desplazar_Ambulancia");
          respuesta.setContent(new AmbulanciaDesplazada());
          respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
          sendMessage(respuesta);
        }
        else {
          System.out.println("[RECHAZADO] El aparcamiento de destino es inalcanzable desde la posición actual");
          // Se rechaza la petición de acción del jugador
          IMessageEvent respuesta = createMessageEvent("Refuse_Desplazar_Ambulancia");
          respuesta.setContent(accion);
          respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
          sendMessage(respuesta);
        }
      }
      // No PA suficientes
      else {
        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene PA suficientes para conducir el camión");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Desplazar_Ambulancia");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      }
    

  }

}
