package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.DemandeAmi;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.DemandeAmiRepository;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
		List<DemandeAmi> demandesEnvoyees = demandeAmiRepository.findByDemandeurAndStatut(utilisateurConnecte, "EN_ATTENTE");
		
		Set<Long> idsReceveursEnAttente = demandesEnvoyees.stream()
				.map(d -> d.getReceveur().getIdEtudiant())
				.collect(Collectors.toSet());
		
		Set<Long> idsDemandeursEnAttente = demandesRecues.stream()
				.map(d -> d.getDemandeur().getIdEtudiant())
				.collect(Collectors.toSet());
		
		session.setAttribute("allEtudiants", allEtudiants);
		session.setAttribute("amis", amis);
		session.setAttribute("demandesRecues", demandesRecues);
		session.setAttribute("demandesEnvoyees", demandesEnvoyees);
		session.setAttribute("idsReceveursEnAttente", idsReceveursEnAttente);
		session.setAttribute("idsDemandeursEnAttente", idsDemandeursEnAttente);
		
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
			demandeAmiRepository.save(demande);
		}
		Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
		List<DemandeAmi> nouvellesDemandes = demandeAmiRepository.findByReceveurAndStatut(utilisateurConnecte, "EN_ATTENTE");
		session.setAttribute("demandesRecues", nouvellesDemandes);
		
		return "redirect:/ami";
	}
	@GetMapping("/refuserDemande")
	public String refuserDemande(@RequestParam Long idDemande, HttpSession session) {
		DemandeAmi demande = demandeAmiRepository.findById(idDemande).orElse(null);
		
		if (demande != null && "EN_ATTENTE".equals(demande.getStatut())) {
			demande.setStatut("REFUSEE");
			demandeAmiRepository.save(demande);
		}
		
		Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
		List<DemandeAmi> nouvellesDemandes = demandeAmiRepository.findByReceveurAndStatut(utilisateurConnecte, "EN_ATTENTE");
		session.setAttribute("demandesRecues", nouvellesDemandes);
		
		return "redirect:/ami";
	}
	
	@GetMapping("/supprimerAmi")
	@Transactional
	public String supprimerAmi(@RequestParam Long id, HttpSession session) {
		Etudiant etudiantConnecteSession = (Etudiant) session.getAttribute("etudiantConnecte");
		
		// 用ID重新加载连接对象，确保在Hibernate Session中
		Etudiant utilisateurConnecte = etudiantRepository.findById(etudiantConnecteSession.getIdEtudiant()).orElse(null);
		Etudiant ami = etudiantRepository.findById(id).orElse(null);
		
		if (utilisateurConnecte != null && ami != null) {
			utilisateurConnecte.getAmis().remove(ami);
			ami.getAmis().remove(utilisateurConnecte);
			etudiantRepository.save(utilisateurConnecte);
			etudiantRepository.save(ami);
		}
		
		return "redirect:/";
	}
	
	
}



