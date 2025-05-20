package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.EtuMessConversation;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.ConversationRepository;
import com.ut1.miage.appRS.repository.EtuMessConversationRepository;
import com.ut1.miage.appRS.repository.EtudiantRepository;
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
public class ConversationController {
	@Autowired
	EtudiantRepository etudiantRepository;
	@Autowired
	ConversationRepository conversationRepository;
	@Autowired
	EtuMessConversationRepository messageRepository;
	@GetMapping("/conversation/{id}")
	public String openConversation(@PathVariable("id") Long idEtudiant,
	                               Model model,
	                               HttpSession session) {
		
		Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
		if (utilisateurConnecte == null) {
			return "redirect:/connexion";
		}
		
		// 尝试找两人共有的 conversation（必须在同一 conversation 下两人都发过消息）
		List<Conversation> allConversations = conversationRepository.findAll();
		Conversation conversation = null;
		
		for (Conversation c : allConversations) {
			List<EtuMessConversation> messages = messageRepository.findByConversation(c);
			boolean contientUtilisateur = messages.stream()
					.anyMatch(m -> m.getEtudiant().getIdEtudiant().equals(utilisateurConnecte.getIdEtudiant()));
			boolean contientAmi = messages.stream()
					.anyMatch(m -> m.getEtudiant().getIdEtudiant().equals(idEtudiant));
			if (contientUtilisateur && contientAmi) {
				conversation = c;
				break;
			}
		}
		
		if (conversation == null) {
			// 创建新会话
			conversation = new Conversation();
			conversation.setDateCommenceConversation(LocalDateTime.now());
			conversationRepository.save(conversation);
			
			// 创建 "开始聊天！" 消息
			EtuMessConversation msg1 = new EtuMessConversation();
			msg1.setEtudiant(utilisateurConnecte);
			msg1.setConversation(conversation);
			msg1.setMessage("Start to chat!");
			msg1.setDateHeureMessage(LocalDateTime.now());
			messageRepository.save(msg1);
			
			Etudiant amiInit = etudiantRepository.findById(idEtudiant).orElseThrow();
			
			EtuMessConversation msg2 = new EtuMessConversation();
			msg2.setEtudiant(amiInit);
			msg2.setConversation(conversation);
			msg2.setMessage("Start to chat!");
			msg2.setDateHeureMessage(LocalDateTime.now());
			messageRepository.save(msg2);
		}
		
		Etudiant ami = etudiantRepository.findById(idEtudiant).orElseThrow();
		List<EtuMessConversation> messages = messageRepository.findByConversation(conversation);
		
		model.addAttribute("ami", ami);
		model.addAttribute("conversation", conversation);
		model.addAttribute("messages", messages);
		model.addAttribute("utilisateurConnecte", utilisateurConnecte);
		
		return "conversation";
	}
	@PostMapping("/send")
	public String sendMessage(@RequestParam("idConversation") Long idConversation,
	                          @RequestParam("idEtudiant") Long idEtudiant,
	                          @RequestParam("message") String message,
	                          HttpSession session) {
		
		Etudiant sender = (Etudiant) session.getAttribute("etudiantConnecte");
		
		if (sender == null || !sender.getIdEtudiant().equals(idEtudiant)) {
			return "redirect:/connexion";
		}
		
		Conversation conversation = conversationRepository.findById(idConversation).orElseThrow();
		
		EtuMessConversation msg = new EtuMessConversation();
		msg.setEtudiant(sender);
		msg.setConversation(conversation);
		msg.setMessage(message);
		msg.setDateHeureMessage(LocalDateTime.now());
		
		messageRepository.save(msg);
		
		return "redirect:/conversation/" + getOtherParticipantId(conversation, idEtudiant);
	}
	
	// 获取聊天对方 ID 的辅助方法
	private Long getOtherParticipantId(Conversation conv, Long myId) {
		List<EtuMessConversation> messages = messageRepository.findByConversation(conv);
		return messages.stream()
				.map(m -> m.getEtudiant().getIdEtudiant())
				.filter(id -> !id.equals(myId))
				.findFirst()
				.orElse(myId);
	}

}
