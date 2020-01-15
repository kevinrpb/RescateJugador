package rescate.jugador.estrategias;

import java.util.ArrayList;
import java.util.HashMap;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

import rescate.jugador.util.*;

public class ActualizarHabitacionEstrategia {

  public static Info ejecutar(Plan plan, Info info, ArrayList<Casilla> casillas, boolean turnoNuevo) {
    Info infoJugador = info;

    int turno = infoJugador.getTurno();

    // Si estamos en un turno nuevo, primero añadimos una entrada al mapa
    if (turnoNuevo) {
      turno++;

      Casilla[][] base = infoJugador.getMapaBase();
      Casilla[][] copia = new Casilla[base.length][base[0].length];

      for (int i = 0; i < base.length; i++) {
        for (int j = 0; j < base[0].length; j++) {
          copia[i][j] = base[i][j]; // No sé si esto está bien lol
        }
      }

      infoJugador.setHistorial(turno, copia);
    }

    // Actualizamos las casillas
    Casilla[][] nuevas = infoJugador.getHistorial(turno);

    for (Casilla c : casillas) {
      int X = c.getPosicion()[0];
      int Y = c.getPosicion()[1];

      nuevas[Y][X] = c;
    }

    infoJugador.setHistorial(turno, nuevas);

    return infoJugador;
  }

}
