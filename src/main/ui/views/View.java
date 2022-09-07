package ui.views;

import model.calorietracker.Food;
import model.calorietracker.FoodManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * View represents a collection of JComponents that create a single user experience
 *
 * View requires parent, foodManager, and parentFrame as parameters in it's constructor
 *
 */

public class View {

    protected FoodManager foodManager;
    protected JFrame parentFrame;

    // REQUIRES: parent, foodManager, parentFrame != null
    // MODIFIES: this
    // EFFECTS: constructs a new View
    public View(JComponent parent, FoodManager foodManager, JFrame parentFrame) {
        init();
        this.foodManager = foodManager;
        this.parentFrame = parentFrame;
        createCards(parent);
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: initializes all component
    public void init() { }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: creates all cards in this view
    public void createCards(JComponent parent) { }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds the name of all foods in a day to a list
    protected ArrayList<Object> addToFoodNames(List<Food> list, ArrayList<Object> toModify) {
        for (Food food: list) {
            toModify.add(food.getName());
        }
        return toModify;
    }

}
