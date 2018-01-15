/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Metodos;

import Conexion.Conexion;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author German Valdez
 */
public class ArchivoPDF {

    Connection cn;

    public ArchivoPDF() {
        Conexion con = new Conexion();
        cn = con.conectar();
    }
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void pdfCotización(String nombreArchivo, String folio, String concepto, Object[][] articulos,
            String tipo, String fecha, String total, String nombre, String repre, String RFC,
            String dire, String email, String comentario, String iva, String subtotal, String descuento, String usuario)
            throws DocumentException, IOException {

        try {
            Document documento = new Document(PageSize.LETTER);
            Image img = Image.getInstance("mega200px.png");

            FileOutputStream ficheroPdf = new FileOutputStream("C:\\Users\\Lenovo\\Documents\\Documentos Sistema\\" + nombreArchivo + ".pdf");
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);

            documento.open();
            img.setAbsolutePosition(380f, 695f);


            Font fuente = new Font();
            Font fuente2 = new Font();
            Font fuenteNegrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            fuente.setSize(8);
            fuente2.setSize(9);

            documento.add(img);
            documento.add(new Paragraph("MEGAPUBLICIDAD", fuenteNegrita));
            documento.add(new Paragraph("DUCJ681122GM7", fuenteNegrita));
            documento.add(new Paragraph("Av. Juan Escutia 134-A", fuente));
            documento.add(new Paragraph("Col. Centro C.P 63000", fuente));
            documento.add(new Paragraph("Tepic Nayarit, Mexico\n", fuente));
            documento.add(new Paragraph("Telefono 218-0320 Celular: 311-269-3605\n", fuente));
            documento.add(new Paragraph("Email: meganayarit@gmail.com\n", fuente));

            documento.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", fuente));
            documento.add(new Paragraph("Folio " + folio + "                                                                          " + tipo + "                                                             Fecha " + fecha, fuente2));
            documento.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", fuente));
            documento.add(new Paragraph("Nombre: " + nombre + "   Representante: " + repre + "   RFC: " + RFC, fuente));
            documento.add(new Paragraph("Dirección: " + dire + "   Email: " + email + "", fuente));
            documento.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", fuente));

            String tipoComentario = "";
            if (tipo.equals("Venta")) {
                tipoComentario = "venta";
            } else {
                tipoComentario = "cotización";
            }

            documento.add(new Paragraph("\n" + comentario, fuente));
            documento.add(new Paragraph("Le envio la " + tipoComentario + " solicitada, quedo a sus ordenes para cualquier duda o comentario.\n"
                    + "El tiempo de entrega es de 5 días hábiles una vez autorizado el diseño, el 50% de anticipo para iniciar el proyecto y el 50% al entregar.", fuente2));
            documento.add(new Paragraph("\n"));

            PdfPTable tabla = crearTablaArticulos(articulos, subtotal, iva, descuento, total, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla);

            documento.add(new Paragraph("\n\n\n"
                    + "                                                               __________________________________________\n"
                    + "                                                                                           " + usuario, fuente2));

            documento.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error en pdfVenta" + ex);
        }

    }

    public void pdfCotización2(String nombreArchivo, String folio, String concepto, Object[][] articulos,
            String tipo, String fecha, String total, String nombre, String repre, String RFC,
            String dire, String email, String comentario, String iva, String subtotal, String descuento, String usuario)
            throws DocumentException, IOException {

        try {
            Document documento = new Document(PageSize.LETTER);
            Image img = Image.getInstance("mega200px.png");

            FileOutputStream ficheroPdf = new FileOutputStream("C:\\Users\\Lenovo\\Documents\\Documentos Sistema\\" + nombreArchivo + ".pdf");
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);

            documento.open();
            img.setAbsolutePosition(380f, 695f);

            Font fuente = new Font();
            Font fuente2 = new Font();
            Font fuenteNegrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            fuente.setSize(8);
            fuente2.setSize(9);

            documento.add(img);
            documento.add(new Paragraph("MEGAPUBLICIDAD", fuenteNegrita));
            documento.add(new Paragraph("DUCJ681122GM7", fuenteNegrita));
            documento.add(new Paragraph("Av. Juan Escutia 134-A", fuente));
            documento.add(new Paragraph("Col. Centro C.P 63000", fuente));
            documento.add(new Paragraph("Tepic Nayarit, Mexico\n", fuente));
            documento.add(new Paragraph("Telefono 218-0320 Celular: 311-269-3605\n", fuente));
            documento.add(new Paragraph("Email: meganayarit@gmail.com\n", fuente));

            documento.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", fuente));
            documento.add(new Paragraph("Folio " + folio + "                                                                          " + tipo + "                                                             Fecha " + fecha, fuente2));
            documento.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", fuente));
            documento.add(new Paragraph("Nombre: " + nombre + "   Representante: " + repre + "   RFC: " + RFC, fuente));
            documento.add(new Paragraph("Dirección: " + dire + "   Email: " + email + "", fuente));
            documento.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", fuente));

            String tipoComentario = "";
            if (tipo.equals("Venta")) {
                tipoComentario = "venta";
            } else {
                tipoComentario = "cotización";
            }

            documento.add(new Paragraph("\n" + comentario, fuente));
            documento.add(new Paragraph("Le envio la " + tipoComentario + " solicitada, quedo a sus ordenes para cualquier duda o comentario.\n"
                    + "El tiempo de entrega es de 5 días hábiles una vez autorizado el diseño, el 50% de anticipo para iniciar el proyecto y el 50% al entregar.", fuente2));
            documento.add(new Paragraph("\n"));

            PdfPTable tabla = crearTablaArticulos2(articulos, subtotal, iva, descuento, total, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla);

            documento.add(new Paragraph("\n\n\n"
                    + "                                                               __________________________________________\n"
                    + "                                                                                           " + usuario, fuente2));

            documento.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error en pdfVenta" + ex);
        }

    }

    public void pdfReporteVentas(String nombreArchivo, Object[][] articulos, String fecha, String fecha2, String total, String comen)
            throws DocumentException, IOException {
        try {
            Document documento = new Document(PageSize.LETTER);

            Image img = Image.getInstance("mega200px.png");

            FileOutputStream ficheroPdf = new FileOutputStream("C:\\Users\\Lenovo\\Documents\\Documentos Sistema\\Reportes\\" + nombreArchivo + ".pdf");
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);


            documento.open();
            img.setAbsolutePosition(380f, 695f);
            documento.add(img);

            Font fuente = new Font();
            Font fuente2 = new Font();
            Font fuenteNegrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            fuente.setSize(8);
            fuente2.setSize(9);

            documento.add(img);
            documento.add(new Paragraph("MEGAPUBLICIDAD", fuenteNegrita));
            documento.add(new Paragraph("DUCJ681122GM7", fuenteNegrita));
            documento.add(new Paragraph("Av. Juan Escutia 134-A", fuente));
            documento.add(new Paragraph("Col. Centro C.P 63000", fuente));
            documento.add(new Paragraph("Tepic Nayarit, Mexico\n", fuente));
            documento.add(new Paragraph("Telefono 218-0320 Celular: 311-269-3605\n", fuente));
            documento.add(new Paragraph("Email: meganayarit@gmail.com\n", fuente));

            documento.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", fuente));
            documento.add(new Paragraph("              " + comen, fuente));
            documento.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", fuente));


            documento.add(new Paragraph("\n"));

            PdfPTable tabla = crearTablaReporte(articulos, "", "", total, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla);

            documento.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error en pdfVenta" + ex);
        }

    }

    public void pdfCorteCaja(String nombreArchivo, Object[][] articulos, Object[][] articulos2, Object[][] articulos3,Object[][] articulos4, String fecha, String total, String usuario,
            String eC, String tC, String cC, String sE, String sT, String sC,
            String sacadoCaja, String sacadoTarjeta, String sacadoCheque, 
            String totCaja, String totDif, String gastos,String totalTipos,String trans)
            throws DocumentException, IOException {
        try {
            Document documento = new Document(PageSize.LETTER);
            Image img = Image.getInstance("mega200px.png");

            FileOutputStream ficheroPdf = new FileOutputStream("C:\\Users\\Lenovo\\Documents\\Documentos Sistema\\Cortes\\" + nombreArchivo + ".pdf");
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);

            documento.open();
            img.setAbsolutePosition(380f, 695f);
            documento.add(img);

            Font fuente = new Font();
            Font fuente2 = new Font();
            Font fuenteNegrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            fuente.setSize(8);
            fuente2.setSize(9);

            documento.add(new Paragraph("MEGAPUBLICIDAD", fuenteNegrita));
            documento.add(new Paragraph("DUCJ681122GM7", fuenteNegrita));
            documento.add(new Paragraph("Av. Juan Escutia 134-A", fuente));
            documento.add(new Paragraph("Col. Centro C.P 63000", fuente));
            documento.add(new Paragraph("Tepic Nayarit, Mexico\n", fuente));
            documento.add(new Paragraph("Telefono 218-0320 Celular: 311-269-3605\n", fuente));
            documento.add(new Paragraph("Email: meganayarit@gmail.com\n", fuente));

            documento.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", fuente));
            documento.add(new Paragraph("                                                  Corte de caja del dia " + fecha));
            documento.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", fuente));
            //            documento.add(new Paragraph("\n"));

            documento.add(new Paragraph("Reporte de ventas\n\n", fuente));
            PdfPTable tabla = crearTablaCorte(articulos, "", "", total, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla);
            documento.add(new Paragraph("\n"));
                        
            documento.add(new Paragraph("Reporte de Transferencias\n\n", fuente));
            PdfPTable tabla5 = crearTablaTransferencias(articulos4, trans, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla5);
            documento.add(new Paragraph("\n"));
                        
            documento.add(new Paragraph("Reporte de gastos\n\n", fuente));
            PdfPTable tabla3 = crearTablaCorte3(articulos2, gastos, "", total, documento.getPageSize().getWidth() / 2 - 40, eC, tC, cC, sE, sT, sC);
            documento.add(tabla3);

            documento.add(new Paragraph("\n"));
            documento.add(new Paragraph("Reporte de corte de caja\n\n", fuente));
            PdfPTable tabla2 = crearTablaCorte2(articulos, "", "", total, documento.getPageSize().getWidth() / 2 - 40, eC, tC,
                    cC, sE, sT, sC, sacadoCaja, sacadoTarjeta, sacadoCheque, totCaja, totDif);
            documento.add(tabla2);

            documento.add(new Paragraph("\n"));
            documento.add(new Paragraph("Reporte de tipos\n\n", fuente));
            PdfPTable tabla4 = crearTablaCorte4(articulos3,totalTipos, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla4);


            documento.add(new Paragraph("\n\n\n"
                    + "                                                                           __________________________________________\n"
                    + "                                                                                                            " + usuario,fuente));


            documento.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error en pdfVenta" + ex);
        }

    }

    private PdfPTable crearTablaArticulos(Object[][] articulos, String simp, String impuestos, String descuento, String total, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(5);
        Font fuenteTabla = new Font();
        fuenteTabla.setSize(9);

        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        float[] medidaCeldas = {7f, 80.80f, 20.60f, 30.60f, 25.60f};

        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        Paragraph columna1 = new Paragraph("#");
        Paragraph columna2 = new Paragraph("Producto/Servicio");
        Paragraph columna3 = new Paragraph("Cantidad");
        Paragraph columna4 = new Paragraph("Precio Unitario");
        Paragraph columna5 = new Paragraph("Importe");

        columna1.getFont().setStyle(Font.BOLD);
        columna1.getFont().setSize(9);

        columna2.getFont().setStyle(Font.BOLD);
        columna2.getFont().setSize(9);

        columna3.getFont().setStyle(Font.BOLD);
        columna3.getFont().setSize(9);

        columna4.getFont().setStyle(Font.BOLD);
        columna4.getFont().setSize(9);

        columna5.getFont().setStyle(Font.BOLD);
        columna5.getFont().setSize(9);

        Font fuente = new Font();
        fuente.setSize(9);

        table.addCell(columna1);
        table.addCell(columna2);
        table.addCell(columna3);
        table.addCell(columna4);
        table.addCell(columna5);

        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);

        for (int counter = 0; counter < articulos.length; counter++) {

            Paragraph col0 = new Paragraph(String.valueOf(counter + 1), fuente);
            table.addCell(col0);
            Paragraph col1 = new Paragraph(articulos[counter][1] + "", fuente);
            table.addCell(col1);
            Paragraph col2 = new Paragraph(articulos[counter][3] + "", fuente);
            table.addCell(col2);
            Paragraph col3 = new Paragraph(articulos[counter][4] + "", fuente);
            table.addCell(col3);
            Paragraph col4 = new Paragraph(articulos[counter][5] + "", fuente);
            table.addCell(col4);
        }

        Paragraph colum1 = new Paragraph("Subtotal", fuente);
        Paragraph colum2 = new Paragraph("Impuestos", fuente);
        Paragraph colum3 = new Paragraph("Descuento", fuente);
        Paragraph colum4 = new Paragraph("Total", fuente);

        Paragraph colum1_2 = new Paragraph(simp + "", fuente);
        Paragraph colum2_2 = new Paragraph(impuestos + "", fuente);
        Paragraph colum3_2 = new Paragraph(descuento + "", fuente);
        Paragraph colum4_2 = new Paragraph(total + "", fuente);

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(colum1);
        table.addCell(colum1_2);

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(colum2);
        table.addCell(colum2_2);

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(colum3);
        table.addCell(colum3_2);

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(colum4);
        table.addCell(colum4_2);

        return table;
    }

    private PdfPTable crearTablaArticulos2(Object[][] articulos, String simp, String impuestos, String descuento, String total, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(5);
        Font fuenteTabla = new Font();
        fuenteTabla.setSize(9);

        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        float[] medidaCeldas = {7f, 80.80f, 20.60f, 30.60f, 25.60f};

        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        Paragraph columna1 = new Paragraph("#");
        Paragraph columna2 = new Paragraph("Producto/Servicio");
        Paragraph columna3 = new Paragraph("Cantidad");
        Paragraph columna4 = new Paragraph("Precio Unitario");
        Paragraph columna5 = new Paragraph("Importe");

        columna1.getFont().setStyle(Font.BOLD);
        columna1.getFont().setSize(9);

        columna2.getFont().setStyle(Font.BOLD);
        columna2.getFont().setSize(9);

        columna3.getFont().setStyle(Font.BOLD);
        columna3.getFont().setSize(9);

        columna4.getFont().setStyle(Font.BOLD);
        columna4.getFont().setSize(9);

        columna5.getFont().setStyle(Font.BOLD);
        columna5.getFont().setSize(9);

        Font fuente = new Font();
        fuente.setSize(9);

        table.addCell(columna1);
        table.addCell(columna2);
        table.addCell(columna3);
        table.addCell(columna4);
        table.addCell(columna5);

        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);

        for (int counter = 0; counter < articulos.length; counter++) {
            Paragraph col0 = new Paragraph(String.valueOf(counter + 1), fuente);
            table.addCell(col0);
            Paragraph col1 = new Paragraph(articulos[counter][1] + "", fuente);
            table.addCell(col1);
            Paragraph col2 = new Paragraph(articulos[counter][2] + "", fuente);
            table.addCell(col2);
            Paragraph col3 = new Paragraph(articulos[counter][3] + "", fuente);
            table.addCell(col3);
            Paragraph col4 = new Paragraph(articulos[counter][4] + "", fuente);
            table.addCell(col4);
        }

        Paragraph colum1 = new Paragraph("Subtotal", fuente);
        Paragraph colum2 = new Paragraph("Impuestos", fuente);
        Paragraph colum3 = new Paragraph("Descuento", fuente);
        Paragraph colum4 = new Paragraph("Total", fuente);

        Paragraph colum1_2 = new Paragraph(simp + "", fuente);
        Paragraph colum2_2 = new Paragraph(impuestos + "", fuente);
        Paragraph colum3_2 = new Paragraph(descuento + "", fuente);
        Paragraph colum4_2 = new Paragraph(total + "", fuente);

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(colum1);
        table.addCell(colum1_2);

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(colum2);
        table.addCell(colum2_2);

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(colum3);
        table.addCell(colum3_2);

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(colum4);
        table.addCell(colum4_2);

        return table;
    }

    private PdfPTable crearTablaReporte(Object[][] articulos, String simp, String impuestos, String total, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(6);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        float[] medidaCeldas = {7f, 60.80f, 40.60f, 30.60f, 25.60f, 25.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        Paragraph columna1 = new Paragraph("#");
        Paragraph columna2 = new Paragraph("Cliente");
        Paragraph columna3 = new Paragraph("Concepto");
        Paragraph columna4 = new Paragraph("Fecha");
        Paragraph columna5 = new Paragraph("Tipo pago");
        Paragraph columna6 = new Paragraph("Total");

        columna1.getFont().setStyle(Font.BOLD);
        columna1.getFont().setSize(9);

        columna2.getFont().setStyle(Font.BOLD);
        columna2.getFont().setSize(9);

        columna3.getFont().setStyle(Font.BOLD);
        columna3.getFont().setSize(9);

        columna4.getFont().setStyle(Font.BOLD);
        columna4.getFont().setSize(9);

        columna5.getFont().setStyle(Font.BOLD);
        columna5.getFont().setSize(9);

        columna6.getFont().setStyle(Font.BOLD);
        columna6.getFont().setSize(9);

        Font fuente = new Font();
        fuente.setSize(9);

        table.addCell(columna1);
        table.addCell(columna2);
        table.addCell(columna3);
        table.addCell(columna4);
        table.addCell(columna5);
        table.addCell(columna6);

        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);

        for (int counter = 0; counter < articulos.length; counter++) {

            Paragraph col1 = new Paragraph(articulos[counter][0] + "", fuente);
            table.addCell(col1);
            Paragraph col2 = new Paragraph(articulos[counter][1] + "", fuente);
            table.addCell(col2);
            Paragraph col3 = new Paragraph(articulos[counter][2] + "", fuente);
            table.addCell(col3);
            Paragraph col4 = new Paragraph(articulos[counter][4] + "", fuente);
            table.addCell(col4);
            Paragraph col5 = new Paragraph(articulos[counter][6] + "", fuente);
            table.addCell(col5);
            Paragraph col6 = new Paragraph(articulos[counter][3] + "", fuente);
            table.addCell(col6);
        }


        Paragraph colum1 = new Paragraph("Total", fuente);
        Paragraph colum1_2 = new Paragraph(total + "", fuente);

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(colum1);
        table.addCell(colum1_2 + "");



        return table;
    }

    private PdfPTable crearTablaCorte(Object[][] articulos, String simp, String impuestos, String total, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(4);
        float[] medidaCeldas = {7f, 60.80f, 40.60f, 30.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        Paragraph columna1 = new Paragraph("#");
        Paragraph columna2 = new Paragraph("Concepto");
        Paragraph columna3 = new Paragraph("Total");
        Paragraph columna4 = new Paragraph("Tipo");

        columna1.getFont().setStyle(Font.BOLD);
        columna1.getFont().setSize(9);

        columna2.getFont().setStyle(Font.BOLD);
        columna2.getFont().setSize(9);

        columna3.getFont().setStyle(Font.BOLD);
        columna3.getFont().setSize(9);

        columna4.getFont().setStyle(Font.BOLD);
        columna4.getFont().setSize(9);

        Font fuente = new Font();
        fuente.setSize(9);

        table.addCell(columna1);
        table.addCell(columna2);
        table.addCell(columna3);
        table.addCell(columna4);

        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);

        for (int counter = 0; counter < articulos.length; counter++) {

            Paragraph col1 = new Paragraph(articulos[counter][0] + "", fuente);
            table.addCell(col1);
            Paragraph col2 = new Paragraph(articulos[counter][1] + "", fuente);
            table.addCell(col2);
            Paragraph col3 = new Paragraph(articulos[counter][2] + "", fuente);
            table.addCell(col3);
            Paragraph col4 = new Paragraph(articulos[counter][3] + "", fuente);
            table.addCell(col4);

        }
        Paragraph colum1 = new Paragraph("Total", fuente);
        Paragraph colum1_2 = new Paragraph(total + "", fuente);

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");

        table.addCell("");
        table.addCell(colum1);
        table.addCell(colum1_2);
        table.addCell("");
        return table;
    }

    private PdfPTable crearTablaCorte2(Object[][] articulos, String simp, String impuestos, String total, float width,
            String eC, String tC, String cC, String sE, String sT, String sC,
            String sacadoCaja, String sacadoTarjeta, String sacadoCheque, String totCaja, String totDif) throws DocumentException {

        PdfPTable table = new PdfPTable(4);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        float[] medidaCeldas = {30f, 60.80f, 40.60f, 30.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        Paragraph columna1 = new Paragraph("Concepto");
        Paragraph columna2 = new Paragraph("Total Caja");
        Paragraph columna3 = new Paragraph("Total Contado");
        Paragraph columna4 = new Paragraph("Diferencia");

        columna1.getFont().setStyle(Font.BOLD);
        columna1.getFont().setSize(9);

        columna2.getFont().setStyle(Font.BOLD);
        columna2.getFont().setSize(9);

        columna3.getFont().setStyle(Font.BOLD);
        columna3.getFont().setSize(9);

        columna4.getFont().setStyle(Font.BOLD);
        columna4.getFont().setSize(9);

        Font fuente = new Font();
        fuente.setSize(9);

        table.addCell(columna1);
        table.addCell(columna2);
        table.addCell(columna3);
        table.addCell(columna4);

        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);

        Paragraph colum1 = new Paragraph("Efectivo", fuente);
        Paragraph colum2 = new Paragraph("Tarjeta", fuente);
        Paragraph colum3 = new Paragraph("Cheque", fuente);
        Paragraph colum4 = new Paragraph("Total", fuente);

        Paragraph colum1_2 = new Paragraph(eC + "", fuente);
        Paragraph colum1_3 = new Paragraph(sE + "", fuente);
        Paragraph colum1_4 = new Paragraph(sacadoCaja + "", fuente);


        table.addCell(colum1);
        table.addCell(colum1_2);
        table.addCell(colum1_3);
        table.addCell(colum1_4);

        Paragraph colum2_2 = new Paragraph(tC + "", fuente);
        Paragraph colum2_3 = new Paragraph(sT + "", fuente);
        Paragraph colum2_4 = new Paragraph(sacadoTarjeta + "", fuente);

        table.addCell(colum2);
        table.addCell(colum2_2);
        table.addCell(colum2_3);
        table.addCell(colum2_4);

        Paragraph colum3_2 = new Paragraph(cC + "", fuente);
        Paragraph colum3_3 = new Paragraph(sC + "", fuente);
        Paragraph colum3_4 = new Paragraph(sacadoCheque + "", fuente);

        table.addCell(colum3);
        table.addCell(colum3_2);
        table.addCell(colum3_3);
        table.addCell(colum3_4);

        Paragraph colum4_2 = new Paragraph(total + "", fuente);
        Paragraph colum4_3 = new Paragraph(totCaja + "", fuente);
        Paragraph colum4_4 = new Paragraph(totDif + "", fuente);

        table.addCell(colum4);
        table.addCell(colum4_3);
        table.addCell(colum4_2);
        table.addCell(colum4_4);

        return table;
    }

    private PdfPTable crearTablaCorte3(Object[][] articulos, String gastos, String impuestos, String total, float width,
            String eC, String tC, String cC, String sE, String sT, String sC) throws DocumentException {

        PdfPTable table = new PdfPTable(4);
        float[] medidaCeldas = {7f, 60.80f, 40.60f, 30.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        Paragraph columna0 = new Paragraph("#");
        Paragraph columna1 = new Paragraph("Concepto");
        Paragraph columna2 = new Paragraph("Cantidad");
        Paragraph columna3 = new Paragraph("Tipo");

        columna0.getFont().setStyle(Font.BOLD);
        columna0.getFont().setSize(9);

        columna1.getFont().setStyle(Font.BOLD);
        columna1.getFont().setSize(9);

        columna2.getFont().setStyle(Font.BOLD);
        columna2.getFont().setSize(9);

        columna3.getFont().setStyle(Font.BOLD);
        columna3.getFont().setSize(9);


        Font fuente = new Font();
        fuente.setSize(9);

        table.addCell(columna0);
        table.addCell(columna1);
        table.addCell(columna2);
        table.addCell(columna3);
        
        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);


        for (int counter = 0; counter < articulos.length; counter++) {
            Paragraph col1 = new Paragraph(String.valueOf(counter + 1), fuente);
            table.addCell(col1);
            Paragraph col2 = new Paragraph(articulos[counter][0] + "", fuente);
            table.addCell(col2);
            Paragraph col3 = new Paragraph(articulos[counter][1] + "", fuente);
            table.addCell(col3);
            Paragraph col4 = new Paragraph(articulos[counter][2] + "", fuente);
            table.addCell(col4);

        }
        
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        
        Paragraph tot = new Paragraph("Total", fuente);
        Paragraph tot2 = new Paragraph(gastos+"",fuente);
        
        table.addCell("");
        table.addCell(tot);
        table.addCell(tot2);
        table.addCell("");
        

        return table;
    }

    private PdfPTable crearTablaCorte4(Object[][] articulos,String total, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(3);

        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        float[] medidaCeldas = {7f, 60.80f, 40.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        Paragraph columna1 = new Paragraph("#");
        Paragraph columna2 = new Paragraph("Tipo");
        Paragraph columna3 = new Paragraph("Total");

        columna1.getFont().setStyle(Font.BOLD);
        columna1.getFont().setSize(9);

        columna2.getFont().setStyle(Font.BOLD);
        columna2.getFont().setSize(9);

        columna3.getFont().setStyle(Font.BOLD);
        columna3.getFont().setSize(9);


        Font fuente = new Font();
        fuente.setSize(9);

        table.addCell(columna1);
        table.addCell(columna2);
        table.addCell(columna3);


        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);


        for (int counter = 0; counter < articulos.length; counter++) {
            Paragraph col1 = new Paragraph(String.valueOf(counter + 1), fuente);
            table.addCell(col1);
            Paragraph col2 = new Paragraph(articulos[counter][0] + "", fuente);
            table.addCell(col2);
            Paragraph col3 = new Paragraph(articulos[counter][1] + "", fuente);
            table.addCell(col3);
        }

        table.addCell("");
        table.addCell("");
        table.addCell("");
        
        Paragraph tot = new Paragraph("Total", fuente);
        Paragraph tot2 = new Paragraph(total+"",fuente);
        
        table.addCell("");
        table.addCell(tot);
        table.addCell(tot2);
        
        return table;
    }
    
    private PdfPTable crearTablaTransferencias(Object[][] articulos, String total, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(4);
        float[] medidaCeldas = {7f, 60.80f, 40.60f, 30.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        Paragraph columna1 = new Paragraph("#");
        Paragraph columna2 = new Paragraph("Concepto");
        Paragraph columna3 = new Paragraph("Total");
        Paragraph columna4 = new Paragraph("Tipo");

        columna1.getFont().setStyle(Font.BOLD);
        columna1.getFont().setSize(9);

        columna2.getFont().setStyle(Font.BOLD);
        columna2.getFont().setSize(9);

        columna3.getFont().setStyle(Font.BOLD);
        columna3.getFont().setSize(9);

        columna4.getFont().setStyle(Font.BOLD);
        columna4.getFont().setSize(9);

        Font fuente = new Font();
        fuente.setSize(9);

        table.addCell(columna1);
        table.addCell(columna2);
        table.addCell(columna3);
        table.addCell(columna4);

        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);

        for (int counter = 0; counter < articulos.length; counter++) {

            Paragraph col1 = new Paragraph(articulos[counter][0] + "", fuente);
            table.addCell(col1);
            Paragraph col2 = new Paragraph(articulos[counter][1] + "", fuente);
            table.addCell(col2);
            Paragraph col3 = new Paragraph(articulos[counter][2] + "", fuente);
            table.addCell(col3);
            Paragraph col4 = new Paragraph(articulos[counter][3] + "", fuente);
            table.addCell(col4);

        }
        Paragraph colum1 = new Paragraph("Total", fuente);
        Paragraph colum1_2 = new Paragraph(total + "", fuente);

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");


        table.addCell("");
        table.addCell(colum1);
        table.addCell(colum1_2);
        table.addCell("");
        return table;
    }
    
}
