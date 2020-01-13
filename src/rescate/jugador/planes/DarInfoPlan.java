package rescate.jugador.planes;

import java.util.ArrayList;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

import rescate.jugador.estrategias.*;
import rescate.jugador.util.*;

@SuppressWarnings("serial")
public class DarInfoPlan extends Plan {

  public void body() {

    // Recibimos la petición
    IMessageEvent peticion = (IMessageEvent) getInitialEvent();

    // Cogemos nuestra info
    Info info = (Info) getBeliefbase().getBelief("info").getFact();

    // Creamos la respuesta
    IMessageEvent respuesta = peticion.createReply(Mensajes.Info.Inform, info);

    // La mandamos
    sendMessage(respuesta);

  }

}
