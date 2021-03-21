/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.konecta.pdf.converter;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import py.com.konecta.pdf.converter.dto.ImagesDTO;

/**
 *
 * @author Juan Vargas <juan.vargas@konecta.com.py at Konecta S.A.>
 */
@Stateless
public class UtilBean {

    public static void mergePdfFiles(List<InputStream> inputPdfList,
            OutputStream outputStream) throws Exception {
        //Create document and pdfReader objects.
        Document document = new Document();
        List<PdfReader> readers
                = new ArrayList<>();
        int totalPages = 0;

        //Create pdf Iterator object using inputPdfList.
        Iterator<InputStream> pdfIterator
                = inputPdfList.iterator();

        // Create reader list for the input pdf files.
        while (pdfIterator.hasNext()) {
            InputStream pdf = pdfIterator.next();
            PdfReader pdfReader = new PdfReader(pdf);
            readers.add(pdfReader);
            totalPages = totalPages + pdfReader.getNumberOfPages();
        }

        // Create writer for the outputStream
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        //Open document.
        document.open();

        //Contain the pdf data.
        PdfContentByte pageContentByte = writer.getDirectContent();

        PdfImportedPage pdfImportedPage;
        int currentPdfReaderPage = 1;
        Iterator<PdfReader> iteratorPDFReader = readers.iterator();

        // Iterate and process the reader list.
        while (iteratorPDFReader.hasNext()) {
            PdfReader pdfReader = iteratorPDFReader.next();
            //Create page and add content.
            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                document.newPage();
                pdfImportedPage = writer.getImportedPage(
                        pdfReader, currentPdfReaderPage);
                pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                currentPdfReaderPage++;
            }
            currentPdfReaderPage = 1;
        }

        //Close document and outputStream.
        outputStream.flush();
        document.close();
        outputStream.close();

    }

    public static void imageToPdf(byte[] imageFile, OutputStream outputStream) throws IOException {
        try {
            Image image;
            try {
                image = Image.getInstance(imageFile);
            } catch (BadElementException bee) {
                throw new IOException(bee);
            }

            Rectangle PageA4 = PageSize.A4;

            Float scalePortrait = Math.min(PageA4.getWidth() / image.getWidth(),
                    PageA4.getHeight() / image.getHeight());

            Float scaleLandscape = Math.min(PageA4.getHeight() / image.getWidth(),
                    PageA4.getWidth() / image.getHeight());

            Boolean isLandscape = scaleLandscape > scalePortrait;

            Float width;
            Float height;
            if (isLandscape) {
                PageA4 = PageA4.rotate();
                width = image.getWidth() * scaleLandscape;
                height = image.getHeight() * scaleLandscape;
            } else {
                width = image.getWidth() * scalePortrait;
                height = image.getHeight() * scalePortrait;
            }

            Document document = new Document(PageA4, 10, 10, 10, 10);

            try {
                PdfWriter.getInstance(document, outputStream);
            } catch (DocumentException e) {
                throw new IOException(e);
            }
            document.open();
            try {
                image.scaleAbsolute(width, height);
                Float posH = ((PageA4.getHeight() - height) / 2);
                Float posW = ((PageA4.getWidth() - width) / 2);

                image.setAbsolutePosition(posW, posH);
                image.setBorder(Image.NO_BORDER);
                image.setBorderWidth(0);

                try {
                    document.newPage();
                    document.add(image);
                } catch (DocumentException de) {
                    throw new IOException(de);
                }
            } finally {
                document.close();
            }
        } finally {
            outputStream.close();
        }
    }

    public static String limpiarInverso(String mensaje) {
        return mensaje.replaceAll("&ntilde;", "ñ")
                .replaceAll("&Ntilde;", "Ñ")
                .replaceAll("&aacute;", "á")
                .replaceAll("&Aacute;", "Á")
                .replaceAll("&eacute;", "é")
                .replaceAll("&Eacute;", "É")
                .replaceAll("&iacute;", "í")
                .replaceAll("&Iacute;", "Í")
                .replaceAll("&oacute;", "ó")
                .replaceAll("&Oacute;", "Ó")
                .replaceAll("&uacute;", "ú")
                .replaceAll("&Uacute;", "Ú")
                .replaceAll("&degree;", "°")
                .replaceAll("&ordm;", "º");
    }

    public static void limpiarRequest(List<ImagesDTO> request) {
        for (ImagesDTO imagesDTO : request) {
            String[] split = imagesDTO.getBase64().split("base64,", 0);
            int longitud = 0, i = -1;
            for (String string : split) {
                if (string.length() >= longitud) {
                    longitud = string.length();
                    i++;
                }
            }
            String documento64 = split[i];
            imagesDTO.setBase64(documento64);
        }
    }
}
