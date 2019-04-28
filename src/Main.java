import javax.swing.*;

public class Main {


    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException  {
       /* Dictionary dictionary = new Dictionary();
        Translator translator = new Translator();

        dictionary.mkDictionary("en_dictionary.json", "ro_dictionary.json");

        translator.getDictionary(dictionary.getEnglish(),dictionary.getRomanian());*/

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TranslatorGUI translatorGUI= null;
                try {
                    translatorGUI = new TranslatorGUI();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                translatorGUI.setVisible(true);
            }
        });
    }
}
