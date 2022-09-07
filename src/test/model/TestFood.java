package model;

import model.calorietracker.Food;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestFood {

    Food food;

    @BeforeEach
    public void runBefore() {
        food = new Food("food");
    }

    @Test
    public void testConstructFood() {
        assertEquals("food", food.getName());
        assertEquals(0, food.getCalories());
        assertEquals(0, food.getCarbs());
        assertEquals(0, food.getFat());
        assertEquals(0, food.getProtein());
    }

    @Test
    public void testGetName() {
        String foodName = "This is a food";
        Food food = new Food(foodName);
        assertEquals(foodName, food.getName());
    }

    @Test
    public void testSetCalories() {
        int calories = 50;
        assertTrue(food.setCalories(calories));
        assertEquals(calories, food.getCalories());

        calories = -50;
        assertFalse(food.setCalories(calories));
        assertEquals(50, food.getCalories());
    }

    @Test
    public void testGetCalories() {
        int calories = 30;
        assertEquals(0, food.getCalories());
        assertTrue(food.setCalories(calories));
        assertEquals(calories, food.getCalories());
    }

    @Test
    public void testSetFat() {
        int fat = 20;
        assertTrue(food.setFat(fat));
        assertEquals(fat, food.getFat());

        fat = -20;
        assertFalse(food.setFat(fat));
        assertEquals(20, food.getFat());
    }

    @Test
    public void testGetFat() {
        int fat = 50;
        assertEquals(0, food.getFat());
        assertTrue(food.setFat(fat));
        assertEquals(fat, food.getFat());
    }

    @Test
    public void testSetCarbs() {
        int carbs = 50;
        assertTrue(food.setCarbs(carbs));
        assertEquals(carbs, food.getCarbs());

        carbs = -50;
        assertFalse(food.setCarbs(carbs));
        assertEquals(50, food.getCarbs());
    }

    @Test
    public void testGetCarbs() {
        int carbs = 50;
        assertEquals(0, food.getCarbs());
        assertTrue(food.setCarbs(carbs));
        assertEquals(carbs, food.getCarbs());
    }

    @Test
    public void testSetProtein() {
        int protein = 50;
        assertTrue(food.setProtein(protein));
        assertEquals(protein, food.getProtein());

        protein = -50;
        assertFalse(food.setProtein(protein));
        assertEquals(50, food.getProtein());
    }

    @Test
    public void testGetCaloriesFromNoMacros() {
        assertEquals(0, food.getCaloriesFromMacros());
        int fat = 10;
        int protein = 10;
        int carbs = 10;
        int calories = 10 * fat + 4 * protein + 4 * carbs;

        assertTrue(food.setFat(fat));
        assertTrue(food.setCarbs(carbs));
        assertTrue(food.setProtein(protein));
        assertEquals(calories, food.getCaloriesFromMacros());
    }

    @Test
    public void testVerifyCaloriesEqual() {
        int fat = 10;
        int protein = 10;
        int carbs = 10;
        int calories = 10 * fat + 4 * protein + 4 * carbs;

        assertTrue(food.setFat(fat));
        assertTrue(food.setCarbs(carbs));
        assertTrue(food.setProtein(protein));
        assertTrue(food.setCalories(calories));

        assertTrue(food.verifyCalories());

        assertTrue(food.setCalories(calories + 4));
        assertTrue(food.verifyCalories());

        assertTrue(food.setCalories(calories + 11));
        assertFalse(food.verifyCalories());

        assertTrue(food.setCalories(calories - 1));
        assertFalse(food.verifyCalories());
    }

    @Test
    public void testVerifyCaloriesNonEqual() {
        int fat = 10;
        int protein = 10;
        int carbs = 10;
        int calories = 5000000;

        assertTrue(food.setFat(fat));
        assertTrue(food.setCarbs(carbs));
        assertTrue(food.setProtein(protein));
        assertTrue(food.setCalories(calories));

        assertFalse(food.verifyCalories());
    }
}
