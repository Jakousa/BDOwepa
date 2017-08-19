package wad.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        timers = initialTimes();
    }

    private ArrayList<BossTimer> initialTimes() {
        ArrayList<BossTimer> timerList = new ArrayList<>();
        List<String> tables = findBossTables();
        timerList.add(generateBossTimer("Bheg", tables));
        timerList.add(generateBossTimer("Karanda", tables));
        timerList.add(generateBossTimer("Kutum", tables));
        timerList.add(generateBossTimer("Kzarka", tables));
        timerList.add(generateBossTimer("Mudster", tables));
        timerList.add(generateBossTimer("Nouver", tables));
        timerList.add(generateBossTimer("Red Nose", tables));
        timerList.add(generateBossTimer("Spirit", tables));
        return timerList;
    }

    public List<BossTimer> getBossTimers(Date now) {
        return timers;
    }

    private BossTimer generateBossTimer(String name, List<String> tables) {
        BossTimer timer = new BossTimer(name);
        for (String table : tables) {
            if (table.contains(name)) {
                timer.setSpawnStart(StringUtils.substringBetween(table, "Next Spawn: ", " CEST"));
                timer.setSpawnEstimated(StringUtils.substringBetween(table, "Est. Spawn: ", " CEST"));
                return timer;
            }
        }
        return timer;
    }

    private List<String> findBossTables() {
        List<String> tables = new ArrayList<>();
        driver.get(url);
        System.out.println("OPENED CONNECTION");
        for (WebElement tbody : driver.findElements(By.xpath("//tbody"))) {
            tables.add(tbody.getText());
        }
        driver.close();
        return tables;
    }
    
    @Scheduled(fixedDelay = 600000)
    public void updateTimers() {
        System.out.println("TODO: UPDATE");
    }
}
