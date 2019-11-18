package sesion;

import Inicio.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import Inicio.login;
import javafx.scene.layout.Pane;
import java.io.IOException;


public class admin{

    @FXML
    public Pane contenido;
    @FXML
    private Pane nuevoPanel;
    @FXML
    private Pane top;

    @FXML
    private void cerrarFormulario(){
        System.exit(0);
    }

    @FXML
    private void minimizarFormulario() throws IOException {
    login login = new login();
    Main.appStage.setIconified(true);
    }


    @FXML
    private void mostrarAsistencia(){

        cargarUi("asistencia");

    }
    @FXML
    private void cargarEven (){
        cargarUi("eventos");
    }

    public void cargarUi(String ui) { //Permite cargar un contenido en el panel
    try {
            contenido.getChildren().clear();
            nuevoPanel = FXMLLoader.load(getClass().getResource(ui + ".fxml"));
            contenido.getChildren().add(nuevoPanel);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
