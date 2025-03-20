package agenda.maskotas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainProgram {
    private final Scanner scanner = new Scanner(System.in);

    enum PERSONAS {
        CLIENTE, EMPLEADO, PROVEEDOR
    }

    boolean running = true, seleccionandoTabla = true, modificandoTabla = false;
    int opcion = -1, tablaElegida = -1, registroElegido;
    ArrayList<ArrayList<Persona>> personas = new ArrayList<>();
    String rutaCargada = "";
    ArrayList<String> menuTablas = new ArrayList<>();
    ArrayList<String> menuModificacionTablas = new ArrayList<>();
    ArrayList<String> menuModificacionRegistro = new ArrayList<>();

    // Datos de persona
    String nombre, apellidos, direccion, email, observaciones;
    int telefono;

//#region Helper Funcions
    public void mostrarMenu(ArrayList<String> opciones) {
        if (opciones.isEmpty()) {
            return;
        }

        // Tamaño mínimo del menú, arbitrario
        int size = 35;
        String barraMenu = "";

        // Hacer el menú tan grande como la opción más larga, más el padding ("|| " al principio y " ||" al final)
        for (String currOpcion : opciones) {
            if (currOpcion.length() + 6 > size) {
                size = currOpcion.length() + 6;
            }
        }

        for (int i = 0; i < size; i++) {
            barraMenu += "=";
        }

        System.out.println(barraMenu);

        for (String currOpcion : opciones) {
            System.out.print("|| " + currOpcion + " ");

            for (int i = currOpcion.length() + 4; i < size - 2; i++) {
                System.out.print(" ");
            }

            System.out.println("||");
        }

        System.out.println(barraMenu);
    }

    public String pedirString(String solicitud, boolean emptyStringValid) {
        String s;

        do {
            System.out.print(solicitud);
            s = scanner.nextLine();
        } while (!emptyStringValid && s.equals(""));

        return s;
    }

    public File pedirRuta(String solicitud, boolean emptyStringValid, boolean rutaDebeExistir) {
        String s;
        File f = null;
        boolean rutaValida = false;

        while (!rutaValida) {
            do {
                System.out.print(solicitud);
                s = scanner.nextLine();
            } while (!emptyStringValid && s.equals(""));

            if (s.isBlank()) {
                return null;
            }

            try {
                f = new File(s);

                rutaValida = (rutaDebeExistir ? f.exists() : true);
            } catch (Exception e) {
                System.err.println("La ruta ingresada no es correcta. Por favor, intente de nuevo.");
            }
        }

        return f;
    }

    public int pedirInt(String solicitud) {
        int n = -1;
        boolean validInput = false;

        do {
            System.out.print(solicitud);

            try {
                n = scanner.nextInt();

                validInput = true;
            } catch (Exception e) {
                System.err.println("No se ingreso un valor correcto. Por favor, intente de nuevo.");
            } finally {
                scanner.nextLine();
            }
        } while (!validInput);

        return n;
    }

    public boolean confirmarSeleccion(String mensajeConfirmacion) {
        String confirmacion = "";

        while (!"S".equals(confirmacion) && !"N".equals(confirmacion)) {
            confirmacion = pedirString(mensajeConfirmacion, false);
        }

        return confirmacion.equals("S");
    }

    public ArrayList<String> getMenuCamposEditados(Persona personaElegida, byte camposEditados) {
        ArrayList<String> menu = new ArrayList<>();
        String s;

        menu.add("Campos Editados:");

        s = "Nombre: " + personaElegida.getNombre();
        if ((camposEditados & 1) != 0) {
            s += " -> " + nombre;
        }
        menu.add(s);

        s = "Apellidos: " + personaElegida.getApellidos();
        if ((camposEditados & (1 << 1)) != 0) {
            s += " -> " + apellidos;
        }
        menu.add(s);

        s = "Dirección: " + personaElegida.getDireccion();
        if ((camposEditados & (1 << 2)) != 0) {
            s += " -> " + direccion;
        }
        menu.add(s);

        s = "Email: " + personaElegida.getEmail();
        if ((camposEditados & (1 << 3)) != 0) {
            s += " -> " + email;
        }
        menu.add(s);

        s = "Teléfono: " + personaElegida.getTelefono();
        if ((camposEditados & (1 << 4)) != 0) {
            s += " -> " + telefono;
        }
        menu.add(s);

        s = "Observaciones: " + (personaElegida.getObservaciones().equals("") ? "N/A" : personaElegida.getObservaciones());
        if ((camposEditados & (1 << 5)) != 0) {
            s += " -> " + (observaciones.equals("") ? "N/A" : observaciones);
        }
        menu.add(s);

        return menu;
    }
//#endregion

    public void mainMenu() {
        personas.add(new ArrayList<>());
        personas.add(new ArrayList<>());
        personas.add(new ArrayList<>());

        menuTablas.add("Tablas:");
        menuTablas.add("1) Cliente");
        menuTablas.add("2) Empleado");
        menuTablas.add("3) Proveedor");

        menuModificacionTablas.add("Opciones:");
        menuModificacionTablas.add("1) Mostrar registros");
        menuModificacionTablas.add("2) Ingresar registro");
        menuModificacionTablas.add("3) Editar registro");
        menuModificacionTablas.add("4) Borrar registro");
        menuModificacionTablas.add("5) Volver atrás");

        menuModificacionRegistro.add("Campos:");
        menuModificacionRegistro.add("1) Nombre");
        menuModificacionRegistro.add("2) Apellidos");
        menuModificacionRegistro.add("3) Dirección");
        menuModificacionRegistro.add("4) Email");
        menuModificacionRegistro.add("5) Teléfono");
        menuModificacionRegistro.add("6) Observaciones");
        menuModificacionRegistro.add("7) Guardar y volver atrás");
        menuModificacionRegistro.add("8) Cancelar y volver atrás");

        while (running) {
            // TODO: Change so it opens a window to select the file to load instead
            File f;
            do {
                f = pedirRuta("Ingrese la ruta de la agenda a cargar (Deje el campo vacío para crear una nueva): ", true, true);

                if (f == null) {
                    break;
                } else if (f.isFile()) {
                    rutaCargada = f.getPath();
                    break;
                } else {
                    System.out.println("Por favor, ingrese la ruta a un archivo.");
                }
            } while (true);

            if (f != null) {
                try (Scanner fileScanner = new Scanner(f)) {
                    int nextId = fileScanner.nextInt();

                    Persona.setNextID(nextId);
                    fileScanner.nextLine();

                    String line;
                    int tabla = -1;
                    int idPersona;
                    while (fileScanner.hasNext()) {
                        line = fileScanner.nextLine();

                        try {
                            if (null != PERSONAS.valueOf(line)) {
                                switch (PERSONAS.valueOf(line)) {
                                    case CLIENTE -> {
                                        tabla = 0;
                                        continue;
                                    }
                                    case EMPLEADO -> {
                                        tabla = 1;
                                        continue;
                                    }
                                    case PROVEEDOR -> {
                                        tabla = 2;
                                        continue;
                                    }
                                    default -> {
                                    }
                                }
                            }
                        } catch (IllegalArgumentException e) {
                            // El string leído no es parte del enum, leer la línea de datos
                        }

                        if (tabla == -1) {
                            System.err.println("ERROR: El archivo no se puede leer ya que el formato no es correcto.");
                        } else {
                            String[] campos = line.split(",");
                            try {
                                idPersona = Integer.parseInt(campos[0]);
                                nombre = campos[1];
                                apellidos = campos[2];
                                direccion = campos[3];
                                email = campos[4];
                                telefono = Integer.parseInt(campos[5]);

                                if (campos.length == 6) {
                                    personas.get(tabla).add(new Persona(idPersona, nombre, apellidos, direccion, email, telefono, ""));

                                } else if (campos.length == 7) {
                                    observaciones = campos[6];
                                    personas.get(tabla).add(new Persona(idPersona, nombre, apellidos, direccion, email, telefono, observaciones));
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("Error en la línea \"" + line + "\" al intentar convertir un valor a número entero.");
                            }
                        }
                    }

                } catch (FileNotFoundException e) {
                    System.err.println("ERROR: La ruta especificada no es válida.");
                    return;
                }
            }

            /**
             * *******************************
             * Menú de selección de tablas
             * ********************************
             */
            while (seleccionandoTabla) {
                menuTablas();
            }

            if (confirmarSeleccion("¿Desea guardar los cambios realizados? (S/N): ")) {
                if (rutaCargada.isEmpty() || confirmarSeleccion("¿Desea guardar la base de datos en un archivo nuevo? (S/N): ")) {
                    // Se creó una nueva Agenda, pedir la ruta completa dónde guardar el archivo (nombre y extensión incluidos)
                    f = pedirRuta("Ingrese la ruta donde guardar la información: ", false, false);
                }
    
                try {
                    try (FileWriter fw = new FileWriter(f)) {
                        fw.append(Persona.getNextID() + "\n");

                        for (int i = 0; i < personas.size(); i++) {
                            // Informa que tipo de Persona se esta guardando
                            fw.append(PERSONAS.values()[i].toString() + "\n");

                            for (Persona persona : personas.get(i)) {
                                fw.append(String.format("%s,%s,%s,%s,%s,%s,%s\n", persona.getId(), persona.getNombre(), persona.getApellidos(), persona.getDireccion(), persona.getEmail(), persona.getTelefono(), persona.getObservaciones()));
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error creando archivo");
                }
            }
        }
    }

    public void menuTablas() {
        mostrarMenu(menuTablas);
        
        while (opcion < 0 || opcion > personas.size()) {
            opcion = pedirInt("¿Qué tabla desea modificar? (0 para cancelar): ");
        }

        if (opcion == 0) {
            seleccionandoTabla = false;
            running = false;
        } else {
            tablaElegida = opcion - 1;
            // Se eligio una tabla, pasar al menú de modificación
            modificandoTabla = true;
        }

        while (modificandoTabla) {
            menuModificacionTabla();
        }
    }

    /**
     * *******************************
     * Menú de modificación de tablas
     * ********************************
     */
    public void menuModificacionTabla() {
        mostrarMenu(menuModificacionTablas);

        opcion = pedirInt("¿Qué desea hacer? (1-5): ");

        switch (opcion) {
            case 1 -> {
                if (personas.get(tablaElegida).isEmpty()) {
                    System.out.println("No hay ningún registro en esta tabla.");
                    break;
                }

                for (int i = 0; i < personas.get(tablaElegida).size(); i++) {
                    System.out.println((i + 1) + ") " + personas.get(tablaElegida).get(i).obtenerInformacion());
                }
            }
            case 2 -> {
                switch (PERSONAS.values()[tablaElegida]) {
                    case CLIENTE -> {
                        System.out.println("Ingrese los datos del cliente:");
                    }
                    case EMPLEADO -> {
                        System.out.println("Ingrese los datos del empleado:");
                    }
                    case PROVEEDOR -> {
                        System.out.println("Ingrese los datos del proveedor:");
                    }
                }

                nombre = pedirString("Nombre: ", false);
                apellidos = pedirString("Apellidos: ", false);
                direccion = pedirString("Dirección: ", false);
                email = pedirString("Correo electrónico: ", false);
                telefono = pedirInt("Número de teléfono: ");
                observaciones = pedirString("Observación (opcional): ", true);

                switch (PERSONAS.values()[tablaElegida]) {
                    case CLIENTE -> {
                        personas.get(tablaElegida).add(
                                new Cliente(nombre, apellidos, direccion, email, telefono, observaciones));
                    }
                    case EMPLEADO -> {
                        personas.get(tablaElegida).add(
                                new Empleado(nombre, apellidos, direccion, email, telefono, observaciones));
                    }
                    case PROVEEDOR -> {
                        personas.get(tablaElegida).add(new Proveedor(nombre, apellidos, direccion, email,
                                telefono, observaciones));
                    }
                }

                System.out.println("Registro agregado correctamente.");
            }
            case 3 -> {
                menuModificacionRegistro();
            }
            case 4 -> {
                if (personas.get(tablaElegida).isEmpty()) {
                    System.out.println("No hay ningún registro en esta tabla.");
                    break;
                }

                opcion = pedirInt("¿Qué registro desea borrar? (0 para cancelar): ") - 1;
                if (opcion != -1 && opcion >= 0 && opcion < personas.get(tablaElegida).size()) {
                    String mensajeConfirmacion = "¿Está seguro que desea eliminar el registro \""
                            + personas.get(tablaElegida).get(opcion).obtenerInformacion()
                            + "\"? (S/N): ";
                    if (confirmarSeleccion(mensajeConfirmacion)) {
                        personas.get(tablaElegida).remove(opcion);
                    }
                }
            }
            case 5 -> {
                modificandoTabla = false;
            }
        }
    }

    public void menuModificacionRegistro() {
        if (personas.get(tablaElegida).isEmpty()) {
            System.out.println("No hay ningún registro.");
            return;
        }
        
        ArrayList<Persona> referenciaATablaElegida = personas.get(tablaElegida);

        System.out.println("Registros:");
        for (int i = 0; i < referenciaATablaElegida.size(); i++) {
            System.out.println((i + 1) + ") " + referenciaATablaElegida.get(i).obtenerInformacion());
        }

        do {
            registroElegido = switch (referenciaATablaElegida.size()) {
                case 1 ->
                0;
                default ->
                pedirInt("Selecciona el registro a modificar (1-" + referenciaATablaElegida.size() + "): ") - 1;
            };
            if (registroElegido < 0 || registroElegido >= referenciaATablaElegida.size()) {
                System.out.println("ERROR: No se ingresó un registro válido.");
            }
        } while (registroElegido < 0 || registroElegido >= referenciaATablaElegida.size());

        boolean done = false;
        byte camposEditados = 0;

        Persona personaElegida = referenciaATablaElegida.get(registroElegido);
        nombre = personaElegida.getNombre();
        apellidos = personaElegida.getApellidos();
        direccion = personaElegida.getDireccion();
        email = personaElegida.getEmail();
        telefono = personaElegida.getTelefono();
        observaciones = personaElegida.getObservaciones();

        while (!done) {
            mostrarMenu(menuModificacionRegistro);

            opcion = pedirInt("¿Qué campo desea modificar? ");

            switch (opcion) {
                case 1 -> {
                    nombre = pedirString("Nombre: ", false);
                    if (!nombre.equals(personaElegida.getNombre())) {
                        camposEditados |= 1;
                    }
                }
                case 2 -> {
                    apellidos = pedirString("Apellidos: ", false);
                    if (!apellidos.equals(personaElegida.getApellidos())) {
                        camposEditados |= (1 << 1);
                    }
                }
                case 3 -> {
                    direccion = pedirString("Dirección: ", false);
                    if (!direccion.equals(personaElegida.getDireccion())) {
                        camposEditados |= (1 << 2);
                    }
                }
                case 4 -> {
                    // TODO: Add a match clause to make sure the String being added is an email
                    email = pedirString("Correo electrónico: ", false);
                    if (!email.equals(personaElegida.getEmail())) {
                        camposEditados |= (1 << 3);
                    }
                }
                case 5 -> {
                    telefono = pedirInt("Número de teléfono: ");
                    if (telefono != personaElegida.getTelefono()) {
                        camposEditados |= (1 << 4);
                    }
                }
                case 6 -> {
                    observaciones = pedirString("Observación: ", true);
                    if (!observaciones.equals(personaElegida.getObservaciones())) {
                        camposEditados |= (1 << 5);
                    }
                }
                case 7 -> {
                    if (camposEditados != 0) {
                        String mensajeConfirmacion = "¿Está seguro que desea sobreescribir los campos editados? (S/N): ";

                        mostrarMenu(getMenuCamposEditados(personaElegida, camposEditados));
                        
                        if (confirmarSeleccion(mensajeConfirmacion)) {
                            personaElegida.setNombre(nombre);
                            personaElegida.setApellidos(apellidos);
                            personaElegida.setDireccion(direccion);
                            personaElegida.setEmail(email);
                            personaElegida.setTelefono(telefono);
                            personaElegida.setObservaciones(observaciones);
                        } else {
                            break;
                        }
                    }

                    done = true;
                }
                case 8 -> {
                    if (camposEditados != 0) {
                        String mensajeConfirmacion = "¿Está seguro que desea descartar los cambios realizados? (S/N): ";

                        mostrarMenu(getMenuCamposEditados(personaElegida, camposEditados));

                        if (confirmarSeleccion(mensajeConfirmacion)) {
                            done = true;
                        }
                    }
                }
                default -> {
                }
            }
        }
    }
}
