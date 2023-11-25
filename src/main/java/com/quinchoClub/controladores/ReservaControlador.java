package com.quinchoClub.controladores;

import com.quinchoClub.entidades.Propiedad;
import com.quinchoClub.entidades.Usuario;
import com.quinchoClub.excepciones.MiException;
import com.quinchoClub.servicios.ReservaServicio;
import java.time.LocalDate;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reserva")
public class ReservaControlador {

    @Autowired
    private ReservaServicio rs;

    @GetMapping("/registrarReserva")
    public String registroReserva() {
        return "registerReserva.html";
    }

    @PostMapping("/registrarReserva")
    public String registrarReserva(@RequestParam Long id, @RequestParam Usuario cliente, @RequestParam Propiedad propiedad,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin,
            @RequestParam String precioTotal, ModelMap modelo) {
        try {
            rs.crearReserva(cliente, propiedad, LocalDate.MIN, LocalDate.MIN, precioTotal);
            return "redirect:/";
        } catch (MiException ex) {
            System.out.println(ex.getMessage());
            modelo.put("Error", "Hubo al registrar la reserva");
            return "reserva";
        }

    }

    @PostMapping("/eliminar/")
    public String eliminarReserva(@PathVariable Long id) {
        try {
            rs.borrarReserva(id);
            System.out.println("Reserva eliminada con Exito");
            return "redirect:/usuario/lista";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect://";
        }
    }
}