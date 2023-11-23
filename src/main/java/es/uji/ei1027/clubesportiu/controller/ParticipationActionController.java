package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.SdgDao;
import es.uji.ei1027.clubesportiu.enums.RelevanceType;
import es.uji.ei1027.clubesportiu.model.Sdg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import es.uji.ei1027.clubesportiu.model.ParticipationAction;
import es.uji.ei1027.clubesportiu.dao.ParticipationActionDao;
import es.uji.ei1027.clubesportiu.enums.StatusType;


@Controller
@RequestMapping("/participationAction")
public class ParticipationActionController {

    private ParticipationActionDao participationActionDao;

    @Autowired
    public void setParticipationActionDao(ParticipationActionDao participationActionDao) {
        this.participationActionDao = participationActionDao;
    }
    @RequestMapping("/list")
    public String listParticipationActions(Model model) {
        model.addAttribute("participationActions", participationActionDao.getListparticipationAction());
        return "participationAction/list";
    }

    @RequestMapping("/add")
    public String addParticipationAction(Model model) {
        StatusType[] statusTypes = StatusType.values();
        model.addAttribute("participationAction", new ParticipationAction());
        model.addAttribute("statusTypes", statusTypes);
        return "participationAction/add";
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddParticipationAction(@ModelAttribute("participationAction") ParticipationAction participationAction, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "participationAction/add";

        participationActionDao.addparticipationAction(participationAction);
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{name}", method = RequestMethod.GET)
    public String updateParticipationAction(Model model, @PathVariable String name) {
        StatusType[] statusTypes = StatusType.values();
        model.addAttribute("participationAction", participationActionDao.getparticipationAction(name));
        model.addAttribute("statusTypes", statusTypes);
        return "participationAction/update";
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateParticipationAction(@ModelAttribute("participationAction") ParticipationAction participationAction, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "participationAction/update";

        participationActionDao.updateparticipationAction(participationAction);
        return "redirect:list";
    }
    @RequestMapping(value = "/delete/{name}")
    public String processDeleteSdg(@PathVariable String name) {
        participationActionDao.deleteparticipationAction(name);
        return "redirect:../list";
    }
}
