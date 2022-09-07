package ui;

import com.sun.org.apache.xpath.internal.operations.Neg;
import exceptions.NegativeCalorieGoalException;
import model.calorietracker.FoodManager;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.views.CalorieGraph;
import ui.views.OverviewView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
/**
 * CalorificUI represents a GUI application housing the Calorific app.
 *
 * CalorificUI requires no parameters upon construction. It extends the JFrame object.
 *
 * CalorificUI's structure was partially adapted from the SimpleDrawingPlayer-Complete project
 * https://github.students.cs.ubc.ca/CPSC210/SimpleDrawingPlayer-Complete
 *
 */

public class CalorificUI extends JFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    private FoodManager fm;
    private static String path = "./data/Calorific.json";

    private JsonReader reader;
    private JsonWriter writer;

    private OverviewView overview;
    private CalorieGraph calorieGraph;

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: Constructs a new Calorific GUI
    public CalorificUI() {
        super("Calorific");
        init();
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: initializes the FoodManager and the JFrame elements
    private void init() {
        initSerialization();
        initFrame();
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: loads the FoodManager from a file if the file exists, if not, constructs a new FoodManager object
    private void initSerialization() {
        reader = new JsonReader(path);
        writer = new JsonWriter(path);

        try {
            fm = loadFile();
            System.out.println(fm.toJson().toString());
        } catch (ParseException | IOException e) {
            System.out.println("FoodManager could not be found, continuing");
            fm = new FoodManager();
        } catch (NegativeCalorieGoalException ncge) {
            System.out.println("Calorie goal is negative");
        }
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: sets layout and initalizes properties of this frame, creates individual frame components
    private void initFrame() {
        setLayout(new GridLayout(2, 1));
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        createComponents();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setCloseListener();
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: adds a listener to the JFrame that allows the application to save before closing
    private void setCloseListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    saveFile();
                    System.out.println("saved");
                } catch (FileNotFoundException fileNotFoundException) {
                    System.out.println("Error with file, file not found");
                }
                System.exit(0);
            }
        });
    }

    // REQUIRES: nothing
    // MODIFIES: this
    // EFFECTS: create JPanel housing all the JComponents of the Calorific UI.
    private void createComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setSize(new Dimension(0, 0));

        calorieGraph = new CalorieGraph(fm, WIDTH, HEIGHT / 2);
        add(calorieGraph);

        overview = new OverviewView(panel, fm, this);
        overview.loadInfo(new Date());

        calorieGraph.renderDates(overview.getCurrentDate());

        add(panel);
        pack();
    }

    public CalorieGraph getCalorieGraph() {
        return calorieGraph;
    }

    // REQUIRES: nothing
    // MODIFIES: nothing
    // EFFECTS: Loads a FoodManager from a file and returns it
    // Throws IOException if no file is found to read from
    // Throws ParseException if the file is not in a proper JSON format
    private FoodManager loadFile() throws IOException, ParseException, NegativeCalorieGoalException {
        return reader.read();
    }

    // REQUIRES: nothing
    // MODIFIES: nothing
    // EFFECTS: Saves the FoodManager to a file. Throws FileNotFoundException if the file doesn't exist at path
    private void saveFile() throws FileNotFoundException {
        writer.open();
        writer.write(fm);
        writer.close();
    }

}
