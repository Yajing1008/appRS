package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.EtudiantRepository;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur gérant l'inscription, la connexion et la déconnexion des étudiants.
 */
@Controller
public class ConnectionController {

    @Autowired
    private EtudiantRepository etudiantRepository;

    /**
     * Affiche le formulaire d'inscription.
     *
     * @param model modèle pour transmettre les données à la vue
     * @return la page d'inscription
     */
    @GetMapping("/inscription")
    public String showForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        return "inscription";
    }

    /**
     * Traite la soumission du formulaire d'inscription.
     * Vérifie si l'e-mail est déjà utilisé puis enregistre l'étudiant.
     *
     * @param etudiant l'objet étudiant rempli depuis le formulaire
     * @param model modèle pour transmettre les données à la vue
     * @return la page de confirmation ou à nouveau la page d'inscription en cas d'erreur
     */
    @PostMapping("/inscription")
    public String submitForm(@ModelAttribute Etudiant etudiant, Model model) {
        if (etudiantRepository.existsByEmailEtudiant(etudiant.getEmailEtudiant())) {
            model.addAttribute("erreur", "Cette adresse e-mail est déjà utilisée.");
            return "inscription";
        }
        etudiantRepository.save(etudiant);
        return "confirmationInscription";
    }

    /**
     * Affiche le formulaire de connexion.
     *
     * @return la page de connexion
     */
    @GetMapping("/connexion")
    public String afficherFormulaireConnexion() {
        return "connexion";
    }

    /**
     * Traite la connexion d'un étudiant.
     * Vérifie les identifiants puis enregistre l'étudiant dans la session.
     *
     * @param email l'adresse e-mail saisie
     * @param motDePasse le mot de passe saisi
     * @param session l'objet de session HTTP pour stocker l'utilisateur connecté
     * @param model modèle pour transmettre les messages à la vue
     * @return la page d'accueil ou la page de connexion en cas d'erreur
     */
    @PostMapping("/connexion")
    public String connecterEtudiant(@RequestParam String email,
                                    @RequestParam String motDePasse,
                                    HttpSession session,
                                    Model model) {
        Optional<Etudiant> etudiant = etudiantRepository.findByEmailEtudiant(email);
        if (etudiant.isPresent() && etudiant.get().getMotDePass().equals(motDePasse)) {
            session.setAttribute("etudiantConnecte", etudiant.get());
            return "redirect:/";
        } else {
            model.addAttribute("erreur", "Identifiants invalides.");
            return "connexion";
        }
    }

    /**
     * Déconnecte l'étudiant et invalide la session.
     *
     * @param session session HTTP à invalider
     * @return redirection vers la page d'accueil
     */
    @GetMapping("/deconnexion")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}