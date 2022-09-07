package model;

import exceptions.DateNotContainedException;
import exceptions.NegativeCalorieGoalException;
import exceptions.NotInitializedException;
import model.calorietracker.DailyMeal;
import model.calorietracker.Food;
import model.calorietracker.FoodManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

public class TestFoodManager {

    FoodManager fm;

    @BeforeEach
    public void runBefore() {
        fm = new FoodManager();
    }

    @Test
    public void testAddMeal() {
        DailyMeal meal = new DailyMeal();
        assertTrue(fm.addMeal(meal));

        DailyMeal meal2 = new DailyMeal();
        assertFalse(fm.addMeal(meal2));
    }

    @Test
    public void testGetMealInList() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        DailyMeal meal1 = new DailyMeal();
        DailyMeal meal2 = new DailyMeal();
        DailyMeal meal3 = new DailyMeal();
        DailyMeal meal4 = new DailyMeal();
        DailyMeal meal5 = new DailyMeal();
        try {
            meal1.setDate(sdf.parse("01 01 2020"));
            meal2.setDate(sdf.parse("01 02 2020"));
            meal3.setDate(sdf.parse("01 03 2020"));
            meal4.setDate(sdf.parse("01 04 2020"));
            meal5.setDate(sdf.parse("01 05 2020"));
            assertTrue(fm.addMeal(meal1));
            assertTrue(fm.addMeal(meal2));
            assertTrue(fm.addMeal(meal3));
            assertTrue(fm.addMeal(meal4));
            assertTrue(fm.addMeal(meal5));
            assertEquals(meal1, fm.getMeal(sdf.parse("01 01 2020")));
            assertEquals(meal2, fm.getMeal(sdf.parse("01 02 2020")));
            assertEquals(meal3, fm.getMeal(sdf.parse("01 03 2020")));
            assertEquals(meal4, fm.getMeal(sdf.parse("01 04 2020")));
            assertEquals(meal5, fm.getMeal(sdf.parse("01 05 2020")));
        } catch (ParseException | DateNotContainedException e) {
            e.printStackTrace();
            fail("No exception should be thrown");
        }
    }

    @Test
    public void testGetMealNotInList() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        try {
            fm.getMeal(sdf.parse("01 10 2020"));
            fail("No meal should exist at date, test should reach exception.");
        } catch (ParseException e) {
            fail("Date format should be parseable");
        } catch (DateNotContainedException dnce) {
            // pass
        }
    }

    @Test
    public void testAddFood() {
        Food food = new Food("Food");
        try {
            fm.addFood(food);
            assertEquals(food, fm.getFood("Food").get(0));
            assertEquals("Food", fm.getFood("Food").get(0).getName());
        } catch (NotInitializedException e) {
            fail("No error should be thrown");
        }
    }

    @Test
    public void testGetFood() {
        Food food1 = new Food("A food");
        Food food2 = new Food("B Food");
        Food food3 = new Food("C food");

        try {
            fm.addFood(food1);
            fm.addFood(food2);
            fm.addFood(food3);
        } catch (NotInitializedException e) {
            fail("Names initialized, no exceptions should be thrown");
        }

        assertEquals(food1, fm.getFood("A food").get(0));
        assertEquals("A food", fm.getFood("A food").get(0).getName());
        assertEquals(food1, fm.getFood("a Food").get(0));
        assertEquals("A food", fm.getFood("a Food").get(0).getName());

        assertEquals(food2, fm.getFood("B Food").get(0));
        assertEquals("B Food", fm.getFood("B Food").get(0).getName());
        assertEquals(food2, fm.getFood("b food").get(0));
        assertEquals("B Food", fm.getFood("b food").get(0).getName());

        assertEquals(food3, fm.getFood("C food").get(0));
        assertEquals("C food", fm.getFood("C food").get(0).getName());
        assertEquals(food3, fm.getFood("c food").get(0));
        assertEquals("C food", fm.getFood("c food").get(0).getName());
    }

    @Test
    public void testGetAndSetCalorieGoal() {
        assertEquals(2000, fm.getCalorieGoal());
        try {
            fm.setCalorieGoal(1000);
            assertEquals(1000, fm.getCalorieGoal());
        } catch (NegativeCalorieGoalException e) {
            fail("1000 is non negative, no exception should be thrown");
        }
    }

    @Test
    public void testNegativeCalorieGoal() {
        try {
            fm.setCalorieGoal(-100);
            fail("Negative calorie goal should throw exception");
        } catch (NegativeCalorieGoalException ncge) {
            // pass
        }
    }

    @Test
    public void testNewFoodNoName() {
        Food food = new Food("");
        try {
            fm.addFood(food);
            fail("NotInitializedException should be thrown");
        } catch (NotInitializedException nie) {
            // pass
        }
    }

}
