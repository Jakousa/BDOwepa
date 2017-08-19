package wad.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BossTimer {

    private String name;
    private String spawnStart;
    private String spawnEstimated;
    private String lastUpdated;

    public BossTimer(String name) {
        this.name = name;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        lastUpdated = dateFormat.format(now);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpawnStart() {
        return spawnStart;
    }

    public void setSpawnStart(String spawnStart) {
        this.spawnStart = spawnStart;
    }

    public String getSpawnEstimated() {
        return spawnEstimated;
    }

    public void setSpawnEstimated(String spawnEstimated) {
        this.spawnEstimated = spawnEstimated;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
