package wad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wad.domain.BossTimer;

@Service
public class BossTimerService {

    int when = 0;
    
    @Autowired
    private UrzasArchives urzas;

    @Autowired
    private SimpMessagingTemplate template;

    public void sendTimer(BossTimer timer) {
        this.template.convertAndSend("/index", timer);
    }

    @Scheduled(fixedDelay = 1000)
    public void updateTimers() {
        System.out.println("TIMERI KASVAAA " + when);
        when++;
        BossTimer timer = urzas.getBossTimer("Now: " + when);
        sendTimer(timer);
    }
}
