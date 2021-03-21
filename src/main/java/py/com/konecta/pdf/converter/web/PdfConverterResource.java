/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.konecta.pdf.converter.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import py.com.konecta.pdf.converter.Constantes;
import py.com.konecta.pdf.converter.PdfConverterBean;
import py.com.konecta.pdf.converter.UtilBean;
import py.com.konecta.pdf.converter.dto.ImagesDTO;

/**
 *
 * @author Juan Vargas <juan.vargas@konecta.com.py at Konecta S.A.>
 */
@Path("/convert")
public class PdfConverterResource {

    private static final Logger LOGGER = LogManager.getLogger(PdfConverterResource.class);

    @EJB
    private PdfConverterBean bean;

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response convertPdf(List<ImagesDTO> request, @HeaderParam("Authorization") String auth) throws IOException {
        LOGGER.info("IN: [{}]", request.toString());
        if (auth == null || !auth.equals(Constantes.HEADER)) {
            LOGGER.info("[return]");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        UtilBean.limpiarRequest(request);
        List<InputStream> list = new ArrayList<>();
        for (ImagesDTO imagesDTO : request) {
            byte[] bytes = Base64.getDecoder().decode(imagesDTO.getBase64());
            InputStream is = new ByteArrayInputStream(bytes);
            list.add(is);
        }

        byte[] pdf = bean.convertToPdf(list);
        if (pdf == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        String name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return Response.ok().header("content-disposition", "inline;filename=file-".concat(name).concat(".pdf"))
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .entity(pdf).build();
    }
}
