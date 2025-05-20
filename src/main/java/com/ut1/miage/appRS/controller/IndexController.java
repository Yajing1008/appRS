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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        Etudiant etudiantConnecte = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiantConnecte != null) {
            // ✅ 重新从数据库加载 Etudiant 实体，确保 Hibernate session 活跃
            etudiantConnecte = etudiantRepository.findById(etudiantConnecte.getIdEtudiant()).orElse(null);
        }

        List<Post> posts;

        if (etudiantConnecte == null) {
            // ✅ 未登录用户：只看公开帖子或公开转发原帖
            posts = postRepository.findAllPublicPostsWithPublicReposts();
        } else {
            // ✅ 获取朋友列表 + 自己
            List<Etudiant> amis = new ArrayList<>(etudiantConnecte.getAmis());
            amis.add(etudiantConnecte);

            // ✅ 已登录用户：所有公开帖 + 我的私人帖 + 我的朋友的私人帖 + 朋友转发的公开帖或朋友的私密帖
            posts = postRepository.findRelativePosts(amis);


        }

        // ✅ 防止懒加载失败：初始化转发列表为空列表（防止null）
        for (Post post : posts) {
            if (post.getRepublications() == null) {
                post.setRepublications(new ArrayList<>());
            }
        }

        // ✅ 可选：添加调试日志
        System.out.println("Nombre de posts récupérés : " + posts.size());

        model.addAttribute("posts", posts);
        model.addAttribute("etudiantConnecte", etudiantConnecte);
        return "index";
    }





    @PostMapping("/publier")
    public String publierPost(
            @RequestParam(value = "contenu", required = false) String contenu,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "estPublic", required = false, defaultValue = "false") boolean estPublic,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        System.out.println("estPublic = " + estPublic);

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
            return "redirect:/";
        }

        post.setUrlsPhotosPost(urls);
        postRepository.save(post);

        redirectAttributes.addFlashAttribute("success", "Publication réussie !");
        return "redirect:/";
    }



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
        return "redirect:/";
    }


    @GetMapping("/reaction/like")
    public String toggleLike(@RequestParam Long postId, HttpSession session, RedirectAttributes redirectAttributes) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour aimer une publication.");
            return "redirect:/#post-" + postId;
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Publication introuvable.");
            return "redirect:/#post-" + postId;
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

        return "redirect:/#post-" + postId;
    }



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

        return "redirect:/#post-" + postId;
    }

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