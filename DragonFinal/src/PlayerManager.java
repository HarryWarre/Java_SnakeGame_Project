import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PlayerManager {
    private static final String FILE_PATH = "players.json";

    public static void savePlayer(int score) throws ParseException {
        // Tạo một đối tượng JSON để lưu trữ thông tin người chơi
        JSONObject player = new JSONObject();
        player.put("score", score);

        // Đọc dữ liệu từ tệp JSON hiện có (nếu có)
        JSONArray players = readPlayers();

        // Thêm người chơi mới vào mảng dữ liệu
        players.add(player);

        // Ghi dữ liệu vào tệp JSON
        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(players.toJSONString());
            file.flush();
        } catch (IOException e) {
        }
    }

    public static JSONArray readPlayers() throws ParseException {
        // Đọc dữ liệu từ tệp JSON
        JSONArray players = new JSONArray();
        try {
            FileReader reader = new FileReader(FILE_PATH);
            JSONParser parser = new JSONParser();
            players = (JSONArray) parser.parse(reader);
            reader.close();
        } catch (IOException | ParseException e) {
        }
        return players;
    }
}
