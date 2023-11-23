package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.SdgDao;
import es.uji.ei1027.clubesportiu.enums.RelevanceType;
import es.uji.ei1027.clubesportiu.model.Sdg;
import es.uji.ei1027.clubesportiu.model.UjiParticipant;
import es.uji.ei1027.clubesportiu.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import es.uji.ei1027.clubesportiu.model.Subscription;
import es.uji.ei1027.clubesportiu.dao.SubscriptionDao;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/subscription")
public class SubscriptionController {

    private SubscriptionDao subscriptionDao;
    private SdgDao sdgDao;

    @Autowired
    public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }
   @Autowired
   public void setSdgDao(SdgDao sdgDao) {
        this.sdgDao = sdgDao;
   }

    @RequestMapping("/add")
    public String addSubscription(Model model, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/user/details/subscriptions");
            return "login";
        }
        Subscription subscription = new Subscription();
        subscription.setIdUjiParticipant(user.getIdParticipant());
        List<Sdg> sdgs = sdgDao.getSdgs();
        model.addAttribute("sdgs", sdgs);
        model.addAttribute("subscription", subscription);
        return "subscription/add";
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubscription(Model model, @ModelAttribute("subscription") Subscription subscription,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "subscription/add";
        }

        subscription.setStartDate(LocalDate.now());
        subscriptionDao.addSubscription(subscription);
        redirectAttributes.addFlashAttribute("success", "New subscription added!");
        return "redirect:/user/details/subscriptions";

    }
    @RequestMapping("/end/{idSdg}/{idParticipant}")
    public String processEndSubscription(
            Model model,
            @PathVariable int idSdg,
            @PathVariable int idParticipant,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/user/details/subscriptions");
            return "login";
        }

        Subscription s = subscriptionDao.getSubscription(idSdg, idParticipant);
        s.setEndDate(LocalDate.now());
        subscriptionDao.updateSubscription(s);
        redirectAttributes.addFlashAttribute("success", "Subscription ended successfully!");
        return "redirect:/user/details/subscriptions";
    }
}
