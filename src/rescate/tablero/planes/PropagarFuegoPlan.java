package rescate.tablero.planes;

import rescate.ontologia.conceptos.Casilla;

import java.util.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

public class PropagarFuegoPlan extends Plan {

  private int propagaciones = 1;

  @Override
  public void body() {

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();
    Casilla[][] mapa = t.getMapa();

    // Propagar el fuego mientras se deba (1 vez siempre, y es posible que otras si ocurren fogonazos)
    do {
      propagaciones--;
      propagar(mapa);
    } while (propagaciones > 0);

    // Guardar mapa
    t.setMapa(mapa);

    // Aturdir y otros efectos
    // TODO:

    //
    for (int i = 0; i < mapa.length; i++) {
      for (int j = 0; j < mapa[i].length; j++) {
        Casilla.Fuego fuego = mapa[i][j].tieneFuego();
        Casilla.PuntoInteres pdi = mapa[i][j].getPuntoInteres();

        // Si la casilla no tiene fuego o si no hay punto de interés, no se produce nada
        if (fuego != Casilla.Fuego.FUEGO || pdi == Casilla.PuntoInteres.NADA) {
          continue;
        }

        // En caso contrario, tenemos PDI en fuego -> Se pierde
        int _pdi = (int) getBeliefbase().getBelief("pdi").getFact();

        getBeliefbase().getBelief("pdi").setFact(_pdi - 1);
        mapa[i][j].setPuntoInteres(Casilla.PuntoInteres.NADA);
      }
    }

    // Guardar tablero
    getBeliefbase().getBelief("tablero").setFact(t);
  }

  // Realiza una propagación aleatoria
  private void propagar(Casilla[][] mapa) {
    // Casilla a propagar
    int x = (int) (Math.random() * mapa.length + 1);
    int y = (int) (Math.random() * mapa[0].length + 1);

    Casilla c = mapa[x][y];

    // Si tenía un foco de calor, hacer otra propagación después
    if (c.tieneFocoCalor()) {
      propagaciones++;
    }

    // Propagar el fuego
    propagar(c, mapa);

    // Llamaradas
    while(humoAdyacenteAFuego(mapa))
      llamarada(mapa);
  }

  // Realiza una propagación en una casilla
  private void propagar(Casilla c, Casilla[][] mapa) {
    int x = c.getPosicion()[0];
    int y = c.getPosicion()[1];

    if (tieneFuego(c)) {
      // La casilla ya tenía fuego. Explosión en todas las direcciones
      explosion(c, mapa);
    } else if(tieneHumo(c) || esAdyancenteAFuego(c, mapa)) {
      // La casilla no tiene fuego, pero tiene humo o es adyacente a fuego, poner fuego y ya
      mapa[x][y].setTieneFuego(Casilla.Fuego.FUEGO);

      // Si la casilla tenía una materia peligrosa, se produce explosión
      if (c.tieneMateriaPeligrosa()) {
        explosion(c, mapa);

        // La materia peligrosa se convierte en un foco de calor
        mapa[x][y].setTieneMateriaPeligrosa(false);
        mapa[x][y].setTieneFocoCalor(true);
      }

    } else {
      // La casilla no tenía nada ni es adyacente, poner humo y ya
      mapa[x][y].setTieneFuego(Casilla.Fuego.HUMO);
    }
  }

  // Realiza explosión en un casilla en todas las direcciones
  private void explosion(Casilla c, Casilla[][] mapa) {
    explosion(c, mapa, 0);
    explosion(c, mapa, 1);
    explosion(c, mapa, 2);
    explosion(c, mapa, 3);
  }

