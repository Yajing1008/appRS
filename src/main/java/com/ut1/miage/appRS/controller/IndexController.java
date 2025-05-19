package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class IndexController {
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @GetMapping("/")
    public String index(HttpSession session) {
        Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
        
        if (utilisateurConnecte != null) {
            List<Etudiant> amis = etudiantRepository.findFriends(utilisateurConnecte.getIdEtudiant());
            session.setAttribute("amis", amis);
        } else {
            session.setAttribute("amis", null);
        }
        
        return "index";
    }
    
}
