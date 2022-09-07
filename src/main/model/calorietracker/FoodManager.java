package model.calorietracker;

import exceptions.DateNotContainedException;
import exceptions.NegativeCalorieGoalException;
import exceptions.NotInitializedException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static model.Daily.sdf;

/**
 * The FoodManager is an object that stores all of the previously created foods, as well as
 * all of the meals that are consumed each day.
 *
 * FoodManager requires no parameters on construction.
 *
 */

public class FoodManager implements Writable {

    private List<DailyMeal> meals;

    private List<Food> foods;

    private int calorieGoal;

    // EFFECTS: Constructs a new food manager object
    public FoodManager() {
        this.meals = new ArrayList<DailyMeal>();
        this.foods = new ArrayList<Food>();
        this.calorieGoal = 2000;
    }

    // MODIFIES: this
    // EFFECTS: adds a daily meal to the list of daily calories if there is not already a meal for that day,
    //          returns true if successfully added, false if not
    public boolean addMeal(DailyMeal meal) {
        String dateString = sdf.format(meal.getDate());
        boolean mealAdded = false;

        if (meals.size() > 0) {
            DailyMeal lastMeal = meals.get(meals.size() - 1);
            if (!sdf.format(lastMeal.getDate()).equals(dateString)) {
                meals.add(meal);
                mealAdded = true;
            }
        } else {
            meals.add(meal);
            mealAdded = true;
        }

        return mealAdded;
    }

    // EFFECTS: returns the meal object at a certain date
    // throws DateNotContainedException if the FoodManager doesn't have a meal at that date
    public DailyMeal getMeal(Date date) throws DateNotContainedException {
        DailyMeal foundMeal = null;
        for (DailyMeal meal : meals) {
            if (meal.checkDate(date)) {
                foundMeal = meal;
            }
        }
        if (foundMeal == null) {
            throw new DateNotContainedException();
        }
        return foundMeal;
    }

    // REQUIRES: food is a fully initialized and set up object
    // MODIFIES: this
    // EFFECTS: adds a completed food to the food list
    // throws NotInitializedException if the food has an empty name string
    public void addFood(Food food) throws NotInitializedException {
        if (food.getName().equals("")) {
            throw new NotInitializedException();
        }
        foods.add(food);
    }

    // EFFECTS: returns food object with a name that matches the food name
    public List<Food> getFood(String foodName) {
        List<Food> foundFoods = new ArrayList<>();
        for (Food fd : foods) {
            if (fd.getName().toLowerCase().contains(foodName.toLowerCase())) {
                foundFoods.add(fd);
            }
        }
        return foundFoods;
    }

    public int getCalorieGoal() {
        return calorieGoal;
    }

    // MODIFIES: this
    // EFFECTS: Sets the calorie goal to a positive integer defined by the caller
    // throws NegativeVCalorieGoalException if the calorie goal is set to negative
    public void setCalorieGoal(int calorieGoal) throws NegativeCalorieGoalException {
        this.calorieGoal = calorieGoal;
        if (this.calorieGoal < 0) {
            throw new NegativeCalorieGoalException();
        }
    }

    // EFFECTS: Returns a JSON Object that represents the FoodManager class in the entirety
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray jsonMeals = new JSONArray(this.meals);
        JSONArray jsonFoods = new JSONArray(this.foods);
        json.append("calorieGoal", calorieGoal);
        json.append("meals", jsonMeals);
        json.append("foods", jsonFoods);
        return json;
    }
}
