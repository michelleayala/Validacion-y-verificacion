package docente;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import sample.Conexion;

import java.io.IOException;
import java.sql.*;

public class CreacionTareas {
    @FXML
    private ComboBox cbxEstados = new ComboBox();
    @FXML
    private ComboBox cbxGrado = new ComboBox();
    @FXML
    private  ComboBox cbxGrupo = new ComboBox();
    @FXML
    public TextField NombreTarea = null;
    @FXML
    public TextArea Descripcion = null;
    @FXML
    public DatePicker fecha = null;
    @FXML
    public Button Guardar = null;

    public CreacionTareas(){}

    @FXML
    private void cargarCbxGrado(){ //Se encarga de cargar los grados en el comboBox grado
        Inicio.Conexion con = new Inicio.Conexion();
        Connection cnx = con.conectar();
        Statement st;
        //Se crea un arreglo de tipo String donde se almacenará los resultados
        ObservableList<String> h = FXCollections.observableArrayList();

        try {
            st = cnx.createStatement();
            ResultSet result = st.executeQuery("select Descripcion from tblgrado " );
            while (result.next()) {
                h.add(result.getString("Descripcion")); //Registrar un valor en el arreglo
                cbxGrado.setItems(h); //Carga el comboBox
            }
            cnx.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    @FXML
    private void cargarCbxGrupo(){//Se encarga de cargar los grados en el comboBox grupo
        Inicio.Conexion con = new Inicio.Conexion();
        Connection cnx = con.conectar();
        Statement st;
        String gradoValue = String.valueOf(cbxGrado.getValue());
        //Se crea un arreglo de tipo String donde se almacenará los resultados
        ObservableList<String> h = FXCollections.observableArrayList();

        try {
            st = cnx.createStatement();
            ResultSet result = st.executeQuery("select tblgrupo.Descripcion as 'grupo', tblgrado.Descripcion as 'grado' from tblgrupo, tbldetgrados_grupos, tblgrado" +
                    " where tblgrupo.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrupo and tbldetgrados_grupos.FKCodigo_tblgrado = tblgrado.PKCodigo and tblgrado.Descripcion ="+ gradoValue);
            //Permite hacer una iteración mientras se cumpla el requisito de encontrar valores en la base de datos
            if(result.next()){
                do{
                    h.add(result.getString("grupo"));
                    cbxGrupo.setItems(h);
                }while (result.next());
            }else{ //Si no hay registros entonces limpia el comboBox
                cbxGrupo.getItems().clear();
            }

            cnx.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
    public void limpiar (){
        NombreTarea.clear();
        Descripcion.clear();
        fecha.setValue(null);
        cbxEstados.getItems().clear();
        cbxGrado.getItems().clear();
        cbxGrupo.getItems().clear();
    }

    @FXML
    private void ftnValidacionNombreTarea(KeyEvent e){
        if(NombreTarea.getText().length()==50){
            e.consume();
        }
        ftnSoloLetras(e);
    }
    @FXML
    private void ftnValidacionDescripcion(KeyEvent e){
        if (Descripcion.getText().length()==50){
            e.consume();
        }
        ftnSoloLetras(e);
    }
    @FXML
    private void ftnSoloLetras(KeyEvent e){
        char key = e.getCharacter().charAt(0);
        if(Character.isDigit(key)){
            e.consume();
        }
    }
    @FXML
    private void Guardar () throws SQLException {
        Inicio.Conexion con = new Inicio.Conexion();
        Connection cnx = con.conectar();
        Statement st;
        int dato = 0;
        try {
            st = cnx.createStatement();
            ResultSet result = st.executeQuery("SELECT  tbldetgrados_grupos.PKCodigo FROM  tblgrado, tblgrupo, tbldetgrados_grupos WHERE tbldetgrados_grupos.FKCodigo_tblgrado = tblgrado.PKCodigo AND tbldetgrados_grupos.FKCodigo_tblgrupo = tblgrupo.PKCodigo AND tblgrado.Descripcion = '" + cbxGrado.getSelectionModel().getSelectedItem() +"' AND tblgrupo.Descripcion = '" + cbxGrupo.getSelectionModel().getSelectedItem() +"' ");

            if (result.next()) {
                System.out.println("Encontrado 1");
                dato=result.getInt(1);
                System.out.println(dato);
            }
            ResultSet result2 = st.executeQuery("SELECT tbltareas.NombreTarea, tbltareas.FKCodigo_detgrados_grupos, tbltareas.FKCodigo_tblestados FROM tbltareas WHERE NombreTarea = '"+ NombreTarea.getText()+ "' AND FKCodigo_detgrados_grupos = '" + dato +"' AND FKCodigo_tblestados = 1");

            if (result2.next()){
                System.out.println("Ya existe");

                String sql =("UPDATE tbltareas SET NombreTarea = '" + NombreTarea.getText() + "', Descripcion = '" + Descripcion.getText() + "', FechaEntrega = '"+ fecha.getValue() + "' WHERE tbltareas.PKCodigo = '"+dato+"'");

                st.executeUpdate(sql);
            }else{
                System.out.println(dato + "2");
                String sql = "INSERT INTO tbltareas (NombreTarea,Descripcion,FechaEntrega,FKCodigo_tblestados,FKCodigo_detgrados_grupos)" + " VALUES(?,?,?,?,?)";
                PreparedStatement tar = cnx.prepareStatement(sql);
                tar.setString(1, NombreTarea.getText());
                tar.setString(2, Descripcion.getText());
                tar.setDate(3, Date.valueOf(fecha.getValue()));
                tar.setInt(4, 1);
                tar.setInt(5, dato);
                tar.executeUpdate();
                System.out.println("Guardado");
            }



            cnx.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }

    }




}
