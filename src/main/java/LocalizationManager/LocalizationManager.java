package LocalizationManager;

import log.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *  Управляет локализацией приложения, предоставляя переведенные строки для разных локалей.
 * Этот класс загружает пакеты ресурсов для поддерживаемых языков и позволяет динамически
 * переключать язык во время выполнения
 */
public class LocalizationManager {
    private static ResourceBundle resourceBundle;
    private static Locale currentLocale;

    public LocalizationManager(String initialLanguage) {
        setLocale(initialLanguage);
    }

    /**
     * Изменяет текущую локаль и загружает соответствующий пакет ресурсов.
     *@param language код языка, на который нужно переключиться (например, "en", "ru")
     */

    public void setLocale(String language) {
        currentLocale = new Locale(language);
        resourceBundle = ResourceBundle.getBundle("localization.messages", currentLocale);
    }

    /**
     * * Извлекает локализованную строку для указанного ключа в текущей локали.
     * @param key ключ, идентифицирующий локализованную строку в пакете ресурсов
     * @return локализованная строка
     */
    public String getString(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (Exception e) {
            Logger.error("" + key);
            return  key ;
        }
    }

}