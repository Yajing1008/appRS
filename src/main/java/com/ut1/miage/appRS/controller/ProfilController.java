package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.*;
import com.ut1.miage.appRS.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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





    @GetMapping("/profil")
    public String afficherProfil(Model model, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        // 将 etudiant 传入页面，不论是否为空，用于左侧用户信息
        model.addAttribute("etudiant", etudiant);

        // 如果未登录，传提示信息到页面，由 Thymeleaf 负责显示
        if (etudiant == null) {
            model.addAttribute("messageConnexion", "Veuillez vous connecter pour voir vos publications.");
            return "profil"; // 不跳转，而是展示提示
        }

        // ✅ 获取我发布的帖子
        List<Post> postsPublies = postRepository.findByEtudiantOrderByDatePublicationPostDesc(etudiant);
        // ✅ 获取我转发的帖子
        List<Republier> republications = republierRepository.findByEtudiantOrderByDateRepublicationDesc(etudiant);
        List<Post> postsRepartages = republications.stream()
                .map(Republier::getPost)
                .toList();

        // ✅ 合并并去重（按时间顺序，你也可以改成只按时间排序）
        Set<Post> posts = new LinkedHashSet<>();
        posts.addAll(postsRepartages); // 转发放前面
        posts.addAll(postsPublies);

        model.addAttribute("posts", posts);
        return "profil";
    }


    /**
     * Affiche le formulaire de modification du profil.
     *
     * @param model     Modèle utilisé pour transmettre les données à la vue.
     * @param session Session HTTP permettant de récupérer l'étudiant connecté.
     * @return Le nom de la vue du formulaire d’édition, ici "profil_modifier".
     */

    @GetMapping("/profil/modifier")
    public String showEditForm(Model model, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        model.addAttribute("etudiant", etudiant);

        // 若用户未登录，只渲染空数据（可选）
        if (etudiant == null) {
            model.addAttribute("posts", Collections.emptyList());
            return "profil_modifier";
        }

        // 获取我发布的帖子
        List<Post> postsPublies = postRepository.findByEtudiantOrderByDatePublicationPostDesc(etudiant);

        // 获取我转发的帖子
        List<Republier> republications = republierRepository.findByEtudiantOrderByDateRepublicationDesc(etudiant);
        List<Post> postsRepartages = republications.stream()
                .map(Republier::getPost)
                .toList();

        // 合并并去重（保留顺序）
        Set<Post> posts = new LinkedHashSet<>();
        posts.addAll(postsRepartages);
        posts.addAll(postsPublies);

        model.addAttribute("posts", posts);
        return "profil_modifier";
    }


    /**
     * Enregistre les modifications du profil étudiant.
     *
     * @param etudiant   L'objet étudiant contenant les données modifiées.
     * @param photoFile  Le fichier photo envoyé via le formulaire (optionnel).
     * @return Le nom de la vue à afficher après sauvegarde, ici "profil".
     * @throws IOException En cas d’erreur lors de la lecture du fichier photo.
     */

    @PostMapping("/profil/modifier")
    public String saveProfile(@ModelAttribute Etudiant etudiant,
                              @RequestParam("photo") MultipartFile photoFile,
                              HttpSession session) throws IOException {

        System.out.println(etudiant);
        // 🔍 先查找这个学生
        Optional<Etudiant> optionalEtudiant =  etudiantRepository.findByEmailEtudiant(etudiant.getEmailEtudiant());

        if (optionalEtudiant.isPresent()) {
            Etudiant existingEtudiant = optionalEtudiant.get();

            // 更新字段
            existingEtudiant.setNomEtudiant(etudiant.getNomEtudiant());
            existingEtudiant.setPrenomEtudiant(etudiant.getPrenomEtudiant());
            existingEtudiant.setEmailEtudiant(etudiant.getEmailEtudiant());
            existingEtudiant.setDateNaissanceEtudiant(etudiant.getDateNaissanceEtudiant());
            existingEtudiant.setSexeEtudiant(etudiant.getSexeEtudiant());
            existingEtudiant.setDescriptionEtudiant(etudiant.getDescriptionEtudiant());

            // 更新照片（如果上传了新照片）
            if (!photoFile.isEmpty()) {
                byte[] photoBytes = photoFile.getBytes();
                String base64 = Base64.getEncoder().encodeToString(photoBytes);
                existingEtudiant.setPhotoEtudiant("data:image/jpeg;base64," + base64);
            }

            // 保存更新后的实体
            etudiantRepository.save(existingEtudiant);
            session.setAttribute("etudiantConnecte", existingEtudiant);
        }


        return "redirect:/profil";
    }


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
