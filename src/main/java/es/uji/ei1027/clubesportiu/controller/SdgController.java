package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.SdgDao;
import es.uji.ei1027.clubesportiu.dao.TargetDao;
import es.uji.ei1027.clubesportiu.enums.RelevanceType;
import es.uji.ei1027.clubesportiu.model.Sdg;
import es.uji.ei1027.clubesportiu.model.Target;
import es.uji.ei1027.clubesportiu.model.UjiParticipant;
import es.uji.ei1027.clubesportiu.model.UserLogin;
import es.uji.ei1027.clubesportiu.validators.SdgValidator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Controller
@RequestMapping("/sdg")
public class SdgController {
    private SdgDao sdgDao;
    private TargetDao targetDao;
    @Autowired
    public void setSdgDao(SdgDao sdgDao) {
        this.sdgDao = sdgDao;
    }
    @Autowired
    public void setTargetDao(TargetDao targetDao) {
        this.targetDao = targetDao;
    }
    @RequestMapping("/list")
    public String listSdgs(Model model, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("isAdmin", false);
        } else {
            if (user.isAdmin()) {
                model.addAttribute("isAdmin", true);
            }    else {
                model.addAttribute("isAdmin", false);
            }
        }
        model.addAttribute("sdgs", sdgDao.getSdgs());
        return "sdg/list";
    }
    @RequestMapping("/add")
    public String addSdg(Model model, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/sdg/add");
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "warning"
            );
        }

        RelevanceType[] relevanceTypes = RelevanceType.values();
        model.addAttribute("sdg", new Sdg());
        model.addAttribute("relevanceTypes", relevanceTypes);
        return "sdg/add";
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSdg(@ModelAttribute("sdg") Sdg sdg, BindingResult bindingResult,
                                Model model) {
        SdgValidator validator = new SdgValidator();
        validator.validate(sdg, bindingResult);
        if (bindingResult.hasErrors()) {
            RelevanceType[] relevanceTypes = RelevanceType.values();
            model.addAttribute("relevanceTypes", relevanceTypes);
            return "sdg/add";
        }

        sdgDao.addSdg(sdg);
        return "redirect:list";
    }
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String addSdgFromFile(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build();
            CSVParser parser = new CSVParser(reader, csvFormat);
            for (CSVRecord record : parser) {
                String goal = record.get("goal");
                String description = record.get("description");
                Sdg sdg = new Sdg();
                sdg.setRelevance(RelevanceType.NORMAL);
                sdg.setNumber(Integer.parseInt(goal));
                sdg.setName(description);
                sdgDao.addSdg(sdg);
            }
        } catch (IOException e) {
            throw new ProjectException(
                    "An error happened during the processing of file '" +
                    file.getOriginalFilename() +
                    "', please ensure the integrity of the data in the file",
                    "warning"
            );
        }
        redirectAttributes.addFlashAttribute("success", "All SDG were imported successfully");
        return "redirect:list";
    }
    @RequestMapping("/details/{idSdg}")
    public String details(Model model, @PathVariable int idSdg) {
        Sdg sdg = sdgDao.getSdg(idSdg);
        List<Target> targets = targetDao.getTargetsFromSdg(idSdg);
        model.addAttribute("sdg", sdg);
        model.addAttribute("targets", targets);
        return "sdg/details";

    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updateSdg(Model model, @PathVariable int id, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/sdg/update/" + id);
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "warning"
            );
        }
        RelevanceType[] relevanceTypes = RelevanceType.values();
        model.addAttribute("sdg", sdgDao.getSdg(id));
        model.addAttribute("relevanceTypes", relevanceTypes);
        return "sdg/update";
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSdg(@ModelAttribute("sdg") Sdg sdg, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "sdg/update";

        sdgDao.updateSdg(sdg);
        return "redirect:list";
    }
    @RequestMapping(value = "/delete/{id}")
    public String processDeleteSdg(@PathVariable int id, Model model, HttpSession session) {
        UjiParticipant user = (UjiParticipant) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("user", new UserLogin());
            session.setAttribute("nextUrl", "/sdg/delete/" + id);
            return "login";
        }

        if (!user.isAdmin()) {
            throw new ProjectException(
                    "You are not an administrator!",
                    "warning"
            );
        }
        sdgDao.deleteSdg(id);
        return "redirect:../list";
    }
}
