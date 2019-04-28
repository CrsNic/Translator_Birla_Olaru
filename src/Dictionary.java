import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.io.FileReader;


public class Dictionary {

    public ArrayList<Words> english = new ArrayList<Words>();
    public ArrayList<Words> romanian = new ArrayList<Words>();

    public void mkDictionary(String enFile, String roFile) {
        JsonArray jsonEN = this.convertFileToJson(enFile);
        JsonArray jsonRO = this.convertFileToJson(roFile);
        english = this.fillDictionary(english, jsonEN);
        romanian = this.fillDictionary(romanian, jsonRO);
    }

    public ArrayList<Words> getEnglish() {
        return english;
    }

    public ArrayList<Words> getRomanian() {
        return romanian;
    }

    private JsonArray convertFileToJson(String file){

        //parse json file
        JsonArray jsonObj = new JsonArray();

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new FileReader(file));
            jsonObj = jsonElement.getAsJsonArray();
        } catch (Exception exception){
            exception.printStackTrace();
        }

        return jsonObj;
    }


    private ArrayList<Words> fillDictionary(ArrayList<Words> wordList, JsonArray dictionary){ //fill dictionary with words
        Words words;

        for(JsonElement jsonWord : dictionary){

            words = new Words();
            JsonObject jsonObj = (JsonObject) jsonWord;

            words.setID(jsonObj.get("ID").getAsInt());
            words.setWord(jsonObj.get("Word").getAsString());
            words.setSpeech(jsonObj.get("Speech").getAsString());
            words.setLemma(jsonObj.get("Lemma").getAsString());
            words.setKind(jsonObj.get("Kind").getAsString());
            words.setType(jsonObj.get("Type").getAsString());
            words.setNumber(jsonObj.get("Number").getAsString());
            words.setArticle(jsonObj.get("Article").getAsString());
            words.setTense(jsonObj.get("Tense").getAsString());
            words.setGender(jsonObj.get("Gender").getAsString());
            words.setPerson(jsonObj.get("Person").getAsString());

            wordList.add(words);
        }

        return wordList;
    }
}