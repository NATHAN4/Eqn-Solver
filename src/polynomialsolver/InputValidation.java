package polynomialsolver;

import java.util.ArrayList;

public class InputValidation {

    String rawInput;
    char[] validChars = {
        'x', //Variable
        '/', '*', '-', '+', //Operators
        '(', ')', '[', ']', //Brackets
        '^', '=', '.', //Misc
        '1', '2', '3', '4', //Numbers
        '5', '6', '7', '8',
        '9', '0'};

    public InputValidation(String input) {
        rawInput = input.replaceAll("\\s+",""); //Remove whitespace
    }

    //Public validate method, checks to make sure the equation is valid
    //If everything is valid, it returns the terms in Term objects
    public boolean validateInput() {
        if (rawInput.isEmpty() || !checkValidChars()) { //First level checks
            return false;
        }
        
        //Split into terms
        String[] terms = returnTerms();
        if(terms[0].charAt(0) != '-' || terms[0].charAt(0) != '+'){
            terms[0] = "+" + terms[0];
        }
        
        for(int i = 0; i<terms.length; i++){
            boolean var = false;
            boolean expSign = false;
            for(int ii=1; ii<terms[i].length(); ii++){
                if(terms[i].charAt(ii) == 'x'){
                    if(var){
                        return false;
                    }else{
                        var = true;
                    }
                }else if(terms[i].charAt(ii) == '^'){
                    if(expSign || ii == terms[i].length() - 1){
                        return false;
                    }else{
                        expSign = true;
                    }
                }
            }
        }
        return true;
    }

    //Checks all characters in equation against valid char list
    //Returns true if the equation has no invalid chars, 
    //returns false otherwise
    private boolean checkValidChars() {
        for (int i = 0; i < rawInput.length(); i++) {
            boolean allGood = false;
            for (int ii = 0; ii < validChars.length; ii++) {
                if (rawInput.charAt(i) == validChars[ii]) {
                    allGood = true;
                }
            }

            if (!allGood) {
                return false;
            }
        }
        return true;
    }
    
    //Method splits equation up into terms
    public String[] returnTerms(){
        int latestBreak = 0;
        ArrayList<String> dynamicList = new ArrayList<>();
        for(int i = 0; i<rawInput.length(); i++){
            if((rawInput.charAt(i) == '-' || rawInput.charAt(i) == '+') && i != 0){
                dynamicList.add(rawInput.substring(latestBreak, i));
                latestBreak = i;
            }
            
            if(i == rawInput.length() - 1){
                dynamicList.add(rawInput.substring(latestBreak, i + 1));
            }
        }
        
        return dynamicList.toArray(new String[dynamicList.size()]);
    }
}
