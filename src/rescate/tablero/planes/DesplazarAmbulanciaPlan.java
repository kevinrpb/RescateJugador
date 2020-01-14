package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class DesplazarAmbulanciaPlan extends Plan {


  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe peticion de desplazar la ambulancia");

    // Peticion
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    DesplazarAmbulancia accion = (DesplazarAmbulancia) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la peticion
    Jugador jugador = t.getJugador(idJugador);

    // Posicion ambulancia
    int posicionAmbulancia;
    if (t.getMapa()[3][0].getAmbulancia()) {
      posicionAmbulancia = 3;
    } else if (t.getMapa()[0][5].getAmbulancia()) {
      posicionAmbulancia = 0;
    } else if (t.getMapa()[3][9].getAmbulancia()) {
      posicionAmbulancia = 1;
    } else {
      posicionAmbulancia = 2;
    }

    // El jugador tiene PA suficientes
    if (jugador.getPuntosAccion() > 1) {
      // Hay jugadores montados
      ArrayList<Jugador> jugadoresMontados = new ArrayList<Jugador>();
      for (int i = 0; i < t.getJugadores().size(); i++) {
        if (t.getJugadores().get(i).getSubidoAmbulancia()) {
          jugadoresMontados.add(t.getJugadores().get(i));
        }
      }
      boolean desplazado = false;
      int victimas_salvadas = 0;
      // Dependiendo del destino...
      switch (accion.getDestino()) {
        case 0:
          // No puede conducir desde ABAJO
          if (posicionAmbulancia != 2) {
            // Vienen de la IZQUIERDA
            if (posicionAmbulancia == 3) {
              // El camion abandona el aparcamiento actual
              t.getMapa()[3][0].setAmbulancia(false);
              t.getMapa()[4][0].setAmbulancia(false);
            }
            // Vienen de la DERECHA
            else {
              // El camion abandona el aparcamiento actual
              t.getMapa()[3][9].setAmbulancia(false);
              t.getMapa()[4][9].setAmbulancia(false);
            }
            
            // Se desplaza al aparcamiento ARRIBA
            t.getMapa()[0][5].setAmbulancia(true);
            t.getMapa()[0][6].setAmbulancia(true);

            // Se salvan las victimas de esas posiciones 
            if(t.getMapa()[0][5].getPuntoInteres() == 2) {
              t.getMapa()[0][5].setPuntoInteres(0);
              victimas_salvadas++;
            } 
            if(t.getMapa()[0][6].getPuntoInteres() == 2) {
              t.getMapa()[0][6].setPuntoInteres(0);
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
        case 1:
          // No puede conducir desde IZQUIERDA
          if (posicionAmbulancia != 3) {
            // Vienen de ARRIBA
            if (posicionAmbulancia == 0) {
              // El camion abandona el aparcamiento actual
              t.getMapa()[0][5].setAmbulancia(false);
              t.getMapa()[0][6].setAmbulancia(false);
            }
            // Vienen de ABAJO
            else {
              // El camion abandona el aparcamiento actual
              t.getMapa()[7][3].setAmbulancia(false);
              t.getMapa()[7][4].setAmbulancia(false);
            }
            // Se desplaza al aparcamiento DERECHA
            t.getMapa()[3][9].setAmbulancia(true);
            t.getMapa()[4][9].setAmbulancia(true);
            
            // Se salvan las victimas de esas posiciones 
            if(t.getMapa()[3][9].getPuntoInteres() == 2) {
              t.getMapa()[3][9].setPuntoInteres(0);
              victimas_salvadas++;
            }
            if(t.getMapa()[4][9].getPuntoInteres() == 2){
              t.getMapa()[4][9].setPuntoInteres(0);
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
        case 2:
          // No puede conducir desde ARRIBA
          if (posicionAmbulancia != 0) {
            // Vienen de la IZQUIERDA
            if (posicionAmbulancia == 3) {
              // El camion abandona el aparcamiento actual
              t.getMapa()[3][0].setAmbulancia(false);
              t.getMapa()[4][0].setAmbulancia(false);
            }
            // Vienen de la DERECHA
            else {
              // El camion abandona el aparcamiento actual
              t.getMapa()[3][9].setAmbulancia(false);
              t.getMapa()[4][9].setAmbulancia(false);
            }
            // Se desplaza al aparcamiento ABAJO
            t.getMapa()[7][3].setAmbulancia(true);
            t.getMapa()[7][4].setAmbulancia(true);

            // Se salvan las victimas de esas posiciones 
            if(t.getMapa()[7][3].getPuntoInteres() == 2) {
              t.getMapa()[7][3].setPuntoInteres(0);
              victimas_salvadas++;
            }
            if(t.getMapa()[7][4].getPuntoInteres() == 2) {
              t.getMapa()[7][4].setPuntoInteres(0);
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
        case 3:
          // No puede conducir desde DERECHA
          if (posicionAmbulancia != 1) {
            // Vienen de ARRIBA
            if (posicionAmbulancia == 0) {
              // El camion abandona el aparcamiento actual
              t.getMapa()[0][5].setAmbulancia(false);
              t.getMapa()[0][6].setAmbulancia(false);
            }
            // Vienen de ABAJO
            else {
              // El camion abandona el aparcamiento actual
              t.getMapa()[7][3].setAmbulancia(false);
              t.getMapa()[7][4].setAmbulancia(false);
            }
            // Se desplaza al aparcamiento IZQUIERDA
            t.getMapa()[3][0].setAmbulancia(true);
            t.getMapa()[4][0].setAmbulancia(true);

            // Se salvan las victimas de esas posiciones 
            if(t.getMapa()[3][0].getPuntoInteres() == 2) {
              t.getMapa()[3][0].setPuntoInteres(0);
              victimas_salvadas++;
            }
            if(t.getMapa()[4][0].getPuntoInteres() == 2){
              t.getMapa()[4][0].setPuntoInteres(0);
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
        // Se actualiza la vista
        ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
        viewUpdater.updateTablero(t);
        getBeliefbase().getBelief("view").setFact(viewUpdater);
        // Se actualiza en la base de creencias el hecho tablero
        getBeliefbase().getBelief("tablero").setFact(t);
        // Se reduce en uno los PDI en el tablero
        getBeliefbase().getBelief("PDITablero").setFact((int) getBeliefbase().getBelief("PDITablero").getFact() - victimas_salvadas);
        // Se aumenta en uno las victimas salvadas
        getBeliefbase().getBelief("salvados").setFact((int) getBeliefbase().getBelief("salvados").getFact() + victimas_salvadas);
        // Se informa al jugador de que la accion ha sido llevada a cabo
        AmbulanciaDesplazada predicado = new AmbulanciaDesplazada();
        predicado.setHabitacion(t.getHabitacion(jugador.getHabitacion()));
        predicado.setJugadores(t.getJugadoresEnHabitacion(jugador.getHabitacion()));
        IMessageEvent respuesta = peticion.createReply("Inform_Ambulancia_Desplazada", predicado);
        sendMessage(respuesta);
      // No se ha desplazado...
      } else {
        System.out.println("[RECHAZADO] El aparcamiento de destino es inalcanzable desde la posicion actual");
        // Se rechaza la peticion de accion del jugador
        IMessageEvent respuesta = peticion.createReply("Refuse_Desplazar_Ambulancia", accion);
        sendMessage(respuesta);
      }
    }
    // No PA suficientes
    else {
      System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene PA suficientes para conducir el camion");
      // Se rechaza la peticion de accion del jugador
      IMessageEvent respuesta = peticion.createReply("Refuse_Desplazar_Ambulancia", accion);
      sendMessage(respuesta);
    }
  }

}
