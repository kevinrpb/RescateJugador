package rescate.tablero.planes;

import rescate.ontologia.conceptos.Casilla;

import jadex.runtime.Plan;

import rescate.ontologia.conceptos.*;

public class PropagarFuegoPlan extends Plan {

  private int propagaciones = 1;
  private Casilla[][] mapa;

  @Override
  public void body() {

    // Tablero
    Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();
    mapa = t.getMapa();

    // Propagar el fuego mientras se deba (1 vez siempre, y es posible que otras si ocurren fogonazos)
    do {
      propagaciones--;
      propagar();
    } while (propagaciones > 0);

    // Guardar tablero
    getBeliefbase().getBelief("tablero").setFact(t);

  } 

  private void propagar() {
    // Posiciones aleatorias para el nuevo fuego en el tablero
    int X = (int) (Math.random() * 8 + 1);
    int Y = (int) (Math.random() * 6 + 1);
    // Casilla en la posicion X e Y
    Casilla c = mapa[Y][X];

    // Si tenía un foco de calor, hacer otra propagación después
    if (c.tieneFocoCalor()) {
      propagaciones++;
    }

    // Propagar el fuego
    propagar(X, Y);

    // Llamaradas
    while(humoAdyacenteAFuego())
      llamarada();

  }

  // Propagacion en casilla
  private void propagar(int X, int Y) {
    // Casilla en la posicion X e Y
    Casilla c = mapa[Y][X];

    // La casilla ya tenía fuego
    if (c.tieneFuego() == Casilla.Fuego.FUEGO) {
      // Se produce una explosion
      explosion(X, Y);
    }
    // La casilla tiene humo o es adyacente a fuego
    else if (c.tieneFuego() == Casilla.Fuego.HUMO || esAdyancenteAFuego(X, Y)) {
      // Se pone fuego en la casilla
      mapa[Y][X].setTieneFuego(Casilla.Fuego.FUEGO);
      // Si la casilla tenía una materia peligrosa
      if (c.tieneMateriaPeligrosa()) {
        // Se produce una explosion
        explosion(X, Y);
        // La materia peligrosa se convierte en un foco de calor
        mapa[Y][X].setTieneMateriaPeligrosa(false);
        mapa[Y][X].setTieneFocoCalor(true);
      }
    }
    // En caso contrario
    else {
      // Se pone humo en la casilla
      mapa[Y][X].setTieneFuego(Casilla.Fuego.HUMO);
    }

  }

  // Explosion arriba, derecha, abajo e izquierda
  private void explosion(int X, int Y) {
    for (int i = 0; i < 4; i ++) {
      explosion(X, Y, i);
    }
  }

  // Explosion en una direccion
  private void explosion(int X, int Y, int direccion) {
    // Casilla en la posicion X e Y
    Casilla c = mapa[Y][X];

    // Primero actuar en la propia casilla

    // Si no tiene fuego, ponerla en llamas y acabar
    if (c.tieneFuego() != Casilla.Fuego.FUEGO) {
      c.setTieneFuego(Casilla.Fuego.FUEGO);
      return;
    } 

    // Si tiene fuego, explosion inicial u onda expansiva
    
    // Si no hay otra casilla en la dirección indicada, no hacer nada
    if (!tieneColindancia(X, Y, direccion)) {
      return;
    }

    // Obtenemos datos para tratar casilla colindante
    int[] posicion = posicionColindante(c.getPosicion(), direccion);

    // Si la casilla en esa direccion es adyacente, se hace onda expansiva (solo se hace explosion en la misma direccion)
    if (c.tieneAdyacencia(direccion)) {
      explosion(posicion[0], posicion[1], direccion);
    }

    // Si tiene una puerta o pared en la dirección, se daña en ambas casillas
    // Esto hace que una pared se rompa y que las puertas desaparezcan
    if (c.conexionEsPared(direccion) || c.conexionEsPuerta(direccion)) {

      // Actualizar cubos de daño
      if (c.conexionEsPared(direccion)) {
        getBeliefbase().getBelief("cubosDanno").setFact((int) getBeliefbase().getBelief("cubosDanno").getFact() - 1);
      }

      // Dañar conexiones
      Casilla.Conexion con = c.dannarConexion(direccion);
      Casilla colindante = null;
        switch (direccion) {
          // Arriba
          case 0:
            colindante = mapa[c.getPosicion()[1] - 1][c.getPosicion()[0]];
            colindante.getConexiones()[2] = con;
            break;
          // Derecha
          case 1:
            colindante = mapa[c.getPosicion()[1]][c.getPosicion()[0] + 1];
            colindante.getConexiones()[3] = con;
            break;
          // Abajo
          case 2:
            colindante = mapa[c.getPosicion()[1] + 1][c.getPosicion()[0]];
            colindante.getConexiones()[0] = con;
            break;
          // Izquierda
          case 3:
            colindante = mapa[c.getPosicion()[1]][c.getPosicion()[0] - 1];
            colindante.getConexiones()[1] = con;
            break;
          // ...
          default:
            break;
        }

    }

  }

