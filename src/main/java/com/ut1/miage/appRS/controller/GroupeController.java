package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Groupe;
import com.ut1.miage.appRS.model.Participer;
import com.ut1.miage.appRS.model.ParticiperId;
import com.ut1.miage.appRS.repository.GroupeRepository;
import com.ut1.miage.appRS.repository.ParticiperRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Controller
public class GroupeController {

    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private ParticiperRepository participerRepository;

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
    public String creerGroupe(@ModelAttribute Groupe groupe,
                              @RequestParam("photo") MultipartFile photo,
                              HttpSession session) {
        Etudiant createur = (Etudiant) session.getAttribute("etudiantConnecte");
        if (createur == null) return "redirect:/connexion";

        // Gestion de la photo
        if (!photo.isEmpty()) {
            try {
                byte[] bytes = photo.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                groupe.setPhotoGroupe(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        groupe.setCreateur(createur);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        return "redirect:/groupe/groupes";
    }

    @GetMapping("/groupe/groupes")
    public String afficherGroupes(@RequestParam(value = "recherche", required = false) String recherche,
                                  Model model,
                                  HttpSession session) {

        // Récupération de l'étudiant connecté
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        model.addAttribute("etudiantConnecte", etudiant);

        // Recherche des groupes selon le critère de recherche
        List<Groupe> groupes;
        if (recherche != null && !recherche.trim().isEmpty()) {
            groupes = groupeRepository.findByNomGroupeContainingIgnoreCase(recherche);
        } else {
            groupes = groupeRepository.findAll();
        }

        model.addAttribute("groupes", groupes);
        model.addAttribute("recherche", recherche);

        Set<Long> groupesRejoints = new HashSet<>();
        List<Groupe> groupesCrees = new ArrayList<>();
        List<Groupe> groupesMembre = new ArrayList<>();

        if (etudiant != null) {
            for (Groupe g : groupes) {
                boolean isCreateur = g.getCreateur() != null &&
                        etudiant.getIdEtudiant().equals(g.getCreateur().getIdEtudiant());

                boolean isMembre = g.getMembres().stream()
                        .anyMatch(p -> p.getEtudiant() != null &&
                                etudiant.getIdEtudiant().equals(p.getEtudiant().getIdEtudiant()));

                if (isCreateur) {
                    groupesCrees.add(g);
                } else if (isMembre) {
                    groupesMembre.add(g);
                    groupesRejoints.add(g.getIdGroupe()); // utile pour les boutons "Rejoindre"
                }
            }
        }

        model.addAttribute("groupesRejoints", groupesRejoints);
        model.addAttribute("groupesCrees", groupesCrees);
        model.addAttribute("groupesMembre", groupesMembre);

        return "groupes";
    }


    @PostMapping("/groupe/{id}/rejoindre")
    public String rejoindreGroupe(@PathVariable Long id, HttpSession session, Model model) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (etudiant == null) {
            return "redirect:/connexion";
        }

        Groupe groupe = groupeRepository.findById(id).orElse(null);
        if (groupe == null || !groupe.getEstPublicGroupe()) {
            return "redirect:/groupe/groupes"; // Redirection sécurité
        }

        //Empêche le créateur de rejoindre son propre groupe
        if (groupe.getCreateur() != null &&
                etudiant.getIdEtudiant().equals(groupe.getCreateur().getIdEtudiant())) {
            return "redirect:/groupe/" + id + "/details";
        }

        //Initialise correctement la clé composite
        ParticiperId pid = new ParticiperId(etudiant.getIdEtudiant(), groupe.getIdGroupe());

        //Création de la participation
        Participer participer = new Participer();
        participer.setId(pid);
        participer.setEtudiant(etudiant);
        participer.setGroupe(groupe);
        participer.setRole("membre");

        participerRepository.save(participer);
        return "redirect:/groupe/" + id + "/details";
    }

    @PostMapping("/groupe/{id}/quitter")
    @Transactional
    public String quitterGroupe(@PathVariable Long id, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (etudiant == null) {
            return "redirect:/connexion";
        }

        ParticiperId participerId = new ParticiperId(etudiant.getIdEtudiant(), id);
        participerRepository.deleteById(participerId);

        return "redirect:/groupe/groupes";
    }

    @GetMapping("/groupe/{id}/details")
    public String voirDetailsGroupe(@PathVariable Long id, HttpSession session, Model model) {
        Optional<Groupe> optGroupe = groupeRepository.findById(id);
        if (optGroupe.isEmpty()) return "redirect:/groupe/groupes";

        Groupe groupe = optGroupe.get();
        model.addAttribute("groupe", groupe);

        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        boolean estMembre = false;

        if (etudiant != null) {
            estMembre = groupe.getMembres().stream()
                    .anyMatch(p -> p.getEtudiant() != null &&
                            etudiant.getIdEtudiant().equals(p.getEtudiant().getIdEtudiant()));
        }

        model.addAttribute("estMembre", estMembre);
        return "groupeDetail";
    }

    // Affiche le formulaire de modification
    @GetMapping("/groupe/{id}/modifier")
    public String afficherFormulaireModification(@PathVariable Long id, HttpSession session, Model model) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (etudiant == null) return "redirect:/connexion";

        Groupe groupe = groupeRepository.findById(id).orElse(null);
        if (groupe == null || !etudiant.getIdEtudiant().equals(groupe.getCreateur().getIdEtudiant())) {
            return "redirect:/groupe/groupes";
        }

        model.addAttribute("groupe", groupe);
        return "modifierGroupe";
    }

    // Gère la modification du groupe
    @PostMapping("/groupe/{id}/modifier")
    public String modifierGroupe(@PathVariable Long id,
                                 @ModelAttribute Groupe groupeModifie,
                                 @RequestParam("photo") MultipartFile fichierPhoto,
                                 HttpSession session) throws IOException {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (etudiant == null) return "redirect:/connexion";

        Groupe groupe = groupeRepository.findById(id).orElse(null);
        if (groupe == null || !groupe.getCreateur().getIdEtudiant().equals(etudiant.getIdEtudiant())) {
            return "redirect:/groupe/groupes";
        }

        groupe.setNomGroupe(groupeModifie.getNomGroupe());
        groupe.setDescriptionGroupe(groupeModifie.getDescriptionGroupe());
        groupe.setEstPublicGroupe(groupeModifie.getEstPublicGroupe());

        if (!fichierPhoto.isEmpty()) {
            byte[] bytes = fichierPhoto.getBytes();
            String base64 = Base64.getEncoder().encodeToString(bytes);
            groupe.setPhotoGroupe(base64);
        }

        groupeRepository.save(groupe);
        return "redirect:/groupe/" + id + "/details";
    }
}