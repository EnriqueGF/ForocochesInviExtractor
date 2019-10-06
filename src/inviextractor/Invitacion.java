package inviextractor;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Enrique
 */
public class Invitacion implements Serializable {

    private String texto;
    private Date fechaObtencion;

    public Invitacion(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFechaObtencion() {
        return fechaObtencion;
    }

    public void setFechaObtencion(Date fechaObtencion) {
        this.fechaObtencion = fechaObtencion;
    }

}
