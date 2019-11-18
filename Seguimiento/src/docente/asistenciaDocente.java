package docente;

import Clases.Persona;
import Clases.asistencia;
import Inicio.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.sql.*;

public class asistenciaDocente {
    docente admin = new docente();

    final int posicionY_tablaAsistencia = 83;
    final int posicionX_tablaAsistencia = 14;

    @FXML
    public TableView<Persona> tablaAsistencia;
    @FXML
    private Button btnBuscarAsistencia;
    @FXML
    private ComboBox cbxGrado;
    @FXML
    private ComboBox cbxGrupo;
    @FXML
    private DatePicker fecha;
    @FXML
    private Pane panelAsistencia;

    asistencia asis = new asistencia();
    Persona persona = new Persona();
    int cantidadResultados = 0;
    CheckBox a = new CheckBox();
    TableColumn<Persona, String> colDocumento;
    TableColumn<Persona, String> colNombres;
    TableColumn<Persona, String> colApellidos;
    TableColumn<Persona, CheckBox> colAsistencia;


    @FXML
    private void ftnBuscarAsistencia() throws SQLException {
        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;
        st = cnx.createStatement();
        String gradoValue = String.valueOf(cbxGrado.getValue());
        String grupoValue = String.valueOf(cbxGrupo.getValue());
        ftnCrearColumnasTablaAsistencia(panelAsistencia);
        if (!(cbxGrado.getSelectionModel().getSelectedItem() ==null) &&
                !(cbxGrupo.getSelectionModel().getSelectedItem() ==null) &&
                !(fecha.getValue() ==null)){

            ResultSet result = st.executeQuery("SELECT tblasistencias.fecha as 'fecha', tblgrupo.Descripcion as 'descripcionGrupo', tblgrado.Descripcion  as 'descripcionGrado', tblpersonas.Nombres  as 'nombresPersona', tblpersonas.Apellidos as 'apellidos', tblpersonas.PKId as 'documento',\n" +
                    "                    tbldetgrados_grupos.PKCodigo as 'grado_grupo'\n" +
                    "                    FROM\n" +
                    "                    tblgrupo,tblgrado,tblpersonas,tbldetgrupos_personas,tbldetgrados_grupos, tblasistencias, tbldet_tblasistencias\n" +
                    "                    WHERE\n" +
                    "                    tblgrupo.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrupo and\n" +
                    "                    tblgrado.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrado and\n" +
                    "                    tblpersonas.PKId = tbldetgrupos_personas.FKId_tblpersonas and\n" +
                    "                    tbldetgrupos_personas.FKId_tblpersonas = tbldet_tblasistencias.FKId_tblpersonas and\n" +
                    "                    tbldetgrados_grupos.PKCodigo = tbldetgrupos_personas.FKCodigo_detgrados_grupos AND\n" +
                    "                    tbldet_tblasistencias.FKCodigo_tblasistencias = tblasistencias.PKCodigo and\n" +
                    "                    tblasistencias.FKCodigo_detgrados_grupos = tbldetgrados_grupos.PKCodigo AND\n" +
                    "                    tblasistencias.fecha = '" + fecha.getValue() + "' AND\n" +
                    "                    tblgrado.Descripcion = '" + gradoValue + "' and" +
                    "                    tblgrupo.Descripcion = '" + grupoValue + "'");

            if (result.next()){
                tablaAsistencia.setItems(cargarAsistencia());
            }else{
                tablaAsistencia.setItems(cargarAsistenciaDefault());
            }
        }

        cnx.close();

    }

