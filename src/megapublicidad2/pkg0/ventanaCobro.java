package megapublicidad2.pkg0;


import java.awt.event.KeyEvent;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author German Valdez
 */
public class ventanaCobro extends javax.swing.JFrame {

    /**
     * Creates new form ventanaCobro
     */
    ventanaPrincipal ventas;
    ventanaFacturas facturas;
    //ventanaVentaCoti ventas2;
    Numero_a_Letra u = new Numero_a_Letra();
    String totalPago, ventana;
    Float total;

    public ventanaCobro(String totalPago, String ventana) {
        initComponents();
        setLocationRelativeTo(null);
        this.totalPago = totalPago;
        this.ventana = ventana;

        btnCancelar.setMnemonic(KeyEvent.VK_ESCAPE);
        lblPago.setText(totalPago);
        total = Float.parseFloat(totalPago);
        String num = totalPago;
        txtEfectivo.setText(totalPago);
        cambio();
        lblTxtTotal.setText(u.Convertir(num, true));

    }

    public void cambio() {
        if (!txtEfectivo.getText().equals("")) {

            float pago = Float.parseFloat(lblPago.getText());
            float efectivo = Float.parseFloat(txtEfectivo.getText());

            if (efectivo == pago) {
                lblCambio.setText("0.00");
                lblTxtCambio.setText(u.Convertir("0.00", true));
            } else {
                lblCambio.setText("" + (efectivo - pago));
                lblTxtCambio.setText(u.Convertir("" + (efectivo - pago), true));
            }
        }
    }

    public class Numero_a_Letra {

        private final String[] UNIDADES = {"", "un ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve "};
        private final String[] DECENAS = {"diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
            "diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ",
            "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa "};
        private final String[] CENTENAS = {"", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
            "setecientos ", "ochocientos ", "novecientos "};

        public Numero_a_Letra() {
        }

        public String Convertir(String numero, boolean mayusculas) {
            String literal = "";
            String parte_decimal;
            //si el numero utiliza (.) en lugar de (,) -> se reemplaza
            numero = numero.replace(".", ",");
            //si el numero no tiene parte decimal, se le agrega ,00
            if (numero.indexOf(",") == -1) {
                numero = numero + ",00";
            }
            //se valida formato de entrada -> 0,00 y 999 999 999,00
            if (Pattern.matches("\\d{1,9},\\d{1,2}", numero)) {
                //se divide el numero 0000000,00 -> entero y decimal
                String Num[] = numero.split(",");
                //de da formato al numero decimal
                parte_decimal = "PESOS " + Num[1] + "/100 MN.";
                //se convierte el numero a literal
                if (Integer.parseInt(Num[0]) == 0) {//si el valor es cero
                    literal = "cero ";
                } else if (Integer.parseInt(Num[0]) > 999999) {//si es millon
                    literal = getMillones(Num[0]);
                } else if (Integer.parseInt(Num[0]) > 999) {//si es miles
                    literal = getMiles(Num[0]);
                } else if (Integer.parseInt(Num[0]) > 99) {//si es centena
                    literal = getCentenas(Num[0]);
                } else if (Integer.parseInt(Num[0]) > 9) {//si es decena
                    literal = getDecenas(Num[0]);
                } else {//sino unidades -> 9
                    literal = getUnidades(Num[0]);
                }
                //devuelve el resultado en mayusculas o minusculas
                if (mayusculas) {
                    return (literal + parte_decimal).toUpperCase();
                } else {
                    return (literal + parte_decimal);
                }
            } else {//error, no se puede convertir
                return literal = null;
            }
        }

        /* funciones para convertir los numeros a literales */
        private String getUnidades(String numero) {// 1 - 9
            //si tuviera algun 0 antes se lo quita -> 09 = 9 o 009=9
            String num = numero.substring(numero.length() - 1);
            return UNIDADES[Integer.parseInt(num)];
        }

        private String getDecenas(String num) {// 99                        
            int n = Integer.parseInt(num);
            if (n < 10) {//para casos como -> 01 - 09
                return getUnidades(num);
            } else if (n > 19) {//para 20...99
                String u = getUnidades(num);
                if (u.equals("")) { //para 20,30,40,50,60,70,80,90
                    return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
                } else {
                    return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
                }
            } else {//numeros entre 11 y 19
                return DECENAS[n - 10];
            }
        }

        private String getCentenas(String num) {// 999 o 099
            if (Integer.parseInt(num) > 99) {//es centena
                if (Integer.parseInt(num) == 100) {//caso especial
                    return " cien ";
                } else {
                    return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
                }
            } else {//por Ej. 099 
                //se quita el 0 antes de convertir a decenas
                return getDecenas(Integer.parseInt(num) + "");
            }
        }

