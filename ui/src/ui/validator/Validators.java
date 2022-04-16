package ui.validator;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Validators {


    public static boolean checkIfFileExists(String path){
        try{
            File f = new File(path);
            if(f.exists() && !f.isDirectory()) {
                return true;
            }
            System.out.println("'" + path + "'" +" doesn't exist in your computer.. Please enter valid path.");
            return false;
        } catch (Exception e) {
            System.out.println("Something wrong with file path " + path);
            return false;
        }
    }

    public static boolean checkIfPathEndsWithXML(String path){
        String extension = "";

        int i = path.lastIndexOf('.');

        if (i > 0)
            extension = path.substring(i+1);


        if (extension.equals("xml"))
            return true;

        System.out.println("Please Enter a file path of a file of file that ends with .xml");
        return false;
    }
    public static boolean validateMainMenuInput(String input){
        Set<String> valids = new HashSet<>(Arrays.asList("1", "2", "3","4","5","6","7","8"));

        return valids.contains(input);
    }

    public static boolean validateDoubleValue(String str){
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public static boolean validateNumericValue(String str){
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean checkNoDuplicates(String[] splitted){
        for(int i = 0; i < splitted.length; i++) {
            for(int j = i + 1; j < splitted.length; j++) {
                if (splitted[i].equals(splitted[j])){
                    System.out.println("Found duplicates in list operation Canceled!");
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkOutOfArrayBounds(String[] splitted,int len){
        for (int i = 0; i < splitted.length; i++) {
            int index = Integer.parseInt(splitted[i]);
            if (index < 0 || index >=len){
                System.out.println("Number in list out of bounds operation Canceled!");
                return false;
            }
        }
        return true;
    }
}
