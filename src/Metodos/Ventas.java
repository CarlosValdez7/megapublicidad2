/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Metodos;

import Conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author German Valdez
 */
public class Ventas {

    Connection cn;

    public Ventas() {
        Conexion con = new Conexion();
        cn = con.conectar();
    }

    public void tablaLonas(DefaultTableModel modelo, String precio) {
        try {

            String sql = "SELECT id,nombre," + precio + " FROM productos where tipo='Lonas'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 3; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaLonas\n" + ex);
        }
    }

    public void tablaViniles(DefaultTableModel modelo, String precio) {
        try {
            String sql = "SELECT id,nombre," + precio + " FROM productos where tipo='Viniles de impresion'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 3; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaViniles");
        }
    }

    public void tablaVinilesCorte(DefaultTableModel modelo, String precio) {
        try {
            String sql = "SELECT id,nombre," + precio + " FROM productos where tipo='Viniles de corte'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 3; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaVinilesCorte");
        }
    }

    public void tablaOFF(DefaultTableModel modelo, String precio) {
        try {
            String sql = "SELECT id,nombre," + precio + " FROM productos where tipo='OFFSET'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 3; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaOFF");
        }
    }

    public void tablaImpDig(DefaultTableModel modelo, String precio) {
        try {
            String sql = "SELECT id,nombre," + precio + " FROM productos where tipo='Impresion Digital'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 3; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaImpDig");
        }
    }

    public void tablaDisplay(DefaultTableModel modelo, String precio) {
        try {
            String sql = "SELECT id,nombre," + precio + " FROM productos where tipo='Display'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 3; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaDisplay");
        }
    }

    public void tablaDiseño(DefaultTableModel modelo, String precio) {
        try {
            String sql = "SELECT id,nombre," + precio + " FROM productos where tipo='Diseño'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 3; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaDiseño");
        }
    }

    public void tablaMO(DefaultTableModel modelo, String precio) {
        try {
            String sql = "SELECT id,nombre," + precio + " FROM productos where tipo='M.O/Instalaciones'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 3; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaMO");
        }
    }

    public void tablaEquipo(DefaultTableModel modelo, String precio) {
        try {
            String sql = "SELECT id,nombre," + precio + " FROM productos where tipo='Estructura y Equipo'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 3; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaEquipo");
        }
    }

    public void tablaAcabados(DefaultTableModel modelo, String precio) {
        try {
            String sql = "SELECT id,nombre," + precio + " FROM productos where tipo='Acabados'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[5];
                for (int i = 0; i < 3; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaAcabados");
        }
    }

    public void RealizaVenta(DefaultTableModel modelo, String cliente, String concepto, String total, String fecha, String estado,
            String tipoPago, String estadoPago, String factu, String entregado, String usu,
            String sub, String descuento, String impuestos, String comentarios) {

        try {
            String sql = "INSERT INTO ventas VALUES(null,'" + cliente + "','" + concepto + "','" + total + "','" + fecha + "',"
                    + "'" + estado + "','" + tipoPago + "','" + estadoPago + "','" + factu + "','" + entregado + "','" + usu + "',"
                    + "'" + sub + "','" + descuento + "','" + impuestos + "', '" + comentarios + "')";
            System.out.println(sql);
            CallableStatement cmd = cn.prepareCall(sql);
            cmd.execute();



            sql = "SELECT id FROM ventas ORDER BY id DESC LIMIT 1";
            cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            rs.next();
            String noVenta = rs.getString("id");

            for (int i = 0; i < modelo.getRowCount(); i++) {
                String codigo = modelo.getValueAt(i, 0) + "";
                String prod = modelo.getValueAt(i, 1) + "";
                String med = modelo.getValueAt(i, 2) + "";
                String cant = modelo.getValueAt(i, 3) + "";
                String pu = modelo.getValueAt(i, 4) + "";
                String tot = modelo.getValueAt(i, 5) + "";

                sql = "INSERT INTO detalle_venta VALUES (" + noVenta + "," + codigo + ",'" + prod + "'," + cant + "," + pu + "," + tot + ", "
                        + "'" + estadoPago + "' )";
                cmd = cn.prepareCall(sql);
                cmd.execute();
                double medidas;
                if (cant.equals("1")) {
                    medidas = Double.parseDouble(med) + 0.5;
                } else {
                    medidas = (Double.parseDouble(med) + 0.5) * Double.parseDouble(cant);
                }
                sql = "UPDATE productos SET existencias =existencias-" + medidas + " WHERE id='" + codigo + "'";
                System.out.println(sql);
                cmd = cn.prepareCall(sql);
                cmd.execute();
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en realizar venta: \n" + ex);
        }
    }

    public String folioVenta() {
        try {
            String sql = "SELECT id FROM ventas ORDER BY id DESC LIMIT 1";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            rs.next();
            String noVenta = rs.getString("id");

            cmd.close();
            return noVenta;

        } catch (Exception ex) {
        }
        return null;
    }

    public String validarCodigo(String codigo) {
        try {
            String sql = "SELECT codigo,fecha,descuento,estado FROM codigos where codigo='" + codigo + "'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            rs.next();
            String cod = rs.getString("codigo");
            String fecha = rs.getString("fecha");
            String desc = rs.getString("descuento");
            String esta = rs.getString("estado");

            cmd.close();
            return cod + "," + fecha + "," + desc + "," + esta;

        } catch (Exception ex) {
        }

        return null;
    }

    public void RealizaVenta2(DefaultTableModel modelo, String efectivo, String cliente, String concepto, String total,
            String fecha, String estado, String tipoPago, String estadoPago, String factu, String entregado, String usu,
            String sub, String descuento, String impuestos, String comentarios) {

        try {
            String sql = "INSERT INTO ventas VALUES(null,'" + cliente + "','" + concepto + "','" + total + "','" + fecha + "',"
                    + "'" + estado + "','" + tipoPago + "','" + estadoPago + "','" + factu + "','" + entregado + "','" + usu + "', "
                    + "'" + sub + "','" + descuento + "','" + impuestos + "','" + comentarios + "')";
            System.out.println("LLEGUE AQUI: \n" + sql);
            CallableStatement cmd = cn.prepareCall(sql);
            cmd.execute();

            sql = "SELECT id FROM ventas ORDER BY id DESC LIMIT 1";
            System.out.println("LLEGUE AQUI: \n" + sql);
            cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            rs.next();
            String noVenta = rs.getString("ID");

            for (int i = 0; i < modelo.getRowCount(); i++) {
                String codigo = modelo.getValueAt(i, 0) + "";
                String prod = modelo.getValueAt(i, 1) + "";
                String cant = modelo.getValueAt(i, 3) + "";
                String pu = modelo.getValueAt(i, 4) + "";
                String tot = modelo.getValueAt(i, 5) + "";

                sql = "INSERT INTO detalle_venta VALUES (" + noVenta + "," + codigo + ",'" + prod + "'," + cant + "," + pu + "," + tot + ",'PENDIENTE')";
                System.out.println("LLEGUE AQUI: \n" + sql);
                cmd = cn.prepareCall(sql);
                cmd.execute();
            }

            sql = "INSERT INTO detalle_abonos VALUES (" + noVenta + ",'" + efectivo + "','" + fecha + "','" + tipoPago + "')";
            System.out.println("LLEGUE AQUI: \n" + sql);
            cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
            //cn.close();


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en realizar venta 2\n" + ex);
        }
    }

    public void concretaVenta(String id, String cliente, String concepto, String fecha,
            String estado, String tipoPago, String estadoPago, String factu, String entregado, String usu) {

        try {
            String sql = "UPDATE ventas set fecha='" + fecha + "',concepto='" + concepto + "', "
                    + "estado='" + estado + "', tipoPago='" + tipoPago + "',"
                    + "estadoPago='" + estadoPago + "',factura='" + factu + "',estadoServicio='" + entregado + "', "
                    + "usuario='" + usu + "' where id=" + id;
            CallableStatement cmd = cn.prepareCall(sql);
            cmd.execute();

            cmd.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en concretar venta");
        }
    }

    public void concretaVenta2(String id, String cliente, String concepto, String fecha,
            String estado, String tipoPago, String estadoPago, String factu, String entregado, String efectivo, String usu) {
        try {
            String sql = "UPDATE ventas set fecha='" + fecha + "',concepto='" + concepto + "',estado='" + estado + "',"
                    + " tipoPago='" + tipoPago + "',estadoPago='" + estadoPago + "',factura='" + factu + "',estadoServicio='" + entregado + "',usuario='" + usu + "' where id=" + id;
            CallableStatement cmd = cn.prepareCall(sql);
            cmd.execute();

            sql = "INSERT INTO detalle_abonos VALUES (" + id + ",'" + efectivo + "','" + fecha + "','" + tipoPago + "')";
            cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en concretar venta 2\n" + ex);
        }
    }
}
