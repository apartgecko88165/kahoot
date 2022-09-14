import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String path = "";

        try {
            File driverPathFile = new File("path.txt");
            Scanner pathScan = new Scanner(driverPathFile);
            if (pathScan.hasNextLine()) {
                path = pathScan.nextLine();
            }
            pathScan.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Please create a file called 'path.txt' in this directory and try again.");
            System.exit(0);
        }


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        System.out.println("Enter pin:");
        String pin = scan.nextLine();
        System.out.println("Enter nickname template:");
        String nick = scan.nextLine();
        System.out.println("Enter recursion depth:");
        int recur = scan.nextInt();
        System.out.println("Sending...");

        List<WebDriver> drivers = new ArrayList<>();

        for (int i = 0; i < recur; i++) {
            System.setProperty("webdriver.chrome.driver", path);
            WebDriver driver = new ChromeDriver(options);

            driver.get("https://kahoot.it");

            WebElement gameInput = driver.findElement(By.id("game-input"));
            gameInput.sendKeys(pin + Keys.ENTER);

            driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

            WebElement nickname = driver.findElement(By.id("nickname"));
            nickname.sendKeys(nick + (i + 1) + Keys.ENTER);

            drivers.add(driver);

            System.out.println("Player added: <" + nick + (i + 1) + ">");
        }
        System.out.println("Press ENTER to exit:");
        try { System.in.read(); }
        catch(Exception e) {}

        for (WebDriver driver : drivers) {
            driver.quit();
        }
    }
}