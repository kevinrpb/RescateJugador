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
public class DesplazarsePlan extends Plan {

  public void body() {

    AgentIdentifier idTablero = (AgentIdentifier) getBeliefbase().getBelief("tablero").getFact();

    // Cargamos la info del jugador
    Jugador jugador = (Jugador) getBeliefbase().getBelief("jugador").getFact();
    int rol = jugador.getRol();
    int jX = jugador.getPosicion()[0];
    int jY = jugador.getPosicion()[1];

    int PA = (int) getBeliefbase().getBelief("PA").getFact();
    int PAMov = (int) getBeliefbase().getBelief("PAMovimiento").getFact();

    // Cogemos la posicion en la que se debe aplicar
    int[] posicionCasilla = (int[]) getBeliefbase().getBelief("casillaEstrategia").getFact();
    int X = posicionCasilla[0];
    int Y = posicionCasilla[1];

    // Calculamos la dirección
    int direccion;

    if (jX < X) { // derecha
      direccion = 1;
    } else if (jX > X) { // izq
      direccion = 3;
    } else if (jY < Y) { // abajo
      direccion = 2;
    } else { // arriba
      direccion = 0;
    }

    // Hacemos la petición
    Desplazar accion = new Desplazar();
    accion.setDireccion(direccion);

    IMessageEvent peticion = createMessageEvent(Mensajes.Desplazar.Request);
    peticion.getParameterSet(SFipa.RECEIVERS).addValue(idTablero);
    peticion.setContent(accion);

    // Obtenemos respuesta
    IMessageEvent respuesta = sendMessageAndWait(peticion);

    if (respuesta.getType().equals(Mensajes.Desplazar.Inform)) {
      Desplazado desplazado = (Desplazado) respuesta.getContent();

      // Actualizamos jugadores
      ArrayList<Jugador> jugadoresList = desplazado.getJugadores();
      Jugador[] jugadores = jugadoresList.toArray(new Jugador[jugadoresList.size()]);

      getBeliefbase().getBeliefSet("jugadoresHabitacion").removeFacts();
      getBeliefbase().getBeliefSet("jugadoresHabitacion").addFacts(jugadores);

      // Actualizamos habitación
      Info info = (Info) getBeliefbase().getBelief("info").getFact();

      ArrayList<Casilla> casillas = desplazado.getHabitacion();
      Info nuevaInfo = ActualizarHabitacionEstrategia.ejecutar(this, info, casillas, false);

      getBeliefbase().getBelief("info").setFact(nuevaInfo);

      // Actualizamos posicion
      jugador.setPosicion(new int[] { X, Y });
      jugador.setHabitacion(casillas.get(0).getHabitacion());

      //
      Casilla casilla = info.getHistorial(info.getTurno())[Y][X];

      int dif = 1;

      if (casilla.getTieneFuego() == 2 ||
          jugador.getLlevandoVictima() == 1 ||
          jugador.getLlevandoMateriaPeligrosa()) {
        dif = 2;
      }

      // Actualizamos PA
      while (dif > 0) {
        if (PAMov > 0) {
          --PAMov;
        } else {
          --PA;
        }

        --dif;
      }

      getBeliefbase().getBelief("PA").setFact(PA);
      getBeliefbase().getBelief("PAMovimiento").setFact(PAMov);

      jugador.setPuntosAccion(PA);
      jugador.setPuntosAccionMovimiento(PAMov);

      getBeliefbase().getBelief("jugador").setFact(jugador);
    } else {
      getBeliefbase().getBelief("PA").setFact(0);
      getBeliefbase().getBelief("PAMovimiento").setFact(0);

      jugador.setPuntosAccion(0);
      jugador.setPuntosAccionMovimiento(0);

      getBeliefbase().getBelief("jugador").setFact(jugador);
    }
  }

  @Override
  public void passed() {
    super.passed();

    // Quitamos la strat
    getBeliefbase().getBelief("estrategia").setFact(Estrategias.Ninguna);
    getBeliefbase().getBelief("casillaEstrategia").setFact(new int[] { -1, -1 });
  }

}
