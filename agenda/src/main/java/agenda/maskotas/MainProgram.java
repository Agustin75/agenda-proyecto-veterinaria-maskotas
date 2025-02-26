package agenda.maskotas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// TODO: Add code to account for chosing "Borrar registro" when no field exists
public class MainProgram {
    private final Scanner scanner = new Scanner(System.in);

    enum PERSONAS {
        CLIENTE, EMPLEADO, PROVEEDOR
    }

    boolean running = true, seleccionandoTabla = true, modificandoTabla = false;
    int opcion = -1, tablaElegida = -1, registroElegido;
    ArrayList<ArrayList<Persona>> personas = new ArrayList<>();
    // Datos de persona
    String nombre, apellidos, direccion, email, observaciones;
    int telefono;

//#region Helper Funcions
    public void mostrarMenu() {
        // TODO: Code to display a good-looking menu
    }

    public String pedirString(String solicitud, boolean emptyStringValid) {
        String s;

        do {
            System.out.print(solicitud);
            s = scanner.nextLine();
        } while (!emptyStringValid && s.equals(""));

        return s;
    }

    public File pedirRuta(String solicitud, boolean emptyStringValid) {
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

                rutaValida = true;
            } catch (Exception e) {
                // TODO: Cambiar para que permita una ruta que no existe cuando se guarda la información
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

    public String getStringCamposEditados(Persona personaElegida, byte camposEditados) {
        String s = "Campos Editados:";

        s += "Nombre: " + personaElegida.getNombre();
        if ((camposEditados & 1) != 0) {
            s += " -> " + nombre;
        }

        s += "\nApellidos: " + personaElegida.getApellidos();
        if ((camposEditados & (1 << 1)) != 0) {
            s += " -> " + apellidos;
        }

        s += "\nDirección: " + personaElegida.getDireccion();
        if ((camposEditados & (1 << 2)) != 0) {
            s += " -> " + direccion;
        }

        s += "\nEmail: " + personaElegida.getEmail();
        if ((camposEditados & (1 << 3)) != 0) {
            s += " -> " + email;
        }

        s += "\nTeléfono: " + personaElegida.getTelefono();
        if ((camposEditados & (1 << 4)) != 0) {
            s += " -> " + telefono;
        }

        s += "\nObservaciones: " + (personaElegida.getObservaciones().equals("") ? "N/A" : personaElegida.getObservaciones());
        if ((camposEditados & (1 << 5)) != 0) {
            s += " -> " + (observaciones.equals("") ? "N/A" : observaciones);
        }

        s += "\n";

        return s;
    }
//#endregion

    public void mainMenu() {
        while (running) {
            personas.add(new ArrayList<>());
            personas.add(new ArrayList<>());
            personas.add(new ArrayList<>());

            // TODO: Add visuals for a good menu

            File f = pedirRuta("Ingrese la ruta de la agena a cargar (Deje el campo vacío para crear una nueva): ", true);

            // No se ingreso una ruta, crear una nueva Agenda
            if (f == null) {
                
            }

            // TODO: Ask for a path to the database to load
            // TODO: Change so it opens a window to select the file to load instead
            // TODO: Load the lists from file here

            /**
             * *******************************
             * Menú de selección de tablas
             * ********************************
             */
            while (seleccionandoTabla) {
                menuTablas();
            }

            // Se creó una nueva Agenda, pedir la ruta completa dónde guardar el archivo (nombre y extensión incluidos)
            if (f == null) {
                f = pedirRuta("Ingrese la ruta donde guardar la información: ", false);
                try {
                    try (FileWriter fw = new FileWriter(f)) {
                        fw.append(Persona.getNextID() + "\n");

                        for (int i = 0; i < personas.size(); i++) {
                            // Informa que tipo de Persona se esta guardando
                            fw.append(PERSONAS.values()[i].toString() + "\n");

                            for (Persona persona : personas.get(i)) {
                                fw.append(String.format("%s,%s,%s,%s,%s,%s,%s\n", persona.getId(), persona.getNombre(), persona.getApellidos(), persona.getDireccion(), persona.getEmail(), persona.getTelefono(),persona.getObservaciones()));
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
        System.out.println("Tablas:");
        System.out.println("1) Cliente");
        System.out.println("2) Empleado");
        System.out.println("3) Proveedor");
        System.out.println("4) Volver atrás");
        while (opcion < 1 || opcion > 4) {
            opcion = pedirInt("¿Qué tabla desea modificar? (1-4): ");
        }

        if (opcion == 4) {
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
        System.out.println("Opciones:");
        System.out.println("1) Mostrar registros");
        System.out.println("2) Ingresar registro");
        System.out.println("3) Editar registro");
        System.out.println("4) Borrar registro");
        System.out.println("5) Volver atrás");
        opcion = pedirInt("¿Qué desea hacer? (1-5): ");

        switch (opcion) {
            case 1 -> {
                if (personas.get(tablaElegida).isEmpty()) {
                    System.out.println("No hay ningún registro en esta tabla.");
                    break;
                }

                for (Persona persona : personas.get(tablaElegida)) {
                    System.out.println("1) " + persona.obtenerInformacion());
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
            System.out.println("No existe ningún registro.");
            return;
        }

        System.out.println("Registros:");
        for (Persona persona : personas.get(tablaElegida)) {
            System.out.println("1) " + persona.obtenerInformacion());
        }

        registroElegido = switch (personas.get(tablaElegida).size()) {
            case 1 ->
                0;
            default ->
                pedirInt("Selecciona el registro a modificar (1-" + personas.get(tablaElegida).size() + "): ") - 1;
        };
        boolean done = false;
        byte camposEditados = 0;

        Persona personaElegida = personas.get(tablaElegida).get(registroElegido);
        nombre = personaElegida.getNombre();
        apellidos = personaElegida.getApellidos();
        direccion = personaElegida.getDireccion();
        email = personaElegida.getEmail();
        telefono = personaElegida.getTelefono();
        observaciones = personaElegida.getObservaciones();

        while (!done) {
            System.out.println("Campos:");
            System.out.println("1) Nombre");
            System.out.println("2) Apellidos");
            System.out.println("3) Dirección");
            System.out.println("4) Email");
            System.out.println("5) Teléfono");
            System.out.println("6) Observaciones");
            System.out.println("7) Guardar y volver atrás");
            System.out.println("8) Cancelar y volver atrás");
            opcion = pedirInt("¿Qué campo desea modificar? (1-7): ");

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

                        String s = getStringCamposEditados(personaElegida, camposEditados);

                        if (confirmarSeleccion(s + mensajeConfirmacion)) {
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

                        String s = getStringCamposEditados(personaElegida, camposEditados);

                        if (confirmarSeleccion(s + mensajeConfirmacion)) {
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
