package Registro;


import Clases.Persona;
import Clases.Usuario;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javafx.scene.input.KeyEvent;
import Inicio.Conexion;
import Inicio.Main;

import javax.swing.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class Registro implements Initializable {


@FXML
    public TextField Documento = null;
    public TextField Nombres = null;
    public TextField Apellidos = null;
    public TextField Direccion = null;
    public TextField Telefono = null;
    public TextField Correo = null;
    public ComboBox Genero = null;
    public Button Registarse = null;

    public Registro(){
    }


    private void MostrarCbx () {

        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;


        try {
            st = cnx.createStatement();
            ResultSet result = st.executeQuery("select Descripcion from tblsexo ");
            while (result.next()) {

                Genero.getItems().addAll((String) result.getString("Descripcion"));

            }
            cnx.close();
        } catch (SQLException ex) {
            System.err.println("error1");
        }

    }

    public void BorderColor_Documento(){
        Documento.setStyle(null);
    }

    public void Limpiar(){
        Documento.clear();
        Nombres.clear();
        Apellidos.clear();
        Direccion.clear();
        Telefono.clear();
        Correo.clear();
        Genero.getItems().clear();
    }
    @FXML
    private void ftnSoloNumeros(KeyEvent e){
        char key = e.getCharacter().charAt(0);
        if(Character.isLetter(key) || Character.isSpaceChar(key))
            e.consume();
    }
    @FXML
    private void ftnSoloLetras(KeyEvent e){
        char key = e.getCharacter().charAt(0);
        if(Character.isDigit(key)){
            e.consume();
        }
    }

    @FXML
    private void ftnValidacionDocumento(KeyEvent e){
        if(Documento.getText().length()==15){
            e.consume();
        }
        ftnSoloNumeros(e);
    }

    @FXML
    private void ftnValidacionNombresApelldios(KeyEvent e){
        if(Nombres.getText().length()==50 || Apellidos.getText().length()==50 ){
            e.consume();
        }
        ftnSoloLetras(e);
    }

    @FXML
    private void ftnValidacionDireccionCorreo(KeyEvent e){
        if(Direccion.getText().length()==50 || Correo.getText().length()==50){
            e.consume();
        }
    }

    @FXML
    private void ftnValidacionTelefono(KeyEvent e){
        if(Telefono.getText().length()==10){
            e.consume();
        }
        ftnSoloNumeros(e);
    }


    private void buscarPersona(){
        //Instacias para la conexion de la base de datos
        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;
        int dato = 0;
        String A = Documento.getText();
        try {
            st = cnx.createStatement();
            ResultSet result = st.executeQuery("select * from tblpersonas where PKId = '"+A+"'");
            while (result.next()) {
                dato++;
            }
            cnx.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        if(dato>0){
            //Existe la Persona
            Documento.setStyle("-fx-border-color: red;");
        }
    }

    public void ftnGuardarBD() throws SQLException {
        ftnCrearPersona();
    }

    private void ftnCrearPersona() throws SQLException {
        //Instancias para la conexión de la base de datos.
        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;

        Persona persona = new Persona(); //Instancia de la clase personas.
        int dato = 0; //Cantidad de resultados.


        //Parametros de la clase Persona tomados de los campos de interfaz.
        persona.setDocumento(Documento.getText().trim());
        persona.setNombres(Nombres.getText());
        persona.setApellidos(Apellidos.getText());
        persona.setDireccion(Direccion.getText());
        persona.setCorreo(Correo.getText().trim());
        persona.setTelefono(Telefono.getText().trim());
        String cb = (String) Genero.getValue();
        persona.setGenero(String.valueOf(Genero.getItems().indexOf(cb) + 1));

        String col = "123"; //Nit del colegio.

        //Valida que todos los campos esten llenos
        if (persona.isLleno() == false) {
            JOptionPane.showMessageDialog(null, "Llene todos los campos", "Campos vacíos", 2);
        } else { //Si todos los campos estan llenos entonces...
            try { //Busca cuantos resultados se pueden encontrar con el documento de la Persona.
                st = cnx.createStatement();
                ResultSet result = st.executeQuery("select * from tblpersonas where PKId = '" + persona.getDocumento() + "'");
                while (result.next()) {
                    dato++;
                }
            } catch (SQLException ex) {
                System.err.println(ex);
            }

            if (dato > 0) { //Existe la personas
                Documento.setStyle("-fx-border-color: red;");
            } else { //Si la Persona no existe entonces...
                //Agrega en la tabla Persona
                String sql = "INSERT INTO tblpersonas VALUES(?,?,?,?,?,?,?,?)";

                try { //Valores que se agregaran en la table personas.
                    PreparedStatement pst = cnx.prepareStatement(sql);
                    pst.setString(1, persona.getDocumento());
                    pst.setString(2, persona.getNombres());
                    pst.setString(3, persona.getApellidos());
                    pst.setString(4, persona.getDireccion());
                    pst.setString(5, persona.getCorreo());
                    pst.setString(6, persona.getTelefono());
                    pst.setInt(7, (Integer.parseInt(persona.getGenero())));
                    pst.setString(8, col);

                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Persona creada");

                    //Creación de Usuario
                    ftnCrearUsuario();

                } catch (SQLException ex) {
                    System.out.println(ex);

                }
            }
        }
        cnx.close();
    }

    private void ftnCrearUsuario() throws SQLException {
        //Instancias para la conexión de la base de datos.
        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;
        int dato = 0; //Cantidad de resultados.

        //Parametros de la clase Usuario tomados de los campos de interfaz.
        Usuario usuario = new Usuario();
        usuario.setDocumento(Documento.getText());
        usuario.setContraseña("docente");
        usuario.setEstado(1);
        usuario.setRol(1);

        try {//Busca cuantos resultados se pueden encontrar con el documento de la Persona.
            st = cnx.createStatement();
            ResultSet result = st.executeQuery("select * from tblusuarios where PKId_TblPersonas = '"+usuario.getDocumento()+"'");
            while (result.next()) {
                dato++;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        if(dato>0){//Si encuentra almenos un resultado
            JOptionPane.showMessageDialog(null, "Este Usuario ya existe", "Usuario ya existente", 2);
        }else{//Si no hay resultados entonces...
            //Agregaen la tabla usuarios.
            String sql = "INSERT INTO tblusuarios (PKId_TblPersonas, Contraseña, FKCodigo_TblEstados, FKId_TblRoles)" +
                    "VALUES(?,?,?,?)";

            try {//Valores que se agregaran en la table usuarios.
                PreparedStatement pst = cnx.prepareStatement(sql);
                pst.setString(1, usuario.getDocumento());
                pst.setString(2, usuario.getContraseña());
                pst.setInt(3, usuario.getEstado());
                pst.setInt(4, usuario.getRol());

                pst.executeUpdate();

                System.out.println("Usuario creado");
                Limpiar();


            } catch (SQLException ex) {
                System.out.println(ex);

            }
        }
        cnx.close();
    }


    public void MostrarInicioSesion() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Inicio/login.fxml"));

        Scene escenaAdmin = new Scene(root);
        Main.MainStage.setScene(escenaAdmin);
    }

    public void cerrarFormulario(){
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MostrarCbx();

        Documento.focusedProperty().addListener((obs, oldVal, newVal) ->
                buscarPersona());

    }

}
