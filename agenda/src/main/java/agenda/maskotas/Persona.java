package agenda.maskotas;

public class Persona {
    private String nombre, apellidos, direccion, email, observaciones;
    private int telefono;

    public Persona(String nombre, String apellidos, String direccion, String email, int telefono, String observaciones) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
        this.observaciones = observaciones;
    }
    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public int getTelefono() {
        return telefono;
    }
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    /*
     * Métodos
     */
    public String obtenerInformacion() {
        String s = "";

        s += "Nombre: " + getNombre();
        s += " - Apellidos: " + getApellidos();
        s += " - Dirección: " + getDireccion();
        s += " - Email: " + getEmail();
        s += " - Teléfono: " + getTelefono();
        s += " - Observaciones: " + getObservaciones();

        return s;
    }
}
