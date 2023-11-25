package com.quinchoClub.entidades;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class Resena {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String autor;
    private String comentario;
    private Integer calificacion;
    private LocalDate fechaComentario;
<<<<<<< HEAD
    
    
=======
>>>>>>> 4c2ad9820b012d0a6c7a4cd41dd55ac5b94bbb12
}