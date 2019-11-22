# Rescate Tablero

Este repositorio contiene el código fuente del Agente Tablero correspondiente al juego de mesa _Rescate_ desarrollado por la clase de Colmenarejo de Inteligencia Artificial en las Organizaciones durante el curso 2019-2020.

A continuación se incluyen las instrucciones necesarias para ejecutar dicho código, así como los pasos a seguir para realizar contribuciones al mismo.

## Contenidos

- [Rescate Tablero](#rescate-tablero)
  - [Contenidos](#contenidos)
  - [Ejecutando el código](#ejecutando-el-c%c3%b3digo)
    - [Prerrequisitos](#prerrequisitos)
    - [Compilando e iniciando JADEX](#compilando-e-iniciando-jadex)
      - [Desde la consola de comandos](#desde-la-consola-de-comandos)
      - [Usando Visual Studio Code (Recomendado)](#usando-visual-studio-code-recomendado)
      - [Usando Eclipse IDE](#usando-eclipse-ide)
    - [Lanzando el agente](#lanzando-el-agente)
  - [Contribuyendo al código](#contribuyendo-al-c%c3%b3digo)

## Ejecutando el código

### Prerrequisitos

- Instala la última versión de Java 1.8 (u231 en el momento de escribir este documento)
- Descarga el repositorio y descomprimelo en un directorio en tu PC
- (_Opcional_) [Visual Studio Code](https://code.visualstudio.com)
- (_Opcional_) [Eclipse IDE](https://www.eclipse.org/downloads/)

### Compilando e iniciando JADEX

#### Desde la consola de comandos

Una vez situados en el directorio del repositorio, encontraremos dos _scripts_: `compilar` y `ejecutar`. Se incluyen tanto la versión para Windows (`.bat`) como la usada por terminales en sistemas UNIX (`.sh`). Para compilar, ejecuta dicho script. Una vez compilado, ejecuta el otro script y se lanzará el entorno JADEX.

#### Usando Visual Studio Code (Recomendado)

Abre el archivo `RescateTablero.code-workspace` con Visual Studio Code. Asegurate de que el paquete de extensiones de Java está instalado y que tienes correctamente configurado el _path_ de Java. Para ello abre los ajustes de VSC y busca "Java home". Aparecerá un link para editar `settings.json`. En este archivo busca una línea similar a la siguiente (si no existe tal línea, añádela) y asegurate de que indica el directorio correcto en el que se encuentra tu JDK

```json
{
	//...
	"java.home": "C:\\Program Files\\Java\\jdk1.8.0_231"
	//...
}
```

Una vez VSC sepa dónde encontrar Java, pulsa F5 (ejecutar el modo Debug) y el entorno JADEX se ejecutará.

> NOTA: En Windows, asegurate de que la configuración que se está lanzando es la indicada como "Externa". Esto previene problemas causados cuando la terminal integrada de VSC no se corresponde con CMD.

#### Usando Eclipse IDE

> TODO: configurar el proyecto para hacerlo fácilmente ejecutable desde Eclipse

### Lanzando el agente

> TODO: describir los pasos para ejecutar los agentes

## Contribuyendo al código

> NOTA: Antes de nada, para poder contribuir, deberás ser parte de la organización.

1. ~Aignate a una de las tareas pendientes en el [documento correspondiente](https://docs.google.com/spreadsheets/d/1UepllTSWQi2oH7iajYn6p4yURnyUvm-_o1md8dRoFBc)~
2. Clona el repositorio en local
3. Crea una nueva rama para tus cambios y apunta el nombre de dicha rama en el doc de tareas
4. En la rama, haz los cambios y _commits_ necesarios para completar la tarea
5. Sube (haz _push_) la rama al repositorio
6. Crea una _Pull Request_ sobre la rama `develop`
7. Espera hasta que alguien más revise tus cambios
   - Mientras esperas, puedes volver al paso 1 y hacer otra tarea
   - Si los cambios no son aceptados, vuelve al paso 4
8. Espera a que se haga _merge_ de la rama
   - Mientras esperas, puedes volver al paso 1 y hacer otra tarea
9. ~Apunta la tarea como completada en el doc señalando "Merge" como "sí"~
