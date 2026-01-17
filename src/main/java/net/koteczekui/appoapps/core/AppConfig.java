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
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(this);
            System.out.println("SAVED TO: " + new File(FILE_NAME).getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AppConfig loadAll() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new AppConfig();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (AppConfig) ois.readObject();
        } catch (Exception e) {
            return new AppConfig();
        }
    }

    public void saveAll() { saveToDat(); }
}
