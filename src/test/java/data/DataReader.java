package data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class DataReader {
    public List<HashMap<String, String>> getJsonDataToMap(String filePath) {
        List<HashMap<String, String>> data;
        try {
            String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            data = mapper.readValue(jsonContent, new TypeReference<>() {
            });
        } catch (IOException e) {
            return null;
        }

        return data;
    }
}