        private String getMiles(String numero) {// 999 999
            //obtiene las centenas
            String c = numero.substring(numero.length() - 3);
            //obtiene los miles
            String m = numero.substring(0, numero.length() - 3);
            String n = "";
            //se comprueba que miles tenga valor entero
            if (Integer.parseInt(m) > 0) {
                n = getCentenas(m);
                return n + "mil " + getCentenas(c);
            } else {
                return "" + getCentenas(c);
            }

        }

        private String getMillones(String numero) { //000 000 000        
            //se obtiene los miles
            String miles = numero.substring(numero.length() - 6);
            //se obtiene los millones
            String millon = numero.substring(0, numero.length() - 6);
            String n = "";
            if (millon.length() > 1) {
                n = getCentenas(millon) + "millones ";
            } else {
                n = getUnidades(millon) + "millon ";
            }
            return n + getMiles(miles);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        labelEfectivo = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtEfectivo = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        pEfec = new javax.swing.JRadioButton();
        pTar = new javax.swing.JRadioButton();
        pAbon = new javax.swing.JRadioButton();
        pComp = new javax.swing.JRadioButton();
        pCheq = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        radioCredito = new javax.swing.JRadioButton();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        labelInfo = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        lblPago = new javax.swing.JLabel();
        lblCambio = new javax.swing.JLabel();
        lblTxtTotal = new javax.swing.JLabel();
        lblTxtCambio = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        factura = new javax.swing.JRadioButton();
        rEntregado = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cobrando..");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setOpaque(false);

        labelEfectivo.setForeground(new java.awt.Color(255, 255, 255));
        labelEfectivo.setText("Efectivo");

        jButton1.setText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        txtEfectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEfectivoKeyReleased(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.Color.white));
        jPanel3.setOpaque(false);

        buttonGroup1.add(pEfec);
        pEfec.setForeground(new java.awt.Color(255, 255, 255));
        pEfec.setSelected(true);
        pEfec.setText("Efectivo");

        buttonGroup1.add(pTar);
        pTar.setForeground(new java.awt.Color(255, 255, 255));
        pTar.setText("Tarjeta");

        buttonGroup2.add(pAbon);
        pAbon.setForeground(new java.awt.Color(255, 255, 255));
        pAbon.setText("Anticipo");

        buttonGroup2.add(pComp);
        pComp.setForeground(new java.awt.Color(255, 255, 255));
        pComp.setSelected(true);
        pComp.setText("Pago Completo");

        buttonGroup1.add(pCheq);
        pCheq.setForeground(new java.awt.Color(255, 255, 255));
        pCheq.setText("Cheque");
        pCheq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pCheqActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton2.setText("Transferencia");

