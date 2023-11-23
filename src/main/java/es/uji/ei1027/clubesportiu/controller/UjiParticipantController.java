package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.InitiativeDao;
import es.uji.ei1027.clubesportiu.dao.SdgDao;
import es.uji.ei1027.clubesportiu.dao.SubscriptionDao;
import es.uji.ei1027.clubesportiu.enums.RelevanceType;
import es.uji.ei1027.clubesportiu.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import es.uji.ei1027.clubesportiu.dao.UjiParticipantDao;
import es.uji.ei1027.clubesportiu.enums.UjiType;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/user")
public class UjiParticipantController {

    private UjiParticipantDao ujiParticipantDao;
    private InitiativeDao initiativeDao;
    private SubscriptionDao subscriptionDao;
    private SdgDao sdgDao;

    @Autowired
    public void setUjiParticipantDao(UjiParticipantDao ujiParticipantDao) {
        this.ujiParticipantDao = ujiParticipantDao;
    }
    @Autowired
    public void setInitiativeDao(InitiativeDao initiativeDao) {
        this.initiativeDao = initiativeDao;
    }
    @Autowired
    public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }
    @Autowired
    public void setSdgDao(SdgDao sdgDao) {
        this.sdgDao = sdgDao;
    }

    @RequestMapping("/details/feed")
    public String detailsUjiParticipantFeed(
            Model model,
            HttpSession session
    ) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/user/details/feed");
            return "login";
        }
        List<Integer> ids = subscriptionDao.getSubscriptionsFromUser(user.getIdParticipant()).stream()
                .filter(s -> s.getEndDate() ==null )
                .map(Subscription::getIdSdg)
                .collect(Collectors.toList());
        List<Initiative> initiativesFeed = initiativeDao.getListInitiativesForFeed(ids);
        model.addAttribute("user", user);
        model.addAttribute("isFeed", true);
        model.addAttribute("initiativesFeed", initiativesFeed);
        return "ujiParticipant/details";
    }
    @RequestMapping("/details/initiatives")
    public String detailsUjiParticipants(Model model, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/user/details/initiatives");
            return "login";
        }
        List<Initiative> userInitiatives = initiativeDao.getListInitiativesFromUser(user.getIdParticipant());
        model.addAttribute("user", user);
        model.addAttribute("isInitiative", true);
        model.addAttribute("initiativesFromUser", userInitiatives);
        return "ujiParticipant/details";
    }
    @RequestMapping("/details/subscriptions")
    public String subscriptionsUjiParticipant(Model model, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/user/details/subscriptions");
            return "login";
        }
        List<UjiUserSubscriptionDetail> userSubscriptions = subscriptionDao.getSubscriptionsFromUser(user.getIdParticipant()).stream()
                .map(s -> {
                    UjiUserSubscriptionDetail us = new UjiUserSubscriptionDetail();
                    String sdgName = sdgDao.getSdgName(s.getIdSdg());
                    us.setIdParticipant(s.getIdUjiParticipant());
                    us.setIdSdg(s.getIdSdg());
                    us.setStartDate(s.getStartDate());
                    us.setEndDate(s.getEndDate());
                    us.setName(sdgName);
                    return us;
                })
                .collect(Collectors.toList());
        model.addAttribute("user", user);
        model.addAttribute("isSubscription", true);
        model.addAttribute("userSubscriptions", userSubscriptions);
        return "ujiParticipant/details";
    }

    @RequestMapping("/add")
    public String addUjiParticipant(Model model) {
        UjiType[] ujiTypes = UjiType.values();
        model.addAttribute("ujiParticipant", new UjiParticipant());
        model.addAttribute("ujiTypes", ujiTypes);
        return "ujiParticipant/add";
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddUjiParticipant(@ModelAttribute("ujiParticipant") UjiParticipant ujiParticipant, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "ujiParticipant/add";

        ujiParticipantDao.addUjiParticipant(ujiParticipant);
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updateUjiParticipant(Model model, @PathVariable int id) {
        UjiType[] ujiTypes = UjiType.values();
        model.addAttribute("ujiParticipant", ujiParticipantDao.getUjiParticipant(id));
        model.addAttribute("ujiTypes", ujiTypes);
        return "ujiParticipant/update";
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateUjiParticipant(@ModelAttribute("ujiParticipant") UjiParticipant ujiParticipant, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "ujiParticipant/update";

        ujiParticipantDao.updateUjiParticipant(ujiParticipant);
        return "redirect:list";
    }
    @RequestMapping(value = "/delete/{id}")
    public String processDeleteUjiParticipant(@PathVariable String id) {
        ujiParticipantDao.deleteUjiParticipant(id);
        return "redirect:../list";
    }
}
