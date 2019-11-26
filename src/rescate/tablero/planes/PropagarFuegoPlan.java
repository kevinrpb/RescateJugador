package rescate.tablero.planes;

import rescate.ontologia.conceptos.Casilla;

import java.util.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

import rescate.ontologia.*;
import rescate.ontologia.conceptos.Ambulancia;
import rescate.ontologia.conceptos.Casilla;
import rescate.ontologia.conceptos.Casilla.Conexion;
import rescate.ontologia.conceptos.Casilla.PuntoInteres;
import rescate.ontologia.conceptos.Casilla.PuntoInteresReal;
import rescate.ontologia.conceptos.Jugador;

public class PropagarFuegoPlan extends Plan {

	public void metodoExplosion(Casilla elegida, Casilla[][] mapa) {
		Jugador[] listaJugadores = (Jugador[]) getBeliefbase().getBelief("jugadores").getFact();

		int posicionY = elegida.getPosicion()[1] - 1;
		int posicionX = elegida.getPosicion()[0];
		boolean calle = false;

		while (mapa[posicionY][posicionX].tieneFuego() == 2 && (elegida.getConexiones()[2] != Conexion.PARED)
				&& (elegida.getConexiones()[2] != Conexion.PARED_SEMIRROTA)
				&& (elegida.getConexiones()[2] != Conexion.PUERTA_CERRADA)) {

			if (mapa[posicionY][posicionX].getPuntoInteresReal() == PuntoInteresReal.VICTIMA) {
				int victim = (int) getBeliefbase().getBelief("victimaMuertas").getFact();
				getBeliefbase().getBeliefSet("victimaMuertas").removeFact(victim);
				victim += 1;
				getBeliefbase().getBeliefSet("victimaMuertas").addFact(victim);
				mapa[posicionY][posicionX].setPuntoInteres(PuntoInteres.NADA);
				mapa[posicionY][posicionX].setPuntoInteresReal(PuntoInteresReal.NADA);

				// Se decrementa el contador de fichas de interes
				int PDIEnTablero = (int) getBeliefbase().getBelief("PDITablero").getFact();
				getBeliefbase().getBeliefSet("PDITablero").removeFact(PDIEnTablero);
				PDIEnTablero -= 1;
				getBeliefbase().getBeliefSet("PDITablero").addFact(PDIEnTablero);
			}

			for (int i = 0; i < listaJugadores.length; i++) {
				if (listaJugadores[i].getPosicion()[0] == posicionY
						&& listaJugadores[i].getPosicion()[1] == posicionX) {
					getBeliefbase().getBeliefSet("jugadores").removeFact(listaJugadores);
					Ambulancia ambulancia = (Ambulancia) getBeliefbase().getBelief("ambulancia").getFact();
					listaJugadores[i].setPosicion(ambulancia.getPosicion());
					getBeliefbase().getBeliefSet("jugadores").addFact(listaJugadores);
				}
			}

			// Si hay una puerta abierta la rompe
			if (mapa[posicionY][posicionX].getConexiones()[2] == Conexion.PUERTA_ABIERTA) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[2] = Conexion.NADA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY - 1][posicionX].getConexiones();
				conexiones[0] = Conexion.NADA;
				mapa[posicionY - 1][posicionX].setConexiones(conexiones);
			}

			// Si hemos llegado a la calle paramos
			if (mapa[posicionY][posicionX].esUnaCalle()) {
				calle = true;
				break;
			}

