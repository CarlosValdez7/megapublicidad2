/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

/**
 *
 * @author German Valdez
 */

import java.sql.*;
import javax.swing.JOptionPane;

public class Conexion{
    public Connection conectar(){
        Connection con;
        try{
            Class.forName("com.mysql.jdbc.Driver");
           //String url = "jdbc:mysql://104.236.17.212/megapublicidad?autoReconnect=true&useSSL=false";
           //con = DriverManager.getConnection(url, "megapub", "sistemas");
            String url = "jdbc:mysql://localhost/megapublicidad";
            con = DriverManager.getConnection(url, "root", "");
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Error en la Conexión con la BD "+ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            con=null;
        }
        return con;
    }
    
}