    @FXML
    private void guardarAsistencia() throws SQLException {
        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;
        st = cnx.createStatement();
        String gradoValue = String.valueOf(cbxGrado.getValue());
        String grupoValue = String.valueOf(cbxGrupo.getValue());

        ResultSet result = st.executeQuery("SELECT tblasistencias.fecha as 'fecha', tblgrupo.Descripcion as 'descripcionGrupo', tblgrado.Descripcion  as 'descripcionGrado', tblpersonas.Nombres  as 'nombresPersona', tblpersonas.Apellidos as 'apellidos', tblpersonas.PKId as 'documento',\n" +
                "                    tbldetgrados_grupos.PKCodigo as 'grado_grupo'\n" +
                "                    FROM\n" +
                "                    tblgrupo,tblgrado,tblpersonas,tbldetgrupos_personas,tbldetgrados_grupos, tblasistencias, tbldet_tblasistencias\n" +
                "                    WHERE\n" +
                "                    tblgrupo.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrupo and\n" +
                "                    tblgrado.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrado and\n" +
                "                    tblpersonas.PKId = tbldetgrupos_personas.FKId_tblpersonas and\n" +
                "                    tbldetgrupos_personas.FKId_tblpersonas = tbldet_tblasistencias.FKId_tblpersonas and\n" +
                "                    tbldetgrados_grupos.PKCodigo = tbldetgrupos_personas.FKCodigo_detgrados_grupos AND\n" +
                "                    tbldet_tblasistencias.FKCodigo_tblasistencias = tblasistencias.PKCodigo and\n" +
                "                    tblasistencias.FKCodigo_detgrados_grupos = tbldetgrados_grupos.PKCodigo AND\n" +
                "                    tblasistencias.fecha = '" + asis.getFecha() + "' AND\n" +
                "                    tblgrado.Descripcion = '" + gradoValue + "' and" +
                "                    tblgrupo.Descripcion = '" + grupoValue + "'");

        for(int x=0; x<cantidadResultados; x++){
            asis.setDocumentoPersona(colDocumento.getCellObservableValue(x).getValue());
            asis.setFecha(fecha.getValue());
            asis.setVerificacion(colAsistencia.getCellObservableValue(x).getValue().isSelected());
            int verificacionTemporal = 0;
            if(asis.getVerificacion()==true){
                verificacionTemporal = 1;
            }else{
                verificacionTemporal = 0;
            }


            if(result.next()){

                    String sql= "UPDATE tbldet_tblasistencias inner JOIN tblasistencias\n" +
                            " on tblasistencias.fecha = '" + fecha.getValue() +"' and tbldet_tblasistencias.FKCodigo_tblasistencias = tblasistencias.PKCodigo AND\n" +
                            " tbldet_tblasistencias.FKId_tblpersonas = '" + asis.getDocumentoPersona() + "'\n" +
                            " SET tbldet_tblasistencias.verificacion =?";
                    PreparedStatement prepared = cnx.prepareStatement(sql);
                    prepared.setInt(1,verificacionTemporal);
                    prepared.executeUpdate();
                    prepared.close();
                JOptionPane.showMessageDialog(null, "Actualización exitosa");

            }}
        if(!result.next()){

                try {

                    int codigotemp = 0;

                    st = cnx.createStatement();
                    String sql = ("insert into tblasistencias(fecha, FKCodigo_detgrados_grupos) values (?,?)");
                    PreparedStatement pst = cnx.prepareStatement(sql);
                    pst.setDate(1, Date.valueOf(asis.getFecha()));
                    pst.setInt(2, asis.getCodigoGrado_Grupo());
                    pst.executeUpdate();
                    pst.close();

                    ResultSet result2 = st.executeQuery("select PKCodigo from tblasistencias where tblasistencias.FKCodigo_detgrados_grupos = '" + asis.getCodigoGrado_Grupo() + "' and " +
                            "tblasistencias.fecha = '" + asis.getFecha()+"'");
                    if(result2.next()){
                        codigotemp = result2.getInt("PKCodigo");
                    }

                    for(int x=0; x<cantidadResultados; x++){
                        asis.setDocumentoPersona(colDocumento.getCellObservableValue(x).getValue());
                        asis.setFecha(fecha.getValue());
                        asis.setVerificacion(colAsistencia.getCellObservableValue(x).getValue().isSelected());
                        int verificacionTemporal = 0;
                        if(asis.getVerificacion()==true){
                            verificacionTemporal = 1;
                        }else{
                            verificacionTemporal = 0;
                        }

                        String sql2 = ("insert into tbldet_tblasistencias(FKCodigo_tblasistencias, FKId_tblpersonas, verificacion) values (?,?,?) ");
                        PreparedStatement pst2 = cnx.prepareStatement(sql2);
                        pst2.setInt(1, codigotemp);
                        pst2.setString(2,asis.getDocumentoPersona());
                        pst2.setInt(3, verificacionTemporal);
                        pst2.executeUpdate();
                        pst2.close();
                        JOptionPane.showMessageDialog(null, "Exitoso");
                    }


                }catch (Exception e) {
                    System.out.println(e);;
                }
            }




    }


