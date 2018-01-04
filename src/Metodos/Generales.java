/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Metodos;

import Conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author German Valdez
 */
public class Generales{

    Connection cn;

    public Generales() {
        Conexion con = new Conexion();
        cn = con.conectar();
    }
    /* METODOS PARA INSERTAR, ELIMINAR Y MODIFICAR CLIENTES EN EL SISTEMA */

    public void insertaCliente(String nom, String rep, String rfc, String dir, String cp, String correo, String tel,
            String mun, String est) {
        try {
            String sql = "insert into clientes values(null,'" + nom + "','" + rep + "','" + rfc + "','" + dir + "'"
                    + ",'" + cp + "','" + correo + "','" + tel + "','" + mun + "','" + est + "')";

            Statement stmt = cn.createStatement();
            stmt.execute(sql);
            /*PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();

            cmd.close();*/
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar cliente.\n"+ex);
        }
    }

    public void eliminarCliente(String codigo) {
        try {
            String sql = "DELETE FROM clientes WHERE idCliente='" + codigo + "' ";
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar cliente");
        }
    }

    public void actualizarCliente(String codigo, String nom, String rep, String rfc, String dir, String cp, String correo, String tel, String mun, String estado) {
        try {
            String sql = "UPDATE clientes set nombre='" + nom + "',representante='" + rep + "',RFC='" + rfc + "',direccion='" + dir + "',CP='" + cp + "',"
                    + "correo='" + correo + "',telefono='" + tel + "',municipio='" + mun + "',estado='" + estado + "' WHERE idCliente=" + codigo;
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar cliente");
        }
    }

    public String idNuevo() {
        try {
            String sql = "SELECT idCliente+1 from clientes order by idCliente desc limit 1";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[2];
                for (int i = 0; i < 1; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                String a = datos[0] + "";
                return a;
            }
            cmd.close();
        } catch (Exception ex) {
        }

        return null;
    }

    public void tablaClientes(DefaultTableModel modelo) {

        try {
            String sql = "SELECT * FROM clientes";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 10; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaClientes\n"+ex);
        }
    }
    
    public void busquedaClientes(DefaultTableModel modelo, String palabra, String tipo) {
        try {
            String sql = "SELECT  * FROM clientes where "+ tipo + " like '" + palabra + "%'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[18];
                for (int i = 0; i < 10; i++) {

                    datos[i] = rs.getString(i + 1);
                }

                modelo.addRow(datos);
            }
            cmd.close();
            //cn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en busquedaClientes");
        }
    }
    
    
    public void busquedaProductos(DefaultTableModel modelo, String palabra, String tipo) {
        try {
            String sql = "SELECT  * FROM productos where "+ tipo + " like '" + palabra + "%'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[18];
                for (int i = 0; i < 6; i++) {

                    datos[i] = rs.getString(i + 1);
                }

                modelo.addRow(datos);
            }
            cmd.close();
            //cn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en busquedaProductos");
        }
    }

    
    /* METODOS PARA INSERTAR ELIMINAR Y MODIFICAR PRODUCTOS O SERVICIOS EN EL SISTEMA*/

    public void tablaProductos(DefaultTableModel modelo,String where) {

        try {
            String sql = "SELECT * FROM productos "+where;
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 9; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaProductos");
        }
    }

    public void insertaProducto(String tipo, String nombre, String desc, Double prec, Double prec2,
            String tipoF, String minima, String existencia) {
        try {
            String sql = "insert into productos values(null,'" + tipo + "','" + nombre + "','" + desc + "','" + prec + "','" + prec2 + "', '"+tipoF+"','"+minima+"','"+existencia+"')";

            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();

            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error, intente de nuevo");
        }
    }

    public void eliminarArticulo(String codigo) {
        try {
            String sql = "DELETE FROM productos WHERE id='" + codigo + "' ";
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en eliminar articulo");
        }
    }

    public void actualizarArticulo(String codigo, String nom, String desc, String prec, String pm,String tipo) {
        try {
            String sql = "UPDATE productos set nombre='" + nom + "',descripcion='" + desc + "',precio=" + prec + ","
                    + "precioMaquila=" + pm + ", tipoFormato='"+tipo+"' WHERE id='" + codigo + "' ";
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en actualizar articulo");
        }
    }

    /* COMBOOOOOOOOOOOOOS DE PRODUCTOS Y SERVICIOOOOOOOOOOOOOOOOOOOOOOOOOOS*/
    public String llenarProd(String tabla) {
        try {
            String sql = "SELECT nombre FROM productos where tipo ='" + tabla + "'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            String a = "Seleccione..,";
            while (rs.next()) {
                Object[] datos = new Object[4];
                for (int i = 0; i < 1; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                a += datos[0] + ",";
            }
            cmd.close();
            return a;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en listas de productos");
        }
        return null;
    }

// *********************************METODOS PARA INICIAR SESION EN EL SISTEMA, LOGEO, CONTRSEÑAS ETC.. *******************************
    public String contra(String usu, String tipo) {
        try {
            String sql = "SELECT contrasena FROM usuarios WHERE contrasena='" + usu + "' and tipo='" + tipo + "'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[2];
                for (int i = 0; i < 1; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                String a = datos[0] + "";
                return a;
            }
            cmd.close();
            //  cn.close();
        } catch (Exception ex) {
        }

        return null;
    }

    public String usuario(String usu) {
        try {
            String sql = "SELECT tipo FROM usuarios WHERE usuario='" + usu + "'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[2];
                for (int i = 0; i < 1; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                String a = datos[0] + "";
                return a;
            }
            cmd.close();
        } catch (Exception ex) {
        }

        return null;
    }

    public String saludo(String usuario) {
        try {

            String sql = "SELECT nombre, apellido FROM usuarios WHERE contrasena='" + usuario + "' ";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[3];
                for (int i = 0; i < 2; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                String a = datos[0] + " " + datos[1];
                return a;

            }
            cmd.close();
        } catch (Exception ex) {
        }

        return null;

    }

    public void añadirUsu(String nom, String ape, String contra, String tipo) {
        try {
            String sql = "insert into usuarios values(null,'" + nom + "','" + ape + "','" + contra + "','" + tipo + "')";
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al añadir nuevo usuario" + ex);
        }
    }

    public void eliminarUsu(String cod) {
        try {
            String sql = "delete from usuarios where id=" + cod;
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
            // cn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error en  eliminar usuario" + ex);
        }
    }

    public void cargarUsu(DefaultTableModel modelo) {

        try {
            String sql = "SELECT id, CONCAT(nombre,' ',apellido), contrasena,tipo from usuarios";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 4; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
        }
    }
    //                 CORTE DE CAJA METODOS Y MÁS ETC ETC BYE                          

    public void tablaCorteCaja(DefaultTableModel modelo) {

        try {
            String sql = "SELECT * FROM cortecaja";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 4; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error en tablaCorteCaja");
        }
    }

    public String corteCaja(String fecha) {
        try {
            String sql = "SELECT id FROM cortecaja WHERE fecha='" + fecha + "' ";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[2];
                for (int i = 0; i < 1; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                String a = datos[0] + "";

                return a;
            }
            cmd.close();

        } catch (Exception ex) {
        }

        return null;
    }
    
    
}
