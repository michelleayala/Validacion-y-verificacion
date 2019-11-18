package Clases;

public class Usuario {

    private String documento;
    private String contraseña;
    private int estado;
    private int rol;


    public Usuario() {
    }

    public Usuario(String documento, String contraseña, int estado, int rol) {
        this.documento = documento;
        this.contraseña = contraseña;
        this.estado = estado;
        this.rol = rol;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public boolean isLleno(){
        if(this.getDocumento().equals("")||
        this.getContraseña().equals("")){
            return false;
        }else{
            return true;
        }
    }

}
