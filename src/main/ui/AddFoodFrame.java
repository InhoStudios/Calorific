package ui;

import model.calorietracker.FoodManager;
import ui.views.AddFoodView;
import ui.views.OverviewView;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
/**
 * AddFoodFrame represents the JFrame pop up for adding a new food to the FoodManager
 *
 * AddFoodFrame requires a food manager, a date, and a reference to the parent view to construct.
 * It extends the JFrame class.
 *
 */

public class AddFoodFrame extends JFrame {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;

    private FoodManager fm;

    private AddFoodView addFoodView;
    private OverviewView parentView;

    private Date currentDate;

    // REQUIRES: fm, currentDate, and parentView are not null
    // MODIFIES: this
    // EFFECTS: constructs a new AddFoodFrame
    public AddFoodFrame(FoodManager fm, Date currentDate, OverviewView parentView) {
        super("Add new food");
        this.fm = fm;
        this.currentDate = currentDate;
        this.parentView = parentView;
        init();
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: initializes the components of the JFrame
    private void init() {
        initFrame();
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: sets properties for the JFrame element of this class. Creates components.
    private void initFrame() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        createComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: creates JFrame components and the adds the AddFoodView to the frame
    private void createComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        addFoodView = new AddFoodView(panel, fm, this);

        add(panel);
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public OverviewView getParentView() {
        return parentView;
    }
}
