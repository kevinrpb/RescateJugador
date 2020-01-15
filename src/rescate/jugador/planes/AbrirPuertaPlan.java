package rescate.jugador.planes;

import java.util.ArrayList;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

import rescate.jugador.estrategias.*;
import rescate.jugador.util.*;

@SuppressWarnings("serial")
public class AbrirPuertaPlan extends Plan {

  public void body() {

    AgentIdentifier idTablero = (AgentIdentifier) getBeliefbase().getBelief("tablero").getFact();

    // Cargamos la info del jugador
    Jugador jugador = (Jugador) getBeliefbase().getBelief("jugador").getFact();
    int rol = jugador.getRol();
    int X = jugador.getPosicion()[0];
    int Y = jugador.getPosicion()[1];

    int PA = (int) getBeliefbase().getBelief("PA").getFact();

    // Conexion
    int conexion = (int) getBeliefbase().getBelief("conexionEstrategia").getFact();

    // Hacemos la petición
    AbrirPuerta accion = new AbrirPuerta();
    accion.setConexion(conexion);

    IMessageEvent peticion = createMessageEvent(Mensajes.Puerta.RequestAbrir);
    peticion.getParameterSet(SFipa.RECEIVERS).addValue(idTablero);
    peticion.setContent(accion);

    // Obtenemos respuesta
    IMessageEvent respuesta = sendMessageAndWait(peticion);

    if (respuesta.getType().equals(Mensajes.Puerta.InformAbierta)) {
      // Actualizamos habitación
      Info info = (Info) getBeliefbase().getBelief("info").getFact();

      Casilla[][] mapa = info.getHistorial(info.getTurno());

      // de la propia casilla
      int[] conex = mapa[Y][X].getConexiones();
      conex[conexion] = 1;
      mapa[Y][X].setConexiones(conex);

      // de la otra
      int[] pos = posicionEnDireccion(mapa[Y][X], conexion);

      if(casillaExisteEnMapa(mapa, pos[0], pos[1])) {
        conex = mapa[pos[0]][pos[1]].getConexiones();
        conex[conexionOpuesta(conexion)] = 1;
        mapa[Y][X].setConexiones(conex);
      }

      // Actualizamos mapa
      info.setHistorial(info.getTurno(), mapa);

      getBeliefbase().getBelief("info").setFact(info);

      // Actualizamos PA
      --PA;

      getBeliefbase().getBelief("PA").setFact(PA);

      jugador.setPuntosAccion(PA);

      getBeliefbase().getBelief("jugador").setFact(jugador);
    }
  }

  @Override
  public void passed() {
    super.passed();

    // Quitamos la strat
    getBeliefbase().getBelief("estrategia").setFact(Estrategias.Ninguna);
    getBeliefbase().getBelief("conexionEstrategia").setFact(-1);
  }

  private int[] posicionEnDireccion(Casilla c, int d) {
    int[] pos = c.getPosicion();
    int X = pos[0];
    int Y = pos[1];

    if (d == 0) {
      pos[1] = Y - 1;
    } else if (d == 1) {
      pos[0] = X + 1;
    } else if (d == 2) {
      pos[1] = Y + 1;
    } else {
      pos[0] = X - 1;
    }

    return pos;
  }

  private boolean casillaExisteEnMapa(Casilla[][] mapa, int X, int Y) {
    return Y >= 0 &&
           Y <  mapa.length &&
           X >= 0 &&
           X <  mapa[0].length;
  }

  private int conexionOpuesta(int c) {
    if (c == 0) return 2;
    else if (c == 1) return 3;
    else if (c == 2) return 0;
    else if (c == 3) return 1;
    else return -1;
  }

}
