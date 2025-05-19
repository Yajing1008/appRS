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
     * Page principale des événements :
     * - calendrier FullCalendar (événements de l'utilisateur connecté)
     * - liste des cartes d'événements pour tout le monde
     */
    @GetMapping("/evenement")
    public String afficherEvenementsEtCalendrier(Model model, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        List<Map<String, Object>> allEvents = new ArrayList<>();
        List<Evenement> mesCreations = new ArrayList<>();
        List<Evenement> mesParticipations = new ArrayList<>();

        if (etudiant != null) {
            // ✅ 获取当前用户创建的活动并按开始时间倒序排列
            mesCreations = evenementRepository.findByCreateur(etudiant)
                    .stream()
                    .sorted(Comparator.comparing(Evenement::getDateHeureDebutEvenement).reversed())
                    .toList();

            // ✅ 获取当前用户参加的活动（包括自己创建的）
            List<Evenement> participationsBrutes = evenementRepository.findByMembreGroupeContains(etudiant);

            // ✅ 过滤掉自己创建的，只保留“我参加的”，并按开始时间倒序排列
            final List<Evenement> finalMesCreations = mesCreations;
            mesParticipations = participationsBrutes.stream()
                    .filter(e -> !finalMesCreations.contains(e))
                    .sorted(Comparator.comparing(Evenement::getDateHeureDebutEvenement).reversed())
                    .toList();

            // ✅ 构建 FullCalendar JSON
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

        // ✅ 转换成 JSON 字符串传给 FullCalendar
        try {
            ObjectMapper mapper = new ObjectMapper();
            String eventJson = mapper.writeValueAsString(allEvents);
            model.addAttribute("eventJson", eventJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            model.addAttribute("eventJson", "[]");
        }

        // ✅ 添加排序后的活动列表到 model
        model.addAttribute("mesCreations", mesCreations);
        model.addAttribute("mesParticipations", mesParticipations);

        return "event";
    }




    /**
     * Formulaire de création d’un événement
     */
    @GetMapping("/evenement/creer")
    public String afficherFormulaireCreation(Model model) {
        model.addAttribute("evenement", new Evenement());
        return "event_creer";
    }


    /**
     * Traitement du formulaire de création
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

        // ✅ 检查开始时间是否在当前之后
        if (evenement.getDateHeureDebutEvenement() == null || evenement.getDateHeureDebutEvenement().isBefore(now)) {
            redirectAttributes.addFlashAttribute("error", "La date de début doit être postérieure à la date actuelle.");
            return "redirect:/evenement"; // 🔁 返回活动总览页
        }

        // ✅ 检查结束时间是否在开始时间之后
        if (evenement.getDateHeureFinEvenement() == null || evenement.getDateHeureFinEvenement().isBefore(evenement.getDateHeureDebutEvenement())) {
            redirectAttributes.addFlashAttribute("error", "La date de fin doit être postérieure à la date de début.");
            return "redirect:/evenement"; // 🔁 返回活动总览页
        }

        // ✅ 如果上传了活动图片，则设置到活动
        if (photoFile != null && !photoFile.isEmpty()) {
            byte[] photoBytes = photoFile.getBytes();
            String base64 = Base64.getEncoder().encodeToString(photoBytes);
            evenement.setImageUrlEvenement("data:image/jpeg;base64," + base64);
        }

        // ✅ 设置创建者并保存事件
        evenement.setCreateur(etudiant);
        evenementRepository.save(evenement);

        redirectAttributes.addFlashAttribute("success", "Événement créé avec succès.");
        return "redirect:/evenement";
    }





    // 构造函数（可选）
    public EvenementController(EvenementRepository evenementRepository) {
        this.evenementRepository = evenementRepository;
    }

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

        // ✅ 验证是否是创建者
        if (!evenement.getCreateur().getIdEtudiant().equals(etudiant.getIdEtudiant())) {
            redirectAttributes.addFlashAttribute("error", "Vous n'êtes pas le créateur de cet événement.");
            return "redirect:/evenement";
        }

        // ✅ 检查是否已开始（不能取消已开始或已结束的活动）
        if (evenement.getDateHeureDebutEvenement().isBefore(java.time.LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "L'événement a déjà commencé, il ne peut plus être annulé.");
            return "redirect:/evenement";
        }

        evenementRepository.delete(evenement);
        redirectAttributes.addFlashAttribute("success", "Événement annulé avec succès.");
        return "redirect:/evenement";
    }




    @GetMapping("/evenement/liste")
    public String listerTousLesEvenements(Model model, HttpSession session) {
        Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");

        // 获取当前时间之后的所有活动并按开始时间升序排序
        List<Evenement> evenementsFuturs = evenementRepository
                .findByDateHeureFinEvenementAfterOrderByDateHeureDebutEvenementAsc(java.time.LocalDateTime.now());

        model.addAttribute("utilisateurConnecte", utilisateurConnecte);
        model.addAttribute("evenementsFiltres", evenementsFuturs);

        return "event_rejoindre";
    }

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

        // ✅ 更稳健的重复加入检查
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

        // 检查是否已结束
        if (evenement.getDateHeureFinEvenement().isBefore(java.time.LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "L'événement est déjà terminé, vous ne pouvez plus le quitter.");
            return "redirect:/evenement";
        }

        // 检查是否是参与者
        boolean estParticipant = evenement.getMembreGroupe().stream()
                .anyMatch(e -> e.getIdEtudiant().equals(utilisateur.getIdEtudiant()));

        if (!estParticipant) {
            redirectAttributes.addFlashAttribute("error", "Vous ne participez pas à cet événement.");
            return "redirect:/evenement";
        }

        // 从成员列表中移除用户
        evenement.getMembreGroupe().removeIf(e -> e.getIdEtudiant().equals(utilisateur.getIdEtudiant()));
        evenementRepository.save(evenement);

        redirectAttributes.addFlashAttribute("success", "Vous avez quitté l'événement avec succès.");
        return "redirect:/evenement";
    }

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

        // ✅ 验证是否是创建者
        if (!original.getCreateur().getIdEtudiant().equals(etudiant.getIdEtudiant())) {
            redirectAttributes.addFlashAttribute("erreur", "Vous n'êtes pas le créateur de cet événement.");
            return "redirect:/evenement";
        }

        // ✅ 验证时间合法性
        LocalDateTime now = LocalDateTime.now();
        if (evenement.getDateHeureDebutEvenement() == null || evenement.getDateHeureDebutEvenement().isBefore(now)) {
            redirectAttributes.addFlashAttribute("error", "La date de début doit être postérieure à la date actuelle.");
            return "redirect:/evenement";
        }

        if (evenement.getDateHeureFinEvenement() == null || evenement.getDateHeureFinEvenement().isBefore(evenement.getDateHeureDebutEvenement())) {
            redirectAttributes.addFlashAttribute("error", "La date de fin doit être postérieure à la date de début.");
            return "redirect:/evenement";
        }

        // ✅ 更新字段
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
