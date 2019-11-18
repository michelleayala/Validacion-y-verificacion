package docente;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class mostrarTareas implements Initializable {
    @FXML
    private Pane mostrarTarea =null;
    @FXML
    private ScrollPane tareas = null;
    @FXML
    private Pane tarea= null;
    @FXML
    private Pane Panel=null;

    @FXML
    private void MetodoPrueba () {

        Pane p = new Pane();
        Inicio.Conexion con = new Inicio.Conexion();
        Connection cnx = con.conectar();
        Statement st = null;

        try {
            st = cnx.createStatement();
            ResultSet resultSet = st.executeQuery("SELECT tbltareas.NombreTarea, tbltareas.Descripcion,tbltareas.FechaEntrega FROM tbltareas");
            int x = 0, y=0;
            while (resultSet.next()) {
                Pane p2 = new Pane();
                p2.setLayoutY(y);
                p2.setPrefWidth(tareas.getPrefWidth()-100);


                TextField l = new TextField("Nombre: " + resultSet.getString(1 ));
                l.setLayoutY(y);
                l.setEditable(false);
                TextField l2 = new TextField("Descripcion " + resultSet.getString(2));
                y=y+20;
                l2.setLayoutY(y);
                l2.setEditable(false);

                TextField l3 = new TextField("Fecha: " + resultSet.getString(3));
                y=y+20;
                l3.setLayoutY(y);
                l3.setEditable(false);
                y = y+15;

                p2.getChildren().addAll(l,l2,l3);
                //p2.setStyle("-fx-border-color: blue");

                p.getChildren().add(p2);

            }
            tareas.setContent(p);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private  void cargarCreacionTarea () throws IOException {
        Panel.setVisible(true);
        tareas.setPrefSize(254,586);
        Panel.getChildren().clear();
        tarea = FXMLLoader.load(getClass().getResource("tareas.fxml"));
        Panel.getChildren().add(tarea);

    }
    public void initialize(URL location, ResourceBundle resources) {
        MetodoPrueba();

    }
    }