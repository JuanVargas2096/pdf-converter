/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.konecta.pdf.converter.dto;

/**
 *
 * @author Juan Vargas <juan.vargas@konecta.com.py at Konecta S.A.>
 */
public class ImagesDTO {

    private String nombre;
    private String base64;

    public ImagesDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    @Override
    public String toString() {
        return "ImagesDTO{" + "nombre=" + nombre + '}';
    }

}
