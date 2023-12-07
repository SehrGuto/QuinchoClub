/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.quinchoClub.repositorios;

/**
 *
 * @author pavan
 */
import com.quinchoClub.entidades.Resena;
import com.quinchoClub.entidades.ResenaUsuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResenaUsuarioRepositorio extends JpaRepository<Resena, String>{
    // Buscar reseñas de usuarios por nombre de usuario
    List<ResenaUsuario> findByUsuario(String usuario);
}
