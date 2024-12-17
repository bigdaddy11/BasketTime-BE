package com.prod.main.baskettime.batch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.Player;
import com.prod.main.baskettime.service.PlayerService;

import io.github.bonigarcia.wdm.WebDriverManager;

@RestController
@RequestMapping("/api/kbl/players/batch")
public class KBLPlayerController {
    @Autowired
    private PlayerService playerService;

    @GetMapping
    public List<Player> getPlayers() {
        // WebDriverManager를 사용해 자동으로 ChromeDriver 다운로드 및 설정
        WebDriverManager.chromedriver().setup();

         // ChromeOptions에 Headless 모드 추가
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // GUI 없이 실행
        options.addArguments("--disable-gpu"); // GPU 가속 비활성화
        options.addArguments("--no-sandbox"); // 샌드박스 비활성화 (리눅스용 옵션이지만 호환성 보장)
        options.addArguments("--disable-dev-shm-usage"); // 메모리 공유 이슈 해결

        WebDriver driver = new ChromeDriver(options);

        List<Player> players = new ArrayList<>();

        try {
            // 1. KBL 페이지 열기
            String url = "https://www.kbl.or.kr/player/player";
            driver.get(url);

            // 페이지가 완전히 로드될 때까지 대기 (간단히 3초 대기, 더 정교한 대기는 WebDriverWait 사용 가능)
            Thread.sleep(2000);

            // 3. 테이블 데이터 가져오기
            while (true) { // 페이지가 끝날 때까지 반복
                List<WebElement> rows = driver.findElements(By.cssSelector("table.player-table1 tbody tr"));
                for (WebElement row : rows) {
                    Player player = new Player();

                    // 선수 정보 추출
                    player.setFirstName(row.findElement(By.cssSelector("div.player-name p")).getText());
                    //player.setT
                    player.setTeamName(row.findElements(By.tagName("td")).get(1).getText());
                    player.setJerseyNumber(row.findElements(By.tagName("td")).get(2).getText());
                    player.setPosition(row.findElements(By.tagName("td")).get(3).getText());
                    
                    String weight = row.findElements(By.tagName("td")).get(4).getText();
                    String height = row.findElements(By.tagName("td")).get(5).getText();
                    player.setWeight(weight);
                    player.setHeight(height);

                    String draftText = row.findElements(By.tagName("td")).get(6).getText();
                    if (draftText.equals("-")){
                        player.setDraftYear(null);
                        player.setDraftRound(null);
                        player.setDraftNumber(null);
                    }else {
                        String[] parts = draftText.split("\\s+");
                        player.setDraftYear(Integer.parseInt(parts[0]));
                        player.setDraftRound(Integer.parseInt(parts[1].replaceAll("[^0-9]", "")));
                        player.setDraftNumber(Integer.parseInt(parts[2].replaceAll("[^0-9]", "")));
                    }
                    
                    player.setType("K");
                    players.add(player);
                }
                 // 다음 페이지 버튼 찾기
                 WebElement nextButton = driver.findElement(By.cssSelector("button[title='다음 페이지']"));
                 // 버튼이 비활성화되었는지 확인
                if (nextButton.getAttribute("disabled") != null) { // disabled 속성이 존재하면 마지막 페이지
                    System.out.println("모든 페이지 수집 완료");
                    break;
                }
                 // 다음 페이지로 이동
                nextButton.click();
                Thread.sleep(2000); // 페이지 로딩 대기 (2초)
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4. 브라우저 닫기
            driver.quit();
        }

        // DB에 저장 (기존 데이터 삭제 후 다시 저장)
        //playerRepository.deleteAll();
        //playerRepository.saveAll(players);
        // Service로 저장 또는 업데이트 실행
        playerService.saveOrUpdatePlayers(players);

        return players;
    }
}
