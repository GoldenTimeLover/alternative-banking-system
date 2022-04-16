package ui.console;

import core.utils.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ui.validator.Validators.*;

public class InputGetters {

    public  static  String getFilePath(){
        System.out.println("Please Enter a full xml file path to load data from: ");
        Scanner in = new Scanner(System.in);
        String userPath = in.nextLine();
        if(checkIfFileExists(userPath) && checkIfPathEndsWithXML(userPath)){
            return userPath;
        }
        return "";
    }
    public  static List<String> getLoanCategories(List<String> categories){
        Scanner in = new Scanner(System.in);
        boolean valid = false;
        System.out.println("Enter the indexes of a categories you are interested" +
                " in.(If you don't want to filter" +
                "it out enter 0\n" +
                "Input Example: 1 3 4");

        String[] splitted = {};
        while(!valid) {
            valid = true;
            System.out.println("0. Don't want to chose a category.");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i));
            }
            String userChoice = in.nextLine();

            splitted = userChoice.split("\\s+");
            for (int i = 0; i < splitted.length; i++) {
                if (!validateNumericValue(splitted[i])){
                    valid = false;
                    System.out.println("Please enter number from the list seperated by a space!");
                }
            }
            if(!(checkOutOfArrayBounds(splitted,categories.size() + 1) && checkNoDuplicates(splitted))){
                valid = false;
                System.out.println("Please enter number from the list seperated by a space!");
            }
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < splitted.length; i++) {
            if (Integer.parseInt(splitted[i]) == 0){
                return categories;
            }
            result.add(categories.get(Integer.parseInt(splitted[i]) - 1));
        }
        return result;
    }
    /**
     * function ask the user for a non-negative integer and doesn't stop until it gets it
     * @return a non-negative integer representing the minimum Interest rate willing to accept
     */
    public static double getMinInterest(){
        Scanner in = new Scanner(System.in);
        double amount = -1;

        while( amount < 0) {
            System.out.println("Enter minimum interest you are willing to accept:");
            System.out.println("Positive values only, if don't care enter 0");
            String userChoice = in.nextLine();
            if (validateDoubleValue(userChoice)) {
                amount = Double.parseDouble(userChoice);
                if (amount < 0) {
                    amount = -1;
                    System.out.println("Positive values only, if don't care enter 0");
                }
                amount = utils.round(amount);
            } else {
                System.out.println("Positive int values only, if don't care enter 0");
            }
        }
        return amount;
    }
    /**
     * function ask the user for a non-negative integer and doesn't stop until it gets it
     * @return a non-negative integer representing the minimum amount of time willing to accept
     */
    public static int getMinYaz(){
        Scanner in = new Scanner(System.in);
        int amount = -1;
        while(amount < 0) {
            System.out.println("Enter minimum Time Units you are willing to accept:");
            System.out.println("Positive values only, if don't care enter 0");
            String userChoice = in.nextLine();
            if (validateNumericValue(userChoice)) {
                amount = Integer.parseInt(userChoice);
                if (amount < 0) {
                    System.out.println("Positive values only, if don't care enter 0");
                }
            } else {
                System.out.println("Positive int values only, if don't care enter 0");
            }
        }
        return amount;
    }

    /**
     * Request from the user a number for how much they want to loan out
     * @param clientBalance current balance of the account lending
     * @return the amount willing to spend, can't be higher than the balance
     */
    public static double getLoanAmount(double clientBalance){
        Scanner in = new Scanner(System.in);
        double amountToTake = 0;
        while (amountToTake <= 0) {
            System.out.println("Please Enter the amount of money for loan: ");
            String userChoice = in.nextLine();

            if (validateNumericValue(userChoice)) {
                amountToTake = Integer.parseInt(userChoice);
                if (amountToTake > clientBalance) {
                    System.out.println("Please Enter a valid integer amount that is not higher than: " + clientBalance);
                    amountToTake = 0;
                }
            } else {
                System.out.println("Please Enter a valid integer amount that is not higher than: " + clientBalance);
            }
        }
        return  amountToTake;
    }

}
