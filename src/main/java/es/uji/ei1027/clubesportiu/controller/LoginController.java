package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.UserLoginDao;
import es.uji.ei1027.clubesportiu.model.UjiParticipant;
import es.uji.ei1027.clubesportiu.model.UserLogin;
import es.uji.ei1027.clubesportiu.validators.LoginValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private UserLoginDao userLoginDao;
    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserLogin());
        return "login";
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String checkLogin(@ModelAttribute("user") UserLogin userLogin, BindingResult bindingResult,
                             HttpSession session)
    {
        LoginValidator loginValidator = new LoginValidator();
        loginValidator.validate(userLogin, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Check that the login is correct by checking the user email and password
        UjiParticipant user = userLoginDao.loadUserByEmail(
                userLogin.getEmail(),
                userLogin.getPassword()
        );

        if (user == null) {
            bindingResult.rejectValue("password", "badpw", "Incorrect password.");
            return "login";
        }

        // Autentication success
        session.setAttribute("user", user);

        String nextUrl = (String) session.getAttribute("nextUrl");
        if (nextUrl == null) {
            return "redirect:/";
        }

        return "redirect:" + nextUrl;

    }
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
