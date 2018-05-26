package mju_avengers.please_my_fridge.recipe_model;

public interface Recommend {
    String name();

    Recommendation recognize(final int id, final int recipeid);
}
