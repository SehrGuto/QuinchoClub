package com.quinchoClub.controladores;
import com.quinchoClub.entidades.Propiedad;
import com.quinchoClub.entidades.ResenaPropiedad;
import com.quinchoClub.entidades.ResenaUsuario;
import com.quinchoClub.entidades.Reserva;
import com.quinchoClub.entidades.Usuario;
import com.quinchoClub.servicios.PropiedadServicio;
import com.quinchoClub.servicios.ResenaServicio;
import com.quinchoClub.servicios.ResenaUsuarioServicio;
import com.quinchoClub.servicios.UsuarioServicio;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/resena")
public class ResenaControlador {

    @Autowired
    private ResenaServicio resenaServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private PropiedadServicio propiedadServicio;
    @Autowired
    private ResenaUsuarioServicio resenaUsuarioServicio;
    
    @GetMapping("/cargarResenaPropiedad/{id}")
    public String cargarResenaPropiedad(@PathVariable String id,HttpSession session, ModelMap modelo){
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Propiedad propiedad = propiedadServicio.obtenerPropiedadPorId(id);
        if (usuario != null) {
            modelo.put("usuario", usuarioServicio.getOne(usuario.getId()));
        }
        modelo.put("propiedad", propiedad);
        return "resenaPropiedad.html";
    }
    @PostMapping("/cargarResenaPropiedad/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    public String cargarResenaPropiedad(@PathVariable String id, ModelMap modelo,@RequestParam String idUsuario,@RequestParam String comentario,@RequestParam Integer calificacion, RedirectAttributes redirectAttributes) {
        try{
            Propiedad propiedad = propiedadServicio.obtenerPropiedadPorId(id);
            Usuario usuario = usuarioServicio.getOne(idUsuario);
            resenaServicio.crearResenaPropiedad(usuario, comentario, calificacion, propiedad);
            redirectAttributes.addFlashAttribute("mensaje", "Registo completado con Exito");
            return "redirect:/";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "algo salio mal, intenta nuevamente");
            return "redirect:/resena/cargarResenapropiedad/" + id;
        }            
    }
    @GetMapping("/cargarResenaUsuario/{id}")
    public String cargarResenaUsuario(){
        // Lógica para cargar información y mostrar formulario de reseña de usuario
        return "resenasUsuario.html";
    }
    @PostMapping("/cargarResenaUsuario/{id}")
    @PreAuthorize("hasRole('ROLE_PROPIETARIO')")
    public String cargarResenaUsuario(
            @PathVariable String id,
            @RequestParam String autor,
            @RequestParam String comentario,
            @RequestParam Integer calificacion,
            RedirectAttributes redirectAttributes) {
      try {
            // Lógica para crear la reseña de usuario
            Usuario usuario = usuarioServicio.getOne(id);
            resenaUsuarioServicio.crearResenaUsuario(usuario, comentario, calificacion, autor);
            
            redirectAttributes.addFlashAttribute("mensaje", "Reseña de usuario registrada con éxito");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Algo salió mal, intenta nuevamente");
            return "redirect:/resena/cargarResenaUsuario/" + id;
        }
    }
    
}
