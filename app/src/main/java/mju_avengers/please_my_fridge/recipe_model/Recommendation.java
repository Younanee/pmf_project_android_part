package mju_avengers.please_my_fridge.recipe_model;

public class Recommendation {

    //conf is the output
    private float conf;
    //input label
    private String label;

    Recommendation() {
        this.conf = -1.0F;
        this.label = null;
    }

    void update(float conf, String label) {
        this.conf = conf;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public float getConf() {
        return conf;
    }

}
