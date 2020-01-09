package rescate.gui;

import java.util.concurrent.CountDownLatch;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import rescate.ontologia.conceptos.Casilla;
import rescate.ontologia.conceptos.Jugador;
import rescate.ontologia.conceptos.Tablero;

public class View extends Application {

  private static final CountDownLatch latch = new CountDownLatch(1);
  public static View view = null;

  private int WIDTH = 1000;
  private int HEIGHT = 800;

  private StackPane root;
  private Pane waiting;
  private Pane tiles;
  private Pane tilesLabels;
  private Pane connections;
  private Pane doors;
  private Pane items;
  private Pane players;
  private Scene game;
  private Stage gameStage;

  private int ROWS = 8;
  private int COLUMNS = 10;

  private int TILE_WIDTH = 100;
  private int TILE_HEIGHT = 100;

  private Tablero tablero;
  private int turno = -1;

  private boolean partidaEmpezada = false;

  public static View waitForStartUpText() {
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return view;
  }

  public static void setView(View view_) {
    view = view_;
    latch.countDown();
  }

  public View() {
    setView(this);
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      root = new StackPane();
      modelSetUp();
      appSetUp(primaryStage);
      if (!partidaEmpezada)
        drawWaiting();
      drawTiles();
      drawPlayers();
      gameStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void appSetUp(Stage primaryStage) {
    waiting = new StackPane();
    tiles = new Pane();
    tilesLabels = new Pane();
    connections = new Pane();
    doors = new Pane();
    items = new Pane();
    players = new Pane();
    root.getChildren().addAll(waiting, tiles, tilesLabels, connections, items, doors, players);
    game = new Scene(root, WIDTH, HEIGHT);
    gameStage = primaryStage;
    gameStage.setScene(game);
  }

  public void modelSetUp() {
    tablero = new Tablero();
  }

  public void clearAndRedraw() {
    tiles.getChildren().clear();
    connections.getChildren().clear();
    doors.getChildren().clear();
    items.getChildren().clear();
    players.getChildren().clear();
    drawTiles();
    drawPlayers();
  }

  public void drawWaiting() {
    Label w = new Label("Esperando jugadores...");
    w.setFont(new Font("Arial Bold", 34));
    w.setTextFill(Color.BLACK);
    waiting.getChildren().add(w);
    FadeTransition ft = new FadeTransition(Duration.millis(1000), w);
    ft.setFromValue(1.0);
    ft.setToValue(0.0);
    ft.setCycleCount(FadeTransition.INDEFINITE);
    ft.setAutoReverse(true);
    ft.play();
  }

  public void drawTiles() {
    if (tablero.getMapa() == null)
      return;
    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLUMNS; j++) {
        Casilla c = tablero.getMapa()[i][j];
        // Floor
        Rectangle r = new Rectangle(j * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
        r.setFill((c.getHabitacion() == 0) ? Color.DARKSEAGREEN : Color.ROSYBROWN);
        r.setStroke(Color.DARKGRAY);
        r.setStrokeType(StrokeType.INSIDE);
        // Floor pos
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.setTranslateX(j * TILE_WIDTH);
        hb.setTranslateY(i * TILE_HEIGHT);
        hb.setPrefWidth(TILE_WIDTH);
        hb.setPrefHeight(TILE_HEIGHT);
        Label pos = new Label(j + ", " + i);
        pos.setFont(new Font("Arial Bold", 14));
        pos.setTextFill(Color.DARKSLATEGRAY);
        hb.getChildren().add(pos);
        tilesLabels.getChildren().add(hb);
        // Aparcamientos
        if (c.getEsAparcamientoAmbulancia())
          r.setFill(Color.DARKGREEN);
        if (c.getEsAparcamientoCamion())
          r.setFill(Color.MEDIUMSEAGREEN);
        // Veh�culos
        if (c.getCamionBomberos())
          r.setFill(Color.ORANGERED);
        if (c.getAmbulancia())
          r.setFill(Color.WHITESMOKE);
        tiles.getChildren().add(r);
        // Conexiones
        for (int k = 0; k < 4; k++) {
          int con = c.getConexiones()[k];
          switch (con) {
          case 0:
            break;
          case 1:
            drawConn(i, j, k, Color.BLUE);
            break;
          case 2:
            drawConn(i, j, k, Color.RED);
            break;
          case 3:
            drawConn(i, j, k, Color.BLACK);
            break;
          case 4:
            drawConn(i, j, k, Color.GRAY);
            break;
          case 5:
            drawConn(i, j, k, Color.DARKGRAY);
            break;
          }
        }
        // PDI
        if (c.getPuntoInteres() != 0) {
          switch (c.getPuntoInteres()) {
          case 1:
            drawPDI(i, j, Color.BLACK);
            break;
          case 2:
            drawPDI(i, j, Color.LAWNGREEN);
            break;
          case 3:
            drawPDI(i, j, Color.CORAL);
            break;
          }
        }
        // Fuego
        int fuego = c.getTieneFuego();
        switch (fuego) {
        case 0:
          break;
        case 1:
          drawFire(i, j, Color.DARKGRAY);
          break;
        case 2:
          drawFire(i, j, Color.RED);
          break;
        }
        // Materia peligrosa
        if (c.getTieneMateriaPeligrosa()) {
          drawDangMat(i, j);
        }
        // Materia peligrosa
        if (c.getTieneFocoCalor()) {
          drawHotPoint(i, j);
        }
      }
    }
  }

