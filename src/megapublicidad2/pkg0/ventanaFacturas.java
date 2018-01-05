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

    public ventanaFacturas(String codigo, String cliente) {
        initComponents();
        setLocationRelativeTo(null);

        Conexion con = new Conexion();
        cn = con.conectar();

        this.codigo = codigo;
        this.cliente = cliente;

        //botonConcretar.setVisible(false);

        txtCodigo.setText(codigo);

        rellenarCampos();
        calcularAnticipos();

    }

    public void enviarCorreo() {

        c.setContrasenia("wyszcdlvkfaycili");
        c.setUsuarioCorreo("carlosgerman.vc@gmail.com");
        c.setAsunto("Recordatorio de pago");
        c.setMensaje("Megapublicidad te manda este mensaje para recordar que tienes cuentas pendientes con nosotros.");
        c.setDestino(txtCorreo.getText().trim());
        c.setNombreArchivo(txtTema.getText()+".pdf");
        c.setRutaArchivo(txtTema.getText()+".pdf");
        Controlador co = new Controlador();
        if (co.enviarCorreo(c)) {
            JOptionPane.showMessageDialog(this, "Se envió");
        } else {
            JOptionPane.showMessageDialog(this, "Error");
        }
    }

    public void parametros(String combo) {
        if (txtTipoFactura.getText().equals("VENTA")) {
            botonConcretar.setVisible(false);
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

        txtTipoFactura.setText(datos2[9]);

        String combo = datos2[3];
        parametros(combo);
    }

    public void calcularAnticipos() {
        if (a.calcularAnticipos(codigo) != null) {
            txtAnticipo.setText(a.calcularAnticipos(codigo));
            double adeudo = Double.parseDouble(txtTotal.getText()) - Double.parseDouble(txtAnticipo.getText());
            txtAdeudo.setText(adeudo + "");
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

            if (txtAdeudo.getText().equals("0.0") && entregado.equals("TERMINADO")) {
                String sql = "UPDATE ventas SET estadoPago='ACOMPLETO', estadoServicio='TERMINADO' "
                        + "WHERE id = " + txtCodigo.getText();
                CallableStatement cmd = cn.prepareCall(sql);
                cmd.execute();
            } else if (txtAdeudo.getText().equals("0.0") && entregado.equals("NO")) {
                String sql = "UPDATE ventas SET estadoPago='ACOMPLETO' "
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
        jPanel9 = new javax.swing.JPanel();
        labelTotal = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        txtTema = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        botonConcretar = new javax.swing.JButton();
        txtFecha = new javax.swing.JTextField();
        txtTipoFactura = new javax.swing.JLabel();
        txt_fichero = new javax.swing.JTextField();
        txtRuta = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Facturas");

        jLabel1.setText("# Factura");

        jLabel2.setText("# Cliente");

        jLabel3.setText("Nombre");

        jLabel4.setText("Telefono");

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

        jLabel6.setText("Atendió");

        comboEstatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TERMINADO", "PENDIENTE" }));

        jLabel7.setText("Estatus");

        jLabel8.setText("Fecha");

        txtComentarios.setColumns(20);
        txtComentarios.setRows(5);
        jScrollPane1.setViewportView(txtComentarios);

        jLabel9.setText("Comentarios");

        jLabel10.setText("Subtotal");

        jLabel11.setText("Impuestos");

        jLabel12.setText("Descuento");

        jLabel13.setText("Anticipo");

        jLabel14.setText("Adeudo");

        jLabel15.setText("Total");

        jLabel16.setText("Representante");

        jButton1.setText("Abonar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Factura PDF");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Enviar Correo");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Factura Producción");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
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
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addContainerGap(249, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(112, 144, 244));

        labelTotal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelTotal.setForeground(new java.awt.Color(255, 255, 255));
        labelTotal.setText("-");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("TOTAL: $");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addGap(11, 11, 11)
                .addComponent(labelTotal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(labelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jButton5.setText("Cancelar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel17.setText("Tema");

        botonConcretar.setText("Concretar");
        botonConcretar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonConcretarActionPerformed(evt);
            }
        });

        txtTipoFactura.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonConcretar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel9)
                                .addComponent(jScrollPane1)
                                .addComponent(jScrollPane10)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2)
                                                .addComponent(txtNumCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                                                .addComponent(jLabel4)
                                                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel5)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(txtTipoFactura)))))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(comboEstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel7))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel8)
                                                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jLabel16)
                                        .addComponent(txtRepre, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(txt_fichero, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(21, 21, 21)
                                            .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
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
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtTipoFactura)
                                            .addComponent(txt_fichero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(12, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAnticipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAdeudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel15)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtImp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(botonConcretar))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

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
        if (txtAdeudo.getText().equals("0.0")) {
            JOptionPane.showMessageDialog(this, "Orden Liquidada");
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
            desktop.open(new java.io.File(txtTema.getText() + ".PDF"));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se puede abrir archivo." + ex.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        ArchivoPDF p = new ArchivoPDF();

        DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
        Object[][] articulos = obtenerArticulos(modelo);
        try {
            p.pdfCotización(txtTema.getText(), txtCodigo.getText(), txtTema.getText(), articulos, "Venta", txtFecha.getText(), labelTotal.getText(),
                    txtCliente.getText(), txtRepre.getText(), "", "", txtCorreo.getText(),
                    "", txtImp.getText(), txtSub.getText(), txtDesc.getText(), txtUsuario.getText());
        } catch (DocumentException ex) {
            Logger.getLogger(ventanaFacturas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ventanaFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new java.io.File(txtTema.getText() + ".PDF"));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se puede abrir archivo." + ex.getMessage());
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //Obtener archivo 
      /*  JFileChooser file = new JFileChooser();
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

            enviarCorreo();

        }*/
        enviarCorreo();


    }//GEN-LAST:event_jButton3ActionPerformed

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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
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
    private javax.swing.JLabel labelTotal;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtAdeudo;
    private javax.swing.JTextField txtAnticipo;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextArea txtComentarios;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDesc;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtImp;
    private javax.swing.JTextField txtNumCliente;
    private javax.swing.JTextField txtRepre;
    private javax.swing.JTextField txtRuta;
    private javax.swing.JTextField txtSub;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTema;
    private javax.swing.JLabel txtTipoFactura;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JTextField txt_fichero;
    // End of variables declaration//GEN-END:variables
}
