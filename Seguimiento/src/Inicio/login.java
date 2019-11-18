package Inicio;

import Clases.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

public class login {

    @FXML
    public TextField txtUsuario = null;
    public PasswordField txtContraseña = null;
    public Button btnIngresar = null;

    private double xOffset = 0;
    private double yOffset = 0;

    public login() throws IOException {
    }
    @FXML
    private void ftnSoloNumeros(KeyEvent e){
        char key = e.getCharacter().charAt(0);
        if(Character.isLetter(key) || Character.isSpaceChar(key))
            e.consume();
    }
    @FXML //Valida la longitud maxima y solo acepta numeros.
    private void ftnValidacionUsuario(KeyEvent e){
        if(txtUsuario.getText().length()==15){
            e.consume();
        }
        ftnSoloNumeros(e);
    }

    @FXML //Valida la longitud maxima.
    private void ftnValidacionContraseña(KeyEvent e){
        if(txtContraseña.getText().length()==20){
            e.consume();
        }
    }

    public void BorderColor_Usuario(){
        txtUsuario.setStyle("-fx-border-color:  #1A84F4;");
    }

    public void BorderColor_Password(){
        txtContraseña.setStyle("-fx-border-color:  #1A84F4;");
    }

    private void cargarEscena(String parent) throws IOException {
        Parent admin = FXMLLoader.load(getClass().getResource(parent + ".fxml"));
        Scene escena = new Scene(admin);

        Main.MainStage.close();
        Main.appStage.initStyle(StageStyle.UNDECORATED);
        escena.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        escena.setOnMouseDragged(event -> {
            Main.appStage.setX(event.getScreenX() - xOffset);
            Main.appStage.setY(event.getScreenY() - yOffset);
        });
        Main.appStage.setScene(escena);
        Main.appStage.show();
    }

    private void sesion(){
        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;
        try {//Busca cuantos resultados se pueden encontrar del Usuario.
            st = cnx.createStatement();
            ResultSet result = st.executeQuery("SELECT tblusuarios.PKId_TblPersonas as 'usuario', tblusuarios.Password as 'pass', tblusuarios.FKCodigo_TblEstados 'estado', tblusuarios.FKId_TblRoles as 'rol' from tblusuarios\n" +
                    "WHERE tblusuarios.PKId_TblPersonas='" + txtUsuario.getText() + "' and tblusuarios.Password='" + txtContraseña.getText() + "' and\n" +
                    "tblusuarios.FKCodigo_TblEstados= 1");
            if (result.next()) {
                //Si existe el Usuario
                //Cierra el stage principal, crea uno nuevo y agrega la scena de administrador

                if(result.getInt(4)==1){ //Usuario administrador
                    cargarEscena("../admin/principal");
                    System.out.println(result.getInt(4));
                }else if(result.getInt(4)==2){ //Usuario docente
                    cargarEscena("../docente/principal");
                }else if(result.getInt(4)==3){ //Usuario estudiante
                    System.out.println("Estudiante");
                    cargarEscena("../Estudiante/principalEstudiante");
            }
            }else{ //Arreglar
               /* ResultSet result2 = st.executeQuery("SELECT tblusuarios.PKId_TblPersonas as 'usuario', tblusuarios.FKCodigo_TblEstados 'estado'from tblusuarios\n" +
                        "WHERE tblusuarios.PKId_TblPersonas='" + txtUsuario.getText() + "' and\n" +
                        "tblusuarios.FKCodigo_TblEstados= 1");

                if (result2.next()){
                    System.out.println("contraseña no valido");
                    txtContraseña.setStyle("-fx-border-color: red;");
                }
                if(result2.next()){
                    System.out.println("usuario no valido");
                }*/

            }

        } catch (SQLException | IOException ex) {
            System.err.println(ex);
        }
    }

    @FXML
    private void ftnGuardarBD(){
        //Parametros de la clase Usuario tomados de los campos de interfaz.
        Usuario usuario = new Usuario();
        usuario.setDocumento(txtUsuario.getText().trim());
        usuario.setContraseña(txtContraseña.getText());

        if(usuario.isLleno()==false){//Verifica si los campos estan vacios.

            if(txtUsuario.getText().isEmpty()){//Verifica si el campo Usuario esta vacio
                txtUsuario.setStyle("-fx-border-color: red;");
            }

            if(txtContraseña.getText().isEmpty()){//Verifica si el campo contraseña esta vacio
                txtContraseña.setStyle("-fx-border-color: red;");
            }

        }else{//Si todos los campos estan llenos entonces...
            sesion();
        }
    }

    public void Guadar_Enter(KeyEvent e){
        if(e.getCode().toString().equals("ENTER"))
        {
            ftnGuardarBD();
        }
    }

    public void MostrarRegistro () throws IOException {
        Parent Registro = FXMLLoader.load(getClass().getResource("../Registro/DiseñoRegistro.fxml"));
        Scene escenaRegistro = new Scene(Registro);
       Main.MainStage.setScene(escenaRegistro);

    }

    public void cerrarFormulario(){
        System.exit(0);
    }
}
