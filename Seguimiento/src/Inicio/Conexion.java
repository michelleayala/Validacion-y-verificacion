package Inicio;

import javax.swing.*;
import java.sql.*;

public class Conexion {
    private com.mysql.jdbc.Connection conectar = null;
    private boolean dato;
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

    public boolean buscarResultados(String stringSql){
        Conexion con = new Conexion();
        Connection cnx = con.conectar();
        Statement st;

        try {//Busca cuantos resultados se pueden encontrar del Usuario.
            st = cnx.createStatement();
            ResultSet result = st.executeQuery(stringSql);
            while (result.next()) {
                dato=true;
            }
        } catch (SQLException ex) {
            dato=false;
            System.err.println(ex);
        }
        return dato;
    }

}
