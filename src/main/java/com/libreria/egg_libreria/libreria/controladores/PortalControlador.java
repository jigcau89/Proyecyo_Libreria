package com.libreria.egg_libreria.libreria.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author jigcau89 - Joaquin Ignacio Gonzalez
 * @siteweb www.jigcomp.com.ar
 */
@Controller
@RequestMapping("/")
public class PortalControlador {
    
 @GetMapping("")
    public String index(ModelMap model) {
        model.addAttribute("nombreReferencia", "index");
        return "index";
    }

//  @PostMapping("/registrarAutor")
//    public String registarAutor(ModelMap modelo, @RequestParam(required = false) String nombre) {
//        System.out.println("Nombre Autor: " + nombre);
//    
//        return "formularioAutor";
//    }
}
