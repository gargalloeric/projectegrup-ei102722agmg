package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.InitiativeDao;
import es.uji.ei1027.clubesportiu.enums.StatusType;
import es.uji.ei1027.clubesportiu.model.AdministrationFilters;
import es.uji.ei1027.clubesportiu.model.Initiative;
import es.uji.ei1027.clubesportiu.model.Sdg;
import es.uji.ei1027.clubesportiu.model.UjiParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AdministrationController {
    private InitiativeDao initiativeDao;
    @Autowired
    public void setInitiativeDao(InitiativeDao initiativeDao) {
        this.initiativeDao = initiativeDao;
    }
    @RequestMapping("/administration")
    public String admin(Model model, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            session.setAttribute("nextUrl", "/administration");
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "restrictedAccess"
            );
        }

        List<Initiative> initiatives = initiativeDao.getListInitiatives();
        model.addAttribute("filters", new AdministrationFilters());
        model.addAttribute("initiatives", initiatives);
        return "administration/administration";
    }
    @RequestMapping(value = "/administration/filter")
    public String adminFilter(@ModelAttribute("filters") AdministrationFilters filters,
                              BindingResult bindingResult, Model model ,HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            session.setAttribute("nextUrl", "/administration");
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "danger"
            );
        }

        if (bindingResult.hasErrors()) {
            return "administration";
        }

        List<Initiative> filteredInitiatives = initiativeDao.getListInitiatives();
        if (!filters.isActive()) {
            filteredInitiatives.removeIf(i -> i.getStatus().equals(StatusType.ACTIVE));
        }

        if (!filters.isPending()) {
            filteredInitiatives.removeIf(i -> i.getStatus().equals(StatusType.PENDING));
        }

        if (!filters.isRejected()) {
            filteredInitiatives.removeIf(i -> i.getStatus().equals(StatusType.REJECTED));
        }

        if (!filters.isSuspended()) {
            filteredInitiatives.removeIf(i -> i.getStatus().equals(StatusType.SUSPENDED));
        }

        if (!filters.isFinished()) {
            filteredInitiatives.removeIf(i -> i.getStatus().equals(StatusType.FINISHED));
        }

        model.addAttribute("initiatives", filteredInitiatives);
        return "administration/administration";
    }
}
