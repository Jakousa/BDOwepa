package wad.service;

import org.springframework.stereotype.Service;
import wad.domain.BossTimer;

@Service
public class UrzasArchives {

    public BossTimer getBossTimer(String name) {
        BossTimer timer = new BossTimer(name);
        return timer;
    }

}
