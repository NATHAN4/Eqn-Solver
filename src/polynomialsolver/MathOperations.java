package polynomialsolver;

import java.util.ArrayList;


public class MathOperations {
    //Finds derivative from a list of terms
    static Term[] findDerivative(Term[] equation){
        ArrayList<Term> derivative = new ArrayList<>();
        ArrayList<Integer> toRemove = new ArrayList<>();
        for(int i = 0; i<equation.length; i++){
            derivative.add(new Term(equation[i].coefficient, equation[i].exponent));
        }
        for(int i = 0; i<equation.length; i++){
            if(derivative.get(i).exponent == 0){
                toRemove.add(i);
            }else{
                derivative.get(i).coefficient = equation[i].coefficient * equation[i].exponent;
                derivative.get(i).exponent = equation[i].exponent - 1;
            }
        }
        for(int i = toRemove.size() - 1; i>=0; i--){
            derivative.remove((int)toRemove.get(i));
            System.out.println("removed one");
        }
        return derivative.toArray(new Term[derivative.size()]);
    }
    
    //Evaluates a function f(x) at a specified x
    static double evaluateFunction(Term[] equation, double xValue){
        double result = 0;
        for(int i = 0; i<equation.length; i++){
            double tempValue = 0;
            tempValue = Math.pow(xValue, equation[i].exponent);
            tempValue *= equation[i].coefficient;
            result += tempValue;
        }
        return result;
    }
    
    //Newton's the shit out a value, function, and its derivative
    static double newtonMethod(double x, Term[] equation, Term[] derivative){
        //result = x - (f(x)/f`(x))
        return (x - (evaluateFunction(equation,x) / evaluateFunction(derivative, x)));
    }
    
    static boolean isZero(double x, Term[] function){
        return(Math.abs(evaluateFunction(function, x))< 0.01);
    }
}
