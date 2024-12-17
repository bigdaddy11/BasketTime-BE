package com.prod.main.baskettime.batch;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.Team;
import com.prod.main.baskettime.service.TeamService;

import io.github.bonigarcia.wdm.WebDriverManager;

@RestController
@RequestMapping("/api/kbl/teams/batch")
public class KBLTeamController {
    @Autowired
    private TeamService teamService;

    @GetMapping
    public List<Team> getTeams() {
        // WebDriverManager를 사용해 자동으로 ChromeDriver 다운로드 및 설정
        WebDriverManager.chromedriver().setup();

         // ChromeOptions에 Headless 모드 추가
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // GUI 없이 실행
        options.addArguments("--disable-gpu"); // GPU 가속 비활성화
        options.addArguments("--no-sandbox"); // 샌드박스 비활성화 (리눅스용 옵션이지만 호환성 보장)
        options.addArguments("--disable-dev-shm-usage"); // 메모리 공유 이슈 해결

        WebDriver driver = new ChromeDriver(options);

        List<Team> teams = new ArrayList<>();

        try {
            // 1. KBL 페이지 열기
            String url = "https://www.kbl.or.kr/team/intro";
            driver.get(url);

            // 페이지가 완전히 로드될 때까지 대기 (간단히 3초 대기, 더 정교한 대기는 WebDriverWait 사용 가능)
            Thread.sleep(2000);

            // 각 팀을 감싸고 있는 li 태그 찾기
            List<WebElement> teamElements = driver.findElements(By.cssSelector("ul.team-intro-wrap > li"));

            // 각 팀 정보 추출
            for (WebElement teamElement : teamElements) {
                Team team = new Team();
                // 팀 이름 가져오기
                String teamName = teamElement.findElement(By.cssSelector("div > p")).getText();
                // 팀 이름 저장
                team.setFullName(teamName);
                team.setType("K");
                // 결과 리스트에 추가
                teams.add(team);

                // Service 호출: 데이터 저장 또는 업데이트
                teamService.saveOrUpdateTeams(teams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4. 브라우저 닫기
            driver.quit();
        }
        return teams;
    }
}
