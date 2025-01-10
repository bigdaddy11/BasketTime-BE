package com.prod.main.baskettime.batch;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.Draw;
import com.prod.main.baskettime.service.DrawService;

import io.github.bonigarcia.wdm.WebDriverManager;

@RestController
@RequestMapping("/api/draw/asics")
public class AsicsDrawController {

    @Autowired
    private DrawService drawService;

    @GetMapping
    public List<Draw> getAsicsDraws() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<Draw> products = new ArrayList<>();

        try {
            String url = "https://www.asics.co.kr/board/?id=spscalendar&status=COMING";
            driver.get(url);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul.sps_gallerylist > li.datalist")));
            List<WebElement> productElements = driver.findElements(By.cssSelector("ul.sps_gallerylist > li.datalist"));

            for (WebElement productElement : productElements) {
                Draw product = new Draw();

                // 제품명
                String productName = productElement.findElement(By.cssSelector("div.goods_sbt")).getText();
                product.setDrawName(productName);

                // 출시 정보 (날짜와 시간 포함)
                String releaseInfo = productElement.findElement(By.cssSelector("div.goods_sbj")).getText();
                product.setReleaseTime(releaseInfo);

                
                try {
                    String[] parts = releaseInfo.split(" ");
                    String datePart = parts[0]; 

                     // 현재 연도를 추가하여 파싱할 문자열 생성
                    int currentYear = LocalDate.now().getYear();
                    String dateString = datePart + " " + currentYear; 

                    // DateTimeFormatter로 날짜 파싱
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d yyyy", Locale.ENGLISH);
                    LocalDate releaseDate = LocalDate.parse(dateString, formatter);

                    product.setDrawDate(releaseDate);
                    
                } catch (Exception e) {
                    System.err.println("날짜 변환 오류: " + releaseInfo);
                }

                // 이미지 URL
                String imageUrl = productElement.findElement(By.cssSelector("div.thumb > span > img")).getAttribute("src");
                product.setImagePath(imageUrl.startsWith("http") ? imageUrl : "https://www.asics.co.kr" + imageUrl);

                // 상세 페이지 링크
                String detailLink = productElement.findElement(By.cssSelector("div.thumb")).getAttribute("viewlink");
                product.setDrawLink(detailLink.startsWith("http") ? detailLink : "https://www.asics.co.kr" + detailLink);

                product.setType("A");

                // 저장 또는 업데이트
                Draw savedProduct = drawService.saveOrUpdateDraw(product);
                if (savedProduct != null) {
                    products.add(savedProduct);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return products;
    }
}
