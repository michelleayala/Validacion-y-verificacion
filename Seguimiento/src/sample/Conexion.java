package sample;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conexion {
    private com.mysql.jdbc.Connection conectar = null;

    public com.mysql.jdbc.Connection conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conectar = (com.mysql.jdbc.Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/seguimiento", "root", "");
            //JOptionPane.showMessageDialog(null, "Conexion Establecida","Mensaje",JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException | SQLException error) {
            JOptionPane.showMessageDialog(null, "Error al Conectarse"+"\n"+error,"Mensaje Error",JOptionPane.ERROR_MESSAGE);
        }

        return conectar;
    }


}
