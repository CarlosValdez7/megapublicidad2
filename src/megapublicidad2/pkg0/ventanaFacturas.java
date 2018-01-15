/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package megapublicidad2.pkg0;

import Conexion.Conexion;
import Metodos.ArchivoPDF;
import Metodos.Controlador;
import Metodos.Correo;
import Metodos.Registros;
import Metodos.Ticket2;
import Metodos.Ventas;
import com.itextpdf.text.DocumentException;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle.Control;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author German Valdez
 */
public class ventanaFacturas extends javax.swing.JFrame {

    /**
     * Creates new form ventanaFacturas
     */
    String codigo = "", cliente = "";
    Registros a = new Registros();
    ventanaFacturas facturas;
    Connection cn;
    private JPanel contentPane;
    File fichero = null;
    Correo c = new Correo();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public ventanaFacturas(String codigo, String cliente) {
        initComponents();
        setLocationRelativeTo(null);

        Conexion con = new Conexion();
        cn = con.conectar();

        this.codigo = codigo;
        this.cliente = cliente;

        txtCodigo.setText(codigo);

        rellenarCampos();
        calcularAnticipos();

    }

    public void enviarCorreo() {
        String asunto = "Recordatorio de pago",
                mensaje = "Por la presente queremos recordar a Usted, el vencimiento de la factura No. " + txtCodigo.getText()
                + " cuyo importe aún no ha sido saldado.\n En caso de que haya realizado dicho pago hacer caso omiso a este mensaje.\n"
                + "Megapublicidad agradece su preferencia.";
        String archivo = "mega200px.png", ruta = "mega200px.png";

        if (txtFacturaTipo.getText().equals("Cotizacion")) {
            asunto = "Cotizacion Megapublicidad";
            mensaje = "Le adjunto la cotizacion";
        }

        c.setContrasenia("zwrapsdyrjmnnypo");
        c.setUsuarioCorreo("meganayarit@gmail.com");
        c.setAsunto(asunto);
        c.setMensaje(mensaje);
        c.setDestino(txtCorreo.getText().trim());

        if (!txt_fichero.getText().equals("-") && !txtRuta.getText().equals("-")) {
            archivo = txt_fichero.getText();
            ruta = txtRuta.getText();
        }
        c.setNombreArchivo(archivo);
        c.setRutaArchivo(ruta);
        Controlador co = new Controlador();
        if (co.enviarCorreo(c)) {
            JOptionPane.showMessageDialog(this, "Correo enviado");
        } else {
            JOptionPane.showMessageDialog(this, "Error");
        }
    }

    public void parametros(String combo) {
        if (txtFacturaTipo.getText().equals("VENTA")) {
            botonConcretar.setVisible(false);
        } else {
            botonConcretar.setVisible(true);
        }

        if (combo.equals("PENDIENTE")) {
            comboEstatus.setSelectedItem("PENDIENTE");

        } else {
            comboEstatus.setSelectedItem("TERMINADO");
        }

    }

    public void rellenarCampos() {
            //Rellenar tabla de productos
            DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
            a.tablaDetProd(modelo, codigo);

            //Rellenar campos de datos de cliente
            if (a.datosCliente(cliente) != null) {
                String datos[] = a.datosCliente(cliente).split(",");
                txtNumCliente.setText(datos[0]);
                txtCliente.setText(datos[1]);
                txtRepre.setText(datos[2]);
                txtCorreo.setText(datos[6]);
                txtTelefono.setText(datos[7]);
            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado");
            }
            //Rellenar datos de venta totales,deudas,etc
            String datos2[] = a.datosVenta(codigo).split(",");
            txtTema.setText(datos2[0]);
            txtTotal.setText(datos2[1]);
            txtFecha.setText(datos2[2]);
            txtUsuario.setText(datos2[4]);
            txtSub.setText(datos2[5]);
            txtDesc.setText(datos2[6]);
            txtImp.setText(datos2[7]);
            txtComentarios.setText(datos2[8]);

            txtFacturaTipo.setText(datos2[9]);

            String combo = datos2[3];
            parametros(combo);
        
    }

