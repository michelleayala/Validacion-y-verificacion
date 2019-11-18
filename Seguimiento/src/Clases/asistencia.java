package Clases;

import java.time.LocalDate;

public class asistencia {

    private String documentoPersona;
    private String nombresPersonas;
    private String apellidosPersonas;
    private LocalDate fecha;
    private Boolean verificacion;
    private int codigoGrado_Grupo;

    public asistencia(){}

    public asistencia(String apellidosPersonas) {
        this.apellidosPersonas = apellidosPersonas;
    }

    public asistencia(String documentoPersona, String nombresPersonas, String apellidosPersonas, LocalDate fecha, Boolean verificacion, int codigoGrado_Grupo) {
        this.documentoPersona = documentoPersona;
        this.nombresPersonas = nombresPersonas;
        this.apellidosPersonas = apellidosPersonas;
        this.fecha = fecha;
        this.verificacion = verificacion;
        this.codigoGrado_Grupo = codigoGrado_Grupo;
    }

    public asistencia(String documentoPersona, String nombresPersonas, String apellidosPersonas) {
        this.documentoPersona = documentoPersona;
        this.nombresPersonas = nombresPersonas;
        this.apellidosPersonas = apellidosPersonas;
    }

    public String getDocumentoPersona() {
        return documentoPersona;
    }

    public void setDocumentoPersona(String documentoPersona) {
        this.documentoPersona = documentoPersona;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }


    public int getCodigoGrado_Grupo() {
        return codigoGrado_Grupo;
    }

    public void setCodigoGrado_Grupo(int codigoGrado_Grupo) {
        this.codigoGrado_Grupo = codigoGrado_Grupo;
    }

    public String getNombresPersonas() {
        return nombresPersonas;
    }

    public void setNombresPersonas(String nombresPersonas) {
        this.nombresPersonas = nombresPersonas;
    }

    public String getApellidosPersonas() {
        return apellidosPersonas;
    }

    public void setApellidosPersonas(String apellidosPersonas) {
        this.apellidosPersonas = apellidosPersonas;
    }

    public Boolean getVerificacion() {
        return verificacion;
    }

    public void setVerificacion(Boolean verificacion) {
        this.verificacion = verificacion;
    }

}
