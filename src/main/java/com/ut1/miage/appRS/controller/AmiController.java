package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.DemandeAmi;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.DemandeAmiRepository;
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
	@Autowired
	private DemandeAmiRepository demandeAmiRepository;
	
	@GetMapping("/ami")
	public String showAmiPage(Model model, HttpSession session) {
		Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
		
		if (utilisateurConnecte == null) {
			return "connexion";
		}
		
		List<Etudiant> allEtudiants = etudiantRepository.findAllExceptSelf(utilisateurConnecte.getIdEtudiant());
		List<Etudiant> amis = etudiantRepository.findFriends(utilisateurConnecte.getIdEtudiant());
		List<DemandeAmi> demandesRecues = demandeAmiRepository.findByReceveurAndStatut(utilisateurConnecte, "EN_ATTENTE");
		
		session.setAttribute("allEtudiants", allEtudiants);
		session.setAttribute("amis", amis);
		session.setAttribute("demandesRecues", demandesRecues); // ⬅️ 关键
		
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
	
	@GetMapping("/envoyerDemande")
	public String envoyerDemande(@RequestParam Long idReceveur, HttpSession session) {
		Etudiant demandeur = (Etudiant) session.getAttribute("etudiantConnecte");
		Etudiant receveur = etudiantRepository.findById(idReceveur).orElse(null);
		
		if (receveur != null && !demandeur.equals(receveur) &&
				!demandeAmiRepository.existsByDemandeurAndReceveurAndStatut(demandeur, receveur, "EN_ATTENTE")) {
			
			DemandeAmi demande = new DemandeAmi();
			demande.setDemandeur(demandeur);
			demande.setReceveur(receveur);
			demande.setStatut("EN_ATTENTE");
			demandeAmiRepository.save(demande);
		}
		
		return "redirect:/ami";
	}
	
	// 接受好友请求
	@GetMapping("/accepterDemande")
	public String accepterDemande(@RequestParam Long idDemande, HttpSession session) {
		DemandeAmi demande = demandeAmiRepository.findById(idDemande).orElse(null);
		
		if (demande != null && "EN_ATTENTE".equals(demande.getStatut())) {
			demande.setStatut("ACCEPTEE");
			
			Etudiant e1 = demande.getDemandeur();
			Etudiant e2 = demande.getReceveur();
			
			e1.getAmis().add(e2);
			e2.getAmis().add(e1);
			
			etudiantRepository.save(e1);
			etudiantRepository.save(e2);
		}
		return "redirect:/demandes";
	}
	
	// 拒绝好友请求
	@GetMapping("/refuserDemande")
	public String refuserDemande(@RequestParam Long idDemande) {
		DemandeAmi demande = demandeAmiRepository.findById(idDemande).orElse(null);
		if (demande != null && "EN_ATTENTE".equals(demande.getStatut())) {
			demande.setStatut("REFUSEE");
		}
		return "redirect:/demandes";
	}
}



