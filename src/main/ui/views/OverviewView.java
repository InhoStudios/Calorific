package ui.views;

import exceptions.DateNotContainedException;
import exceptions.NegativeCalorieGoalException;
import model.Daily;
import model.calorietracker.DailyMeal;
import model.calorietracker.Food;
import model.calorietracker.FoodManager;
import ui.AddFoodFrame;
import ui.CalorificUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * OverviewView represents the view showing all necessary data for the current day
 *
 * Overview requires a parent, foodmanager, and parentFrame parameter in it's constructor.
 * It extends the view and creates three cards for viewing information related to the days foods
 *
 */

public class OverviewView extends View {

    private JButton nextArrow;
    private JButton prevArrow;

    private JTextField dateField;

    private JComboBox<String> mealComboBox;

    private JList<String> foodsList;

    private JProgressBar calorieProgress;

    private JTextField calorieCount;
    private JTextField calorieGoal;
    private JTextField fatCount;
    private JTextField carbCount;
    private JTextField proteinCount;

    private JLabel calorieLabel;
    private JLabel fatLabel;
    private JLabel carbsLabel;
    private JLabel proteinLabel;

    private JButton addFood;

    private DailyMeal today;

    private ArrayList<Object> allMealNames;
    private List<Food> allFoods;

    private Date currentDate;

    private int calGoals;

