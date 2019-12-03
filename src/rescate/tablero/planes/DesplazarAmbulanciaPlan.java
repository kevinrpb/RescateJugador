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

    // Posición ambulancia
    Aparcamiento posicionAmbulancia;
    if (t.getMapa()[3][0].esAmbulancia()) {
      posicionAmbulancia = Aparcamiento.IZQUIERDA;
    } else if (t.getMapa()[0][5].esAmbulancia()) {
      posicionAmbulancia = Aparcamiento.ARRIBA;
    } else if (t.getMapa()[3][9].esAmbulancia()) {
      posicionAmbulancia = Aparcamiento.DERECHA;
    } else {
      posicionAmbulancia = Aparcamiento.ABAJO;
    }

    // El jugador tiene PA suficientes
    if (jugador.getPuntosAccion() > 2) {
      // Hay jugadores montados
      ArrayList<Jugador> jugadoresMontados = new ArrayList<Jugador>();
      for (int i = 0; i < t.getJugadores().size(); i++) {
        if (t.getJugadores().get(i).subidoAmbulancia()) {
          jugadoresMontados.add(t.getJugadores().get(i));
        }
      }
      boolean desplazado = false;
      int victimas_salvadas = 0;
      // Dependiendo del destino...
      switch (accion.getDestino()) {
        case ARRIBA:
          // No puede conducir desde ABAJO
          if (posicionAmbulancia != Aparcamiento.ABAJO) {
            // Vienen de la IZQUIERDA
            if (posicionAmbulancia == Aparcamiento.IZQUIERDA) {
              // El camión abandona el aparcamiento actual
              t.getMapa()[3][0].setAmbulancia(false);
              t.getMapa()[4][0].setAmbulancia(false);
            }
            // Vienen de la DERECHA
            else {
              // El camión abandona el aparcamiento actual
              t.getMapa()[3][9].setAmbulancia(false);
              t.getMapa()[4][9].setAmbulancia(false);
            }
            
            // Se desplaza al aparcamiento ARRIBA
            t.getMapa()[0][5].setAmbulancia(true);
            t.getMapa()[0][6].setAmbulancia(true);

            // Se salvan las victimas de esas posiciones 
            if(t.getMapa()[0][5].getPuntoInteres() == Casilla.PuntoInteres.VICTIMA) {
              t.getMapa()[0][5].setPuntoInteres(Casilla.PuntoInteres.NADA);
              victimas_salvadas++;
            } 
            if(t.getMapa()[0][6].getPuntoInteres() == Casilla.PuntoInteres.VICTIMA) {
              t.getMapa()[0][6].setPuntoInteres(Casilla.PuntoInteres.NADA);
              victimas_salvadas++;
            }
            
            // Los jugadores se desplazan
            for (int i = 0; i < 2 && !jugadoresMontados.isEmpty(); i++) {
              jugadoresMontados.get(0).setPosicion(new int[] {5 + i, 0});
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
              t.getMapa()[0][5].setAmbulancia(false);
              t.getMapa()[0][6].setAmbulancia(false);
            }
            // Vienen de ABAJO
            else {
              // El camión abandona el aparcamiento actual
              t.getMapa()[7][3].setAmbulancia(false);
              t.getMapa()[7][4].setAmbulancia(false);
            }
            // Se desplaza al aparcamiento DERECHA
            t.getMapa()[3][9].setAmbulancia(true);
            t.getMapa()[4][9].setAmbulancia(true);
            
            // Se salvan las victimas de esas posiciones 
            if(t.getMapa()[3][9].getPuntoInteres() == Casilla.PuntoInteres.VICTIMA) {
              t.getMapa()[3][9].setPuntoInteres(Casilla.PuntoInteres.NADA);
              victimas_salvadas++;
            }
            if(t.getMapa()[4][9].getPuntoInteres() == Casilla.PuntoInteres.VICTIMA){
              t.getMapa()[4][9].setPuntoInteres(Casilla.PuntoInteres.NADA);
              victimas_salvadas++;
            }

            // Los jugadores se desplazan
            for (int i = 0; i < 2 && !jugadoresMontados.isEmpty(); i++) {
              jugadoresMontados.get(0).setPosicion(new int[] {9, 3 + i});
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
              t.getMapa()[3][0].setAmbulancia(false);
              t.getMapa()[4][0].setAmbulancia(false);
            }
            // Vienen de la DERECHA
            else {
              // El camión abandona el aparcamiento actual
              t.getMapa()[3][9].setAmbulancia(false);
              t.getMapa()[4][9].setAmbulancia(false);
            }
            // Se desplaza al aparcamiento ABAJO
            t.getMapa()[7][3].setAmbulancia(true);
            t.getMapa()[7][4].setAmbulancia(true);

            // Se salvan las victimas de esas posiciones 
            if(t.getMapa()[7][3].getPuntoInteres() == Casilla.PuntoInteres.VICTIMA) {
              t.getMapa()[7][3].setPuntoInteres(Casilla.PuntoInteres.NADA);
              victimas_salvadas++;
            }
            if(t.getMapa()[7][4].getPuntoInteres() == Casilla.PuntoInteres.VICTIMA) {
              t.getMapa()[7][4].setPuntoInteres(Casilla.PuntoInteres.NADA);
              victimas_salvadas++;
            }

            // Los jugadores se desplazan
            for (int i = 0; i < 2 && !jugadoresMontados.isEmpty(); i++) {
              jugadoresMontados.get(0).setPosicion(new int[] {3 + i, 7});
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
              t.getMapa()[0][5].setAmbulancia(false);
              t.getMapa()[0][6].setAmbulancia(false);
            }
            // Vienen de ABAJO
            else {
              // El camión abandona el aparcamiento actual
              t.getMapa()[7][3].setAmbulancia(false);
              t.getMapa()[7][4].setAmbulancia(false);
            }
            // Se desplaza al aparcamiento IZQUIERDA
            t.getMapa()[3][0].setAmbulancia(true);
            t.getMapa()[4][0].setAmbulancia(true);

            // Se salvan las victimas de esas posiciones 
            if(t.getMapa()[3][0].getPuntoInteres() == Casilla.PuntoInteres.VICTIMA) {
              t.getMapa()[3][0].setPuntoInteres(Casilla.PuntoInteres.NADA);
              victimas_salvadas++;
            }
            if(t.getMapa()[4][0].getPuntoInteres() == Casilla.PuntoInteres.VICTIMA){
              t.getMapa()[4][0].setPuntoInteres(Casilla.PuntoInteres.NADA);
              victimas_salvadas++;
            }

            // Los jugadores se desplazan
            for (int i = 0; i < 2 && !jugadoresMontados.isEmpty(); i++) {
              jugadoresMontados.get(0).setPosicion(new int[] {0, 3 + i});
              jugadoresMontados.remove(0);
            }
            
            desplazado = true;
          }
          break;
      }
      // Si finalmente se ha desplazado...
      if (desplazado) {
        System.out.println("[INFO] La ambulancia y los jugadores en ella se han desplazado al aparcamiento: " + accion.getDestino());
        // Se actualiza el jugador (consumo de PA)
        jugador.setPuntosAccion(jugador.getPuntosAccion() - 2);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se reduce en uno los PDI en el tablero
        getBeliefbase().getBelief("PDITablero").setFact((int) getBeliefbase().getBelief("PDITablero").getFact() - victimas_salvadas);
        // Se aumenta en uno las victimas salvadas
        getBeliefbase().getBelief("salvados").setFact((int) getBeliefbase().getBelief("salvados").getFact() + victimas_salvadas);

        // Se informa al jugador de que la acción ha sido llevada a cabo
        IMessageEvent respuesta = createMessageEvent("Inform_Desplazar_Ambulancia");
        respuesta.setContent(new AmbulanciaDesplazada());
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);
      } else {
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
