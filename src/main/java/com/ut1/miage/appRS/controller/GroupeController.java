package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.*;
import com.ut1.miage.appRS.repository.ConversationRepository;
import com.ut1.miage.appRS.repository.DemandeRejoindreGroupeRepository;
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
import java.util.stream.Collectors;

@Controller
public class GroupeController {

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private ParticiperRepository participerRepository;

    @Autowired
    private DemandeRejoindreGroupeRepository demandeRejoindreGroupeRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    /**
    * Affiche le formulaire de création de groupe.
    * Vérifie si l'étudiant est connecté avant de lui permettre de créer un groupe.
    *
    * @param model le modèle Spring MVC pour injecter les attributs dans la vue
    * @param session la session HTTP contenant éventuellement l'étudiant connecté
    * @return la vue du formulaire de création ou redirection vers la page de connexion
    */
    @GetMapping("/groupe/nouveau")
    public String afficherFormulaire(Model model, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (etudiant == null) {
            return "redirect:/connexion";
        }

        model.addAttribute("groupe", new Groupe());
        return "formulaireGroupe";
    }

    /**
    * Traite la soumission du formulaire de création de groupe.
    * Enregistre le groupe dans la base avec ou sans photo, en l’associant à son créateur.
    *
    * @param groupe le groupe saisi dans le formulaire
    * @param photo la photo envoyée par l'utilisateur (optionnelle)
    * @param session la session HTTP contenant l'étudiant connecté
    * @return redirection vers la liste des groupes ou vers la connexion si non connecté
    */
    @PostMapping("/groupe/nouveau")
    public String creerGroupe(@ModelAttribute Groupe groupe,
                              @RequestParam("photo") MultipartFile photo,
                              HttpSession session) {
        Etudiant createur = (Etudiant) session.getAttribute("etudiantConnecte");
        if (createur == null) return "redirect:/connexion";

        if (!photo.isEmpty()) {
            try {
                byte[] bytes = photo.getBytes();
                String contentType = photo.getContentType();
                String prefix = "data:" + (contentType != null ? contentType : "application/octet-stream") + ";base64,";
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                String base64Url = prefix + base64Image;
                groupe.setPhotoGroupe(base64Url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        groupe.setCreateur(createur);
        groupe.setDateCreerGroupe(LocalDate.now());
        Conversation conversation = new Conversation();
        conversation = conversationRepository.save(conversation);
        groupe.setConversation(conversation);
        groupeRepository.save(groupe);

        return "redirect:/groupe/groupes";
    }

    /**
    * Affiche la liste des groupes avec filtres selon la recherche et l'étudiant connecté.
    * Classe les groupes en sections personnalisées (créés, rejoints, demandes).
    *
    * @param recherche terme de recherche optionnel pour filtrer les groupes
    * @param model le modèle pour injecter les listes dans la vue
    * @param session session HTTP permettant d’identifier l’étudiant connecté
    * @return la vue listant les groupes, avec leurs catégories personnalisées
    */
    @GetMapping("/groupe/groupes")
    public String afficherGroupes(@RequestParam(value = "recherche", required = false) String recherche,
                                  Model model,
                                  HttpSession session) {

        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        model.addAttribute("etudiantConnecte", etudiant);

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
                    groupesRejoints.add(g.getIdGroupe());
                }
            }
        }

        List<DemandeRejoindreGroupe> demandesExistantes = demandeRejoindreGroupeRepository.findByEtudiant(etudiant);
        Set<Long> groupesDemandes = demandesExistantes.stream()
                .filter(d -> !Boolean.TRUE.equals(d.getApprouvee()))
                .map(d -> d.getGroupe().getIdGroupe())
                .collect(Collectors.toSet());

        model.addAttribute("groupesDemandes", groupesDemandes);
        model.addAttribute("groupesRejoints", groupesRejoints);
        model.addAttribute("groupesCrees", groupesCrees);
        model.addAttribute("groupesMembre", groupesMembre);

        return "groupes";
    }

    /**
    * Permet à un étudiant de rejoindre un groupe public.
    * Vérifie que le groupe existe, qu’il est public et que l’étudiant n’est pas déjà le créateur.
    * Ajoute ensuite l’étudiant à la liste des membres du groupe.
    *
    * @param id l’identifiant du groupe à rejoindre
    * @param session session HTTP pour récupérer l’étudiant connecté
    * @param model modèle utilisé pour injecter des attributs dans la vue
    * @return redirection vers les détails du groupe ou vers la connexion/liste selon les cas
    */
    @PostMapping("/groupe/{id}/rejoindre")
    public String rejoindreGroupe(@PathVariable Long id, HttpSession session, Model model) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (etudiant == null) {
            return "redirect:/connexion";
        }

