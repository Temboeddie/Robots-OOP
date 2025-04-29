package LocalizationManager;

import log.Logger;

import java.io.*;
import java.util.Properties;

/**
 * Этот класс сохраняет настройки языка и загружает их при перезапуске приложения.
 */
public class SettingsManager {
    private static final String SETTINGS_FILE = "settings.properties";
    private static final String DEFAULT_LANGUAGE = "en";


    public  void saveLanguagePreference(String language) {
        Properties props = new Properties();
        props.setProperty("language", language);
        try (OutputStream out = new FileOutputStream(SETTINGS_FILE)) {
            props.store(out, "Application Settings");
        } catch (IOException e) {
            Logger.error("Couldn't save language preference: " + e.getMessage());
        }
    }

    public  String loadLanguagePreference() {
        Properties props = new Properties();
        try (InputStream in = new FileInputStream(SETTINGS_FILE)) {
            props.load(in);
            String language = props.getProperty("language");

            return isValidLanguage(language) ? language : DEFAULT_LANGUAGE;
        } catch (IOException e) {
            Logger.error("Error loading language preference: " + e.getMessage());
            return DEFAULT_LANGUAGE;
        }
    }
    private boolean isValidLanguage(String language) {
        return language != null && (language.equals("en") || language.equals("ru"));
    }
}