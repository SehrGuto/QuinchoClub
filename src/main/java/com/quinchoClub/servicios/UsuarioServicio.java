/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quinchoClub.servicios;

import com.quinchoClub.entidades.Propiedad;
import com.quinchoClub.entidades.Usuario;
import com.quinchoClub.enumeraciones.Rol;
import com.quinchoClub.excepciones.MiException;
import com.quinchoClub.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author lauta
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio ur;

    @Transactional
    public void crearUsuario(String nombre, String apellido, String email, String password, String password2, Integer dni, Date FechaDeNacimiento, Integer telefono, boolean propietario) throws MiException {
        validar(nombre, apellido, email, password, password2, dni, FechaDeNacimiento, telefono);
        if (ur.buscarporEmail(email) != null) {
            throw new MiException("Ese email ya se encuentra registrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setDni(dni);
        usuario.setFechaDeNacimiento(FechaDeNacimiento);
        usuario.setTelefono(telefono);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        //se agrega validacion de rol para inicializar lista de propiedades.
        if (propietario == true) {
            usuario.setRol(Rol.PROPIETARIO);
            usuario.setPropiedades(new ArrayList());
        } else {
            usuario.setRol(Rol.CLIENTE);
        }
        ur.save(usuario);
    }

    //esta funcion de actualizar solo debe ser accesible por el Administrador, puede cambiar todos los datos de un usuario EXCEPTO su contraseña.
    @Transactional
    public void actualizar(String id, String nombre, String apellido, String email, String rol, Integer dni, Date FechaDeNacimiento, Integer telefono) {
        try {
            Optional<Usuario> respuesta = ur.findById(id);
            System.out.println(respuesta);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setEmail(email);
                if (rol.equals("ADMIN")) {
                    usuario.setRol(Rol.ADMIN);
                } else if (rol.equals("PROPIETARIO")) {
                    usuario.setRol(Rol.PROPIETARIO);
                } else {
                    usuario.setRol(Rol.CLIENTE);
                }
                usuario.setDni(dni);
                usuario.setFechaDeNacimiento(FechaDeNacimiento);
                usuario.setRol(usuario.getRol());
                usuario.setTelefono(telefono);
                ur.save(usuario);
            } else {
                throw new IllegalArgumentException("El usuario con el ID proporcionado no existe");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Error al actualizar usuario: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error general al actualizar usuario: " + ex.getMessage());
        }
    }

//    public void actualizar(String id, String nombre, String apellido, String email, String password, String password2, Integer dni, Date FechaDeNacimiento, Integer telefono) throws Exception {
//        Optional<Usuario> respuesta = ur.findById(id);
//        System.out.println(respuesta);
//        if (respuesta !=null) {
//            
//            System.out.println("entre");
//            Usuario usuario = respuesta.get();
//            usuario.setNombre(nombre);
//            usuario.setApellido(apellido);
//            usuario.setEmail(email);
//            usuario.setPassword(password);
////            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
//            usuario.setDni(dni);
//            usuario.setFechaDeNacimiento(FechaDeNacimiento);
//            usuario.setRol(usuario.getRol());
//            usuario.setTelefono(telefono);
//            ur.save(usuario);
//            System.out.println("sali");
//        }
//    }
    public List<Usuario> listarUsuarios() {
        return ur.findAll();
    }

    private void validar(String nombre, String apellido, String email, String password, String password2, Integer dni, Date FechaDeNacimiento, Integer telefono) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new MiException("El apellido no puede ser nulo");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede ser nulo");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener mas de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
        if (dni == null || dni.equals("")) {
            throw new MiException("El dni no puede ser nulo");
        }
        if (FechaDeNacimiento == null || FechaDeNacimiento.equals("")) {
            throw new MiException("La fecha de nacimiento no puede ser nulo");
        }

        if (telefono == null) {
            throw new MiException("El telefono no puede ser nulo");
        }
    }

    public void borrarUsuario(String id) throws MiException {
        if (id.isEmpty() || id.equals("")) {
            throw new MiException("El id proporcionado es nulo");
        } else {
            Optional<Usuario> respuesta = ur.findById(id);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                ur.delete(usuario);
            }
        }
    }

    public Usuario getOne(String id) {
        return ur.getOne(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = ur.buscarporEmail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            throw new UsernameNotFoundException("Usuario Invalido");
        }
    }
    // reveer utilidad de funcion

    public void guardarUsuarioCompleto(Usuario usuario) {
        ur.save(usuario);
    }

    public Usuario buscarPorPropiedad(String id) {
        return ur.buscarPorPropiedad(id);
    }

    @Transactional
    public boolean cambiarContrasena(String id, String contrasenaActual, String nuevaContrasena, String confirmarNuevaContrasena) {
        // Verificar que la nueva contraseña y la confirmación coincidan
        if (!nuevaContrasena.equals(confirmarNuevaContrasena)) {
            return false; // La nueva contraseña y la confirmación no coinciden
        }

        // Obtener el usuario desde la base de datos
        Optional<Usuario> optionalUsuario = ur.findById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            // Verificar si la contraseña actual proporcionada coincide con la almacenada
            if (new BCryptPasswordEncoder().matches(contrasenaActual, usuario.getPassword())) {
                // Actualizar la contraseña en la base de datos
                usuario.setPassword(new BCryptPasswordEncoder().encode(nuevaContrasena));
                ur.save(usuario);
                return true; // Cambio exitoso
            } else {
                return false; // La contraseña actual no es correcta
            }
        } else {
            return false; // El usuario no se encontró
        }
    }
    
     @Transactional
    //servicio para actualizar el perfil de usuario, opcion disponible para el usuario, no para el administrador
    public void actualizarPerfil(
            String id,
            String nombre,
            String apellido,
            String email,
            Integer dni,
            Date fechaDeNacimiento,
            Integer telefono)  {
    
    
        Usuario usuario = ur.findById(id).orElse(null);
        
        // Actualizando info del perfil
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setDni(dni);
        usuario.setFechaDeNacimiento(fechaDeNacimiento);
        usuario.setTelefono(telefono);
        
        // guardando la info en la base de datos
        ur.save(usuario);
        
    }
    
    
}
