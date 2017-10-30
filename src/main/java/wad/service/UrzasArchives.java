package wad.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wad.domain.BossTimer;

@Service
public class UrzasArchives {

    private ArrayList<BossTimer> timers;
    private WebDriver driver;
    private final String url;

    public UrzasArchives() {
        url = "http://urzasarchives.com/bdo/wbtbdo/wbteu/";
        driver = new HtmlUnitDriver();
        timers = initialTimes();
    }

    private ArrayList<BossTimer> initialTimes() {
        ArrayList<BossTimer> timerList = new ArrayList<>();
        List<String> tables = findBossTables();
        timerList.add(generateBossTimer(new BossTimer("Bheg"), tables));
        timerList.add(generateBossTimer(new BossTimer("Karanda"), tables));
        timerList.add(generateBossTimer(new BossTimer("Kutum"), tables));
        timerList.add(generateBossTimer(new BossTimer("Kzarka"), tables));
        timerList.add(generateBossTimer(new BossTimer("Mudster"), tables));
        timerList.add(generateBossTimer(new BossTimer("Nouver"), tables));
        timerList.add(generateBossTimer(new BossTimer("Red Nose"), tables));
        timerList.add(generateBossTimer(new BossTimer("Spirit"), tables));
        return timerList;
    }

    public List<BossTimer> getBossTimers() {
        return timers;
    }
    
    private String findBeforeCESTorCET(String where, String from) {
        Pattern pattern = Pattern.compile(from + "([^']{1,15})(CE[S]*T)");
        Matcher matcher = pattern.matcher(where);
        matcher.find();
        
        return matcher.group(1) + matcher.group(2);
    }

    private BossTimer generateBossTimer(BossTimer timer, List<String> tables) {
        String start = "";
        String estim = "";
        String last = "";
        for (String table : tables) {
            if (table.contains(timer.getName())) {
                if (table.contains("Server Maintenance")) {
                    last = "Wed, 15:00 CEST";
                } else {
                    last = findBeforeCESTorCET(table, "Last Spawn: ");
                }
                start = findBeforeCESTorCET(table, "Next Spawn: ");
                estim = findBeforeCESTorCET(table, "Est. Spawn: ");
                break;
            }
        }
        Date now = new Date();
        Long difference = differenceBetweenNowAnd(start);
        now.setTime(now.getTime() + difference);
        timer.setSpawnStart(now);
        now = new Date();
        difference = differenceBetweenNowAnd(estim);
        now.setTime(now.getTime() + difference);
        timer.setSpawnEstimated(now);
        now = new Date();
        difference = differenceBetweenNowAnd(last);
        now.setTime(now.getTime() + difference);
        timer.setLastSpawn(now);
        return timer;
    }

    private List<String> findBossTables() {
        List<String> tables = new ArrayList<>();
        driver.get(url);
        driver.findElements(By.xpath("//tbody")).stream().forEach((tbody) -> {
            tables.add(tbody.getText());
        });
        return tables;
    }

    @Scheduled(fixedDelay = 200000)
    public void updateTimers() throws ParseException {
        List<String> tables = findBossTables();
        timers.stream().forEach((timer) -> {
            generateBossTimer(timer, tables);
        });
    }

    private long differenceBetweenNowAnd(String time) {
        DateFormat dateFormat = new SimpleDateFormat("EEE, HH:mm z", Locale.ENGLISH);
        Date now = new Date();
        Date then = new Date();
        try {
            now = dateFormat.parse(dateFormat.format(now));
            then = dateFormat.parse(time);
        } catch (ParseException ex) {
            Logger.getLogger(UrzasArchives.class.getName()).log(Level.SEVERE, null, ex);
        }
        long diff = then.getTime() - now.getTime();
        long diffHours = diff / (60 * 60 * 1000);
        if (diffHours < -(24 * 4)) {
            diff += 7 * 24 * 60 * 60 * 1000;
        }
        return diff;
    }

}