  public void drawConn(int i, int j, int pos, Color color) {
    Rectangle r = null;
    switch (pos) {
    case 0:
      r = new Rectangle(j * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT / 8);
      break;
    case 1:
      r = new Rectangle(j * TILE_WIDTH + (TILE_WIDTH - TILE_WIDTH / 8), i * TILE_HEIGHT, TILE_WIDTH / 8, TILE_HEIGHT);
      break;
    case 2:
      r = new Rectangle(j * TILE_WIDTH, i * TILE_HEIGHT + (TILE_HEIGHT - TILE_HEIGHT / 8), TILE_WIDTH, TILE_HEIGHT / 8);
      break;
    case 3:
      r = new Rectangle(j * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH / 8, TILE_HEIGHT);
      break;
    }
    r.setFill(color);
    connections.getChildren().add(r);
  }

  public void drawPDI(int i, int j, Color color) {
    Circle c = new Circle(j * TILE_WIDTH + TILE_WIDTH / 4, i * TILE_HEIGHT + TILE_HEIGHT / 4, TILE_WIDTH / 8);
    c.setFill(Color.BLUE);
    c.setStrokeWidth(5f);
    c.setStroke(color);
    items.getChildren().add(c);
  }

  public void drawFire(int i, int j, Color color) {
    Circle c = new Circle(j * TILE_WIDTH + 3 * TILE_WIDTH / 4, i * TILE_HEIGHT + TILE_HEIGHT / 4, TILE_WIDTH / 8);
    c.setFill(color);
    items.getChildren().add(c);
  }

  public void drawDangMat(int i, int j) {
    Circle c = new Circle(j * TILE_WIDTH + TILE_WIDTH / 4, i * TILE_HEIGHT + 3 * TILE_HEIGHT / 4, TILE_WIDTH / 8);
    c.setFill(Color.MEDIUMPURPLE);
    items.getChildren().add(c);
  }

  public void drawHotPoint(int i, int j) {
    Circle c = new Circle(j * TILE_WIDTH + 3 * TILE_WIDTH / 4, i * TILE_HEIGHT + 3 * TILE_HEIGHT / 4, TILE_WIDTH / 8);
    c.setFill(Color.DARKRED);
    items.getChildren().add(c);
  }

