package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class MandoPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero recibe petición de mando");
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    DarOrden accion = (DarOrden) peticion.getContent();

    // Se encuentra en la lista de jugadores del tablero el jugador con id igual al de la petición
    Jugador jugador_jefe = t.getJugador(idJugador);
    // Se encuentra el jugador sobre el que se hace el mando
    Jugador jugador_esclavo = t.getJugador(accion.getIdJugador());
    
    Casilla c = t.getMapa()[jugador_esclavo.getPosicion()[1]][jugador_esclavo.getPosicion()[0]];
    Casilla.Conexion conexion = c.getConexiones()[accion.getConexion()];

    // Comprobamos si los jugadores estan en la misma habitación y si la accion sobre la conexion recibida es posible
    if(jugador_jefe.getHabitacion()==jugador_esclavo.getHabitacion() 
      || (accion.getAccion() == DarOrden.Mandato.ABRIR && conexion == Casilla.Conexion.PUERTA_CERRADA)
      || (accion.getAccion() == DarOrden.Mandato.CERRAR && conexion == Casilla.Conexion.PUERTA_ABIERTA)
      || (accion.getAccion() == DarOrden.Mandato.MOVER && !MoverJugadorPlan.hayObstaculo(conexion))){


        boolean accion_realizada = false;
        int puntosAccion = 0;

        // Casilla + conexion --> colindante
        Casilla colindante = null;
          switch(accion.getConexion()) {
            case 0:
              colindante = t.getMapa()[c.getPosicion()[1] - 1][c.getPosicion()[0]];
              break;
            case 1:
              colindante = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] + 1];
              break;
            case 2:
              colindante = t.getMapa()[c.getPosicion()[1] + 1][c.getPosicion()[0]];
              break;
            case 3:
              colindante = t.getMapa()[c.getPosicion()[1]][c.getPosicion()[0] - 1];
              break;
          }

        // La acción es sobre una puerta (abrir o cerrar)
        if((accion.getAccion() == DarOrden.Mandato.ABRIR || accion.getAccion() == DarOrden.Mandato.CERRAR) && jugador_jefe.getPuntosAccion() + jugador_jefe.getPuntosAccionMando() > 1){

          Casilla.Conexion nuevo_estado = ((accion.getAccion() == DarOrden.Mandato.ABRIR) ? Casilla.Conexion.PUERTA_ABIERTA  :  Casilla.Conexion.PUERTA_CERRADA);
          System.out.println("[INFO] El jugador con id " + accion.getIdJugador() + " ha abierto una puerta en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "] debido a la orden del jugador "+ idJugador);
          // Se modifica la conexion a puerta abierta
          conexion = nuevo_estado;
          // Casilla colindante (donde también esta la referencia a la puerta cerrada y hay que abrirla)
          
          switch (accion.getConexion()) {
            // Arriba
            case 0:
              colindante.getConexiones()[2] = nuevo_estado;
              break;
            // Derecha
            case 1:
              colindante.getConexiones()[3] = nuevo_estado;
              break;
            // Abajo
            case 2:
              colindante.getConexiones()[0] = nuevo_estado;
              break;
            // Izquierda
            case 3:
              colindante.getConexiones()[1] = nuevo_estado;
              break;
            // ...
            default:
              break;
          }

          accion_realizada = true;
          puntosAccion = 1;
          
        
        }else if( accion.getAccion() == DarOrden.Mandato.MOVER && jugador_jefe.getPuntosAccion() + ((jugador_esclavo.getRol() != Jugador.Rol.ESPUMA_IGNIFUGA) ? jugador_jefe.getPuntosAccionMando() : 1)  > MoverJugadorPlan.puntosAccionNecesarios(colindante, jugador_esclavo)){
            jugador_esclavo.setPosicion(colindante.getPosicion());
            accion_realizada = true;
            puntosAccion = MoverJugadorPlan.puntosAccionNecesarios(colindante, jugador_esclavo);
          }

        if(accion_realizada){

          // Reducimos los puntos de accion o puntos de mando del jefe 
          if(jugador_jefe.getPuntosAccion()>=puntosAccion)  jugador_jefe.setPuntosAccion(jugador_jefe.getPuntosAccion() - puntosAccion);
          else {
            jugador_jefe.setPuntosAccionMando(jugador_jefe.getPuntosAccionMando() - (puntosAccion - jugador_jefe.getPuntosAccion()));
            jugador_jefe.setPuntosAccion(0);
          }
           // Se actualiza en la base de creencias el hecho tablero
          getBeliefbase().getBelief("tablero").setFact(t);
          // Se informa al jugador de que la acción ha sido llevada a cabo
          IMessageEvent respuesta = createMessageEvent("Inform_Orden_Recibida");
          respuesta.setContent(new OrdenRecibida(puntosAccion));
          respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
          sendMessage(respuesta);
        }else {
          System.out.println("[FALLO] El jefe no dispone de PA necesarios para realizar la accion");
          // Se rechaza la petición de acción del jugador
          IMessageEvent respuesta = createMessageEvent("Refuse_Dar_Orden");
          respuesta.setContent(accion);
          respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
          sendMessage(respuesta);
        }


    }else {
      System.out.println("[FALLO] No se puede realizar dicha orden");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Dar_Orden");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);
    }

  }

  
    


}
