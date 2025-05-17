package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AmiController {
	
	@Autowired
	private EtudiantRepository etudiantRepository;
	
	@GetMapping("/ami")
	public String showAmiPage(@RequestParam(name = "search", required = false) String search,
	                          Model model,
	                          HttpSession session) {
		
		Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
		if (utilisateurConnecte == null) {
			return "index";//il faut modifier en connexion apres pull!!
		}
		
		List<Etudiant> resultat;
		
		if (search != null && !search.trim().isEmpty()) {
			resultat = etudiantRepository.searchEtudiantsExceptSelf(search.trim(), utilisateurConnecte.getIdEtudiant());
		} else {
			resultat = etudiantRepository.findAllExceptSelf(utilisateurConnecte.getIdEtudiant());
		}
		
		model.addAttribute("etudiants", resultat);
		model.addAttribute("amis", utilisateurConnecte.getAmis());
		
		return "ami";
	}
	
}



