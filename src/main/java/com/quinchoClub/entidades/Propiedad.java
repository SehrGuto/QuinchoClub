package com.quinchoClub.entidades;

import com.sun.istack.NotNull;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Tincho
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Propiedad {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @NotNull
    private String tipo;
    @NotNull
    private String detalles;
    @NotNull
    private String ubicacion;
    @Positive //siempre valores positivos
    private Double tamanio;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date disponibilidad;
    @NotNull
    private Double precioDia;

    private boolean wifi;
    private boolean pileta;
    private boolean parrilla;
    private boolean accesorios;
    private boolean cama;
    private boolean aire;

    @OneToMany
    private List<Imagen> imagenes;
    @OneToMany
    private List<ResenaPropiedad>resenas;
    
    @OneToMany
    private List<Reserva>reservaPropiedad;
    
 

}
