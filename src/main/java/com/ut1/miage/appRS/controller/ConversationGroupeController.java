package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.EtuMessConversation;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Groupe;
import com.ut1.miage.appRS.repository.ConversationRepository;
import com.ut1.miage.appRS.repository.EtuMessConversationRepository;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import com.ut1.miage.appRS.repository.GroupeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class ConversationGroupeController {
	@Autowired
	GroupeRepository groupeRepository;
	@Autowired
	ConversationRepository conversationRepository;
	@Autowired
	EtuMessConversationRepository messageRepository;
	@Autowired
	EtudiantRepository etudiantRepository;
	@GetMapping("/conversationGroupe/{id}")
	public String afficherConversationGroupe(@PathVariable("id") Long idGroupe,
	                                         Model model,
	                                         HttpSession session) {
		
		Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
		if (utilisateurConnecte == null) {
			return "redirect:/connexion";
		}
		
		Groupe groupe = groupeRepository.findById(idGroupe).orElse(null);
		if (groupe == null || groupe.getConversation() == null) {
			return "redirect:/groupe/groupes";
		}
		
		Conversation conversation = groupe.getConversation();
		List<EtuMessConversation> messages = messageRepository.findByConversation(conversation);
		
		model.addAttribute("groupe", groupe); // 用于显示群组名、头像
		model.addAttribute("conversation", conversation);
		model.addAttribute("messages", messages);
		model.addAttribute("utilisateurConnecte", utilisateurConnecte);
		
		return "conversationGroupe";
	}
	
	@PostMapping("/sendGroupe")
	public String envoyerMessageGroupe(@RequestParam("idConversation") Long idConversation,
	                                   @RequestParam("idEtudiant") Long idEtudiant,
	                                   @RequestParam("message") String contenu,
	                                   HttpSession session) {
		
		Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
		if (etudiant == null || !etudiant.getIdEtudiant().equals(idEtudiant)) {
			return "redirect:/connexion";
		}
		
		Conversation conversation = conversationRepository.findById(idConversation).orElse(null);
		if (conversation == null) {
			return "redirect:/groupe/groupes";
		}
		
		// 创建消息对象
		EtuMessConversation message = new EtuMessConversation();
		message.setConversation(conversation);
		message.setEtudiant(etudiant);
		message.setMessage(contenu);
		message.setDateHeureMessage(LocalDateTime.now());
		
		messageRepository.save(message);
		
		// 重定向回群组聊天页面
		Groupe groupe = groupeRepository.findByConversation(conversation).orElse(null);
		if (groupe != null) {
			return "redirect:/conversationGroupe/" + groupe.getIdGroupe();
		} else {
			return "redirect:/groupe/groupes";
		}
	}
	
}
