package rescate.tablero.planes;

import java.util.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.*;
import rescate.ontologia.conceptos.Casilla;
import rescate.ontologia.conceptos.Jugador;
import rescate.ontologia.conceptos.Casilla.Conexion;
import rescate.ontologia.conceptos.Casilla.Flecha;
import rescate.ontologia.conceptos.Casilla.PuntoInteres;
import rescate.ontologia.conceptos.Casilla.PuntoInteresReal;

public class ColocarPuntosInteresPlan extends Plan {

	@Override
	public void body() {

		int puntosRestantes = (int) getBeliefbase().getBelief("PDITablero").getFact();
		int victimaRestantes = (int) getBeliefbase().getBelief("fichasVictima").getFact();
		int falsaRestantes = (int) getBeliefbase().getBelief("fichasFalsaAlarma").getFact();
		Casilla[][] mapa = (Casilla[][]) getBeliefbase().getBeliefSet("casillas").getFacts();
		Jugador[] listaJugadores = (Jugador[]) getBeliefbase().getBelief("jugadores").getFact();

		getBeliefbase().getBeliefSet("PDITablero").removeFact(puntosRestantes);
		getBeliefbase().getBeliefSet("fichasVictima").removeFact(victimaRestantes);
		getBeliefbase().getBeliefSet("fichasFalsaAlarma").removeFact(falsaRestantes);
		getBeliefbase().getBeliefSet("casillas").removeFact(mapa);
		getBeliefbase().getBeliefSet("jugadores").removeFact(listaJugadores);

		for (; puntosRestantes < 3; puntosRestantes++) {

			int posicionX = (int) (Math.random() * 8 + 1); // entre 1 y 8
			int posicionY = (int) (Math.random() * 6 + 1); // entre 1 y 6
			Casilla elegida = mapa[posicionY][posicionX];
			boolean ok = true;
			if (elegida.getPuntoInteres() == PuntoInteres.NADA && elegida.tieneFuego() != 2) {
				for (int i = 0; i < listaJugadores.length; i++) {
					if (listaJugadores[i].getPosicion()[0] == posicionY
							&& listaJugadores[i].getPosicion()[1] == posicionX) {
						ok = false;
					}
				}

				// caso flechas
				if (ok == false) {

					while (ok == false) {
						if (elegida.getFlecha() == Flecha.ARRIBA) {
							posicionY += 1;
						} else if (elegida.getFlecha() == Flecha.ARRIBADERECHA) {
							posicionY += 1;
							posicionX += 1;
						} else if (elegida.getFlecha() == Flecha.DERECHA) {
							posicionX += 1;
						} else if (elegida.getFlecha() == Flecha.DERECHAABAJO) {
							posicionY -= 1;
							posicionX += 1;
						} else if (elegida.getFlecha() == Flecha.ABAJO) {
							posicionY -= 1;
						} else if (elegida.getFlecha() == Flecha.ABAJOIZQUIERDA) {
							posicionY -= 1;
							posicionX -= 1;
						} else if (elegida.getFlecha() == Flecha.IZQUIERDA) {
							posicionX -= 1;
						} else if (elegida.getFlecha() == Flecha.IZQUIERDAARRIBA) {
							posicionY += 1;
							posicionX -= 1;
						}
						elegida = mapa[posicionY][posicionX];
						if (elegida.getPuntoInteres() == PuntoInteres.NADA && elegida.tieneFuego() != 2) {
							ok = true;
						}
					}
				}

				// caso sencillo
				if (ok == true) {
					if (falsaRestantes == 0) {
						elegida.setPuntoInteresReal(PuntoInteresReal.VICTIMA);
						elegida.setPuntoInteres(PuntoInteres.VICTIMA);
						victimaRestantes--;
					} else {
						int random = (int) (Math.random() * 2);// entre 0 y 1
						if (random == 0) { // victima
							elegida.setPuntoInteresReal(PuntoInteresReal.VICTIMA);
							elegida.setPuntoInteres(PuntoInteres.VICTIMA);
							victimaRestantes--;
						} else {// falsa alarma
							elegida.setPuntoInteresReal(PuntoInteresReal.FALSA_ALARMA);
							elegida.setPuntoInteres(PuntoInteres.FALSA_ALARMA);
							falsaRestantes--;
						}

					}
				}

			}

		}

		getBeliefbase().getBeliefSet("PDITablero").addFact(puntosRestantes);
		getBeliefbase().getBeliefSet("fichasVictima").addFact(victimaRestantes);
		getBeliefbase().getBeliefSet("fichasFalsaAlarma").addFact(falsaRestantes);
		getBeliefbase().getBeliefSet("casillas").addFact(mapa);
		getBeliefbase().getBeliefSet("jugadores").addFact(listaJugadores);

	}

}
