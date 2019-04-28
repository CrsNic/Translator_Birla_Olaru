import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TranslatorGUI extends JFrame{
    private JButton translateButton;
    private JTextArea en_text;
    private JTextArea translatedText;
    private JPanel rootPanel;
    private JFormattedTextField unknownWords;
    private JLabel En_text;
    private JLabel Ro_Text;

    public String romanianText;
    public boolean pushed=false;

    public TranslatorGUI() throws ClassNotFoundException{
         add(rootPanel);
         setTitle("Expert Translator");
         setSize(1200,900);
         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

         en_text.setForeground(Color.YELLOW);
         translatedText.setForeground(Color.YELLOW);
         unknownWords.setForeground(Color.RED);

        Dictionary dictionary = new Dictionary();
        Translator translator = new Translator();

        dictionary.mkDictionary("en_dictionary.json", "ro_dictionary.json");

        translator.getDictionary(dictionary.getEnglish(),dictionary.getRomanian());


         translateButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 translator.getText(en_text.getText());
                 romanianText=translator.translate();

                 String romanianWordsArray[];
                 boolean ukn=false;
                 romanianWordsArray=romanianText.split(";");
                 for (int i=0; i<romanianWordsArray.length;i++){
                     if (romanianWordsArray[i].contains("[u w:")){
                        ukn=true;
                        break;
                     }
                 }
                 translatedText.setText(romanianText);
                 if (ukn){
                     unknownWords.setText("Unknown words existing in the given text! ");
                 }
             }
         });
    }
}
