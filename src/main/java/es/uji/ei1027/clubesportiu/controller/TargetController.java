package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.SdgDao;
import es.uji.ei1027.clubesportiu.model.Sdg;
import es.uji.ei1027.clubesportiu.model.UjiParticipant;
import es.uji.ei1027.clubesportiu.model.UserLogin;
import es.uji.ei1027.clubesportiu.validators.TargetValidator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import es.uji.ei1027.clubesportiu.model.Target;
import es.uji.ei1027.clubesportiu.dao.TargetDao;
import es.uji.ei1027.clubesportiu.enums.RelevanceType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;


@Controller
@RequestMapping("/target")
public class TargetController {

    private TargetDao targetDao;
    private SdgDao sdgDao;

    @Autowired
    public void setTargetDao(TargetDao targetDao) {
        this.targetDao = targetDao;
    }
    @Autowired
    public void setSdgDao(SdgDao sdgDao) {
        this.sdgDao = sdgDao;
    }
    @RequestMapping("/list")
    public String listTargets(Model model, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("isAdmin", false);
        } else {
            if (user.isAdmin()) {
                model.addAttribute("isAdmin", true);
            } else {
                model.addAttribute("isAdmin", false);
            }
        }
        model.addAttribute("targets", targetDao.getTargets());
        return "target/list";
    }
    @RequestMapping("/add")
    public String addNormalTarget(Model model, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/target/add/");
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "warning"
            );
        }
        List<Sdg> sdgs = sdgDao.getSdgs();
        RelevanceType[] relevanceTypes = RelevanceType.values();
        model.addAttribute("target", new Target());
        model.addAttribute("sdgs", sdgs);
        model.addAttribute("relevanceTypes", relevanceTypes);
        return "target/add_normal";
    }

    @RequestMapping("/add/{idSdg}")
    public String addTarget(Model model, @PathVariable int idSdg, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/target/add/" + idSdg);
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "warning"
            );
        }
        RelevanceType[] relevanceTypes = RelevanceType.values();
        model.addAttribute("target", new Target());
        model.addAttribute("idSdg", idSdg);
        model.addAttribute("relevanceTypes", relevanceTypes);
        return "target/add";
    }
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String addTargetFromFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build();
            CSVParser parser = new CSVParser(reader, csvFormat);
            for (CSVRecord record : parser) {
                int goal = Integer.parseInt(record.get("goal"));
                String target = record.get("target");
                String description = record.get("description");
                Target t = new Target();
                t.setIdSdg(goal);
                t.setNumber(target);
                t.setRelevance(RelevanceType.NORMAL);
                t.setDescription(description);
                targetDao.addTarget(t);

            }
        } catch (IOException e) {
            throw new ProjectException(
                    "An error happened during the processing of file" +
                    file.getOriginalFilename() +
                    "', please ensure the integrity of the data in the file",
                    "warning"
            );
        }

        redirectAttributes.addFlashAttribute("success", "Targets from file imported successfully!");
        return "redirect:/sdg/list";
    }
    @RequestMapping(value = "/add/{idSdg}", method = RequestMethod.POST)
    public String processAddTargetWithId(@ModelAttribute("target") Target target,
                                   BindingResult bindingResult,
                                   @PathVariable int idSdg,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        target.setIdSdg(idSdg);
        TargetValidator validator = new TargetValidator();
        validator.validate(target, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Sdg> sdgs = sdgDao.getSdgs();
            RelevanceType[] relevanceTypes = RelevanceType.values();
            model.addAttribute("idSdg", idSdg);
            model.addAttribute("sdgs", sdgs);
            model.addAttribute("relevanceTypes", relevanceTypes);
            return "target/add";
        }

        targetDao.addTarget(target);
        redirectAttributes.addFlashAttribute("success", "Target added successfully");
        return "redirect:list";
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddTarget(@ModelAttribute("target") Target target, BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        TargetValidator validator = new TargetValidator();
        validator.validate(target, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Sdg> sdgs = sdgDao.getSdgs();
            RelevanceType[] relevanceTypes = RelevanceType.values();
            model.addAttribute("sdgs", sdgs);
            model.addAttribute("relevanceTypes", relevanceTypes);
            return "target/add_normal";
        }

        targetDao.addTarget(target);
        redirectAttributes.addFlashAttribute("success", "Target added successfully");
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{idTarget}", method = RequestMethod.GET)
    public String updateTarget(Model model, @PathVariable int idTarget, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/target/update/"+idTarget);
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "warning"
            );
        }
        RelevanceType[] relevanceTypes = RelevanceType.values();
        List<Sdg> sdgs = sdgDao.getSdgs();
        model.addAttribute("target", targetDao.getTarget(idTarget));
        model.addAttribute("relevanceTypes", relevanceTypes);
        model.addAttribute("sdgs", sdgs);
        return "target/update";
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateTarget(@ModelAttribute("target") Target target, BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        TargetValidator validator = new TargetValidator();
        validator.validate(target, bindingResult);
        if (bindingResult.hasErrors())
            return "target/update";

        targetDao.updateTarget(target);
        redirectAttributes.addFlashAttribute("success", "Target updated successfully!");
        return "redirect:/target/list";
    }
    @RequestMapping(value = "/delete/{id}")
    public String processDeleteTarget(@PathVariable int id, RedirectAttributes redirectAttributes, HttpSession session,
                                      Model model) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/target/delete/" + id);
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "warning"
            );
        }
        targetDao.deleteTarget(id);
        redirectAttributes.addFlashAttribute("success", "Target removed successfully");
        return "redirect:../list";
    }
}
