package ui.views;

import exceptions.DateNotContainedException;
import exceptions.NotInitializedException;
import model.calorietracker.DailyMeal;
import model.calorietracker.Food;
import model.calorietracker.FoodManager;
import ui.AddFoodFrame;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

/**
 * AddFoodView models a view that contains JComponents required to add a new food
 *
 * AddFoodView requires a parent, foodManager, and parentFrame parameter in the constructor.
 * It extends the View, which models a series of JComponents used to represent a certain user experience
 *
 */

public class AddFoodView extends View {

    private JTextField nameField;

    private JTextField calorieField;
    private JTextField fatField;
    private JTextField carbField;
    private JTextField proteinField;

    private JButton saveButton;

    private JTextField searchField;
    private JList<String> searchedFoodsList;

    private JComboBox<String> mealTypeBox;

    private List<Food> searchedFoods;
    private ArrayList<Object> foodNames;

    private Food selectedFood;

    // REQUIRES: parent, foodManager, and parentFrame are all not null
    // MODIFIES: this
    // EFFECTS: constructs a new AddFoodView
    public AddFoodView(JComponent parent, FoodManager foodManager, JFrame parentFrame) {
        super(parent, foodManager, parentFrame);
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: initializes all fields and components
    public void init() {
        nameField = new JTextField("Name");

        calorieField = new JTextField("Calories");
        fatField = new JTextField("Fat (g)");
        carbField = new JTextField("Carbs (g)");
        proteinField = new JTextField("Protein (g)");

        saveButton = new JButton("Save food");
        searchField = new JTextField("Search for food");
        searchedFoodsList = new JList<>();

        String[] options = {"Breakfast", "Lunch", "Dinner"};
        mealTypeBox = new JComboBox<>(options);

        setListeners();
    }

    // REQUIRES: parent != null
    // MODIFIES: parent
    // EFFECTS: creates all cards and panels and adds them to the JComponent parent
    public void createCards(JComponent parent) {
        createPastFoodsCard(parent);
        createNewFoodCard(parent);
    }

    // REQUIRES: nothing
    // MODIFIES: parent
    // EFFECTS: constructs the card representing the search bar and list of previously added foods
    public void createPastFoodsCard(JComponent parent) {
        JPanel pastFoodsCard = new JPanel();
        pastFoodsCard.setLayout(new GridLayout(3, 1));

        pastFoodsCard.add(mealTypeBox);
        pastFoodsCard.add(searchField);
        pastFoodsCard.add(searchedFoodsList);

        parent.add(pastFoodsCard);
    }

    // REQUIRES: nothing
    // MODIFIES: parent
    // EFFECTS: constructs the card representing a panel with fields to construct a new food
    public void createNewFoodCard(JComponent parent) {
        JPanel foodNutrition = new JPanel();
        foodNutrition.setLayout(new GridLayout(3, 2));

        foodNutrition.add(nameField);
        foodNutrition.add(calorieField);
        foodNutrition.add(fatField);
        foodNutrition.add(carbField);
        foodNutrition.add(proteinField);

        foodNutrition.add(saveButton);

        parent.add(foodNutrition);
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds all listeners to JComponents and adds their functionality
    private void setListeners() {
        setFieldListeners();
        setButtonListeners();
        setSearchListListener();
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds listeners to the text fields. calls setFocusListeners
    private void setFieldListeners() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
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
                searchedFoods = foodManager.getFood(searchField.getText());
                foodNames = new ArrayList<>();
                foodNames = addToFoodNames(searchedFoods, foodNames);
                searchedFoodsList.setListData(foodNames.toArray(new String[0]));
            }
        });
        setFocusListeners();
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds listeners for all text fields
    private void setFocusListeners() {
        setFocusListener(nameField);
        setFocusListener(calorieField);
        setFocusListener(fatField);
        setFocusListener(carbField);
        setFocusListener(proteinField);
        setFocusListener(searchField);
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds a focus listener for a specific textField
    private void setFocusListener(JTextField thisField) {
        thisField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                thisField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                // nothing
            }
        });
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds a search list listener
    private void setSearchListListener() {
        searchedFoodsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = searchedFoodsList.getSelectedIndex();
                if (index >= 0 && index <= searchedFoodsList.getMaxSelectionIndex()) {
                    selectedFood = searchedFoods.get(index);
                    calorieField.setText(selectedFood.getCalories() + "cal");
                    fatField.setText(selectedFood.getFat() + "g fat");
                    carbField.setText(selectedFood.getCarbs() + "g carbs");
                    proteinField.setText(selectedFood.getProtein() + "g protein");
                    nameField.setText(selectedFood.getName());
                } else {
                    calorieField.setText("Calories");
                    fatField.setText("Fat");
                    carbField.setText("Carbs");
                    proteinField.setText("Protein");
                    nameField.setText("Name");
                    selectedFood = null;
                }
            }
        });
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds a listener to the add food button that creates a new food and leaves the create food dialogue
    private void setButtonListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFood == null || searchedFoodsList.getSelectedIndex() == -1) {
                    selectedFood = new Food(nameField.getText());
                    try {
                        selectedFood.setFat(Integer.parseInt(fatField.getText()));
                        selectedFood.setCarbs(Integer.parseInt(carbField.getText()));
                        selectedFood.setProtein(Integer.parseInt(proteinField.getText()));
                        selectedFood.setCalories(Integer.parseInt(calorieField.getText()));

                        if (selectedFood.getCalories() == 0) {
                            selectedFood.setCalories(selectedFood.getCaloriesFromMacros());
                        }

                        addFoodToFoodManager();
                    } catch (NumberFormatException nfe) {
                        System.out.println("Number not formatted correctly");
                    }
                } else {
                    addFoodToFoodManager();
                }
            }
        });
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds the newly created food to FoodManager if it does not already exist
    private void addFoodToFoodManager() {
        try {
            foodManager.addFood(selectedFood);
            AddFoodFrame foodFrame = (AddFoodFrame) parentFrame;
            DailyMeal thisMeal = null;
            try {
                thisMeal = foodManager.getMeal(foodFrame.getCurrentDate());
            } catch (DateNotContainedException dnce) {
                thisMeal = new DailyMeal();
                thisMeal.setDate(foodFrame.getCurrentDate());
                foodManager.addMeal(thisMeal);
            } finally {
                thisMeal.addToMeal((short) mealTypeBox.getSelectedIndex(), selectedFood);
            }
            foodFrame.getParentView().fillAllFoods();
            foodFrame.dispose();
        } catch (NotInitializedException nie) {
            nameField.setText("Please set a name");
        }
    }

}
