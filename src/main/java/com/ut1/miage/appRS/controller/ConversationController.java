package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.EtuMessConversation;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.ConversationRepository;
import com.ut1.miage.appRS.repository.EtuMessConversationRepository;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ConversationController {
	@Autowired
	EtudiantRepository etudiantRepository;
	@Autowired
	ConversationRepository conversationRepository;
	@Autowired
	EtuMessConversationRepository messageRepository;
	
	@GetMapping("/conversation/{id}")
	public String openConversation(@PathVariable("id") Long idEtudiant, Model model, Principal principal) {
		// 获取当前登录用户
		Etudiant utilisateurConnecte = etudiantRepository.findByEmailEtudiant(principal.getName()).orElseThrow();
		
		// 查找两人之间是否已有对话
		Optional<Conversation> conversationExistante = conversationRepository.findConversationBetween(utilisateurConnecte.getIdEtudiant(), idEtudiant);
		
		Conversation conversation;
		if (conversationExistante.isPresent()) {
			conversation = conversationExistante.get();
		} else {
			// 创建新的对话
			conversation = new Conversation();
			conversation.setDateCommenceConversation(LocalDateTime.now());
			conversationRepository.save(conversation);
			
			// 插入一条空消息作为初始化（可选）
		}
		
		// 获取目标聊天好友
		Etudiant ami = etudiantRepository.findById(idEtudiant).orElseThrow();
		
		// 查询消息记录
		List<EtuMessConversation> messages = messageRepository.findByConversation(conversation);
		
		model.addAttribute("ami", ami);
		model.addAttribute("conversation", conversation);
		model.addAttribute("messages", messages);
		model.addAttribute("utilisateurConnecte", utilisateurConnecte);
		
		return "conversation";
	}
	
}
