package model;

import model.calorietracker.DailyMeal;
import model.calorietracker.Food;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TestDailyMeal {

    DailyMeal dailyMeal;

    @BeforeEach
    public void createDailyMeal() {
        dailyMeal = new DailyMeal();
    }

    @Test
    public void testAddBreakfast() {
        Food food = new Food("breakfast");
        assertTrue(dailyMeal.addToMeal(DailyMeal.BREAKFAST, food));
        assertEquals(1, dailyMeal.getBreakfast().size());
        assertEquals(food, dailyMeal.getBreakfast().get(0));
    }

    @Test
    public void testAddLunch() {
        Food food = new Food("lunch");
        assertTrue(dailyMeal.addToMeal(DailyMeal.LUNCH, food));
        assertEquals(1, dailyMeal.getLunch().size());
        assertEquals(food, dailyMeal.getLunch().get(0));
    }

    @Test
    public void testAddDinner() {
        Food food = new Food("dinner");
        assertTrue(dailyMeal.addToMeal(DailyMeal.DINNER, food));
        assertEquals(1, dailyMeal.getDinner().size());
        assertEquals(food, dailyMeal.getDinner().get(0));
    }

    @Test
    public void testAddUndefinedMeal() {
        Food food = new Food("Food");
        assertFalse(dailyMeal.addToMeal((short) 3, food));
    }

    @Test
    public void testGetBreakfast() {
        for(int i = 0; i < 5; i++) {
            assertTrue(dailyMeal.addToMeal(DailyMeal.BREAKFAST, new Food("Food")));
            assertEquals(i + 1, dailyMeal.getBreakfast().size());
        }
        assertEquals(5, dailyMeal.getBreakfast().size());
        for(int i = 0; i < 5; i++) {
            assertEquals("Food", dailyMeal.getBreakfast().get(i).getName());
        }
    }

    @Test
    public void testGetLunch() {
        for(int i = 0; i < 5; i++) {
            assertTrue(dailyMeal.addToMeal(DailyMeal.LUNCH, new Food("Food")));
            assertEquals(i + 1, dailyMeal.getLunch().size());
        }
        assertEquals(5, dailyMeal.getLunch().size());
        for(int i = 0; i < 5; i++) {
            assertEquals("Food", dailyMeal.getLunch().get(i).getName());
        }
    }

    @Test
    public void testGetDinner() {
        for(int i = 0; i < 5; i++) {
            assertTrue(dailyMeal.addToMeal(DailyMeal.DINNER, new Food("Food")));
            assertEquals(i + 1, dailyMeal.getDinner().size());
        }
        assertEquals(5, dailyMeal.getDinner().size());
        for(int i = 0; i < 5; i++) {
            assertEquals("Food", dailyMeal.getDinner().get(i).getName());
        }
    }

    @Test
    public void testGetTotalCalories() {
        Food breakfast = new Food("Breakfast");
        Food lunch = new Food("Lunch");
        Food dinner = new Food("Dinner");

        int breakfastCals = 150;
        int lunchCals = 200;
        int dinnerCals = 250;

        breakfast.setCalories(breakfastCals);
        lunch.setCalories(lunchCals);
        dinner.setCalories(dinnerCals);

        dailyMeal.addToMeal(DailyMeal.BREAKFAST, breakfast);

        assertEquals(breakfastCals, dailyMeal.getTotalCalories());

        dailyMeal.addToMeal(DailyMeal.LUNCH, lunch);

        assertEquals(breakfastCals + lunchCals, dailyMeal.getTotalCalories());

        dailyMeal.addToMeal(DailyMeal.DINNER, dinner);

        assertEquals(breakfastCals + lunchCals + dinnerCals, dailyMeal.getTotalCalories());
    }

    @Test
    public void testGetMacrosFat() {
        Food breakfast = new Food("Breakfast");
        Food lunch = new Food("Lunch");
        Food dinner = new Food("Dinner");

        int breakfastFat = 150;
        int lunchFat = 200;
        int dinnerFat = 250;

        breakfast.setFat(breakfastFat);
        lunch.setFat(lunchFat);
        dinner.setFat(dinnerFat);

        dailyMeal.addToMeal(DailyMeal.BREAKFAST, breakfast);

        assertEquals(breakfastFat, dailyMeal.getTotalMacros(DailyMeal.FAT));

        breakfast.setProtein(breakfastFat);
        breakfast.setCarbs(breakfastFat);

        assertEquals(breakfastFat, dailyMeal.getTotalMacros(DailyMeal.FAT));

        dailyMeal.addToMeal(DailyMeal.LUNCH, lunch);

        assertEquals(breakfastFat + lunchFat, dailyMeal.getTotalMacros(DailyMeal.FAT));

        dailyMeal.addToMeal(DailyMeal.DINNER, dinner);

        assertEquals(breakfastFat + lunchFat + dinnerFat, dailyMeal.getTotalMacros(DailyMeal.FAT));
    }

    @Test
    public void testGetMacrosCarbs() {
        Food breakfast = new Food("Breakfast");
        Food lunch = new Food("Lunch");
        Food dinner = new Food("Dinner");

        int breakfastCarbs = 150;
        int lunchCarbs = 200;
        int dinnerCarbs = 250;

        breakfast.setCarbs(breakfastCarbs);
        lunch.setCarbs(lunchCarbs);
        dinner.setCarbs(dinnerCarbs);

        dailyMeal.addToMeal(DailyMeal.BREAKFAST, breakfast);

        assertEquals(breakfastCarbs, dailyMeal.getTotalMacros(DailyMeal.CARBS));

        breakfast.setProtein(breakfastCarbs);
        breakfast.setFat(breakfastCarbs);

        assertEquals(breakfastCarbs, dailyMeal.getTotalMacros(DailyMeal.CARBS));

        dailyMeal.addToMeal(DailyMeal.LUNCH, lunch);

        assertEquals(breakfastCarbs + lunchCarbs, dailyMeal.getTotalMacros(DailyMeal.CARBS));

        dailyMeal.addToMeal(DailyMeal.DINNER, dinner);

        assertEquals(breakfastCarbs + lunchCarbs + dinnerCarbs, dailyMeal.getTotalMacros(DailyMeal.CARBS));
    }

    @Test
    public void testGetMacrosProtein() {
        Food breakfast = new Food("Breakfast");
        Food lunch = new Food("Lunch");
        Food dinner = new Food("Dinner");

        int breakfastProtein = 150;
        int lunchProtein = 200;
        int dinnerProtein = 250;

        breakfast.setProtein(breakfastProtein);
        lunch.setProtein(lunchProtein);
        dinner.setProtein(dinnerProtein);

        dailyMeal.addToMeal(DailyMeal.BREAKFAST, breakfast);

        assertEquals(breakfastProtein, dailyMeal.getTotalMacros(DailyMeal.PROTEIN));

        breakfast.setFat(breakfastProtein);
        breakfast.setCarbs(breakfastProtein);

        assertEquals(breakfastProtein, dailyMeal.getTotalMacros(DailyMeal.PROTEIN));

        dailyMeal.addToMeal(DailyMeal.LUNCH, lunch);

        assertEquals(breakfastProtein + lunchProtein, dailyMeal.getTotalMacros(DailyMeal.PROTEIN));

        dailyMeal.addToMeal(DailyMeal.DINNER, dinner);

        assertEquals(breakfastProtein + lunchProtein + dinnerProtein,
                dailyMeal.getTotalMacros(DailyMeal.PROTEIN));
    }

    @Test
    public void testGetMacrosDefaultCase() {
        Food food = new Food("Food");
        food.setCalories(100);
        food.setProtein(100);
        food.setCarbs(100);
        food.setFat(100);
        dailyMeal.addToMeal(DailyMeal.BREAKFAST, food);
        dailyMeal.addToMeal(DailyMeal.LUNCH, food);
        dailyMeal.addToMeal(DailyMeal.DINNER, food);
        assertEquals(-1, dailyMeal.getTotalMacros((short) 5));
    }


    @Test
    public void testGetDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        Date newDate = new Date();
        assertEquals(sdf.format(newDate), sdf.format(dailyMeal.getDate()));
    }

    @Test
    public void testSetNewDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        String dateString = "07 09 2020";
        try {
            Date newDate = sdf.parse(dateString);
            assertNotEquals(sdf.format(newDate), sdf.format(dailyMeal.getDate()));
            dailyMeal.setDate(newDate);
            assertEquals(sdf.format(newDate), sdf.format(dailyMeal.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
