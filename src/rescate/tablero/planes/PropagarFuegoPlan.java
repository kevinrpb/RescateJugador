package rescate.tablero.planes;

import rescate.ontologia.conceptos.Casilla;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import rescate.gui.ViewUpdater;
import rescate.ontologia.conceptos.*;

public class PropagarFuegoPlan extends Plan {

  private int propagaciones = 1;
  private Casilla[][] mapa;

  @Override
  public void body() {
    
    System.out.println("[PLAN] El tablero propaga el incendio");

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();
    // Mapa
    mapa = t.getMapa();

    do {
      propagaciones--;
      // Propagar el incendio hasta que se propague en una casilla sin foco de calor
      propagar();
    } while (propagaciones > 0);

    // Humos adyacentes a fuego -> Fuego
    for (int i = 0; i < mapa.length; i++) {
      for (int j = 0; j < mapa[i].length; j++) {
        expandirFuego(j, i);
      }
    }

    // Materias peligrosas en casilla con fuego -> Explosion
    for (int i = 0; i < mapa.length; i++) {
      for (int j = 0; j < mapa[i].length; j++) {
        explosionMatPeligrosa(j, i);
      }
    }

    // Para cada jugador
    for (int i = 0; i < t.getJugadores().size(); i++) {
      // Si esta en una casilla con fuego
      if (mapa[t.getJugadores().get(i).getPosicion()[1]][t.getJugadores().get(i).getPosicion()[0]].getTieneFuego() == 2) {
        // Si esta llevando una victima
        if (t.getJugadores().get(i).getLlevandoVictima() != 0) {
          // Ya no llevas la victima
          t.getJugadores().get(i).setLlevandoVictima(0);
          // Se actualizan los hechos
          getBeliefbase().getBelief("PDITablero").setFact((int) getBeliefbase().getBelief("PDITablero").getFact() - 1);
          getBeliefbase().getBelief("victimas").setFact((int) getBeliefbase().getBelief("victimas").getFact() + 1);
        }
        // Se mueve al bombero a la casilla en la que está la ambulancia
        System.out.println("[INFO] bombero"+i+" ha sido trasladado a la ambulancia");
        if (mapa[3][0].getAmbulancia()) {
          t.getJugadores().get(i).setPosicion(new int[] {0, 3});
        } else if (mapa[0][5].getAmbulancia()) {
          t.getJugadores().get(i).setPosicion(new int[] {5, 0});
        } else if (mapa[3][9].getAmbulancia()) {
          t.getJugadores().get(i).setPosicion(new int[] {9, 3});
        } else {
          t.getJugadores().get(i).setPosicion(new int[] {3, 7});
        }
        t.getJugadores().get(i).setHabitacion(0);
      }
    }

    // Para cada PDI
    for (int i = 0; i < mapa.length; i++) {
      for (int j = 0; j < mapa[i].length; j++) {
        // Si hay fuego
        if (mapa[i][j].getTieneFuego() == 2) {
          // Dependiendo del tipo de punto de interes
          switch(mapa[i][j].getPuntoInteres()) {
            case 0:
              break;
            case 1:
              // Se obtienen los valores de los hechos
              int PDITablero = (int) getBeliefbase().getBelief("PDITablero").getFact();
              int PDIVictima = (int) getBeliefbase().getBelief("PDIVictima").getFact();
              int PDIFalsaAlarma = (int) getBeliefbase().getBelief("PDIFalsaAlarma").getFact();
              // Si no queda de un tipo, se coloca del otro...
              if (PDIVictima == 0) {
                mapa[i][j].setPuntoInteres(0);
                PDIFalsaAlarma--;
              } else if (PDIFalsaAlarma == 0) {
                mapa[i][j].setPuntoInteres(0);
                PDIVictima--;
                getBeliefbase().getBelief("victimas").setFact((int) getBeliefbase().getBelief("victimas").getFact() + 1);
              }
              // Si quedan de los dos tipos, de manera aleatoria...
              else if (Math.random() < 0.5) {
                mapa[i][j].setPuntoInteres(0);
                PDIFalsaAlarma--;
              } else {
                mapa[i][j].setPuntoInteres(0);
                PDIVictima--;
                getBeliefbase().getBelief("victimas").setFact((int) getBeliefbase().getBelief("victimas").getFact() + 1);
              }
              PDITablero--;
              // Se actualizan los hechos
              getBeliefbase().getBelief("PDITablero").setFact(PDITablero);
              getBeliefbase().getBelief("PDIVictima").setFact(PDIVictima);
              getBeliefbase().getBelief("PDIFalsaAlarma").setFact(PDIFalsaAlarma);
              break;
            case 2:
            case 3:
              mapa[i][j].setPuntoInteres(0);
              getBeliefbase().getBelief("PDITablero").setFact((int) getBeliefbase().getBelief("PDITablero").getFact() - 1);
              getBeliefbase().getBelief("victimas").setFact((int) getBeliefbase().getBelief("victimas").getFact() + 1);
              break;
          }
        }
      }
    }
    
    // Se actualiza la vista
    ViewUpdater viewUpdater = (ViewUpdater) getBeliefbase().getBelief("view").getFact();
    viewUpdater.updateTablero(t);
    getBeliefbase().getBelief("view").setFact(viewUpdater);
    // Guardar tablero
    getBeliefbase().getBelief("tablero").setFact(t);
    getBeliefbase().getBelief("propagarFuego").setFact(false);

    while (((int) getBeliefbase().getBelief("PDITablero").getFact()) < 3) {
      IGoal goal = createGoal("colocarPDIMeta");
      dispatchSubgoalAndWait(goal);
    }

    getBeliefbase().getBelief("siguienteTurno").setFact(true);
    //getBeliefbase().getBelief("finTurno").setFact(true);

  }

