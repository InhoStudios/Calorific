package ui.views;

import exceptions.DateNotContainedException;
import model.Daily;
import model.calorietracker.DailyMeal;
import model.calorietracker.FoodManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * CalorieGraph represents a graph that shows all the calorie counts in respect to one another for a certain week
 *
 * CalorieGraph is constructed with a FoodManager, a width, and a height. It extends JPanel and utilizes the
 * Graphics object to draw shapes to the panel
 *
 */

public class CalorieGraph extends JPanel {

    private FoodManager foodManager;
    private ArrayList<DailyMeal> sevenDayMeal;

    private Date currentDate;

    private int width;
    private int height;

    // REQUIRES: foodManager is not null, width and height are > 0
    // MODIFIES: this
    // EFFECTS: constructs a new CalorieGraph
    public CalorieGraph(FoodManager foodManager, int width, int height) {
        super();
        this.width = width;
        this.height = height;
        Dimension size = new Dimension(width, height);
        setSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        this.foodManager = foodManager;
    }

    // REQUIRES: nothing
    // MODIFIES: nothing
    // EFFECTS: Creates an arraylist of meals surrounding the current date
    public ArrayList<DailyMeal> getCurrentWeekFoods(Date date) {
        ArrayList<DailyMeal> weekMeals = new ArrayList<>();
        for (int i = -3; i < 4; i++) {
            DailyMeal dm;
            try {
                dm = foodManager.getMeal(getDateFromIncrement(i, date));
            } catch (DateNotContainedException dnce) {
                dm = new DailyMeal();
                dm.setDate(date);
                foodManager.addMeal(dm);
            }
            weekMeals.add(dm);
        }
        return weekMeals;
    }

    // REQUIRES: currentDate is initialized
    // MODIFIES: nothing
    // EFFECTS: returns a date a certain distance away from the current date
    public Date getDateFromIncrement(int increment, Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, increment);
        return calendar.getTime();
    }

    // REQUIRES: date is initialized
    // MODIFIES: nothing
    // EFFECTS: renders calorie counts for each day in a week surrounding the current day
    public void renderDates(Date date) {
        currentDate = date;
        sevenDayMeal = getCurrentWeekFoods(currentDate);
        repaint();
    }

    // REQUIRES: nothing
    // MODIFIES: nothing
    // EFFECTS: paints each bar in the graph with corresponding size and colours
    // Overrides paintComponent in JPanel class
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int rectWidth = width / 7;

        for (int i = 0; i < 7; i++) {
            DailyMeal curMeal = sevenDayMeal.get(i);
            if (curMeal == null) {
                curMeal = new DailyMeal();
                curMeal.setDate(getDateFromIncrement(i - 3, currentDate));
                foodManager.addMeal(curMeal);
            }

            int xx = rectWidth * i;
            double yy = height * (((double) foodManager.getCalorieGoal() - (double) curMeal
                    .getTotalCalories()) / (double) foodManager.getCalorieGoal());
            int rectHeight = height - (int) yy;

            g.setColor(Color.LIGHT_GRAY);
            if (curMeal.checkDate(currentDate)) {
                g.setColor(Color.BLUE);
                if (yy < 0) {
                    g.setColor(Color.RED);
                }
            }

            createBars(g, xx, (int) yy, rectWidth, rectHeight);
        }

    }

    // REQUIRES: width == this.width/7
    // MODIFIES: nothing
    // EFFECTS: Draws filled in rectangles with outlines according to specified dimensions
    private void createBars(Graphics g, int x, int y, int width, int height) {
        g.fillRect(x, y, width, height);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, y, width, height);
    }

}
