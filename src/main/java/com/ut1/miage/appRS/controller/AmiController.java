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
	public String showAmiPage(Model model, HttpSession session) {
		
		Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
		
		if (utilisateurConnecte == null) {
			return "connexion";
		}
		
		List<Etudiant> allEtudiants = etudiantRepository.findAllExceptSelf(utilisateurConnecte.getIdEtudiant());
		List<Etudiant> amis = etudiantRepository.findFriends(utilisateurConnecte.getIdEtudiant());
		
		session.setAttribute("allEtudiants", allEtudiants);
		session.setAttribute("amis", amis);
		return "ami";
	}
	
	
	@GetMapping("/searchAmis")
	public String searchAmis(@RequestParam String search, HttpSession session, Model model) {
		Etudiant etudiantConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
		System.out.println("search = " + search);
		List<Etudiant> etudiantsDB = etudiantRepository.searchEtudiantsExceptSelf(search, etudiantConnecte.getIdEtudiant());

		model.addAttribute("etudiants_trouves", etudiantsDB);
		return "ami";
	}
	
}



