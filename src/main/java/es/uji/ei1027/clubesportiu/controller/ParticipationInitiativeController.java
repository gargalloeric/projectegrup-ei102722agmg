package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.InitiativeDao;
import es.uji.ei1027.clubesportiu.dao.ParticipationInitiativeDao;
import es.uji.ei1027.clubesportiu.dao.SdgDao;
import es.uji.ei1027.clubesportiu.dao.UjiParticipantDao;
import es.uji.ei1027.clubesportiu.enums.ClassificationType;
import es.uji.ei1027.clubesportiu.enums.StatusType;
import es.uji.ei1027.clubesportiu.model.*;
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
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/participation-initiative")
public class ParticipationInitiativeController {

    private ParticipationInitiativeDao participationInitiativeDao;
    private UjiParticipantDao ujiParticipantDao;
    private SdgDao sdgDao;
    private InitiativeDao initiativeDao;

    @Autowired
    public void setParticipationInitiativeDao(ParticipationInitiativeDao participationInitiativeDao) {
        this.participationInitiativeDao = participationInitiativeDao;
    }
    @Autowired
    public void setUjiParticipantDao(UjiParticipantDao ujiParticipantDao) {
        this.ujiParticipantDao = ujiParticipantDao;
    }
    @Autowired
    public void setSdgDao(SdgDao sdgDao) {
        this.sdgDao = sdgDao;
    }
    @Autowired
    public void setInitiativeDao(InitiativeDao initiativeDao) {
        this.initiativeDao = initiativeDao;
    }
    @RequestMapping("/list")
    public String listParticipationInitiatives(Model model) {
        model.addAttribute("participationInitiatives", participationInitiativeDao.getListParticipationInitiative());
        return "participationInitiative/list";
    }
    @RequestMapping(value = "/add/creation", method = RequestMethod.POST)
    public String addParticipationsInitiativeCreation(@ModelAttribute("initiative") Initiative initiative, Model model,
                                                      BindingResult bindingResult,
                                                      HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/initiative/add");
            return "login";
        }

