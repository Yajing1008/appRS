package com.ut1.miage.appRS.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Evenement;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import com.ut1.miage.appRS.repository.EvenementRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class EvenementController {

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    /**
     * Affiche la page des événements de l'étudiant connecté.
     *
     * <p>Cette méthode permet :
     * <ul>
     *   <li>d'afficher les événements créés par l'étudiant,</li>
     *   <li>d'afficher les événements auxquels il participe,</li>
     *   <li>de générer un JSON compatible avec FullCalendar,</li>
     *   <li>et de transmettre toutes les données nécessaires à la vue "event".</li>
     * </ul>
     *
     * @param model   le modèle pour passer les attributs à la vue
     * @param session la session HTTP contenant l'étudiant connecté
     * @return la vue "event" avec les données d'événements
     */
    @GetMapping("/evenement")
    public String afficherEvenementsEtCalendrier(Model model, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        List<Map<String, Object>> allEvents = new ArrayList<>();
        List<Evenement> mesCreations = new ArrayList<>();
        List<Evenement> mesParticipations = new ArrayList<>();

        if (etudiant != null) {

            mesCreations = evenementRepository.findByCreateur(etudiant)
                    .stream()
                    .sorted(Comparator.comparing(Evenement::getDateHeureDebutEvenement).reversed())
                    .toList();


            List<Evenement> participationsBrutes = evenementRepository.findByMembreGroupeContains(etudiant);


            final List<Evenement> finalMesCreations = mesCreations;
            mesParticipations = participationsBrutes.stream()
                    .filter(e -> !finalMesCreations.contains(e))
                    .sorted(Comparator.comparing(Evenement::getDateHeureDebutEvenement).reversed())
                    .toList();


            for (Evenement e : mesCreations) {
                Map<String, Object> eventMap = new HashMap<>();
                eventMap.put("title", e.getNomEvenement() + " (Créé)");
                eventMap.put("start", e.getDateHeureDebutEvenement().toString());
                eventMap.put("end", e.getDateHeureFinEvenement().toString());
                eventMap.put("url", "/evenements/" + e.getIdEvenement());
                allEvents.add(eventMap);
            }

            for (Evenement e : mesParticipations) {
                Map<String, Object> eventMap = new HashMap<>();
                eventMap.put("title", e.getNomEvenement() + " (Participé)");
                eventMap.put("start", e.getDateHeureDebutEvenement().toString());
                eventMap.put("end", e.getDateHeureFinEvenement().toString());
                eventMap.put("url", "/evenements/" + e.getIdEvenement());
                allEvents.add(eventMap);
            }

            model.addAttribute("utilisateurConnecte", etudiant);
        }


        try {
            ObjectMapper mapper = new ObjectMapper();
            String eventJson = mapper.writeValueAsString(allEvents);
            model.addAttribute("eventJson", eventJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            model.addAttribute("eventJson", "[]");
        }


        model.addAttribute("mesCreations", mesCreations);
        model.addAttribute("mesParticipations", mesParticipations);

        return "event";
    }




    /**
     * Affiche le formulaire de création d’un nouvel événement.
     *
     * <p>Cette méthode initialise un objet {@link Evenement} vide
     * et l'ajoute au modèle afin que le formulaire puisse être rempli.
     * Elle renvoie la vue "event_creer".
     *
     * @param model le modèle utilisé pour passer les données à la vue
     * @return la vue "event_creer" contenant le formulaire
     */
    @GetMapping("/evenement/creer")
    public String afficherFormulaireCreation(Model model) {
        model.addAttribute("evenement", new Evenement());
        return "event_creer";
    }


    /**
     * Traite la soumission du formulaire de création d’un événement.
     *
     * <p>Cette méthode :
     * <ul>
     *   <li>vérifie que l’utilisateur est connecté,</li>
     *   <li>valide les dates de début et de fin,</li>
     *   <li>enregistre éventuellement une image en base64,</li>
     *   <li>associe l’événement à son créateur,</li>
     *   <li>et enregistre l’événement dans la base de données.</li>
     * </ul>
     *
     * <p>Elle redirige vers la page des événements avec un message de succès ou d’erreur.</p>
     *
     * @param evenement l’objet Evenement contenant les données du formulaire
     * @param photoFile le fichier image (optionnel)
     * @param session la session contenant l’utilisateur connecté
     * @param redirectAttributes pour afficher les messages flash
     * @return redirection vers "/evenement" ou "/connexion" si non connecté
     * @throws IOException si une erreur survient lors de la lecture du fichier
     */
    @PostMapping("/evenement/save")
    public String sauvegarderEvenement(@ModelAttribute Evenement evenement,
                                       @RequestParam(value = "photo", required = false) MultipartFile photoFile,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) throws IOException {

        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Vous devez être connecté pour créer un événement.");
            return "redirect:/connexion";
        }

        LocalDateTime now = LocalDateTime.now();


        if (evenement.getDateHeureDebutEvenement() == null || evenement.getDateHeureDebutEvenement().isBefore(now)) {
            redirectAttributes.addFlashAttribute("error", "La date de début doit être postérieure à la date actuelle.");
            return "redirect:/evenement";
        }


        if (evenement.getDateHeureFinEvenement() == null || evenement.getDateHeureFinEvenement().isBefore(evenement.getDateHeureDebutEvenement())) {
            redirectAttributes.addFlashAttribute("error", "La date de fin doit être postérieure à la date de début.");
            return "redirect:/evenement";
        }


        if (photoFile != null && !photoFile.isEmpty()) {
            byte[] photoBytes = photoFile.getBytes();
            String base64 = Base64.getEncoder().encodeToString(photoBytes);
            evenement.setImageUrlEvenement("data:image/jpeg;base64," + base64);
        }


        evenement.setCreateur(etudiant);
        evenementRepository.save(evenement);

        redirectAttributes.addFlashAttribute("success", "Événement créé avec succès.");
        return "redirect:/evenement";
    }




    /**
     * Constructeur du contrôleur EvenementController.
     *
     * <p>Ce constructeur permet à Spring d’injecter automatiquement
     * une instance de {@link EvenementRepository} pour gérer l’accès aux événements.
     *
     * @param evenementRepository le dépôt permettant l’accès aux événements en base
     */
    public EvenementController(EvenementRepository evenementRepository) {
        this.evenementRepository = evenementRepository;
    }


    /**
     * Annule un événement si l'utilisateur connecté est son créateur
     * et que l'événement n’a pas encore commencé.
     *
     * <p>Cette méthode vérifie :
     * <ul>
     *   <li>que l'utilisateur est connecté,</li>
     *   <li>que l’événement existe,</li>
     *   <li>que l'utilisateur en est le créateur,</li>
     *   <li>et que l’événement n’a pas encore commencé.</li>
     * </ul>
     *
     * <p>Si toutes les conditions sont réunies, l'événement est supprimé.</p>
     *
     * @param idEvenement l’identifiant de l’événement à annuler
     * @param session la session contenant l’utilisateur connecté
     * @param redirectAttributes pour transmettre les messages flash
     * @return une redirection vers la page des événements
     */
    @PostMapping("/evenement/annuler")
    public String annulerEvenement(@RequestParam Long idEvenement,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Vous devez être connecté pour annuler un événement.");
            return "redirect:/connexion";
        }

        Optional<Evenement> optionalEvent = evenementRepository.findById(idEvenement);

        if (optionalEvent.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "L'événement n'existe pas.");
            return "redirect:/evenement";
        }

        Evenement evenement = optionalEvent.get();


        if (!evenement.getCreateur().getIdEtudiant().equals(etudiant.getIdEtudiant())) {
            redirectAttributes.addFlashAttribute("error", "Vous n'êtes pas le créateur de cet événement.");
            return "redirect:/evenement";
        }


        if (evenement.getDateHeureDebutEvenement().isBefore(java.time.LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "L'événement a déjà commencé, il ne peut plus être annulé.");
            return "redirect:/evenement";
        }

        evenementRepository.delete(evenement);
        redirectAttributes.addFlashAttribute("success", "Événement annulé avec succès.");
        return "redirect:/evenement";
    }



    /**
     * Affiche la liste de tous les événements à venir (non encore terminés).
     *
     * <p>Cette méthode récupère tous les événements dont la date de fin est
     * postérieure à l’instant actuel, et les transmet à la vue "event_rejoindre"
     * pour affichage. Si un utilisateur est connecté, il est également passé au modèle.
     *
     * @param model le modèle utilisé pour transmettre les données à la vue
     * @param session la session contenant potentiellement l’utilisateur connecté
     * @return la vue "event_rejoindre" affichant les événements futurs
     */
    @GetMapping("/evenement/liste")
    public String listerTousLesEvenements(Model model, HttpSession session) {
        Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");


        List<Evenement> evenementsFuturs = evenementRepository
                .findByDateHeureFinEvenementAfterOrderByDateHeureDebutEvenementAsc(java.time.LocalDateTime.now());

        model.addAttribute("utilisateurConnecte", utilisateurConnecte);
        model.addAttribute("evenementsFiltres", evenementsFuturs);

        return "event_rejoindre";
    }


    /**
     * Recherche des événements à venir à partir d’un mot-clé optionnel.
     *
     * <p>Si le mot-clé est renseigné, cette méthode retourne tous les événements
     * dont le nom contient ce mot-clé (sans tenir compte de la casse),
     * et dont la date de fin est postérieure à maintenant.
     * Sinon, elle retourne tous les événements futurs.
     *
     * <p>Le résultat est affiché dans la vue {@code event_rejoindre}.
     * L’utilisateur connecté est également transmis au modèle s’il est présent.
     *
     * @param motCle le mot-clé saisi par l’utilisateur (optionnel)
     * @param model le modèle contenant les résultats
     * @param session la session HTTP contenant éventuellement l’utilisateur connecté
     * @return la vue {@code event_rejoindre} avec la liste filtrée des événements
     */
    @GetMapping("/evenement/recherche")
    public String rechercherEvenements(@RequestParam(name = "motCle", required = false) String motCle,
                                       Model model,
                                       HttpSession session) {
        Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");

        List<Evenement> resultats;

        if (motCle != null && !motCle.trim().isEmpty()) {
            resultats = evenementRepository
                    .findByNomEvenementContainingIgnoreCaseAndDateHeureFinEvenementAfterOrderByDateHeureDebutEvenementAsc(
                            motCle.trim(), java.time.LocalDateTime.now());
        } else {
            resultats = evenementRepository
                    .findByDateHeureFinEvenementAfterOrderByDateHeureDebutEvenementAsc(java.time.LocalDateTime.now());
        }

        model.addAttribute("utilisateurConnecte", utilisateurConnecte);
        model.addAttribute("evenementsFiltres", resultats);

        return "event_rejoindre";
    }

    /**
     * Permet à un utilisateur connecté de rejoindre un événement existant.
     *
     * <p>Cette méthode :
     * <ul>
     *   <li>vérifie que l’utilisateur est connecté ;</li>
     *   <li>vérifie que l’événement existe ;</li>
     *   <li>s’assure que l’événement n’est pas terminé ;</li>
     *   <li>s’assure que l’utilisateur n’en est pas le créateur ;</li>
     *   <li>et qu’il n’y participe pas déjà.</li>
     * </ul>
     *
     * <p>Si toutes les conditions sont réunies, l’utilisateur est ajouté
     * aux participants de l’événement.</p>
     *
     * @param idEvenement l’identifiant de l’événement à rejoindre
     * @param session la session contenant l’utilisateur connecté
     * @param redirectAttributes pour afficher les messages de succès ou d’erreur
     * @return une redirection vers la page des événements
     */
    @GetMapping("/evenement/rejoindre/{idEvenement}")
    public String rejoindreEvenement(@PathVariable Long idEvenement,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        Etudiant utilisateur = (Etudiant) session.getAttribute("etudiantConnecte");

        if (utilisateur == null) {
            redirectAttributes.addFlashAttribute("error", "Vous devez être connecté pour rejoindre un événement.");
            return "redirect:/connexion";
        }

        Optional<Evenement> optionalEvent = evenementRepository.findById(idEvenement);
        if (optionalEvent.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Événement introuvable.");
            return "redirect:/evenement";
        }

        Evenement evenement = optionalEvent.get();

        if (evenement.getDateHeureFinEvenement().isBefore(java.time.LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "L'événement est déjà terminé.");
            return "redirect:/evenement";
        }

        if (evenement.getCreateur().getIdEtudiant().equals(utilisateur.getIdEtudiant())) {
            redirectAttributes.addFlashAttribute("error", "Vous êtes le créateur de cet événement.");
            return "redirect:/evenement";
        }


        boolean dejaParticipant = evenement.getMembreGroupe().stream()
                .anyMatch(e -> e.getIdEtudiant().equals(utilisateur.getIdEtudiant()));

        if (dejaParticipant) {
            redirectAttributes.addFlashAttribute("error", "Vous participez déjà à cet événement.");
            return "redirect:/evenement";
        }

        evenement.getMembreGroupe().add(utilisateur);
        evenementRepository.save(evenement);

        redirectAttributes.addFlashAttribute("success", "Vous avez rejoint l'événement avec succès !");
        return "redirect:/evenement";
    }
    /**
     * Permet à un utilisateur connecté de quitter un événement auquel il participe.
     *
     * <p>Cette méthode effectue plusieurs vérifications :
     * <ul>
     *   <li>l’utilisateur est connecté ;</li>
     *   <li>l’événement existe ;</li>
     *   <li>l’événement n’est pas encore terminé ;</li>
     *   <li>l’utilisateur est bien un participant de cet événement.</li>
     * </ul>
     *
     * <p>Si toutes les conditions sont remplies, l’utilisateur est retiré de la liste
     * des participants, et l’événement est mis à jour.</p>
     *
     * @param idEvenement identifiant de l’événement à quitter
     * @param session la session HTTP contenant l’utilisateur connecté
     * @param redirectAttributes messages flash pour le retour utilisateur
     * @return redirection vers la page des événements
     */
    @GetMapping("/evenement/quitter/{idEvenement}")
    public String quitterEvenement(@PathVariable Long idEvenement,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Etudiant utilisateur = (Etudiant) session.getAttribute("etudiantConnecte");

        if (utilisateur == null) {
            redirectAttributes.addFlashAttribute("error", "Vous devez être connecté pour quitter un événement.");
            return "redirect:/connexion";
        }

        Optional<Evenement> optionalEvent = evenementRepository.findById(idEvenement);
        if (optionalEvent.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Événement introuvable.");
            return "redirect:/evenement";
        }

        Evenement evenement = optionalEvent.get();


        if (evenement.getDateHeureFinEvenement().isBefore(java.time.LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "L'événement est déjà terminé, vous ne pouvez plus le quitter.");
            return "redirect:/evenement";
        }


        boolean estParticipant = evenement.getMembreGroupe().stream()
                .anyMatch(e -> e.getIdEtudiant().equals(utilisateur.getIdEtudiant()));

        if (!estParticipant) {
            redirectAttributes.addFlashAttribute("error", "Vous ne participez pas à cet événement.");
            return "redirect:/evenement";
        }


        evenement.getMembreGroupe().removeIf(e -> e.getIdEtudiant().equals(utilisateur.getIdEtudiant()));
        evenementRepository.save(evenement);

        redirectAttributes.addFlashAttribute("success", "Vous avez quitté l'événement avec succès.");
        return "redirect:/evenement";
    }

    /**
     * Affiche le formulaire de modification d’un événement existant,
     * uniquement si l’utilisateur est connecté et est le créateur de l’événement.
     *
     * <p>Conditions vérifiées :
     * <ul>
     *   <li>L’utilisateur est connecté ;</li>
     *   <li>L’événement existe ;</li>
     *   <li>L’utilisateur est le créateur de l’événement.</li>
     * </ul>
     *
     * <p>Si toutes les conditions sont remplies, l’événement est injecté dans le modèle
     * pour affichage dans le formulaire {@code event_modifier.html}.</p>
     *
     * @param id identifiant de l’événement à modifier
     * @param model le modèle à transmettre à la vue
     * @param session session HTTP contenant potentiellement l’utilisateur connecté
     * @param redirectAttributes attributs flash pour transmettre les erreurs
     * @return la vue {@code event_modifier} ou une redirection en cas d’erreur
     */
    @GetMapping("/evenement/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable Long id,
                                                 Model model,
                                                 HttpSession session,
                                                 RedirectAttributes redirectAttributes) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Vous devez être connecté pour modifier un événement.");
            return "redirect:/connexion";
        }

        Optional<Evenement> optionalEvenement = evenementRepository.findById(id);

        if (optionalEvenement.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Événement introuvable.");
            return "redirect:/evenement";
        }

        Evenement evenement = optionalEvenement.get();

        if (!evenement.getCreateur().getIdEtudiant().equals(etudiant.getIdEtudiant())) {
            redirectAttributes.addFlashAttribute("error", "Vous n'êtes pas le créateur de cet événement.");
            return "redirect:/evenement";
        }

        model.addAttribute("evenement", evenement);
        return "event_modifier";
    }


    /**
     * Met à jour les informations d’un événement existant.
     *
     * <p>Cette méthode :
     * <ul>
     *   <li>vérifie que l’utilisateur est connecté ;</li>
     *   <li>vérifie que l’événement existe et que l’utilisateur en est le créateur ;</li>
     *   <li>valide les dates de début et de fin ;</li>
     *   <li>met à jour le nom, la description, le lieu, les dates, et éventuellement l’image ;</li>
     *   <li>enregistre les modifications en base et redirige vers la liste des événements.</li>
     * </ul>
     *
     * @param evenement l’événement modifié à sauvegarder
     * @param photoFile un fichier image optionnel (peut être vide)
     * @param session la session contenant l’utilisateur connecté
     * @param redirectAttributes messages flash pour les retours utilisateur
     * @return une redirection vers "/evenement"
     * @throws IOException en cas d’erreur de traitement du fichier image
     */
    @PostMapping("/evenement/update")
    public String modifierEvenement(@ModelAttribute Evenement evenement,
                                    @RequestParam(value = "photo", required = false) MultipartFile photoFile,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) throws IOException {

        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Vous devez être connecté pour modifier un événement.");
            return "redirect:/connexion";
        }

        Optional<Evenement> optionalEvenement = evenementRepository.findById(evenement.getIdEvenement());
        if (optionalEvenement.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "L'événement que vous essayez de modifier n'existe pas.");
            return "redirect:/evenement";
        }

        Evenement original = optionalEvenement.get();


        if (!original.getCreateur().getIdEtudiant().equals(etudiant.getIdEtudiant())) {
            redirectAttributes.addFlashAttribute("erreur", "Vous n'êtes pas le créateur de cet événement.");
            return "redirect:/evenement";
        }


        LocalDateTime now = LocalDateTime.now();
        if (evenement.getDateHeureDebutEvenement() == null || evenement.getDateHeureDebutEvenement().isBefore(now)) {
            redirectAttributes.addFlashAttribute("error", "La date de début doit être postérieure à la date actuelle.");
            return "redirect:/evenement";
        }

        if (evenement.getDateHeureFinEvenement() == null || evenement.getDateHeureFinEvenement().isBefore(evenement.getDateHeureDebutEvenement())) {
            redirectAttributes.addFlashAttribute("error", "La date de fin doit être postérieure à la date de début.");
            return "redirect:/evenement";
        }


        original.setNomEvenement(evenement.getNomEvenement());
        original.setLieuEvenement(evenement.getLieuEvenement());
        original.setDateHeureDebutEvenement(evenement.getDateHeureDebutEvenement());
        original.setDateHeureFinEvenement(evenement.getDateHeureFinEvenement());
        original.setDescriptionEvenement(evenement.getDescriptionEvenement());

        if (photoFile != null && !photoFile.isEmpty()) {
            byte[] photoBytes = photoFile.getBytes();
            String base64 = Base64.getEncoder().encodeToString(photoBytes);
            original.setImageUrlEvenement("data:image/jpeg;base64," + base64);
        }

        evenementRepository.save(original);

        redirectAttributes.addFlashAttribute("success", "Événement modifié avec succès.");
        return "redirect:/evenement";
    }






}