    // REQUIRES: parent, foodManager, parentFrame != null
    // MODIFIES: this
    // EFFECTS: constructs a new OverviewView with a foodManager and a reference to it's container panel and frame
    public OverviewView(JComponent parent, FoodManager foodManager, JFrame parentFrame) {
        super(parent, foodManager, parentFrame);
        allMealNames = new ArrayList<>();
        allFoods = new ArrayList<>();
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: initializes all components
    @Override
    public void init() {
        prevArrow = new JButton("<");
        dateField = new JTextField("Date: ");
        nextArrow = new JButton(">");

        String[] meals = {"All foods", "Breakfast", "Lunch", "Dinner"};
        mealComboBox = new JComboBox<>(meals);

        String[] placeholderFoods = {"No Foods"};
        foodsList = new JList<>(placeholderFoods);

        calorieProgress = new JProgressBar();

        calorieGoal = new JTextField();

        calorieCount = new JTextField();
        fatCount = new JTextField();
        carbCount = new JTextField();
        proteinCount = new JTextField();

        calorieLabel = new JLabel("Calories");
        fatLabel = new JLabel("Fat");
        carbsLabel = new JLabel("Carbs");
        proteinLabel = new JLabel("Protein");

        addFood = new JButton("Add Food");

        setListeners();
        initComponentProperties();
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: modifies properties of certain textfields and components
    private void initComponentProperties() {
        dateField.setEditable(false);
        calorieCount.setEditable(false);
        fatCount.setEditable(false);
        carbCount.setEditable(false);
        proteinCount.setEditable(false);

    }

    // REQUIRES: nothing
    // MODIFIES: parent
    // EFFECTS: creates both cards in this view and adds it to the parent panel
    @Override
    public void createCards(JComponent parent) {
        createDateNavCard(parent);

        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new GridLayout());
        createFoodListCard(secondPanel);
        createNutritionalInfoCard(secondPanel);

        parent.add(secondPanel);
    }

    // REQUIRES: nothing
    // MODIFIES: parent
    // EFFECTS: creates the card containing the navigation between dates
    private void createDateNavCard(JComponent parent) {
        JPanel dateNav = new JPanel();
        dateNav.setLayout(new GridLayout(0, 3));

        dateNav.add(prevArrow);
        dateNav.add(dateField);
        dateNav.add(nextArrow);

        parent.add(dateNav);
    }

    // REQUIRES: nothing
    // MODIFIES: parent
    // EFFECTS: creates a card that lists all of the foods in the day
    private void createFoodListCard(JComponent parent) {
        JPanel foodList = new JPanel();
        foodList.setLayout(new GridLayout(2, 1));

        foodList.add(mealComboBox);
        foodList.add(foodsList);

        parent.add(foodList);
    }

    // REQUIRES: nothing
    // MODIFIES: parent
    // EFFECTS: creates a card that has fields for all of the nutritional information of a certain food
    private void createNutritionalInfoCard(JComponent parent) {
        JPanel nutInfoPanel = new JPanel();
        nutInfoPanel.setLayout(new GridLayout(6, 2));

        nutInfoPanel.add(calorieProgress);

        nutInfoPanel.add(calorieGoal);

        nutInfoPanel.add(calorieLabel);
        nutInfoPanel.add(fatLabel);

        nutInfoPanel.add(calorieCount);
        nutInfoPanel.add(fatCount);

        nutInfoPanel.add(carbsLabel);
        nutInfoPanel.add(proteinLabel);

        nutInfoPanel.add(carbCount);
        nutInfoPanel.add(proteinCount);
        nutInfoPanel.add(addFood);

        parent.add(nutInfoPanel);

    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: loads all nutritional information into fields and progress bars for specified day
    public void loadInfo(Date date) {
        currentDate = date;

        try {
            today = foodManager.getMeal(currentDate);
        } catch (DateNotContainedException dnce) {
            System.out.println("No values for this date");
            today = new DailyMeal();
            today.setDate(currentDate);
            foodManager.addMeal(today);
        } finally {
            fillAllFoods();
            dateField.setText(Daily.legibleDateFormat.format(date));
            fillAllFoods();

            calGoals = foodManager.getCalorieGoal();
            calorieGoal.setText(calGoals + "");
        }
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: loads allMealNames and allFoods with the names and foods respectively,
    // loads all of the nutritional value into fields
    public void fillAllFoods() {
        allMealNames = new ArrayList<>();
        allFoods = new ArrayList<>();

        if (today != null) {
            addToFoodNames(today.getBreakfast(), allMealNames);
            addToFoodNames(today.getLunch(), allMealNames);
            addToFoodNames(today.getDinner(), allMealNames);

            addToFoodList(today.getBreakfast(), allFoods);
            addToFoodList(today.getLunch(), allFoods);
            addToFoodList(today.getDinner(), allFoods);
        }

        foodsList.setListData(allMealNames.toArray(new String[0]));

        fillNutritionalValue(today.getTotalCalories(),
                today.getTotalMacros(DailyMeal.FAT),
                today.getTotalMacros(DailyMeal.CARBS),
                today.getTotalMacros(DailyMeal.PROTEIN));

        updateProgressBar(today.getTotalCalories());

    }

    // REQUIRES: calories, fat, carbs, and protein are all >= 0
    // MODIFIES: this
    // EFFECTS: fills each text field with it's respective nutritional values for either a selected food or all foods
    private void fillNutritionalValue(int calories, int fat, int carbs, int protein) {
        calorieCount.setText(calories + "cal");
        fatCount.setText(fat + "g");
        carbCount.setText(carbs + "g");
        proteinCount.setText(protein + "g");

    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: replaces fields with nutritional value of one specific food
    private void replaceFood() {
        int index = foodsList.getSelectedIndex();
        if (index >= 0 && index <= foodsList.getMaxSelectionIndex()) {
            Food thisFood = allFoods.get(index);

            fillNutritionalValue(thisFood.getCalories(),
                    thisFood.getFat(),
                    thisFood.getCarbs(),
                    thisFood.getProtein());
        }
    }

    // REQUIRES: progress >= 0
    // MODIFIES: this
    // EFFECTS: resets calorie goals and updates the progress bar and the progress bar text
    private void updateProgressBar(int progress) {
        try {
            calGoals = Integer.parseInt(calorieGoal.getText());
        } catch (NumberFormatException nfe) {
            System.out.println("Empty or non integer value");
            calGoals = 2000;
        }
        calorieProgress.setMaximum(calGoals);
        calorieProgress.setValue(progress);

        calorieProgress.setString(progress + "/" + calGoals + "cal");
        calorieProgress.setStringPainted(true);
    }

    // REQUIRES: list and toModify are not null
    // MODIFIES: toModify
    // EFFECTS: adds all foods in a food list to one master list and returns it
    private List<Food> addToFoodList(List<Food> list, List<Food> toModify) {
        for (Food food: list) {
            toModify.add(food);
        }
        return toModify;

    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: add all event listeners
    private void setListeners() {
        setFoodListListeners();
        setCalorieGoalListeners();
        setMealComboBoxListeners();
        setNavArrowListener();
        setAddFoodListener();
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds all event listeners for the food list
    private void setFoodListListeners() {
        foodsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                replaceFood();
            }
        });

    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds event listeners for the calorie goal text box
    private void setCalorieGoalListeners() {
        calorieGoal.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                updateProgressBar(today.getTotalCalories());
                try {
                    foodManager.setCalorieGoal(calGoals);
                } catch (NegativeCalorieGoalException e) {
                    System.out.println("Negative Calorie Goals");
                }
                ((CalorificUI) parentFrame).getCalorieGraph().renderDates(currentDate);
            }
        });
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds event listeners for the jcombobox
    private void setMealComboBoxListeners() {
        mealComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (mealComboBox.getSelectedIndex()) {
                    case 0:
                        fillAllFoods();
                        break;
                    case 1:
                        allFoods = today.getBreakfast();
                        break;
                    case 2:
                        allFoods = today.getLunch();
                        break;
                    case 3:
                        allFoods = today.getDinner();
                        break;
                }
                ArrayList<Object> thisMeal = new ArrayList<>();
                thisMeal = addToFoodNames(allFoods, thisMeal);
                foodsList.setListData(thisMeal.toArray(new String[0]));
                replaceFood();
            }
        });
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds event listeners for the navigation arrows
    private void setNavArrowListener() {
        prevArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                incrementCalendar(-1);
            }
        });
        nextArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                incrementCalendar(1);
            }
        });
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: sets the currentDate up by one and refreshes UI with new information
    private void incrementCalendar(int increment) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, increment);
        currentDate = calendar.getTime();
        loadInfo(currentDate);
        CalorificUI calUI = (CalorificUI) parentFrame;
        calUI.getCalorieGraph().renderDates(currentDate);
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds listener for the add food button
    private void setAddFoodListener() {
        addFood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createFoodFrame();
            }
        });
    }

    // REQUIRES: nothing
    // MODIFIES: nothing
    // EFFECTS: creates a new AddFoodFrame to add a new food to the foodlist
    private void createFoodFrame() {
        new AddFoodFrame(foodManager, currentDate, this);
    }

    public Date getCurrentDate() {
        return currentDate;
    }

}
