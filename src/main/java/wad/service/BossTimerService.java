package wad.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Scheduled(fixedDelay = 10000)
    public void updateTimers() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        time = dateFormat.format(now);
        List<BossTimer> timers = urzas.getBossTimers(now);
        sendTimer(timers);
    }
    
}
