package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.*;
import com.ut1.miage.appRS.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class ProfilController {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RepublierRepository republierRepository;

    @Autowired
    private ReagirRepository reagirRepository;

    @Autowired
    private CommenterRepository commenterRepository;

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private CentreInteretRepository centreInteretRepository;

    @InitBinder("etudiantForm")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("universites", "centresInteret");
    }


    /**
     * Affiche le profil de l'étudiant actuellement connecté.
     *
     * Ce contrôleur récupère l'étudiant depuis la session et affiche son profil personnel,
     * ses publications et ses republications. Il détermine également si l'utilisateur
     * est bien le propriétaire du profil (isOwner).
     *
     * Si aucun étudiant n'est connecté, un message invitant à se connecter est affiché,
     * et le modèle est rempli avec des valeurs vides pour éviter les erreurs d'affichage.
     *
     * @param model   Le modèle pour transmettre les données à la vue.
     * @param session La session HTTP contenant l'étudiant connecté.
     * @return La page de profil ("profil.html").
     */
    @GetMapping("/profil")
    public String afficherProfil(Model model, HttpSession session) {
        Etudiant etudiantSession = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiantSession == null) {
            model.addAttribute("messageConnexion", "Veuillez vous connecter pour voir vos publications.");
            model.addAttribute("etudiant", null);
            model.addAttribute("posts", Collections.emptyList());
            model.addAttribute("postDates", Collections.emptyMap());
            model.addAttribute("isOwner", false); //  安全：未登录当然不是本人
            return "profil";
        }

        Optional<Etudiant> optionalEtudiant = etudiantRepository.findById(etudiantSession.getIdEtudiant());

        if (optionalEtudiant.isEmpty()) {
            model.addAttribute("messageConnexion", "Profil introuvable.");
            model.addAttribute("isOwner", false);
            return "profil";
        }

        Etudiant etudiant = optionalEtudiant.get();
        etudiant.getUniversites().size();
        etudiant.getCentresInteret().size();

        model.addAttribute("etudiant", etudiant);


        boolean isOwner = etudiantSession.getIdEtudiant().equals(etudiant.getIdEtudiant());
        model.addAttribute("isOwner", isOwner);


        List<Post> postsPublies = postRepository.findByEtudiantOrderByDatePublicationPostDesc(etudiant);
        List<Republier> republications = republierRepository.findByEtudiantOrderByDateRepublicationDesc(etudiant);
        List<Post> postsRepartages = republications.stream().map(Republier::getPost).toList();

        Set<Post> posts = new LinkedHashSet<>();
        posts.addAll(postsRepartages);
        posts.addAll(postsPublies);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH:mm", Locale.FRENCH);
        Map<Long, String> postDates = new HashMap<>();
        for (Post post : posts) {
            if (post.getDatePublicationPost() != null) {
                postDates.put(post.getIdPost(), post.getDatePublicationPost().format(formatter));
            }
        }

        model.addAttribute("posts", posts);
        model.addAttribute("postDates", postDates);

        return "profil";
    }

    /**
     * Affiche le profil d'un étudiant spécifique identifié par son ID.
     *
     * Ce contrôleur permet de consulter le profil d'un autre étudiant, en affichant ses
     * publications et republications selon que l'utilisateur soit le propriétaire du profil
     * ou un ami (isFriend). Si l'utilisateur n'est ni l'un ni l'autre, seuls les posts publics
     * sont visibles.
     *
     * Le modèle est enrichi avec les informations de l'étudiant consulté, la liste de ses posts,
     * les dates de publication formatées, et des indicateurs booleens indiquant si le profil
     * consulté est celui de l'utilisateur ou un ami.
     *
     * @param id      L'identifiant de l'étudiant à afficher.
     * @param model   Le modèle pour transmettre les données à la vue.
     * @param session La session HTTP contenant l'étudiant connecté.
     * @return La page de profil ("profil.html").
     */
    @GetMapping("/profil/{id}")
    public String afficherProfilParId(@PathVariable Long id, Model model, HttpSession session) {
        Etudiant etudiantSession = (Etudiant) session.getAttribute("etudiantConnecte");

        Optional<Etudiant> optionalEtudiant = etudiantRepository.findById(id);
        if (optionalEtudiant.isEmpty()) {
            model.addAttribute("messageConnexion", "Profil introuvable.");
            model.addAttribute("etudiant", null);
            model.addAttribute("posts", Collections.emptyList());
            model.addAttribute("postDates", Collections.emptyMap());
            model.addAttribute("isOwner", false);
            model.addAttribute("isFriend", false);
            return "profil";
        }

        Etudiant etudiant = optionalEtudiant.get();


        etudiant.getUniversites().size();
        etudiant.getCentresInteret().size();
        etudiant.getAmis().size();

        model.addAttribute("etudiant", etudiant);


        boolean isOwner = etudiantSession != null && etudiantSession.getIdEtudiant().equals(id);
        model.addAttribute("isOwner", isOwner);


        boolean isFriend = false;
        if (etudiantSession != null) {
            // Recharger etudiantSession depuis la base de données pour initialiser la liste des amis
            Optional<Etudiant> optSession = etudiantRepository.findById(etudiantSession.getIdEtudiant());
            if (optSession.isPresent()) {
                Etudiant sessionLoaded = optSession.get();
                sessionLoaded.getAmis().size();
                isFriend = sessionLoaded.getAmis().contains(etudiant);
            }
        }
        model.addAttribute("isFriend", isFriend);

        // 获取帖文列表
        List<Post> postsPublies;
        List<Republier> republications;
        List<Post> postsRepartages;

        if (isOwner || isFriend) {
            postsPublies = postRepository.findByEtudiantOrderByDatePublicationPostDesc(etudiant);
            republications = republierRepository.findByEtudiantOrderByDateRepublicationDesc(etudiant);
        } else {
            postsPublies = postRepository.findByEtudiantAndEstPublicPostTrueOrderByDatePublicationPostDesc(etudiant);
            republications = Collections.emptyList();
        }

        postsRepartages = republications.stream().map(Republier::getPost).toList();

        Set<Post> posts = new LinkedHashSet<>();
        posts.addAll(postsRepartages);
        posts.addAll(postsPublies);

        // 格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH:mm", Locale.FRENCH);
        Map<Long, String> postDates = new HashMap<>();
        for (Post post : posts) {
            if (post.getDatePublicationPost() != null) {
                postDates.put(post.getIdPost(), post.getDatePublicationPost().format(formatter));
            }
        }

        model.addAttribute("posts", posts);
        model.addAttribute("postDates", postDates);

        return "profil";
    }








    /**
     * Affiche le formulaire de modification du profil de l'étudiant connecté.
     *
     * Ce contrôleur prépare les données nécessaires à l'affichage du formulaire :
     * - informations personnelles de l'étudiant,
     * - liste de toutes les universités,
     * - liste de tous les centres d’intérêt,
     * - publications et republications de l'étudiant avec dates formatées.
     *
     * Si aucun étudiant n'est connecté, des valeurs vides sont transmises au modèle
     * pour éviter les erreurs d'affichage.
     *
     * @param model   Le modèle pour transmettre les données à la vue.
     * @param session La session contenant l'étudiant connecté.
     * @return La page de modification de profil ("profil_modifier.html").
     */
    @GetMapping("/profil/modifier")
    public String showEditForm(Model model, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        model.addAttribute("etudiant", etudiant);

        if (etudiant == null) {
            model.addAttribute("posts", Collections.emptyList());
            model.addAttribute("postDates", Collections.emptyMap());
            model.addAttribute("toutesUniversites", Collections.emptyList());
            model.addAttribute("tousCentresInteret", Collections.emptyList()); // ✅ 加这一行
            return "profil_modifier";
        }

        List<Post> postsPublies = postRepository.findByEtudiantOrderByDatePublicationPostDesc(etudiant);
        List<Republier> republications = republierRepository.findByEtudiantOrderByDateRepublicationDesc(etudiant);
        List<Post> postsRepartages = republications.stream().map(Republier::getPost).toList();

        Set<Post> posts = new LinkedHashSet<>();
        posts.addAll(postsRepartages);
        posts.addAll(postsPublies);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH:mm", Locale.FRENCH);
        Map<Long, String> postDates = new HashMap<>();
        for (Post post : posts) {
            if (post.getDatePublicationPost() != null) {
                postDates.put(post.getIdPost(), post.getDatePublicationPost().format(formatter));
            }
        }

        List<Universite> toutesUniversites = universiteRepository.findAll();
        List<CentreInteret> tousCentresInteret = centreInteretRepository.findAll();

        model.addAttribute("toutesUniversites", toutesUniversites);
        model.addAttribute("tousCentresInteret", tousCentresInteret);
        model.addAttribute("posts", posts);
        model.addAttribute("postDates", postDates);

        return "profil_modifier";
    }




    /**
     * Enregistre les modifications apportées au profil de l'étudiant connecté.
     *
     * Ce contrôleur met à jour :
     * - les informations personnelles de l'étudiant,
     * - la photo de profil (si fournie),
     * - les universités (créées si inexistantes),
     * - les centres d’intérêt (créés si inexistants).
     *
     * Les anciennes associations (universités et centres) sont remplacées.
     * Une fois les données sauvegardées, la session est mise à jour et
     * l'utilisateur est redirigé vers la page de profil avec un message de succès.
     *
     * @param universiteNoms      Liste des noms d'universités sélectionnées.
     * @param centresNoms         Liste des centres d’intérêt sélectionnés.
     * @param photoFile           Fichier photo du profil.
     * @param session             Session contenant l’étudiant connecté.
     * @param redirectAttributes  Attributs pour transmettre un message flash.
     * @param etudiantForm        Données du formulaire de l'étudiant modifié.
     * @return Redirection vers la page de profil.
     * @throws IOException En cas d'erreur lors du traitement du fichier photo.
     */

    @PostMapping("/profil/modifier")
    public String saveProfile(@RequestParam(value = "universites", required = false) List<String> universiteNoms,
                              @RequestParam(value = "centresInteret", required = false) List<String> centresNoms,
                              @RequestParam("photo") MultipartFile photoFile,
                              HttpSession session,
                              RedirectAttributes redirectAttributes,
                              @ModelAttribute("etudiantForm") Etudiant etudiantForm) throws IOException {

        Etudiant sessionEtudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (sessionEtudiant == null) return "redirect:/connexion";

        Optional<Etudiant> optionalEtudiant = etudiantRepository.findById(sessionEtudiant.getIdEtudiant());
        if (optionalEtudiant.isEmpty()) return "redirect:/connexion";

        Etudiant existingEtudiant = optionalEtudiant.get();

        //  Mettre à jour les informations personnelles
        existingEtudiant.setNomEtudiant(etudiantForm.getNomEtudiant());
        existingEtudiant.setPrenomEtudiant(etudiantForm.getPrenomEtudiant());
        existingEtudiant.setEmailEtudiant(etudiantForm.getEmailEtudiant());
        existingEtudiant.setDateNaissanceEtudiant(etudiantForm.getDateNaissanceEtudiant());
        existingEtudiant.setSexeEtudiant(etudiantForm.getSexeEtudiant());
        existingEtudiant.setDescriptionEtudiant(etudiantForm.getDescriptionEtudiant());

        // Télécharger la photo de profil (si fournie)
        if (!photoFile.isEmpty()) {
            byte[] photoBytes = photoFile.getBytes();
            String base64 = Base64.getEncoder().encodeToString(photoBytes);
            existingEtudiant.setPhotoEtudiant("data:image/jpeg;base64," + base64);
        }

        // Enregistrer les universités (si non vides)
        if (universiteNoms != null) {
            List<Universite> universites = new ArrayList<>();
            for (String nom : universiteNoms) {
                Universite u = universiteRepository.findByNomUnivIgnoreCase(nom)
                        .orElseGet(() -> {
                            Universite nouvelle = new Universite();
                            nouvelle.setNomUniv(nom);
                            return universiteRepository.save(nouvelle);
                        });
                universites.add(u);
            }
            existingEtudiant.setUniversites(universites);
        } else {
            existingEtudiant.setUniversites(new ArrayList<>()); // 清空
        }

        // Enregistrer les centres d’intérêt (si non vides)
        if (centresNoms != null) {
            List<CentreInteret> centres = new ArrayList<>();
            for (String nom : centresNoms) {
                CentreInteret c = centreInteretRepository.findByNomCentreInteretIgnoreCase(nom)
                        .orElseGet(() -> {
                            CentreInteret nouveau = new CentreInteret();
                            nouveau.setNomCentreInteret(nom);
                            return centreInteretRepository.save(nouveau);
                        });
                centres.add(c);
            }
            existingEtudiant.setCentresInteret(centres);
        } else {
            existingEtudiant.setCentresInteret(new ArrayList<>()); // 清空
        }

        etudiantRepository.save(existingEtudiant);
        session.setAttribute("etudiantConnecte", existingEtudiant);

        redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès.");
        return "redirect:/profil";
    }






    /**
     * Gère la publication d’un post par un étudiant depuis la page de profil.
     *
     * Cette méthode permet à un étudiant connecté de publier un post avec du texte,
     * jusqu'à trois images, et de spécifier si le post est public ou non.
     * Elle effectue les vérifications suivantes :
     * <ul>
     *     <li>Vérifie si l’étudiant est connecté ; sinon, redirige avec un message d’erreur.</li>
     *     <li>Refuse une publication vide (ni texte ni image).</li>
     *     <li>Limite à 3 images maximum ; refuse au-delà.</li>
     *     <li>Enregistre les images localement dans <code>static/uploads</code> et stocke leurs URLs.</li>
     *     <li>Crée et sauvegarde un objet {@link Post} avec toutes les données saisies.</li>
     * </ul>
     *
     * @param contenu Le texte du post (optionnel)
     * @param images La liste des fichiers image envoyés (optionnel, max. 3 fichiers)
     * @param estPublic Indique si le post est public ou non (checkbox)
     * @param session La session HTTP permettant d’identifier l’étudiant connecté
     * @param redirectAttributes Permet d’ajouter des messages flash lors des redirections
     * @return une redirection vers la page de profil, avec message de succès ou d'erreur
     */
    @PostMapping("/profil/publier")
    public String publierPost(
            @RequestParam(value = "contenu", required = false) String contenu,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "estPublic", required = false, defaultValue = "false") boolean estPublic,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour publier.");
            return "redirect:/profil";
        }

        boolean contenuVide = (contenu == null || contenu.trim().isEmpty());
        boolean aucuneImage = (images == null || images.isEmpty());

        if (contenuVide && aucuneImage) {
            redirectAttributes.addFlashAttribute("error", "Le contenu ne peut pas être vide.");
            return "redirect:/profil";
        }

        if (images != null && images.size() > 3) {
            redirectAttributes.addFlashAttribute("error", "Maximum 3 images autorisées.");
            return "redirect:/profil";
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
                // Créer le répertoire cible (static/uploads)
                File uploadDir = new File(projectDir + "/src/main/resources/static/uploads");
                if (!uploadDir.exists()) uploadDir.mkdirs();

                for (MultipartFile file : images) {
                    if (!file.isEmpty()) {
                        // Utiliser un timestamp pour éviter les doublons de nom
                        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        File dest = new File(uploadDir, filename);
                        file.transferTo(dest);

                        // Chemin d'accès depuis le navigateur : /uploads/filename
                        urls.add("/uploads/" + filename);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'envoi des images.");
            return "redirect:/profil";
        }

        post.setUrlsPhotosPost(urls);
        postRepository.save(post);

        redirectAttributes.addFlashAttribute("success", "Publication réussie !");
        return "redirect:/profil";
    }

    /**
     * Permet à un étudiant connecté de republier un post existant.
     *
     * Ce contrôleur :
     * - vérifie si l'étudiant est connecté,
     * - vérifie si le post d'origine existe,
     * - crée une nouvelle entité de republication avec un commentaire et une visibilité (publique ou non),
     * - enregistre la republication dans la base de données,
     * - redirige vers la page de profil avec un message de succès ou d'erreur.
     *
     * @param postId             L'identifiant du post à republier.
     * @param commentaire        Le commentaire ajouté à la republication.
     * @param estPublic          Indique si la republication est publique ou non.
     * @param session            La session contenant l'étudiant connecté.
     * @param redirectAttributes Attributs pour transmettre un message flash.
     * @return Redirection vers la page de profil.
     */
    @PostMapping("/profil/republication")
    public String republier(
            @RequestParam("postId") Long postId,
            @RequestParam("commentaire") String commentaire,
            @RequestParam(value = "estPublic", defaultValue = "false") boolean estPublic,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour republier.");
            return "redirect:/profil";
        }

        Post originalPost = postRepository.findById(postId).orElse(null);
        if (originalPost == null) {
            redirectAttributes.addFlashAttribute("error", "Publication introuvable.");
            return "redirect:/profil";
        }

        // Construire une clé primaire composée
        RepublierId id = new RepublierId(postId, etudiant.getIdEtudiant());

        // Créer une entité de republication
        Republier republier = new Republier();
        republier.setId(id);
        republier.setPost(originalPost);
        republier.setEtudiant(etudiant);
        republier.setCommentaireRepublication(commentaire);
        republier.setEstPublic(estPublic);
        republier.setDateRepublication(LocalDateTime.now());

        republierRepository.save(republier);

        redirectAttributes.addFlashAttribute("success", "Républication réussie !");
        return "redirect:/profil";
    }


    /**
     * Contrôleur pour gérer l'action de like/délike sur une publication.
     *
     * Si l'utilisateur est connecté et a déjà liké le post, le like sera retiré.
     * Sinon, un like sera ajouté.
     *
     * @param postId identifiant du post à liker ou déliker
     * @param session session HTTP pour identifier l'étudiant connecté
     * @param redirectAttributes pour transmettre un message flash à la redirection
     * @return redirection vers l'ancre du post sur la page profil
     */
    @GetMapping("/profil/reaction/like")
    public String toggleLike(@RequestParam Long postId, HttpSession session, RedirectAttributes redirectAttributes) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour aimer une publication.");
            return "redirect:/profil#post-" + postId;
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Publication introuvable.");
            return "redirect:/profil#post-" + postId;
        }

        Post post = postOpt.get();
        ReagirId id = new ReagirId(post.getIdPost(), etudiant.getIdEtudiant());


        Optional<Reagir> existingLike = reagirRepository.findByPostIdAndEtudiantIdAndStatut(
                post.getIdPost(), etudiant.getIdEtudiant(), "Like");

        if (existingLike.isPresent()) {
            reagirRepository.delete(existingLike.get());
            redirectAttributes.addFlashAttribute("success", "Like retiré.");
        } else {
            Reagir r = new Reagir();
            r.setPost(post);
            r.setEtudiant(etudiant);
            r.getReagirId().setStatut("Like");
            reagirRepository.save(r);
            redirectAttributes.addFlashAttribute("success", "Publication aimée !");
        }

        return "redirect:/profil#post-" + postId;
    }



    /**
     * Permet à un utilisateur connecté d'ajouter ou de retirer une publication de ses favoris.
     *
     * - Si l'utilisateur n'est pas connecté, une redirection vers le profil est effectuée avec un message d'erreur.
     * - Si la publication n'existe pas, un message d'erreur est affiché.
     * - Si la réaction "Favori" existe déjà, elle est supprimée (fonction "toggle").
     * - Sinon, une nouvelle réaction de type "Favori" est enregistrée.
     *
     * @param postId identifiant du post à ajouter ou retirer des favoris
     * @param session session HTTP contenant l'étudiant connecté
     * @param redirectAttributes attributs de redirection pour les messages flash
     * @return redirection vers l'ancre du post sur la page de profil
     */
    @GetMapping("/profil/reaction/favori")
    public String toggleFavori(@RequestParam Long postId, HttpSession session, RedirectAttributes redirectAttributes) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour ajouter aux favoris.");
            return "redirect:/profil#post-" + postId;
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Publication introuvable.");
            return "redirect:/profil#post-" + postId;
        }

        Post post = postOpt.get();


        Optional<Reagir> existingFavori = reagirRepository.findByPostIdAndEtudiantIdAndStatut(
                post.getIdPost(), etudiant.getIdEtudiant(), "Favori");

        System.out.println("existingFavori = " + existingFavori);
        if (existingFavori.isPresent()) {
            reagirRepository.delete(existingFavori.get());
            redirectAttributes.addFlashAttribute("success", "Favori supprimé.");
        } else {
            Reagir reaction = new Reagir();
            reaction.setPost(post);
            reaction.setEtudiant(etudiant);
            reaction.getReagirId().setStatut("Favori");
            reagirRepository.save(reaction);
            redirectAttributes.addFlashAttribute("success", "Ajouté aux favoris !");
        }

        return "redirect:/profil#post-" + postId;
    }



    /**
     * Permet à un étudiant connecté de commenter une publication.
     *
     * Vérifie si l'étudiant est connecté et si le post existe.
     * Enregistre ensuite un nouveau commentaire associé à la publication.
     * Redirige vers la publication concernée avec un message flash.
     *
     * @param postId             L'identifiant de la publication commentée.
     * @param commentaire        Le contenu du commentaire.
     * @param session            La session contenant l'étudiant connecté.
     * @param redirectAttributes Attributs pour transmettre un message flash.
     * @return Redirection vers la publication sur la page de profil.
     */
    @PostMapping("/profil/commenter")
    public String commenter(@RequestParam Long postId,
                            @RequestParam String commentaire,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour commenter.");
            return "redirect:/profil#post-" + postId;
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Publication introuvable.");
            return "redirect:/profil";
        }

        Post post = postOpt.get();

        Commenter commentaireEntity = new Commenter();
        commentaireEntity.setPost(post);
        commentaireEntity.setEtudiant(etudiant);
        commentaireEntity.setCommentaire(commentaire);
        commentaireEntity.setDateHeureCommentaire(LocalDateTime.now());

        commenterRepository.save(commentaireEntity);

        redirectAttributes.addFlashAttribute("success", "Commentaire publié !");
        return "redirect:/profil#post-" + postId;
    }



    /**
     * Supprime un commentaire publié par l'étudiant connecté.
     *
     * Vérifie si l'étudiant est connecté et si le commentaire existe.
     * Seul l’auteur du commentaire est autorisé à le supprimer.
     * Redirige vers la publication concernée avec un message de succès ou d’erreur.
     *
     * @param postId             L’identifiant de la publication concernée.
     * @param idCommentaire      L’identifiant du commentaire à supprimer.
     * @param session            La session contenant l’étudiant connecté.
     * @param redirectAttributes Attributs pour transmettre un message flash.
     * @return Redirection vers la publication sur la page de profil.
     */
    @Transactional
    @PostMapping("/profil/commenter/supprimer")
    public String supprimerCommentaire(@RequestParam Long postId,
                                       @RequestParam Long idCommentaire,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {

        Etudiant etudiantConnecte = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiantConnecte == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour supprimer un commentaire.");
            return "redirect:/profil#post-" + postId;
        }

        Optional<Commenter> comOpt = commenterRepository.findById(idCommentaire);
        if (comOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Commentaire introuvable.");
            return "redirect:/profil#post-" + postId;
        }

        Commenter commentaire = comOpt.get();

        if (!commentaire.getEtudiant().getIdEtudiant().equals(etudiantConnecte.getIdEtudiant())) {
            redirectAttributes.addFlashAttribute("error", "Vous ne pouvez supprimer que vos propres commentaires.");
            return "redirect:/profil#post-" + postId;
        }

        commenterRepository.delete(commentaire);

        redirectAttributes.addFlashAttribute("success", "Commentaire supprimé !");
        return "redirect:/profil#post-" + postId;
    }



    /**
     * Supprime un post publié ou repartagé par l’étudiant connecté.
     *
     * Si l’étudiant est l’auteur du post, tous les éléments liés sont supprimés :
     * commentaires, réactions, republications, puis le post lui-même.
     * Si l’étudiant a simplement repartagé le post, seule la republication est supprimée.
     * Sinon, l’accès est refusé.
     *
     * @param postId             L’identifiant du post à supprimer.
     * @param session            La session contenant l’étudiant connecté.
     * @param redirectAttributes Attributs pour transmettre un message flash.
     * @return Redirection vers la page de profil.
     */
    @Transactional
    @PostMapping("/profil/post/supprimer")
    public String supprimerPost(@RequestParam Long postId, HttpSession session, RedirectAttributes redirectAttributes) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter.");
            return "redirect:/profil#post-" + postId;
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Publication introuvable.");
            return "redirect:/profil";
        }

        Post post = postOpt.get();

        // Vérifier si l'utilisateur est l’auteur du post
        boolean isAuteur = post.getEtudiant().getIdEtudiant().equals(etudiant.getIdEtudiant());

        // Vérifier si l'utilisateur a repartagé le post
        Optional<Republier> repubOpt = republierRepository.findByPostAndEtudiant(post, etudiant);

        if (isAuteur) {
            // Si auteur : supprimer les dépendances (manuellement ou via cascade)

            // Supprimer les commentaires
            commenterRepository.deleteAllByPost(post);

            // Supprimer les réactions (Like/Favori)
            reagirRepository.deleteAllByPost(post);

            // Supprimer les republications
            republierRepository.deleteAllByPost(post);

            // Supprimer la publication
            postRepository.delete(post);

            redirectAttributes.addFlashAttribute("success", "Post supprimé avec succès !");
        } else if (repubOpt.isPresent()) {
            // Si republicateur : supprimer uniquement la republication
            republierRepository.delete(repubOpt.get());
            redirectAttributes.addFlashAttribute("success", "Républication supprimée !");
        } else {
            // Accès non autorisé
            redirectAttributes.addFlashAttribute("error", "Vous n'avez pas le droit de supprimer ce post.");
        }

        return "redirect:/profil";
    }



}
