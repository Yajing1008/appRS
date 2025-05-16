package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.EtudiantRepository;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

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
        return "confirmationInscription"; 
    }

    @GetMapping("/connexion")
    public String afficherFormulaireConnexion() {
        return "connexion";
    }

    @PostMapping("/connexion")
    public String connecterEtudiant(@RequestParam String email, 
                                    @RequestParam String motDePasse, 
                                    HttpSession session, 
                                    Model model) {
        Optional<Etudiant> etudiant = etudiantRepository.findByEmailEtudiant(email);
        if (etudiant.isPresent() && etudiant.get().getMotDePass().equals(motDePasse)) {
            session.setAttribute("etudiantConnecte", etudiant.get());
            return "redirect:/"; // redirection après connexion réussie
        } else {
            model.addAttribute("erreur", "Identifiants invalides.");
            return "connexion"; // on revient sur le formulaire avec le message d’erreur
        }
    }

    @GetMapping("/deconnexion")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
