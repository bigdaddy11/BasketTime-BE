package com.prod.main.baskettime.batch;
import java.time.Duration;
import java.time.LocalDate;
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
@RequestMapping("/api/draw/newbalance")
public class NewBalanceLaunchController {

    @Autowired
    private DrawService drawService;

    @GetMapping
    public List<Draw> getNewBalanceLaunches() {
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
            String url = "https://www.nbkorea.com/launchingCalendar/list.action?listStatus=C";
            driver.get(url);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul#launchingList > li")));

            List<WebElement> productElements = driver.findElements(By.cssSelector("ul#launchingList > li"));

            for (int i = 0; i < productElements.size(); i++) {
                WebElement productElement = productElements.get(i);
                Draw product = new Draw();

                int currentYear = LocalDate.now().getYear();
                String month = productElement.findElement(By.cssSelector("div.launching_date > span.lMonth")).getText();
                String day = productElement.findElement(By.cssSelector("div.launching_date > span.lDay")).getText();

                try {
                    String dateString = month + " " + day + " " + currentYear;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
                    LocalDate releaseDate = LocalDate.parse(dateString, formatter);
                    product.setDrawDate(releaseDate);
                } catch (DateTimeParseException e) {
                    System.err.println("날짜 변환 오류: " + e.getMessage());
                }

                String name = productElement.findElement(By.cssSelector("div.launching_open > span.launching_name")).getText();
                product.setDrawName(name);

                String imageUrl = productElement.findElement(By.cssSelector("div.launching_img > img")).getAttribute("src");
                product.setImagePath(imageUrl);

                String detailLink = productElement.findElement(By.cssSelector("a")).getAttribute("href");
                product.setDrawLink(detailLink);
                product.setType("NB");

                // 디테일 페이지로 이동
                driver.get(detailLink);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.pr_info > p.date")));

                try {
                    String releaseInfo = driver.findElement(By.cssSelector("div.pr_info > p.date")).getText();
                    product.setReleaseTime(releaseInfo);

                    String priceText = driver.findElement(By.cssSelector("ul.price_info > li > span.price")).getText();
                    product.setPrice(priceText + " 원");
                } catch (Exception e) {
                    System.err.println("상세 정보 가져오기 실패: " + detailLink);
                }

                Draw savedProduct = drawService.saveOrUpdateDraw(product);
                if (savedProduct != null) {
                    products.add(savedProduct);
                }

                // 메인 페이지로 복귀 및 대기
                driver.navigate().back();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul#launchingList > li")));

                // 리스트를 다시 로드
                productElements = driver.findElements(By.cssSelector("ul#launchingList > li"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return products;
    }

    @GetMapping("/kids")
    public List<Draw> getNewBalanceKidsLaunches() {
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
            String url = "https://www.nbkorea.com/launchingCalendar/list.action?brandCode=NK&listStatus=C";
            driver.get(url);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul#launchingList > li")));

            List<WebElement> productElements = driver.findElements(By.cssSelector("ul#launchingList > li"));

            for (int i = 0; i < productElements.size(); i++) {
                WebElement productElement = productElements.get(i);
                Draw product = new Draw();

                int currentYear = LocalDate.now().getYear();
                String month = productElement.findElement(By.cssSelector("div.launching_date > span.lMonth")).getText();
                String day = productElement.findElement(By.cssSelector("div.launching_date > span.lDay")).getText();

                try {
                    String dateString = month + " " + day + " " + currentYear;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
                    LocalDate releaseDate = LocalDate.parse(dateString, formatter);
                    product.setDrawDate(releaseDate);
                } catch (DateTimeParseException e) {
                    System.err.println("날짜 변환 오류: " + e.getMessage());
                }

                String name = productElement.findElement(By.cssSelector("div.launching_open > span.launching_name")).getText();
                product.setDrawName(name);

                String imageUrl = productElement.findElement(By.cssSelector("div.launching_img > img")).getAttribute("src");
                product.setImagePath(imageUrl);

                String detailLink = productElement.findElement(By.cssSelector("a")).getAttribute("href");
                product.setDrawLink(detailLink);
                product.setType("NB");

                // 디테일 페이지로 이동
                driver.get(detailLink);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.pr_info > p.date")));

                try {
                    String releaseInfo = driver.findElement(By.cssSelector("div.pr_info > p.date")).getText();
                    product.setReleaseTime(releaseInfo);

                    String priceText = driver.findElement(By.cssSelector("ul.price_info > li > span.price")).getText();
                    product.setPrice(priceText + " 원");
                } catch (Exception e) {
                    System.err.println("상세 정보 가져오기 실패: " + detailLink);
                }

                Draw savedProduct = drawService.saveOrUpdateDraw(product);
                if (savedProduct != null) {
                    products.add(savedProduct);
                }

                // 메인 페이지로 복귀 및 대기
                driver.navigate().back();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul#launchingList > li")));

                // 리스트를 다시 로드
                productElements = driver.findElements(By.cssSelector("ul#launchingList > li"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return products;
    }
}

