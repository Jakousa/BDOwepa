package wad.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wad.domain.BossTimer;
import wad.service.BossTimerService;

@RestController
@RequestMapping("/api")
public class TimerController {

    @Autowired
    BossTimerService bossTimerService;

    @RequestMapping(value = "timers", method = RequestMethod.GET)
    public List<BossTimer> view(Model model) {
        List<BossTimer> timers = bossTimerService.getTimers();
        
        
        return timers;
    }
}
