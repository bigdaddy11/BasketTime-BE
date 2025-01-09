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
@RequestMapping("/api/draw/adidas")
public class AdidasDrawController {

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
            String url = "https://www.adidas.co.kr/adizero";
            driver.get(url);

            // 2초 대기 (페이지 로드)
            Thread.sleep(2000);

            // 제품 정보를 감싸는 모든 div 태그 찾기
            List<WebElement> articles = driver.findElements(By.cssSelector("section[data-testid='product-grid'] article"));

            // 각 제품 정보를 순회하며 데이터 추출
            for (WebElement article : articles) {
                try {
                Draw product = new Draw();
                    // 신발 이름
                    // 제품 이름
                String productName = article.findElement(By.cssSelector("p[data-testid='product-card-title']")).getText();
                // 제품 하위 제목
                String subtitle = article.findElement(By.cssSelector("p[data-testid='product-card-subtitle']")).getText();
                // 가격
                String price = article.findElement(By.cssSelector("div[data-testid='primary-price']")).getText();
                // 링크
                String detailLink = article.findElement(By.cssSelector("a[data-testid='product-card-image-link']")).getAttribute("href");
                // 이미지 URL
                String imageUrl = article.findElement(By.cssSelector("img[data-testid='product-card-primary-image']")).getAttribute("src");

                System.out.println("제품명: " + productName);
                System.out.println("하위 제목: " + subtitle);
                System.out.println("가격: " + price);
                System.out.println("상세 링크: https://www.adidas.com" + detailLink);
                System.out.println("이미지 URL: " + imageUrl);
                System.out.println("----------------------");

                // 상세 페이지 추가 데이터 크롤링
                crawlProductDetail("https://www.adidas.com" + detailLink);
                product.setType("A");

                // Product 객체 생성
                // Draw savedProduct = drawService.saveOrUpdateDraw(product);
                // if (savedProduct != null) {
                //     products.add(savedProduct);
                // }
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

    // 상세 페이지에서 추가 데이터 크롤링
    public static void crawlProductDetail(String url) throws InterruptedException {
        WebDriver detailDriver = new ChromeDriver();
        try {
            detailDriver.get(url);
            Thread.sleep(2000); // 페이지 로드 대기

            // 예: 출시일 정보를 가져오는 로직
            try {
                WebElement releaseDateElement = detailDriver.findElement(By.cssSelector("출시일 관련 셀렉터"));
                String releaseDate = releaseDateElement.getText();
                System.out.println("출시일: " + releaseDate);
            } catch (Exception e) {
                System.out.println("출시일 정보를 찾을 수 없습니다.");
            }

        } finally {
            detailDriver.quit();
        }
    }
}
