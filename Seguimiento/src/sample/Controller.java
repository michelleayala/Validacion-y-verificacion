package sample;

import Clases.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

public class Controller {

    private Parent Registro = FXMLLoader.load(getClass().getResource("../Registro/DiseñoRegistro.fxml"));
    private Scene escenaRegistro = new Scene(Registro);

    private Parent admin = FXMLLoader.load(getClass().getResource("../sesion/diseñoAdmin2.fxml"));
    private Scene escenaAdmin = new Scene(admin);

    private Stage LoginStage = new Stage();

    @FXML
    public TextField txtUsuario = null;
    public PasswordField txtContraseña = null;
    public Button btnIngresar = null;


    public Controller() throws IOException {
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

    public void ftnGuardarBD(){
        //Instancias para la conexion a la base de datos.
        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;
        int dato = 0;//Cantidad de resultados.

        //Parametros de la clase Usuario tomados de los campos de interfaz.
        Usuario usuario = new Usuario();
        usuario.setDocumento(txtUsuario.getText().trim());
        usuario.setContraseña(txtContraseña.getText());

        if(usuario.isLleno()==false){//Verifica si los campos estan vacios.
            JOptionPane.showMessageDialog(null, "Llene todos los campos", "Campos vacíos", 2);

            if(txtUsuario.getText().isEmpty()){//Verifica si el campo Usuario esta vacio
                txtUsuario.setStyle("-fx-border-color: red;");
            }

            if(txtContraseña.getText().isEmpty()){//Verifica si el campo contraseña esta vacio
                txtContraseña.setStyle("-fx-border-color: red;");
            }

        }else{//Si todos los campos estan llenos entonces...
            try {//Busca cuantos resultados se pueden encontrar del Usuario.
                st = cnx.createStatement();
                ResultSet result = st.executeQuery("select * from tblusuarios where PKId_TblPersonas = '"+usuario.getDocumento()+"'");
                while (result.next()) {
                    dato++;
                }
            } catch (SQLException ex) {
                System.err.println(ex);
            }

            if(dato>0){//Si existe el Usuario
                //Cierra el stage principal, crea uno nuevo y agrega la scena de administrador
                Main.MainStage.close();

                LoginStage.initStyle(StageStyle.UNDECORATED);
                //LoginStage.setMaximized(true);
                LoginStage.setScene(escenaAdmin);
                LoginStage.show();

            }else {//Si no existe el Usuario
                JOptionPane.showMessageDialog(null, "El Usuario no existe", "Usuario inexistente", 2);
            }
        }
    }

    public void Guadar_Enter(KeyEvent e){
        if(e.getCode().toString().equals("ENTER"))
        {
            ftnGuardarBD();
        }
    }

    public void MostrarRegistro (){

       Main.MainStage.setScene(escenaRegistro);

    }

    public void cerrarFormulario(){
        System.exit(0);
    }
}
