/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Metodos;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

/**
 *
 * @author German Valdez
 */

public class Ticket2{

 	public Ticket2(String texto) {
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		DocPrintJob docPrintJob = printService.createPrintJob();
		Doc doc = new SimpleDoc(texto.getBytes(), flavor, null);

		try {

                    docPrintJob.print(doc, null);

		} catch (PrintException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

}