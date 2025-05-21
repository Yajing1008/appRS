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
     * Affiche la page de profil de l'étudiant connecté.
     *
     * Ce contrôleur récupère les informations de l'étudiant actuellement connecté depuis la session,
     * puis charge ses publications et ses repartages (républications), en les combinant et en les triant
     * par date de publication ou de republication.
     * Les dates sont formatées en français pour l'affichage.
     *
     * @param model l'objet Model utilisé pour transmettre les attributs à la vue
     * @param session la session HTTP contenant les informations de l'étudiant connecté
     * @return le nom de la vue "profil" à afficher
     */
    @GetMapping("/profil")
    public String afficherProfil(Model model, HttpSession session) {
        Etudiant etudiantSession = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiantSession == null) {
            model.addAttribute("messageConnexion", "Veuillez vous connecter pour voir vos publications.");
            model.addAttribute("etudiant", null);
            model.addAttribute("posts", Collections.emptyList());
            model.addAttribute("postDates", Collections.emptyMap());
            model.addAttribute("isOwner", false); // 🔒 安全：未登录当然不是本人
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

        // ✅ 判断是否是本人查看自己的页面
        boolean isOwner = etudiantSession.getIdEtudiant().equals(etudiant.getIdEtudiant());
        model.addAttribute("isOwner", isOwner);

        // 查找帖子
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

        // ⚠️ 手动初始化懒加载集合
        etudiant.getUniversites().size();
        etudiant.getCentresInteret().size();
        etudiant.getAmis().size(); // 如果前端页面用到了这个字段

        model.addAttribute("etudiant", etudiant);

        // 是否本人
        boolean isOwner = etudiantSession != null && etudiantSession.getIdEtudiant().equals(id);
        model.addAttribute("isOwner", isOwner);

        // 是否为好友（注意懒加载初始化）
        boolean isFriend = false;
        if (etudiantSession != null) {
            // ✅ 再次从数据库加载 etudiantSession 以初始化 amis
            Optional<Etudiant> optSession = etudiantRepository.findById(etudiantSession.getIdEtudiant());
            if (optSession.isPresent()) {
                Etudiant sessionLoaded = optSession.get();
                sessionLoaded.getAmis().size(); // 初始化
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
     * Si l'étudiant n'est pas connecté, une vue vide avec un message sera affichée.
     * Sinon, ses publications et republications seront récupérées,
     * triées par date, et passées au modèle avec des dates formatées.
     *
     * @param model le modèle pour transmettre les données à la vue
     * @param session la session HTTP contenant l'étudiant connecté
     * @return le nom de la vue "profil_modifier"
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
        List<CentreInteret> tousCentresInteret = centreInteretRepository.findAll(); // ✅ 新增

        model.addAttribute("toutesUniversites", toutesUniversites);
        model.addAttribute("tousCentresInteret", tousCentresInteret); // ✅ 传递兴趣列表
        model.addAttribute("posts", posts);
        model.addAttribute("postDates", postDates);

        return "profil_modifier";
    }




    /**
     * Enregistre les modifications apportées au profil de l'étudiant connecté.
     *
     * Cette méthode récupère les nouvelles informations de l'étudiant depuis le formulaire,
     * puis met à jour les champs correspondants dans la base de données. Si une nouvelle photo
     * est envoyée, elle est convertie en Base64 et stockée également. Une fois la mise à jour
     * effectuée, l'étudiant modifié est replacé dans la session.
     *
     * @param photoFile le fichier de photo envoyé via le formulaire
     * @param session la session HTTP en cours
     * @param universiteNoms les noms d'universités
     * @return une redirection vers la page de profil
     * @throws IOException en cas d’erreur de lecture du fichier photo
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

        // ✅ 更新基本信息
        existingEtudiant.setNomEtudiant(etudiantForm.getNomEtudiant());
        existingEtudiant.setPrenomEtudiant(etudiantForm.getPrenomEtudiant());
        existingEtudiant.setEmailEtudiant(etudiantForm.getEmailEtudiant());
        existingEtudiant.setDateNaissanceEtudiant(etudiantForm.getDateNaissanceEtudiant());
        existingEtudiant.setSexeEtudiant(etudiantForm.getSexeEtudiant());
        existingEtudiant.setDescriptionEtudiant(etudiantForm.getDescriptionEtudiant());

        // ✅ 上传头像（如果上传了）
        if (!photoFile.isEmpty()) {
            byte[] photoBytes = photoFile.getBytes();
            String base64 = Base64.getEncoder().encodeToString(photoBytes);
            existingEtudiant.setPhotoEtudiant("data:image/jpeg;base64," + base64);
        }

        // ✅ 保存大学（如果不为空）
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

        // ✅ 保存兴趣爱好（如果不为空）
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
                // 创建目标目录（static/uploads）
                File uploadDir = new File(projectDir + "/src/main/resources/static/uploads");
                if (!uploadDir.exists()) uploadDir.mkdirs();

                for (MultipartFile file : images) {
                    if (!file.isEmpty()) {
                        // 使用时间戳防止重名
                        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        File dest = new File(uploadDir, filename);
                        file.transferTo(dest);

                        // 浏览器访问路径：/uploads/filename
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

        // 构造复合主键
        RepublierId id = new RepublierId(postId, etudiant.getIdEtudiant());

        // 创建 republication 实体
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
        Optional<Reagir> existing = reagirRepository.findById(id);

        if (existing.isPresent() && "Like".equals(existing.get().getStatut())) {
            reagirRepository.delete(existing.get());
            redirectAttributes.addFlashAttribute("success", "Like retiré.");
        } else {
            // 删除旧反应（如果是 Favori）
            existing.ifPresent(reagirRepository::delete);

            Reagir r = new Reagir();
            r.setPost(post);
            r.setEtudiant(etudiant);
            r.setStatut("Like");
            reagirRepository.save(r);
            redirectAttributes.addFlashAttribute("success", "Publication aimée !");
        }

        return "redirect:/profil#post-" + postId;
    }



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
        ReagirId id = new ReagirId(post.getIdPost(), etudiant.getIdEtudiant());
        Optional<Reagir> existingReaction = reagirRepository.findById(id);

        if (existingReaction.isPresent() && "Favori".equals(existingReaction.get().getStatut())) {
            reagirRepository.delete(existingReaction.get());
            redirectAttributes.addFlashAttribute("success", "Favori supprimé.");
        } else {
            // 删除旧反应（如果是 Like）
            existingReaction.ifPresent(reagirRepository::delete);

            Reagir reaction = new Reagir();
            reaction.setPost(post);
            reaction.setEtudiant(etudiant);
            reaction.setStatut("Favori");
            reagirRepository.save(reaction);
            redirectAttributes.addFlashAttribute("success", "Ajouté aux favoris !");
        }

        return "redirect:/profil#post-" + postId;
    }

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

        // 🧾 当前用户是否是这个帖子的作者
        boolean isAuteur = post.getEtudiant().getIdEtudiant().equals(etudiant.getIdEtudiant());

        // 🔁 当前用户是否转发了这个帖子
        Optional<Republier> repubOpt = republierRepository.findByPostAndEtudiant(post, etudiant);

        if (isAuteur) {
            // ✅ 是原作者：先删除依赖（可用 cascade，也可手动）

            // 删除评论
            commenterRepository.deleteAllByPost(post);

            // 删除点赞、收藏（Reagir）
            reagirRepository.deleteAllByPost(post);

            // 删除所有转发
            republierRepository.deleteAllByPost(post);

            // 最后删除原始帖子
            postRepository.delete(post);

            redirectAttributes.addFlashAttribute("success", "Post supprimé avec succès !");
        } else if (repubOpt.isPresent()) {
            // ✅ 是转发者：只删除该转发
            republierRepository.delete(repubOpt.get());
            redirectAttributes.addFlashAttribute("success", "Républication supprimée !");
        } else {
            // ❌ 无权限
            redirectAttributes.addFlashAttribute("error", "Vous n'avez pas le droit de supprimer ce post.");
        }

        return "redirect:/profil";
    }












}