  // Realiza llamarada
  private void llamarada() {
    // Iteramos todas las posiciones
    for (int i = 0; i < mapa.length; i++) {
      for (int j = 0; j < mapa[i].length; j++) {
        // Cogemos la casilla
        Casilla c = mapa[i][j];
        // Si no tiene humo, seguimos
        if (c.tieneFuego() != Casilla.Fuego.HUMO) {
          continue;
        }
        // Si tiene humo y es adyacente a fuego
        if (!esAdyancenteAFuego(j, i)) {
          c.setTieneFuego(Casilla.Fuego.FUEGO);
        }
      }
    }
  }

  // Indica si una casilla es adyacente a otra con fuego
  private Boolean esAdyancenteAFuego(int X, int Y) {
    // Casilla en la posicion X e Y
    Casilla c = mapa[Y][X];

    // Comprueba si tiene una casilla adyacente hacia arriba y si esta tiene fuego
    if (c.tieneAdyacencia(0) && mapa[Y - 1][X].tieneFuego() == Casilla.Fuego.FUEGO) {
          return true;
    }

    // Comprueba si tiene una casilla adyacente hacia la derecha y si esta tiene fuego
    if (c.tieneAdyacencia(1) && mapa[Y][X + 1].tieneFuego() == Casilla.Fuego.FUEGO) {
          return true;
    }

    // Comprueba si tiene una casilla adyacente hacia abajo y si esta tiene fuego
    if (c.tieneAdyacencia(2) && mapa[Y + 1][X].tieneFuego() == Casilla.Fuego.FUEGO) {
          return true;
    }

    // Comprueba si tiene una casilla adyacente hacia la izquierda y si esta tiene fuego
    if (c.tieneAdyacencia(3) && mapa[Y][X - 1].tieneFuego() == Casilla.Fuego.FUEGO) {
          return true;
    }

    return false;
  }

  // Comprueba si existe en el mapa una casilla colindante a la dada en una direccion
  private boolean tieneColindancia(int X, int Y, int direccion) {
    int[] posicion = posicionColindante(mapa[Y][X].getPosicion(), direccion);
    return mapa[posicion[1]][posicion[0]] != null;
  }

  // Calcula la posicion colindante a otra en una direccion
  private int[] posicionColindante(int[] pos, int direccion) {
    int[] posicion = new int[] {pos[0], pos[1]};

    if (direccion == 0) {
      posicion[1] = pos[1] - 1;
    } else if (direccion == 1) {
      posicion[0] = pos[0] + 1;
    } else if (direccion == 2) {
      posicion[1] = pos[1] + 1;
    } else {
      posicion[0] = pos[0] - 1;
    }

    return posicion;
  }

  // Comprueba el tablero para ver si hay casillas de humo adyacentes a fuego
  private boolean humoAdyacenteAFuego() {
    // Buscamos entre las casillas las que tienen humo
    for (int i = 0; i < mapa.length; i++) {
      for (int j = 0; j < mapa[i].length; j++) {
        // Si la casilla no tiene humo, nada
        if (mapa[i][j].tieneFuego() != Casilla.Fuego.HUMO) {
          continue;
        }
        // Para cada una de las direcciones, si la casilla tiene colindancia ahí y adyacencia, comprobamos si esa casilla tiene fuego
        for (int k = 0; k < 4; k ++) {
          if (tieneColindancia(j, i, k) && mapa[i][j].tieneAdyacencia(k)) {
            int[] posicion = posicionColindante(mapa[i][j].getPosicion(), k);
            if (mapa[posicion[1]][posicion[0]].tieneFuego() == Casilla.Fuego.FUEGO) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

}
