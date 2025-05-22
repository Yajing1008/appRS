package com.ut1.miage.appRS.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ut1.miage.appRS.model.DemandeAmi;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.DemandeAmiRepository;
import com.ut1.miage.appRS.repository.EtudiantRepository;

import jakarta.servlet.http.HttpSession;

/**
 * Contrôleur permettant de gérer les fonctionnalités liées aux relations d'amitié :
 * affichage des amis, envoi et gestion des demandes d'amis, suppression d'un ami.
 */
@Controller
public class AmiController {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private DemandeAmiRepository demandeAmiRepository;

    /**
     * Affiche la page des amis pour l'utilisateur connecté.
     * Charge la liste des autres étudiants, amis, demandes reçues et envoyées.
     *
     * @param model   modèle utilisé pour transmettre les données à la vue
     * @param session session utilisateur
     * @return nom de la vue "ami"
     */
    @GetMapping("/ami")
    public String showAmiPage(Model model, HttpSession session) {
        Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");

        if (utilisateurConnecte == null) {
            return "connexion";
        }

        List<Etudiant> allEtudiants = etudiantRepository.findAllExceptSelf(utilisateurConnecte.getIdEtudiant());
        List<Etudiant> amis = etudiantRepository.findFriends(utilisateurConnecte.getIdEtudiant());
        List<DemandeAmi> demandesRecues = demandeAmiRepository.findByReceveurAndStatut(utilisateurConnecte, "EN_ATTENTE");
        List<DemandeAmi> demandesEnvoyees = demandeAmiRepository.findByDemandeurAndStatut(utilisateurConnecte, "EN_ATTENTE");

        Set<Long> idsReceveursEnAttente = demandesEnvoyees.stream()
                .map(d -> d.getReceveur().getIdEtudiant())
                .collect(Collectors.toSet());

        Set<Long> idsDemandeursEnAttente = demandesRecues.stream()
                .map(d -> d.getDemandeur().getIdEtudiant())
                .collect(Collectors.toSet());

        session.setAttribute("allEtudiants", allEtudiants);
        session.setAttribute("amis", amis);
        session.setAttribute("demandesRecues", demandesRecues);
        session.setAttribute("demandesEnvoyees", demandesEnvoyees);
        session.setAttribute("idsReceveursEnAttente", idsReceveursEnAttente);
        session.setAttribute("idsDemandeursEnAttente", idsDemandeursEnAttente);

        return "ami";
    }

    /**
     * Recherche les étudiants correspondant au mot-clé saisi (hors soi-même).
     *
     * @param search  mot-clé de recherche
     * @param session session utilisateur
     * @param model   modèle pour transmettre les résultats à la vue
     * @return vue "ami" avec les résultats de recherche
     */
    @GetMapping("/searchAmis")
    public String searchAmis(@RequestParam String search, HttpSession session, Model model) {
        Etudiant etudiantConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
        System.out.println("search = " + search);
        List<Etudiant> etudiantsDB = etudiantRepository.searchEtudiantsExceptSelf(search, etudiantConnecte.getIdEtudiant());

        model.addAttribute("etudiants_trouves", etudiantsDB);
        return "ami";
    }

    /**
     * Permet d'envoyer une demande d'ami à un autre étudiant.
     *
     * @param idReceveur identifiant de l'étudiant cible
     * @param session    session utilisateur
     * @return redirection vers la page /ami
     */
    @GetMapping("/envoyerDemande")
    public String envoyerDemande(@RequestParam Long idReceveur, HttpSession session) {
        Etudiant demandeur = (Etudiant) session.getAttribute("etudiantConnecte");
        Etudiant receveur = etudiantRepository.findById(idReceveur).orElse(null);

        if (receveur != null && !demandeur.equals(receveur) &&
                !demandeAmiRepository.existsByDemandeurAndReceveurAndStatut(demandeur, receveur, "EN_ATTENTE")) {

            DemandeAmi demande = new DemandeAmi();
            demande.setDemandeur(demandeur);
            demande.setReceveur(receveur);
            demande.setStatut("EN_ATTENTE");
            demandeAmiRepository.save(demande);
        }

        return "redirect:/ami";
    }

    /**
     * Accepte une demande d'ami reçue et établit la relation d'amitié entre les deux étudiants.
     *
     * @param idDemande identifiant de la demande à accepter
     * @param session   session utilisateur
     * @return redirection vers la page /ami
     */
    @GetMapping("/accepterDemande")
    public String accepterDemande(@RequestParam Long idDemande, HttpSession session) {
        DemandeAmi demande = demandeAmiRepository.findById(idDemande).orElse(null);

        if (demande != null && "EN_ATTENTE".equals(demande.getStatut())) {
            demande.setStatut("ACCEPTEE");

            Etudiant e1 = demande.getDemandeur();
            Etudiant e2 = demande.getReceveur();

            e1.getAmis().add(e2);
            e2.getAmis().add(e1);

            etudiantRepository.save(e1);
            etudiantRepository.save(e2);
            demandeAmiRepository.save(demande);
        }
        Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
        List<DemandeAmi> nouvellesDemandes = demandeAmiRepository.findByReceveurAndStatut(utilisateurConnecte, "EN_ATTENTE");
        session.setAttribute("demandesRecues", nouvellesDemandes);

        return "redirect:/ami";
    }

    /**
     * Refuse une demande d'ami en attente.
     *
     * @param idDemande identifiant de la demande à refuser
     * @param session   session utilisateur
     * @return redirection vers la page /ami
     */
    @GetMapping("/refuserDemande")
    public String refuserDemande(@RequestParam Long idDemande, HttpSession session) {
        DemandeAmi demande = demandeAmiRepository.findById(idDemande).orElse(null);

        if (demande != null && "EN_ATTENTE".equals(demande.getStatut())) {
            demande.setStatut("REFUSEE");
            demandeAmiRepository.save(demande);
        }

        Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
        List<DemandeAmi> nouvellesDemandes = demandeAmiRepository.findByReceveurAndStatut(utilisateurConnecte, "EN_ATTENTE");
        session.setAttribute("demandesRecues", nouvellesDemandes);

        return "redirect:/ami";
    }

    /**
     * Supprime un ami de la liste de l'utilisateur connecté (relation réciproque).
     *
     * @param id      identifiant de l'ami à supprimer
     * @param session session utilisateur
     * @return redirection vers la page d'accueil
     */
    @GetMapping("/supprimerAmi")
    @Transactional
    public String supprimerAmi(@RequestParam Long id, HttpSession session) {
        Etudiant etudiantConnecteSession = (Etudiant) session.getAttribute("etudiantConnecte");

        // Recharge les objets pour qu'ils soient bien liés à la session Hibernate
        Etudiant utilisateurConnecte = etudiantRepository.findById(etudiantConnecteSession.getIdEtudiant()).orElse(null);
        Etudiant ami = etudiantRepository.findById(id).orElse(null);

        if (utilisateurConnecte != null && ami != null) {
            utilisateurConnecte.getAmis().remove(ami);
            ami.getAmis().remove(utilisateurConnecte);
            etudiantRepository.save(utilisateurConnecte);
            etudiantRepository.save(ami);
        }

        return "redirect:/";
    }
}