        if (bindingResult.hasErrors()) {
            List<Sdg> sdgs = sdgDao.getSdgs();
            ClassificationType[] classificationTypes = ClassificationType.values();
            model.addAttribute("sdgs", sdgs);
            model.addAttribute("classifications", classificationTypes);
            return "initiative/add";
        }
        session.setAttribute("initiative", initiative);
        List<UjiParticipant> users = ujiParticipantDao.getUjiParticipants();
        ParticipantSelection participantSelection = new ParticipantSelection();
        model.addAttribute("idUser", user.getIdParticipant());
        model.addAttribute("users", users);
        model.addAttribute("participantSelection", participantSelection);
        return "participationInitiative/add_creation";

    }
    @RequestMapping("/request/{idInitiative}")
    public String requestParticipation(Model model, HttpSession session, RedirectAttributes redirectAttributes,
                                       @PathVariable int idInitiative) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/participation-initiative/request/"+idInitiative);
            return "login";
        }
        ParticipationInitiative p = new ParticipationInitiative();
        p.setStatus(StatusType.PENDING);
        p.setIdInitiative(idInitiative);
        p.setIdParticipant(user.getIdParticipant());
        participationInitiativeDao.addparticipationInitiative(p);
        redirectAttributes.addFlashAttribute("success", "Request created successfully!");
        return "redirect:/initiative/details/"+idInitiative+"/actions";

    }
    @RequestMapping(value = "/add/creation/process", method = RequestMethod.POST)
    public String processAddParticipationsInitiativeCreation(@ModelAttribute("participantSelection") ParticipantSelection participantSelection,
                                                             BindingResult bindingResult,
                                                             Model model,
                                                             HttpSession session) {
        if (bindingResult.hasErrors()) {
            List<UjiParticipant> users = ujiParticipantDao.getUjiParticipants();
            model.addAttribute("users", users);
            model.addAttribute("participantSelection", new ParticipantSelection());
            return "participationInitiative/add_creation";
        }
        session.setAttribute("idParticipants", participantSelection.getParticipants());
        return "redirect:/initiative/add";
    }
    @RequestMapping("/add/{idInitiative}")
    public String addParticipationInitiative(Model model, @PathVariable int idInitiative) {
        List<UjiParticipant> users = ujiParticipantDao.getUjiParticipants();
        ParticipantSelection participantSelection = new ParticipantSelection();
        List<Integer> participants = participationInitiativeDao.getListParticipationsIninitiative(idInitiative)
                        .stream().map(ParticipationInitiative::getIdParticipant).collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("participants", participants);
        model.addAttribute("participantSelection", participantSelection);
        return "participationInitiative/add";
    }
    @RequestMapping(value = "/add/{idInitiative}", method = RequestMethod.POST)
    public String processAddParticipationInitiative(@ModelAttribute("participationSelection") ParticipantSelection participantSelection,
                                                    BindingResult bindingResult,
                                                    @PathVariable int idInitiative,
                                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "participationInitiative/add";
        }
        List<Integer> participations = participationInitiativeDao.getListParticipationsIninitiative(idInitiative)
                .stream().map(ParticipationInitiative::getIdParticipant).collect(Collectors.toList());
        boolean ok = true;
        for (Integer idParticipant : participantSelection.getParticipants()) {
            if (participations.contains(idParticipant)) {
                continue;
            }
            ParticipationInitiative p = new ParticipationInitiative();
            p.setIdParticipant(idParticipant);
            p.setIdInitiative(idInitiative);
            p.setStartDate(LocalDate.now());
            p.setStatus(StatusType.ACTIVE);
            try {
                participationInitiativeDao.addparticipationInitiative(p);
            } catch (Exception e) {
                ok = false;
            }
        }
        if (ok) {
            redirectAttributes.addFlashAttribute("success", "New participants were added!");
        } else {
            redirectAttributes.addFlashAttribute("warning", "Something happend!");
        }
        return "redirect:/initiative/details/" + idInitiative + "/participants";
    }
    @RequestMapping("/end/{idInitiative}/{idParticipant}")
    public String endParticipation(Model model, HttpSession session,
                                   @PathVariable int idParticipant,
                                   @PathVariable int idInitiative,
                                   RedirectAttributes redirectAttributes) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/participation-initiative/end/" + idParticipant + "/" + idInitiative);
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "warning"
            );
        }
        ParticipationInitiative p = participationInitiativeDao.getParticipationInitiative(idInitiative, idParticipant);
        p.setEndDate(LocalDate.now());
        participationInitiativeDao.updateParticipationInitiative(p);
        redirectAttributes.addFlashAttribute("success", "Participation ended successfully");
        return "redirect:/initiative/details/"+idInitiative+"/participants";
    }
    @RequestMapping("/accept/{idInitiative}/{idParticipant}")
    public String acceptParticipation(Model model, HttpSession session,
                                      @PathVariable int idInitiative, @PathVariable int idParticipant,
                                      RedirectAttributes redirectAttributes) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/participation-initiative/accept/" + idParticipant + "/" + idInitiative);
            return "login";
        }
        Initiative initiative = initiativeDao.getInitative(idInitiative);
        if (user.getIdParticipant() !=  initiative.getIdUjiParticipant()) {
            throw new ProjectException(
                    "You are not the initiative responsible",
                    "warning"
            );
        }
        ParticipationInitiative p = participationInitiativeDao.getParticipationInitiative(idInitiative, idParticipant);
        p.setStatus(StatusType.ACTIVE);
        p.setStartDate(LocalDate.now());
        participationInitiativeDao.updateParticipationInitiative(p);
        redirectAttributes.addFlashAttribute("success", "Request accepted successfully!");
        return "redirect:/initiative/details/"+idInitiative+"/requests";
    }
    @RequestMapping(value = "/update/{idInitiative}/{idParticipant}", method = RequestMethod.GET)
    public String updateParticipationInitiative(Model model, @PathVariable int idInitiative, @PathVariable int idParticipant) {
        StatusType[] statusTypes = StatusType.values();
        model.addAttribute("participationInitiative", participationInitiativeDao.getParticipationInitiative(idInitiative, idParticipant));
        model.addAttribute("statusTypes", statusTypes);
        return "participationInitiative/update";
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateParticipationInitiative(@ModelAttribute("participationInitiative") ParticipationInitiative participationInitiative, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "participationInitiative/update";

        participationInitiativeDao.updateParticipationInitiative(participationInitiative);
        return "redirect:list";
    }
    @RequestMapping(value = "/delete/{name}")
    public String processDeleteParticipationInitiative(@PathVariable String name) {
        participationInitiativeDao.deleteparticipationInitiative(name);
        return "redirect:../list";
    }
}
