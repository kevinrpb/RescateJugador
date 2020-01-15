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
public class ApagarFuegoPlan extends Plan {

  public void body() {

    // Cargamos info de la base
    Info info = (Info) getBeliefbase().getBelief("info").getFact();
    int PA = (int) getBeliefbase().getBelief("PA").getFact();
    int PAExt = (int) getBeliefbase().getBelief("PAExtincion").getFact();

    // Cogemos la posicion en la que se debe aplicar
    int[] posicionCasilla = (int[]) getBeliefbase().getBelief("casillaEstrategia").getFact();
    int X = posicionCasilla[0];
    int Y = posicionCasilla[1];

    // Y encontramos esa casilla
    Casilla[][] mapaActual = info.getHistorial(info.getTurno());

    Casilla casilla = mapaActual[Y][X];

    int fuego = casilla.getTieneFuego();

    // Creamos la petición
    // - 1 vez si era humo
    // - 2 veces si era fuego
    // - 1 vez si era fuego pero no tenemos suficientes PA
    while (fuego > 0) {
      // Si nos hemos quedado sin PA (solo podiamos pasar de fuego a humo)
      if (PA + PAExt < 1) { break; }

      ApagarFuego accion = new ApagarFuego();
      accion.setCasilla(casilla);

      IMessageEvent peticion = createMessageEvent(Mensajes.Fuego.RequestApagar);
      peticion.setContent(accion);

      // Mandamos y recibimos respuesta
      IMessageEvent respuesta = sendMessageAndWait(peticion);

      // Si se apagó con éxito
      if (respuesta.getType().equals(Mensajes.Fuego.InformApagado)) {
        // restamos PA
        if (PAExt > 0) --PAExt; else --PA;

        // acutalizamos fuego
        --fuego;
      }
    }

    // Actualizamos la casilla y PA
    casilla.setTieneFuego(0);
    mapaActual[Y][X] = casilla;
    info.setHistorial(info.getTurno(), mapaActual);

    getBeliefbase().getBelief("info").setFact(info);
    getBeliefbase().getBelief("PA").setFact(PA);
    getBeliefbase().getBelief("PAExtincion").setFact(PAExt);
  }

  @Override
  public void passed() {
    super.passed();

    // Quitamos la strat
    getBeliefbase().getBelief("estrategia").setFact(Estrategias.Ninguna);
    getBeliefbase().getBelief("casillaEstrategia").setFact(new int[] { -1, -1 });
  }

}
