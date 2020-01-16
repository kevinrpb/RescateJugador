package rescate.jugador.util;

public class Mensajes {

  public class Puerta {

    public static final String RequestAbrir   = "Request_Abrir_Puerta";
    public static final String InformAbierta  = "Inform_Puerta_Abierta";
    public static final String RefuseAbrir    = "Refuse_Abrir_Puerta";
    public static final String FailureAbrir   = "Failure_Abrir_Puerta";

  }

  public class Fuego {

    public static final String RequestApagar  = "Request_Apagar_Fuego";
    public static final String InformApagado  = "Inform_Fuego_Apagado";
    public static final String RefuseApagar   = "Refuse_Apagar_Fuego";
    public static final String FailureApagar  = "Failure_Apagar_Fuego";

  }

  public class Turno {

    public static final String RequestCambiar = "Request_Cambiar_Turno";
    public static final String InformCambiado = "Inform_Turno_Cambiado";
    public static final String RefuseCambiar  = "Refuse_Cambiar_Turno";

  }

  public class Info {

    public static final String Request        = "Request_Info";
    public static final String Inform         = "Inform_Info";

  }

  public class Desplazar {

    public static final String Request        = "Request_Desplazar";
    public static final String Inform         = "Inform_Desplazado";
    public static final String Refuse         = "Refuse_Desplazar";
    public static final String Failure        = "Failure_Desplazar";

  }

  public class Rol {

    public static final String RequestElegir  = "Request_Elegir_Rol";
    public static final String InformElegido  = "Inform_Rol_Elegido";
    public static final String RefuseElegir   = "Refuse_Elegir_Rol";

  }

  public class Partida {

    public static final String RequestUnirse  = "Request_Unirse_Partida";

  }

  public class Victima {

    public static final String RequestCoger   = "Request_Coger_Victima";
    public static final String InformCogida   = "Inform_Victima_Cogida";
    public static final String RefuseCoger    = "Refuse_Coger_Victima";
    public static final String FailureCoger   = "Failure_Coger_Victima";

    public static final String RequestDejar   = "Request_Dejar_Victima";
    public static final String InformDejada   = "Inform_Victima_Dejada";
    public static final String RefuseDejar    = "Refuse_Dejar_Victima";
    public static final String FailureDejar   = "Failure_Dejar_Victima";

  }

}
