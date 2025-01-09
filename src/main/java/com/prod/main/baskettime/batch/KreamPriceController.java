package com.prod.main.baskettime.batch;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
@RequestMapping("/api/kream")
public class KreamPriceController {
    @Autowired
    private DrawService drawService;

    @GetMapping("/update-prices")
    public void updateKreamPrices() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);

        try {
            // draw 테이블에서 product_code가 있는 데이터 가져오기
            List<Draw> draws = drawService.findAllWithProductCode();
            
            for (Draw draw : draws) {
                String productCode = draw.getProductCode();
                String url = "https://kream.co.kr/search?keyword=" + productCode;
                driver.get(url);
                Thread.sleep(5000); // 페이지 로드 대기

                try {
                    // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    // WebElement priceElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                    //     By.cssSelector("span.text-lookup.bold.display_paragraph.line_break_by_truncating_tail")
                    // ));
            
                    // JavaScript로 가격 정보 추출
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    String price = (String) js.executeScript(
                        "return document.querySelector('span.text-lookup.bold.display_paragraph.line_break_by_truncating_tail')?.innerText;"
                    );

                    if (price != null && !price.isEmpty()) {
                        // kreamPrice 업데이트
                        draw.setKreamPrice(price);
                        drawService.saveOrUpdateDraw(draw);

                        System.out.println("업데이트된 상품: " + productCode + " - " + price);
                    } else {
                        System.err.println("가격 정보를 찾을 수 없습니다: " + productCode);
                    }
                } catch (Exception e) {
                    System.err.println("가격 정보를 찾을 수 없습니다: " + productCode);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
