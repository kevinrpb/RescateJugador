package rescate.tablero.planes;

import java.util.ArrayList;


import jadex.runtime.Plan;

import rescate.ontologia.conceptos.Casilla;
import rescate.ontologia.conceptos.Jugador;
import rescate.ontologia.conceptos.Tablero;

public class EmpezarPlan extends Plan{

	// Casillas con sus atributos de nuestro mapa
	// {con1, con2, con3, con4, flecha, apCamion, apAmbul, hab}
	public int[][][] modelo = new int[][][]
			 {
				{{0, 0, 0, 0, 8, 0, 0, 0},{0, 0, 3, 0, 8, 0, 0, 0},{0, 0, 3, 0, 8, 0, 0, 0},{0, 0, 3, 0, 8, 0, 0, 0},{0, 0, 3, 0, 8, 0, 0, 0},{0, 0, 3, 0, 8, 0, 1, 0},{0, 0, 1, 0, 8, 0, 1, 0},{0, 0, 3, 0, 8, 1, 0, 0},{0, 0, 3, 0, 8, 1, 0, 0},{0, 0, 0, 0, 8, 0, 0, 0}},
				{{0, 3, 0, 0, 8, 1, 0, 0},{3, 0, 0, 3, 3, 0, 0, 1},{3, 0, 0, 0, 4, 0, 0, 1},{3, 2, 0, 0, 4, 0, 0, 1},{3, 0, 0, 2, 4, 0, 0, 2},{3, 3, 0, 0, 4, 0, 0, 2},{1, 0, 0, 3, 4, 0, 0, 3},{3, 0, 0, 0, 4, 0, 0, 3},{3, 3, 0, 0, 5, 0, 0, 3},{0, 0, 0, 3, 8, 0, 0, 0}},
				{{0, 3, 0, 0, 8, 1, 0, 0},{0, 0, 0, 3, 2, 0, 0, 1},{0, 0, 0, 0, 3, 0, 0, 1},{0, 3, 3, 0, 6, 0, 0, 1},{0, 0, 3, 3, 4, 0, 0, 2},{0, 2, 3, 0, 4, 0, 0, 2},{0, 0, 3, 2, 2, 0, 0, 3},{0, 0, 3, 0, 5, 0, 0, 3},{0, 3, 2, 0, 6, 0, 0, 3},{0, 0, 0, 3, 8, 0, 0, 0}},
				{{0, 1, 0, 0, 8, 0, 1, 0},{0, 0, 0, 1, 2, 0, 0, 1},{0, 2, 0, 0, 0, 0, 0, 1},{3, 0, 0, 2, 4, 0, 0, 4},{3, 0, 0, 0, 6, 0, 0, 4},{3, 0, 0, 0, 6, 0, 0, 4},{3, 3, 0, 0, 6, 0, 0, 4},{3, 0, 0, 3, 0, 0, 0, 5},{2, 3, 0, 0, 6, 0, 0, 5},{0, 0, 0, 3, 8, 0, 1, 0}},
				{{0, 3, 0, 0, 8, 0, 1, 0},{0, 0, 3, 3, 2, 0, 0, 1},{0, 3, 3, 0, 4, 0, 0, 1},{0, 0, 3, 3, 2, 0, 0, 4},{0, 0, 2, 0, 2, 0, 0, 4},{0, 0, 3, 0, 2, 0, 0, 4},{0, 2, 3, 0, 0, 0, 0, 4},{0, 0, 3, 2, 4, 0, 0, 5},{0, 1, 3, 0, 6, 0, 0, 5},{0, 0, 0, 1, 8, 0, 1, 0}},
				{{0, 3, 0, 0, 8, 0, 0, 0},{3, 0, 0, 3, 2, 0, 0, 6},{3, 0, 0, 0, 1, 0, 0, 6},{3, 0, 0, 0, 6, 0, 0, 6},{2, 0, 0, 0, 0, 0, 0, 6},{3, 3, 0, 0, 0, 0, 0, 6},{3, 0, 0, 3, 2, 0, 0, 7},{3, 3, 0, 0, 7, 0, 0, 7},{3, 3, 0, 3, 6, 0, 0, 8},{0, 0, 0, 3, 8, 1, 0, 0}},
				{{0, 3, 0, 0, 8, 0, 0, 0},{0, 0, 3, 3, 1, 0, 0, 6},{0, 0, 3, 0, 0, 0, 0, 6},{0, 0, 1, 0, 0, 0, 0, 6},{0, 0, 3, 0, 0, 0, 0, 6},{0, 2, 3, 0, 0, 0, 0, 6},{0, 0, 3, 2, 0, 0, 0, 7},{0, 2, 3, 0, 0, 0, 0, 7},{0, 3, 3, 2, 7, 0, 0, 8},{0, 0, 0, 3, 8, 1, 0, 0}},
				{{0, 0, 0, 0, 8, 0, 0, 0},{3, 0, 0, 0, 8, 1, 0, 0},{3, 0, 0, 0, 8, 1, 0, 0},{1, 0, 0, 0, 8, 0, 1, 0},{3, 0, 0, 0, 8, 0, 1, 0},{3, 0, 0, 0, 8, 0, 0, 0},{3, 0, 0, 0, 8, 0, 0, 0},{3, 0, 0, 0, 8, 0, 0, 0},{3, 0, 0, 0, 8, 0, 0, 0},{0, 0, 0, 0, 8, 0, 0, 0}},
			 };

