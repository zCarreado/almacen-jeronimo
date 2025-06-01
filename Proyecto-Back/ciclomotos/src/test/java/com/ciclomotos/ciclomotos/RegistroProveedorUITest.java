package com.ciclomotos.ciclomotos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class RegistroProveedorUITest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Quitar si se quiere ver el navegador
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testRegistroProveedor() {
        driver.get("http://localhost:9091/proveedores");

        // Llenar el formulario
        WebElement nombre = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nombre")));
        WebElement correo = driver.findElement(By.id("correo"));
        WebElement telefono = driver.findElement(By.id("telefono"));

        String nombreTest = "Proveedor Selenium";
        String correoTest = "proveedor.selenium@correo.com";
        String telefonoTest = "3009876543";

        nombre.clear();
        nombre.sendKeys(nombreTest);
        correo.clear();
        correo.sendKeys(correoTest);
        telefono.clear();
        telefono.sendKeys(telefonoTest);

        // Esperar a que el botón sea clickeable y hacer scroll
        WebElement boton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", boton);
        // Forzar el clic con JavaScript para evitar overlays
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", boton);

        // Esperar el alert y aceptarlo
        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        assertTrue(alertText.contains("Proveedor registrado correctamente"));
        driver.switchTo().alert().accept();

        // Recargar la página para asegurar que la tabla se actualiza
        driver.navigate().refresh();

        // Verificar que el proveedor aparece en la tabla
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr/td[contains(text(), '" + nombreTest + "')]")));
        WebElement fila = driver.findElement(By.xpath("//table/tbody/tr/td[contains(text(), '" + nombreTest + "')]/.."));
        assertNotNull(fila);
        assertTrue(fila.getText().contains(correoTest));
        assertTrue(fila.getText().contains(telefonoTest));
    }
}