    public void calcularAnticipos() {
        try {
            if (a.calcularAnticipos(codigo) != null) {
                txtAnticipo.setText(a.calcularAnticipos(codigo));
                double adeudo = Double.parseDouble(txtTotal.getText()) - Double.parseDouble(txtAnticipo.getText());
                txtAdeudo.setText(df.format(adeudo) + "");
            }
        } catch (Exception e) {
        }
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

    public void abonar(String efectivo, String tipoPago, String pagos, String entregado) {

        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(GregorianCalendar.getInstance().getTime());
        SimpleDateFormat fTicket = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat hora = new SimpleDateFormat("hh:mm a");
        Calendar cal = GregorianCalendar.getInstance();
        String fechaTicket = fTicket.format(cal.getTime());
        String horaTicket = hora.format(cal.getTime());


        try {
            if (!efectivo.equals("")) {
                String sql = "INSERT INTO detalle_abonos VALUES (" + txtCodigo.getText() + "," + efectivo + ","
                        + "'" + fecha + "','" + tipoPago + "')";
                CallableStatement cmd = cn.prepareCall(sql);
                cmd.execute();
            }

            calcularAnticipos();

            if (txtAdeudo.getText().equals("0.00") && entregado.equals("TERMINADO")) {
                String sql = "UPDATE ventas SET estadoPago='ACOMPLETO', estadoServicio='TERMINADO' "
                        + "WHERE id = " + txtCodigo.getText();
                CallableStatement cmd = cn.prepareCall(sql);
                cmd.execute();
            } else if (txtAdeudo.getText().equals("0.00") && entregado.equals("NO")) {
                String sql = "UPDATE ventas SET estadoPago='ACOMPLETO', estadoServicio='PENDIENTE' "
                        + "WHERE id = " + txtCodigo.getText();
                CallableStatement cmd = cn.prepareCall(sql);
                cmd.execute();
            }

        } catch (SQLException ex) {
            Logger.getLogger(ventanaFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Abonos();
        String tic =
                "         LIQUIDACION              \n"
                + "       MEGAPUBLICIDAD\n"
                + "Prop Clara Hortencia Gomez Parra\n"
                + "Lerdo No. 116-A Pte. \n"
                + "Zona Centro\n"
                + "C.P. 63000 TEPIC, NAYARIT\n"
                + "================================\n"
                + "    **** NOTA DE PAGO ***\n"
                + "FECHA: " + fechaTicket + "  " + horaTicket + "\n"
                + "NO. DE VENTA: " + txtCodigo.getText() + "\n"
                + "================================\n";
        double totArt = 0;
        for (int i = 0; i < tablaProductos.getRowCount(); i++) {
            String cod = tablaProductos.getValueAt(i, 0) + "";
            String prod = tablaProductos.getValueAt(i, 1) + "";
            double can = Integer.parseInt(tablaProductos.getValueAt(i, 2) + "");
            String pu = tablaProductos.getValueAt(i, 3) + "";
            String tot = tablaProductos.getValueAt(i, 4) + "";
            tic +=
                    cod + "   " + prod + "\n"
                    + "         " + can + "\t" + pu + "\t" + tot + "\n";
            totArt += can;

        }

        tic += "\nPAGO:\t" + "$" + efectivo + "\n\n"
                + "  >>>>> TIPO DE PAGO <<<<<  \n"
                + "" + pagos + "\n"
                + "================================\n"
                + " !!GRACIAS POR SU PREFERENCIA!!\n\n\n\n";

        System.out.println(tic);

        Ticket2 t = new Ticket2(tic);

        JOptionPane.showMessageDialog(null, "Pago registrado");


    }

    public void cobrar(String cambio, String efectivo, String estadoPago, String tipoPago, String totalPago, String factu, String entregado) {
        Ventas v = new Ventas();
        ArchivoPDF a = new ArchivoPDF();
        String status = estadoPago;
        String iva, pagos = "";

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

        SimpleDateFormat fTicket = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat fBase = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hora = new SimpleDateFormat("hh:mm a");
        Calendar cal = GregorianCalendar.getInstance();

        String fechaTicket = fTicket.format(cal.getTime());
        String fechaBase = fBase.format(cal.getTime());

        String horaTicket = hora.format(cal.getTime());


        if (status.equals("COMPLETO")) {
            //v.RealizaVenta(modelo, nombre, concepto, totalPago, fechaBase, "VENTA", tipoPago, estadoPago, factu, entregado, jLabel20.getText());
            v.concretaVenta(txtCodigo.getText(), cliente, txtTema.getText(), fechaBase, "VENTA",
                    tipoPago, estadoPago, factu, entregado, txtUsuario.getText());


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
                    + "NO. DE VENTA: " + txtCodigo.getText() + "\n"
                    + "================================\n"
                    + "CODIGO   CANT\tP.UNIT\tIMPORTE\n"
                    + "================================\n";

            int totArt = 0;
            for (int i = 0; i < tablaProductos.getRowCount(); i++) {
                String cod = tablaProductos.getValueAt(i, 0) + "";
                String prod = tablaProductos.getValueAt(i, 1) + "";
                double can = Integer.parseInt(tablaProductos.getValueAt(i, 2) + "");
                String pu = tablaProductos.getValueAt(i, 3) + "";
                String tot = tablaProductos.getValueAt(i, 4) + "";
                tic +=
                        cod + "   " + prod + "\n"
                        + "         " + can + "\t" + pu + "\t" + tot + "\n";
                totArt += can;
            }

            tic += //"\nA PAGAR:\t" + "$" + labelTotal.getText() + "\n"
                    //+ "\tDESCUENTO:\t" + lblDesc.getText() + "\n"
                    "\nSUBTOTAL:\t" + "$" + txtSub.getText() + "\n"
                    + "IMPUESTOS:\t" + "$" + txtImp.getText() + "\n"
                    + "TOTAL:\t\t" + "$" + txtTotal.getText() + "\n"
                    + "PAGADO:\t\t" + "$" + efectivo + "\n"
                    + "CAMBIO:\t\t" + "$" + cambio + "\n"
                    + "  >>>>> TIPO DE PAGO <<<<<  \n"
                    + "" + pagos + "\n\n"
                    + "ARTICULOS VENDIDOS: " + totArt + "\n"
                    + "================================\n"
                    + "       " + txtUsuario.getText() + "\n"
                    + " !!! GRACIAS POR SU COMPRA !!!\n\n\n\n";

            System.out.println(tic);

            Ticket2 p = new Ticket2(tic);

            JOptionPane.showMessageDialog(null, "Venta/Apartado finalizado");

        } else {
            //String idventa = ven.create(tNombre.getText(), concepto, labelTotal.getText(), fechaBase, status, tipoPago, estadoPago) + "";
            //u.RealizaVenta2(modelo, efectivo, nombre, concepto, totalPago, fechaBase, "VENTA", tipoPago, estadoPago, factu, entregado, jLabel20.getText());
            v.concretaVenta2(txtCodigo.getText(), cliente, txtTema.getText(), fechaBase, "VENTA",
                    tipoPago, estadoPago, factu, entregado, efectivo, txtUsuario.getText());

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
                    + "NO. DE VENTA: " + txtCodigo.getText() + "\n"
                    + "================================\n"
                    + "CODIGO   CANT\tP.UNIT\tIMPORTE\n"
                    + "================================\n";

            int totArt = 0;
            for (int i = 0; i < tablaProductos.getRowCount(); i++) {
                String cod = tablaProductos.getValueAt(i, 0) + "";
                String prod = tablaProductos.getValueAt(i, 1) + "";
                double can = Integer.parseInt(tablaProductos.getValueAt(i, 2) + "");
                String pu = tablaProductos.getValueAt(i, 3) + "";
                String tot = tablaProductos.getValueAt(i, 4) + "";
                tic +=
                        cod + "   " + prod + "\n"
                        + "         " + can + "\t" + pu + "\t" + tot + "\n";
                totArt += can;

            }

            tic += //"\nTOTAL:\t\t" + "$" + labelTotal.getText() + "\n"
                    //  + "\tDESCUENTO:\t" + lblDesc.getText() + "\n"
                    "\nSUBTOTAL:\t" + "$" + txtSub.getText() + "\n"
                    + "IMPUESTOS:\t" + "$" + txtImp.getText() + "\n"
                    + "TOTAL:\t\t" + "$" + txtTotal.getText() + "\n"
                    + "DEJÓ:\t\t" + "$" + efectivo + "\n"
                    + "RESTANTE:\t" + "$" + cambio + "\n"
                    + "  >>>>> TIPO DE PAGO <<<<<  \n"
                    + "" + pagos + "\n\n"
                    + "ARTICULOS APARTADOS: " + totArt + "\n"
                    + "================================\n"
                    + "       " + txtUsuario.getText() + "    \n"
                    + " !!! GRACIAS POR SU COMPRA !!!\n\n\n\n";

            System.out.println(tic);

            Ticket2 t = new Ticket2(tic);

            JOptionPane.showMessageDialog(null, "Venta/Apartado finalizado");

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtNumCliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        txtUsuario = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        comboEstatus = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtComentarios = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtSub = new javax.swing.JTextField();
        txtImp = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtDesc = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtAnticipo = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtAdeudo = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtRepre = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        txtTema = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        botonConcretar = new javax.swing.JButton();
        txtFecha = new javax.swing.JTextField();
        txt_fichero = new javax.swing.JTextField();
        txtRuta = new javax.swing.JTextField();
        txtFacturaTipo = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Facturas");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setOpaque(false);

        txtCodigo.setEditable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("# Factura");

        txtNumCliente.setEditable(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("# Cliente");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nombre");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Telefono");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Correo Electronico");

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Producto/Servicio", "Cantidad", "Precio Unitario", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(tablaProductos);
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(5);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(20);
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(20);
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Atendió");

        comboEstatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TERMINADO", "PENDIENTE" }));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Estatus");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Fecha");

        txtComentarios.setColumns(20);
        txtComentarios.setRows(5);
        jScrollPane1.setViewportView(txtComentarios);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Comentarios");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Subtotal");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Impuestos");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Descuento");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Anticipo");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Adeudo");

        txtAdeudo.setText("-");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Total");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Representante");

