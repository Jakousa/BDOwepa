package wad.service;

import java.net.MalformedURLException;
import java.net.URL;
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
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wad.domain.BossTimer;

@Service
public class UrzasArchives {

    private ArrayList<BossTimer> timers;
    private WebDriver driver;
    private final String url;

    public UrzasArchives() throws MalformedURLException {
        url = "http://urzasarchives.com/bdo/wbtbdo/wbteu/";
        driver = new HtmlUnitDriver();
        driver.get(url);
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

    private BossTimer generateBossTimer(BossTimer timer, List<String> tables) {
        String start = "";
        String estim = "";
        for (String table : tables) {
            if (table.contains(timer.getName())) {
                start = StringUtils.substringBetween(table, "Next Spawn: ", " CEST");
                estim = StringUtils.substringBetween(table, "Est. Spawn: ", " CEST");
                break;
            }
        }
        timer.setSpawnStart(start);
        timer.setSpawnEstimated(estim);
        return timer;
    }

    private List<String> findBossTables() {
        List<String> tables = new ArrayList<>();
        System.out.println("OPENED CONNECTION");
        for (WebElement tbody : driver.findElements(By.xpath("//tbody"))) {
            tables.add(tbody.getText());
        }
        return tables;
    }

    @Scheduled(fixedDelay = 300000)
    public void updateTimers() throws ParseException {
        List<String> tables = findBossTables();

        for (BossTimer timer : timers) {
            Long differenceInMinutes = differenceInMinutesBetweenNowAnd(timer.getSpawnEstimated());
            if (differenceInMinutes < 0) {
                generateBossTimer(timer, tables);
            }
        }
    }

    private long differenceInMinutesBetweenNowAnd(String time) {
        DateFormat dateFormat = new SimpleDateFormat("EEE, hh:mm", Locale.ENGLISH);
        Date now = new Date();
        Date then = null;
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

        return diff / (60 * 1000);
    }

}
