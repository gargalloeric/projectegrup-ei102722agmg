package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.*;
import es.uji.ei1027.clubesportiu.enums.ClassificationType;
import es.uji.ei1027.clubesportiu.enums.StatusType;
import es.uji.ei1027.clubesportiu.model.*;
import es.uji.ei1027.clubesportiu.validators.InitiativeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/initiative")
public class InitiativeController {

    private InitiativeDao initiativeDao;
    private SdgDao sdgDao;
    private ActionDao actionDao;
    private ParticipationInitiativeDao participationInitiativeDao;
    private UjiParticipantDao ujiParticipantDao;

    @Autowired
    public void setInitiativeDao(InitiativeDao initiativeDao) {
        this.initiativeDao = initiativeDao;
    }
    @Autowired
    public void setSdgDao(SdgDao sdgDao) {
        this.sdgDao = sdgDao;
    }
    @Autowired
    public void setActionDao(ActionDao actionDao) {
        this.actionDao = actionDao;
    }
    @Autowired
    public void setParticipationInitiativeDao(ParticipationInitiativeDao participationInitiativeDao) {
        this.participationInitiativeDao = participationInitiativeDao;
    }
    @Autowired
    public void setUjiParticipantDao(UjiParticipantDao ujiParticipantDao) {
        this.ujiParticipantDao = ujiParticipantDao;
    }
    @RequestMapping("/list")
    public String listInitiatives(Model model) {
        model.addAttribute("initiatives", initiativeDao.getListInitiativesActive());
        return "initiative/list";
    }

    @RequestMapping("/add")
    public String addInitiative(Model model, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/initiative/add");
            return "login";
        }
        Initiative initiative = (Initiative) session.getAttribute("initiative");
        if (initiative == null) {
            initiative = new Initiative();
            initiative.setStatus(StatusType.PENDING);
            initiative.setIdUjiParticipant(user.getIdParticipant());
        }
        List<Integer> idParticipants = (List<Integer>) session.getAttribute("idParticipants");
        if ( idParticipants != null) {
            List<UjiParticipant> participants = idParticipants.stream()
                    .map(id -> ujiParticipantDao.getUjiParticipant(id)).collect(Collectors.toList());
            model.addAttribute("participants", participants);
            session.setAttribute("participants", participants);
        }
        List<Action> actions = (List<Action>) session.getAttribute("actions");
        if (actions == null) {
            actions = new LinkedList<>();
            session.setAttribute("actions", actions);
        } else {
            model.addAttribute("actions", actions);
        }
        List<Sdg> sdgs = sdgDao.getSdgs();
        ClassificationType[] classificationTypes = ClassificationType.values();
        model.addAttribute("sdgs", sdgs);
        model.addAttribute("classifications", classificationTypes);
        model.addAttribute("initiative", initiative);
        return "initiative/add";
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddInitiative(@ModelAttribute("initiative") Initiative initiative, BindingResult bindingResult, Model model,
                                       RedirectAttributes redirectAttributes,
                                       HttpSession session) {
        InitiativeValidator initiativeValidator = new InitiativeValidator();
        initiativeValidator.validate(initiative, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Sdg> sdgs = sdgDao.getSdgs();
            ClassificationType[] classificationTypes = ClassificationType.values();
            model.addAttribute("sdgs", sdgs);
            model.addAttribute("classifications", classificationTypes);
            return "initiative/add";
        }
        // Last modified when creation
        initiative.setLastModified(LocalDateTime.now());
        int idInitiative = initiativeDao.addInitative(initiative);

        List<UjiParticipant> participants = (List<UjiParticipant>) session.getAttribute("participants");
        for (UjiParticipant p : participants) {
            ParticipationInitiative pi = new ParticipationInitiative();
            pi.setStatus(StatusType.ACTIVE);
            pi.setIdInitiative(idInitiative);
            pi.setStartDate(LocalDate.now());
            pi.setIdParticipant(p.getIdParticipant());
            participationInitiativeDao.addparticipationInitiative(pi);
        }

        List<Action> actions = (List<Action>) session.getAttribute("actions");
        for (Action a : actions) {
            a.setIdInitiative(idInitiative);
            a.setLastModified(LocalDate.now());
            actionDao.addAction(a);
        }

        redirectAttributes.addFlashAttribute("success", "Initiative created, pending of approval");
        session.removeAttribute("participants");
        session.removeAttribute("idParticipants");
        session.removeAttribute("actions");
        session.removeAttribute("initiative");
        return "redirect:list";
    }

