package wad.domain;

import java.util.Date;

public class BossTimer {

    private String name;
    private Date spawnStart;
    private Date spawnEstimated;
    private Date lastUpdated;

    public BossTimer(String name) {
        this.name = name;
        Date now = new Date();
        lastUpdated = now;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getSpawnStart() {
        return spawnStart;
    }

    public void setSpawnStart(Date spawnStart) {
        this.spawnStart = spawnStart;
    }

    public Date getSpawnEstimated() {
        return spawnEstimated;
    }

    public void setSpawnEstimated(Date spawnEstimated) {
        this.spawnEstimated = spawnEstimated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
