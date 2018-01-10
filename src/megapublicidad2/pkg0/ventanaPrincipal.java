/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package megapublicidad2.pkg0;

import Metodos.ArchivoPDF;
import Metodos.Generales;
import Metodos.Registros;
import Metodos.Ticket2;
import Metodos.Ventas;
import com.itextpdf.text.DocumentException;
import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author German Valdez
 */
public class ventanaPrincipal extends javax.swing.JFrame {

    Generales u = new Generales();
    Registros a = new Registros();
    ventanaLogin vlog;
    String fecha = "";
    String ico = "";
    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Creates new form ventanaPrincipal
     */
    public ventanaPrincipal(String usuario, String ico) {
        initComponents();
        setLocationRelativeTo(null);

        this.ico = ico;

        labelUsuarioLog.setText(usuario.toUpperCase());

        jButton43.setBackground(Color.getHSBColor(163, 238, 66));
        panelInicio.setVisible(true);
        panelClientes.setVisible(false);
        panelUsuarios.setVisible(false);
        panelVentas.setVisible(false);
        panelProductos.setVisible(false);
        panelFacturas.setVisible(false);
        panelUsuarios.setVisible(false);
        panelCotizaciones.setVisible(false);
        panelCorteCaja.setVisible(false);
        panelReportes.setVisible(false);
        panelContador.setVisible(false);

        fecha = new SimpleDateFormat("yyyy-MM-dd").format(GregorianCalendar.getInstance().getTime());

        cargarTablas();
    }

    public void cargarTablas() {
        llenaUsuarios();
        cargarCupones();
        cargarClientes();
        cargarProductos();
        iniciaCortes();
        iniciaOrdenes("general");
        cargarReportes("", "");
        iniciaFacturas();
        iniciaTransferencias();
        iniciaPendientes();

        if (!ico.equals(" ")) {
            String path = "";
            switch (ico) {
                case "2 ":
                    path = "/Imagenes/chica.png";
                    break;
                case "3 ":
                    path = "/Imagenes/chica2.png";
                    break;
                case "4 ":
                    path = "/Imagenes/chica3.png";
                    break;
                case "5 ":
                    path = "/Imagenes/chico.png";
                    break;
                case "6 ":
                    path = "/Imagenes/chico2.png";
                    break;
                case "1 ":
                    path = "/Imagenes/chico (3).png";
                    break;
            }
            URL url = this.getClass().getResource(path);
            ImageIcon icon = new ImageIcon(url);
            labelIconUsuario.setIcon(icon);
        }
        calcularExistencias();
    }

    public void agregarArticulo(String codigo, String articulo, String medidas, String cantidad, String pu, String importe) {
        DefaultTableModel modelo = (DefaultTableModel) tablaVenta.getModel();
        modelo.addRow(new Object[]{codigo, articulo, medidas, cantidad, pu, importe});
        sumaVenta();
    }

    public void agregarArticuloCotizacion(String codigo, String articulo, String medidas, String cantidad, String pu, String importe) {
        DefaultTableModel modelo = (DefaultTableModel) tablaVenta1.getModel();
        modelo.addRow(new Object[]{codigo, articulo, medidas, cantidad, pu, importe});
        sumaCotizacion();
    }

    public void llenaUsuarios() {
        limpiar(tablaUsuarios);
        DefaultTableModel modelo = (DefaultTableModel) tablaUsuarios.getModel();
        u.cargarUsu(modelo);
    }
    
    public void iniciaPendientes() {
        limpiar(tablaPendientes);
        DefaultTableModel modelo = (DefaultTableModel) tablaPendientes.getModel();
        a.tablaPendientes(modelo);
    }

    public void iniciaFacturas() {
        limpiar(tablaFacturas);
        DefaultTableModel modelo = (DefaultTableModel) tablaFacturas.getModel();
        if (jRadioButton6.isSelected()) {
            a.tablaFacturas(modelo);
        } else {
            a.tablaFacturasFIN(modelo);
        }
    }

    public void iniciaTransferencias() {
        limpiar(tablaTrans);
        DefaultTableModel modelo = (DefaultTableModel) tablaTrans.getModel();
        if (jRadioButton8.isSelected()) {
            a.tablaTransferencias(modelo, "PENDIENTE");
        } else {
            a.tablaTransferenciasC(modelo, "COMPLETO");
        }
    }

    public void cargarCupones() {
        limpiar(tablaCupones);
        DefaultTableModel modelo = (DefaultTableModel) tablaCupones.getModel();
        u.cargarCupones(modelo);
    }

    public void cargarClientes() {
        limpiar(tablaClientes);
        DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
        u.tablaClientes(modelo);
    }

    private void iniciaOrdenes(String estado) {
        String estadoVenta = "VENTA", estadoServicio = "";
        limpiar(tablaOrdenes);
        DefaultTableModel modelo = (DefaultTableModel) tablaOrdenes.getModel();
        if (estado.equals("general")) {
            estadoVenta = "VENTA";
        } else if (estado.equals("pendientes")) {
            estadoServicio = "and estadoServicio='PENDIENTE'";
        } else if (estado.equals("cancelados")) {
            estadoVenta = "CANCELADO";
        } else if (estado.equals("pagados")) {
            //Aqui es el estado del pago
            estadoServicio = "and estadoPago='COMPLETO'";
        } else if (estado.equals("cotizaciones")) {
            estadoVenta = "COTIZACION";
        }

        a.tablaOrdenes(modelo, estadoVenta, estadoServicio);
    }

    public void iniciaCortes() {
        limpiar(tablaCortes);
        DefaultTableModel modelo = (DefaultTableModel) tablaCortes.getModel();
        u.tablaCorteCaja(modelo);
    }

    public void cargarProductos() {
        limpiar(tablaServ);
        DefaultTableModel modelo = (DefaultTableModel) tablaServ.getModel();
        u.tablaProductos(modelo, "");
    }

    public void cargarReportes(String tipo, String where2) {
        limpiar(tablaReportes);
        String where = "";
        DefaultTableModel modelo = (DefaultTableModel) tablaReportes.getModel();
        switch (tipo) {
            case "General":
                where = " where estado='VENTA'";
                break;
            case "Pagados":
                where = " where estadoPago='COMPLETO'";
                break;
            case "Pendientes":
                where = " where estadoServicio='PENDIENTE'";
                break;
            case "Cancelados":
                where = " where estado='CANCELADO'";
                break;
            case "Cotizaciones":
                where = " where estado='COTIZACION'";
                break;
            case "Fechas":
                where = where2;
                break;

        }
        a.tablaReportes(modelo, where);
        sumaReportes();
    }

    private void limpiar(JTable tabla) {
        while (tabla.getRowCount() > 0) {
            ((DefaultTableModel) tabla.getModel()).removeRow(0);
        }
    }

    public void calcularDescuento(double porcentaje) {
        double cantidad = porcentaje / 100;
        double descuent = Double.parseDouble(txtTotal.getText()) * cantidad;
        double totalNuevo = Double.parseDouble(txtTotal.getText()) - descuent;

        txtDescuento.setText(df.format(descuent) + "");
        txtTotal.setText(df.format(totalNuevo) + "");
    }

    public void calcularDescuentoCoti(double porcentaje) {
        double cantidad = porcentaje / 100;
        double descuent = Double.parseDouble(txtTotal1.getText()) * cantidad;
        double totalNuevo = Double.parseDouble(txtTotal1.getText()) - descuent;

        txtDescuento1.setText(df.format(descuent) + "");
        txtTotal1.setText(df.format(totalNuevo) + "");
    }

    public void limpiarCampos() {
        txtNombreP.setText("");
        txtDesc.setText("");
        txtPI.setText("0");
        txtPI2.setText("0");
        txtMinima.setText("0");
        txtExistencias.setText("0");
    }

    public void limpiarVenta() {
        limpiar(tablaVenta);
        txtSub.setText("");
        txtDescuento.setText("0.00");
        txtImpuestos.setText("");
        txtTotal.setText("");
        txtComentarios.setText("");
        labelTema.setText("-");
        //agregar cliente publico de nuevo
        tCliente.setText("");
        tNombre.setText("Publico General");
        tRepre.setText("-");
        tEmail.setText("-");
        tTelefono.setText("-");
        tRFC.setText("-");
        tDire.setText("-");
    }

    public void limpiarCotizacion() {
        limpiar(tablaVenta1);
        txtSub1.setText("");
        txtDescuento1.setText("0.00");
        txtImpuestos1.setText("");
        txtTotal1.setText("");
        txtComentarios1.setText("");
        labelTema1.setText("-");
        //agregar cliente publico de nuevo
        tCliente1.setText("");
        tNombre1.setText("Publico General");
        tRepre1.setText("-");
        tEmail1.setText("-");
        tTelefono1.setText("-");
        tRFC1.setText("-");
        tDire1.setText("-");
    }

