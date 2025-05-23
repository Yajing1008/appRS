package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

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

    /**
     * Affiche le formulaire de réinitialisation du mot de passe (oubli du mot de passe).
     *
     * Cette méthode gère les requêtes HTTP GET vers l'URL "/motdepasse/reinitialiser".
     * Elle retourne la vue contenant le formulaire permettant à un utilisateur de
     * réinitialiser son mot de passe en saisissant son adresse e-mail.
     *
     * @return Le nom de la vue "motdepasse_reinitialiser".
     */
    @GetMapping("/motdepasse/reinitialiser")
    public String afficherFormulaireReinitialisationMotDePasse() {
        return "motdepasse_modifier";
    }

    /**
     * Traite la demande de réinitialisation du mot de passe par e-mail.
     *
     * @param email adresse e-mail de l'utilisateur
     * @param nouveauMotDePasse nouveau mot de passe saisi
     * @param confirmation confirmation du nouveau mot de passe
     * @param model modèle pour afficher les messages à la vue
     * @return la page de réinitialisation avec un message de succès ou d'erreur
     */
    @PostMapping("/motdepasse/reinitialiser")
    public String reinitialiserMotDePasse(@RequestParam String email,
                                          @RequestParam String nouveauMotDePasse,
                                          @RequestParam String confirmation,
                                          Model model) {
        // Recherche de l'utilisateur par email
        Optional<Etudiant> optionalEtudiant = etudiantRepository.findByEmailEtudiant(email);

        if (optionalEtudiant.isEmpty()) {
            model.addAttribute("erreur", "Aucun compte trouvé avec cette adresse e-mail.");
            return "motdepasse_modifier";
        }

        if (!nouveauMotDePasse.equals(confirmation)) {
            model.addAttribute("erreur", "La confirmation ne correspond pas au nouveau mot de passe.");
            return "motdepasse_modifier";
        }

        Etudiant etudiant = optionalEtudiant.get();
        etudiant.setMotDePass(nouveauMotDePasse);
        etudiantRepository.save(etudiant);

        model.addAttribute("message", "Mot de passe réinitialisé avec succès.");
        return "motdepasse_modifier";
    }



}