			posicionY--;
		}

		if (!calle) {
			if (mapa[posicionY][posicionX].getConexiones()[2] == Conexion.PARED) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[2] = Conexion.PARED_SEMIRROTA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY - 1][posicionX].getConexiones();
				conexiones[0] = Conexion.PARED_SEMIRROTA;
				mapa[posicionY - 1][posicionX].setConexiones(conexiones);

			} else if (mapa[posicionY][posicionX].getConexiones()[2] == Conexion.PARED_SEMIRROTA) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[2] = Conexion.NADA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY - 1][posicionX].getConexiones();
				conexiones[0] = Conexion.NADA;
				mapa[posicionY - 1][posicionX].setConexiones(conexiones);
			} else {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[2] = Conexion.PUERTA_ABIERTA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY - 1][posicionX].getConexiones();
				conexiones[0] = Conexion.PUERTA_ABIERTA;
				mapa[posicionY - 1][posicionX].setConexiones(conexiones);

			}

		}

		posicionY = elegida.getPosicion()[1] + 1;
		posicionX = elegida.getPosicion()[0];
		calle = false;

		while (mapa[posicionY][posicionX].tieneFuego() == 2 && (elegida.getConexiones()[0] != Conexion.PARED)
				&& (elegida.getConexiones()[0] != Conexion.PARED_SEMIRROTA)
				&& (elegida.getConexiones()[0] != Conexion.PUERTA_CERRADA)) {

			if (mapa[posicionY][posicionX].getPuntoInteresReal() == PuntoInteresReal.VICTIMA) {
				int victim = (int) getBeliefbase().getBelief("victimaMuertas").getFact();
				getBeliefbase().getBeliefSet("victimaMuertas").removeFact(victim);
				victim += 1;
				getBeliefbase().getBeliefSet("victimaMuertas").addFact(victim);
				mapa[posicionY][posicionX].setPuntoInteres(PuntoInteres.NADA);
				mapa[posicionY][posicionX].setPuntoInteresReal(PuntoInteresReal.NADA);

				// Se decrementa el contador de fichas de interes
				int PDIEnTablero = (int) getBeliefbase().getBelief("PDITablero").getFact();
				getBeliefbase().getBeliefSet("PDITablero").removeFact(PDIEnTablero);
				PDIEnTablero -= 1;
				getBeliefbase().getBeliefSet("PDITablero").addFact(PDIEnTablero);
			}
			for (int i = 0; i < listaJugadores.length; i++) {
				if (listaJugadores[i].getPosicion()[0] == posicionY
						&& listaJugadores[i].getPosicion()[1] == posicionX) {
					getBeliefbase().getBeliefSet("jugadores").removeFact(listaJugadores);
					Ambulancia ambulancia = (Ambulancia) getBeliefbase().getBelief("ambulancia").getFact();
					listaJugadores[i].setPosicion(ambulancia.getPosicion());
					getBeliefbase().getBeliefSet("jugadores").addFact(listaJugadores);
				}
			}

			// Si hay una puerta abierta la rompe
			if (mapa[posicionY][posicionX].getConexiones()[0] == Conexion.PUERTA_ABIERTA) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[0] = Conexion.NADA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY + 1][posicionX].getConexiones();
				conexiones[2] = Conexion.NADA;
				mapa[posicionY + 1][posicionX].setConexiones(conexiones);
			}

			// Si hemos llegado a la calle paramos
			if (mapa[posicionY][posicionX].esUnaCalle()) {
				calle = true;
				break;
			}

			posicionY++;
		}

		if (!calle) {
			if (mapa[posicionY][posicionX].getConexiones()[0] == Conexion.PARED) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[0] = Conexion.PARED_SEMIRROTA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY + 1][posicionX].getConexiones();
				conexiones[2] = Conexion.PARED_SEMIRROTA;
				mapa[posicionY + 1][posicionX].setConexiones(conexiones);

			} else if (mapa[posicionY][posicionX].getConexiones()[0] == Conexion.PARED_SEMIRROTA) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[0] = Conexion.NADA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY + 1][posicionX].getConexiones();
				conexiones[2] = Conexion.NADA;
				mapa[posicionY + 1][posicionX].setConexiones(conexiones);
			} else {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[0] = Conexion.PUERTA_ABIERTA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY + 1][posicionX].getConexiones();
				conexiones[2] = Conexion.PUERTA_ABIERTA;
				mapa[posicionY + 1][posicionX].setConexiones(conexiones);

			}

		}

		posicionY = elegida.getPosicion()[1];
		posicionX = elegida.getPosicion()[0] - 1;
		calle = false;

		while (mapa[posicionY][posicionX].tieneFuego() == 2 && (elegida.getConexiones()[3] != Conexion.PARED)
				&& (elegida.getConexiones()[3] != Conexion.PARED_SEMIRROTA)
				&& (elegida.getConexiones()[3] != Conexion.PUERTA_CERRADA)) {

			if (mapa[posicionY][posicionX].getPuntoInteresReal() == PuntoInteresReal.VICTIMA) {
				int victim = (int) getBeliefbase().getBelief("victimaMuertas").getFact();
				getBeliefbase().getBeliefSet("victimaMuertas").removeFact(victim);
				victim += 1;
				getBeliefbase().getBeliefSet("victimaMuertas").addFact(victim);
				mapa[posicionY][posicionX].setPuntoInteres(PuntoInteres.NADA);
				mapa[posicionY][posicionX].setPuntoInteresReal(PuntoInteresReal.NADA);

				// Se decrementa el contador de fichas de interes
				int PDIEnTablero = (int) getBeliefbase().getBelief("PDITablero").getFact();
				getBeliefbase().getBeliefSet("PDITablero").removeFact(PDIEnTablero);
				PDIEnTablero -= 1;
				getBeliefbase().getBeliefSet("PDITablero").addFact(PDIEnTablero);
			}
			for (int i = 0; i < listaJugadores.length; i++) {
				if (listaJugadores[i].getPosicion()[0] == posicionY
						&& listaJugadores[i].getPosicion()[1] == posicionX) {
					getBeliefbase().getBeliefSet("jugadores").removeFact(listaJugadores);
					Ambulancia ambulancia = (Ambulancia) getBeliefbase().getBelief("ambulancia").getFact();
					listaJugadores[i].setPosicion(ambulancia.getPosicion());
					getBeliefbase().getBeliefSet("jugadores").addFact(listaJugadores);
				}
			}

			// Si hay una puerta abierta la rompe
			if (mapa[posicionY][posicionX].getConexiones()[3] == Conexion.PUERTA_ABIERTA) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[3] = Conexion.NADA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY][posicionX - 1].getConexiones();
				conexiones[1] = Conexion.NADA;
				mapa[posicionY][posicionX - 1].setConexiones(conexiones);
			}

			// Si hemos llegado a la calle paramos
			if (mapa[posicionY][posicionX].esUnaCalle()) {
				calle = true;
				break;
			}

			posicionX--;
		}

		if (!calle) {
			if (mapa[posicionY][posicionX].getConexiones()[3] == Conexion.PARED) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[3] = Conexion.PARED_SEMIRROTA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY][posicionX - 1].getConexiones();
				conexiones[1] = Conexion.PARED_SEMIRROTA;
				mapa[posicionY][posicionX - 1].setConexiones(conexiones);

			} else if (mapa[posicionY][posicionX].getConexiones()[3] == Conexion.PARED_SEMIRROTA) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[3] = Conexion.NADA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY][posicionX - 1].getConexiones();
				conexiones[1] = Conexion.NADA;
				mapa[posicionY][posicionX - 1].setConexiones(conexiones);
			} else {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[3] = Conexion.PUERTA_ABIERTA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY][posicionX - 1].getConexiones();
				conexiones[1] = Conexion.PUERTA_ABIERTA;
				mapa[posicionY][posicionX - 1].setConexiones(conexiones);

			}

		}

		posicionY = elegida.getPosicion()[1];
		posicionX = elegida.getPosicion()[0] + 1;
		calle = false;

		while (mapa[posicionY][posicionX].tieneFuego() == 2 && (elegida.getConexiones()[1] != Conexion.PARED)
				&& (elegida.getConexiones()[1] != Conexion.PARED_SEMIRROTA)
				&& (elegida.getConexiones()[1] != Conexion.PUERTA_CERRADA)) {

			if (mapa[posicionY][posicionX].getPuntoInteresReal() == PuntoInteresReal.VICTIMA) {
				int victim = (int) getBeliefbase().getBelief("victimaMuertas").getFact();
				getBeliefbase().getBeliefSet("victimaMuertas").removeFact(victim);
				victim += 1;
				getBeliefbase().getBeliefSet("victimaMuertas").addFact(victim);
				mapa[posicionY][posicionX].setPuntoInteres(PuntoInteres.NADA);
				mapa[posicionY][posicionX].setPuntoInteresReal(PuntoInteresReal.NADA);

				// Se decrementa el contador de fichas de interes
				int PDIEnTablero = (int) getBeliefbase().getBelief("PDITablero").getFact();
				getBeliefbase().getBeliefSet("PDITablero").removeFact(PDIEnTablero);
				PDIEnTablero -= 1;
				getBeliefbase().getBeliefSet("PDITablero").addFact(PDIEnTablero);
			}
			for (int i = 0; i < listaJugadores.length; i++) {
				if (listaJugadores[i].getPosicion()[0] == posicionY
						&& listaJugadores[i].getPosicion()[1] == posicionX) {
					getBeliefbase().getBeliefSet("jugadores").removeFact(listaJugadores);
					Ambulancia ambulancia = (Ambulancia) getBeliefbase().getBelief("ambulancia").getFact();
					listaJugadores[i].setPosicion(ambulancia.getPosicion());
					getBeliefbase().getBeliefSet("jugadores").addFact(listaJugadores);
				}
			}

			// Si hay una puerta abierta la rompe
			if (mapa[posicionY][posicionX].getConexiones()[1] == Conexion.PUERTA_ABIERTA) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[1] = Conexion.NADA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY][posicionX + 1].getConexiones();
				conexiones[3] = Conexion.NADA;
				mapa[posicionY][posicionX + 1].setConexiones(conexiones);
			}

			// Si hemos llegado a la calle paramos
			if (mapa[posicionY][posicionX].esUnaCalle()) {
				calle = true;
				break;
			}

			posicionX++;
		}

		if (!calle) {
			if (mapa[posicionY][posicionX].getConexiones()[1] == Conexion.PARED) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[1] = Conexion.PARED_SEMIRROTA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY][posicionX + 1].getConexiones();
				conexiones[3] = Conexion.PARED_SEMIRROTA;
				mapa[posicionY][posicionX + 1].setConexiones(conexiones);

			} else if (mapa[posicionY][posicionX].getConexiones()[1] == Conexion.PARED_SEMIRROTA) {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[1] = Conexion.NADA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY][posicionX + 1].getConexiones();
				conexiones[3] = Conexion.NADA;
				mapa[posicionY][posicionX + 1].setConexiones(conexiones);
			} else {
				// Modificar este lado de la puerta
				Conexion[] conexiones = mapa[posicionY][posicionX].getConexiones();
				conexiones[1] = Conexion.PUERTA_ABIERTA;
				mapa[posicionY][posicionX].setConexiones(conexiones);

				// Modificar la puerta en el otro lado
				conexiones = mapa[posicionY][posicionX + 1].getConexiones();
				conexiones[3] = Conexion.PUERTA_ABIERTA;
				mapa[posicionY][posicionX + 1].setConexiones(conexiones);

			}

		}

	}

	@Override
	public void body() {

		Casilla[][] mapa = (Casilla[][]) getBeliefbase().getBeliefSet("casillas").getFacts();
		getBeliefbase().getBeliefSet("casillas").removeFact(mapa);
		boolean ok = true;
		while (ok) {
			int posicionX = (int) (Math.random() * 8 + 1); // entre 1 y 8
			int posicionY = (int) (Math.random() * 6 + 1); // entre 1 y 6
			Casilla elegida = mapa[posicionY][posicionX];

			if (elegida.tieneFuego() == 0) { // si la elegida no tiene ni fuego ni humo
				elegida.setTieneFuego(1);
				if (mapa[posicionY - 1][posicionX].tieneFuego() == 2 && (elegida.getConexiones()[2] != Conexion.PARED)
						&& (elegida.getConexiones()[2] != Conexion.PARED_SEMIRROTA)
						&& (elegida.getConexiones()[2] != Conexion.PUERTA_CERRADA)) {
					elegida.setTieneFuego(2);
				} else if (mapa[posicionY + 1][posicionX].tieneFuego() == 2
						&& (elegida.getConexiones()[0] != Conexion.PARED)
						&& (elegida.getConexiones()[0] != Conexion.PARED_SEMIRROTA)
						&& (elegida.getConexiones()[0] != Conexion.PUERTA_CERRADA)) {
					elegida.setTieneFuego(2);
				} else if (mapa[posicionY][posicionX + 1].tieneFuego() == 2
						&& (elegida.getConexiones()[1] != Conexion.PARED)
						&& (elegida.getConexiones()[1] != Conexion.PARED_SEMIRROTA)
						&& (elegida.getConexiones()[1] != Conexion.PUERTA_CERRADA)) {
					elegida.setTieneFuego(2);
				} else if (mapa[posicionY][posicionX - 1].tieneFuego() == 2
						&& (elegida.getConexiones()[3] != Conexion.PARED)
						&& (elegida.getConexiones()[3] != Conexion.PARED_SEMIRROTA)
						&& (elegida.getConexiones()[3] != Conexion.PUERTA_CERRADA)) {
					elegida.setTieneFuego(2);
				}

			} else if (elegida.tieneFuego() == 1) { // si tiene humo, poner fuego
				elegida.setTieneFuego(2);
				if (elegida.tieneMateriaPeligrosa()) { // hacer explosion
					metodoExplosion(elegida, mapa);
				}

				Jugador[] listaJugadores = (Jugador[]) getBeliefbase().getBelief("jugadores").getFact();

				if (elegida.getPuntoInteresReal() == PuntoInteresReal.VICTIMA) {
					int victim = (int) getBeliefbase().getBelief("victimaMuertas").getFact();
					getBeliefbase().getBeliefSet("victimaMuertas").removeFact(victim);
					victim += 1;
					getBeliefbase().getBeliefSet("victimaMuertas").addFact(victim);

					int PDIEnTablero = (int) getBeliefbase().getBelief("PDITablero").getFact();
					getBeliefbase().getBeliefSet("PDITablero").removeFact(PDIEnTablero);
					PDIEnTablero -= 1;
					getBeliefbase().getBeliefSet("PDITablero").addFact(PDIEnTablero);

					mapa[posicionY][posicionX].setPuntoInteres(PuntoInteres.NADA);
					mapa[posicionY][posicionX].setPuntoInteresReal(PuntoInteresReal.NADA);
				} else if (elegida.getPuntoInteresReal() == PuntoInteresReal.FALSA_ALARMA) {
					mapa[posicionY][posicionX].setPuntoInteres(PuntoInteres.NADA);
					mapa[posicionY][posicionX].setPuntoInteresReal(PuntoInteresReal.NADA);
				}

				for (int i = 0; i < listaJugadores.length; i++) {
					if (listaJugadores[i].getPosicion()[0] == posicionY
							&& listaJugadores[i].getPosicion()[1] == posicionX) {
						getBeliefbase().getBeliefSet("jugadores").removeFact(listaJugadores);
						Ambulancia ambulancia = (Ambulancia) getBeliefbase().getBelief("ambulancia").getFact();
						listaJugadores[i].setPosicion(ambulancia.getPosicion());
						getBeliefbase().getBeliefSet("jugadores").addFact(listaJugadores);
					}
				}

				if (mapa[posicionY - 1][posicionX].tieneFuego() == 2 && (elegida.getConexiones()[2] != Conexion.PARED)
						&& (elegida.getConexiones()[2] != Conexion.PARED_SEMIRROTA)
						&& (elegida.getConexiones()[2] != Conexion.PUERTA_CERRADA)) {
					elegida.setTieneFuego(2);
				} else if (mapa[posicionY + 1][posicionX].tieneFuego() == 2
						&& (elegida.getConexiones()[0] != Conexion.PARED)
						&& (elegida.getConexiones()[0] != Conexion.PARED_SEMIRROTA)
						&& (elegida.getConexiones()[0] != Conexion.PUERTA_CERRADA)) {
					elegida.setTieneFuego(2);
				} else if (mapa[posicionY][posicionX + 1].tieneFuego() == 2
						&& (elegida.getConexiones()[1] != Conexion.PARED)
						&& (elegida.getConexiones()[1] != Conexion.PARED_SEMIRROTA)
						&& (elegida.getConexiones()[1] != Conexion.PUERTA_CERRADA)) {
					elegida.setTieneFuego(2);
				} else if (mapa[posicionY][posicionX - 1].tieneFuego() == 2
						&& (elegida.getConexiones()[3] != Conexion.PARED)
						&& (elegida.getConexiones()[3] != Conexion.PARED_SEMIRROTA)
						&& (elegida.getConexiones()[3] != Conexion.PUERTA_CERRADA)) {
					elegida.setTieneFuego(2);
				}
			} else { // si tiene fuego, explosion
				metodoExplosion(elegida, mapa);
			}

			// si no tiene foco de calor
			if (!elegida.tieneFocoCalor()) {
				ok = false;
			}

		}

		getBeliefbase().getBeliefSet("casillas").addFact(mapa);

	}

}