        buttonGroup1.add(radioCredito);
        radioCredito.setForeground(new java.awt.Color(255, 255, 255));
        radioCredito.setText("Credito");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pEfec)
                    .addComponent(pTar)
                    .addComponent(pComp)
                    .addComponent(pAbon)
                    .addComponent(pCheq, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioCredito)
                    .addComponent(jRadioButton2))
                .addGap(10, 10, 10))
            .addComponent(jSeparator5)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(pEfec)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pTar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pCheq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioCredito)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, Short.MAX_VALUE)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pComp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pAbon))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelEfectivo, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEfectivo, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(labelEfectivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar)
                .addContainerGap())
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 170, 360));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 20, 410, 11));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 55, 410, 16));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("TOTAL A PAGAR");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, -1, -1));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 162, 410, 11));

        labelInfo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labelInfo.setForeground(new java.awt.Color(255, 255, 255));
        labelInfo.setText("CAMBIO");
        jPanel1.add(labelInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 180, -1, -1));
        jPanel1.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 204, 410, 16));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/bandera.png"))); // NOI18N
        jLabel3.setText("  $");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 230, 90, 40));

        lblPago.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblPago.setForeground(new java.awt.Color(255, 255, 255));
        lblPago.setText("$ 0.00");
        jPanel1.add(lblPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 70, -1, 60));

        lblCambio.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblCambio.setForeground(new java.awt.Color(255, 255, 255));
        lblCambio.setText("$ 200.00");
        jPanel1.add(lblCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(372, 222, -1, 54));

        lblTxtTotal.setForeground(new java.awt.Color(255, 255, 255));
        lblTxtTotal.setText("-");
        lblTxtTotal.setVerifyInputWhenFocusTarget(false);
        jPanel1.add(lblTxtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 310, -1));

        lblTxtCambio.setForeground(new java.awt.Color(255, 255, 255));
        lblTxtCambio.setText("-");
        jPanel1.add(lblTxtCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 280, 320, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/bandera.png"))); // NOI18N
        jLabel6.setText("  $");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 90, 40));

        factura.setForeground(new java.awt.Color(255, 255, 255));
        factura.setText("Facturación");
        jPanel1.add(factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 320, -1, -1));

        rEntregado.setForeground(new java.awt.Color(255, 255, 255));
        rEntregado.setText("Entregado");
        jPanel1.add(rEntregado, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 340, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 610, 373));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondoCobro2.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 420));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        //  System.out.println("Efectivo: "+txtEfectivo.getText()+" Pago: "+totalpago);
        String efectivo = txtEfectivo.getText();
        String tipoPago = "", estadoPago = "", fact = "NO", entregado = "PENDIENTE";

        if (pEfec.isSelected()) {
            tipoPago = "EFECTIVO";
        } else if (pTar.isSelected()) {
            tipoPago = "TARJETA";
        } else if (pCheq.isSelected()) {
            tipoPago = "CHEQUE";
        } else if (radioCredito.isSelected()) {
            tipoPago = "CREDITO";
        } else {
            tipoPago = "TRANSFERENCIA";
        }

        if (pComp.isSelected()) {
            estadoPago = "COMPLETO";
        } else {
            estadoPago = "ABONO";
        }

        if (factura.isSelected()) {
            fact = "SI";
        }
        if (rEntregado.isSelected()) {
            entregado = "TERMINADO";
        }

        // ventas = new ventanaVenta2();
        if (ventana.equals("venta")) {
           ventas.cobrar(lblCambio.getText(), efectivo, estadoPago, tipoPago, totalPago, fact, entregado);
            this.dispose();
        } else {
            facturas.cobrar(lblCambio.getText(), efectivo, estadoPago, tipoPago, totalPago, fact, entregado);
            this.dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        //    ventas.cancelar();
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtEfectivoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEfectivoKeyReleased
        // TODO add your handling code here:
        try {
            float pago = Float.parseFloat(lblPago.getText());
            float efectivo = Float.parseFloat(txtEfectivo.getText());

            if (efectivo > pago) {

                if (!txtEfectivo.getText().equals("")) {
                    labelInfo.setText("CAMBIO");

                    if (efectivo == pago) {
                        lblCambio.setText("0.00");
                        lblTxtCambio.setText(u.Convertir("0.00", true));
                    } else {
                        lblCambio.setText("" + (efectivo - pago));
                        lblTxtCambio.setText(u.Convertir("" + (efectivo - pago), true));
                    }
                }
            } else {
                if (!txtEfectivo.getText().equals("")) {
                    labelInfo.setText("RESTANTE");
                    if (efectivo == pago) {
                        lblCambio.setText("0.00");
                        lblTxtCambio.setText(u.Convertir("0.00", true));
                    } else {
                        lblCambio.setText("" + (pago - efectivo));
                        lblTxtCambio.setText(u.Convertir("" + (pago - efectivo), true));
                    }
                }
            }
        } catch (Exception e) {//JOptionPane.showMessageDialog(this,"Error: "+e);
        }
    }//GEN-LAST:event_txtEfectivoKeyReleased
    private void pCheqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pCheqActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pCheqActionPerformed

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
            java.util.logging.Logger.getLogger(ventanaCobro.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 

catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaCobro.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 

catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaCobro.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 

catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaCobro.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        // java.awt.EventQueue.invokeLater(new Runnable() {
        // public void run() {
        //     new ventanaCobro().setVisible(true);
        //}
        //   });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JRadioButton factura;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel labelEfectivo;
    private javax.swing.JLabel labelInfo;
    private javax.swing.JLabel lblCambio;
    private javax.swing.JLabel lblPago;
    private javax.swing.JLabel lblTxtCambio;
    private javax.swing.JLabel lblTxtTotal;
    private javax.swing.JRadioButton pAbon;
    private javax.swing.JRadioButton pCheq;
    private javax.swing.JRadioButton pComp;
    private javax.swing.JRadioButton pEfec;
    private javax.swing.JRadioButton pTar;
    private javax.swing.JRadioButton rEntregado;
    private javax.swing.JRadioButton radioCredito;
    private javax.swing.JTextField txtEfectivo;
    // End of variables declaration//GEN-END:variables
}
