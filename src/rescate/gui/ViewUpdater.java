package rescate.gui;

import java.util.ArrayList;

import javafx.application.Platform;
import rescate.ontologia.conceptos.Tablero;

public class ViewUpdater {

  private View view;

  public ViewUpdater() {
    new Thread() {
      @Override
      public void run() {
        javafx.application.Application.launch(View.class);
      }
    }.start();
    view = View.waitForStartUpText();
  }

  public void updateTablero(Tablero t) {
    Platform.runLater(() -> {
      view.setTablero(t);
      view.clearAndRedraw();
    });
  }

  public void empezarPartida() {
    view.setPartidaEmpezada(true);
  }

  public void cambiarTurno(int turno) {
    view.setTurno(turno);
  }

}