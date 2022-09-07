package model.calorietracker;

import model.Daily;

import java.util.ArrayList;
import java.util.List;

/**
 * Models the three meals in a day
 * Extends a daily object, which sets and gets a specific date
 * Each meal is represented by a list of food objects
 *
 * DailyMeal can calculate the total calories and macronutrients consumed in a certain day
 *
 */

public class DailyMeal extends Daily {

    public static final short BREAKFAST = 0;
    public static final short LUNCH = 1;
    public static final short DINNER = 2;

    public static final short FAT = 0;
    public static final short CARBS = 1;
    public static final short PROTEIN = 2;

    private List<Food> breakfast;
    private List<Food> lunch;
    private List<Food> dinner;

    // REQUIRES: nothing
    // MODIFIES: nothing
    // EFFECTS: constructs a new daily meal with the date set to the current date
    public DailyMeal() {
        super();
        this.breakfast = new ArrayList<>();
        this.lunch = new ArrayList<>();
        this.dinner = new ArrayList<>();
    }

    // REQUIRES: mealType be one of BREAKFAST (0), LUNCH (1), or DINNER (2), and food be a fully set up food object
    // MODIFIES: this
    // EFFECTS: adds a food item to the meal list of either breakfast, lunch, or dinner, depending on mealType.
    //          returns true if the food is added successfully, false if not
    public boolean addToMeal(short mealType, Food food) {
        boolean mealAdded = false;
        switch (mealType) {
            case BREAKFAST:
                breakfast.add(food);
                mealAdded = true;
                break;
            case LUNCH:
                lunch.add(food);
                mealAdded = true;
                break;
            case DINNER:
                dinner.add(food);
                mealAdded = true;
                break;
            default:
                break;
        }
        return mealAdded;
    }

    // REQUIRES: breakfast is not an empty list
    // MODIFIES: nothing
    // EFFECTS: returns the list of all breakfast items
    public List<Food> getBreakfast() {
        return breakfast;
    }

    // REQUIRES: lunch is not an empty list
    // MODIFIES: nothing
    // EFFECTS: returns the list of all lunch items
    public List<Food> getLunch() {
        return lunch;
    }

    // REQUIRES: dinner is not an empty list
    // MODIFIES: nothing
    // EFFECTS: returns the list of all food items
    public List<Food> getDinner() {
        //stub
        return dinner;
    }

    // REQUIRES: nothing
    // MODIFIES: nothing
    // EFFECTS: returns the list total calories of all food items
    public int getTotalCalories() {
        int totalCals = 0;
        for (Food bf : breakfast) {
            totalCals += bf.getCalories();
        }
        for (Food l : lunch) {
            totalCals += l.getCalories();
        }
        for (Food d : dinner) {
            totalCals += d.getCalories();
        }
        return totalCals;
    }

    // REQUIRES: macros is one of CARBS, FAT, or PROTEIN
    // MODIFIES: nothing
    // EFFECTS: returns the list total macro of all food items
    public int getTotalMacros(short macro) {
        int totalMacros = 0;
        for (Food bf : breakfast) {
            totalMacros += getSpecifiedMacro(bf, macro);
        }
        for (Food l : lunch) {
            totalMacros += getSpecifiedMacro(l, macro);
        }
        for (Food d : dinner) {
            totalMacros += getSpecifiedMacro(d, macro);
        }
        if (totalMacros < 0) {
            totalMacros = -1;
        }
        return totalMacros;
    }

    // REQUIRES: Food is not null
    // MODIFIES: nothing
    // EFFECTS: returns the macronutrient of the food of a specified type
    private int getSpecifiedMacro(Food food, short macro) {
        int specMacro = -1;
        switch (macro) {
            case FAT:
                specMacro = food.getFat();
                break;
            case CARBS:
                specMacro = food.getCarbs();
                break;
            case PROTEIN:
                specMacro = food.getProtein();
                break;
        }
        return specMacro;
    }

}
