package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ConnectionController {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @GetMapping("/inscription")
    public String showForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        return "inscription";
    }

    @PostMapping("/inscription")
    public String submitForm(@ModelAttribute Etudiant etudiant,Model model) {

        if (etudiantRepository.existsByEmailEtudiant(etudiant.getEmailEtudiant())) {
            model.addAttribute("erreur", "Cette adresse e-mail est déjà utilisée.");
            return "inscription";
        }

        etudiantRepository.save(etudiant);
        return "confirmationInscription"; // ou vers une page de confirmation
    }
}
