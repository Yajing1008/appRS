package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
public class ProfilController {

    @Autowired
    private EtudiantRepository etudiantRepository;

    /**
     * Affiche la page de profil de l'étudiant connecté.
     *
     * @param model   Modèle utilisé pour transmettre les données à la vue.
     * @param session Session HTTP permettant de récupérer l'étudiant connecté.
     * @return Le nom de la vue à afficher, ici "profil".
     */
    @GetMapping("/profil")
    public String afficherProfil(Model model, HttpSession session) {
        // 尝试从 session 获取登录的学生
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");

        // 无论是否为 null 都传入视图，由 Thymeleaf 判断是否显示内容
        model.addAttribute("etudiant", etudiant);

        return "profil";
    }



    /**
     * Affiche le formulaire de modification du profil.
     *
     * @param model     Modèle utilisé pour transmettre les données à la vue.
     * @param session Session HTTP permettant de récupérer l'étudiant connecté.
     * @return Le nom de la vue du formulaire d’édition, ici "profil_modifier".
     */
  // 显示编辑页面
    @GetMapping("/profil/modifier")
    public String showEditForm(Model model, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        model.addAttribute("etudiant", etudiant);
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

    // 保存修改
    @PostMapping("/profil/modifier")
    public String saveProfile(@ModelAttribute Etudiant etudiant,
                              @RequestParam("photo") MultipartFile photoFile) throws IOException {
        // 如果上传了图片，转换格式保存
        if (!photoFile.isEmpty()) {
            byte[] photoBytes = photoFile.getBytes();
            String base64 = Base64.getEncoder().encodeToString(photoBytes);
            etudiant.setPhotoEtudiant("data:image/jpeg;base64," + base64);
        }

        etudiantRepository.save(etudiant);
        return "profil"; // 重定向到只读页面
    }


}
