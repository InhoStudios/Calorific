package persistence;

import exceptions.DateNotContainedException;
import exceptions.NegativeCalorieGoalException;
import model.calorietracker.DailyMeal;
import model.calorietracker.FoodManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import static model.Daily.sdf;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Class adapted from the JsonReaderTest class in
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 *
 */
public class TestJsonReader {

    @Test
    void testReaderNonExistantFile() {
        JsonReader reader = new JsonReader("./data/inexistant.json");
        try {
            FoodManager fm = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            //pass
        } catch (ParseException pe) {
            fail("IOException should be thrown");
        } catch (NegativeCalorieGoalException ncge) {
            fail("Calorie goal should not be negative");
        }
    }

    @Test
    void testReadFMInvalidDate() {
        JsonReader reader = new JsonReader("./data/testJsonBadDate.json");
        try {
            FoodManager testFM = reader.read();
            fail("Date should throw parseexception");
        } catch (IOException e) {
            fail("File should exist, date should throw parseexception");
        } catch (ParseException e) {
            // pass
        } catch (NegativeCalorieGoalException ncge) {
            fail("Calorie goal should not be negative");
        }
    }

    @Test
    void testReaderEmptyFoodManager() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFoodManager.json");
        try {
            FoodManager fm = reader.read();
            assertEquals(0, fm.getFood("").size());
            assertEquals(2000, fm.getCalorieGoal());
            fm.getMeal(new Date());
            fail("Date should not exist");
        } catch (IOException e) {
            fail("no exception should be thrown");
        } catch (ParseException pe) {
            fail("Date should be in correct format");
        } catch (NegativeCalorieGoalException ncge) {
            fail("Calorie goal should not be negative");
        } catch (DateNotContainedException dnce) {
            // pass
        }

    }

    @Test
    void testReaderFoodManager() {
        FoodManager fm = null;
        int totalCalories = 1110;
        DailyMeal meal1;
        DailyMeal meal2;
        DailyMeal meal3;
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
        JsonReader reader = new JsonReader("./data/testReaderFoodManager.json");
        try {
            fm = reader.read();
        } catch (IOException e) {
            fail("No exception should be caught");
        } catch (ParseException pe) {
            fail("Date should be in correct format");
        } catch (NegativeCalorieGoalException ncge) {
            fail("Calorie goal should be non-negative");
        }
        try {
            date1 = sdf.parse("01 01 2020");
            date2 = sdf.parse("01 02 2020");
            date3 = sdf.parse("01 03 2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
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

            assertEquals(1800, fm.getCalorieGoal());
        } catch (DateNotContainedException e) {
            fail("Date should exist in range");
        }
    }

    @Test
    void testReaderSetCalories() {
        FoodManager fm = new FoodManager();
        assertEquals(2000, fm.getCalorieGoal());

        JsonReader reader = new JsonReader("./data/testReaderFoodManager.json");
        try {
            fm = reader.read();
        } catch (IOException e) {
            fail("No exception should be caught");
        } catch (ParseException pe) {
            fail("Date should be in correct format");
        } catch (NegativeCalorieGoalException ncge) {
            fail("Calorie goal is above 0");
        }
        assertEquals(1800, fm.getCalorieGoal());
    }

    @Test
    void testReaderNegativeCalorieGoal() {
        FoodManager fm = new FoodManager();

        JsonReader reader = new JsonReader("./data/testReaderNegativeCalorieGoal.json");
        try {
            fm = reader.read();
            fail("Calorie goal should be negative");
        } catch (IOException ie) {
            fail("File should exist, no exception should be thrown");
        } catch (ParseException pe) {
            fail("Date should be readable");
        } catch (NegativeCalorieGoalException ncge) {
            // pass;
        }
        assertEquals(2000, fm.getCalorieGoal());
    }

    @Test
    void testReaderNotInitializedFood() {
        FoodManager fm = new FoodManager();

        JsonReader reader = new JsonReader("./data/testReaderNotInitializedFood.json");
        try {
            fm = reader.read();
            assertEquals(0, fm.getFood("").size());
        } catch (IOException ie) {
            fail("File should exist, no exception should be thrown");
        } catch (ParseException pe) {
            fail("Date should be readable");
        } catch (NegativeCalorieGoalException ncge) {
            fail("Calorie goal should be non-negative");
        }
    }

}