    @RequestMapping("/update/{idInitiative}")
    public String updateInitiative(Model model, @PathVariable int idInitiative, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/update/"+idInitiative);
            return "login";
        }
        StatusType[] statusTypes = StatusType.values();
        Initiative initiative = initiativeDao.getInitative(idInitiative);
        ClassificationType[] classificationType = ClassificationType.values();
        List<Sdg> sdgs = sdgDao.getSdgs();
        model.addAttribute("initiative", initiative);
        model.addAttribute("statusTypes", statusTypes);
        model.addAttribute("classifications", classificationType);
        model.addAttribute("sdgs", sdgs);
        return "initiative/update";
    }
    @RequestMapping(value = "/update/{idInitiative}", method = RequestMethod.POST)
    public String processUpdateInitiative(@ModelAttribute("initiative") Initiative initiative, BindingResult bindingResult,
                                          @PathVariable int idInitiative,
                                          RedirectAttributes redirectAttributes) {
        InitiativeValidator validator = new InitiativeValidator();
        validator.validate(initiative, bindingResult);
        if (bindingResult.hasErrors()) {
            return "initiative/update";
        }

        initiativeDao.updateInitiative(initiative);
        redirectAttributes.addFlashAttribute("success_update", "Initiative updated successfully!");
        return "redirect:/initiative/details/" + idInitiative + "/actions";
    }
    @RequestMapping("/details/{idInitiative}/actions")
    public String initiativeDetailsActions(Model model, @PathVariable int idInitiative, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        Initiative initiative = initiativeDao.getInitative(idInitiative);
        if (initiative == null) {
            return "initiative/list";
        }
        if (user != null && user.getIdParticipant() == initiative.getIdUjiParticipant()) {
            model.addAttribute("isResponsible", true);
        } else {
            if (user != null) {
                model.addAttribute("isResponsible", false);
                Set<Integer> participants = participationInitiativeDao.getListParticipationsIninitiative(idInitiative).stream()
                        .map(ParticipationInitiative::getIdParticipant)
                        .collect(Collectors.toSet());
                if (!participants.contains(user.getIdParticipant())) {
                    model.addAttribute("canRequest", true);
                }
            } else {
                model.addAttribute("canRequest", false);
            }
        }
        List<Action> initiativeActions = actionDao.getActionsByInitiative(initiative.getIdInitiative());
        model.addAttribute("initiative", initiative);
        model.addAttribute("initiativeActions", initiativeActions);
        model.addAttribute("isActions", true);
        return "initiative/details";
    }
    @RequestMapping("/details/{idInitiative}/participants")
    public String initiativeDetailsParticipants(Model model, @PathVariable int idInitiative, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        Initiative initiative = initiativeDao.getInitative(idInitiative);
        if (initiative == null) {
            return "initiative/list";
        }
        if (user != null && user.getIdParticipant() == initiative.getIdUjiParticipant()) {
            model.addAttribute("isResponsible", true);
        } else {
            model.addAttribute("isResponsible", false);
            if (user != null) {
                model.addAttribute("isResponsible", false);
                Set<Integer> participants = participationInitiativeDao.getListParticipationsIninitiative(idInitiative).stream()
                        .map(ParticipationInitiative::getIdParticipant)
                        .collect(Collectors.toSet());
                if (!participants.contains(user.getIdParticipant())) {
                    model.addAttribute("canRequest", true);
                }
            } else {
                model.addAttribute("canRequest", false);
            }
        }
        List<ParticipantInitiativeDetails> participants = participationInitiativeDao.getListParticipantDetails(idInitiative);
        model.addAttribute("participants", participants);
        model.addAttribute("initiative", initiative);
        model.addAttribute("isParticipants", true);
        return "initiative/details";
    }
    @RequestMapping("/details/{idInitiative}/requests")
    public String initiativeDetailsRequests(Model model, @PathVariable int idInitiative, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        Initiative initiative = initiativeDao.getInitative(idInitiative);
        if (initiative == null) {
            return "initiative/list";
        }

        if (user != null && user.getIdParticipant() == initiative.getIdUjiParticipant()) {
            model.addAttribute("isResponsible", true);
        } else {
            model.addAttribute("isResponsible", false);
            if (user != null) {
                model.addAttribute("isResponsible", false);
                Set<Integer> participants = participationInitiativeDao.getListParticipationsIninitiative(idInitiative).stream()
                        .map(ParticipationInitiative::getIdParticipant)
                        .collect(Collectors.toSet());
                if (!participants.contains(user.getIdParticipant())) {
                    model.addAttribute("canRequest", true);
                } else {
                    model.addAttribute("canRequest", false);
                }
            }
        }

        List<ParticipantInitiativeDetails> participants = participationInitiativeDao.getListParticipantsPending(idInitiative);
        model.addAttribute("participants", participants);
        model.addAttribute("initiative", initiative);
        model.addAttribute("isRequests", true);
        return "initiative/details";

    }
    @RequestMapping("/reject/{id}")
    public String processRejectInitiaive(@PathVariable int id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/administration");
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "danger"
            );
        }

        Initiative initiative = initiativeDao.getInitative(id);
        initiative.setStatus(StatusType.REJECTED);
        initiativeDao.updateInitiative(initiative);
        redirectAttributes.addFlashAttribute("success", "Initiative '" +
                initiative.getName() +
                "' was rejected successfully");
        return "redirect:/administration";
    }

    @RequestMapping("/approve/{id}")
    public String processApproveInitiaive(@PathVariable int id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/administration");
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "warning"
            );
        }

        Initiative initiative = initiativeDao.getInitative(id);
        initiative.setStatus(StatusType.ACTIVE);
        initiativeDao.updateInitiative(initiative);
        redirectAttributes.addFlashAttribute("success", "Initiative '" +
                initiative.getName() +
                "' was approved successfully");
        return "redirect:/administration";
    }
}