  private void expandirFuego(int X, int Y) {
    // Si la casilla tiene humo
    if (Y >= 0 && Y < mapa.length && X >= 0 && X < mapa[Y].length && mapa[Y][X].getTieneFuego() == 1) {
      // Tiene fuego adyacente
      if (fuegoAdyacente(X, Y)) {
        // Se cambia a fuego
        mapa[Y][X].setTieneFuego(2);
        // Se comprueban adyacentes
        expandirFuego(X, Y - 1);
        expandirFuego(X + 1, Y);
        expandirFuego(X, Y + 1);
        expandirFuego(X - 1, Y);
      }
    }
  }

  private void explosionMatPeligrosa(int X, int Y) {
    // Si la casilla tiene fuego
    if (mapa[Y][X].getTieneFuego() == 2) {
      // Y materia peligrosa
      if (mapa[Y][X].getTieneMateriaPeligrosa()) {
        // Explosion en la casilla
        explosion(X, Y);
        // Materia peligrosa -> Foco calor
        mapa[Y][X].setTieneMateriaPeligrosa(false);
        mapa[Y][X].setTieneFocoCalor(true);
        // Se comprueban adyacentes
        explosionMatPeligrosa(X, Y - 1);
        explosionMatPeligrosa(X + 1, Y);
        explosionMatPeligrosa(X, Y + 1);
        explosionMatPeligrosa(X - 1, Y);
      }
    }
  }

  // Devuelve si hay fuego arriba, derecha, abajo o izquierda de una casilla[X, Y]
  private boolean fuegoAdyacente(int X, int Y) {
    return (Y - 1 > -1 && mapa[Y - 1][X].getTieneFuego() == 2 && !obstaculo(X, Y, 0))
        || (X + 1 < mapa[0].length && mapa[Y][X + 1].getTieneFuego() == 2 && !obstaculo(X, Y, 1))
        || (Y + 1 < mapa.length && mapa[Y + 1][X].getTieneFuego() == 2 && !obstaculo(X, Y, 2))
        || (X - 1 > -1 && mapa[Y][X - 1].getTieneFuego() == 2 && !obstaculo(X, Y, 3));
  }

  // Devuelve si hay un obstaculo (pared sin romper o puerta cerrada) en la direccion indicada de una casilla
  private boolean obstaculo(int X, int Y, int direccion) {
    return mapa[Y][X].getConexiones()[direccion] == 2
        || mapa[Y][X].getConexiones()[direccion] == 3
        || mapa[Y][X].getConexiones()[direccion] == 4;
  }

  // Propaga el incendio en una casilla aleatoria
  private void propagar() {
    // Posicion aleatoria
    int X = (int) (Math.random() * 8 + 1);
    int Y = (int) (Math.random() * 6 + 1);
    // Casilla en la posicion
    Casilla c = mapa[Y][X];

    // Si hay un foco de calor, se propagará de nuevo
    if (c.getTieneFocoCalor()) {
      propagaciones++;
    }

    // Dependiendo del estado de la casilla
    switch (c.getTieneFuego()) {
      // Nada -> Humo
      case 0:
        c.setTieneFuego(1);
        break;
      // Humo -> Fuego
      case 1:
        c.setTieneFuego(2);
        break;
      // Fuego -> Explosion
      case 2:
        explosion(X, Y);
        break;
    }
  }

  // Explosion arriba, derecha, abajo e izquierda dada una casilla[X, Y]
  public void explosion(int X, int Y) {
    explosion(X, Y, 0);
    explosion(X, Y, 1);
    explosion(X, Y, 2);
    explosion(X, Y, 3);
  }

  // Explosion en una direccion desde una casilla[X, Y]
  private void explosion(int X, int Y, int direccion) {

    // Posicion nueva casilla
    int X_ = X;
    int Y_ = Y;
    // Direccion opuesta para dañar conexion
    int direccion_ = 0;

    // Dependiendo de la direccion
    switch (direccion) {
      case 0:
        Y_ = Y - 1;
        direccion_ = 2;
        break;
      case 1:
        X_ = X + 1;
        direccion_ = 3;
        break;
      case 2:
        Y_ = Y + 1;
        direccion_ = 0;
        break;
      case 3:
        X_ = X - 1;
        direccion_ = 1;
        break;
    }

    // Si la casilla esta dentro de los limites
    if (Y > -1 && X > -1 && Y < mapa.length && X < mapa[0].length) {
      // Si hay obstaculo, se daña y se para
      if (obstaculo(X, Y, direccion)) {
        // Si es una pared
        if(mapa[Y][X].getConexiones()[direccion] != 2) {
          // Se reduce en uno los cubos de daño
          getBeliefbase().getBelief("cubosDanno").setFact((int) getBeliefbase().getBelief("cubosDanno").getFact() - 1);
        }
        mapa[Y][X].dannarConexion(direccion);
        mapa[Y_][X_].dannarConexion(direccion_);
        return;
      }
      // Si hay una puerta abierta, se daña y se continua
      if (mapa[Y][X].getConexiones()[direccion] == 1) {
        mapa[Y][X].dannarConexion(direccion);
        mapa[Y_][X_].dannarConexion(direccion_);
      }
      // Si la nueva casilla esta dentro de los limites
      if (Y_ > -1 && X_ > -1 && Y_ < mapa.length && X_ < mapa[0].length) {
        // Si no hay fuego, se cambia a fuego y se para
        if (mapa[Y_][X_].getTieneFuego() != 2) {
          mapa[Y_][X_].setTieneFuego(2);
          return;
        }
        // Si hay fuego, se realiza una nueva explosion en la misma direccion
        explosion(X_, Y_, direccion);
      }
    }
  }

}