        Groupe groupe = groupeRepository.findById(id).orElse(null);
        if (groupe == null || !groupe.getEstPublicGroupe()) {
            return "redirect:/groupe/groupes";
        }

        if (groupe.getCreateur() != null &&
                etudiant.getIdEtudiant().equals(groupe.getCreateur().getIdEtudiant())) {
            return "redirect:/groupe/" + id + "/details";
        }

        ParticiperId pid = new ParticiperId(etudiant.getIdEtudiant(), groupe.getIdGroupe());

        Participer participer = new Participer();
        participer.setId(pid);
        participer.setEtudiant(etudiant);
        participer.setGroupe(groupe);
        participer.setRole("membre");

        participerRepository.save(participer);
        return "redirect:/groupe/" + id + "/details";
    }

    /**
    * Permet à un membre de quitter un groupe.
    * Supprime l’entrée correspondante dans la table de jointure `Participer`.
    *
    * @param id identifiant du groupe à quitter
    * @param session session HTTP contenant l’étudiant connecté
    * @return redirection vers la liste des groupes ou la page de connexion si non connecté
    */
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

    /**
    * Affiche les détails d’un groupe.
    * Si l’utilisateur est connecté, indique également s’il est membre du groupe.
    *
    * @param id identifiant du groupe à consulter
    * @param session session HTTP contenant l’étudiant connecté
    * @param model modèle pour injecter les informations dans la vue
    * @return vue des détails du groupe ou redirection si le groupe n’existe pas
    */
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
            model.addAttribute("etudiantConnecte", etudiant);
        }

        model.addAttribute("estMembre", estMembre);
        return "groupeDetail";
    }

    /**
    * Affiche le formulaire de modification d’un groupe.
    * Vérifie que l’étudiant est bien le créateur du groupe avant d’afficher la vue.
    *
    * @param id identifiant du groupe à modifier
    * @param session session HTTP contenant l’étudiant connecté
    * @param model modèle pour injecter le groupe dans la vue
    * @return vue de modification ou redirection vers la connexion ou la liste
    */
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

    /**
    * Applique les modifications (nom, description, visibilité, photo) à un groupe.
    * Seul le créateur du groupe peut effectuer cette opération.
    *
    * @param id identifiant du groupe à modifier
    * @param groupeModifie groupe contenant les nouvelles valeurs saisies
    * @param fichierPhoto photo à mettre à jour, facultative
    * @param session session HTTP contenant l’étudiant connecté
    * @return redirection vers la page de détails du groupe ou vers la connexion/liste si non autorisé
    * @throws IOException en cas d’erreur lors de la lecture du fichier
    */
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
            String contentType = fichierPhoto.getContentType();
            String prefix = "data:" + (contentType != null ? contentType : "application/octet-stream") + ";base64,";
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            String base64Url = prefix + base64Image;
            groupe.setPhotoGroupe(base64Url);
        }

        groupeRepository.save(groupe);
        return "redirect:/groupe/" + id + "/details";
    }

    /**
    * Envoie une demande pour rejoindre un groupe privé.
    * L’étudiant connecté sera en attente d’approbation par le créateur du groupe.
    *
    * @param id identifiant du groupe ciblé
    * @param session session HTTP contenant l’étudiant connecté
    * @return redirection vers la liste des groupes ou vers la connexion
    */
    @PostMapping("/groupe/{id}/demande-rejoindre")
    public String demanderARejoindre(@PathVariable Long id, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (etudiant == null) return "redirect:/connexion";

        Groupe groupe = groupeRepository.findById(id).orElse(null);
        if (groupe == null || groupe.getEstPublicGroupe()) return "redirect:/groupe/groupes";

        DemandeRejoindreGroupe demande = new DemandeRejoindreGroupe();
        demande.setEtudiant(etudiant);
        demande.setGroupe(groupe);
        demandeRejoindreGroupeRepository.save(demande);

        return "redirect:/groupe/groupes";
    }

    /**
    * Affiche les demandes d’adhésion reçues pour tous les groupes créés par l’étudiant connecté.
    *
    * @param model modèle utilisé pour injecter la liste des demandes dans la vue
    * @param session session HTTP contenant l’étudiant connecté
    * @return la page listant les demandes d’adhésion ou redirection vers la connexion
    */
    @GetMapping("/groupe/mes-demandes")
    public String afficherDemandes(Model model, HttpSession session) {
        Etudiant createur = (Etudiant) session.getAttribute("etudiantConnecte");
        if (createur == null) return "redirect:/connexion";

        List<DemandeRejoindreGroupe> demandes = demandeRejoindreGroupeRepository.findByGroupeCreateurIdEtudiant(createur.getIdEtudiant());
        model.addAttribute("demandes", demandes);
        return "mesDemandes";
    }

    /**
    * Accepte une demande d’adhésion à un groupe et ajoute l’étudiant en tant que membre.
    * Supprime ensuite la demande de la base de données.
    *
    * @param id identifiant de la demande à traiter
    * @return redirection vers la page des demandes du créateur
    */
    @PostMapping("/groupe/demande/{id}/accepter")
    public String accepterDemande(@PathVariable Long id) {
        DemandeRejoindreGroupe demande = demandeRejoindreGroupeRepository.findById(id).orElse(null);
        if (demande != null) {
            demande.setApprouvee(true);
            demandeRejoindreGroupeRepository.save(demande);

            Participer participer = new Participer();
            participer.setEtudiant(demande.getEtudiant());
            participer.setGroupe(demande.getGroupe());
            participer.setRole("membre");
            ParticiperId pid = new ParticiperId(demande.getEtudiant().getIdEtudiant(), demande.getGroupe().getIdGroupe());
            participer.setId(pid);
            participerRepository.save(participer);
        }

        demandeRejoindreGroupeRepository.delete(demande);

        return "redirect:/groupe/mes-demandes";
    }

    /**
    * Refuse une demande d’adhésion à un groupe.
    * Met à jour son statut comme refusée puis la supprime de la base.
    *
    * @param id identifiant de la demande à refuser
    * @return redirection vers la page des demandes du créateur
    */
    @PostMapping("/groupe/demande/{id}/refuser")
    public String refuserDemande(@PathVariable Long id) {
        DemandeRejoindreGroupe demande = demandeRejoindreGroupeRepository.findById(id).orElse(null);
        if (demande != null) {
            demande.setApprouvee(false);
            demandeRejoindreGroupeRepository.save(demande);
        }
        demandeRejoindreGroupeRepository.delete(demande);

        return "redirect:/groupe/mes-demandes";
    }

    /**
    * Permet au créateur d’un groupe de retirer un membre donné.
    *
    * @param id identifiant du groupe
    * @param idEtudiant identifiant de l’étudiant à retirer
    * @param session session HTTP contenant l’étudiant connecté (créateur attendu)
    * @return redirection vers les détails du groupe ou vers la liste si non autorisé
    */
    @PostMapping("/groupe/{id}/retirer-membre/{idEtudiant}")
    @Transactional
    public String retirerMembre(@PathVariable Long id, @PathVariable Long idEtudiant, HttpSession session) {
        Etudiant createur = (Etudiant) session.getAttribute("etudiantConnecte");
        if (createur == null) return "redirect:/connexion";

        Groupe groupe = groupeRepository.findById(id).orElse(null);
        if (groupe == null || !groupe.getCreateur().getIdEtudiant().equals(createur.getIdEtudiant())) {
            return "redirect:/groupe/groupes";
        }

        ParticiperId participerId = new ParticiperId(idEtudiant, id);
        participerRepository.deleteById(participerId);

        return "redirect:/groupe/" + id + "/details";
    }

    /**
    * Supprime un groupe si l’étudiant connecté en est le créateur.
    * Tous les membres et données associées seront supprimés en cascade.
    *
    * @param id identifiant du groupe à supprimer
    * @param session session HTTP contenant l’étudiant connecté
    * @return redirection vers la liste des groupes
    */
    @PostMapping("/groupe/{id}/supprimer")
    @Transactional
    public String supprimerGroupe(@PathVariable Long id, HttpSession session) {
        Etudiant createur = (Etudiant) session.getAttribute("etudiantConnecte");
        if (createur == null) return "redirect:/connexion";

        Groupe groupe = groupeRepository.findById(id).orElse(null);
        if (groupe == null || !groupe.getCreateur().getIdEtudiant().equals(createur.getIdEtudiant())) {
            return "redirect:/groupe/groupes";
        }

        groupeRepository.deleteById(id);
        return "redirect:/groupe/groupes";
    }

}
