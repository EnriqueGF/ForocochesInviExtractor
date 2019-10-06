package inviextractor;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class InviExtractor {

    private static ArrayList<String> URLs = new ArrayList<String>();
    private static ArrayList<Invitacion> invitaciones = new ArrayList<Invitacion>();
    private static final File invisGuardadas = new File("invis.dat");
    private static final String chromeDriverPath = "chromedriver.exe";
    // ID de chat de Instagram al que enviar las alertas
    private static final int chatId = -382395582;
    // Tiempo de espera entre cada petición
    private static final int delayBetweenRequests = 20000;
    // Usuario de Instagram
    private static final String instagramUser = "";
    // Contraseña de Instagram
    private static final String instagramPassword = "";
    public static WebDriver webBrowser;

    public static void main(String[] args) {

        deserialize();
        startUpWebDriver();

        URLs.add("https://www.instagram.com/forocochescom/?hl=es");

        while (true) {
            obtenerInvis();
            try {
                Thread.sleep(delayBetweenRequests);
            } catch (InterruptedException ex) {
                Logger.getLogger(InviExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private static void startUpWebDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");

        webBrowser = new ChromeDriver(options);
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        login(instagramUser, instagramPassword);

    }

    private static void login(String user, String password) {
        try {
            System.out.println("Logeando...");
            webBrowser.get("https://www.instagram.com/accounts/login/");
            Thread.sleep(1000);
            webBrowser.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/article/div/div[1]/div/form/div[2]/div/label/input")).sendKeys(user);
            webBrowser.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/article/div/div[1]/div/form/div[3]/div/label/input")).sendKeys(password);
            Thread.sleep(1000);
            webBrowser.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/article/div/div[1]/div/form/div[4]/button/div")).click();
            Thread.sleep(3000);
            System.out.println("Logeado.");
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
    }

    private static void obtenerInvis() {

        for (String URL : URLs) {
            webBrowser.get(URL);
            String html = webBrowser.getPageSource();
            System.out.println("Request. " + webBrowser.getTitle());
            boolean continuar = true;

            while (continuar) {
                if (!(html.contains("C\\u00f3digo"))) {
                    continuar = false;
                } else {
                    extraerInviDeHtml(html);
                    html = html.replaceFirst("C\\\\u00f3digo", "");

                }
            }
        }
    }

    static final String decode(final String in) {
        String working = in;
        int index;
        index = working.indexOf("\\u");
        while (index > -1) {
            int length = working.length();
            if (index > (length - 6)) {
                break;
            }
            int numStart = index + 2;
            int numFinish = numStart + 4;
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring, 16);
            String stringStart = working.substring(0, index);
            String stringEnd = working.substring(numFinish);
            working = stringStart + ((char) number) + stringEnd;
            index = working.indexOf("\\u");
        }
        return working;
    }

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    private static void extraerInviDeHtml(String html) {

        String substrInvi, inviText;
        substrInvi = html.substring(html.indexOf("C\\u00f3digo"));

        substrInvi = substrInvi.substring(0, 80);

        if (inviIsSerialized(substrInvi)) {
            return;
        }
        inviText = decode(substrInvi);

        Invitacion inviExtraida = new Invitacion(inviText);

        invitaciones.add(inviExtraida);
        serialize();
        System.out.println("Invitación encontrada: " + inviText);
        try {
            Desktop.getDesktop().browse(new URI("http://forocoches.com/codigo"));
        } catch (URISyntaxException ex) {
            Logger.getLogger(InviExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InviExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }

        GetRequest request = new GetRequest("https://api.telegram.org/bot854572573:AAHjoP2bm9Xy7d66zgoXQ_NxmYN4WUsjS5s/sendMessage?chat_id=" + chatId + "&text=" + encodeValue(inviText));

    }

    private static boolean inviIsSerialized(String substrInvi) {

        for (Invitacion invi : invitaciones) {
            if (invi.getTexto().equals(decode(substrInvi))) {
                return true;
            }
        }
        return false;

    }

    private static void deserialize() {
        FileInputStream fileInput = null;
        try {
            if (!invisGuardadas.exists()) {
                return;
            }
            fileInput = new FileInputStream(invisGuardadas);
            ObjectInputStream invisObjectInput = new ObjectInputStream(fileInput);
            invitaciones = (ArrayList<Invitacion>) invisObjectInput.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InviExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InviExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InviExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void serialize() {
        try {
            FileOutputStream fileOutput = new FileOutputStream(invisGuardadas);
            ObjectOutputStream invisObjectOutput = new ObjectOutputStream(fileOutput);
            invisObjectOutput.writeObject(invitaciones);
            fileOutput.close();
            invisObjectOutput.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InviExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InviExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void checkResponseCode(GetRequest request) {
        switch (request.getResponseCode()) {
            case 429: {
                try {
                    System.out.println("429 detectado, esperando 60 segundos antes de continuar.");
                    System.out.println("Respuesta: " + request.getHTML());
                    System.out.println("header: " + request.getHeader("Retry-After"));
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(InviExtractor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
        }
    }

}
