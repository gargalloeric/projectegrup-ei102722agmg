package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.SdgDao;
import es.uji.ei1027.clubesportiu.dao.TargetDao;
import es.uji.ei1027.clubesportiu.enums.ClassificationType;
import es.uji.ei1027.clubesportiu.enums.RelevanceType;
import es.uji.ei1027.clubesportiu.model.*;
import es.uji.ei1027.clubesportiu.validators.ActionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import es.uji.ei1027.clubesportiu.dao.ActionDao;
import es.uji.ei1027.clubesportiu.enums.ProgressType;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/action")
public class ActionController {

    private ActionDao actionDao;
    private TargetDao targetDao;
    private SdgDao sdgDao;

    @Autowired
    public void setActionDao(ActionDao actionDao) {
        this.actionDao = actionDao;
    }
    @Autowired
    public void setTargetDao(TargetDao targetDao) {
        this.targetDao = targetDao;
    }
    @Autowired
    public void setSdgDao(SdgDao sdgDao) {
        this.sdgDao = sdgDao;
    }
    @RequestMapping("/list")
    public String listActions(Model model) {
        model.addAttribute("actions", actionDao.getAction());
        return "action/list";
    }
    @RequestMapping(value = "/add/creation", method = RequestMethod.POST)
    public String addActionCreation(@ModelAttribute("initiative") Initiative initiative, Model model,
                                    BindingResult bindingResult, HttpSession session) {
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
        List<Target> targets = targetDao.getTargets();
        ProgressType[] progressTypes = ProgressType.values();
        model.addAttribute("action", new Action());
        model.addAttribute("targets", targets);
        model.addAttribute("progressTypes", progressTypes);
        return "action/add_creation";
    }
    @RequestMapping(value = "/add/creation/process", method = RequestMethod.POST)
    public String processAddActionCreation(@ModelAttribute("action") Action action, BindingResult bindingResult,
                                           Model model,
                                           HttpSession session) {
        ActionValidator validator = new ActionValidator();
        validator.validate(action, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("targets", targetDao.getTargets());
            model.addAttribute("progressTypes", ProgressType.values());
            return "action/add_creation";
        }
        List<Action> actions = (List<Action>) session.getAttribute("actions");
        actions.add(action);
        return "redirect:/initiative/add";
    }

    @RequestMapping("/add/{idInitiative}")
    public String addAction(Model model, @PathVariable int idInitiative, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/action/add/" + idInitiative);
            return "login";
        }
        List<Target> targets = targetDao.getTargets();
        ProgressType[] progressTypes = ProgressType.values();
        Action action = new Action();
        action.setIdInitiative(idInitiative);
        model.addAttribute("action", action);
        model.addAttribute("targets", targets);
        model.addAttribute("progressTypes", progressTypes);
        return "action/add";
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddAction(@ModelAttribute("action") Action action, Model model,BindingResult bindingResult) {
        ActionValidator actionValidator = new ActionValidator();
        actionValidator.validate(action, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("targets", targetDao.getTargets());
            model.addAttribute("progressTypes", ProgressType.values());
            return "action/add";
        }
        actionDao.addAction(action);
        return "redirect:/initiative/details/" + action.getIdInitiative()+"/actions";
    }

    @RequestMapping(value = "/update/{idAction}", method = RequestMethod.GET)
    public String updateAction(Model model, @PathVariable int idAction, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/action/update/" + idAction);
            return "login";
        }
        ProgressType[] progressTypes = ProgressType.values();
        List<Target> targets = targetDao.getTargets();
        model.addAttribute("action", actionDao.getAction(idAction));
        model.addAttribute("progressTypes", progressTypes);
        model.addAttribute("targets", targets);
        return "action/update";
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateAction(@ModelAttribute("action") Action action, BindingResult bindingResult, Model model) {
        ActionValidator validator = new ActionValidator();
        validator.validate(action, bindingResult);
        if (bindingResult.hasErrors()) {
            ProgressType[] progressTypes = ProgressType.values();
            model.addAttribute("progressTypes", progressTypes);
            return "action/update";
        }
        action.setLastModified(LocalDate.now());
        actionDao.updateAction(action);
        return "redirect:/initiative/details/"+action.getIdInitiative()+"/actions";
    }

    @RequestMapping(value = "/delete/{name}")
    public String processDeleteAction(@PathVariable String name) {
        actionDao.deleteAction(name);
        return "redirect:../list";
    }
}