        jPanel2.setOpaque(false);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/dinero.png"))); // NOI18N
        jButton1.setText("Abonar");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf grande.png"))); // NOI18N
        jButton2.setText("Abrir PDF");
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/gmail.png"))); // NOI18N
        jButton3.setText("Enviar Correo");
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf grande.png"))); // NOI18N
        jButton4.setText("Crear PDF");
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/comprobado (2).png"))); // NOI18N
        jButton6.setText("Terminar");
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(112, 144, 244));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jButton5.setText("Cancelar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Tema");

        botonConcretar.setText("Concretar");
        botonConcretar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonConcretarActionPerformed(evt);
            }
        });

        txt_fichero.setText("-");

        txtRuta.setText("-");

        txtFacturaTipo.setEditable(false);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/carpeta.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Sin Archivo");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(botonConcretar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton5))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane10)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(txtCodigo))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel6))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel17)
                                                    .addComponent(txtTema, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel2)
                                                    .addComponent(txtNumCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel3)
                                                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtFacturaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txt_fichero, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtRuta)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton7))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(comboEstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel16)
                                    .addComponent(txtRepre, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel9)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(txtSub, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(txtImp, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(txtAnticipo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(txtAdeudo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(comboEstatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addGap(26, 26, 26)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNumCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtRepre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFacturaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_fichero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton7))))
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtAnticipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel14)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtAdeudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addGap(26, 26, 26))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel12)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(botonConcretar)
                            .addComponent(jButton8)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1030, 550));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoMenu.png"))); // NOI18N
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(null, "¿Seguro que deseas canelar?", null, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            a.cancelarCoti(codigo);
            this.dispose();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void botonConcretarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonConcretarActionPerformed
        // TODO add your handling code here:
        ventanaCobro vc = new ventanaCobro(txtTotal.getText(), "cotizacion");
        vc.facturas = this;
        vc.setVisible(true);
    }//GEN-LAST:event_botonConcretarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (txtAdeudo.getText().equals("0.00")) {
            JOptionPane.showMessageDialog(this, "Orden Liquidada");
        } else if (txtAdeudo.getText().equals("-")) {
            JOptionPane.showMessageDialog(this, "Esta orden no admite abonos");
        } else {
            ventanaAbonos va = new ventanaAbonos(txtAdeudo.getText());
            va.facturas = this;
            va.setVisible(true);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new java.io.File("C:\\Users\\Lenovo\\Documents\\Documentos Sistema\\" + txtTema.getText() + ".pdf"));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "El PDF no existe");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        ArchivoPDF p = new ArchivoPDF();

        DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
        Object[][] articulos = obtenerArticulos(modelo);
        try {
            p.pdfCotización2(txtTema.getText(), txtCodigo.getText(), txtTema.getText(), articulos, "Venta", txtFecha.getText(), txtTotal.getText(),
                    txtCliente.getText(), txtRepre.getText(), "", "", txtCorreo.getText(),
                    "", txtImp.getText(), txtSub.getText(), txtDesc.getText(), txtUsuario.getText());
        } catch (DocumentException ex) {
            Logger.getLogger(ventanaFacturas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ventanaFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new java.io.File("C:\\Users\\Lenovo\\Documents\\Documentos Sistema\\" + txtTema.getText() + ".pdf"));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se puede abrir archivo." + ex.getMessage());
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        enviarCorreo();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        String combo = comboEstatus.getSelectedItem() + "";
        if (combo.equals("TERMINADO")) {
            JOptionPane.showMessageDialog(this, "Esta orden ya fue terminada");
        } else {
            int result = JOptionPane.showConfirmDialog(null, "¿Desea continuar?",
                    null, JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                a.actualizaEstadoServ(txtCodigo.getText());
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Orden de servicio pendiente");
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // Obtener archivo
        JFileChooser file = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.pdf", "pdf");
        file.setFileFilter(filtro);

        int seleccion = file.showOpenDialog(contentPane);
        //Si el usuario, pincha en aceptar
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            //Seleccionamos el fichero
            fichero = file.getSelectedFile();
            //Ecribe la ruta del fichero seleccionado en el campo de texto
            txt_fichero.setText(fichero.getName());
            txtRuta.setText(fichero.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        txt_fichero.setText("-");
        txtRuta.setText("-");
    }//GEN-LAST:event_jButton8ActionPerformed

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
            java.util.logging.Logger.getLogger(ventanaFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
         new ventanaFacturas(codigo).setVisible(true);
         }
         });*/
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonConcretar;
    private javax.swing.JComboBox comboEstatus;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtAdeudo;
    private javax.swing.JTextField txtAnticipo;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextArea txtComentarios;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDesc;
    private javax.swing.JTextField txtFacturaTipo;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtImp;
    private javax.swing.JTextField txtNumCliente;
    private javax.swing.JTextField txtRepre;
    private javax.swing.JTextField txtRuta;
    private javax.swing.JTextField txtSub;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTema;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JTextField txt_fichero;
    // End of variables declaration//GEN-END:variables
}
