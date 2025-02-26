package agenda.maskotas;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void mostrarMenu() {
        // TODO: Code to display a good-looking menu
    }

    public static String pedirString(String solicitud, boolean emptyStringValid) {
        String s;

        do {
            System.out.print(solicitud);
            s = scanner.nextLine();
        } while (!emptyStringValid && s.equals(""));

        return s;
    }

    public static int pedirInt(String solicitud) {
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

    public static boolean confirmarSeleccion(String mensajeConfirmacion) {
        String confirmacion = "";

        while (!"S".equals(confirmacion) && !"N".equals(confirmacion)) {
            confirmacion = pedirString(mensajeConfirmacion, false);
        }

        return confirmacion.equals("S");
    }

    public static void main(String[] args) {
        enum PERSONAS {
            CLIENTE, EMPLEADO, PROVEEDOR
        }

        boolean running = true, seleccionandoTabla = true, modificandoTabla = false;
        int opcion = -1, tablaElegida = -1, registroElegido;
        ArrayList<ArrayList<Persona>> personas = new ArrayList<>();
        // Datos de persona
        String nombre, apellidos, direccion, email, observaciones;
        int telefono;

        while (running) {
            // TODO: Add visuals for a good menu

            // TODO: Ask the user whether they want to load a file or create a new database
            // TODO: Ask for a path to the database to load
            // TODO: Change so it opens a window to select the file to load instead
            // TODO: Load the lists from file here
            
            personas.add(new ArrayList<>());
            personas.add(new ArrayList<>());
            personas.add(new ArrayList<>());

            /**
             * *******************************
             * Menú de selección de tablas ********************************
             */
            while (seleccionandoTabla) {
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
                } else {
                    tablaElegida = opcion;
                    // Se eligio una tabla, pasar al menú de modificación
                    modificandoTabla = true;
                }

                /**
                 * *******************************
                 * Menú de modificación de tablas
                 * ********************************
                 */
                while (modificandoTabla) {
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
                            if (personas.get(tablaElegida).isEmpty()) {
                                System.out.println("No existe ningún registro.");
                                break;
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
                                                s += " -> " + direccion + "\n";
                                            }
                                            
                                            s += "\nEmail: " + personaElegida.getEmail();
                                            if ((camposEditados & (1 << 3)) != 0) {
                                                s += " -> " + email + "\n";
                                            }

                                            s +=  "\nTeléfono: " + personaElegida.getTelefono();
                                            if ((camposEditados & (1 << 4)) != 0) {
                                                s += " -> " + telefono + "\n";
                                            }
                                            
                                            s +=  "\nObservaciones: " + (personaElegida.getObservaciones().equals("") ? "N/A" : personaElegida.getObservaciones());
                                            if ((camposEditados & (1 << 5)) != 0) {
                                                s +=  " -> " + (observaciones.equals("") ? "N/A" : observaciones) + "\n";
                                            }

                                            s += "\n";

                                            if (confirmarSeleccion(s + mensajeConfirmacion)) {
                                                personaElegida.setNombre(nombre);
                                                personaElegida.setApellidos(apellidos);
                                                personaElegida.setDireccion(direccion);
                                                personaElegida.setEmail(email);
                                                personaElegida.setTelefono(telefono);
                                                personaElegida.setObservaciones(observaciones);
                                            }
                                            else {
                                                break;
                                            }
                                        }

                                        done = true;
                                    }
                                    case 8 ->
                                        done = true;
                                    default -> {
                                    }
                                }
                            }
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
            }

            // TODO: Save lists to file here
        }
    }
}
