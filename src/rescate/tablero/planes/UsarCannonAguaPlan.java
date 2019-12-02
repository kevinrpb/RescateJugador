package rescate.tablero.planes;

import java.util.*;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

class UsarCannonDeAguaPlan extends Plan {

  public enum Aparcamiento {
    ARRIBA, DERECHA, ABAJO, IZQUIERDA
  }
  Tablero t = null;

  @Override
  public void body() {

    System.out.println("[PLAN] El t recibe petición de usar el cañón de agua");
    
    
    // Petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // t
    t = (Tablero) getBeliefbase().getBelief("t").getFact();

    // Parámetros de la peticion
    AgentIdentifier idJugador = (AgentIdentifier) peticion.getParameter("sender").getValue();
    UsarCannonAgua accion = (UsarCannonAgua) peticion.getContent();

    // Se encuentra en la lista de jugadores del t el jugador con id igual al de la petición
    Jugador jugador = t.getJugador(idJugador);

    

    if(!jugador.subidoCamion()){

      System.out.println("[FALLO] El jugador " + idJugador + " no esta en el camion de bomberos");
      // Se rechaza la petición de acción del jugador
      IMessageEvent respuesta = createMessageEvent("Failure_Usar_Cannon_Agua");
      respuesta.setContent(accion);
      respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
      sendMessage(respuesta);

    }
    else {

      if (jugador.getPuntosAccion() > 4 || (jugador.getRol() == Jugador.Rol.CONDUCTOR && jugador.getPuntosAccion() > 2)){
        
        // Se tiran los dados
        int [] posicion = tirarDados(); 

        // Pregunatmos al conductor de ambualncia si quiere volver a tirar los dados 
        if(jugador.getRol() == Jugador.Rol.CONDUCTOR){
            
          IMessageEvent r = createMessageEvent("Request_Aceptar_Tirada");
          r.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
          AceptarTirada aceptar = new AceptarTirada(posicion);
          r.setContent(aceptar);
          IMessageEvent respuesta = sendMessageAndWait(r, 10000);
          aceptar = (AceptarTirada) respuesta.getContent();

          System.out.println("La tirada ha sido " +  ((aceptar.tirada_aceptada == true) ? "aceptada" : "rechazada") + " por el juador "+ idJugador);

          // Si la respuesta es negativa se vulven a tirar los dados 
          if (aceptar.tirada_aceptada == false){
            posicion = tirarDados();
          }

        }
        apagarAdyacentes(posicion);

      }
      else {

        System.out.println("[RECHAZADO] El jugador con id " + idJugador + " no tiene suficientes PA para usar cañon de agua");
        // Se rechaza la petición de acción del jugador
        IMessageEvent respuesta = createMessageEvent("Refuse_Usar_Cannon_Agua");
        respuesta.setContent(accion);
        respuesta.getParameterSet(SFipa.RECEIVERS).addValue(idJugador);
        sendMessage(respuesta);

      }
 
    }

  }


  public int[] tirarDados(){
    int[] posicion;
        if (t.getMapa()[0][7].esCamionBomberos()) {
          //Cuadrante de arriba-derecha
          posicion[0] = (int)(Math.random() * 4 + 5);// entre 5 y 8
          posicion[1] = (int)(Math.random() * 3 + 1);// entre 1 y 3
        }else if(t.getMapa()[5][9].esCamionBomberos()){
          //Cuadrante de abajo-derecha
          posicion[0] = (int)(Math.random() * 4 + 5);// entre 5 y 8
          posicion[1] = (int)(Math.random() * 3 + 4);// entre 4 y 6
        }else if(t.getMapa()[1][0].esCamionBomberos()){
          //Cuadrante de arriba-izquierda
          posicion[0] = (int)(Math.random() * 4 + 1);// entre 1 y 4
          posicion[1] = (int)(Math.random() * 3 + 1);// entre 1 y 3
        }else{
          //Cuadrante de abajo-izquierda
          posicion[0] = (int)(Math.random() * 4 + 1);// entre 1 y 4
          posicion[1] = (int)(Math.random() * 3 + 4);// entre 4 y 6
          
        }
    return posicion;
  }

  public void apagarAdyacentes(int[]posicion){

    Casilla c =  t.getMapa()[posicion[0]][posicion[1]];
    Casilla.Conexion[] conexiones =  c.getConexiones();;

    c.setTieneFuego(Casilla.Fuego.NADA);

    //Casilla adyacente arriba
    if(conexiones[0] == Casilla.Conexion.NADA || conexiones[0] == Casilla.Conexion.PUERTA_ABIERTA || conexiones[0] == Casilla.Conexion.PARED_ROTA){
      t.getMapa()[posicion[1]][posicion[0]].setTieneFuego(Casilla.Fuego.NADA);
    }
    //Casilla adyacente derecha
    if(conexiones[1] == Casilla.Conexion.NADA || conexiones[1] == Casilla.Conexion.PUERTA_ABIERTA || conexiones[1] == Casilla.Conexion.PARED_ROTA){
      t.getMapa()[posicion[1]][posicion[0]+1].setTieneFuego(Casilla.Fuego.NADA);
    }
    //Casilla adyacente abajo
    if(conexiones[2] == Casilla.Conexion.NADA || conexiones[2] == Casilla.Conexion.PUERTA_ABIERTA || conexiones[2] == Casilla.Conexion.PARED_ROTA){
      t.getMapa()[posicion[1]+1][posicion[0]].setTieneFuego(Casilla.Fuego.NADA);
    }
    
    //Casilla adyacente izquierda
    if(conexiones[3] == Casilla.Conexion.NADA || conexiones[3] == Casilla.Conexion.PUERTA_ABIERTA || conexiones[3] == Casilla.Conexion.PARED_ROTA){
      t.getMapa()[posicion[1]][posicion[0]-1].setTieneFuego(Casilla.Fuego.NADA);
    }

    
  }

  }

}
