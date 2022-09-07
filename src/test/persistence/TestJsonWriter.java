package persistence;

import exceptions.DateNotContainedException;
import exceptions.NegativeCalorieGoalException;
import exceptions.NotInitializedException;
import model.calorietracker.DailyMeal;
import model.calorietracker.Food;
import model.calorietracker.FoodManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import static model.Daily.sdf;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Class adapted from the JsonWriterTest class in
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 *
 */
public class TestJsonWriter {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyFoodManager() {
        try {
            FoodManager fm = new FoodManager();

            JsonWriter writer = new JsonWriter("./data/testWriterEmptyFoodManager.json");

            writer.open();
            writer.write(fm);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyFoodManager.json");
            fm = reader.read();
            assertEquals(0, fm.getFood("").size());
            fm.getMeal(new Date());
            fail("No meal should exist at new date");
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (ParseException e) {
            fail("Date should be correctly written");
        } catch (DateNotContainedException dnce) {
            // pass
        } catch (NegativeCalorieGoalException ncge) {
            fail("Calorie goal should not be negative");
        }
    }

    @Test
    void testWriterFoodManager() {
        try {
            FoodManager fm = new FoodManager();
            Food bf = new Food("Eggs");
            Food l = new Food("Sandwich");
            Food d = new Food("Steak");
            int totalCalories = 0;
            DailyMeal meal1 = new DailyMeal();
            DailyMeal meal2 = new DailyMeal();
            DailyMeal meal3 = new DailyMeal();
            JsonWriter writer = new JsonWriter("./data/testWriterFoodManager.json");
            Date date1 = null;
            Date date2 = null;
            Date date3 = null;
            try {
                date1 = sdf.parse("01 01 2020");
                date2 = sdf.parse("01 02 2020");
                date3 = sdf.parse("01 03 2020");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            meal1.setDate(date1);
            meal2.setDate(date2);
            meal3.setDate(date3);

            bf.setCalories(150);
            l.setFat(20);
            l.setCarbs(20);
            l.setProtein(20);
            l.setCalories(l.getCaloriesFromMacros());
            d.setCalories(600);

            totalCalories = bf.getCalories() + l.getCalories() + d.getCalories();

            meal1.addToMeal(DailyMeal.BREAKFAST, bf);
            meal1.addToMeal(DailyMeal.LUNCH, l);
            meal1.addToMeal(DailyMeal.DINNER, d);

            meal2.addToMeal(DailyMeal.LUNCH, bf);
            meal2.addToMeal(DailyMeal.DINNER, l);
            meal2.addToMeal(DailyMeal.BREAKFAST, d);

            meal3.addToMeal(DailyMeal.DINNER, bf);
            meal3.addToMeal(DailyMeal.BREAKFAST, l);
            meal3.addToMeal(DailyMeal.LUNCH, d);

            fm.addMeal(meal1);
            fm.addMeal(meal2);
            fm.addMeal(meal3);

            fm.addFood(bf);
            fm.addFood(l);
            fm.addFood(d);

            writer.open();
            writer.write(fm);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterFoodManager.json");
            fm = reader.read();
            try {
                date1 = sdf.parse("01 01 2020");
                date2 = sdf.parse("01 02 2020");
                date3 = sdf.parse("01 03 2020");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            meal1 = fm.getMeal(date1);
            meal2 = fm.getMeal(date2);
            meal3 = fm.getMeal(date3);

            assertNotNull(meal1);
            assertNotNull(meal2);
            assertNotNull(meal3);

            assertEquals(totalCalories, meal1.getTotalCalories());
            assertEquals(totalCalories, meal2.getTotalCalories());
            assertEquals(totalCalories, meal3.getTotalCalories());
            assertEquals(3, fm.getFood("").size());

            assertEquals("Eggs", meal1.getBreakfast().get(0).getName());
            assertEquals("Sandwich", meal1.getLunch().get(0).getName());
            assertEquals("Steak", meal1.getDinner().get(0).getName());

            assertEquals("Steak", meal2.getBreakfast().get(0).getName());
            assertEquals("Eggs", meal2.getLunch().get(0).getName());
            assertEquals("Sandwich", meal2.getDinner().get(0).getName());

            assertEquals("Sandwich", meal3.getBreakfast().get(0).getName());
            assertEquals("Steak", meal3.getLunch().get(0).getName());
            assertEquals("Eggs", meal3.getDinner().get(0).getName());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (ParseException e) {
            fail("Date should be correctly written");
        } catch (DateNotContainedException dnce) {
            fail("Date should exist within foodmanager");
        } catch (NotInitializedException nie) {
            fail("Names should be initialized for foods");
        } catch (NegativeCalorieGoalException ncge) {
            fail("Calorie goal should not be negative");
        }
    }

}
