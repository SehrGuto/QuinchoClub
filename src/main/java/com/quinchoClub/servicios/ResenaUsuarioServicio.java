/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quinchoClub.servicios;

import com.quinchoClub.entidades.ResenaUsuario;
import com.quinchoClub.entidades.Usuario;
import com.quinchoClub.repositorios.ResenaRepositorio;
import com.quinchoClub.repositorios.ResenaUsuarioRepositorio;
import com.quinchoClub.repositorios.UsuarioRepositorio;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author pavan
 */
@Service
public class ResenaUsuarioServicio {
    
    
    
     @Autowired
    private ResenaUsuarioRepositorio rup;
     
    @Autowired
    private UsuarioRepositorio ur;

    //CRUD DE RESENAS DE USUARIOS.
    
    public void crearResenaUsuario(Usuario usuario, String comentario, Integer calificacion, String nombre) {
        ResenaUsuario resenaUsuario = new ResenaUsuario();
        resenaUsuario.setUsuario(usuario);
        resenaUsuario.setComentario(comentario);
        resenaUsuario.setCalificacion(calificacion);
        resenaUsuario.setFechaComentario(LocalDate.now());
        resenaUsuario.setNombre(nombre);
        
        //guardar la reseña de usuario en el repositorio
        rup.save(resenaUsuario);
        
        // Actualizar la lista de reseñas del usuario
        if (usuario.getResenas() != null) {
         usuario.getResenas().add(resenaUsuario);
        // No es necesario llamar a us.actualizarPerfil() aquí

         // Actualizar el usuario en la base de datos para guardar la nueva reseña
         ur.save(usuario);
}
        
    }
}
