package com.prod.main.baskettime.batch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.time.Duration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
@RequestMapping("/api/draw/nike")
public class NikeDrawController {

    @Autowired
    private DrawService drawService;

    @GetMapping
    public List<Draw> getNikeDraws() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        List<Draw> products = new ArrayList<>();
        WebDriver driver = new ChromeDriver(options);

        try {
            String url = "https://www.nike.com/kr/launch?s=upcoming";
            driver.get(url);
            Thread.sleep(2000);

            while (true) {
                List<WebElement> productElements = driver.findElements(By.cssSelector(".css-1lu53xk.e4lt99o0.nds-grid-item"));

                for (int i = 0; i < productElements.size(); i++) {
                    try {
                        // 제품 리스트를 다시 탐색 (항상 유효한 WebElement 보장)
                        productElements = driver.findElements(By.cssSelector(".css-1lu53xk.e4lt99o0.nds-grid-item"));
                        WebElement productElement = productElements.get(i);

                        Draw product = new Draw();
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

                        String detailLink = productElement.findElement(By.cssSelector("a.product-card-link")).getAttribute("href");
                        product.setDrawLink(detailLink);

                        String imageUrl = productElement.findElement(By.cssSelector("picture img")).getAttribute("src");
                        product.setImagePath(imageUrl);
                        product.setType("N");

                        // 상세 페이지 이동
                        driver.get(detailLink);
                        Thread.sleep(2000);
                        try {
                            // SKU 값 추출
                            try {
                                //driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
                                String title = driver.getTitle();
                                System.out.println("Page Title: " + title);
                                
                                // 정규식으로 괄호 안의 값 추출
                                Pattern pattern = Pattern.compile("\\((.*?)\\)");
                                Matcher matcher = pattern.matcher(title);
                            
                                // SKU 값 추출
                                if (matcher.find()) {
                                    String extractedValue = matcher.group(1); // 괄호 안의 값
                                    System.out.println("Extracted Value: " + extractedValue);
                                    product.setProductCode(extractedValue);
                                } else {
                                    System.out.println("괄호 안의 값을 찾을 수 없습니다.");
                                }
                                
                            } catch (Exception e) {
                                System.err.println("SKU 정보 가져오기 실패: " + e.getMessage());
                            }
                        
                            // 가격 정보 추출
                            try {
                                WebElement priceElement = driver.findElement(By.cssSelector("div.headline-5.pb6-sm.fs14-sm.fs16-md"));
                                String price = priceElement.getText();
                                System.out.println("Price: " + price);
                                product.setPrice(price);
                            } catch (Exception e) {
                                System.err.println("가격 정보 가져오기 실패: " + e.getMessage());
                            }

                            // 출시일 추출
                            try {
                                WebElement dateElement = driver.findElement(By.cssSelector("div.available-date-component"));
                                String releaseTime = dateElement.getText();
                                System.out.println("releaseTime: " + releaseTime);
                                product.setReleaseTime(releaseTime);
                            } catch (Exception e) {
                                System.err.println("출시일 정보 가져오기 실패: " + e.getMessage());
                            }
                        
                        } catch (Exception e) {
                            System.err.println("상세 정보 가져오기 실패: " + product);
                        }

                        // 목록 페이지 복귀
                        driver.navigate().back();
                        Thread.sleep(2000);

                        // Product 저장
                        Draw savedProduct = drawService.saveOrUpdateDraw(product);
                        if (savedProduct != null) {
                            products.add(savedProduct);
                        }

                    } catch (Exception e) {
                        System.err.println("오류 발생: " + e.getMessage());
                    }
                }
                break; // 페이지 이동 로직 추가 가능
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return products;
    }
}

