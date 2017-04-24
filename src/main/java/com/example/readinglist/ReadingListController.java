package com.example.readinglist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/readingList")
@ConfigurationProperties(prefix="ower")
public class ReadingListController {
	
	private ReadingListRepository readingListRepository;
	private String userName;
	
	@Autowired
	public ReadingListController(ReadingListRepository readingListRepository) {
		this.readingListRepository = readingListRepository;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String readersBooks(Model model, HttpServletRequest request) {
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) request  
				 .getSession().getAttribute("SPRING_SECURITY_CONTEXT");  
		// 登录名  
		String loginUser = securityContextImpl.getAuthentication().getName();
		List<Book> readingList = readingListRepository.findByReader(loginUser);
		if (readingList != null) {
			model.addAttribute("books", readingList);
		}
		model.addAttribute("userName", userName);
		model.addAttribute("loginUser", loginUser);
		return "readingList/readingList";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String addToReadingList(Book book, HttpServletRequest request) {
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) request  
				 .getSession().getAttribute("SPRING_SECURITY_CONTEXT");  
		// 登录名  
		String loginUser = securityContextImpl.getAuthentication().getName();
		book.setReader(loginUser);
		readingListRepository.save(book);
		return "redirect:readingList";
	}

}
