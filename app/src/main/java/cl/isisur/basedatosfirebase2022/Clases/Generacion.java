package cl.isisur.basedatosfirebase2022.Clases;

public class Generacion {
    private String idGeneracion;
    private String nombre;
    private String estado;

    public Generacion() {
        this.idGeneracion = "";
        this.nombre = "";
        this.estado = "";
    }

    public Generacion(String idGeneracion, String nombre, String estado) {
        this.idGeneracion = idGeneracion;
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getIdGeneracion() {
        return idGeneracion;
    }

    public void setIdGeneracion(String idGeneracion) {
        this.idGeneracion = idGeneracion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Generacion{" +
                "idGeneracion='" + idGeneracion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
