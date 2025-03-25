package gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Этот класс управляет состояниями, сохраняя их в файл и загружая при перезапуске приложения.
 */
public class StateManager {
    private  final String CONFIG_DIR = "C:\\Users\\EDWARD\\Desktop\\Edward";
    private  final String CONFIG_FILE = CONFIG_DIR + "/stat.cfg";

    private final Properties properties;

    public StateManager() {
        properties = new Properties();
        ensureConfigDirectoryExists();
    }

    private void ensureConfigDirectoryExists() {
        try {
            Path path = Paths.get(CONFIG_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Failed to create config directory: " + e.getMessage());
        }
    }

    public void saveState(String key, String value) {
        properties.setProperty(key, value);
    }

    public String loadState(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public void saveToFile() {
        try (FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "Application State");
            System.out.println("Saved to file Successfully");
        } catch (IOException e) {
            System.err.println("Failed to save state to file: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            System.out.println("File loaded ");
        } catch (IOException e) {
            System.err.println("Failed to load state from file: " + e.getMessage());
        }
    }

}
