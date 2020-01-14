package rescate.tablero.planes;

import java.util.*;

import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.conceptos.*;

public class ColocarPuntosInteresPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero trata de colocar un PDI");

    // PDI en el tablero
    int PDITablero = (int) getBeliefbase().getBelief("PDITablero").getFact();
    // PDI tipo victima sin colocar
    int PDIVictima = (int) getBeliefbase().getBelief("PDIVictima").getFact();
    // PDI tipo falsa alarma sin colocar
    int PDIFalsaAlarma = (int) getBeliefbase().getBelief("PDIFalsaAlarma").getFact();

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();
    // Mapa
    Casilla[][] mapa = t.getMapa();
    // Lista de jugadores
    ArrayList<Jugador> jugadores = t.getJugadores();

    // Si no quedan fichas por colocar
    if (PDIVictima + PDIFalsaAlarma == 0) {
      System.out.println("[ERROR] No quedan PDIs por colocar");
      return;
    }

    // Posiciones aleatorias para el nuevo PDI en el tablero
    int X = (int) (Math.random() * 8 + 1);
    int Y = (int) (Math.random() * 6 + 1);
    // Casilla en la posicion X e Y
    Casilla c = mapa[Y][X];

    // Se evitan posibles bucles infinitos (por las flechas)
    int maxIntentos = 11;
    int intentos = 0;

    // Hasta que se coloque el PDI
    while (true) {

      // Nuevo intento
      intentos++;

      // Si se ha entrado en un bucle infinito
      if (intentos > maxIntentos) {
        for (int i = 1; i < mapa.length - 1; i++) {
          for (int j = 1; j < mapa[i].length - 1; j++) {
            // Se encuentra la primera casilla en la que sea viable poner el PDI
            Casilla c_ = mapa[i][j];
            if (c_.getPuntoInteres() == 0 && c_.getTieneFuego() != 2 && !hayBombero(j, i, jugadores)) {
              // Se coloca el PDI (oculto y cuando se descubra se decidirá si es falsa alarma o víctima)
              System.out.println("[INFO] Se ha colocado un PDI en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
              c.setPuntoInteres(1);

              // Se actualiza la vista
              ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
              viewUpdater.updateTablero(t);
              getBeliefbase().getBelief("view").setFact(viewUpdater);
              // Una vez colocado el nuevo PDI, se actualizan las creencias
              getBeliefbase().getBelief("PDITablero").setFact(PDITablero + 1);
              getBeliefbase().getBelief("tablero").setFact(t);
              getBeliefbase().getBelief("finTurno").setFact(false);
              return;
            }
          }
        }
      }

      // Se puede colocar...
      if (c.getPuntoInteres() == 0 && c.getTieneFuego() != 2 && !hayBombero(X, Y, jugadores)) {

        // Se coloca el PDI (oculto y cuando se descubra se decidirá si es falsa alarma o víctima)
        System.out.println("[INFO] Se ha colocado un PDI en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        c.setPuntoInteres(1);
        break;

      }

      // No se puede colocar, se siguen las flechas para encontrar una nueva posible casilla
      else {
        // La nueva casilla es la indica por la flecha de la casilla actual
        switch (c.getFlecha()) {
          case 0:
            Y--;
            break;
          case 1:
            X++;
            Y--;
            break;
          case 2:
            X++;
            break;
          case 3:
            X++;
            Y++;
            break;
          case 4:
            Y++;
            break;
          case 5:
            X--;
            Y++;
            break;
          case 6:
            X--;
            break;
          case 7:
            X--;
            Y--;
            break;
          case 8:
            break;
        }
        // Se actualiza la casilla
        c = mapa[Y][X];
      }

    }

    // Se actualiza la vista
    ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
    viewUpdater.updateTablero(t);
    getBeliefbase().getBelief("view").setFact(viewUpdater);
    // Una vez colocado el nuevo PDI, se actualizan las creencias
		getBeliefbase().getBelief("PDITablero").setFact(PDITablero + 1);
    getBeliefbase().getBelief("tablero").setFact(t);
    getBeliefbase().getBelief("finTurno").setFact(false);
    //getBeliefbase().getBelief("siguienteTurno").setFact(true);
    
  }

  private boolean hayBombero(int X, int Y, ArrayList<Jugador> jugadores) {
    for (Jugador j : jugadores) {
      if (j.getPosicion()[0] == X && j.getPosicion()[1] == Y) {
        return true;
      }
    }
    return false;
  }

}