  public void drawPlayers() {
    if (tablero.getJugadores() == null)
      return;
    for (int i = 0; i < tablero.getJugadores().size(); i++) {
      VBox player = new VBox(1);
      player.setPrefWidth(TILE_WIDTH);
      player.setPrefHeight(TILE_HEIGHT);
      player.setAlignment(Pos.CENTER);
      String cssLayout = "-fx-border-color:"
          + ((partidaEmpezada && turno % tablero.getJugadores().size() == i) ? "aqua" : "white") + ";\n"
          + "-fx-border-insets: 5;\n" + "-fx-border-width: "
          + ((partidaEmpezada && turno % tablero.getJugadores().size() == i) ? 6 : 3) + ";\n"
          + "-fx-border-style: dashed;\n";
      player.setStyle(cssLayout);
      HBox bottomInfo = new HBox(10);
      bottomInfo.setPrefWidth(TILE_WIDTH);
      bottomInfo.setAlignment(Pos.CENTER);
      Jugador j = tablero.getJugadores().get(i);
      Label l = new Label("J" + (i + 1));
      l.setFont(new Font("Arial Bold", 32));
      l.setTextFill(Color.BLACK);
      if (j.getSubidoAmbulancia()) {
        Label A = new Label("A");
        A.setFont(new Font("Arial Bold", 14));
        A.setTextFill(Color.ORANGERED);
        bottomInfo.getChildren().add(A);
      } else if (j.getSubidoCamion()) {
        Label C = new Label("C");
        C.setFont(new Font("Arial Bold", 14));
        C.setTextFill(Color.WHITESMOKE);
        bottomInfo.getChildren().add(C);
      }
      Label ROL = new Label();
      switch (j.getRol()) {
      case 0:
        ROL.setText("NINGUN");
        break;
      case 1:
        ROL.setText("SANITA");
        break;
      case 2:
        ROL.setText("<JEFE>");
        break;
      case 3:
        ROL.setText("EXPIMG");
        break;
      case 4:
        ROL.setText("ESPUMA");
        break;
      case 5:
        ROL.setText("MATPEL");
        break;
      case 6:
        ROL.setText("GENERA");
        break;
      case 7:
        ROL.setText("RESCAT");
        break;
      case 8:
        ROL.setText("CONDUC");
        break;
      }
      ROL.setFont(new Font("Arial Bold", 14));
      ROL.setTextFill(Color.BLACK);
      if (j.getLlevandoVictima() == 1) {
        Label V = new Label("VICT.");
        V.setFont(new Font("Arial Bold", 14));
        V.setTextFill(Color.BLACK);
        bottomInfo.getChildren().add(V);
      }
      if (j.getLlevandoVictima() == 2) {
        Label V = new Label("V.CUR.");
        V.setFont(new Font("Arial Bold", 14));
        V.setTextFill(Color.BLACK);
        bottomInfo.getChildren().add(V);
      }
      if (j.getLlevandoMateriaPeligrosa()) {
        Label V = new Label("MAT.");
        V.setFont(new Font("Arial Bold", 14));
        V.setTextFill(Color.BLACK);
        bottomInfo.getChildren().add(V);
      }
      int PAtotales = j.getPuntosAccion() + j.getPuntosAccionExtincion() + j.getPuntosAccionMando()
          + j.getPuntosAccionMovimiento();
      Label PA = new Label(String.valueOf(PAtotales));
      PA.setFont(new Font("Arial Bold", 14));
      PA.setTextFill(Color.BLACK);
      bottomInfo.getChildren().add(PA);
      player.getChildren().addAll(ROL, l, bottomInfo);
      if (j.getPosicion() != null) {
        player.setTranslateX(j.getPosicion()[0] * TILE_WIDTH);
        player.setTranslateY(j.getPosicion()[1] * TILE_HEIGHT);
      } else {
        player.setTranslateY(i * TILE_HEIGHT);
      }
      players.getChildren().addAll(player);
    }
  }

  public Tablero getTablero() {
    return tablero;
  }

  public void setTablero(Tablero tablero) {
    this.tablero = tablero;
  }

  public int getTurno() {
    return turno;
  }

  public void setTurno(int turno) {
    this.turno = turno;
  }

  public boolean isPartidaEmpezada() {
    return partidaEmpezada;
  }

  public void setPartidaEmpezada(boolean partidaEmpezada) {
    this.partidaEmpezada = partidaEmpezada;
  }

}
