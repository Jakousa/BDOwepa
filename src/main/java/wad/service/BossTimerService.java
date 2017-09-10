package wad.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wad.domain.BossTimer;

@Service
public class BossTimerService {

    private String time;

    @Autowired
    private UrzasArchives urzas;

    @Autowired
    private SimpMessagingTemplate template;

    public void sendTimer(List<BossTimer> timers) {
        for (BossTimer timer : timers) {
            this.template.convertAndSend("/index", timer);
        }
    }
    
    public List<BossTimer> getTimers() {
        return urzas.getBossTimers();
    }

    @Scheduled(fixedDelay = 30000)
    public void updateTimers() {
        List<BossTimer> timers = urzas.getBossTimers();
        sendTimer(timers);
    }
    
}
