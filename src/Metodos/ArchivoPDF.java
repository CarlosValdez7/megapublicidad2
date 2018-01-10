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

            FileOutputStream ficheroPdf = new FileOutputStream("W:\\megapublicidad2.0\\documentos\\"+nombreArchivo + ".pdf");
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);

            documento.open();
            img.setAbsolutePosition(380f, 695f);

            documento.add(img);
            documento.add(new Paragraph("MEGAPUBLICIDAD"));
            documento.add(new Paragraph("DUCJ681122GM7"));
            documento.add(new Paragraph("Av. Juan Escutia 134-A"));
            documento.add(new Paragraph("Col. Centro C.P 63000"));
            documento.add(new Paragraph("Tepic Nayarit, Mexico\n"));
            documento.add(new Paragraph("Telefono 218-0320 Celular: 311-269-3605 Email: meganayarit@gmail.com\n"));
            documento.add(new Paragraph("Cuenta Banamex: 0199096973\n"));

            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------"));
            documento.add(new Paragraph("Folio " + folio + "                                                  "+tipo+"                                              Fecha " + fecha));
            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------"));
            documento.add(new Paragraph("" + nombre + "   " + repre + "   " + RFC));
            documento.add(new Paragraph("" + dire + "   " + email));
            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------"));

            documento.add(new Paragraph("\n" + comentario));
            documento.add(new Paragraph("\n"));

            PdfPTable tabla = crearTablaArticulos(articulos, subtotal, iva, descuento, total, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla);

            documento.add(new Paragraph("\n\n\n"
                    + "                                                               __________________________________________\n"
                    + "                                                                                           " + usuario));

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

            FileOutputStream ficheroPdf = new FileOutputStream("W:\\megapublicidad2.0\\documentos\\"+nombreArchivo + ".pdf");
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);

            documento.open();
            img.setAbsolutePosition(380f, 695f);

            documento.add(img);
            documento.add(new Paragraph("MEGAPUBLICIDAD"));
            documento.add(new Paragraph("DUCJ681122GM7"));
            documento.add(new Paragraph("Av. Juan Escutia 134-A"));
            documento.add(new Paragraph("Col. Centro C.P 63000"));
            documento.add(new Paragraph("Tepic Nayarit, Mexico\n"));
            documento.add(new Paragraph("Telefono 218-0320 Celular: 311-269-3605 Email: meganayarit@gmail.com\n"));
            documento.add(new Paragraph("Cuenta Banamex: 0199096973\n"));

            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------"));
            documento.add(new Paragraph("Folio " + folio + "                                                  "+tipo+"                                              Fecha " + fecha));
            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------"));
            documento.add(new Paragraph("" + nombre + "   " + repre + "   " + RFC));
            documento.add(new Paragraph("" + dire + "   " + email));
            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------"));

            documento.add(new Paragraph("\n" + comentario));
            documento.add(new Paragraph("\n"));

            PdfPTable tabla = crearTablaArticulos2(articulos, subtotal, iva, descuento, total, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla);

            documento.add(new Paragraph("\n\n\n"
                    + "                                                               __________________________________________\n"
                    + "                                                                                           " + usuario));

            documento.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error en pdfVenta" + ex);
        }

    }

    
    
    public void pdfReporteCreditos(String nombreArchivo, Object[][] articulos, String fecha, String fecha2, String total, String deuda, String comen)
            throws DocumentException, IOException {
        try {
            Document documento = new Document(PageSize.LETTER);
            Image img = Image.getInstance("mega200px.png");

            FileOutputStream ficheroPdf = new FileOutputStream(nombreArchivo + ".pdf");
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);

            documento.open();
            img.setAbsolutePosition(380f, 695f);

            documento.add(img);

            documento.add(new Paragraph("MEGAPUBLICIDAD"));
            documento.add(new Paragraph("DUCJ681122GM7"));
            documento.add(new Paragraph("Av. Juan Escutia 134-A"));
            documento.add(new Paragraph("Col. Centro C.P 63000"));
            documento.add(new Paragraph("Tepic Nayarit, Mexico\n"));
            documento.add(new Paragraph("Telefono 218-0320 Celular: 311-269-3605 Email: meganayarit@gmail.com\n"));
            documento.add(new Paragraph("Cuenta Banamex: 0199096973"));
            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------"));
            documento.add(new Paragraph("                              " + comen));
            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------\n"));


            PdfPTable tabla = crearTablaReporteCreditos(articulos, "", "", total, deuda, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla);

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

            FileOutputStream ficheroPdf = new FileOutputStream("W:\\megapublicidad2.0\\documentos\\reportes\\"+nombreArchivo + ".pdf");
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);


            documento.open();
            img.setAbsolutePosition(380f, 695f);
            documento.add(img);

            documento.add(new Paragraph("MEGAPUBLICIDAD"));
            documento.add(new Paragraph("DUCJ681122GM7"));
            documento.add(new Paragraph("Av. Juan Escutia 134-A"));
            documento.add(new Paragraph("Col. Centro C.P 63000"));
            documento.add(new Paragraph("Tepic Nayarit, Mexico\n"));
            documento.add(new Paragraph("Telefono 218-0320 Celular: 311-269-3605 Email: meganayarit@gmail.com\n"));
            documento.add(new Paragraph("Cuenta Banamex: 0199096973"));
            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------"));
            documento.add(new Paragraph("              " + comen));
            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------"));


            documento.add(new Paragraph("\n"));

            PdfPTable tabla = crearTablaReporte(articulos, "", "", total, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla);

            documento.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error en pdfVenta" + ex);
        }

    }

    public void pdfCorteCaja(String nombreArchivo, Object[][] articulos, Object[][] articulos2, Object[][] articulos3, String fecha, String total, String usuario,
            String eC, String tC, String cC, String sE, String sT, String sC,
            String sacadoCaja, String sacadoTarjeta, String sacadoCheque, String totCaja, String totDif)
            throws DocumentException, IOException {
        try {
            Document documento = new Document(PageSize.LETTER);
            Image img = Image.getInstance("mega200px.png");

            FileOutputStream ficheroPdf = new FileOutputStream("W:\\megapublicidad2.0\\documentos\\cortes\\"+nombreArchivo + ".pdf");
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);

            documento.open();
            img.setAbsolutePosition(380f, 695f);
            documento.add(img);

            documento.add(new Paragraph("MEGAPUBLICIDAD"));
            documento.add(new Paragraph("DUCJ681122GM7"));
            documento.add(new Paragraph("Av. Juan Escutia 134-A"));
            documento.add(new Paragraph("Col. Centro C.P 63000"));
            documento.add(new Paragraph("Tepic Nayarit, Mexico\n"));
            documento.add(new Paragraph("Telefono 218-0320 Celular: 311-269-3605 Email: meganayarit@gmail.com\n"));
            documento.add(new Paragraph("Cuenta Banamex: 0199096973"));
            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------"));
            documento.add(new Paragraph("                                                  Corte de caja del dia " + fecha));
            documento.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------------"));

            documento.add(new Paragraph("\n"));

            PdfPTable tabla = crearTablaCorte(articulos, "", "", total, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla);

            documento.add(new Paragraph("\n"));

            PdfPTable tabla3 = crearTablaCorte3(articulos2, "", "", total, documento.getPageSize().getWidth() / 2 - 40, eC, tC, cC, sE, sT, sC);
            documento.add(tabla3);

            documento.add(new Paragraph("\n"));

            PdfPTable tabla2 = crearTablaCorte2(articulos, "", "", total, documento.getPageSize().getWidth() / 2 - 40, eC, tC,
                    cC, sE, sT, sC, sacadoCaja, sacadoTarjeta, sacadoCheque, totCaja, totDif);
            documento.add(tabla2);

            documento.add(new Paragraph("\n"));

            PdfPTable tabla4 = crearTablaCorte4(articulos3, documento.getPageSize().getWidth() / 2 - 40);
            documento.add(tabla4);


            documento.add(new Paragraph("\n\n\n"
                    + "                                                                           __________________________________________\n"
                    + "                                                                                                            " + usuario));


            documento.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error en pdfVenta" + ex);
        }

    }

    private PdfPTable crearTablaArticulos(Object[][] articulos, String simp, String impuestos, String descuento, String total, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(5);
        //  table.setTotalWidth(width);
        //  table.setLockedWidth(true);
        //  table.getDefaultCell().setUseAscender(true);
        //  table.getDefaultCell().setUseDescender(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        float[] medidaCeldas = {7f, 80.80f, 20.60f, 30.60f, 25.60f};

        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.addCell("#");
        table.addCell("Producto/Servicio");
        table.addCell("Cantidad");
        table.addCell("Precio Unitario");
        table.addCell("Importe");
        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);
        for (int counter = 0; counter < articulos.length; counter++) {
            table.addCell(String.valueOf(counter + 1));
            table.addCell(articulos[counter][1].toString());
            table.addCell(articulos[counter][3].toString());
            table.addCell(articulos[counter][4].toString());
            table.addCell(articulos[counter][5].toString());
        }
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
        table.addCell("Subtotal: ");
        table.addCell(simp + "");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("Impuestos: ");
        table.addCell(impuestos + "");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("Descuento: ");
        table.addCell(descuento+"");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("Total: ");
        table.addCell((total) + "");

        return table;
    }
    
    private PdfPTable crearTablaArticulos2(Object[][] articulos, String simp, String impuestos, String descuento, String total, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(5);
        //  table.setTotalWidth(width);
        //  table.setLockedWidth(true);
        //  table.getDefaultCell().setUseAscender(true);
        //  table.getDefaultCell().setUseDescender(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        float[] medidaCeldas = {7f, 80.80f, 20.60f, 30.60f, 25.60f};

        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.addCell("#");
        table.addCell("Producto/Servicio");
        table.addCell("Cantidad");
        table.addCell("Precio Unitario");
        table.addCell("Importe");
        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);
        for (int counter = 0; counter < articulos.length; counter++) {
            table.addCell(String.valueOf(counter + 1));
            table.addCell(articulos[counter][1].toString());
            table.addCell(articulos[counter][2].toString());
            table.addCell(articulos[counter][3].toString());
            table.addCell(articulos[counter][4].toString());
        }
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
        table.addCell("Subtotal: ");
        table.addCell(simp + "");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("Impuestos: ");
        table.addCell(impuestos + "");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("Descuento: ");
        table.addCell(descuento+"");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("Total: ");
        table.addCell((total) + "");

        return table;
    }


    private PdfPTable crearTablaReporte(Object[][] articulos, String simp, String impuestos, String total, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(6);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        float[] medidaCeldas = {7f, 60.80f, 40.60f, 30.60f, 25.60f,25.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.addCell("#");
        table.addCell("Cliente");
        table.addCell("Concepto");
        table.addCell("Fecha");
        table.addCell("Tipo Pago");
        table.addCell("Total");
        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);

        for (int counter = 0; counter < articulos.length; counter++) {
            table.addCell(articulos[counter][0].toString());
            table.addCell(articulos[counter][1].toString());
            table.addCell(articulos[counter][2].toString());
            table.addCell(articulos[counter][4].toString());
            table.addCell(articulos[counter][6].toString());
            table.addCell(articulos[counter][3].toString());
        }
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
        table.addCell("Total: ");
        table.addCell(total + "");



        return table;
    }

    private PdfPTable crearTablaReporteCreditos(Object[][] articulos, String simp, String impuestos, String total, String deuda, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(5);
        //  table.setTotalWidth(width);
        //  table.setLockedWidth(true);
        //  table.getDefaultCell().setUseAscender(true);
        //  table.getDefaultCell().setUseDescender(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        float[] medidaCeldas = {7f, 60.80f, 40.60f, 30.60f, 25.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.addCell("#");
        table.addCell("Cliente");
        table.addCell("Concepto");
        table.addCell("Pagado");
        table.addCell("Total");
        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);

        for (int counter = 0; counter < articulos.length; counter++) {
            table.addCell(articulos[counter][0].toString());
            table.addCell(articulos[counter][1].toString());
            table.addCell(articulos[counter][2].toString());
            table.addCell(articulos[counter][3].toString());
            table.addCell(articulos[counter][4].toString());
        }
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");

        table.addCell("");
        table.addCell("");
        table.addCell("Total: ");
        table.addCell(deuda + "");
        table.addCell(total + "");



        return table;
    }

    private PdfPTable crearTablaCorte(Object[][] articulos, String simp, String impuestos, String total, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(4);
        //  table.setTotalWidth(width);
        //  table.setLockedWidth(true);
        //  table.getDefaultCell().setUseAscender(true);
        //  table.getDefaultCell().setUseDescender(true);
        float[] medidaCeldas = {7f, 60.80f, 40.60f, 30.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.addCell("#");
        table.addCell("Concepto");
        table.addCell("Total");
        table.addCell("Tipo");
        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);

        for (int counter = 0; counter < articulos.length; counter++) {
            table.addCell(articulos[counter][0].toString());
            table.addCell(articulos[counter][1].toString());
            table.addCell(articulos[counter][2].toString());
            table.addCell(articulos[counter][3].toString());
        }
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");

        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");

        table.addCell("");
        table.addCell("Total: ");
        table.addCell(total + "");
        table.addCell("");
        return table;
    }

    private PdfPTable crearTablaCorte2(Object[][] articulos, String simp, String impuestos, String total, float width,
            String eC, String tC, String cC, String sE, String sT, String sC,
            String sacadoCaja, String sacadoTarjeta, String sacadoCheque, String totCaja, String totDif) throws DocumentException {

        PdfPTable table = new PdfPTable(4);
        //table.setTotalWidth(width);
        //table.setLockedWidth(true);
        //table.getDefaultCell().setUseAscender(true);
        //table.getDefaultCell().setUseDescender(true);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        float[] medidaCeldas = {30f, 60.80f, 40.60f, 30.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        table.addCell("Concepto");
        table.addCell("Total Caja");
        table.addCell("Total Contado");
        table.addCell("Retirado");


        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);

        table.addCell("Efectivo");
        table.addCell(eC);
        table.addCell(sE);
        table.addCell(sacadoCaja);

        table.addCell("Tarjeta");
        table.addCell(tC);
        table.addCell(sT);
        table.addCell(sacadoTarjeta);

        table.addCell("Cheque");
        table.addCell(cC);
        table.addCell(sC);
        table.addCell(sacadoCheque);

        table.addCell("Total");
        table.addCell(total);
        table.addCell(totCaja);
        table.addCell(totDif);

        return table;
    }

    private PdfPTable crearTablaCorte3(Object[][] articulos, String simp, String impuestos, String total, float width,
            String eC, String tC, String cC, String sE, String sT, String sC) throws DocumentException {

        PdfPTable table = new PdfPTable(4);
        // table.setTotalWidth(width);
        // table.setLockedWidth(true);
        // table.getDefaultCell().setUseAscender(true);
        // table.getDefaultCell().setUseDescender(true);
        float[] medidaCeldas = {7f, 60.80f, 40.60f, 30.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.addCell("#");
        table.addCell("Concepto");
        table.addCell("Cantidad");
        table.addCell("Tipo");
        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);


        for (int counter = 0; counter < articulos.length; counter++) {
            table.addCell(String.valueOf(counter + 1));
            table.addCell(articulos[counter][0].toString());
            table.addCell(articulos[counter][1].toString());
            table.addCell(articulos[counter][2].toString());
        }


        return table;
    }

    private PdfPTable crearTablaCorte4(Object[][] articulos, float width) throws DocumentException {

        PdfPTable table = new PdfPTable(3);
        //table.setTotalWidth(width);
        //table.setLockedWidth(true);
        //table.getDefaultCell().setUseAscender(true);
        //table.getDefaultCell().setUseDescender(true);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        float[] medidaCeldas = {7f, 60.80f, 40.60f};
        table.setWidths(medidaCeldas);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

        table.addCell("#");
        table.addCell("Tipo");
        table.addCell("Total");

        table.setHeaderRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);


        for (int counter = 0; counter < articulos.length; counter++) {
            table.addCell(String.valueOf(counter + 1));
            table.addCell(articulos[counter][0].toString());
            table.addCell(articulos[counter][1].toString());
        }


        return table;
    }
}
