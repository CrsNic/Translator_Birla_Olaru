public class Words {

    private int id = 0;
    private String word = "";
    private String speech = "";
    private String lemma = "";
    private String kind = "";
    private String type = "";
    private String nb = "";
    private String article = "";
    private String tense = "";
    private String gender = "";
    private String person = "";
    private String subject ="";

    public String toString(){

        return "\n\tID: "+ id
                +"\n\tWord: "+word
                +"\n\tSpeech Type: "+speech //verb/adjective...
                +"\n\tLemma: "+ lemma // did -> do
                +"\n\tKind: "+kind
                +"\n\tType: "+type
                +"\n\tNumber: "+nb //sg/pl
                +"\n\tArticle: "+article
                +"\n\tTense: "+tense  // past/present/future...
                +"\n\tGender: "+gender // female / male
                +"\n\tPerson: "+person+"\n" //3rd,2nd,1st
                +"\n\tSubject: "+subject+"\n";

    }

    public int getID(){
        return id;
    }
    public void setID(int id){
        this.id = id;
    }
    public String getWord(){
        return word;
    }
    public void setWord(String word){
        this.word = word;
    }
    public String getSpeech(){
        return speech;
    }
    public void setSpeech(String speech){
        this.speech = speech;
    }
    public String getLemma(){
        return lemma;
    }
    public void setLemma(String lemma){
        this.lemma = lemma;
    }
    public String getKind(){
        return kind;
    }
    public void setKind(String kind){
        this.kind = kind;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getNumber(){
        return nb;
    }
    public void setNumber(String nb){
        this.nb = nb;
    }
    public String getArticle(){
        return article;
    }
    public void setArticle(String article){
        this.article = article;
    }
    public String getTense(){
        return tense;
    }
    public void setTense(String tense){
        this.tense = tense;
    }
    public String getGender(){
        return gender;
    }
    public void setGender(String Gender){
        this.gender = gender;
    }
    public String getPerson(){
        return person;
    }
    public void setPerson(String person){
        this.person = person;
    }
    public String getSubject(){ return subject;}

}