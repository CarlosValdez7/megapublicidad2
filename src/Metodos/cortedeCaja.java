/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Metodos;

import Conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author German Valdez
 */
public class cortedeCaja {

    Connection cn;

    public cortedeCaja() {
        Conexion con = new Conexion();
        cn = con.conectar();
    }

    public String sumEfectivo(String fecha) {
        try {
            String sql = "select Sum(abono) from detalle_abonos where fecha ='" + fecha + "' and tipoPago='EFECTIVO'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            rs.next();
            String suma = rs.getString("SUM(abono)");

            if (suma == null) {
                suma = "0";
            }
            sql = "select Sum(total) from ventas where fecha ='" + fecha + "' and tipoPago='EFECTIVO' and estadoPago='COMPLETO'";
            cmd = cn.prepareCall(sql);
            rs = cmd.executeQuery();
            rs.next();
            String suma2 = rs.getString("SUM(total)");

            if (suma2 == null) {
                suma2 = "0";
            }

            float total = Float.parseFloat(suma) + Float.parseFloat(suma2);
            cmd.close();

            return total + "";
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error suma de efectivo");
        }
        return null;
    }

    public String sumTarjeta(String fecha) {
        try {
            String sql = "select Sum(abono) from detalle_abonos where fecha ='" + fecha + "' and tipoPago='TARJETA'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            rs.next();
            String suma = rs.getString("SUM(abono)");

            if (suma == null) {
                suma = "0";
            }
            sql = "select Sum(total) from ventas where fecha ='" + fecha + "' and tipoPago='TARJETA' and estadoPago='COMPLETO'";
            cmd = cn.prepareCall(sql);
            rs = cmd.executeQuery();
            rs.next();
            String suma2 = rs.getString("SUM(total)");

            if (suma2 == null) {
                suma2 = "0";
            }

            float total = Float.parseFloat(suma) + Float.parseFloat(suma2);
            cmd.close();
            return total + "";
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error suma de tarjetas");
        }
        return null;
    }

    public String sumCheque(String fecha) {
        try {
            String sql = "select Sum(abono) from detalle_abonos where fecha ='" + fecha + "' and tipoPago='CHEQUE'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            rs.next();
            String suma = rs.getString("SUM(abono)");

            if (suma == null) {
                suma = "0";
            }

            sql = "select Sum(total) from ventas where fecha ='" + fecha + "' and tipoPago='CHEQUE' and estadoPago='COMPLETO'";
            cmd = cn.prepareCall(sql);
            rs = cmd.executeQuery();
            rs.next();
            String suma2 = rs.getString("SUM(total)");

            if (suma2 == null) {
                suma2 = "0";
            }

            float total = Float.parseFloat(suma) + Float.parseFloat(suma2);
            cmd.close();

            return total + "";
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error suma de cheques");
        }
        return null;
    }

    public String sumTrans(String fecha) {
        try {
            String sql = "select Sum(total) from ventas where fecha ='" + fecha + "' and (tipoPago='TRANSFERENCIA' and estadoPago='COMPLETO')";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            rs.next();
            String suma = rs.getString("SUM(total)");

            if (suma == null) {
                suma = "0";
            }
            cmd.close();

            return suma + "";
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error suma de cheques");
        }
        return null;
    }

    public void tablaCorteCaja(DefaultTableModel modelo, String fecha) {

        try {
            String sql = "select id, concepto, total,'Venta' from ventas where fecha='" + fecha + "' and estadoPago='COMPLETO'";
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

    public void tablaCorteCajaAbonos(DefaultTableModel modelo, String fecha) {

        try {
            String sql = "select d.idVenta, v.concepto, d.abono,'Anticipo/Liquidacion' from ventas v "
                    + "INNER JOIN detalle_abonos d on(v.id=d.idVenta) "
                    + "where d.fecha='" + fecha + "'";
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
            JOptionPane.showMessageDialog(null, "Error en tablaCorteCajaAbonos");
        }

    }

    public void tablaCorteCajaTipos(DefaultTableModel modelo, String fecha) {

        try {
            String sql = "SELECT p.tipo, SUM( d.total ) , v.fecha\n"
                    + " FROM detalle_venta d\n"
                    + " INNER JOIN productos p ON ( p.id = d.idProd ) \n"
                    + " INNER JOIN ventas v ON ( v.id = d.idVenta ) \n"
                    + " WHERE v.fecha =  '"+fecha+"' AND v.estadoPago='COMPLETO' \n"
                    + " GROUP BY p.tipo";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 2; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaCorteCajaAbonos");
        }

    }
}
