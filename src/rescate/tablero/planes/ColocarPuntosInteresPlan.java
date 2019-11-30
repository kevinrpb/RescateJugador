package rescate.tablero.planes;

import java.util.*;

import jadex.runtime.Plan;

import rescate.ontologia.conceptos.*;

public class ColocarPuntosInteresPlan extends Plan {

  @Override
  public void body() {

    System.out.println("[PLAN] El tablero trata de colocar un PDI");

    // PDI tipo victima sin colocar
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

    // Hasta que se coloque el PDI
    while (true) {

      // Hay bombero
      boolean hayBombero = false;
      for (Jugador j : jugadores) {
        if (j.getPosicion()[0] == Y && j.getPosicion()[1] == X) {
          hayBombero = true;
          break;
        }
      }

      // Se puede colocar...
      if (c.getPuntoInteres() == Casilla.PuntoInteres.NADA && c.tieneFuego() != Casilla.Fuego.FUEGO && !hayBombero) {

        // Se coloca el PDI (oculto y cuando se descubra se decidirá si es falsa alarma o víctima)
        System.out.println("[INFO] Se ha colocado un PDI en la casilla[" + c.getPosicion()[0] + ", " + c.getPosicion()[1] + "]");
        c.setPuntoInteres(Casilla.PuntoInteres.OCULTO);
        break;

      }

      // No se puede colocar, se siguen las flechas para encontrar una nueva posible casilla
      else {
        // La nueva casilla es la indica por la flecha de la casilla actual
        switch (c.getFlecha()) {
          case ARRIBA:
            Y--;
            break;
          case ARRIBA_DERECHA:
            X++;
            Y--;
            break;
          case DERECHA:
            X++;
            break;
          case ABAJO_DERECHA:
            X++;
            Y++;
            break;
          case ABAJO:
            Y++;
            break;
          case ABAJO_IZQUIERDA:
            X--;
            Y++;
            break;
          case IZQUIERDA:
            X--;
            break;
          case ARRIBA_IZQUIERDA:
            X--;
            Y--;
            break;
          case NADA:
            break;
        }
        // Se actualiza la casilla
        c = mapa[Y][X];
      }

    }

    // Una vez colocado el nuevo PDI, se actualizan las creencias
		getBeliefbase().getBelief("PDITablero").setFact(PDITablero + 1);
    getBeliefbase().getBelief("tablero").setFact(t);

    /*
    // Casillas en la habitación
    ArrayList<Casilla> habitacion = t.getHabitacion(c.getHabitacion());
    // Se oculta el PDI de aquellos PDI sin revelar aun
    for (Casilla c_: habitacion) {
      if (c_.puntoInteresOculto()) {
        c_.setPuntoInteres(Casilla.PuntoInteres.OCULTO);
      }
    }
    
    // Se informa a los jugadores en la habitación del PDI nuevo
    for (Jugador j: jugadores) {
      if (j.getHabitacion() == c.getHabitacion()) {
        System.out.println("[ACTUALIZACION] Se informa a " + j.getIdAgente() + " del nuevo PDI");
        // Se informa al jugador
        IMessageEvent mensaje = createMessageEvent("InformHabitacionActualizada");
        HabitacionActualizada habitacionPredicado = new HabitacionActualizada();
        habitacionPredicado.setCasillas(habitacion);
        mensaje.getParameterSet(SFipa.RECEIVERS).addValue(j.getIdAgente());
        mensaje.setContent(habitacionPredicado);
        sendMessage(mensaje);
      }
    }
    */
  }

}
