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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author German Valdez
 */
public class Registros {

    Connection cn;

    public Registros() {
        Conexion con = new Conexion();
        cn = con.conectar();
    }

    public void tablaPendientes(DefaultTableModel modelo) {
        try {
            String sql = "SELECT id,cliente,concepto,fecha,total,usuario FROM ventas where estadoServicio='PENDIENTE'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 6; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaOrdenes");
        }
    }

    // **************************** querys para cotizaciones blabla **********************************
    public void tablaOrdenes(DefaultTableModel modelo, String estadoVenta, String estadoServicio) {
        try {
            String sql = "SELECT id,cliente,concepto,fecha,total,usuario FROM ventas where (estado = '" + estadoVenta + "') " + estadoServicio;
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 6; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaOrdenes");
        }
    }

    public void tablaDetProd(DefaultTableModel modelo, String id) {
        try {
            String sql = "select idProd,producto,cantidad,pu,total from detalle_venta where idVenta=" + id;
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 5; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaDetProd");
        }
    }

    public void cancelarCoti(String id) {
        try {
            String sql = "update ventas set estado='CANCELADO',tipoPago='CANCELADO',estadoPago='CANCELADO',estadoServicio='CANCELADO' where id=" + id;
            System.out.print(sql);
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
            JOptionPane.showMessageDialog(null,"Cotización cancelada");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error -> Cancelar cotizacion");
        }
    }

    public String datosCliente(String cliente) {
        try {
            String sql = "Select * from clientes where nombre='" + cliente + "'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[18];
                for (int i = 0; i <= 9; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                String a = datos[0] + ","
                        + datos[1] + ","
                        + datos[2] + ","
                        + datos[3] + ","
                        + datos[4] + ","
                        + datos[5] + ","
                        + datos[6] + ","
                        + datos[7] + ","
                        + datos[8] + ","
                        + datos[9];
                return a;
            }

            cmd.close();
            //   cn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex + "Error -> Datos Cliente");
        }
        return null;
    }

    public String datosVenta(String id) {
        try {
            String sql = "Select concepto,total,fecha,estadoServicio,usuario,subtotal,descuento,impuestos,comentarios,estado from ventas where id=" + id;
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[18];
                for (int i = 0; i <= 9; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                String a = datos[0] + "," + ""
                        + datos[1] + "," + ""
                        + datos[2] + "," + ""
                        + datos[3] + "," + ""
                        + datos[4] + "," + ""
                        + datos[5] + "," + ""
                        + datos[6] + "," + ""
                        + datos[7] + "," + ""
                        + datos[8] + "," + ""
                        + datos[9];
               // System.out.println(a);
                return a;
            }

            cmd.close();
            //   cn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex + "Error -> Datos Venta");
        }
        return null;
    }

    public String calcularAnticipos(String id) {
        try {
            String sql = "select SUM(abono) from detalle_abonos where idVenta=" + id;
    //        System.out.println(sql);
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            rs.next();
            String anticipo = rs.getString("SUM(abono)");
      //      System.out.println("Anti: " + anticipo);
            cmd.close();
            return anticipo;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex + "Error -> calcularAnticipos");
        }
        return null;
    }
    
    public void tablaReportes(DefaultTableModel modelo,String where) {

        try {
            String sql = "SELECT id, cliente, concepto,total,fecha,usuario,tipoPago FROM ventas "+where;
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
            JOptionPane.showMessageDialog(null, "Error en tablaReportes");
        }
    }
    
    // ACTUALIZAR EL ESTADO A TERMINADO
    public void actualizaEstadoServ(String cod) {
        try {
            String sql = "update ventas set estadoServicio='TERMINADO' where id=" + cod;
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en actualizaEstadoServ");
        }
    }
    
    
    /**
     * ************************************* REGISTROS CONTADOR
     * **************************************
     */
    public void tablaFacturas(DefaultTableModel modelo) {
        try {
            String sql = "SELECT id,cliente,concepto,fecha,total FROM ventas where factura = 'SI'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 5; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaFacturas");
        }
    }

    public void tablaFacturasFIN(DefaultTableModel modelo) {
        try {
            String sql = "SELECT id,cliente,concepto,fecha,total FROM ventas where factura = 'FIN'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 5; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaFacturasFin");
        }
    }

    public void actualizaFactura(String cod) {
        try {
            String sql = "update ventas set factura='FIN' where id=" + cod;
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en actualizar factura");
        }
    }

    public void tablaTransferencias(DefaultTableModel modelo, String estado) {
        try {
            String sql = "SELECT id,cliente,concepto,fecha,total FROM ventas where tipoPago = 'TRANSFERENCIA' AND estadoPago='" + estado + "'";
            System.out.println(sql);
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 5; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaTransferencias");
        }
    }

    public void tablaTransferenciasC(DefaultTableModel modelo, String estado) {
        try {
            String sql = "SELECT id,cliente,concepto,fecha,total FROM ventas where tipoPago = 'TRANSFERENCIA' AND estadoPago='" + estado + "'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 5; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaTransferenciasC");
        }
    }

    public void transferenciaConcretada(String cod, String fecha) {
        try {
            String sql = "update ventas set estadoPago='COMPLETO', fecha='" + fecha + "' where id=" + cod;
            PreparedStatement cmd = cn.prepareCall(sql);
            cmd.execute();
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en transferenciaConcretada");
        }
    }

    public void busquedaFacturas(DefaultTableModel modelo, String palabra, String tipo, String estado) {
        try {

            String sql = "SELECT  id,cliente,concepto,fecha,total FROM ventas where factura = '" + estado + "' AND " + tipo + " like '" + palabra + "%'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[18];
                for (int i = 0; i < 5; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en busquedaFacturas");
        }
    }

    public void busquedaTrans(DefaultTableModel modelo, String palabra, String tipo, String estado) {
        try {
            String sql = "SELECT  id,cliente,concepto,fecha,total FROM ventas where tipoPago = 'TRANSFERENCIA' AND estadoPago='" + estado + "' AND " + tipo + " like '" + palabra + "%'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[18];
                for (int i = 0; i < 5; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en busquedaTrans");
        }
    }
    
    public void busquedaOrdenes(DefaultTableModel modelo, String tipo, String palabra) {
        try {
            String sql = "SELECT id,cliente,concepto,fecha,total,usuario FROM ventas where "+ tipo + " like '" + palabra + "%'";
            System.out.println(sql);
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                Object[] datos = new Object[10];
                for (int i = 0; i < 6; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo.addRow(datos);
            }
            cmd.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en tablaOrdenes");
        }
    }

    public String datos(String cliente) {
        try {
            String sql = "Select * from clientes where Nombre='" + cliente + "'";
            CallableStatement cmd = cn.prepareCall(sql);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                Object[] datos = new Object[18];
                for (int i = 0; i <= 9; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                String a = datos[0] + ","
                        + datos[1] + ","
                        + datos[2] + ","
                        + datos[3] + ","
                        + datos[4] + ","
                        + datos[5] + ","
                        + datos[6] + ","
                        + datos[7] + ","
                        + datos[8] + ","
                        + datos[9];
                return a;
            }

            cmd.close();
            //   cn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex + "Error -> Datos");
        }
        return null;
    }


}
