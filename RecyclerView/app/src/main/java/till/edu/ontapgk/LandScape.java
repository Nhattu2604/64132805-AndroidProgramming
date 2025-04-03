package till.edu.ontapgk;

public class LandScape {
    String langImageFileName;
    String landCaption;
    public LandScape (String langImageFileName, String landCaption){
        this.langImageFileName = langImageFileName;
        this.landCaption = landCaption;
    }

    public String getLangImageFileName() {
        return langImageFileName;
    }

    public void setLangImageFileName(String langImageFileName) {
        this.langImageFileName = langImageFileName;
    }

    public String getLandCaption() {
        return landCaption;
    }

    public void setLandCaption(String landCaption) {
        this.landCaption = landCaption;
    }
}