    public void calcularExistencias() {
        DefaultTableModel modelo = (DefaultTableModel) tablaServ.getModel();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (Float.parseFloat(modelo.getValueAt(i, 7) + "") >= Float.parseFloat(modelo.getValueAt(i, 8) + "")) {
                JOptionPane.showMessageDialog(this, "Revisa tu inventario: " + (modelo.getValueAt(i, 2) + ""));
            }
        }
    }

    public void sumaVenta() {
        DefaultTableModel modelo = (DefaultTableModel) tablaVenta.getModel();

        float total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total = total + Float.parseFloat(modelo.getValueAt(i, 5) + "");
        }
        txtTotal.setText(df.format(total));
    }

    public void sumaReportes() {
        DefaultTableModel modelo = (DefaultTableModel) tablaReportes.getModel();

        float total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total = total + Float.parseFloat(modelo.getValueAt(i, 3) + "");
        }
        reportesTotal.setText(df.format(total));
    }

    public void sumaCotizacion() {
        DefaultTableModel modelo = (DefaultTableModel) tablaVenta1.getModel();

        float total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total = total + Float.parseFloat(modelo.getValueAt(i, 5) + "");
        }
        txtTotal1.setText(df.format(total));
    }

    public Object[][] obtenerArticulos(DefaultTableModel modelo) {
        Object[][] articulos = null;
        articulos = new Object[modelo.getRowCount()][modelo.getColumnCount()];
        for (int i = 0; i < modelo.getRowCount(); i++) {
            for (int j = 0; j < modelo.getColumnCount(); j++) {
                articulos[i][j] = modelo.getValueAt(i, j);
            }
        }
        return articulos;
    }

    public void cobrar(String cambio, String efectivo, String estadoPago, String tipoPago, String totalPago, String factu, String entregado) {
        Ventas v = new Ventas();
        ArchivoPDF a = new ArchivoPDF();
        String status = estadoPago;
        String iva = "", pagos = "";

        if (tipoPago.equals("TRANSFERENCIA")) {
            estadoPago = "PENDIENTE";
        }

        if (tipoPago.equals("EFECTIVO")) {
            pagos = "EFECTIVO:       $" + efectivo + "\n"
                    + "TARJETA:        $0.00 \n"
                    + "CHEQUE:         $0.00\n";
        }

        if (tipoPago.equals("TARJETA")) {
            pagos = "EFECTIVO:       $0.00\n"
                    + "TARJETA:        $" + efectivo + " \n"
                    + "CHEQUE:         $0.00\n";
        }


        if (tipoPago.equals("CHEQUE")) {
            pagos = "EFECTIVO:       $0.00\n"
                    + "TARJETA:        $0.00\n"
                    + "CHEQUE:         $" + efectivo;
        }


        DefaultTableModel modelo = (DefaultTableModel) tablaVenta.getModel();

        SimpleDateFormat fTicket = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat fBase = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hora = new SimpleDateFormat("hh:mm a");
        Calendar cal = GregorianCalendar.getInstance();

        String fechaTicket = fTicket.format(cal.getTime());
        String fechaBase = fBase.format(cal.getTime());

        String horaTicket = hora.format(cal.getTime());

        //  ven = new Venta();

        String nombre = tNombre.getText();
        if (status.equals("COMPLETO")) {

            v.RealizaVenta(modelo, nombre, labelTema.getText(), totalPago, fechaBase, "VENTA",
                    tipoPago, estadoPago, factu, entregado, labelUsuarioLog.getText(),
                    txtSub.getText(), txtDescuento.getText(), txtImpuestos.getText(), txtComentarios.getText());

            String tic =
                    "         MEGAPUBLICIDAD\n"
                    + "       DUCJ68TT22GM7\n\n"
                    + "Av. Juan Escutia 134-A \n"
                    + "Zona Centro\n"
                    + "C.P. 63000 TEPIC, NAYARIT\n"
                    + "meganayarit@gmail.com\n"
                    + "311-218-0320\n"
                    + "================================\n"
                    + "    **** NOTA DE VENTA ***\n"
                    + "FECHA: " + fechaTicket + "  " + horaTicket + "\n"
                    + "NO. DE VENTA: " + v.folioVenta() + "\n"
                    + "================================\n"
                    + "CODIGO   CANT\tP.UNIT\tIMPORTE\n"
                    + "================================\n";

            int totArt = 0;
            for (int i = 0; i < tablaVenta.getRowCount(); i++) {
                String cod = tablaVenta.getValueAt(i, 0) + "";
                String prod = tablaVenta.getValueAt(i, 1) + "";
                float can = Float.parseFloat(tablaVenta.getValueAt(i, 3) + "");
                String pu = tablaVenta.getValueAt(i, 4) + "";
                String tot = tablaVenta.getValueAt(i, 5) + "";
                tic +=
                        cod + "   " + prod + "\n"
                        + "         " + can + "\t" + pu + "\t" + tot + "\n";
                totArt += can;
            }

            tic += //"\nA PAGAR:\t" + "$" + labelTotal.getText() + "\n"
                    //+ "\tDESCUENTO:\t" + lblDesc.getText() + "\n"
                    "\nSUBTOTAL:\t" + "$" + txtSub.getText() + "\n"
                    + "IMPUESTOS:\t" + "$" + txtImpuestos.getText() + "\n"
                    + "DESCUENTO:\t" + "$" + txtDescuento.getText() + "\n"
                    + "TOTAL:\t\t" + "$" + txtTotal.getText() + "\n"
                    + "PAGADO:\t\t" + "$" + efectivo + "\n"
                    + "CAMBIO:\t\t" + "$" + cambio + "\n"
                    + "  >>>>> TIPO DE PAGO <<<<<  \n"
                    + "" + pagos + "\n\n"
                    + "ARTICULOS VENDIDOS: " + totArt + "\n"
                    + "================================\n"
                    + "                                \n"
                    + " !!! GRACIAS POR SU COMPRA !!!\n\n\n\n";

            System.out.println(tic);

            Ticket2 p = new Ticket2(tic);

            JOptionPane.showMessageDialog(null, "Venta/Apartado finalizado");

            Object[][] articulos = obtenerArticulos(modelo);

            try {
                a.pdfCotización(labelTema.getText(), v.folioVenta(), labelTema.getText(), articulos, "        Venta", fechaBase, txtTotal.getText(),
                        tNombre.getText(), tRepre.getText(), tRFC.getText(), tDire.getText(), tEmail.getText(),
                        txtComentarios.getText(), txtImpuestos.getText(), txtSub.getText(), txtDescuento.getText(), labelUsuarioLog.getText());
            } catch (DocumentException ex) {
                Logger.getLogger(ventanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ventanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(new java.io.File("W:\\megapublicidad2.0\\documentos\\" + labelTema.getText() + ".PDF"));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "No se puede abrir archivo." + ex.getMessage());
            }

        } else {
            //String idventa = ven.create(tNombre.getText(), concepto, labelTotal.getText(), fechaBase, status, tipoPago, estadoPago) + "";
            v.RealizaVenta2(modelo, efectivo, nombre, labelTema.getText(), totalPago, fechaBase, "VENTA",
                    tipoPago, estadoPago, factu, entregado, labelUsuarioLog.getText(),
                    txtSub.getText(), txtDescuento.getText(), txtImpuestos.getText(), txtComentarios.getText());

            String tic =
                    "           ANTICIPO               \n"
                    + "       MEGAPUBLICIDAD           \n"
                    + "       DUCJ68TT22GM7          \n\n"
                    + "Av. Juan Escutia 134-A          \n"
                    + "Zona Centro                     \n"
                    + "C.P. 63000 TEPIC, NAYARIT       \n"
                    + "meganayarit@gmail.com           \n"
                    + "311-218-0320\n"
                    + "================================\n"
                    + "    **** NOTA DE VENTA ***\n"
                    + "FECHA: " + fechaTicket + "  " + horaTicket + "\n"
                    + "NO. DE VENTA: " + v.folioVenta() + "\n"
                    + "================================\n"
                    + "CODIGO   CANT\tP.UNIT\tIMPORTE\n"
                    + "================================\n";

            int totArt = 0;
            for (int i = 0; i < tablaVenta.getRowCount(); i++) {
                String cod = tablaVenta.getValueAt(i, 0) + "";
                String prod = tablaVenta.getValueAt(i, 1) + "";
                float can = Float.parseFloat(tablaVenta.getValueAt(i, 3) + "");
                String pu = tablaVenta.getValueAt(i, 4) + "";
                String tot = tablaVenta.getValueAt(i, 5) + "";
                tic +=
                        cod + "   " + prod + "\n"
                        + "         " + can + "\t" + pu + "\t" + tot + "\n";
                totArt += can;

            }

            tic += //"\nTOTAL:\t\t" + "$" + labelTotal.getText() + "\n"
                    //  + "\tDESCUENTO:\t" + lblDesc.getText() + "\n"
                    "\nSUBTOTAL:\t" + "$" + txtSub.getText() + "\n"
                    + "IMPUESTOS:\t" + "$" + txtImpuestos.getText() + "\n"
                    + "TOTAL:\t\t" + "$" + txtTotal.getText() + "\n"
                    + "DEJÓ:\t\t" + "$" + efectivo + "\n"
                    + "RESTANTE:\t" + "$" + cambio + "\n"
                    + "  >>>>> TIPO DE PAGO <<<<<  \n"
                    + "" + pagos + "\n\n"
                    + "ARTICULOS APARTADOS: " + totArt + "\n"
                    + "================================\n"
                    + "       \n"
                    + " !!! GRACIAS POR SU COMPRA !!!\n\n\n\n";

            System.out.println(tic);

            Ticket2 t = new Ticket2(tic);

            JOptionPane.showMessageDialog(null, "Venta/Apartado finalizado");

            /*if (jRadioButton1.isSelected()) {
             iva = "S";
             } else {
             iva = "N";
             }
             */
            Object[][] articulos = obtenerArticulos(modelo);

            try {
                a.pdfCotización(labelTema.getText(), v.folioVenta(), labelTema.getText(), articulos, "        Venta", fechaBase, txtTotal.getText(),
                        tNombre.getText(), tRepre.getText(), tRFC.getText(), tDire.getText(), tEmail.getText(),
                        txtComentarios.getText(), txtImpuestos.getText(), txtSub.getText(), txtDescuento.getText(), labelUsuarioLog.getText());
            } catch (DocumentException ex) {
                Logger.getLogger(ventanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ventanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(new java.io.File("W:\\megapublicidad2.0\\documentos\\" + labelTema.getText() + ".PDF"));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "No se puede abrir archivo." + ex.getMessage());
            }

        }

        // reiniciar();


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        panelContador = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel23 = new javax.swing.JPanel();
        ventanaVentas = new javax.swing.JScrollPane();
        tablaFacturas = new javax.swing.JTable();
        jComboBox2 = new javax.swing.JComboBox();
        txtBus1 = new javax.swing.JTextField();
        jButton44 = new javax.swing.JButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        ventanaVentas1 = new javax.swing.JScrollPane();
        tablaTrans = new javax.swing.JTable();
        jComboBox3 = new javax.swing.JComboBox();
        txtBus2 = new javax.swing.JTextField();
        jButton49 = new javax.swing.JButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        panelProductos = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtNombreP = new javax.swing.JTextField();
        txtPI = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtDesc = new javax.swing.JTextField();
        comboTipo = new javax.swing.JComboBox();
        txtPI2 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtMinima = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtExistencias = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        radioGranFormato = new javax.swing.JRadioButton();
        radioProductos = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaServ = new javax.swing.JTable();
        combo1 = new javax.swing.JComboBox();
        txtBusquedaProductos = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        panelReportes = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        primeraFecha = new com.toedter.calendar.JDateChooser();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        segundaFecha = new com.toedter.calendar.JDateChooser();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        tablaReportes = new javax.swing.JTable();
        labelAviso = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        reportesTotal = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        panelCotizaciones = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        tCliente1 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        tNombre1 = new javax.swing.JTextField();
        tRepre1 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        tEmail1 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        tRFC1 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        tDire1 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        tTelefono1 = new javax.swing.JTextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        tablaVenta1 = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtComentarios1 = new javax.swing.JTextArea();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txtSub1 = new javax.swing.JTextField();
        txtImpuestos1 = new javax.swing.JTextField();
        txtDescuento1 = new javax.swing.JTextField();
        txtTotal1 = new javax.swing.JTextField();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        labelTema1 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jButton29 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jButton52 = new javax.swing.JButton();
        panelVentas = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        tCliente = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        tNombre = new javax.swing.JTextField();
        tRepre = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        tEmail = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        tRFC = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        tDire = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        tTelefono = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        tablaVenta = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtComentarios = new javax.swing.JTextArea();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jLabel59 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        labelTema = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        txtSub = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtImpuestos = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jButton46 = new javax.swing.JButton();
        jButton51 = new javax.swing.JButton();
        panelClientes = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtNumCliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtRepre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtRFC = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDir = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtCE = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtMunicipio = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCP = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();
        combo = new javax.swing.JComboBox();
        txtBus = new javax.swing.JTextField();
        jButton47 = new javax.swing.JButton();
        panelUsuarios = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaUsuarios = new javax.swing.JTable();
        txtNombreU = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtContra = new javax.swing.JTextField();
        cmbSucursal = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jBAñadir = new javax.swing.JButton();
        jBEliminar = new javax.swing.JButton();
        jBLimpiar = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        labelIcono = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tablaCupones = new javax.swing.JTable();
        jLabel55 = new javax.swing.JLabel();
        jButton45 = new javax.swing.JButton();
        jLabel56 = new javax.swing.JLabel();
        txtCupon = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtCuponDesc = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        fechaCupon = new com.toedter.calendar.JDateChooser();
        panelFacturas = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaOrdenes = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        panelCorteCaja = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaCortes = new javax.swing.JTable();
        jButton19 = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        panelInicio = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tablaPendientes = new javax.swing.JTable();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        labelUsuarioLog = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        labelIconUsuario = new javax.swing.JLabel();
        jButton43 = new javax.swing.JButton();
        jButton50 = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelContador.setOpaque(false);

        jPanel23.setOpaque(false);

        tablaFacturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Cliente", "Concepto", "Fecha", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaFacturasMouseClicked(evt);
            }
        });
        ventanaVentas.setViewportView(tablaFacturas);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cliente", "Concepto", "Fecha", "Total" }));

        txtBus1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBus1KeyReleased(evt);
            }
        });

        jButton44.setText("Abrir");
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButton6);
        jRadioButton6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jRadioButton6.setForeground(new java.awt.Color(0, 51, 51));
        jRadioButton6.setSelected(true);
        jRadioButton6.setText("Pendientes");
        jRadioButton6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton6ItemStateChanged(evt);
            }
        });

        buttonGroup3.add(jRadioButton7);
        jRadioButton7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jRadioButton7.setForeground(new java.awt.Color(0, 51, 51));
        jRadioButton7.setText("Concretadas");
        jRadioButton7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton7ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ventanaVentas, javax.swing.GroupLayout.DEFAULT_SIZE, 966, Short.MAX_VALUE)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBus1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jRadioButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton7)))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBus1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton44)
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ventanaVentas, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Facturación", jPanel23);

        jPanel24.setOpaque(false);

        jPanel25.setOpaque(false);

        tablaTrans.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Cliente", "Concepto", "Fecha", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaTrans.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaTransMouseClicked(evt);
            }
        });
        ventanaVentas1.setViewportView(tablaTrans);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cliente", "Concepto", "Fecha", "Total" }));

        txtBus2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBus2KeyReleased(evt);
            }
        });

        jButton49.setText("Abrir");
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });

        buttonGroup4.add(jRadioButton8);
        jRadioButton8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jRadioButton8.setSelected(true);
        jRadioButton8.setText("Pendientes");
        jRadioButton8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton8ItemStateChanged(evt);
            }
        });

        buttonGroup4.add(jRadioButton9);
        jRadioButton9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jRadioButton9.setText("Concretadas");
        jRadioButton9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton9ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBus2, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 372, Short.MAX_VALUE)
                        .addComponent(jRadioButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton9))
                    .addComponent(ventanaVentas1))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBus2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton49)
                    .addComponent(jRadioButton8)
                    .addComponent(jRadioButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ventanaVentas1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 986, Short.MAX_VALUE)
            .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel24Layout.createSequentialGroup()
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
            .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel24Layout.createSequentialGroup()
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("Transferencias", jPanel24);

        javax.swing.GroupLayout panelContadorLayout = new javax.swing.GroupLayout(panelContador);
        panelContador.setLayout(panelContadorLayout);
        panelContadorLayout.setHorizontalGroup(
            panelContadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContadorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelContadorLayout.setVerticalGroup(
            panelContadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContadorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        jPanel1.add(panelContador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1010, 540));

        panelProductos.setBackground(new java.awt.Color(255, 255, 255));
        panelProductos.setOpaque(false);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));
        jPanel5.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Tipo");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Producto");

        txtPI.setText("0");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Precio General");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("Descripcion");

        comboTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione..", "Acabados", "Lonas", "Viniles de Impresion", "Viniles de Corte", "OFFSET", "Impresion digital", "Display", "Diseño", "M.O/Instalaciones", "Estructura y Equipo" }));
        comboTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboTipoItemStateChanged(evt);
            }
        });

        txtPI2.setText("0");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Precio Maquila");

        txtMinima.setText("0");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setText("Cantidad Minima");

        txtExistencias.setText("0");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setText("Existencias");

        buttonGroup1.add(radioGranFormato);
        radioGranFormato.setText("Gran Formato");

        buttonGroup1.add(radioProductos);
        radioProductos.setSelected(true);
        radioProductos.setText("Productos");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(148, 148, 148)
                        .addComponent(jLabel12))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(radioGranFormato)
                            .addComponent(radioProductos))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreP, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPI, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(txtPI2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(8, 8, 8)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(txtMinima, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(txtExistencias, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel1))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(5, 5, 5)
                        .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(5, 5, 5)
                        .addComponent(txtPI2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(radioGranFormato))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(radioProductos)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(5, 5, 5)
                        .addComponent(txtMinima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(5, 5, 5)
                        .addComponent(txtExistencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));
        jPanel6.setOpaque(false);

        jButton7.setText("Guardar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Eliminar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Modificar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton18.setText("Stock");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setOpaque(false);

        tablaServ.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Categoria", "Nombre", "Descripcion", "Precio General", "Precio Maquila", "Tipo", "Cantidad Minima", "Existencias"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaServ.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaServMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaServ);

        combo1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nombre", "Tipo" }));

        txtBusquedaProductos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaProductosKeyReleased(evt);
            }
        });

        jDateChooser1.setDate(GregorianCalendar.getInstance().getTime());
        jDateChooser1.setOpaque(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(combo1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtBusquedaProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(combo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBusquedaProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout panelProductosLayout = new javax.swing.GroupLayout(panelProductos);
        panelProductos.setLayout(panelProductosLayout);
        panelProductosLayout.setHorizontalGroup(
            panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelProductosLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        panelProductosLayout.setVerticalGroup(
            panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(panelProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 990, 540));

        panelReportes.setBackground(new java.awt.Color(255, 255, 255));
        panelReportes.setOpaque(false);

        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel18.setOpaque(false);
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonGroup2.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("General");
        jRadioButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton1ItemStateChanged(evt);
            }
        });
        jPanel18.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        buttonGroup2.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jRadioButton2.setText("Pendientes");
        jRadioButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton2ItemStateChanged(evt);
            }
        });
        jPanel18.add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 50, -1, -1));

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jRadioButton3.setText("Cancelados");
        jRadioButton3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton3ItemStateChanged(evt);
            }
        });
        jPanel18.add(jRadioButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 50, -1, -1));

        primeraFecha.setOpaque(false);
        jPanel18.add(primeraFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 40, 140, -1));

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel50.setText("Fecha Inicial");
        jPanel18.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, -1, -1));

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel51.setText("Filtros de busqueda:");
        jPanel18.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel52.setText("Fecha Final");
        jPanel18.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 20, -1, -1));

        segundaFecha.setOpaque(false);
        jPanel18.add(segundaFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 40, 140, -1));

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jRadioButton4.setText("Cotizaciones");
        jRadioButton4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton4ItemStateChanged(evt);
            }
        });
        jPanel18.add(jRadioButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(272, 50, -1, -1));

        buttonGroup2.add(jRadioButton5);
        jRadioButton5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jRadioButton5.setText("Pagados");
        jRadioButton5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton5ItemStateChanged(evt);
            }
        });
        jPanel18.add(jRadioButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 50, -1, -1));

        jButton40.setText("Consultar");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });
        jPanel18.add(jButton40, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 40, -1, -1));

        jButton41.setText("Consultar");
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });
        jPanel18.add(jButton41, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 40, -1, -1));

        tablaReportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Cliente", "Tema", "Total", "Fecha", "Usuario", "Pago"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(tablaReportes);

        labelAviso.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        labelAviso.setText("Listado General");

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel53.setText("TOTAL $");

        reportesTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        reportesTotal.setText("-");

        jButton17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf chico.png"))); // NOI18N
        jButton17.setBorderPainted(false);
        jButton17.setContentAreaFilled(false);
        jButton17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton17.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelReportesLayout = new javax.swing.GroupLayout(panelReportes);
        panelReportes.setLayout(panelReportesLayout);
        panelReportesLayout.setHorizontalGroup(
            panelReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
                    .addComponent(jScrollPane8)
                    .addGroup(panelReportesLayout.createSequentialGroup()
                        .addComponent(labelAviso)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelReportesLayout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reportesTotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton17)))
                .addContainerGap())
        );
        panelReportesLayout.setVerticalGroup(
            panelReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(labelAviso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(reportesTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(panelReportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1010, 540));

        panelCotizaciones.setBackground(new java.awt.Color(255, 255, 255));
        panelCotizaciones.setOpaque(false);

        jPanel15.setOpaque(false);

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel37.setText("#");

        tCliente1.setEditable(false);

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel38.setText("Nombre");

        tNombre1.setText("Publico en General");

        tRepre1.setText("Publico en General");

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel39.setText("Representante");

        tEmail1.setText("-");

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel40.setText("Email");

        tRFC1.setText("-");

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel41.setText("RFC");

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel42.setText("Direccion");

        tDire1.setText("-");

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel43.setText("Telefono");

        tTelefono1.setText("-");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(tCliente1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addComponent(tNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(tRepre1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tEmail1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tRFC1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tDire1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tTelefono1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel43)
                                .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tRFC1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tDire1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tTelefono1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jLabel37)
                                .addGap(9, 9, 9))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(6, 6, 6)))
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tRepre1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tEmail1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaVenta1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Producto/Servicio", "Medidas", "Cantidad", "Precio Unitario", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(tablaVenta1);
        tablaVenta1.getColumnModel().getColumn(0).setMinWidth(25);
        tablaVenta1.getColumnModel().getColumn(0).setPreferredWidth(30);
        tablaVenta1.getColumnModel().getColumn(0).setMaxWidth(35);

        txtComentarios1.setColumns(20);
        txtComentarios1.setRows(5);
        jScrollPane7.setViewportView(txtComentarios1);

        jPanel16.setBackground(new java.awt.Color(112, 144, 244));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 996, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel17.setOpaque(false);
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("SUBTOTAL");
        jPanel17.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 24, 65, -1));

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("IMPUESTOS");
        jPanel17.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 55, 65, -1));

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("DESCUENTO");
        jPanel17.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 24, 65, -1));

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("TOTAL");
        jPanel17.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 51, 65, -1));
        jPanel17.add(txtSub1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 18, 103, -1));
        jPanel17.add(txtImpuestos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 103, -1));

        txtDescuento1.setText("0.00");
        jPanel17.add(txtDescuento1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 18, 103, -1));
        jPanel17.add(txtTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 103, -1));

        jButton31.setBackground(new java.awt.Color(104, 215, 141));
        jButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/granformato.png"))); // NOI18N
        jButton31.setBorderPainted(false);
        jButton31.setContentAreaFilled(false);
        jButton31.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton31.setOpaque(true);
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jButton32.setBackground(new java.awt.Color(153, 153, 255));
        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/borrar.png"))); // NOI18N
        jButton32.setBorderPainted(false);
        jButton32.setContentAreaFilled(false);
        jButton32.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton32.setOpaque(true);
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton33.setBackground(new java.awt.Color(255, 102, 102));
        jButton33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/basura.png"))); // NOI18N
        jButton33.setBorderPainted(false);
        jButton33.setContentAreaFilled(false);
        jButton33.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton33.setOpaque(true);
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton34.setBackground(new java.awt.Color(102, 153, 255));
        jButton34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/carrito.png"))); // NOI18N
        jButton34.setBorderPainted(false);
        jButton34.setContentAreaFilled(false);
        jButton34.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton34.setOpaque(true);
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jButton35.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/descuento.png"))); // NOI18N
        jButton35.setText("Aplicar Descuento");
        jButton35.setBorderPainted(false);
        jButton35.setContentAreaFilled(false);
        jButton35.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton35.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton35.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jButton36.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/impuestos_1.png"))); // NOI18N
        jButton36.setText("Agregar Impuestos");
        jButton36.setBorderPainted(false);
        jButton36.setContentAreaFilled(false);
        jButton36.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton36.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton36.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        jButton37.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        jButton37.setText("Guardar");
        jButton37.setBorderPainted(false);
        jButton37.setContentAreaFilled(false);
        jButton37.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton37.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton37.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel44.setText("Tema: ");
        jLabel44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel44MouseClicked(evt);
            }
        });

        labelTema1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelTema1.setText("-");

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel60.setText("Comentarios");

        jButton29.setText("+");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton48.setText("Seleccionar");
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });

        jButton52.setBackground(new java.awt.Color(227, 238, 115));
        jButton52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/balas (1).png"))); // NOI18N
        jButton52.setBorderPainted(false);
        jButton52.setContentAreaFilled(false);
        jButton52.setOpaque(true);
        jButton52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton52ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCotizacionesLayout = new javax.swing.GroupLayout(panelCotizaciones);
        panelCotizaciones.setLayout(panelCotizacionesLayout);
        panelCotizacionesLayout.setHorizontalGroup(
            panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelCotizacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCotizacionesLayout.createSequentialGroup()
                        .addComponent(jButton36)
                        .addGap(47, 47, 47)
                        .addComponent(jButton35)
                        .addGap(47, 47, 47)
                        .addComponent(jButton37)
                        .addGap(135, 135, 135)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelCotizacionesLayout.createSequentialGroup()
                                .addComponent(jLabel44)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelTema1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton52)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton33))
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 996, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelCotizacionesLayout.createSequentialGroup()
                            .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton48))
                        .addComponent(jLabel60)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 996, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 11, Short.MAX_VALUE))
        );
        panelCotizacionesLayout.setVerticalGroup(
            panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCotizacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton29)
                    .addComponent(jButton48))
                .addGap(1, 1, 1)
                .addGroup(panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCotizacionesLayout.createSequentialGroup()
                        .addGroup(panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton31)
                                .addGroup(panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton34)
                                    .addComponent(jButton33)
                                    .addComponent(jButton32)))
                            .addGroup(panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel44)
                                .addComponent(labelTema1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton37)
                            .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelCotizacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton36)
                                .addComponent(jButton35)))
                        .addGap(97, 97, 97))
                    .addGroup(panelCotizacionesLayout.createSequentialGroup()
                        .addComponent(jButton52)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel1.add(panelCotizaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1010, 540));

        panelVentas.setOpaque(false);

        jPanel8.setOpaque(false);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setText("#");

        tCliente.setEditable(false);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel19.setText("Nombre");

        tNombre.setText("Publico en General");

        tRepre.setText("Publico en General");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel20.setText("Representante");

        tEmail.setText("-");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel21.setText("Email");

        tRFC.setText("-");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel22.setText("RFC");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel23.setText("Direccion");

        tDire.setText("-");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel24.setText("Telefono");

        tTelefono.setText("-");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(tCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(tNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(tRepre, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tDire, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(tTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(9, 9, 9)
                        .addComponent(tCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tRepre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel24)
                                .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel22)
                                .addComponent(jLabel21)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tRFC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tDire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(81, 81, 81))
        );

        tablaVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Producto/Servicio", "Medidas", "Cantidad", "Precio Unitario", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(tablaVenta);
        tablaVenta.getColumnModel().getColumn(0).setMinWidth(25);
        tablaVenta.getColumnModel().getColumn(0).setPreferredWidth(30);
        tablaVenta.getColumnModel().getColumn(0).setMaxWidth(35);

        jPanel9.setBackground(new java.awt.Color(112, 144, 244));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 999, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jButton11.setBackground(new java.awt.Color(102, 153, 255));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/carrito.png"))); // NOI18N
        jButton11.setBorderPainted(false);
        jButton11.setContentAreaFilled(false);
        jButton11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton11.setOpaque(true);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        txtComentarios.setColumns(20);
        txtComentarios.setRows(5);
        jScrollPane3.setViewportView(txtComentarios);

        jButton24.setBackground(new java.awt.Color(255, 102, 102));
        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/basura.png"))); // NOI18N
        jButton24.setBorderPainted(false);
        jButton24.setContentAreaFilled(false);
        jButton24.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton24.setOpaque(true);
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton25.setBackground(new java.awt.Color(104, 215, 141));
        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/granformato.png"))); // NOI18N
        jButton25.setBorderPainted(false);
        jButton25.setContentAreaFilled(false);
        jButton25.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton25.setOpaque(true);
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton26.setBackground(new java.awt.Color(153, 153, 255));
        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/borrar.png"))); // NOI18N
        jButton26.setBorderPainted(false);
        jButton26.setContentAreaFilled(false);
        jButton26.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton26.setOpaque(true);
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel59.setText("Comentarios");

        jButton16.setText("+");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        labelTema.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelTema.setText("-");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel32.setText("Tema: ");
        jLabel32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel32MouseClicked(evt);
            }
        });

        jPanel22.setOpaque(false);

        jButton12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/impuestos_1.png"))); // NOI18N
        jButton12.setText("Agregar Impuestos");
        jButton12.setBorderPainted(false);
        jButton12.setContentAreaFilled(false);
        jButton12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setIconTextGap(5);
        jButton12.setVerifyInputWhenFocusTarget(false);
        jButton12.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/descuento.png"))); // NOI18N
        jButton13.setText("Aplicar Descuento");
        jButton13.setBorderPainted(false);
        jButton13.setContentAreaFilled(false);
        jButton13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setIconTextGap(5);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/dinero.png"))); // NOI18N
        jButton15.setText("Realizar Pago");
        jButton15.setBorderPainted(false);
        jButton15.setContentAreaFilled(false);
        jButton15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton15.setIconTextGap(5);
        jButton15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("SUBTOTAL");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("IMPUESTOS");

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("TOTAL");

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("DESCUENTO");

        txtDescuento.setText("0.0");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jButton12)
                .addGap(45, 45, 45)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jButton15)
                .addGap(131, 131, 131)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtImpuestos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSub, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton12)
                    .addComponent(jButton15)
                    .addComponent(jButton13))
                .addGap(33, 33, 33))
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel35)
                    .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txtImpuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton46.setText("Seleccionar");
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        jButton51.setBackground(new java.awt.Color(227, 238, 115));
        jButton51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/balas (1).png"))); // NOI18N
        jButton51.setBorderPainted(false);
        jButton51.setContentAreaFilled(false);
        jButton51.setOpaque(true);
        jButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton51ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelVentasLayout = new javax.swing.GroupLayout(panelVentas);
        panelVentas.setLayout(panelVentasLayout);
        panelVentasLayout.setHorizontalGroup(
            panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVentasLayout.createSequentialGroup()
                .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelVentasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelVentasLayout.createSequentialGroup()
                                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton46))
                            .addGroup(panelVentasLayout.createSequentialGroup()
                                .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 999, Short.MAX_VALUE))
                                    .addComponent(jLabel59)
                                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelVentasLayout.createSequentialGroup()
                                            .addComponent(jLabel32)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(labelTema)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton51)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jButton25)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jButton11)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jButton26)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jButton24))
                                        .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 999, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        panelVentasLayout.setVerticalGroup(
            panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton16)
                    .addComponent(jButton46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVentasLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(labelTema)))
                    .addComponent(jButton25)
                    .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton11)
                        .addComponent(jButton24)
                        .addComponent(jButton26))
                    .addComponent(jButton51))
                .addGap(11, 11, 11)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(panelVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1010, 540));

        panelClientes.setBackground(new java.awt.Color(255, 255, 255));
        panelClientes.setOpaque(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Generales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.Color.white));
        jPanel2.setOpaque(false);

        txtNumCliente.setEditable(false);
        txtNumCliente.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Numero de Cliente");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Nombre");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Representante");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("RFC");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Direccion");

        txtDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDirActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Correo Electronico");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Municipio");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("C.P.");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Estado");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Telefono");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));
        jPanel3.setOpaque(false);

        jButton1.setText("Guardar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Limpiar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Eliminar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Modificar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtNumCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(txtRepre, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                            .addComponent(txtRFC)
                            .addComponent(txtNombre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)))
                            .addComponent(txtDir)
                            .addComponent(txtCE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(txtMunicipio, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                            .addComponent(txtCP)
                            .addComponent(txtEstado))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(5, 5, 5)
                        .addComponent(txtNumCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(3, 3, 3)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel4)
                                .addGap(3, 3, 3)
                                .addComponent(txtRepre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel5)
                                .addGap(1, 1, 1)
                                .addComponent(txtRFC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(3, 3, 3)
                                .addComponent(txtDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel11)
                                .addGap(3, 3, 3)
                                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel7)
                                .addGap(1, 1, 1)
                                .addComponent(txtCE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(3, 3, 3)
                                .addComponent(txtCP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel8)
                                .addGap(2, 2, 2)
                                .addComponent(txtMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(jLabel10)
                                .addGap(1, 1, 1)
                                .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setOpaque(false);

        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Numero Cliente", "Nombre", "Representante", "RFC", "Direccion", "Codigo Postal", "Email", "Telefono", "Municipio", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaClientes);

        combo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nombre", "Representante", "RFC", "Direccion", "Email" }));

        txtBus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusKeyReleased(evt);
            }
        });

        jButton47.setText("Seleccionar");
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton47)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelClientesLayout = new javax.swing.GroupLayout(panelClientes);
        panelClientes.setLayout(panelClientesLayout);
        panelClientesLayout.setHorizontalGroup(
            panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelClientesLayout.setVerticalGroup(
            panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(panelClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 990, 540));

        panelUsuarios.setOpaque(false);

        jPanel10.setOpaque(false);

        tablaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Contraseña", "Sucursal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaUsuariosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablaUsuarios);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Nombre");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("Apellido");

        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtApellidoKeyReleased(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setText("Contraseña");

        cmbSucursal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Centro", "Allende" }));

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setText("Sucursal");

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel11.setOpaque(false);

        jBAñadir.setText("Añadir");
        jBAñadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAñadirActionPerformed(evt);
            }
        });

        jBEliminar.setText("Eliminar");
        jBEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEliminarActionPerformed(evt);
            }
        });

        jBLimpiar.setText("Limpiar");
        jBLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBLimpiarActionPerformed(evt);
            }
        });

        jButton42.setText("Cambiar Avatar");
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton42, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(jBLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBAñadir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jBAñadir)
                .addGap(18, 18, 18)
                .addComponent(jBEliminar)
                .addGap(18, 18, 18)
                .addComponent(jBLimpiar)
                .addGap(18, 18, 18)
                .addComponent(jButton42)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labelIcono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelIcono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/chico (3).png"))); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(txtNombreU, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel28)
                            .addComponent(txtContra, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(cmbSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 789, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelIcono, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel16)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtContra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(labelIcono)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(86, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Usuarios", jPanel10);

        jPanel21.setOpaque(false);

        tablaCupones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo de Promocion", "Descuento %", "Fecha de Expiracion", "Estatus"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(tablaCupones);

        jLabel55.setText("Configuracion de cupones de descuento");

        jButton45.setText("Agregar");
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });

        jLabel56.setText("Codigo");

        jLabel57.setText("Descuento %");

        jLabel58.setText("Fecha");

        fechaCupon.setOpaque(false);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9)
                    .addComponent(jLabel55)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCupon, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel56))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel57)
                            .addComponent(txtCuponDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel58)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(fechaCupon, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 392, Short.MAX_VALUE)
                                .addComponent(jButton45)))))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel56)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCupon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel58)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaCupon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCuponDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Cupones", jPanel21);

        javax.swing.GroupLayout panelUsuariosLayout = new javax.swing.GroupLayout(panelUsuarios);
        panelUsuarios.setLayout(panelUsuariosLayout);
        panelUsuariosLayout.setHorizontalGroup(
            panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        panelUsuariosLayout.setVerticalGroup(
            panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        jPanel1.add(panelUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1010, 540));

        panelFacturas.setBackground(new java.awt.Color(255, 255, 255));
        panelFacturas.setOpaque(false);

        jPanel13.setOpaque(false);

        tablaOrdenes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "#", "Cliente", "Concepto", "Fecha", "Total", "Usuario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaOrdenes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaOrdenesMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tablaOrdenes);

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel30.setText("Listado de Ordenes y Cotizaciones en el sistema.");

        jLabel31.setText("Para abrir una orden seleciona un registro en la tabla y da doble click para ver el contenido.");

        jButton20.setBackground(new java.awt.Color(98, 161, 247));
        jButton20.setText("Pendientes");
        jButton20.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setBackground(new java.awt.Color(112, 234, 100));
        jButton21.setText("Pagados");
        jButton21.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton22.setBackground(new java.awt.Color(251, 63, 63));
        jButton22.setText("Cancelados");
        jButton22.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cliente", "Concepto", "Fecha", "Total" }));

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton27.setBackground(new java.awt.Color(239, 231, 101));
        jButton27.setText("Cotizaciones");
        jButton27.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton28.setText("Actualizar");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 970, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton28))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton20)
                    .addComponent(jButton21)
                    .addComponent(jButton22)
                    .addComponent(jButton27))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelFacturasLayout = new javax.swing.GroupLayout(panelFacturas);
        panelFacturas.setLayout(panelFacturasLayout);
        panelFacturasLayout.setHorizontalGroup(
            panelFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFacturasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelFacturasLayout.setVerticalGroup(
            panelFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFacturasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(panelFacturas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 1010, 540));

        panelCorteCaja.setBackground(new java.awt.Color(255, 255, 255));
        panelCorteCaja.setOpaque(false);

        jPanel12.setOpaque(false);

        tablaCortes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "#", "Fecha", "Total", "Usuario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaCortes.setToolTipText("");
        jScrollPane5.setViewportView(tablaCortes);

        jButton19.setText("Hacer Corte");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jLabel54.setText("Para realizar el corte del día da click en el boton \"Hacer Corte\".");

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel45.setText("Listado de Cortes de Caja realizados");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton19))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 970, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel45)
                            .addComponent(jLabel54))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel54)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton19)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelCorteCajaLayout = new javax.swing.GroupLayout(panelCorteCaja);
        panelCorteCaja.setLayout(panelCorteCajaLayout);
        panelCorteCajaLayout.setHorizontalGroup(
            panelCorteCajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCorteCajaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCorteCajaLayout.setVerticalGroup(
            panelCorteCajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCorteCajaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(panelCorteCaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1010, 540));

        panelInicio.setBackground(new java.awt.Color(255, 255, 255));
        panelInicio.setOpaque(false);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Ordenes pendientes"));
        jPanel14.setOpaque(false);

        tablaPendientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "#", "Cliente", "Concepto", "Fecha", "Total", "Usuario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaPendientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPendientesMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(tablaPendientes);

        jLabel64.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel64.setText("¡Haz doble click para obtener más información!");

        jLabel65.setText("Recuerda que si ya fue concluida un orden, dar click en \"Terminar\" para cambiar su estatus.");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel64)
                    .addComponent(jLabel65))
                .addContainerGap(529, Short.MAX_VALUE))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 958, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel64)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel65)
                .addContainerGap(389, Short.MAX_VALUE))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                    .addContainerGap(51, Short.MAX_VALUE)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel62.setText("¡BIENVENIDO!");

        javax.swing.GroupLayout panelInicioLayout = new javax.swing.GroupLayout(panelInicio);
        panelInicio.setLayout(panelInicioLayout);
        panelInicioLayout.setHorizontalGroup(
            panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelInicioLayout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelInicioLayout.setVerticalGroup(
            panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInicioLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel62)
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(panelInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1010, 538));

        jLabel63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondopanel2.jpg"))); // NOI18N
        jLabel63.setText(" ");
        jPanel1.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 135, 1030, 560));

        jPanel19.setBackground(new java.awt.Color(27, 51, 94));

        labelUsuarioLog.setBackground(new java.awt.Color(13, 33, 83));
        labelUsuarioLog.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelUsuarioLog.setForeground(new java.awt.Color(255, 255, 255));
        labelUsuarioLog.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelUsuarioLog.setText("CARLOS VALDEZ");
        labelUsuarioLog.setOpaque(true);

        jButton5.setBackground(new java.awt.Color(27, 51, 94));
        jButton5.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/clientes.png"))); // NOI18N
        jButton5.setText("Clientes");
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton5.setIconTextGap(45);
        jButton5.setOpaque(true);
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton5MouseExited(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jButton5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButton5FocusGained(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(27, 51, 94));
        jButton6.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/productos.png"))); // NOI18N
        jButton6.setText("Productos");
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton6.setIconTextGap(40);
        jButton6.setOpaque(true);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(27, 51, 94));
        jButton10.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ventas.png"))); // NOI18N
        jButton10.setText("Ventas");
        jButton10.setBorderPainted(false);
        jButton10.setContentAreaFilled(false);
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton10.setIconTextGap(48);
        jButton10.setOpaque(true);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton23.setBackground(new java.awt.Color(27, 51, 94));
        jButton23.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton23.setForeground(new java.awt.Color(255, 255, 255));
        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ordenes.png"))); // NOI18N
        jButton23.setText("Ordenes");
        jButton23.setBorderPainted(false);
        jButton23.setContentAreaFilled(false);
        jButton23.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton23.setIconTextGap(45);
        jButton23.setOpaque(true);
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton30.setBackground(new java.awt.Color(27, 51, 94));
        jButton30.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton30.setForeground(new java.awt.Color(255, 255, 255));
        jButton30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cotizacion.png"))); // NOI18N
        jButton30.setText("Cotizacion");
        jButton30.setBorderPainted(false);
        jButton30.setContentAreaFilled(false);
        jButton30.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton30.setIconTextGap(40);
        jButton30.setOpaque(true);
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jButton38.setBackground(new java.awt.Color(27, 51, 94));
        jButton38.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton38.setForeground(new java.awt.Color(255, 255, 255));
        jButton38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/corte.png"))); // NOI18N
        jButton38.setText("Corte de Caja");
        jButton38.setBorderPainted(false);
        jButton38.setContentAreaFilled(false);
        jButton38.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton38.setIconTextGap(30);
        jButton38.setOpaque(true);
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        jButton39.setBackground(new java.awt.Color(27, 51, 94));
        jButton39.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton39.setForeground(new java.awt.Color(255, 255, 255));
        jButton39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/reportes.png"))); // NOI18N
        jButton39.setText("Reportes");
        jButton39.setBorderPainted(false);
        jButton39.setContentAreaFilled(false);
        jButton39.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton39.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton39.setIconTextGap(43);
        jButton39.setOpaque(true);
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(27, 51, 94));
        jButton14.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ajustes.png"))); // NOI18N
        jButton14.setText("Configuraciones");
        jButton14.setBorderPainted(false);
        jButton14.setContentAreaFilled(false);
        jButton14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton14.setIconTextGap(22);
        jButton14.setOpaque(true);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        labelIconUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelIconUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/usuario.png"))); // NOI18N

        jButton43.setBackground(new java.awt.Color(27, 51, 94));
        jButton43.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton43.setForeground(new java.awt.Color(255, 255, 255));
        jButton43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicio.png"))); // NOI18N
        jButton43.setText("Inicio");
        jButton43.setBorderPainted(false);
        jButton43.setContentAreaFilled(false);
        jButton43.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton43.setIconTextGap(55);
        jButton43.setOpaque(true);
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        jButton50.setBackground(new java.awt.Color(27, 51, 94));
        jButton50.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jButton50.setForeground(new java.awt.Color(255, 255, 255));
        jButton50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/contabilidad.png"))); // NOI18N
        jButton50.setText("Contador");
        jButton50.setBorderPainted(false);
        jButton50.setContentAreaFilled(false);
        jButton50.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton50.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton50.setIconTextGap(40);
        jButton50.setOpaque(true);
        jButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelUsuarioLog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(labelIconUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
            .addComponent(jButton43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(labelIconUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelUsuarioLog, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton50, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 700));

        jPanel20.setBackground(new java.awt.Color(91, 166, 221));

        jLabel61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/barraMega.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jLabel61)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 1030, 134));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Guardar Cliente
        if (txtNombre.getText().length() == 0 || txtRepre.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Debe existir un nombre o representante");
        } else {

            u.insertaCliente(txtNombre.getText(), txtRepre.getText(), txtRFC.getText(),
                    txtDir.getText(), txtCP.getText(), txtCE.getText(),
                    txtTelefono.getText(), txtMunicipio.getText(), txtEstado.getText());
            cargarClientes();
            JOptionPane.showMessageDialog(this, "Cliente guardado");

        }

        txtNombre.setText("");
        txtRepre.setText("");
        txtRFC.setText("");
        txtDir.setText("");
        txtCP.setText("");
        txtCE.setText("");
        txtTelefono.setText("");
        txtMunicipio.setText("");
        txtEstado.setText("");
        txtNumCliente.setText("");
        txtNumCliente.setText(u.idNuevo());

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        txtNombre.setText("");
        txtRepre.setText("");
        txtRFC.setText("");
        txtDir.setText("");
        txtCP.setText("");
        txtCE.setText("");
        txtTelefono.setText("");
        txtMunicipio.setText("");
        txtEstado.setText("");
        txtNumCliente.setText("");
        txtNumCliente.setText(u.idNuevo());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(null, "¿Seguro que deseas eliminar?", null, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            u.eliminarCliente(txtNumCliente.getText());
            cargarClientes();
        }

        txtNombre.setText("");
        txtRepre.setText("");
        txtRFC.setText("");
        txtDir.setText("");
        txtCP.setText("");
        txtCE.setText("");
        txtTelefono.setText("");
        txtMunicipio.setText("");
        txtEstado.setText("");
        txtNumCliente.setText("");
        txtNumCliente.setText(u.idNuevo());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        u.actualizarCliente(codC, txtNombre.getText(), txtRepre.getText(), txtRFC.getText(), txtDir.getText(),
                txtCP.getText(), txtCE.getText(), txtTelefono.getText(), txtMunicipio.getText(), txtEstado.getText());
        cargarClientes();
        JOptionPane.showMessageDialog(null, "Datos actualizados");
    }//GEN-LAST:event_jButton4ActionPerformed
    String codC = "";
    private void tablaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClientesMouseClicked
        // TODO add your handling code here:
        DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
        codC = modelo.getValueAt(tablaClientes.getSelectedRow(), 0) + "";
        txtNumCliente.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 0) + "");
        txtNombre.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 1) + "");
        txtRepre.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 2) + "");
        txtRFC.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 3) + "");
        txtDir.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 4) + "");
        txtCP.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 5) + "");
        txtCE.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 6) + "");
        txtTelefono.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 7) + "");
        txtMunicipio.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 8) + "");
        txtEstado.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 9) + "");
    }//GEN-LAST:event_tablaClientesMouseClicked

    private void txtBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusKeyReleased
        // TODO add your handling code here:
        Generales a = new Generales();
        limpiar(tablaClientes);
        DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
        String comb = combo.getSelectedItem() + "";
        if (comb.equals("Email")) {
            comb = "correo";
        }
        a.busquedaClientes(modelo, txtBus.getText(), comb);
    }//GEN-LAST:event_txtBusKeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // OCULTAR PANELES
        panelClientes.setVisible(true);
        panelVentas.setVisible(false);
        panelProductos.setVisible(false);
        panelReportes.setVisible(false);
        panelCorteCaja.setVisible(false);
        panelCotizaciones.setVisible(false);
        panelUsuarios.setVisible(false);
        panelInicio.setVisible(false);
        panelFacturas.setVisible(false);
        panelContador.setVisible(false);

        //CAMBIAR COLORES BOTOTES
        jButton5.setBackground(Color.getHSBColor(163, 238, 66));
        //QUITAR COLOR
        jButton6.setBackground(Color.getColor("FFFFFF"));
        jButton43.setBackground(Color.getColor("FFFFFF"));
        jButton10.setBackground(Color.getColor("FFFFFF"));
        jButton23.setBackground(Color.getColor("FFFFFF"));
        jButton30.setBackground(Color.getColor("FFFFFF"));
        jButton39.setBackground(Color.getColor("FFFFFF"));
        jButton14.setBackground(Color.getColor("FFFFFF"));
        jButton38.setBackground(Color.getColor("FFFFFF"));
        jButton50.setBackground(Color.getColor("FFFFFF"));
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // ABRIR PANEL PRODUCTOS
        panelClientes.setVisible(false);
        panelVentas.setVisible(false);
        panelProductos.setVisible(true);
        panelReportes.setVisible(false);
        panelCorteCaja.setVisible(false);
        panelCotizaciones.setVisible(false);
        panelUsuarios.setVisible(false);
        panelInicio.setVisible(false);
        panelFacturas.setVisible(false);
        panelContador.setVisible(false);

        //CAMBIAR COLORES BOTOTES
        jButton6.setBackground(Color.getHSBColor(163, 238, 66));
        //QUITAR COLOR
        jButton5.setBackground(Color.getColor("FFFFFF"));
        jButton43.setBackground(Color.getColor("FFFFFF"));
        jButton10.setBackground(Color.getColor("FFFFFF"));
        jButton23.setBackground(Color.getColor("FFFFFF"));
        jButton30.setBackground(Color.getColor("FFFFFF"));
        jButton39.setBackground(Color.getColor("FFFFFF"));
        jButton14.setBackground(Color.getColor("FFFFFF"));
        jButton38.setBackground(Color.getColor("FFFFFF"));
        jButton50.setBackground(Color.getColor("FFFFFF"));
    }//GEN-LAST:event_jButton6ActionPerformed

    private void comboTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboTipoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_comboTipoItemStateChanged

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        String tipo = comboTipo.getSelectedItem() + "";
        if (tipo.equals("Seleccione..")) {
            JOptionPane.showMessageDialog(null, "Seleccione un tipo");
        } else {
            String tipoFormato = "";
            if (radioGranFormato.isSelected()) {
                tipoFormato = "GRAN FORMATO";
            } else {
                tipoFormato = "PRODUCTOS";
            }

            u.insertaProducto(tipo, txtNombreP.getText(), txtDesc.getText(),
                    Double.parseDouble(txtPI.getText()), Double.parseDouble(txtPI2.getText()),
                    tipoFormato, txtMinima.getText(), txtExistencias.getText());
            JOptionPane.showMessageDialog(null, "Registro exitoso");

            limpiarCampos();
        }
        cargarProductos();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        try {
            u.eliminarArticulo(codP);
            cargarProductos();
            JOptionPane.showMessageDialog(null, "Producto/Servicio eliminado");
            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error, eliminar producto\n" + e);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        try {
            String tipoFormato = "";
            if (radioGranFormato.isSelected()) {
                tipoFormato = "GRAN FORMATO";
            } else {
                tipoFormato = "PRODUCTOS";
            }

            u.actualizarArticulo(codP, txtNombreP.getText(), txtDesc.getText(), txtPI.getText(), txtPI2.getText(),
                    tipoFormato, txtMinima.getText(), txtExistencias.getText());
            cargarProductos();
            limpiarCampos();
            JOptionPane.showMessageDialog(null, "Producto/Servicio actualizado");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error, intente de nuevo");
        }
    }//GEN-LAST:event_jButton9ActionPerformed
    String codP = "";
    private void tablaServMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaServMouseClicked
        // TODO add your handling code here:
        DefaultTableModel modelo = (DefaultTableModel) tablaServ.getModel();
        codP = modelo.getValueAt(tablaServ.getSelectedRow(), 0) + "";  //id usuario

        txtNombreP.setText(modelo.getValueAt(tablaServ.getSelectedRow(), 2) + "");
        txtDesc.setText(modelo.getValueAt(tablaServ.getSelectedRow(), 3) + "");
        txtPI.setText(modelo.getValueAt(tablaServ.getSelectedRow(), 4) + "");
        txtPI2.setText(modelo.getValueAt(tablaServ.getSelectedRow(), 5) + "");
        txtMinima.setText(modelo.getValueAt(tablaServ.getSelectedRow(), 7) + "");
        txtExistencias.setText(modelo.getValueAt(tablaServ.getSelectedRow(), 8) + "");

        String tipo = modelo.getValueAt(tablaServ.getSelectedRow(), 6) + "";

        if (tipo.equals("GRAN FORMATO")) {
            radioGranFormato.setSelected(true);
        } else {
            radioProductos.setSelected(true);
        }

        //operaciones();
    }//GEN-LAST:event_tablaServMouseClicked

    private void txtBusquedaProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaProductosKeyReleased
        // Buscar en productos
        limpiar(tablaServ);
        DefaultTableModel modelo = (DefaultTableModel) tablaServ.getModel();
        String comb = combo.getSelectedItem() + "";
        u.busquedaProductos(modelo, txtBusquedaProductos.getText(), comb);
    }//GEN-LAST:event_txtBusquedaProductosKeyReleased

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        //Generales p = new Generales();
        try {
            if (tNombre.getText().length() == 0 || tRepre.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "Debe existir un nombre o representante");
            } else {

                u.insertaCliente(tNombre.getText(), tRepre.getText(), tRFC.getText(),
                        tDire.getText(), "-", tEmail.getText(),
                        tTelefono.getText(), "-", "-");
                JOptionPane.showMessageDialog(null, "Cliente registrado");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error, intente de nuevo");
        }
