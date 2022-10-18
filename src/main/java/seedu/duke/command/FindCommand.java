package seedu.duke.command;

import seedu.duke.Parser;
import seedu.duke.Ui;
import seedu.duke.biometrics.Biometrics;
import seedu.duke.exception.IllegalValueException;
import seedu.duke.exercise.Exercise;
import seedu.duke.exercise.ExerciseList;
import seedu.duke.exercise.StrengthExercise;
import seedu.duke.food.FoodList;
import seedu.duke.storage.Storage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FindCommand extends Command {
    private String arguments;
    private ExerciseList exerciseList;

    private FoodList foodList;
    private Ui ui;

    public FindCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute() throws IllegalValueException {
        String[] argumentList = Parser.getArgumentList(arguments);
        String findType = Parser.getClassType(argumentList);
        switch (findType) {
        case "strength":
            findStrength(argumentList);
            break;
        case "food":
            findFood(argumentList);
            break;
        default:
            handleInvalidFindType();
        }
    }

    private void findStrength(String[] argumentList) throws IllegalValueException {
        handleInvalidFindStrengthCommand(argumentList);
        ArrayList<Exercise> filteredExerciseList = getFilteredExerciseList(argumentList);
        ui.output("", "Here are the matching strength exercises in your list:");
        ui.outputExerciseList(filteredExerciseList);
    }

    private void findFood(String[] argumentList) throws IllegalValueException {
        handleInvalidFindFoodCommand(argumentList);
        ui.output("Here are the matching food in your food list:");
        for (int i = 0; i < foodList.getFoodListSize(); i++) {
            if (foodList.getFood(i).getFoodDescription().contains(argumentList[1])) {
                ui.output((i + 1) + " " + (foodList.getFood(i).getFoodDescription()) + " "
                        + foodList.getFood(i).getCalories());
            }
        }
    }

    private static void handleInvalidFindStrengthCommand(String[] argumentList) throws IllegalValueException {
        if (argumentList.length != 2) {
            throw new IllegalValueException("Invalid find strength command");
        }
    }

    private static void handleInvalidFindFoodCommand(String[] argumentList) throws IllegalValueException {
        if (argumentList.length != 2) {
            throw new IllegalValueException("Invalid find food command");
        }
    }


    private ArrayList<Exercise> getFilteredExerciseList(String[] argumentList) {
        ArrayList<Exercise> filteredExerciseList = (ArrayList<Exercise>) exerciseList.getCompletedExerciseList()
                .stream().filter(StrengthExercise.class::isInstance)
                .filter(e -> e.getExerciseName().contains(argumentList[1]))
                .collect(Collectors.toList());
        filteredExerciseList.addAll(exerciseList.getCurrentExerciseList()
                .stream().filter(StrengthExercise.class::isInstance)
                .filter(e -> e.getExerciseName().contains(argumentList[1]))
                .collect(Collectors.toList()));
        return filteredExerciseList;
    }

    private void handleInvalidFindType() throws IllegalValueException {
        throw new IllegalValueException("Invalid find command");
    }

    @Override
    public void setData(Ui ui, Storage storage, Biometrics biometrics, ExerciseList exerciseList, FoodList foodList) {
        this.ui = ui;
        this.exerciseList = exerciseList;
        this.foodList = foodList;
    }
}
