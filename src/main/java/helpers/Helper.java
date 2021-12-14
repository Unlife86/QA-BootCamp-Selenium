package helpers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Helper {

    public static Map<String, Map<String,String>> _gson(String key, String fileName) throws FileNotFoundException, IOException {
        Path path = Paths.get(fileName);
        JsonElement tree = null;
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonParser parser = new JsonParser();
            tree = parser.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (new Gson()).fromJson(((JsonObject) tree).get(key),Map.class);
    }

    public static int _getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }



}
