package polynomialsolver;

public class Term {
    double coefficient;
    double exponent;
    
    //Takes a string, outputs a Term variable
    public static Term stringToTerm(String input){
        double coefficient;
        double exponent;
        
        boolean x = false;
        boolean exp = false;
        
        //Figure out what type of term this is (constant, single x, x^n)
        for(int i = 0; i<input.length(); i++){
            if(input.charAt(i) == 'x'){
                x = true;
            }else if(input.charAt(i) == '^'){
                exp = true;
                break;
            }
        }
        
        if(!x && !exp){ //Constant
            coefficient = Double.parseDouble(input);
            exponent = 0;
        }else if(x && !exp){ //Single x
            try{
                coefficient = Double.parseDouble(input.substring(0, input.length()-1));
            }catch(Exception e){
                coefficient = 1;    
            }
            exponent = 1;
        }else{ //x^n
            String[] splitTerm = input.split("\\^");
            try{
                coefficient = Double.parseDouble(splitTerm[0].substring(0, splitTerm[0].length()-1));
            }catch(Exception e){
                coefficient = 1;
            }
            exponent = Double.parseDouble(splitTerm[1]);
        }
        return new Term(coefficient, exponent);
    }
    
    public Term(double coefficient, double exponent){
        this.coefficient = coefficient;
        this.exponent = exponent;
    }
}
