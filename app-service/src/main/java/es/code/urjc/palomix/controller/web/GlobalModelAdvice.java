package es.code.urjc.palomix.controller.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.code.urjc.palomix.model.Account;
import es.code.urjc.palomix.service.AccountService;

@ControllerAdvice
public class GlobalModelAdvice {
    @Autowired private AccountService accountService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, Principal principal) {
        model.addAttribute("isLoggedIn", principal != null);
    }

    @ModelAttribute
    public void addCurrentUser(Model model, Principal principal){
        if(principal !=null){
            Account currentUser = accountService.findByEmail(principal.getName());
            model.addAttribute("currentUser",currentUser);
        }
    }
}