//        iniciaClientes();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        panelVentas.setVisible(true);
        panelClientes.setVisible(false);
        panelProductos.setVisible(false);
        panelFacturas.setVisible(false);
        panelUsuarios.setVisible(false);
        panelCotizaciones.setVisible(false);
        panelCorteCaja.setVisible(false);
        panelInicio.setVisible(false);
        panelReportes.setVisible(false);
        panelContador.setVisible(false);

        //CAMBIAR COLORES BOTOTES
        jButton10.setBackground(Color.getHSBColor(163, 238, 66));
        //QUITAR COLOR
        jButton6.setBackground(Color.getColor("FFFFFF"));
        jButton43.setBackground(Color.getColor("FFFFFF"));
        jButton5.setBackground(Color.getColor("FFFFFF"));
        jButton23.setBackground(Color.getColor("FFFFFF"));
        jButton30.setBackground(Color.getColor("FFFFFF"));
        jButton39.setBackground(Color.getColor("FFFFFF"));
        jButton14.setBackground(Color.getColor("FFFFFF"));
        jButton38.setBackground(Color.getColor("FFFFFF"));
        jButton50.setBackground(Color.getColor("FFFFFF"));
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        ventanaArticulos va = new ventanaArticulos("VENTAS");
        va.ventas = this;
        va.setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed
    String codU = "";
    private void tablaUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaUsuariosMouseClicked
        // TODO add your handling code here:
        DefaultTableModel modelo = (DefaultTableModel) tablaUsuarios.getModel();
        codU = modelo.getValueAt(tablaUsuarios.getSelectedRow(), 0) + "";  //id usuario
    }//GEN-LAST:event_tablaUsuariosMouseClicked

    private void txtApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoKeyReleased

    private void jBAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAñadirActionPerformed
        // TODO add your handling code here:
        Generales U = new Generales();

        String tipo = cmbSucursal.getSelectedItem() + "";

        U.añadirUsu(txtNombreU.getText(), txtApellido.getText(), txtContra.getText(), tipo, icono);

        txtNombreU.setText("");
        txtApellido.setText("");
        txtContra.setText("");

        llenaUsuarios();
    }//GEN-LAST:event_jBAñadirActionPerformed

    private void jBEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEliminarActionPerformed
        // TODO add your handling code here:
        u.eliminarUsu(codU);
        llenaUsuarios();
    }//GEN-LAST:event_jBEliminarActionPerformed

    private void jBLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBLimpiarActionPerformed
        // TODO add your handling code here:
        limpiar(tablaUsuarios);
        txtNombreU.setText("");
        txtApellido.setText("");
        txtContra.setText("");
    }//GEN-LAST:event_jBLimpiarActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:

        if (u.corteCaja(fecha) != null) {
            JOptionPane.showMessageDialog(null, "El corte de caja de hoy ya se realizó");
        } else {
            corteCaja cc = new corteCaja(labelUsuarioLog.getText());
            cc.prin = this;
            cc.setVisible(true);
        }

    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        //Ocultar
        panelInicio.setVisible(false);
        panelCotizaciones.setVisible(false);;
        panelFacturas.setVisible(true);
        panelVentas.setVisible(false);
        panelClientes.setVisible(false);
        panelProductos.setVisible(false);
        panelUsuarios.setVisible(false);
        panelCorteCaja.setVisible(false);
        panelReportes.setVisible(false);
        panelContador.setVisible(false);

        //CAMBIAR COLORES BOTOTES
        jButton23.setBackground(Color.getHSBColor(163, 238, 66));
        //QUITAR COLOR
        jButton6.setBackground(Color.getColor("FFFFFF"));
        jButton43.setBackground(Color.getColor("FFFFFF"));
        jButton10.setBackground(Color.getColor("FFFFFF"));
        jButton5.setBackground(Color.getColor("FFFFFF"));
        jButton30.setBackground(Color.getColor("FFFFFF"));
        jButton39.setBackground(Color.getColor("FFFFFF"));
        jButton14.setBackground(Color.getColor("FFFFFF"));
        jButton38.setBackground(Color.getColor("FFFFFF"));
        jButton50.setBackground(Color.getColor("FFFFFF"));
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jLabel32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel32MouseClicked
        // TODO add your handling code here:
        String tema = JOptionPane.showInputDialog(this, "Ingrese tema:");
        labelTema.setText(tema);
    }//GEN-LAST:event_jLabel32MouseClicked

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
        if (tablaVenta.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(null, "Selecciona un producto");
        } else {

            int result = JOptionPane.showConfirmDialog(null,
                    "¿Estas seguro de eliminar?", null, JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {

                DefaultTableModel dtm = (DefaultTableModel) tablaVenta.getModel();
                dtm.removeRow(tablaVenta.getSelectedRow());
                sumaVenta();
            }
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        ventanaGranFormato va = new ventanaGranFormato("VENTA");
        va.ventas = this;
        va.setVisible(true);
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        if (labelTema.getText().equals("-")) {
            JOptionPane.showMessageDialog(this, "Asigna un tema a la cotización");
        } else {
            ventanaCobro vc = new ventanaCobro(txtTotal.getText(), "venta");
            vc.ventas = this;
            vc.setVisible(true);
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        double impuestos = 0.00, total = 0.00, simp = 0.0;
        String sub = "", imp = "";

        total = Double.parseDouble(txtTotal.getText());
        simp = total / 1.16;
        impuestos = simp * .16;

        sub = df.format(simp);
        imp = df.format(impuestos);

        txtSub.setText(sub);
        txtImpuestos.setText(imp);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        ventanaDescuento vd = new ventanaDescuento("VENTA");
        vd.principal = this;
        vd.setVisible(true);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
        limpiarVenta();
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        iniciaOrdenes("pendientes");
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
        iniciaOrdenes("cancelados");
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        iniciaOrdenes("pagados");
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
        iniciaOrdenes("cotizaciones");
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
        iniciaOrdenes("");
    }//GEN-LAST:event_jButton28ActionPerformed

    private void tablaOrdenesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaOrdenesMouseClicked
        // TODO add your handling code here:
        DefaultTableModel modelo = (DefaultTableModel) tablaOrdenes.getModel();
        String codigo = modelo.getValueAt(tablaOrdenes.getSelectedRow(), 0) + "";  //id orden
        String cliente = modelo.getValueAt(tablaOrdenes.getSelectedRow(), 1) + "";  //cliente

        if (evt.getClickCount() == 1) {
            System.out.println("Se ha hecho un click");
        }
        if (evt.getClickCount() == 2) {
            ventanaFacturas vf = new ventanaFacturas(codigo, cliente);
            vf.setVisible(true);
        }
    }//GEN-LAST:event_tablaOrdenesMouseClicked

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jLabel44MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel44MouseClicked
        // TODO add your handling code here:
        String tema = JOptionPane.showInputDialog(this, "Tema de cotización:");
        labelTema1.setText(tema);
    }//GEN-LAST:event_jLabel44MouseClicked

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
        panelCotizaciones.setVisible(true);;
        panelFacturas.setVisible(false);
        panelVentas.setVisible(false);
        panelClientes.setVisible(false);
        panelProductos.setVisible(false);
        panelUsuarios.setVisible(false);
        panelCorteCaja.setVisible(false);
        panelReportes.setVisible(false);
        panelContador.setVisible(false);

        //CAMBIAR COLORES BOTOTES
        jButton30.setBackground(Color.getHSBColor(163, 238, 66));
        //QUITAR COLOR
        jButton6.setBackground(Color.getColor("FFFFFF"));
        jButton43.setBackground(Color.getColor("FFFFFF"));
        jButton10.setBackground(Color.getColor("FFFFFF"));
        jButton23.setBackground(Color.getColor("FFFFFF"));
        jButton5.setBackground(Color.getColor("FFFFFF"));
        jButton39.setBackground(Color.getColor("FFFFFF"));
        jButton14.setBackground(Color.getColor("FFFFFF"));
        jButton38.setBackground(Color.getColor("FFFFFF"));
        jButton50.setBackground(Color.getColor("FFFFFF"));
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        // GRAN FORMATO - PANEL COTIZACIONES
        ventanaGranFormato va = new ventanaGranFormato("COTI");
        va.ventas = this;
        va.setVisible(true);
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        // LIMPIAR PANEL COTIZACION
        limpiarCotizacion();
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
// TODO add your handling code here:
        if (tablaVenta1.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(null, "Selecciona un producto");
        } else {

            int result = JOptionPane.showConfirmDialog(null,
                    "¿Estas seguro de eliminar?", null, JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {

                DefaultTableModel dtm = (DefaultTableModel) tablaVenta1.getModel();
                dtm.removeRow(tablaVenta1.getSelectedRow());
                sumaCotizacion();
            }
        }
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        // ARTICULOS - PANEL COTIZACION
        ventanaArticulos va = new ventanaArticulos("COTI");
        va.ventas = this;
        va.setVisible(true);
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        //Descuento en panel cotizaciones
        ventanaDescuento vd = new ventanaDescuento("COTI");
        vd.principal = this;
        vd.setVisible(true);

    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // Impuestos - Panel cotizacion
        double impuestos = 0.00, total = 0.00, simp = 0.0;
        String sub = "", imp = "";

        total = Double.parseDouble(txtTotal1.getText());
        simp = total / 1.16;
        impuestos = simp * .16;

        sub = df.format(simp);
        imp = df.format(impuestos);

        txtSub1.setText(sub);
        txtImpuestos1.setText(imp);
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        // GUARDAR COTIZACION EN PANEL COTIZACIONES OBVIO
        ArchivoPDF p = new ArchivoPDF();
        Ventas v = new Ventas();

        DefaultTableModel modelo = (DefaultTableModel) tablaVenta1.getModel();

        SimpleDateFormat fBase = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = GregorianCalendar.getInstance();
        String fechaBase = fBase.format(cal.getTime());

        Object[][] articulos = obtenerArticulos(modelo);

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No existen articulos en la cotización");
        } else {
            if (labelTema1.getText().equals("-")) {
                JOptionPane.showMessageDialog(this, "Asigna un tema a la cotización");
            } else {
                v.RealizaVenta(modelo, tNombre1.getText(), labelTema1.getText(), txtTotal1.getText(),
                        fechaBase, "Cotizacion", "", "Cotizacion", "NO", "PENDIENTE",
                        labelUsuarioLog.getText(), txtSub1.getText(), txtDescuento1.getText(), txtImpuestos1.getText(), txtComentarios1.getText());

                try {
                    p.pdfCotización(labelTema1.getText(), v.folioVenta(), labelTema1.getText(), articulos, "Cotizacion", fechaBase, txtTotal1.getText(),
                            tNombre1.getText(), tRepre1.getText(), tRFC1.getText(), tDire1.getText(), tEmail1.getText(),
                            txtComentarios1.getText(), txtImpuestos1.getText(), txtSub1.getText(), txtDescuento1.getText(), labelUsuarioLog.getText());

                } catch (DocumentException | IOException e) {
                    JOptionPane.showMessageDialog(this, "Error creando pdf..");
                }

                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(new java.io.File("W:\\megapublicidad2.0\\documentos\\" + labelTema1.getText() + ".pdf"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "No se puede abrir archivo." + ex.getMessage());
                }

            }
        }

    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        // Panel corte de caja
        panelInicio.setVisible(false);
        panelCotizaciones.setVisible(false);
        panelFacturas.setVisible(false);
        panelVentas.setVisible(false);
        panelClientes.setVisible(false);
        panelProductos.setVisible(false);
        panelUsuarios.setVisible(false);
        panelCorteCaja.setVisible(true);
        panelReportes.setVisible(false);
        panelContador.setVisible(false);

        //CAMBIAR COLORES BOTOTES
        jButton38.setBackground(Color.getHSBColor(163, 238, 66));
        //QUITAR COLOR
        jButton6.setBackground(Color.getColor("FFFFFF"));
        jButton43.setBackground(Color.getColor("FFFFFF"));
        jButton10.setBackground(Color.getColor("FFFFFF"));
        jButton23.setBackground(Color.getColor("FFFFFF"));
        jButton30.setBackground(Color.getColor("FFFFFF"));
        jButton39.setBackground(Color.getColor("FFFFFF"));
        jButton14.setBackground(Color.getColor("FFFFFF"));
        jButton5.setBackground(Color.getColor("FFFFFF"));
        jButton50.setBackground(Color.getColor("FFFFFF"));
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        // Panel Reportes
        panelInicio.setVisible(false);
        panelCotizaciones.setVisible(false);;
        panelFacturas.setVisible(false);
        panelVentas.setVisible(false);
        panelClientes.setVisible(false);
        panelProductos.setVisible(false);
        panelUsuarios.setVisible(false);
        panelCorteCaja.setVisible(false);
        panelReportes.setVisible(true);
        panelContador.setVisible(false);

        //CAMBIAR COLORES BOTOTES
        jButton39.setBackground(Color.getHSBColor(163, 238, 66));
        //QUITAR COLOR
        jButton6.setBackground(Color.getColor("FFFFFF"));
        jButton43.setBackground(Color.getColor("FFFFFF"));
        jButton10.setBackground(Color.getColor("FFFFFF"));
        jButton23.setBackground(Color.getColor("FFFFFF"));
        jButton30.setBackground(Color.getColor("FFFFFF"));
        jButton5.setBackground(Color.getColor("FFFFFF"));
        jButton14.setBackground(Color.getColor("FFFFFF"));
        jButton38.setBackground(Color.getColor("FFFFFF"));
        jButton50.setBackground(Color.getColor("FFFFFF"));
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton5FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5FocusGained

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        //jButton5.setBackground(Color.yellow);    
        //jButton5.setBackground(Color.getHSBColor(163,238,66)); //2222, 84, 32));
        // PRUEBAS CUANDO ENTRA EL CURSOR EN EL BOTON
    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseExited
        //  jButton5.setBackground(Color.getColor("FFFFFF"));        // PRUEBAS CUANDO SE SALE EL CURSOR DEL BOTON
    }//GEN-LAST:event_jButton5MouseExited

    private void jRadioButton5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton5ItemStateChanged
        // TODO add your handling code here:
        cargarReportes("Pagados", "");
        labelAviso.setText("Listado de ordenes pagadas");
        sumaReportes();
    }//GEN-LAST:event_jRadioButton5ItemStateChanged

    private void jRadioButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton2ItemStateChanged
        // TODO add your handling code here:
        cargarReportes("Pendientes", "");
        labelAviso.setText("Listado de ordenes pendientes");
        sumaReportes();
    }//GEN-LAST:event_jRadioButton2ItemStateChanged

    private void jRadioButton4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton4ItemStateChanged
        // TODO add your handling code here:
        cargarReportes("Cotizaciones", "");
        sumaReportes();
        labelAviso.setText("Listado de Cotizaciones");
    }//GEN-LAST:event_jRadioButton4ItemStateChanged

    private void jRadioButton3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton3ItemStateChanged
        // TODO add your handling code here:
        cargarReportes("Cancelados", "");
        labelAviso.setText("Listado de Ordenes y Cotizaciones canceladas");
        sumaReportes();
    }//GEN-LAST:event_jRadioButton3ItemStateChanged

    private void jRadioButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton1ItemStateChanged
        // TODO add your handling code here:
        cargarReportes("General", "");
        labelAviso.setText("Listado de Ordenes General");
        sumaReportes();
    }//GEN-LAST:event_jRadioButton1ItemStateChanged
    String f1 = "", f2 = "";
    String tipoPDF = "";
    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        // 1 Fecha
        try {

            f1 = new SimpleDateFormat("yyyy-MM-dd").format(primeraFecha.getDate());

            if (jRadioButton1.isSelected()) {
                cargarReportes("Fechas", " where fecha= '" + f1 + "' ");
            } else if (jRadioButton2.isSelected()) {
                //PENDIENTES
                cargarReportes("Fechas", " where estadoServicio='PENDIENTE' and fecha='" + f1 + "' ");
            } else if (jRadioButton3.isSelected()) {
                //CANCELADOS
                cargarReportes("Fechas", " where estado='CANCELADO' and fecha='" + f1 + "'");
            } else if (jRadioButton4.isSelected()) {
                //COTIZACIONES
                cargarReportes("Fechas", " where estado='Cotizacion' and fecha='" + f1 + "' ");
            } else {
                //PAGADOS
                cargarReportes("Fechas", " where estadoPago='COMPLETO' and fecha='" + f1 + "' ");
            }

            tipoPDF = "1";
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // PDF REPORTES
        ArchivoPDF p = new ArchivoPDF();
        DefaultTableModel modelo = (DefaultTableModel) tablaReportes.getModel();
        Object[][] articulos = obtenerArticulos(modelo);
        try {
            String comen = "                               " + labelAviso.getText();// "                     Reporte de ventas general";
            if (tipoPDF.equals("1")) {
                comen = "                     " + labelAviso.getText() + " del dia " + f1;
            } else if (tipoPDF.equals("2")) {
                comen = labelAviso.getText() + " del dia " + f1 + " al dia " + f2;
            }

            String archivo = JOptionPane.showInputDialog("Nombre del archivo");

            if (archivo == null || archivo.equals("")) {
                JOptionPane.showMessageDialog(this, "Para abrir PDF asigna un nombre");
            } else {
                p.pdfReporteVentas(archivo, articulos, f1, f2, reportesTotal.getText(), comen);

                Desktop desktop = Desktop.getDesktop();
                desktop.open(new java.io.File("W:\\megapublicidad2.0\\documentos\\reportes\\" + archivo + ".pdf"));
            }
        } catch (DocumentException ex) {
            Logger.getLogger(ventanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ventanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        panelUsuarios.setVisible(true);
        panelCotizaciones.setVisible(false);
        panelFacturas.setVisible(false);
        panelVentas.setVisible(false);
        panelClientes.setVisible(false);
        panelProductos.setVisible(false);
        panelCorteCaja.setVisible(false);
        panelReportes.setVisible(false);
        panelInicio.setVisible(false);
        panelContador.setVisible(false);

        //CAMBIAR COLORES BOTOTES
        jButton14.setBackground(Color.getHSBColor(163, 238, 66));
        //QUITAR COLOR
        jButton6.setBackground(Color.getColor("FFFFFF"));
        jButton43.setBackground(Color.getColor("FFFFFF"));
        jButton10.setBackground(Color.getColor("FFFFFF"));
        jButton23.setBackground(Color.getColor("FFFFFF"));
        jButton30.setBackground(Color.getColor("FFFFFF"));
        jButton39.setBackground(Color.getColor("FFFFFF"));
        jButton5.setBackground(Color.getColor("FFFFFF"));
        jButton38.setBackground(Color.getColor("FFFFFF"));
        jButton50.setBackground(Color.getColor("FFFFFF"));
    }//GEN-LAST:event_jButton14ActionPerformed
    String icono = "1";
    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        // TODO add your handling code here:
        String path = "";
        switch (icono) {
            case "1":
                path = "/Imagenes/chica.png";
                icono = "2";
                break;
            case "2":
                path = "/Imagenes/chica2.png";
                icono = "3";
                break;
            case "3":
                path = "/Imagenes/chica3.png";
                icono = "4";
                break;
            case "4":
                path = "/Imagenes/chico.png";
                icono = "5";
                break;
            case "5":
                path = "/Imagenes/chico2.png";
                icono = "6";
                break;
            case "6":
                path = "/Imagenes/chico (3).png";
                icono = "1";
                break;
        }

        URL url = this.getClass().getResource(path);
        ImageIcon icon = new ImageIcon(url);
        labelIcono.setIcon(icon);

    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        // TODO add your handling code here:
        try {

            f1 = new SimpleDateFormat("yyyy-MM-dd").format(primeraFecha.getDate());
            f2 = new SimpleDateFormat("yyyy-MM-dd").format(segundaFecha.getDate());

            if (jRadioButton1.isSelected()) {
                cargarReportes("Fechas", " where fecha between '" + f1 + "' and '" + f2 + "' ");
            } else if (jRadioButton2.isSelected()) {
                //PENDIENTES
                cargarReportes("Fechas", " where estadoServicio='PENDIENTE' and fecha between '" + f1 + "' and '" + f2 + "' ");
            } else if (jRadioButton3.isSelected()) {
                //CANCELADOS
                cargarReportes("Fechas", " where estado='CANCELADO' and fecha between '" + f1 + "' and '" + f2 + "'");
            } else if (jRadioButton4.isSelected()) {
                //COTIZACIONES
                cargarReportes("Fechas", " where estado='Cotizacion' and fecha between '" + f1 + "' and '" + f2 + "' ");
            } else {
                //PAGADOS
                cargarReportes("Fechas", " where estadoPago='COMPLETO' and fecha between '" + f1 + "' and '" + f2 + "' ");
            }

            tipoPDF = "2";
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        // AGREGAR CUPON
        String fcupon = new SimpleDateFormat("yyyy-MM-dd").format(fechaCupon.getDate());
        u.insertaCupon(txtCupon.getText(), fcupon, txtCuponDesc.getText(), "VIGENTE");
        cargarCupones();
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        // TODO add your handling code here:
        panelInicio.setVisible(true);
        panelCotizaciones.setVisible(false);
        panelFacturas.setVisible(false);
        panelVentas.setVisible(false);
        panelClientes.setVisible(false);
        panelProductos.setVisible(false);
        panelUsuarios.setVisible(false);
        panelCorteCaja.setVisible(false);
        panelReportes.setVisible(false);
        panelContador.setVisible(false);

        //CAMBIAR COLORES BOTOTES
        jButton43.setBackground(Color.getHSBColor(163, 238, 66));
        //QUITAR COLOR
        jButton6.setBackground(Color.getColor("FFFFFF"));
        jButton5.setBackground(Color.getColor("FFFFFF"));
        jButton10.setBackground(Color.getColor("FFFFFF"));
        jButton23.setBackground(Color.getColor("FFFFFF"));
        jButton30.setBackground(Color.getColor("FFFFFF"));
        jButton39.setBackground(Color.getColor("FFFFFF"));
        jButton14.setBackground(Color.getColor("FFFFFF"));
        jButton38.setBackground(Color.getColor("FFFFFF"));
        jButton50.setBackground(Color.getColor("FFFFFF"));
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:

        String stock = JOptionPane.showInputDialog(this, "Cantidad:");
        if (stock.equals("") || stock == null) {
            JOptionPane.showMessageDialog(this, "Es necesario ingresar cantidad para modificar el stock");
        } else {
            Double total = Double.parseDouble(txtExistencias.getText()) + Double.parseDouble(stock);
            txtExistencias.setText(total + "");
            u.actualizarStock(codP, total + "");
            cargarProductos();
        }
    }//GEN-LAST:event_jButton18ActionPerformed
    String tipoPanel = "";
    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        // TODO add your handling code here:
        panelClientes.setVisible(true);
        panelVentas.setVisible(false);
        tipoPanel = "venta";
    }//GEN-LAST:event_jButton46ActionPerformed

    public void añadirCliente(String panel) {
        if (panel.equals("venta")) {
            DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
            codC = modelo.getValueAt(tablaClientes.getSelectedRow(), 0) + "";
            tCliente.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 0) + "");
            tNombre.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 1) + "");
            tRepre.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 2) + "");
            tRFC.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 3) + "");
            tDire.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 4) + "");
            tEmail.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 6) + "");
            tTelefono.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 7) + "");

            panelClientes.setVisible(false);
            panelVentas.setVisible(true);
        } else {
            // AÑADIR CLIENTE A LA COTIZACION
            DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
            codC = modelo.getValueAt(tablaClientes.getSelectedRow(), 0) + "";
            tCliente1.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 0) + "");
            tNombre1.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 1) + "");
            tRepre1.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 2) + "");
            tRFC1.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 3) + "");
            tDire1.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 4) + "");
            tEmail1.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 6) + "");
            tTelefono1.setText(modelo.getValueAt(tablaClientes.getSelectedRow(), 7) + "");

            panelClientes.setVisible(false);
            panelCotizaciones.setVisible(true);
        }
    }
    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        // AÑADIR CLIENTE A LA VENTA/Coti
        añadirCliente(tipoPanel);
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
        panelClientes.setVisible(true);
        panelCotizaciones.setVisible(false);
        tipoPanel = "coti";
    }//GEN-LAST:event_jButton48ActionPerformed
    String codFac = "", clienteFac = "";
    private void tablaFacturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaFacturasMouseClicked
        // TODO add your handling code here:
        DefaultTableModel modelo = (DefaultTableModel) tablaFacturas.getModel();
        codFac = (modelo.getValueAt(tablaFacturas.getSelectedRow(), 0) + "");
        clienteFac = (modelo.getValueAt(tablaFacturas.getSelectedRow(), 1) + "");

        if (evt.getClickCount() == 1) {
            System.out.println("Se ha hecho un click");
        }
        if (evt.getClickCount() == 2) {
            vetanaDetalleFactura vdf = new vetanaDetalleFactura(codFac, clienteFac, "FACTURA");
            vdf.princ = this;
            vdf.setVisible(true);
        }
    }//GEN-LAST:event_tablaFacturasMouseClicked

    private void txtBus1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBus1KeyReleased
        // TODO add your handling code here:
        limpiar(tablaFacturas);
        DefaultTableModel modelo = (DefaultTableModel) tablaFacturas.getModel();
        String combo = jComboBox2.getSelectedItem() + "";
        if (jRadioButton6.isSelected()) {
            a.busquedaFacturas(modelo, txtBus1.getText(), combo, "SI");
        } else {
            a.busquedaFacturas(modelo, txtBus1.getText(), combo, "FIN");
        }
        //a.busquedaVenta(modelo, jTextField2.getText(), combo, tipo);
    }//GEN-LAST:event_txtBus1KeyReleased

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        // TODO add your handling code here:
        iniciaFacturas();
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jRadioButton6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton6ItemStateChanged
        // TODO add your handling code here:
        iniciaFacturas();
    }//GEN-LAST:event_jRadioButton6ItemStateChanged

    private void jRadioButton7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton7ItemStateChanged
        // TODO add your handling code here:
        iniciaFacturas();
    }//GEN-LAST:event_jRadioButton7ItemStateChanged
    String codTrans = "", clienteTrans = "";
    private void tablaTransMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaTransMouseClicked
        // TODO add your handling code here:
        DefaultTableModel modelo = (DefaultTableModel) tablaTrans.getModel();
        codTrans = (modelo.getValueAt(tablaTrans.getSelectedRow(), 0) + "");
        clienteTrans = (modelo.getValueAt(tablaTrans.getSelectedRow(), 1) + "");
        if (evt.getClickCount() == 1) {
            System.out.println("Se ha hecho un click");
        }
        if (evt.getClickCount() == 2) {
            vetanaDetalleFactura vdf = new vetanaDetalleFactura(codTrans, clienteTrans, "TRANSFERENCIA");
            vdf.princ = this;
            vdf.setVisible(true);
        }
    }//GEN-LAST:event_tablaTransMouseClicked

    private void txtBus2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBus2KeyReleased
        // TODO add your handling code here:
        limpiar(tablaTrans);
        DefaultTableModel modelo = (DefaultTableModel) tablaTrans.getModel();
        String combo = jComboBox2.getSelectedItem() + "";
        if (jRadioButton8.isSelected()) {
            a.busquedaTrans(modelo, txtBus2.getText(), combo, "PENDIENTE");
        } else {
            a.busquedaTrans(modelo, txtBus2.getText(), combo, "COMPLETO");
        }
    }//GEN-LAST:event_txtBus2KeyReleased

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        // TODO add your handling code here:
        iniciaTransferencias();
    }//GEN-LAST:event_jButton49ActionPerformed

    private void jRadioButton8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton8ItemStateChanged
        // TODO add your handling code here:
        iniciaTransferencias();
    }//GEN-LAST:event_jRadioButton8ItemStateChanged

    private void jRadioButton9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton9ItemStateChanged
        // TODO add your handling code here:
        iniciaTransferencias();
    }//GEN-LAST:event_jRadioButton9ItemStateChanged

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        // TODO add your handling code here:
        panelContador.setVisible(true);
        panelUsuarios.setVisible(false);
        panelCotizaciones.setVisible(false);
        panelFacturas.setVisible(false);
        panelVentas.setVisible(false);
        panelClientes.setVisible(false);
        panelProductos.setVisible(false);
        panelCorteCaja.setVisible(false);
        panelReportes.setVisible(false);
        panelInicio.setVisible(false);
        //CAMBIAR COLORES BOTOTES
        jButton50.setBackground(Color.getHSBColor(163, 238, 66));
        //QUITAR COLOR
        jButton6.setBackground(Color.getColor("FFFFFF"));
        jButton43.setBackground(Color.getColor("FFFFFF"));
        jButton10.setBackground(Color.getColor("FFFFFF"));
        jButton23.setBackground(Color.getColor("FFFFFF"));
        jButton30.setBackground(Color.getColor("FFFFFF"));
        jButton39.setBackground(Color.getColor("FFFFFF"));
        jButton5.setBackground(Color.getColor("FFFFFF"));
        jButton38.setBackground(Color.getColor("FFFFFF"));
        jButton14.setBackground(Color.getColor("FFFFFF"));
    }//GEN-LAST:event_jButton50ActionPerformed

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        // articulo rapido
        DefaultTableModel modelo = (DefaultTableModel) tablaVenta.getModel();

        String produ = JOptionPane.showInputDialog(null, "Producto/Servicio:");
        if (produ != null) {
            String pu = JOptionPane.showInputDialog(null, "Precio Unitario:");
            String cantidad = JOptionPane.showInputDialog(null, "Cantidad:");

            double prec = Double.parseDouble(pu) * Double.parseDouble(cantidad);

            modelo.addRow(new Object[]{"0", produ, "-", cantidad, pu, df.format(prec)});
            sumaVenta();
        }
    }//GEN-LAST:event_jButton51ActionPerformed

    private void jButton52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton52ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel modelo = (DefaultTableModel) tablaVenta1.getModel();

        String produ = JOptionPane.showInputDialog(null, "Producto/Servicio:");
        if (produ != null) {
            String pu = JOptionPane.showInputDialog(null, "Precio Unitario:");
            String cantidad = JOptionPane.showInputDialog(null, "Cantidad:");

            double prec = Double.parseDouble(pu) * Double.parseDouble(cantidad);

            modelo.addRow(new Object[]{"0", produ, "-", cantidad, pu, df.format(prec)});
            sumaVenta();
        }
    }//GEN-LAST:event_jButton52ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // Busqueda en ordenes de venta
        limpiar(tablaOrdenes);
        DefaultTableModel modelo = (DefaultTableModel) tablaOrdenes.getModel();
        String com = jComboBox1.getSelectedItem() + "";
        a.busquedaOrdenes(modelo, com, jTextField1.getText());

    }//GEN-LAST:event_jTextField1KeyReleased

    private void tablaPendientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPendientesMouseClicked
        //Tabla de pendientes ..
        DefaultTableModel modelo = (DefaultTableModel) tablaPendientes.getModel();
        String codigo = modelo.getValueAt(tablaPendientes.getSelectedRow(), 0) + "";  //id orden
        String cliente = modelo.getValueAt(tablaPendientes.getSelectedRow(), 1) + "";  //cliente

        if (evt.getClickCount() == 1) {
            System.out.println("Se ha hecho un click");
        }
        if (evt.getClickCount() == 2) {
            ventanaFacturas vf = new ventanaFacturas(codigo, cliente);
            vf.setVisible(true);
        }        
    }//GEN-LAST:event_tablaPendientesMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;


                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ventanaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
         new ventanaPrincipal().setVisible(true);
         }
         });*/
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JComboBox cmbSucursal;
    private javax.swing.JComboBox combo;
    private javax.swing.JComboBox combo1;
    private javax.swing.JComboBox comboTipo;
    private com.toedter.calendar.JDateChooser fechaCupon;
    private javax.swing.JButton jBAñadir;
    private javax.swing.JButton jBEliminar;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton52;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel labelAviso;
    private javax.swing.JLabel labelIconUsuario;
    private javax.swing.JLabel labelIcono;
    private javax.swing.JLabel labelTema;
    private javax.swing.JLabel labelTema1;
    private javax.swing.JLabel labelUsuarioLog;
    private javax.swing.JPanel panelClientes;
    private javax.swing.JPanel panelContador;
    private javax.swing.JPanel panelCorteCaja;
    private javax.swing.JPanel panelCotizaciones;
    private javax.swing.JPanel panelFacturas;
    private javax.swing.JPanel panelInicio;
    private javax.swing.JPanel panelProductos;
    private javax.swing.JPanel panelReportes;
    private javax.swing.JPanel panelUsuarios;
    private javax.swing.JPanel panelVentas;
    private com.toedter.calendar.JDateChooser primeraFecha;
    private javax.swing.JRadioButton radioGranFormato;
    private javax.swing.JRadioButton radioProductos;
    private javax.swing.JLabel reportesTotal;
    private com.toedter.calendar.JDateChooser segundaFecha;
    private javax.swing.JTextField tCliente;
    private javax.swing.JTextField tCliente1;
    private javax.swing.JTextField tDire;
    private javax.swing.JTextField tDire1;
    private javax.swing.JTextField tEmail;
    private javax.swing.JTextField tEmail1;
    private javax.swing.JTextField tNombre;
    private javax.swing.JTextField tNombre1;
    private javax.swing.JTextField tRFC;
    private javax.swing.JTextField tRFC1;
    private javax.swing.JTextField tRepre;
    private javax.swing.JTextField tRepre1;
    private javax.swing.JTextField tTelefono;
    private javax.swing.JTextField tTelefono1;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JTable tablaCortes;
    private javax.swing.JTable tablaCupones;
    private javax.swing.JTable tablaFacturas;
    private javax.swing.JTable tablaOrdenes;
    private javax.swing.JTable tablaPendientes;
    private javax.swing.JTable tablaReportes;
    private javax.swing.JTable tablaServ;
    private javax.swing.JTable tablaTrans;
    private javax.swing.JTable tablaUsuarios;
    private javax.swing.JTable tablaVenta;
    private javax.swing.JTable tablaVenta1;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBus;
    private javax.swing.JTextField txtBus1;
    private javax.swing.JTextField txtBus2;
    private javax.swing.JTextField txtBusquedaProductos;
    private javax.swing.JTextField txtCE;
    private javax.swing.JTextField txtCP;
    private javax.swing.JTextArea txtComentarios;
    private javax.swing.JTextArea txtComentarios1;
    private javax.swing.JTextField txtContra;
    private javax.swing.JTextField txtCupon;
    private javax.swing.JTextField txtCuponDesc;
    private javax.swing.JTextField txtDesc;
    private javax.swing.JTextField txtDescuento;
    private javax.swing.JTextField txtDescuento1;
    private javax.swing.JTextField txtDir;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtExistencias;
    private javax.swing.JTextField txtImpuestos;
    private javax.swing.JTextField txtImpuestos1;
    private javax.swing.JTextField txtMinima;
    private javax.swing.JTextField txtMunicipio;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombreP;
    private javax.swing.JTextField txtNombreU;
    private javax.swing.JTextField txtNumCliente;
    private javax.swing.JTextField txtPI;
    private javax.swing.JTextField txtPI2;
    private javax.swing.JTextField txtRFC;
    private javax.swing.JTextField txtRepre;
    private javax.swing.JTextField txtSub;
    private javax.swing.JTextField txtSub1;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotal1;
    private javax.swing.JScrollPane ventanaVentas;
    private javax.swing.JScrollPane ventanaVentas1;
    // End of variables declaration//GEN-END:variables
}
