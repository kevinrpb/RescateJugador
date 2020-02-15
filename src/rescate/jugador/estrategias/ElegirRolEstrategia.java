package rescate.jugador.estrategias;

import java.util.ArrayList;

import jadex.adapter.fipa.*;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.IGoal;

import rescate.ontologia.acciones.*;
import rescate.ontologia.conceptos.*;
import rescate.ontologia.predicados.*;

import rescate.jugador.util.*;

public class ElegirRolEstrategia {

  private static int[] preferenciasRoles = new int[] {
    Roles.Espuma,
    Roles.Generalista,
    Roles.Rescates,
    Roles.Sanitaria,
    Roles.Materias,
    Roles.Imagenes,
    Roles.Conductor,
    Roles.Jefe
  };

  public static RolElegido ejecutar(Plan plan, AgentIdentifier idTablero) {
    int rolElegido = preferenciasRoles[0];

    // Intentamos elegir el primer rol
    ElegirRol accion = new ElegirRol();
    accion.setRol(rolElegido);

    // Creamos el mensaje
    IMessageEvent rolMsgSend = plan.createMessageEvent(Mensajes.Rol.RequestElegir);
    rolMsgSend.setContent(accion);
    rolMsgSend.getParameterSet(SFipa.RECEIVERS).addValue(idTablero);

    // Mandamos el mensaje
    IMessageEvent rolMsgReceive = plan.sendMessageAndWait(rolMsgSend);

    // Si el rol no está disponible, mandamos otro
    // (hacemos while por si otro jugador elige antes que nosotros)
    while (rolMsgReceive.getName().contains(Mensajes.Rol.RefuseElegir)) {
      // Cogemos los roles disponibles
      ArrayList<Integer> rolesDisponibles = ((RolesDisponibles) rolMsgReceive.getContent()).getRolesDisponibles();

      // Buscamos el primer rol de nuestras preferencias que esté disponible
      for (int rol : preferenciasRoles) {
        if (rolesDisponibles.contains(rol)) {
          rolElegido = rol;
          accion.setRol(rolElegido);
          rolMsgSend.setContent(accion);

          break;
        }
      }

      // Volvemos a mandar el mensaje
      rolMsgReceive = plan.sendMessageAndWait(rolMsgSend);
    }

    // Si llegamos aquí, el mensaje debería ser positivo. Comprobamos
    assert rolMsgReceive.getName().contains(Mensajes.Rol.InformElegido);

    return (RolElegido) rolMsgReceive.getContent();
  }

}
