package com.ut1.miage.appRS.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Groupe;
import com.ut1.miage.appRS.repository.GroupeRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class GroupeController {

    @Autowired
    private GroupeRepository groupeRepository;

    @GetMapping("/groupe/groupes")
    public String afficherGroupes(Model model) {
        List<Groupe> groupes = groupeRepository.findAll(); // Ajoute les groupes au modèle
        model.addAttribute("groupes", groupes);
        return "groupes"; // Assure-toi que templates/groupes.html existe
    }

    @GetMapping("/groupe/nouveau")
    public String afficherFormulaire(Model model, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (etudiant == null) {
            return "redirect:/connexion";
        }

        model.addAttribute("groupe", new Groupe());
        return "formulaireGroupe";
    }

    @PostMapping("/groupe/nouveau")
    public String creerGroupe(@ModelAttribute Groupe groupe, HttpSession session) {
        Etudiant createur = (Etudiant) session.getAttribute("etudiantConnecte");
        if (createur == null) {
            return "redirect:/connexion";
        }

        groupe.setCreateur(createur);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        return "redirect:/groupe/groupes"; // redirige vers la liste des groupes
    }
}