  // Realiza explosión en un casilla en una dirección
  private void explosion(Casilla c, Casilla[][] mapa, int direccion) {
    int[] pos = c.getPosicion();

    // Primero actuar en la propia casilla
    // Si no tiene fuego, simplemente ponerla en llamas y acabar
    if (!tieneFuego(c)) {
      c.setTieneFuego(Casilla.Fuego.FUEGO);

      mapa[pos[0]][pos[1]] = c;
      return;
    }

    // Si se sigue, es porque tiene llamas: hay explosión inicial u onda expansiva

    // Si no hay otra casilla en la dirección indicada, no hacer nada
    if (!tieneColindancia(c, direccion, mapa)) {
      return;
    }

    // Obtenemos datos para tratar casilla colindante
    int[] posC = posicionColindante(pos, direccion);
    Casilla cC = mapa[posC[0]][posC[1]];
    int direccionO = direccionOpuesta(direccion);

    // Si la casilla en esa direccion es adyacente, se hace onda expansiva (solo se hace explosión en la misma dirección)
    if (c.tieneAdyacencia(direccion)) {
      explosion(cC, mapa, direccion);
    }

    // Si tiene una puerta o pared en la dirección, se daña en ambas casillas
    // Esto hace que una pared se rompa y que las puertas desaparezcan
    if (c.conexionEsPared(direccion) || c.conexionEsPuerta(direccion)) {
      c.dannarConexion(direccion);
      cC.dannarConexion(direccionO);

      mapa[pos[0]][pos[1]] = c;
      mapa[posC[0]][posC[1]] = cC;
    }

  }

  // Realiza llamarada
  private void llamarada(Casilla[][] mapa) {
    // Iteramos todas las posiciones
    for (int i = 0; i < mapa.length; i++) {
      for (int j = 0; j < mapa[i].length; j++) {
        // Cogemos la casilla
        Casilla c = mapa[i][j];

        // Si no tiene humo, seguimos
        if (!tieneHumo(c)) {
          continue;
        }

        // Si tiene humo y es adyacente a fuego
        if (!esAdyancenteAFuego(c, mapa)) {
          c.setTieneFuego(Casilla.Fuego.FUEGO);

          mapa[i][j] = c;
        }

      }
    }
  }

  // Indica si una casilla tiene humo
  private Boolean tieneHumo(Casilla c) {
    return c.tieneFuego() == Casilla.Fuego.HUMO;
  }

  // Indica si una casilla tiene fuego
  private Boolean tieneFuego(Casilla c) {
    return c.tieneFuego() == Casilla.Fuego.FUEGO;
  }

  // Indica si una casilla es adyacente a otra con fuego
  private Boolean esAdyancenteAFuego(Casilla c, Casilla[][] mapa) {
    int x = c.getPosicion()[0];
    int y = c.getPosicion()[1];

    // Comprueba si tiene una casilla adyacente hacia arriba y si esta tiene fuego
    if (c.tieneAdyacencia(0) && mapa[x][y - 1].tieneFuego() == Casilla.Fuego.FUEGO) {
          return true;
    }

    // Comprueba si tiene una casilla adyacente hacia la derecha y si esta tiene fuego
    if (c.tieneAdyacencia(1) && mapa[x + 1][y].tieneFuego() == Casilla.Fuego.FUEGO) {
          return true;
    }

    // Comprueba si tiene una casilla adyacente hacia abajo y si esta tiene fuego
    if (c.tieneAdyacencia(2) && mapa[x][y + 1].tieneFuego() == Casilla.Fuego.FUEGO) {
          return true;
    }

    // Comprueba si tiene una casilla adyacente hacia la izquierda y si esta tiene fuego
    if (c.tieneAdyacencia(3) && mapa[x - 1][y].tieneFuego() == Casilla.Fuego.FUEGO) {
          return true;
    }

    return false;
  }

  // Comprueba si existe en el mapa una casilla colindante a la dada en una direccion
  private Boolean tieneColindancia(Casilla c, int direccion, Casilla[][] mapa) {
    int[] pos = c.getPosicion();
    int[] posC = posicionColindante(pos, direccion);

    return mapa[posC[0]][posC[1]] != null;
  }

  // Calcula la posicion colindante a otra en una direccion
  private int[] posicionColindante(int[] pos, int d) {
    int[] p = new int[] { pos[0], pos[1] };

    if (d == 0) {
      p[1] = pos[1] - 1;
    } else if (d == 1) {
      p[0] = pos[0] + 1;
    } else if (d == 2) {
      p[1] = pos[1] + 1;
    } else {
      p[0] = pos[0] - 1;
    }

    return p;
  }

  // Calcula la direccion opuesta a una dada
  private int direccionOpuesta(int d) {
    if (d == 0) return 2;
    else if (d == 1) return 3;
    else if (d == 2) return 0;
    else return 1;
  }

  // Comprueba el tablero para ver si hay casillas de humo adyacentes a fuego
  private Boolean humoAdyacenteAFuego(Casilla[][] mapa) {

    return false;
  }

}