	Casilla[][] mapa = new Casilla[modelo.length][modelo[0].length];

	public void body() {

		System.out.println("[PLAN] Se inicializa el tablero");

    	// Tablero
		Tablero t = (Tablero) getBeliefbase().getBelief("tablero").getFact();
		
		// Lista de jugadores
		ArrayList<Jugador> jugadores = t.getJugadores();

		for (int i = 0; i < modelo.length; i++) {
			for (int j = 0; i < modelo[i].length; j++) {
				mapa[i][j] = new Casilla();
				mapa[i][j].setPosicion(new int[] { j, i });
				final Casilla.Conexion[] c = Casilla.Conexion.values();
				mapa[i][j].setConexiones(new Casilla.Conexion [] {c[modelo[i][j][0]], c[modelo[i][j][1]], c[modelo[i][j][2]], c[modelo[i][j][3]]});
				mapa[i][j].setFlecha(Casilla.Direccion.values()[modelo[i][j][4]]);
				mapa[i][j].setEsAparcamientoCamion(modelo[i][j][5] == 1);
				mapa[i][j].setEsAparcamientoAmbulancia(modelo[i][j][6] == 1);
				mapa[i][j].setHabitacion(modelo[i][j][7]);
				mapa[i][j].setTieneFuego(Casilla.Fuego.NADA);
				mapa[i][j].setPuntoInteres(Casilla.PuntoInteres.NADA);
			}
		}
		t.setMapa(mapa);
		
		// 3 fuegos con foco de calor y una explosión en cada uno
		PropagarFuegoPlan p = new PropagarFuegoPlan();
		for (int i = 0; i < 3; i++) {
			// Posiciones aleatorias para la materia peligrosa
		    int X = (int) (Math.random() * 8 + 1);
		    int Y = (int) (Math.random() * 6 + 1);
		    // Casilla en la posicion X e Y
		    Casilla casilla = mapa[Y][X];
		    if (casilla.tieneFuego() == Casilla.Fuego.NADA) {
				mapa[Y][X].setTieneFuego(Casilla.Fuego.FUEGO);
				mapa[Y][X].setTieneFocoCalor(true);
				p.metodoExplosion(mapa[Y][X], mapa);
			}else i--;
		}
		
		// Colocamos 3 PDI en posiciones random
		ColocarPuntosInteresPlan c = new ColocarPuntosInteresPlan();
		c.body();
		c.body();
		c.body();
		mapa = t.getMapa();
		
		// Colocamos 6 materias peligrosas
		for (int i = 0; i < 6; i++) {
			// Posiciones aleatorias para la materia peligrosa
		    int X = (int) (Math.random() * 8 + 1);
		    int Y = (int) (Math.random() * 6 + 1);
		    // Casilla en la posicion X e Y
		    Casilla casilla = mapa[Y][X];
		    if (casilla.tieneFuego() == Casilla.Fuego.NADA)
		    	mapa[Y][X].setTieneMateriaPeligrosa(true);
		    else i--;
		}
		
		// Colocamos el camion y la ambulancia en uno de los 4 aparcamientos de manera aleatoria
		switch ((int)(Math.random()*4)) {
		case 0:
			mapa[0][7].setCamionBomberos(true);
			mapa[0][8].setCamionBomberos(true);
			break;
		case 1:
			mapa[5][9].setCamionBomberos(true);
			mapa[6][9].setCamionBomberos(true);
			break;
		case 2:
			mapa[7][1].setCamionBomberos(true);
			mapa[7][2].setCamionBomberos(true);
			break;
		case 3:
			mapa[1][0].setCamionBomberos(true);
			mapa[2][0].setCamionBomberos(true);
			break;
		default:
			break;
		}switch ((int)(Math.random()*4)) {
		case 0:
			mapa[0][5].setAmbulancia(true);
			mapa[0][6].setAmbulancia(true);
			break;
		case 1:
			mapa[3][9].setAmbulancia(true);
			mapa[4][9].setAmbulancia(true);
			break;
		case 2:
			mapa[7][3].setAmbulancia(true);
			mapa[7][4].setAmbulancia(true);
			break;
		case 3:
			mapa[3][0].setAmbulancia(true);
			mapa[4][0].setAmbulancia(true);
			break;
		default:
			break;
		}
		
		// Posicion inicial de jugadores
		ArrayList<Jugador> j = new ArrayList<Jugador>();
		for (int i = 0; i < jugadores.size(); i++) {
			Jugador jugador = jugadores.get(i);
			switch ((int)( Math.random()*4)) {
				case 0:
					jugador.setPosicion(new int[]{6,0});
					break;
				case 1:
					jugador.setPosicion(new int[]{9,4});
					break;
				case 2:
					jugador.setPosicion(new int[]{3,7});
					break;
				case 3:
					jugador.setPosicion(new int[]{0,3});
					break;
				default:
					break;
			}	
			jugador.setHabitacion(0);
			jugador.setLlevandoVictima(Jugador.LlevandoVictima.NO);
			jugador.setRol(Jugador.Rol.NINGUNO);
			j.add(jugador);			
		}

		// Actualizamos la informacion de los jugadores
		t.setJugadores(j);

		// Actualizamos el belief del tablero
		getBeliefbase().getBelief("tablero").setFact(t);
			
	}
	
}