    @FXML
    private void cargarCbxGrado(){ //Se encarga de cargar los grados en el comboBox grado
        Conexion con = new Conexion();
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
        Conexion con = new Conexion();
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

    private CheckBox verificacionAsistencia(CheckBox c, boolean b){
        c.setSelected(b);
        return c;
    }

    private ObservableList<Persona> cargarAsistencia(){
        cantidadResultados=0;
        boolean temp =false;

        String gradoValue = String.valueOf(cbxGrado.getValue());
        String grupoValue = String.valueOf(cbxGrupo.getValue());
        //Crea una lista observable de la clase Persona
        ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();
        //Instacias para la conexion de la base de datos
        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;
        try {
            st = cnx.createStatement();
            ResultSet result = st.executeQuery("SELECT tblasistencias.fecha as 'fecha', tblgrupo.Descripcion as 'descripcionGrupo', tblgrado.Descripcion  as 'descripcionGrado', tblpersonas.Nombres  as 'nombresPersona', tblpersonas.Apellidos as 'apellidos', tblpersonas.PKId as 'documento', tbldet_tblasistencias.verificacion as 'verificacion',\n" +
                    "                    tbldetgrados_grupos.PKCodigo as 'grado_grupo'\n" +
                    "                    FROM\n" +
                    "                    tblgrupo,tblgrado,tblpersonas,tbldetgrupos_personas,tbldetgrados_grupos, tblasistencias, tbldet_tblasistencias\n" +
                    "                    WHERE\n" +
                    "                    tblgrupo.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrupo and\n" +
                    "                    tblgrado.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrado and\n" +
                    "                    tblpersonas.PKId = tbldetgrupos_personas.FKId_tblpersonas and\n" +
                    "                    tbldetgrupos_personas.FKId_tblpersonas = tbldet_tblasistencias.FKId_tblpersonas and\n" +
                    "                    tbldetgrados_grupos.PKCodigo = tbldetgrupos_personas.FKCodigo_detgrados_grupos AND\n" +
                    "                    tbldet_tblasistencias.FKCodigo_tblasistencias = tblasistencias.PKCodigo and\n" +
                    "                    tblasistencias.FKCodigo_detgrados_grupos = tbldetgrados_grupos.PKCodigo AND\n" +
                    "                    tblasistencias.fecha = '" + fecha.getValue() + "' AND\n" +
                    "                    tblgrado.Descripcion = '" + gradoValue + "'" +
                    "                    and tblgrupo.Descripcion = '" + grupoValue + "'");
            if(result.next()){
                do {
                    if(result.getInt("verificacion")==0){
                        temp = false;
                        System.out.println("0");
                    }else{
                        temp = true;
                        System.out.println("1");
                    }

                    listaPersonas.add(new Persona(result.getString("documento"),
                            result.getString("nombresPersona"), result.getString("apellidos"),
                            verificacionAsistencia(new CheckBox(), temp)));
                    asis.setCodigoGrado_Grupo(result.getInt("grado_grupo"));
                    cantidadResultados++;
                }while(result.next());
            }else{
                asis.setCodigoGrado_Grupo(0);
                listaPersonas.add(new Persona(result.getString("documento"),
                        result.getString("nombresPersona"), result.getString("apellidos")));
                asis.setCodigoGrado_Grupo(result.getInt("grado_grupo"));
                cantidadResultados++;
            }

            cnx.close();
        } catch (SQLException ex) {
            System.err.println("Excepcion");
        }
        return listaPersonas;

    }

    private ObservableList<Persona> cargarAsistenciaDefault(){
        cantidadResultados=0;

        String gradoValue = String.valueOf(cbxGrado.getValue());
        String grupoValue = String.valueOf(cbxGrupo.getValue());
        //Crea una lista observable de la clase Persona
        ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();
        //Instacias para la conexion de la base de datos
        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;
        try {
            st = cnx.createStatement();
            ResultSet result = st.executeQuery("SELECT \n" +
                    "tblgrupo.Descripcion as 'descripcionGrupo', tblgrado.Descripcion  as 'descripcionGrado', tblpersonas.Nombres  as 'nombresPersona', tblpersonas.Apellidos as 'apellidos', tblpersonas.PKId as 'documento', tbldetgrados_grupos.PKCodigo as 'grado_grupo' FROM tblgrupo,tblgrado,tblpersonas,tbldetgrupos_personas,tbldetgrados_grupos\n" +
                    "WHERE\n" +
                    "tblgrupo.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrupo and\n" +
                    "tblgrado.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrado and\n" +
                    "tblpersonas.PKId = tbldetgrupos_personas.FKId_tblpersonas and\n" +
                    "tbldetgrados_grupos.PKCodigo = tbldetgrupos_personas.FKCodigo_detgrados_grupos AND\n" +
                    "tblgrado.Descripcion ="+gradoValue+" and \n" +
                    "tblgrupo.Descripcion = '"+grupoValue+"'");
            if(result.next()){
                do {
                    listaPersonas.add(new Persona(result.getString("documento"),
                            result.getString("nombresPersona"), result.getString("apellidos")));
                    asis.setCodigoGrado_Grupo(result.getInt("grado_grupo"));
                    cantidadResultados++;
                }while(result.next());
            }else{
                asis.setCodigoGrado_Grupo(0);
            }

            cnx.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return listaPersonas;

    }

    public void ftnCrearColumnasTablaAsistencia(Pane panelContenido){
        //Crea la columna documento, le da un tamaño minimo y le asigna un valor.
        colDocumento = new TableColumn<>("Documento");
        colDocumento.setMinWidth(200);
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));

        //Crea la columna nombres, le da un tamaño minimo y le asigna un valor.
        colNombres = new TableColumn<>("Nombres");
        colNombres.setMinWidth(200);
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));

        //Crea la columna apelldios, le da un tamaño minimo y le asigna un valor.
        colApellidos = new TableColumn<>("Apellidos");
        colApellidos.setMinWidth(200);
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));

        //Crea la columna asistencia, le da un tamaño minimo y crea un Checkbox.
        colAsistencia = new TableColumn<>("Asistencia");
        colAsistencia.setMinWidth(200);
        colAsistencia.setCellValueFactory(new PropertyValueFactory<>("asistencia"));


        tablaAsistencia = new TableView<>();
        //Carga los valores agregados en el metodo
        //tablaAsistencia.setItems(ftnCargarTablaAsistencia());
        tablaAsistencia.getColumns().addAll(colDocumento,colNombres, colApellidos, colAsistencia);
        //Muestra la tabla en el panel
        tablaAsistencia.setLayoutX(posicionX_tablaAsistencia);
        tablaAsistencia.setLayoutY(posicionY_tablaAsistencia);
        tablaAsistencia.setEditable(false);
        panelContenido.getChildren().add(tablaAsistencia);
    }
}
