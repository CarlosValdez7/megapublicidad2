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
public class Generales {

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

             PreparedStatement cmd = cn.prepareCall(sql);
             cmd.execute();

             cmd.close();
             JOptionPane.showMessageDialog(null, "Cliente registrado");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar cliente.\n" );
        }
    }

    public void insertaCupon(String nom, String desc, String fecha, String estatus) {
        try {
            String sql = "insert into codigos values(null,'" + nom + "','" + desc + "','" + fecha + "','" + estatus + "' )";

            Statement stmt = cn.createStatement();
            stmt.execute(sql);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar cupon.\n" + ex);
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
            JOptionPane.showMessageDialog(null, "Error en tablaClientes\n" + ex);
        }
    }

    public void busquedaClientes(DefaultTableModel modelo, String palabra, String tipo) {
        try {
            String sql = "SELECT  * FROM clientes where " + tipo + " like '" + palabra + "%'";
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
            String sql = "SELECT  * FROM productos where " + tipo + " like '" + palabra + "%'";
            System.out.println(sql);
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[18];
                for (int i = 0; i < 9; i++) {

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

    public void busquedaProductos2(DefaultTableModel modelo, String palabra, String tipo,String where) {
        try {
            String sql = "SELECT id,tipo,nombre,descripcion,precio,precioMaquila,existencias FROM productos where"+where+" AND(" + tipo + " like '" + palabra + "%')";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[18];
                for (int i = 0; i < 7; i++) {

                    datos[i] = rs.getString(i + 1);
                }

                modelo.addRow(datos);
            }
            cmd.close();
            //cn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en busquedaProductos2");
        }
    }
    
    /* METODOS PARA INSERTAR ELIMINAR Y MODIFICAR PRODUCTOS O SERVICIOS EN EL SISTEMA*/
    public void tablaProductos(DefaultTableModel modelo, String where) {

        try {
            String sql = "SELECT * FROM productos " + where;
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
    public void tablaProductos2(DefaultTableModel modelo, String where) {

        try {
            String sql = "SELECT id,tipo,nombre,descripcion,precio,precioMaquila,existencias FROM productos " + where;
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 7; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaProductos2");
        }
    }

    public void insertaProducto(String tipo, String nombre, String desc, Double prec, Double prec2,
            String tipoF, String minima, String existencia) {
        try {
            String sql = "insert into productos values(null,'" + tipo + "','" + nombre + "','" + desc + "','" + prec + "','" + prec2 + "', '" + tipoF + "','" + minima + "','" + existencia + "')";

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

    public void actualizarArticulo(String codigo, String nom, String desc, String prec, String pm, String tipo, String minima, String exis) {
        try {
            String sql = "UPDATE productos set nombre='" + nom + "',descripcion='" + desc + "',precio=" + prec + ","
                    + "precioMaquila=" + pm + ", tipoFormato='" + tipo + "',minima='" + minima + "',existencias='" + exis + "' WHERE id='" + codigo + "' ";
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en actualizar articulo");
        }
    }

    public void actualizarStock(String codigo, String stock) {
        try {
            String sql = "UPDATE productos set existencias='" + stock + "' WHERE id=" + codigo;
            System.out.println(sql);
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
            JOptionPane.showMessageDialog(null, "Existencias actualizadas");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en actualizar stock");
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

            String sql = "SELECT nombre, apellido,icono FROM usuarios WHERE contrasena='" + usuario + "' ";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[4];
                for (int i = 0; i < 3; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                String a = datos[0] + " " + datos[1] + "," + datos[2] + " ";
                System.out.println(a);
                return a;

            }
            cmd.close();
        } catch (Exception ex) {
        }

        return null;

    }

    public void añadirUsu(String nom, String ape, String contra, String tipo, String ico) {
        try {
            String sql = "insert into usuarios values(null,'" + nom + "','" + ape + "','" + contra + "','" + tipo + "', '" + ico + "')";
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
            JOptionPane.showMessageDialog(null, "Error en  eliminar usuario" + ex);
        }
    }

    public String llenarCategorias() {
        try {
            String sql = "SELECT nombre FROM categorias ORDER BY nombre ASC";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            String a = "Seleccione..,";
            while (rs.next()) {
                Object[] datos = new Object[2];
                for (int i = 0; i < 1; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                a += datos[0] + ",";
            }
            cmd.close();
            return a;
        } catch (Exception ex) {
        }
        return null;
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
            JOptionPane.showMessageDialog(null, "Error en tablaCorteCaja");
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

    public void cargarCupones(DefaultTableModel modelo) {

        try {
            String sql = "SELECT codigo, descuento, fecha,estado from codigos";
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

    public void cargarCate(DefaultTableModel modelo) {

        try {
            String sql = "SELECT id, nombre from categorias";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 2; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
        }

    }

    public void insertaCat(String nom) {
        try {
            String sql = "insert into categorias values(null,'"+ nom +"')";
            Statement stmt = cn.createStatement();
            stmt.execute(sql);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar cat.\n" + ex);
        }
    }

  public void eliminarCat(String cod) {
        try {
            String sql = "delete from categorias where id=" + cod;
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en  eliminar cat." + ex);
        }
    }
}
