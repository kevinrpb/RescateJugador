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

    // Calculamos donde está el jugador, el rol y el mapa
    int[] posicion = jugador.getPosicion();
    int X = posicion[0];
    int Y = posicion[1];

    int rol = jugador.getRol();

    Casilla[][] mapaActual = info.getHistorial(info.getTurno());
    Casilla casillaActual = mapaActual[Y][X];

    // Si estamos en la habitación 0 (fuera)
    if (jugador.getHabitacion() == 0) {
      // Si llevamos una victima la dejamos
      if (jugador.getLlevandoVictima() > 0) {
        return new int[] { Estrategias.DejarVictima };
      } else if (jugador.getPuntosAccion() + jugador.getPuntosAccionMovimiento() > 0) {

        // Sino buscamos una puerta cercana en la habitación
        int[] conex = casillaActual.getConexiones();

        for (int i = 0; i < 4; i++) {
          int d = conex[i];

          if (d == 1) { // puerta abierta
            int[] pos = posicionEnDireccion(casillaActual, i);

            return new int[] { Estrategias.Desplazarse, pos[0], pos[1] };
          }
        }

        // sino nos movemos a la colindante que tenga puerta
        ArrayList<Casilla> colin = casillasColindantes(mapaActual, X, Y);

        for (Casilla c : colin) {
          conex = c.getConexiones();

          for (int i = 0; i < 4; i++) {
            int d = conex[i];

            if (d == 1) {
              int[] pos = c.getPosicion();

              return new int[] { Estrategias.Desplazarse, pos[0], pos[1] };
            }
          }
        }
      }
    }

    // Si estamos trasladando una victima, intentamos ir a fuera
    if (jugador.getLlevandoVictima() > 0) {
      int hab = jugador.getHabitacion();
      int[] dest = new int[] { X, Y };

      switch (hab) {
        case 1:
          if (X > 1) {
            dest[0] = X - 1;
          } else if (Y < 3) {
            dest[1] = Y + 1;
          } else if (Y > 3) {
            dest[1] = Y - 1;
          } else {
            dest[0] = X - 1;
          }

          break;

        case 2:
          if (Y == 1) {
            dest[1] = Y + 1;
          } else {
            dest[0] = X + 1;
          }
          break;

        case 3:
          if (X > 6) {
            dest[0] = X - 1;
          } else {
            dest[1] = Y - 1;
          }

          break;

        case 4:
          if (X == 4) {
            dest[0] = X - 1;
          } else if (X == 5) {
            dest[0] = X + 1;
          } else if (X == 3) {
            if (Y == 4) {
              dest[1] = Y - 1;
            } else {
              dest[0] = X - 1;
            }
          } else {
            if (Y == 4) {
              dest[0] = X + 1;
            } else {
              dest[1] = Y + 1;
            }
          }
          break;

        case 5:
          if (Y == 3) { // tenemos que bajar
            dest[1] = Y + 1;
          } else {
            dest[0] = X + 1; // vamos a derecha
          }
          break;

        case 6:
          if (Y == 5) { // tenemos que bajar
            dest[1] = Y + 1;
          } else if (X > 3) { // vamos a izq
            dest[0] = X - 1;
          } else if (X < 3) {
            dest[0] = X + 1;
          } else {
            dest[1] = Y + 1;
          }
          break;

        case 7:
          if (Y == 5) { // tenemos que bajar
            dest[1] = Y + 1;
          } else { // vamos a izq
            dest[0] = X - 1;
          }
          break;

        case 8:
          if (Y == 5) { // tenemos que bajar
            dest[1] = Y + 1;
          } else { // vamos a izq
            dest[0] = X - 1;
          }
          break;
      }

      if (dest[0] != X || dest[1] != Y) {
        return new int[] { Estrategias.Desplazarse, dest[0], dest[1] };
      }

    }

    // Si estamos en una casilla con victima, la trasladamos
    if (casillaActual.getPuntoInteres() > 1) {
      return new int[] { Estrategias.CogerVictima };
    }

    // Si:
    //   - no somos el conductor o la sanitaria
    //   - tenemos al menos un PA
    // lo primero que intentamos es apagar o reducir llamas (si hay)
    if (rol != Roles.Conductor &&
        rol != Roles.Sanitaria &&
        jugador.getPuntosAccion() + jugador.getPuntosAccionExtincion() > 1) {

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

        for (Casilla c : fuego) {
          if (casillasColindantes(mapaActual, X, Y).contains(c) &&
              puedeIrDeCasillaACasilla(casillaActual, c)) {
                int[] p = c.getPosicion();

                return new int[] { Estrategias.ApagarFuego, p[0], p[1] };
          }
        }
    }

    // Si tenemos una puerta cerrada en nuestra casilla la abrimos
    if (jugador.getPuntosAccion() > 0) {
      int[] conexiones = casillaActual.getConexiones();

      for (int i = 0; i < 4; i++) {
        int d = conexiones[i];

        if (d == 2) { // puerta cerrada
          return new int[] { Estrategias.AbrirPuerta, i };
        }
      }
    }

    // Si no hemos apagado fuego, y
    //   - somos bombero con espuma o generalista
    // tratamos de andar en la dirección de un fuego tengamos en los dos últimos turnos o hacia el centro del edificio
    if (jugador.getPuntosAccion() + jugador.getPuntosAccionMovimiento() > 0) {
      ArrayList<Casilla> fuegos = new ArrayList<Casilla>();
      ArrayList<Casilla> humos = new ArrayList<Casilla>();

      // Este turno
      for (Casilla[] fila : mapaActual) {
        for (Casilla casilla : fila) {
          if (casilla.getTieneFuego() == 2) fuegos.add(casilla);
          if (casilla.getTieneFuego() == 1) humos.add(casilla);
        }
      }

      // Turno anterior
      if (info.getTurno() > 1) {
        for (Casilla[] fila : info.getHistorial(info.getTurno() - 1)) {
          for (Casilla casilla : fila) {
            if (casilla.getTieneFuego() == 2) fuegos.add(casilla);
            if (casilla.getTieneFuego() == 1) humos.add(casilla);
          }
        }
      }

      Casilla destino = null;
      int distancia = 1000;

      // buscamos el fuego más cercano
      for (Casilla casilla : fuegos) {
        int d = distanciaCasillas(casillaActual, casilla);

        if (d < distancia) {
          distancia = d;
          destino = casilla;
        }
      }

      // si no tenemos ningun fuego buscamos en humos
      if (destino == null) {
        for (Casilla casilla : fuegos) {
          int d = distanciaCasillas(casillaActual, casilla);

          if (d < distancia) {
            distancia = d;
            destino = casilla;
          }
        }
      }

      // Vemos en qué dirección tendríamos que ir
      if (destino != null) {
        int d = direccionDeCasillaACasilla(casillaActual, destino);
        int[] pos = posicionEnDireccion(casillaActual, d);

        destino = mapaActual[pos[1]][pos[0]];

        // Si en esa dirección no puede moverse, probamos otra cosa
        if (!puedeIrDeCasillaACasilla(casillaActual, destino)) {
          destino = null;
        }
      }

      // Si tampoco teniamos humo, tratamos de ir hacia el centro del edificio

      // Sino
      double midX = 4.5;
      double midY = 3.5;

      if (destino == null) {
        if (Y < midY) {
          if (X < midX) {
            // objetivo x+1 y+1
            int conex1 = casillaActual.getConexiones()[1];
            int conex2 = casillaActual.getConexiones()[2];

            if (conex1 == 0 || conex1 == 1 || conex1 == 5) {
              destino = mapaActual[Y][X + 1];
            } else if (conex2 == 0 || conex2 == 1 || conex2 == 5) {
              destino = mapaActual[Y + 1][X];
            }
          } else {
            //objetivo x-1 y+1
            int conex1 = casillaActual.getConexiones()[3];
            int conex2 = casillaActual.getConexiones()[2];

            if (conex1 == 0 || conex1 == 1 || conex1 == 5) {
              destino = mapaActual[Y][X - 1];
            } else if (conex2 == 0 || conex2 == 1 || conex2 == 5) {
              destino = mapaActual[Y + 1][X];
            }
          }
        } else {
          if (X < midX) {
            //objetivo x+1 y-1
            int conex1 = casillaActual.getConexiones()[1];
            int conex2 = casillaActual.getConexiones()[0];

            if (conex1 == 0 || conex1 == 1 || conex1 == 5) {
              destino = mapaActual[Y][X + 1];
            } else if (conex2 == 0 || conex2 == 1 || conex2 == 5) {
              destino = mapaActual[Y - 1][X];
            }
          } else {
            //objetivo x-1 y-1
            int conex1 = casillaActual.getConexiones()[3];
            int conex2 = casillaActual.getConexiones()[0];

            if (conex1 == 0 || conex1 == 1 || conex1 == 5) {
              destino = mapaActual[Y][X - 1];
            } else if (conex2 == 0 || conex2 == 1 || conex2 == 5) {
              destino = mapaActual[Y - 1][X];
            }
          }
        }
      }

      // Si no podemos movernos en una de estas direcciones, miramos la primera direccion que sea valida
      if (destino == null) {
        int[] conex = casillaActual.getConexiones();

        // Intentamos a derecha
        if (X + 1 < mapaActual[0].length &&
           (conex[1] == 0 || conex[1] == 1 || conex[1] == 5)) {
            destino = mapaActual[Y][X + 1];
        } else
        // Intentamos a izquierda
        if (X - 1 > 0 &&
           (conex[3] == 0 || conex[3] == 1 || conex[3] == 5)) {
            destino = mapaActual[Y][X - 1];
        } else
        // Intentamos arriba
        if (Y - 1 > 0 &&
           (conex[0] == 0 || conex[0] == 1 || conex[0] == 5)) {
            destino = mapaActual[Y - 1][X];
        } else
        // Intentamos arriba
        if (Y + 1 < mapaActual.length&
           (conex[2] == 0 || conex[2] == 1 || conex[2] == 5)) {
            destino = mapaActual[Y + 1][X];
        }
      }

      // Deberíamos llegar aquí con algun destino pero porsi
      if (destino != null) {
        int destX = destino.getPosicion()[0];
        int destY = destino.getPosicion()[1];

        return new int[] { Estrategias.Desplazarse, destX, destY };
      }

    }

    // Si hemos llegado aquí sin hacer nada, miramos si tenemos una víctima cerca
    ArrayList<Casilla> posiblesPDIVictima = casillasColindantes(mapaActual, X, Y);
    posiblesPDIVictima.add(casillaActual);

    List<Casilla> PDIVictima = posiblesPDIVictima.stream()
      .filter(casilla -> {
        return casilla.getHabitacion() > 0 && casilla.getPuntoInteres() > 1;
      }).collect(Collectors.toList());

    // Si no tenemos ninguno, intentamos buscar en el turno anterior
    if (PDIVictima.size() < 1 && info.getTurno() > 1) {
      Casilla[][] anterior = info.getHistorial(info.getTurno() - 1);
      for (Casilla[] fila : anterior) {
        for (Casilla c : fila) {
          if (c.getPuntoInteres() > 1) {
            // Miramos como ir hasta allí
            int d = direccionDeCasillaACasilla(casillaActual, c);
            int[] pos = posicionEnDireccion(casillaActual, d);
            if (casillaExisteEnMapa(mapaActual, pos[0], pos[1])) {
              Casilla des = mapaActual[pos[1]][pos[0]];

              if (des.getHabitacion() > 0)
                PDIVictima.add(des);
            }
          }
        }
      }
    }

    // Nos quedamos con aquellas a las que podamos ir
    PDIVictima = PDIVictima.stream()
      .filter(casilla -> {
        return puedeIrDeCasillaACasilla(casillaActual, casilla);
      }).collect(Collectors.toList());

    // si tenemos alguno nos movemos
    if (PDIVictima.size() > 0) {
      int[] pos = PDIVictima.get(0).getPosicion();

      return new int[] { Estrategias.Desplazarse, pos[0], pos[1] };
    }

    // Si no tenemos nada más que hacer, acabamos
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

  private static int direccionDeCasillaACasilla(Casilla c1, Casilla c2) {
    int[] pos1 = c1.getPosicion();
    int[] pos2 = c2.getPosicion();

    if (pos1 == pos2) return -1;

    int x1 = pos1[0];
    int y1 = pos1[1];
    int x2 = pos2[0];
    int y2 = pos2[1];

    if (y1 > y2) {
      return 0;
    } else if (x1 < x2) {
      return 1;
    } else if (y1 < y2) {
      return 2;
    } else if (x1 > x2) {
      return 3;
    }

    return -1;
  }

  private static int[] posicionEnDireccion(Casilla c, int d) {
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

  private static boolean puedeIrDeCasillaACasilla(Casilla c1, Casilla c2) {
    int d = direccionDeCasillaACasilla(c1, c2);

    if (d == -1) return false;

    int c = c1.getConexiones()[d];

    return c == 0 || c == 1 || c == 5;
  }

  private static int distanciaCasillas(Casilla c1, Casilla c2) {
    int[][] distancias = new int[][] {
      /*desde\          hasta*/
            /* 0, 1, 2, 3, 4, 5, 6, 7, 8 */
      /*0*/  { 0, 1, 2, 1, 2, 1, 1, 2, 3},
      /*1*/  { 0, 0, 1, 2, 1, 2, 2, 3, 4},
      /*2*/  { 0, 0, 0, 1, 2, 3, 3, 4, 5},
      /*3*/  { 0, 0, 0, 0, 2, 1, 3, 4, 5},
      /*4*/  { 0, 0, 0, 0, 0, 1, 1, 2, 3},
      /*5*/  { 0, 0, 0, 0, 0, 0, 2, 3, 4},
      /*6*/  { 0, 0, 0, 0, 0, 0, 0, 1, 2},
      /*7*/  { 0, 0, 0, 0, 0, 0, 0, 0, 1},
      /*8*/  { 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    int h1 = c1.getHabitacion();
    int h2 = c2.getHabitacion();

    int distancia = distancias[h1][h2];

    if (distancia == 0) distancia = distancias[h2][h1];

    return distancia;
  }

}