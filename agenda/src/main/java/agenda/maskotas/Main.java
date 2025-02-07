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
        System.out.print(solicitud);

        // TODO: Add exception catching
        int n = scanner.nextInt();
        scanner.nextLine();

        return n;
    }

    public static void main(String[] args) {
        boolean running = true, seleccionandoTabla = false, modificandoTabla = false;
        int opcion, tablaElegida = -1;
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Empleado> empleados = new ArrayList<>();
        ArrayList<Proveedor> proveedores = new ArrayList<>();
        // Datos de persona
        String nombre, apellidos, direccion, email, observaciones;
        int telefono;

        while (running) {
            // TODO: Add visuals for a good menu

            // TODO: Ask the user whether they want to load a file or create a new database
            // TODO: Ask for a path to the database to load
            // TODO: Change so it opens a window to select the file to load instead
            // TODO: Load the lists from file here
            
            /*********************************
            * Menú de selección de tablas
            **********************************/
            while (seleccionandoTabla) {
                System.out.println("Tablas:");
                System.out.println("1) Cliente");
                System.out.println("2) Proveedor");
                System.out.println("3) Empleado");
                System.out.println("4) Volver atrás");
                opcion = pedirInt("¿Qué tabla desea modificar? (1-4): ");

                if (opcion == 4) {
                    seleccionandoTabla = false;
                } else {
                    tablaElegida = opcion;
                    // Se eligio una tabla, pasar al menú de modificación
                    modificandoTabla = true;
                }

                /*********************************
                * Menú de modificación de tablas
                **********************************/
                while (modificandoTabla) {
                    System.out.println("Opciones:");
                    System.out.println("1) Ingresar registro");
                    System.out.println("2) Editar registro");
                    System.out.println("3) Borrar registro");
                    System.out.println("4) Volver");
                    opcion = pedirInt("¿Qué desea hacer? (1-4): ");

                    switch (opcion) {
                        case 1 -> {
                            switch (tablaElegida) {
                                case 1 -> {
                                    System.out.println("Ingrese los datos del cliente:");
                                }
                                case 2 -> {
                                    System.out.println("Ingrese los datos del empleado:");
                                }
                                case 3 -> {
                                    System.out.println("Ingrese los datos del proveedor:");
                                }
                            }

                            nombre = pedirString("Nombre: ", false);
                            apellidos = pedirString("Apellidos: ", false);
                            direccion = pedirString("Dirección: ", false);
                            email = pedirString("Correo electrónico: ", false);
                            telefono = pedirInt("Número de teléfono: ");
                            observaciones = pedirString("Observación (opcional): ", true);
                            
                            switch (tablaElegida) {
                                case 1 -> {
                                    clientes.add(new Cliente(nombre, apellidos, direccion, email, telefono, observaciones));
                                }
                                case 2 -> {
                                    empleados.add(new Empleado(nombre, apellidos, direccion, email, telefono, observaciones));
                                }
                                case 3 -> {
                                    proveedores.add(new Proveedor(nombre, apellidos, direccion, email, telefono, observaciones));
                                }
                            }

                            System.out.println("Registro agregado correctamente.");
                        }
                        case 2 -> {
                        }
                        case 3 -> {
                        }
                        case 4 -> {
                            modificandoTabla = false;
                        }
                    }
                }
            }

            // TODO: Save lists to file here
        }
    }
}
