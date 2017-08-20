package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.service.BossTimerService;

@Controller
@RequestMapping("*")
public class DefaultController {
    
    @Autowired
    BossTimerService bossTimerService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model) {
        model.addAttribute("timers", bossTimerService.getTimers());
        return "index";
    }
}
