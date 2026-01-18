package net.koteczekui.appoapps.core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE_NAME = "config.dat";

    public List<String> searchFolders = new ArrayList<>();
    public String lastSearch = "";
    public Boolean currentTheme = true;

    public void saveToDat() {
        File file = new File(FILE_NAME);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AppConfig loadAll() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new AppConfig();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            AppConfig config = (AppConfig) ois.readObject();
            return config;
        } catch (Exception e) {
            return new AppConfig();
        }
    }

    public void saveAll() {
        saveToDat();
    }
}
