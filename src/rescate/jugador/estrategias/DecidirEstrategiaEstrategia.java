package rescate.jugador.estrategias;

import java.util.*;
import java.util.stream.Collectors;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

import rescate.jugador.util.*;

public class DecidirEstrategiaEstrategia {

  private static final int[] ACABAR = new int[] { Estrategias.AcabarTurno, -1, -1 };

  // Devuelve [estrategia, casillaX, casillaY]
  public static int[] ejecutar(Plan plan, Jugador jugador, Info info) {
    // Si no nos queda ningún PA, acaba el turno directamente
    if (jugador.getPuntosAccion() < 1 &&
        jugador.getPuntosAccionExtincion() < 1 &&
        jugador.getPuntosAccionMovimiento() < 1 &&
        jugador.getPuntosAccionMando() < 1) {
          return ACABAR;
    }

    // Calculamos donde está el jugador y el mapa
    int[] posicion = jugador.getPosicion();
    int X = posicion[0];
    int Y = posicion[1];

    Casilla[][] mapaActual = info.getHistorial(info.getTurno());
    Casilla casillaActual = mapaActual[Y][X];

    // Si:
    //   - no somos el conductor o la sanitaria
    //   - tenemos al menos un PA
    // lo primero que intentamos es apagar o reducir llamas (si hay)
    if (jugador.getRol() != Roles.Conductor && jugador.getRol() != Roles.Sanitaria && jugador.getPuntosAccion() + jugador.getPuntosAccionExtincion() > 1) {

      // Buscamos casillas importantes: la casilla donde estamos y colindantes
      ArrayList<Casilla> casillasImportantes = casillasColindantes(mapaActual, X, Y);

      casillasImportantes.add(casillaActual);

      // Buscamos si apagamos alguna. Prioridad:

      //   1. Casillas con humo y PDI victima
      List<Casilla> humoPDIVictima = casillasImportantes.stream()
        .filter(casilla -> {
          return casilla.getTieneFuego() == 1 &&
                (casilla.getPuntoInteres() == 2 || casilla.getPuntoInteres() == 3);
        }).collect(Collectors.toList());

      if (humoPDIVictima.size() > 0) {
        int[] p = humoPDIVictima.get(0).getPosicion();

        return new int[] { Estrategias.ApagarFuego, p[0], p[1] };
      }

      //   2. Casillas con humo y PDI oculto
      List<Casilla> humoPDIOculto = casillasImportantes.stream()
        .filter(casilla -> {
          return casilla.getTieneFuego() == 1 &&
                (casilla.getPuntoInteres() == 1);
        }).collect(Collectors.toList());

      if (humoPDIOculto.size() > 0) {
        int[] p = humoPDIOculto.get(0).getPosicion();

        return new int[] { Estrategias.ApagarFuego, p[0], p[1] };
      }

      //   3. Casillas con fuego al lado de PDI
      List<Casilla> fuegoPDI = casillasImportantes.stream()
        .filter(casilla -> {
          int[] p = casilla.getPosicion();

          if (casilla.getTieneFuego() != 2) return false;

          ArrayList<Casilla> col = casillasColindantes(mapaActual, p[0], p[1]);

          List<Casilla> pdis = col.stream()
            .filter(c -> {
              return c.getPuntoInteres() != 0;
            }).collect(Collectors.toList());

          return pdis.size() > 0;
        }).collect(Collectors.toList());

      if (fuegoPDI.size() > 0) {
        int[] p = fuegoPDI.get(0).getPosicion();

        return new int[] { Estrategias.ApagarFuego, p[0], p[1] };
      }

      //   4. Casillas con fuego, primero en la que estamos
      if (casillaActual.getTieneFuego() == 2) {
        int[] p = casillaActual.getPosicion();

        return new int[] { Estrategias.ApagarFuego, p[0], p[1] };
      }

      List<Casilla> fuego = casillasImportantes.stream()
        .filter(casilla -> {
          return casilla.getTieneFuego() == 2;
        }).collect(Collectors.toList());

      if (fuego.size() > 0) {
        int[] p = fuego.get(0).getPosicion();

        return new int[] { Estrategias.ApagarFuego, p[0], p[1] };
      }
    }

    // Si no hemos apagado fuego, y somos bombero con espuma


    // Si no hemos encontrado una ficha que apagar (o somos conductor), tenemos oras estrategias específicas
    switch (jugador.getRol()) {
      case Roles.Sanitaria:
        return decidirEstrategiaSanitaria(plan, info);
      case Roles.Jefe:
        return decidirEstrategiaJefe(plan, info);
      case Roles.Imagenes:
        return decidirEstrategiaImágenes(plan, info);
      case Roles.Espuma:
        return decidirEstrategiaEspuma(plan, info);
      case Roles.Materias:
        return decidirEstrategiaMaterias(plan, info);
      case Roles.Generalista:
        return decidirEstrategiaGeneralista(plan, info);
      case Roles.Rescates:
        return decidirEstrategiaRescates(plan, info);
      case Roles.Conductor:
        return decidirEstrategiaConductor(plan, info);
      default: // ¿No sabemos el rol...? cosa mala
        return ACABAR;
    }
  }

  private static int[] decidirEstrategiaSanitaria(Plan plan, Info info) {
    //TODO: implement

    return ACABAR;
  }

  private static int[] decidirEstrategiaJefe(Plan plan, Info info) {
    //TODO: implement

    return ACABAR;
  }

  private static int[] decidirEstrategiaImágenes(Plan plan, Info info) {
    //TODO: implement

    return ACABAR;
  }

  private static int[] decidirEstrategiaEspuma(Plan plan, Info info) {
    //TODO: implement

    return ACABAR;
  }

  private static int[] decidirEstrategiaMaterias(Plan plan, Info info) {
    //TODO: implement

    return ACABAR;
  }

  private static int[] decidirEstrategiaGeneralista(Plan plan, Info info) {
    //TODO: implement

    return ACABAR;
  }

  private static int[] decidirEstrategiaRescates(Plan plan, Info info) {
    //TODO: implement

    return ACABAR;
  }

  private static int[] decidirEstrategiaConductor(Plan plan, Info info) {
    //TODO: implement

    return ACABAR;
  }

  // Utilidad

  private static ArrayList<Casilla> casillasColindantes(Casilla[][] mapa, int X, int Y) {
    ArrayList<Casilla> casillas = new ArrayList<Casilla>();
    Casilla casilla = mapa[Y][X];

    if (casillaExisteEnMapa(mapa, X - 1, Y) &&
        casilla.esColindante(mapa[Y][X - 1])) {
          casillas.add(mapa[Y][X - 1]);
    }

    if (casillaExisteEnMapa(mapa, X + 1, Y) &&
        casilla.esColindante(mapa[Y][X + 1])) {
          casillas.add(mapa[Y][X + 1]);
    }

    if (casillaExisteEnMapa(mapa, X, Y - 1) &&
        casilla.esColindante(mapa[Y - 1][X])) {
          casillas.add(mapa[Y - 1][X]);
    }

    if (casillaExisteEnMapa(mapa, X, Y + 1) &&
        casilla.esColindante(mapa[Y + 1][X])) {
          casillas.add(mapa[Y + 1][X]);
    }

    return casillas;
  }

  private static boolean casillaExisteEnMapa(Casilla[][] mapa, int X, int Y) {
    return Y >= 0 &&
           Y <  mapa.length &&
           X >= 0 &&
           X <  mapa[0].length;
  }

}
