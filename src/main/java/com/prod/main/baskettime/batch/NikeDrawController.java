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
@RequestMapping("/api/draw/nike")
public class NikeDrawController {

    @Autowired
    private DrawService drawService;

    @GetMapping
    public List<Draw> getNikeDraws() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");

        List<Draw> products = new ArrayList<>();
        WebDriver driver = new ChromeDriver(options);

        try {
            String url = "https://www.nike.com/kr/launch?s=upcoming";
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1lu53xk.e4lt99o0.nds-grid-item")));

            List<WebElement> productElements = driver.findElements(By.cssSelector(".css-1lu53xk.e4lt99o0.nds-grid-item"));

            for (WebElement productElement : productElements) {
                try {
                    Draw product = new Draw();
                    product.setDrawStyle(productElement.findElement(By.cssSelector("h1.nds-text.title")).getText());
                    product.setDrawName(productElement.findElement(By.cssSelector("h2.nds-text.category")).getText());

                    int month = Integer.parseInt(productElement.findElement(By.cssSelector("p[data-qa='test-startDate']")).getText().split("월")[0]);
                    int day = Integer.parseInt(productElement.findElement(By.cssSelector("h3[data-qa='test-day']")).getText().split("일")[0]);
                    product.setDrawDate(LocalDate.of(LocalDate.now().getYear(), month, day));

                    String detailLink = productElement.findElement(By.cssSelector("a.product-card-link")).getAttribute("href");
                    product.setDrawLink(detailLink);
                    product.setImagePath(productElement.findElement(By.cssSelector("picture img")).getAttribute("src"));
                    product.setType("N");

                    // ✅ 새로운 탭에서 상세 페이지 크롤링
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("window.open(arguments[0], '_blank');", detailLink);

                    ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
                    driver.switchTo().window(tabs.get(1)); // 새 창으로 전환
                    wait.until(ExpectedConditions.titleContains("(")); // 페이지 로드 대기

                    // SKU 추출
                    Matcher matcher = Pattern.compile("\\((.*?)\\)").matcher(driver.getTitle());
                    if (matcher.find()) product.setProductCode(matcher.group(1));

                    // 가격 정보
                    try {
                        WebElement priceElement = driver.findElement(By.cssSelector("div.headline-5.pb6-sm.fs14-sm.fs16-md"));
                        product.setPrice(priceElement.getText());
                    } catch (Exception ignored) {}

                    // 출시일 정보
                    try {
                        WebElement dateElement = driver.findElement(By.cssSelector("div.available-date-component"));
                        product.setReleaseTime(dateElement.getText());
                    } catch (Exception ignored) {}

                    driver.close(); // 새 창 닫기
                    driver.switchTo().window(tabs.get(0)); // 다시 메인 창으로 전환

                    products.add(product); // ✅ 리스트에 추가
                    // Product 저장
                    Draw savedProduct = drawService.saveOrUpdateDraw(product);
                    if (savedProduct != null) {
                        products.add(savedProduct);
                    }
                } catch (Exception e) {
                    System.err.println("❌ 크롤링 오류 발생: " + e.getMessage());
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

