# Calorific

## A calorie tracking app

**What is Calorific?**

Calorific is a calorie tracking desktop app intended to help users develop healthy eating habits.
Users will be able to track calorie intake each day, and they will be able to document their foods and meals.


*Functions will include*
* Tracking calories and macronutrients
* Creating and tracking new foods
* Searching for and reusing previously created foods
* Visualizing weekly caloric intake
* Setting calorie goals

**Who is Calorific for?**

Calorific is built for anybody who wants to take control of their personal fitness.
Whether you want to have healthier eating habits, lose weight, or build muscle,
Calorific can help you with that. 

**Why did I choose to build Calorific?**

My lifestyle has been incredibly unhealthy for many years. I have tried many
times before to track my fitness and work towards becoming more fit, but I have never
been able to stay committed to one calorie tracking app. Most calorie tracking apps 
are very convoluted in adding foods and visualizing intake, and so Calorific was the solution.

## User Stories

As a user, I want to be able to...
* create a new food with basic nutrition information
* add foods to a list of foods consumed in a day, separated by meal
* see the total calories and macronutrients in a day
* search previously added foods to easily add them again
* see foods I ate during certain meals in the day
* save all tracked meals to a file
* load all meals from a file when the app is launched
* set calorie intake goals

**Phase 4: Task 2**
* Made FoodManager class robust
    * The exceptions are under the exception package as DateNotContainedException, 
    NegativeCalorieGoalException, and NotInitializedException
    * FoodManager is modified to throw exceptions
        * addFood throws NotInitializedException'
        * getMeal throws DateNotContainedException
        * setCalorieGoal throws NegativeCalorieGoalException
    * Classes modified to capture new exceptions
        * OverviewView has exceptions thrown in loadInfo() and setCalorieGoalListeners()
        * AddFoodView has exceptions thrown in addFoodToFoodManager()
        * JsonReader has exceptions thrown in parseFoodManager() and addFoods()
    * TestFoodManager, TestJsonReader, and TestJsonWriter classes have been modified to reflect new
    exceptions
* Type Hierarchy between View, AddFoodView, and OverviewView
    * View class contains constructor, init(), createCards(), and addToFoodNames()
    * OverviewView and AddFoodView override init() and createCards() to create their respective vies
    * addToFoodNames is inherited by both OverviewView and AddFoodView
* 1-1 Bi-directional relationship between CalorificUI and OverviewView
    * CalorificUI contains a reference to OverviewView to add it to the JFrame panel
    * OverviewView has a reference to the CalorificUI to add components to the panel and
    modify the CalorieGraph object in the CalorificUI
    
**Phase 4: Task 3**

Overall, my class design is very subpar. There is a lot of coupling with the 
FoodManager class, and there are a lot of unnecessary associations that could have
been cleaned up had I put better forethought into the design. 
A lot of my design decisions early on were to account for the second half
of my application that I was unable to build: I intended to build an exercise tracker as well,
which would have also used the DailyClass and would have had it's own Manager class. These were
supposed to interact within their own parent class that would be the global app manager,
but I was unable to implement all these features, due to being short on time. It may also
have been two ambitious to essentially integrate two apps into one project.

If I could refactor my code, I would move the management with viewing the whole week,
creating new meals for missing days, and other features into the FoodManager class.
I would also deal with getting and displaying dates in the Daily parent class, or perhaps in the 
DailyMeal class (ie. getDateByIncrement could be a static funtion within Daily, as it is used
a few times throughout the code). To reduce code coupling, I could handle most input within the CalorificUI,
rather than within the respective views, which would mean I wouldn't need upwards of seven objects
all storing references to the same FoodManager. I would simply need a reference that calls back to the CalorificUI,
which could house each component separately, and I could call methods within CalorificUI through
calling methods on an object's reference. This would greatly reduce the coupling that exists within my code.
Optionally, I would also itemize more of the functions within the UI as to follow the single
responsibility principle: I could have a different component for the list of foods, for the nutrition information,
and for handling the date. These could further modularize the code. Finally, I would build better
exceptions and have better robustness for my code, as currently there are a lot of bandage patches
for issues in my code.