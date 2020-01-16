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
public class DecidirEstrategiaPlan extends Plan {

  public void body() {

    Jugador jugador = (Jugador) getBeliefbase().getBelief("jugador").getFact();
    Info info = (Info) getBeliefbase().getBelief("info").getFact();

    getBeliefbase().getBelief("estrategia").setFact(Estrategias.Decidiendo);

    int[] datosEstrategia = DecidirEstrategiaEstrategia.ejecutar(this, jugador, info);

    int estrategia = datosEstrategia[0];

    int[] posicion = new int[] { -1, -1 };
    int conexion = -1;

    switch (estrategia) {
      case Estrategias.ApagarFuego:
        posicion[0] = datosEstrategia[1];
        posicion[1] = datosEstrategia[2];
        break;

      case Estrategias.Desplazarse:
        posicion[0] = datosEstrategia[1];
        posicion[1] = datosEstrategia[2];
        break;

      case Estrategias.AbrirPuerta:
        conexion = datosEstrategia[1];
        break;

      default:
        break;
    }


    System.out.println("[PLAN] El jugador con id " + jugador.getIdAgente() + " decide la estrategia " + estrategia);

    getBeliefbase().getBelief("casillaEstrategia").setFact(posicion);
    getBeliefbase().getBelief("conexionEstrategia").setFact(conexion);
    getBeliefbase().getBelief("estrategia").setFact(estrategia);
  }

}
