package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.*;
import com.ut1.miage.appRS.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Contrôleur principal de l'application, gérant l'affichage du fil d'actualité,
 * la publication de posts, les réactions (like/favori), les commentaires
 * ainsi que les republications.
 */
@Controller
public class IndexController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RepublierRepository republierRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ReagirRepository reagirRepository;

    @Autowired
    private CommenterRepository commenterRepository;

    @Autowired
    private ParticiperRepository participerRepository;

    /**
     * Affiche la page d’accueil (fil d’actualité) avec les publications relatives à l’utilisateur connecté.
     *
     * @param model   modèle pour passer les données à la vue
     * @param session session utilisateur
     * @return nom de la vue "index"
     */
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        Etudiant etudiantConnecte = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiantConnecte != null) {
            etudiantConnecte = etudiantRepository.findById(etudiantConnecte.getIdEtudiant()).orElse(null);
            List<Etudiant> amis = new ArrayList<>(etudiantConnecte.getAmis());
            List<Groupe> groupes = new ArrayList<>(etudiantConnecte.getGroupesCrees());
            List<Participer> participations = participerRepository.findByEtudiant_IdEtudiant(etudiantConnecte.getIdEtudiant());
            List<Groupe> groupesMembre = participations.stream().map(Participer::getGroupe).toList();
            groupes.addAll(groupesMembre);
            session.setAttribute("amis", amis);
            session.setAttribute("mesGroupes", groupes);
        } else {
            session.setAttribute("amis", null);
            session.setAttribute("mesGroupes", null);
        }

        List<Post> posts;
        if (etudiantConnecte == null) {
            posts = postRepository.findAllPublicPostsWithPublicReposts();
        } else {
            List<Etudiant> amis = new ArrayList<>(etudiantConnecte.getAmis());
            amis.add(etudiantConnecte);
            posts = postRepository.findRelativePosts(amis);
        }

        for (Post post : posts) {
            if (post.getRepublications() == null) {
                post.setRepublications(new ArrayList<>());
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH:mm", Locale.FRENCH);
        Map<Long, String> postDates = new HashMap<>();
        for (Post post : posts) {
            if (post.getDatePublicationPost() != null) {
                String formatted = post.getDatePublicationPost().format(formatter);
                postDates.put(post.getIdPost(), formatted);
            }
        }

        model.addAttribute("posts", posts);
        model.addAttribute("postDates", postDates);
        model.addAttribute("etudiantConnecte", etudiantConnecte);
        return "index";
    }

    /**
     * Gère la publication d’un nouveau post avec contenu et/ou images.
     *
     * @param contenu            texte du post
     * @param images             fichiers image uploadés
     * @param estPublic          visibilité du post
     * @param session            session utilisateur
     * @param redirectAttributes pour afficher des messages flash
     * @return redirection vers la page d'accueil
     */
    @PostMapping("/publier")
    public String publierPost(
            @RequestParam(value = "contenu", required = false) String contenu,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "estPublic", required = false, defaultValue = "false") boolean estPublic,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour publier.");
            return "redirect:/";
        }

        boolean contenuVide = (contenu == null || contenu.trim().isEmpty());
        boolean aucuneImage = (images == null || images.isEmpty());

        if (contenuVide && aucuneImage) {
            redirectAttributes.addFlashAttribute("error", "Le contenu ne peut pas être vide.");
            return "redirect:/";
        }

        if (images != null && images.size() > 3) {
            redirectAttributes.addFlashAttribute("error", "Maximum 3 images autorisées.");
            return "redirect:/";
        }

        Post post = new Post();
        post.setContenuPost(contenu);
        post.setEstPublicPost(estPublic);
        post.setDatePublicationPost(LocalDateTime.now());
        post.setEtudiant(etudiant);
        List<String> urls = new ArrayList<>();
        String projectDir = System.getProperty("user.dir");

        try {
            if (images != null) {
                File uploadDir = new File(projectDir + "/src/main/resources/static/uploads");
                if (!uploadDir.exists()) uploadDir.mkdirs();

                for (MultipartFile file : images) {
                    if (!file.isEmpty()) {
                        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        File dest = new File(uploadDir, filename);
                        file.transferTo(dest);
                        urls.add("/uploads/" + filename);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'envoi des images.");
            return "redirect:/";
        }

        post.setUrlsPhotosPost(urls);
        postRepository.save(post);

        redirectAttributes.addFlashAttribute("success", "Publication réussie !");
        return "redirect:/";
    }

    /**
     * Permet de republier un post avec un commentaire et une visibilité.
     *
     * @param postId             identifiant du post original
     * @param commentaire        commentaire de republication
     * @param estPublic          visibilité de la republication
     * @param session            session utilisateur
     * @param redirectAttributes messages flash pour la vue
     * @return redirection vers la page d'accueil
     */
    @PostMapping("/republication")
    public String republier(
            @RequestParam("postId") Long postId,
            @RequestParam("commentaire") String commentaire,
            @RequestParam(value = "estPublic", defaultValue = "false") boolean estPublic,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour republier.");
            return "redirect:/";
        }

        Post originalPost = postRepository.findById(postId).orElse(null);
        if (originalPost == null) {
            redirectAttributes.addFlashAttribute("error", "Publication introuvable.");
            return "redirect:/";
        }

        RepublierId id = new RepublierId(postId, etudiant.getIdEtudiant());

        Republier republier = new Republier();
        republier.setId(id);
        republier.setPost(originalPost);
        republier.setEtudiant(etudiant);
        republier.setCommentaireRepublication(commentaire);
        republier.setEstPublic(estPublic);
        republier.setDateRepublication(LocalDateTime.now());

        republierRepository.save(republier);

        redirectAttributes.addFlashAttribute("success", "Républication réussie !");
        return "redirect:/";
    }

    /**
     * Active ou désactive un like sur un post.
     */
    @GetMapping("/reaction/like")
    public String toggleLike(@RequestParam Long postId, HttpSession session, RedirectAttributes redirectAttributes) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour aimer une publication.");
            return "redirect:/#post-" + postId;
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        System.out.println("postOpt = " + postOpt);
        if (postOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Publication introuvable.");
            return "redirect:/#post-" + postId;
        }

        Post post = postOpt.get();
        ReagirId id = new ReagirId(post.getIdPost(), etudiant.getIdEtudiant(), "Like");
        Optional<Reagir> existing = reagirRepository.findById(id);

        if (existing.isPresent() && "Like".equals(existing.get().getReagirId().getStatut())) {
            reagirRepository.delete(existing.get());
            redirectAttributes.addFlashAttribute("success", "Like retiré.");
        } else {
            existing.ifPresent(reagirRepository::delete);

            Reagir r = new Reagir();
            r.setPost(post);
            r.setEtudiant(etudiant);
            r.getReagirId().setStatut("Like");
            reagirRepository.save(r);
            redirectAttributes.addFlashAttribute("success", "Publication aimée !");
        }

        return "redirect:/#post-" + postId;
    }

    /**
     * Active ou désactive un favori sur un post.
     */
    @GetMapping("/reaction/favori")
    public String toggleFavori(@RequestParam Long postId, HttpSession session, RedirectAttributes redirectAttributes) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour ajouter aux favoris.");
            return "redirect:/#post-" + postId;
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Publication introuvable.");
            return "redirect:/#post-" + postId;
        }

        Post post = postOpt.get();
        ReagirId id = new ReagirId(post.getIdPost(), etudiant.getIdEtudiant(), "Favori");
        Optional<Reagir> existingReaction = reagirRepository.findById(id);

        if (existingReaction.isPresent() && "Favori".equals(existingReaction.get().getReagirId().getStatut())) {
            reagirRepository.delete(existingReaction.get());
            redirectAttributes.addFlashAttribute("success", "Favori supprimé.");
        } else {
            existingReaction.ifPresent(reagirRepository::delete);

            Reagir reaction = new Reagir();
            reaction.setPost(post);
            reaction.setEtudiant(etudiant);
            reaction.getReagirId().setStatut("Favori");
            reagirRepository.save(reaction);
            redirectAttributes.addFlashAttribute("success", "Ajouté aux favoris !");
        }

        return "redirect:/#post-" + postId;
    }

    /**
     * Permet à l'utilisateur de commenter un post.
     */
    @PostMapping("/commenter")
    public String commenter(@RequestParam Long postId,
                            @RequestParam String commentaire,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour commenter.");
            return "redirect:/#post-" + postId;
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Publication introuvable.");
            return "redirect:/";
        }

        Post post = postOpt.get();

        Commenter commentaireEntity = new Commenter();
        commentaireEntity.setPost(post);
        commentaireEntity.setEtudiant(etudiant);
        commentaireEntity.setCommentaire(commentaire);
        commentaireEntity.setDateHeureCommentaire(LocalDateTime.now());

        commenterRepository.save(commentaireEntity);

        redirectAttributes.addFlashAttribute("success", "Commentaire publié !");
        return "redirect:/#post-" + postId;
    }

    /**
     * Supprime un commentaire si l'utilisateur en est l'auteur.
     */
    @Transactional
    @PostMapping("/commenter/supprimer")
    public String supprimerCommentaire(@RequestParam Long postId,
                                       @RequestParam Long idCommentaire,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {

        Etudiant etudiantConnecte = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiantConnecte == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour supprimer un commentaire.");
            return "redirect:/#post-" + postId;
        }

        Optional<Commenter> comOpt = commenterRepository.findById(idCommentaire);
        if (comOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Commentaire introuvable.");
            return "redirect:/#post-" + postId;
        }

        Commenter commentaire = comOpt.get();

        if (!commentaire.getEtudiant().getIdEtudiant().equals(etudiantConnecte.getIdEtudiant())) {
            redirectAttributes.addFlashAttribute("error", "Vous ne pouvez supprimer que vos propres commentaires.");
            return "redirect:/#post-" + postId;
        }

        commenterRepository.delete(commentaire);

        redirectAttributes.addFlashAttribute("success", "Commentaire supprimé !");
        return "redirect:/#post-" + postId;
    }
}
