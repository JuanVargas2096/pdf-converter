/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.konecta.pdf.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Juan Vargas <juan.vargas@konecta.com.py at Konecta S.A.>
 */
@Stateless
public class PdfConverterBean {

    private static final Logger LOGGER = LogManager.getLogger(PdfConverterBean.class);

    public byte[] convertToPdf(List<InputStream> list) {
        try {

            List<InputStream> finalL = new ArrayList<>();
            for (InputStream inputStream : list) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] pdf = IOUtils.toByteArray(inputStream);
                UtilBean.imageToPdf(pdf, baos);
                finalL.add(new ByteArrayInputStream(baos.toByteArray()));
            }
            ByteArrayOutputStream pdf = new ByteArrayOutputStream();
            UtilBean.mergePdfFiles(finalL, pdf);
            return pdf.toByteArray();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return null;
    }
}
