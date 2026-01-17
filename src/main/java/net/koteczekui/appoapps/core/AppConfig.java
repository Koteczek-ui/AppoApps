package net.koteczekui.appoapps.core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CONFIG_FILE = "config.dat";

    public List<String> searchFolders = new ArrayList<>();
    public String lastSearch = "";
    public Boolean currentTheme = true;

    public void saveAll() {
        saveToDat();
    }

    public void saveToDat() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONFIG_FILE))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AppConfig loadAll() {
        return loadFromDat();
    }

    public static AppConfig loadFromDat() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) return new AppConfig();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (AppConfig) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new AppConfig();
        }
    }
}
