package net.koteczekui.appoapps.utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonHandler {

    public static String getValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) return "";

        int valueStart = json.indexOf("\"", start + searchKey.length());
        int valueEnd = json.indexOf("\"", valueStart + 1);

        if (valueStart != -1 && valueEnd != -1) {
            return json.substring(valueStart + 1, valueEnd);
        }
        return "";
    }

    public static List<String> getList(String json, String key) {
        List<String> list = new ArrayList<>();
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) return list;

        int arrayStart = json.indexOf("[", start);
        int arrayEnd = json.indexOf("]", arrayStart);

        if (arrayStart != -1 && arrayEnd != -1) {
            String content = json.substring(arrayStart + 1, arrayEnd);
            String[] items = content.split(",");
            for (String item : items) {
                list.add(item.trim().replace("\"", "").replace("\\\\", "\\"));
            }
        }
        return list;
    }

    public static void saveToFile(File file, Map<String, Object> data) throws IOException {
        StringBuilder sb = new StringBuilder("{\n");
        Iterator<Map.Entry<String, Object>> it = data.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            sb.append("  \"").append(entry.getKey()).append("\": ");

            if (entry.getValue() instanceof List) {
                sb.append(formatList((List<?>) entry.getValue()));
            } else {
                sb.append("\"").append(entry.getValue().toString().replace("\\", "\\\\")).append("\"");
            }

            if (it.hasNext()) sb.append(",");
            sb.append("\n");
        }
        sb.append("}");
        Files.write(file.toPath(), sb.toString().getBytes());
    }

    private static String formatList(List<?> list) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append("    \"").append(list.get(i).toString().replace("\\", "\\\\")).append("\"");
            if (i < list.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]");
        return sb.toString();
    }
}
