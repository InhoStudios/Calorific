package persistence;

import exceptions.NegativeCalorieGoalException;
import exceptions.NotInitializedException;
import model.calorietracker.DailyMeal;
import model.calorietracker.Food;
import model.calorietracker.FoodManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Class adapted from the JsonReader class in
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 *
 * Utility to read Json files and initialize objects
 *
 */
public class JsonReader {
    private String source;
    private SimpleDateFormat defaultDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

    // EFFECTS: Constructs a new JsonReader object with a filepath field
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: Reads the json file stored at source as a string
    private String readJsonFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: reads FoodManager from file and returns a new object with that data
    //          throws IOException if an error occurs while reading data
    public FoodManager read() throws IOException, ParseException, NegativeCalorieGoalException {
        String jsonData = readJsonFile(source);
        JSONObject jsonFM = new JSONObject(jsonData);
        return parseFoodManager(jsonFM);
    }

    // EFFECTS: parses a FoodManager from a json object and returns it
    private FoodManager parseFoodManager(JSONObject jsonFM) throws ParseException, NegativeCalorieGoalException {
        FoodManager fm = new FoodManager();
        try {
            int calorieGoals = jsonFM.getJSONArray("calorieGoal").getInt(0);
            System.out.println(calorieGoals);
            fm.setCalorieGoal(calorieGoals);
        } catch (JSONException jse) {
            System.out.println("No calorie goal set");
        }
        JSONArray jsonMeals = jsonFM.getJSONArray("meals").getJSONArray(0);
        JSONArray jsonFoods = jsonFM.getJSONArray("foods").getJSONArray(0);
        addMeals(fm, jsonMeals);
        addFoods(fm, jsonFoods);

        return fm;
    }

    // MODIFIES: foodManager
    // EFFECTS: parses meals from a json array and adds them to the foodmanager
    private void addMeals(FoodManager foodManager, JSONArray meals) throws ParseException {
        for (Object jsonMeal : meals) {
            JSONObject thisMeal = (JSONObject) jsonMeal;
            addMeal(foodManager, thisMeal);
        }
    }

    // MODIFIES: foodManager
    // EFFECTS: parses meal from a json object and adds it to the foodmanager
    private void addMeal(FoodManager foodManager, JSONObject day) throws ParseException {
        DailyMeal thisMeal = new DailyMeal();
        Date mealDate = defaultDateFormat.parse(day.getString("date"));
        thisMeal.setDate(mealDate);
        JSONArray breakfast = day.getJSONArray("breakfast");
        JSONArray lunch = day.getJSONArray("lunch");
        JSONArray dinner = day.getJSONArray("dinner");

        for (Object food : breakfast) {
            JSONObject thisFood = (JSONObject) food;
            thisMeal.addToMeal(DailyMeal.BREAKFAST, getFoodFromJson(thisFood));
        }
        for (Object food : lunch) {
            JSONObject thisFood = (JSONObject) food;
            thisMeal.addToMeal(DailyMeal.LUNCH, getFoodFromJson(thisFood));
        }
        for (Object food : dinner) {
            JSONObject thisFood = (JSONObject) food;
            thisMeal.addToMeal(DailyMeal.DINNER, getFoodFromJson(thisFood));
        }

        foodManager.addMeal(thisMeal);
    }

    // MODIFIES: foodManager
    // EFFECTS: parses foods from a json array and adds them to the foodmanager
    private void addFoods(FoodManager foodManager, JSONArray foods) {
        for (Object jsonFood : foods) {
            JSONObject thisFood = (JSONObject) jsonFood;
            try {
                foodManager.addFood(getFoodFromJson(thisFood));
            } catch (NotInitializedException e) {
                System.out.println("Food found without name");
            }
        }

    }

    // EFFECTS: parses food from a json object and returns it
    private Food getFoodFromJson(JSONObject food) {
        String name = food.getString("name");
        int fat = food.getInt("fat");
        int protein = food.getInt("protein");
        int carbs = food.getInt("carbs");
        int calories = food.getInt("calories");
        int caloriesFromMacros = food.getInt("caloriesFromMacros");

        Food thisFood = new Food(name);
        thisFood.setFat(fat);
        thisFood.setCarbs(carbs);
        thisFood.setProtein(protein);
        thisFood.setCalories(calories);

        return thisFood;
    }

}
