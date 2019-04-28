import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Translator {
    private String text;
    private String wordArray[];
    private ArrayList<Words> enWords, enExp, roWords, roExp, compoundWords;
    private Words subject;

    public void getDictionary(ArrayList<Words> enWords, ArrayList<Words> roWords) {
        this.enWords = enWords;
        this.roWords = roWords;
        this.enExp = this.extractEnExp(enWords);
        this.roExp = this.extractRoExp(roWords);
        this.compoundWords = this.extractCompoundWords(enWords);
    }

    public void getText(String en_text) {
        text = en_text;

        //System.out.println("\nEnglish Text: " + text);
        if (text!=null) {
            wordArray = text.split(" ");
        }

    }

    public String translate() {
        Words englishWord = null;
        String translated = "";
        String[] expWords;
        String[] splitCompoundWords;
        String compoundExp = "", translatedExp = "";
        subject = this.find(wordArray[0]); //first word

        boolean compundNounOrExp = false;
        int i = 0;
        while (i < wordArray.length) { //if we have words to translate
            String word = wordArray[i];  // the word to be translated is the word from the array
            englishWord = this.find(word);
            // System.out.println("Word:" + english.getWord());

            //SEARCH FOR EXPRESSIONS
            for (Words expression : enExp) {
                expWords = expression.getWord().split(" "); //split expression into words
                if (expWords.length > wordArray.length - i)
                    break;

                boolean isExpression = true;

                for (int j = 0; j < expWords.length; j++) {  //Once in a blue sun != Once in a blue moon
                    if (!wordArray[i + j].equals(expWords[j]))
                        isExpression = false;
                }

                if (isExpression) {
                    translatedExp = translateENExp(expression);
                    translated += translatedExp;
                    i = i + expWords.length - 1;
                    compundNounOrExp = true;
                }
            }


            // SEARCH IN COMPOUND NOUNS DICTIONARY

            for (Words compWdsIterator : compoundWords) {
                splitCompoundWords = compWdsIterator.getWord().split(" ");
                if (splitCompoundWords.length > wordArray.length - i)
                    break;
                boolean isCompWord = true;

                for (int j = 0; j < splitCompoundWords.length; j++)
                    if (!wordArray[i + j].equals(splitCompoundWords[i]))
                        isCompWord = false;


                if (isCompWord) {
                    translatedExp = translateENExp(compWdsIterator);
                    translated += translatedExp;
                    i += splitCompoundWords.length - 1;
                    compundNounOrExp = true;
                    subject = compWdsIterator;
                }
            }

            if (!compundNounOrExp) {
                if (word.equals("the")) {
                    i++;
                    String wordAfterThe = wordArray[i];
                    englishWord = this.find(wordAfterThe);

                    if (englishWord.getSpeech().equals("noun") || englishWord.getSpeech().equals("pronoun")) {
                        subject = englishWord;
                    }

                    Map<String, String> wordParam = new HashMap<>();
                    wordParam.put("article", "definite");

                    translated += this.translateWithChecking(englishWord, wordParam) + " ";

                    if (i != wordArray.length - 1) {
                        i++;
                        word = wordArray[i];
                    } else
                        break;

                }

                englishWord = this.find(word);
                if (englishWord != null) {
                    if (englishWord.getSpeech().equals("verb")) {
                        if (englishWord.getWord().equals("was")) { // Checking for the "to be" verb ; past continuous tense
                            if ((this.find(wordArray[i + 1]).getSpeech().equals("verb"))
                                    || (this.find(wordArray[i + 1]).getWord().equals("not") && this.find(wordArray[i + 2]).getSpeech().equals("verb"))) {
                                i++;
                                continue;
                            } else
                                translated += this.translateFromEnglish(englishWord);

                        }
                        else if (subject.getPerson().equals("3")) { //Checking third person singular/plural for verbs
                            if (subject.getNumber().equals("singular")) {
                                if (this.hasSameRoInfinitive(englishWord)) {
                                    translated += "a ";
                                }
                            }
                            else {
                                if (subject.getNumber().equals("plural")) {
                                    if (this.hasSameRoInfinitive(englishWord)) {
                                        translated += "au ";
                                    }
                                }
                            }

                            translated += "a "+this.translateFromEnglish(englishWord);
                        } else {

                            translated += this.translateFromEnglish(englishWord);
                        }

                    } else if (englishWord.getSpeech().equals("noun") || englishWord.getSpeech().equals("pronoun")) {
                        subject = englishWord;
                        translated += this.translateFromEnglish(englishWord);
                    } else if (englishWord.getSpeech().equals("adjective")) {

                        System.out.println("Subject: " + subject.getWord() + " " + subject.getGender());
                        Map<String, String> wordParam = new HashMap<>();
                        wordParam.put("gender", subject.getGender());
                        translated += this.translateWithChecking(englishWord, wordParam) + " ";

                    } else if (englishWord.getSpeech().equals("adverb")) {
                        subject = englishWord;
                        translated += this.translateFromEnglish(englishWord);
                        Words nextWord = this.find(wordArray[i + 1]);
                        if (nextWord.getSpeech().equals("verb") && englishWord.getWord().equals("nothing")) {
                            translated += "nu a";
                        }
                    } else if (englishWord.getWord().equals(".") || englishWord.getWord().equals(",")) {
                        translated += englishWord.getWord() + " ";
                    } else {
                        translated += this.translateFromEnglish(englishWord);
                    }
                }

            }
            compundNounOrExp = false;
            englishWord = null;
            i++;
        }
        return translated;
    }

    // GET EN EXPRESSIONS from dictionary
    private ArrayList<Words> extractEnExp(ArrayList<Words> dictionary) {
        ArrayList<Words> exp = new ArrayList<>();
        for (Words word : dictionary) {
            if (word.getSpeech().equals("expression"))
                exp.add(word);
        }
        return exp;
    }

    //GET RO EXPRESSIONS from dictionary
    private ArrayList<Words> extractRoExp(ArrayList<Words> dictionary) {
        ArrayList<Words> exp = new ArrayList<>();
        for (Words word : dictionary) {
            if (word.getSpeech().equals("expression"))
                exp.add(word);
        }
        return exp;
    }

    //GET COMPOUND words from dictionary
    private ArrayList<Words> extractCompoundWords(ArrayList<Words> dictionary) {
        ArrayList<Words> compWord = new ArrayList<>();
        for (Words word : dictionary) {
            if (word.getSpeech().equals("compound"))
                compWord.add(word);
        }
        return compWord;

    }

    //FIND word in dictionary

    private Words find(String word) {
        Words englishWord = new Words();
        englishWord.setWord(word);
        for (Words enWord : enWords) {
            if (enWord.getWord().equals(word)) {
                englishWord = enWord;
                break;
            }
        }
        return englishWord;
    }

    //TRANSLATE method

    private String translateFromEnglish(Words englishWord) {
        if (englishWord != null)
            for (Words roWord : roWords) {
                if (roWord.getID() == englishWord.getID()) {
                    return roWord.getWord() + " ";
                }
            }
        return "[u w: " + englishWord.getWord() + " ] ";
    }

    // CHECK FOR SAME LEMMA ("INFINITIVE" FORM)
    private boolean hasSameRoInfinitive(Words englishWord) {
        Words romanianWord = null;
        if (englishWord != null) {
            for (Words roWord : roWords) {
                if (roWord.getID() == englishWord.getID()) {
                    romanianWord = roWord;
                    break;
                }
            }
            if (romanianWord.getWord().equals(romanianWord.getLemma()))
                return true;
        }
        return false;

    }

    private String translateWithChecking(Words englishWord, Map<String, String> wordParam) {
        ArrayList<Words> lemmaFamily = new ArrayList<>();
        //SEARCH for all words that have the same LEMMA (INFINITIVE form)
        if (englishWord != null) { //
            for (Words roWord : roWords) {
                if (roWord.getID() == englishWord.getID()) {
                    lemmaFamily.add(roWord);
                }
            }
            //IF A WORD NEEDS TO BE ARTICULATED IN RO TRANSLATION (if it has "a"/"an" in english)
            if (englishWord.getSpeech().equals("noun")) {
                if (wordParam.containsKey("article")) {
                    for (Words roWord : lemmaFamily) {
                        if (roWord.getArticle().equals(wordParam.get("article")))
                            return roWord.getWord();
                    }
                }
            }

            //IF EN WORD IS AN ADJECTIVE

            if (englishWord.getSpeech().equals("adjective")) {
                if (wordParam.containsKey("gender")) {
                    for (Words roWord : lemmaFamily) {
                        if (roWord.getGender().equals(wordParam.get("gender")))
                            return roWord.getWord();
                    }
                }
            }

        }
        return "[u w: " + englishWord.getWord() + "]; ";

    }

    private String translateENExp(Words enExp) {
        if (enExp != null)
            for (Words roExps : roExp) {
                if (roExps.getID() == enExp.getID()) {
                    return roExps.getWord() + " ";
                }
            }
        return "[u e:" + enExp.getWord() + "]; ";
    }
}










