package Clases;

import javafx.scene.control.CheckBox;

public class Persona {

    private String documento;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String correo;
    private String telefono;
    private String genero;
    private CheckBox asistencia;

    public Persona() {
    }

    public Persona(String documento, String nombres, String apellidos) {
        this.documento = documento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.asistencia = new CheckBox();

    }

    public Persona(String documento, String nombres, String apellidos, CheckBox asistencia) {
        this.documento = documento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.asistencia = asistencia;
    }

    public Persona(String documento, String nombres, String apellidos, String direccion, String correo, String telefono, String genero) {
        this.documento = documento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.correo = correo;
        this.telefono = telefono;
        this.genero = genero;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public CheckBox getAsistencia() {

        return asistencia;
    }

    public boolean getVerificacion(){
        if(asistencia.isSelected()){
            return true;
        }else{
            return false;
        }
    }

    public CheckBox setAsistencia(boolean asistencia) {
        this.asistencia.setSelected(asistencia);
        return this.asistencia;
    }

    public boolean isLleno(){
        if(this.getDocumento().equals("")||
        this.getNombres().equals("")|| this.getApellidos().equals("")||
        this.getDireccion().equals("") || this.getTelefono().equals("") ||
        this.getGenero().equals("")){
            return false;
        }else{
            return true;
        }
    }

}
