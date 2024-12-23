package com.prod.main.baskettime.batch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.Draw;
import com.prod.main.baskettime.service.DrawService;

import io.github.bonigarcia.wdm.WebDriverManager;

@RestController
@RequestMapping("/api/draw/nike")
public class NikeDrawController {

    @Autowired
    private DrawService drawService;

    @GetMapping
    public List<Draw> getNikeDraws() {
        // WebDriverManager를 사용해 자동으로 ChromeDriver 다운로드 및 설정
        WebDriverManager.chromedriver().setup();

        // ChromeOptions에 Headless 모드 추가
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // GUI 없이 실행
        options.addArguments("--disable-gpu"); // GPU 가속 비활성화
        options.addArguments("--no-sandbox"); // 샌드박스 비활성화 (리눅스용 옵션이지만 호환성 보장)
        options.addArguments("--disable-dev-shm-usage"); // 메모리 공유 이슈 해결

        List<Draw> products = new ArrayList<>();

        WebDriver driver = new ChromeDriver(options);
         try {
            // 나이키 출시 페이지로 이동
            String url = "https://www.nike.com/kr/launch?s=upcoming";
            driver.get(url);

            // 2초 대기 (페이지 로드)
            Thread.sleep(2000);

            // 제품 정보를 감싸는 모든 div 태그 찾기
            List<WebElement> productElements = driver.findElements(By.cssSelector(".css-1lu53xk.e4lt99o0.nds-grid-item"));

            // 각 제품 정보를 순회하며 데이터 추출
            for (WebElement productElement : productElements) {
                try {
                    Draw product = new Draw();
                    // 신발 이름
                    String style = productElement.findElement(By.cssSelector("h1.nds-text.title")).getText();
                    product.setDrawStyle(style);
                    String name = productElement.findElement(By.cssSelector("h2.nds-text.category")).getText();
                    product.setDrawName(name);

                    String releaseMonth = productElement.findElement(By.cssSelector("p[data-qa='test-startDate']")).getText();
                    String releaseDay = productElement.findElement(By.cssSelector("h3[data-qa='test-day']")).getText();

                    int month = Integer.parseInt(releaseMonth.split("월")[0]);
                    int day = Integer.parseInt(releaseDay.split("일")[0]);
                    int year = LocalDate.now().getYear();

                    LocalDate releaseDate = LocalDate.of(year, month, day);
                    product.setDrawDate(releaseDate);

                    // 상세 페이지 링크
                    String detailLink = productElement.findElement(By.cssSelector("a.product-card-link")).getAttribute("href");
                    product.setDrawLink(detailLink);

                    // 이미지 URL
                    String imageUrl = productElement.findElement(By.cssSelector("picture img")).getAttribute("src");
                    product.setImagePath(imageUrl);
                    product.setType("N");

                    // Product 객체 생성
                    Draw savedProduct = drawService.saveOrUpdateDraw(product);
                    if (savedProduct != null) {
                        products.add(savedProduct);
                    }
                } catch (Exception e) {
                    System.err.println("오류 발생: 일부 요소를 찾을 수 없습니다.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 브라우저 종료
            driver.quit();
        }
        return products;
    }
}
