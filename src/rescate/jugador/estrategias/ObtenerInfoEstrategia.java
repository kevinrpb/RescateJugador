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

public class ObtenerInfoEstrategia {

  public static Info ejecutar(Plan plan, Jugador jugador, Jugador[] jugadores) {

    Info infoJugador = (Info) plan.getBeliefbase().getBelief("info").getFact();

    for (Jugador j : jugadores) {

      // Si no es el mismo jugador
      if (j.getIdAgente() != jugador.getIdAgente()) {

        System.out.println("[INFO] Pidiendo info al jugador con id " + j.getIdAgente());

        // Creamos la petición
        IMessageEvent msgInfoSend = plan.createMessageEvent(Mensajes.Info.Request);
        msgInfoSend.getParameterSet(SFipa.RECEIVERS).addValue(j.getIdAgente());

        // TODO: añadir acción ?

        // La mandamos y recibimos la info de vuelta
        IMessageEvent msgInfoReceive = plan.sendMessageAndWait(msgInfoSend);

        Info info = (Info) msgInfoReceive.getContent();

        // Juntamos ambas informaciones
        infoJugador = juntarInfo(infoJugador, info);
      }
    }

    return infoJugador;

  }

  private static Info juntarInfo(Info principal, Info anadida) {
    Info nuevaInfo = principal;

    for (int i = 1; i < principal.getTurno(); i++) {
      Casilla[][] tableroBase = principal.getMapaBase();
      Casilla[][] tableroPrincipal = principal.getHistorial(i);
      Casilla[][] tableroAnadida = anadida.getHistorial(i);

      Casilla[][] nuevoTablero = tableroPrincipal;

      // Si ambos tienen información para el turno
      if (tableroPrincipal != null && tableroAnadida != null) {
        // Recorremos todas las casillas
        for (int Y = 0; Y < tableroBase.length; Y++) {
          for (int X = 0; X < tableroBase[0].length; X++) {

            Casilla casillaBase = tableroBase[Y][X];
            Casilla casillaPrincipal = tableroPrincipal[Y][X];
            Casilla casillaAnadida = tableroAnadida[Y][X];

            // si la anadida no es igual a la base, pero la principal sí
            if (casillasSonIguales(casillaPrincipal, casillaBase) && ! casillasSonIguales(casillaAnadida, casillaBase)) {
              nuevoTablero[Y][X] = tableroAnadida[Y][X];
            }
          }
        }

        nuevaInfo.setHistorial(i, nuevoTablero);
      }
    }

    return nuevaInfo;
  }

  private static boolean casillasSonIguales(Casilla casilla1, Casilla casilla2) {
    return casilla1.mismaPosicion(casilla2) &&
        (casilla1.getTieneFuego() == casilla2.getTieneFuego()) &&
        (casilla1.getTieneMateriaPeligrosa() == casilla2.getTieneMateriaPeligrosa()) &&
        (casilla1.getTieneFocoCalor() == casilla2.getTieneFocoCalor()) &&
        (casilla1.getPuntoInteres() == casilla2.getPuntoInteres()) &&
        (casilla1.getFlecha() == casilla2.getFlecha()) &&
        (casilla1.getCamionBomberos() == casilla2.getCamionBomberos()) &&
        (casilla1.getAmbulancia() == casilla2.getAmbulancia()) &&
        (casilla1.getEsAparcamientoCamion() == casilla2.getEsAparcamientoCamion()) &&
        (casilla1.getEsAparcamientoAmbulancia() == casilla2.getEsAparcamientoAmbulancia()) &&
        (casilla1.getHabitacion() == casilla2.getHabitacion());
  }

}
