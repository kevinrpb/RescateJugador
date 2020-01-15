package rescate.jugador.estrategias;

import java.util.ArrayList;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

import rescate.jugador.util.*;

public class DecidirEstrategiaEstrategia {

  public static int ejecutar(Plan plan, Jugador jugador) {
    switch (jugador.getRol()) {
      case 1:
        return decidirEstrategiaSanitaria(plan);
      case 2:
        return decidirEstrategiaJefe(plan);
      case 3:
        return decidirEstrategiaImágenes(plan);
      case 4:
        return decidirEstrategiaEspuma(plan);
      case 5:
        return decidirEstrategiaMaterias(plan);
      case 6:
        return decidirEstrategiaGeneralista(plan);
      case 7:
        return decidirEstrategiaRescates(plan);
      case 8:
        return decidirEstrategiaConductor(plan);
      default:
        return Estrategias.AcabarTurno;
    }
  }

  private static int decidirEstrategiaSanitaria(Plan plan) {
    //TODO: implement

    return Estrategias.AcabarTurno;
  }

  private static int decidirEstrategiaJefe(Plan plan) {
    //TODO: implement

    return Estrategias.AcabarTurno;
  }

  private static int decidirEstrategiaImágenes(Plan plan) {
    //TODO: implement

    return Estrategias.AcabarTurno;
  }

  private static int decidirEstrategiaEspuma(Plan plan) {
    //TODO: implement

    return Estrategias.AcabarTurno;
  }

  private static int decidirEstrategiaMaterias(Plan plan) {
    //TODO: implement

    return Estrategias.AcabarTurno;
  }

  private static int decidirEstrategiaGeneralista(Plan plan) {
    //TODO: implement

    return Estrategias.AcabarTurno;
  }

  private static int decidirEstrategiaRescates(Plan plan) {
    //TODO: implement

    return Estrategias.AcabarTurno;
  }

  private static int decidirEstrategiaConductor(Plan plan) {
    //TODO: implement

    return Estrategias.AcabarTurno;
  }

}
