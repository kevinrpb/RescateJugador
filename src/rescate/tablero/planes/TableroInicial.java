package rescate.tablero.planes;

import rescate.ontologia.conceptos.Casilla;
import jadex.runtime.Plan;

public class TableroInicial extends Plan {

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
			 
	Casilla[][] habitaciones = new Casilla[9][];
	Casilla[][] mapa = new Casilla[modelo.length][modelo[0].length];

	public int indiceHabitacion(int habitacion) {
		for (int i = 0; i < habitaciones[habitacion].length; i++) {
			if (habitaciones[habitacion][i] == null) {
				return i;
			}
		}
		return habitaciones[habitacion].length;
	}

	@Override
	public void body() {

		/*
		 * habitaciones[0] = new Casilla[32]; // fuera habitaciones[1] = new Casilla[6];
		 * // cocina habitaciones[2] = new Casilla[6]; // dormitorio peq 1
		 * habitaciones[3] = new Casilla[6]; // dormitorio peq 2 habitaciones[4] = new
		 * Casilla[15]; // sala de estar habitaciones[5] = new Casilla[9]; // dormitorio
		 * principal habitaciones[6] = new Casilla[2]; // vestidor habitaciones[7] = new
		 * Casilla[4]; // baño
		 */

		habitaciones[0] = new Casilla[32]; // fuera
		habitaciones[1] = new Casilla[10]; // salon
		habitaciones[2] = new Casilla[4]; // baño 1
		habitaciones[3] = new Casilla[6]; // dormitorio principal
		habitaciones[4] = new Casilla[8]; // cocina
		habitaciones[5] = new Casilla[4]; // sala de estar
		habitaciones[6] = new Casilla[10]; // comedor
		habitaciones[7] = new Casilla[4]; // dormitorio pequeño
		habitaciones[8] = new Casilla[2]; // baño 2

		for (int i = 0; i < modelo.length; i++) {
			for (int j = 0; i < modelo[i].length; j++) {
				mapa[i][j] = new Casilla();
				mapa[i][j].setPosicion(new int[] { j, i });
				Casilla.Conexion[] c = Casilla.Conexion.values();
				mapa[i][j].setConexiones(new Casilla.Conexion[] { c[modelo[i][j][0]], c[modelo[i][j][1]],
						c[modelo[i][j][2]], c[modelo[i][j][3]] });
				mapa[i][j].setFlecha(Casilla.Flecha.values()[modelo[i][j][4]]);
				mapa[i][j].setEsAparcamientoCamion(modelo[i][j][5] == 1);
				mapa[i][j].setEsAparcamientoAmbulancia(modelo[i][j][6] == 1);
				habitaciones[modelo[i][j][6]][indiceHabitacion(modelo[i][j][7])] = mapa[i][j];
			}
		}

		getBeliefbase().getBeliefSet("casillas").addFact(mapa);

		/*
		 * for (int i = 0; i < habitaciones.length; i++) { for (int j = 0; j <
		 * habitaciones[i].length; j++) { habitaciones[i][j] = new Casilla();
		 * habitaciones[i][j].setConexiones(new int [][] {{0, 0},{0, 0},{0, 0},{0, 0}});
		 * habitaciones[i][j].setPuntoInteres(new int [] {0, 0});
		 * habitaciones[i][j].setFlecha(new boolean[]{false, false, false, false}); } }
		 * 
		 * habitaciones[0][0].setPosicion(new int[] {0, 0});
		 * habitaciones[0][0].setAdyacentes(new Casilla[] {null, habitaciones[0][1],
		 * habitaciones[0][10], null});
		 * 
		 * habitaciones[0][1].setPosicion(new int[] {0, 1});
		 * habitaciones[0][1].setAdyacentes(new Casilla[] {null, habitaciones[0][2],
		 * habitaciones[4][0], habitaciones[0][0]});
		 * habitaciones[0][1].setConexiones(new int [][] {{0, 0},{0, 0},{2, 0},{0, 0}});
		 * 
		 * habitaciones[0][2].setPosicion(new int[] {0, 2});
		 * habitaciones[0][2].setAdyacentes(new Casilla[] {null, habitaciones[0][3],
		 * habitaciones[4][1], habitaciones[0][1]});
		 * habitaciones[0][2].setConexiones(new int [][] {{0, 0},{0, 0},{2, 0},{0, 0}});
		 * 
		 * habitaciones[0][3].setPosicion(new int[] {0, 3});
		 * habitaciones[0][3].setAdyacentes(new Casilla[] {null, habitaciones[0][4],
		 * habitaciones[4][2], habitaciones[0][2]});
		 * habitaciones[0][3].setConexiones(new int [][] {{0, 0},{0, 0},{2, 0},{0, 0}});
		 * habitaciones[0][3].setEsAparcamientoCamion(true);
		 * 
		 * habitaciones[0][4].setPosicion(new int[] {0, 4});
		 * habitaciones[0][4].setAdyacentes(new Casilla[] {null, habitaciones[0][5],
		 * habitaciones[6][0], habitaciones[0][3]});
		 * habitaciones[0][4].setConexiones(new int [][] {{0, 0},{0, 0},{2, 0},{0, 0}});
		 * habitaciones[0][4].setEsAparcamientoCamion(true);
		 * 
		 * habitaciones[0][5].setPosicion(new int[] {0, 5});
		 * habitaciones[0][5].setAdyacentes(new Casilla[] {null, habitaciones[0][6],
		 * habitaciones[6][1], habitaciones[0][4]});
		 * habitaciones[0][5].setConexiones(new int [][] {{0, 0},{0, 0},{2, 0},{0, 0}});
		 * habitaciones[0][5].setEsAparcamientoAmbulancia(true);
		 * 
		 * habitaciones[0][6].setPosicion(new int[] {0, 6});
		 * habitaciones[0][6].setAdyacentes(new Casilla[] {null, habitaciones[0][7],
		 * habitaciones[5][0], habitaciones[0][5]});
		 * habitaciones[0][6].setConexiones(new int [][] {{0, 0},{0, 0},{2, 0},{0, 0}});
		 * habitaciones[0][6].setEsAparcamientoAmbulancia(true);
		 * 
		 * habitaciones[0][7].setPosicion(new int[] {0, 7});
		 * habitaciones[0][7].setAdyacentes(new Casilla[] {null, habitaciones[0][8],
		 * habitaciones[5][1], habitaciones[0][6]});
		 * habitaciones[0][7].setConexiones(new int [][] {{0, 0},{0, 0},{2, 0},{0, 0}});
		 * 
		 * habitaciones[0][7].setPosicion(new int[] {0, 7});
		 * habitaciones[0][7].setAdyacentes(new Casilla[] {null, habitaciones[0][8],
		 * habitaciones[5][1], habitaciones[0][6]});
		 * habitaciones[0][7].setConexiones(new int [][] {{0, 0},{0, 0},{2, 0},{0, 0}});
		 */
	}
/*
	public static void main(String[] args) {
		System.out.println(Casilla.Conexion.NADA.ordinal());
		System.out.println(Casilla.Conexion.PUERTA_ABIERTA.ordinal());
	}
	*/
}
