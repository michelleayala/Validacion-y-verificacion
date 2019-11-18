package sesion;

import Clases.Persona;
import Clases.asistencia;
import Inicio.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.sql.*;

public class asistenciaAdmin{
    admin admin = new admin();

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

            ResultSet result = st.executeQuery("SELECT tblasistencias.fecha as 'fecha', tblasistencias.verificacion as 'verificacion', tblgrupo.Descripcion as 'descripcionGrupo', tblgrado.Descripcion  as 'descripcionGrado', tblpersonas.Nombres  as 'nombresPersona', tblpersonas.Apellidos as 'apellidos', tblpersonas.PKId as 'documento',\n" +
                    "tbldetgrados_grupos.PKCodigo as 'grado_grupo'\n" +
                    "FROM\n" +
                    "tblgrupo,tblgrado,tblpersonas,tbldetgrupos_personas,tbldetgrados_grupos, tblasistencias\n" +
                    "WHERE\n" +
                    "tblgrupo.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrupo and\n" +
                    "tblgrado.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrado and\n" +
                    "tblpersonas.PKId = tbldetgrupos_personas.FKId_tblpersonas and\n" +
                    "tbldetgrupos_personas.FKId_tblpersonas = tblasistencias.FKId_tblpersonas and\n" +
                    "tbldetgrados_grupos.PKCodigo = tbldetgrupos_personas.FKCodigo_detgrados_grupos AND\n" +
                    "tblasistencias.fecha = '"+fecha.getValue()+"' and\n"+
                    "tblgrado.Descripcion = '"+gradoValue+"' and \n" +
                    "tblgrupo.Descripcion = '"+grupoValue+"'");

            if (result.next()){
                tablaAsistencia.setItems(cargarAsistencia());
            }else{
                tablaAsistencia.setItems(cargarAsistenciaDefault());
            }
        }

        cnx.close();

    }

    private void actualizarAsistencia(){

    }

    @FXML
    private void guardarAsistencia() throws SQLException {
        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;
        st = cnx.createStatement();
        String gradoValue = String.valueOf(cbxGrado.getValue());
        String grupoValue = String.valueOf(cbxGrupo.getValue());
        for(int x=0; x<cantidadResultados; x++){
            asis.setDocumentoPersona(colDocumento.getCellObservableValue(x).getValue());
            asis.setFecha(fecha.getValue());
            asis.setVerificacion(colAsistencia.getCellObservableValue(x).getValue().isSelected());
            asis.getCodigoGrado_Grupo();

            ResultSet result = st.executeQuery("SELECT tblasistencias.fecha as 'fecha', tblasistencias.verificacion as 'verificacion', tblgrupo.Descripcion as 'descripcionGrupo', tblgrado.Descripcion  as 'descripcionGrado', tblpersonas.Nombres  as 'nombresPersona', tblpersonas.Apellidos as 'apellidos', tblpersonas.PKId as 'documento',\n" +
                    "tbldetgrados_grupos.PKCodigo as 'grado_grupo'\n" +
                    "FROM\n" +
                    "tblgrupo,tblgrado,tblpersonas,tbldetgrupos_personas,tbldetgrados_grupos, tblasistencias\n" +
                    "WHERE\n" +
                    "tblgrupo.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrupo and\n" +
                    "tblgrado.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrado and\n" +
                    "tblpersonas.PKId = tbldetgrupos_personas.FKId_tblpersonas and\n" +
                    "tbldetgrupos_personas.FKId_tblpersonas = tblasistencias.FKId_tblpersonas and\n" +
                    "tbldetgrados_grupos.PKCodigo = tbldetgrupos_personas.FKCodigo_detgrados_grupos AND\n" +
                    "tblasistencias.fecha = '"+fecha.getValue()+"' and\n"+
                    "tblgrado.Descripcion = '"+gradoValue+"' and \n" +
                    "tblgrupo.Descripcion = '"+grupoValue+"'");

            if(result.next()){
                do {
                    String sql= "UPDATE tblasistencias\n" +
                            "SET tblasistencias.verificacion = ?\n" +
                            "WHERE tblasistencias.fecha = '2019-11-01'";
                    PreparedStatement prepared = cnx.prepareStatement(sql);
                    prepared.setInt(1,0);
                    prepared.executeUpdate();
                    prepared.close();
                }while (result.next());


            }else{
                try {
                    st = cnx.createStatement();
                    String sql = ("insert into tblasistencias(FKId_tblpersonas, fecha, verificacion, FKCodigo_detgrados_grupos) values (?,?,?,?)");
                    PreparedStatement pst = cnx.prepareStatement(sql);
                    pst.setString(1, asis.getDocumentoPersona());
                    pst.setDate(2, Date.valueOf(asis.getFecha()));
                    pst.setBoolean(3, asis.getVerificacion());
                    pst.setInt(4, asis.getCodigoGrado_Grupo());
                    pst.executeUpdate();

                    System.out.println("funciono");
                }catch (SQLException e) {
                    System.out.println(e);;
                }
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

    private CheckBox prueba(CheckBox c, boolean b){
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
            ResultSet result = st.executeQuery("SELECT tblasistencias.fecha as 'fecha', tblasistencias.verificacion as 'verificacion', tblgrupo.Descripcion as 'descripcionGrupo', tblgrado.Descripcion  as 'descripcionGrado', tblpersonas.Nombres  as 'nombresPersona', tblpersonas.Apellidos as 'apellidos', tblpersonas.PKId as 'documento',\n" +
                    "tbldetgrados_grupos.PKCodigo as 'grado_grupo'\n" +
                    "FROM\n" +
                    "tblgrupo,tblgrado,tblpersonas,tbldetgrupos_personas,tbldetgrados_grupos, tblasistencias\n" +
                    "WHERE\n" +
                    "tblgrupo.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrupo and\n" +
                    "tblgrado.PKCodigo = tbldetgrados_grupos.FKCodigo_tblgrado and\n" +
                    "tblpersonas.PKId = tbldetgrupos_personas.FKId_tblpersonas and\n" +
                    "tbldetgrupos_personas.FKId_tblpersonas = tblasistencias.FKId_tblpersonas and\n" +
                    "tbldetgrados_grupos.PKCodigo = tbldetgrupos_personas.FKCodigo_detgrados_grupos AND\n" +
                    "tblasistencias.fecha = '"+fecha.getValue()+"' and\n"+
                    "tblgrado.Descripcion = '"+gradoValue+"' and \n" +
                    "tblgrupo.Descripcion = '"+grupoValue+"'");
            if(result.next()){
                do {
                    System.out.println("funci?");
                    if(result.getInt("verificacion")==0){
                        temp = false;
                        System.out.println("0");
                    }else{
                        temp = true;
                        System.out.println("1");
                    }

                    listaPersonas.add(new Persona(result.getString("documento"),
                            result.getString("nombresPersona"), result.getString("apellidos"),
                            prueba(new CheckBox(), temp)));
                    asis.setCodigoGrado_Grupo(result.getInt("grado_grupo"));
                    cantidadResultados++;
                }while(result.next());
            }else{
                asis.setCodigoGrado_Grupo(0);
                System.out.println("no func?");
                cargarAsistenciaDefault();
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
