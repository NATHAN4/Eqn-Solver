package polynomialsolver;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class PolynomialSolver extends JFrame {

    static PolynomialSolver MainWindow;
    public Term[] equation;
    public Term[] derivative;
    public long T1 = 0;
    public long T2 = 0;

    public void buildGUI() {
        JFrame frame = new JFrame();
        JPanel contentPanel = (JPanel) frame.getContentPane();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        final JLabel titleLabel = new JLabel("Polynomial Solver");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        titlePanel.add(titleLabel);

        //Text Panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        final JLabel text = new JLabel("Please enter an equation:");
        text.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        textPanel.add(text);

        //Declare button forward
        final JButton button = new JButton("Solve");
        final JLabel rootsLabel = new JLabel("Roots: ");
        
        //Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        final JTextField textBox = new JTextField("") {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                max.width = getPreferredSize().width;
                return max;
            }
        ;
        };
        textBox.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    button.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }

        });
        textBox.setPreferredSize(new Dimension(180, 25));
        textBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        JLabel equalsLabel = new JLabel(" = 0");
        equalsLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        inputPanel.add(textBox);
        inputPanel.add(equalsLabel);

        //Error panel
        JPanel errorPanel = new JPanel();
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.X_AXIS));
        final JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        errorPanel.add(errorLabel);

        //Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                T1 = System.currentTimeMillis();
                InputValidation validator = new InputValidation(textBox.getText());
                if (validator.validateInput()) {
                    errorLabel.setText(" ");

                    //Build list of Terms
                    String[] terms = validator.returnTerms();
                    equation = new Term[terms.length];
                    for (int i = 0; i < terms.length; i++) {
                        equation[i] = Term.stringToTerm(terms[i]);
                        
                    }
                    
                    //Calculate Derivative
                    derivative = MathOperations.findDerivative(equation);
                    
                    //Now we need to guess a domain for the roots. 
                    //I'm multiplying all the coefficients together to estimate a domain.
                    double max = 1;
                    double min = 1;
                    
                    for(int i = 0; i<equation.length; i++){
                        max *= equation[i].coefficient;
                    }
                    max = Math.abs(max);
                    min= -1 * max;
                    
                    //Create 1000 points to check with newtons method evenly
                    //Evenly spaced throughout estimated domain
                    double[] xValsToCheck = new double[1000];
                    double[] convergancePoints = new double[1000];
                    double spacing = max / 500;
                    double temp = 0;
                    //Positive x vals
                    for(int i = 0; i<500; i++){
                        temp += spacing;
                        xValsToCheck[i] = temp;
                    }
                    temp = 0;
                    //Negative x vals
                    for(int i = 0; i<500; i++){
                        temp -= spacing;
                        xValsToCheck[500+i] = temp;
                    }
                    
                    //Now for each of the 1000 points:
                    for(int i = 0; i<xValsToCheck.length; i++){
                        //Do newtons method 100 times and save the value we converge on
                        double convergance = xValsToCheck[i];
                        
                        for(int ii = 0; ii<30; ii++){
                            convergance = MathOperations.newtonMethod(convergance, equation, derivative);
                        }
                        convergancePoints[i] = convergance;
                    }
                    
                    //Next we'll round each value to 3 DP, and add it to the unique root list
                    HashSet rootSet = new HashSet();
                    ArrayList<Double> roots = new ArrayList();
                    DecimalFormat df = new DecimalFormat("#.###");
                    for(int i = 0; i<convergancePoints.length; i++){
                        convergancePoints[i] = Double.parseDouble(df.format(convergancePoints[i]));
                        rootSet.add(convergancePoints[i]);
                        //System.out.println(convergancePoints[i] + ", " + xValsToCheck[i]);
                    }
                    roots.addAll(rootSet);
                    T2 = System.currentTimeMillis();
                    //now print the results
                    rootsLabel.setText("Roots: ");
                    for(int i = 0; i<roots.size(); i++){
                        boolean isZero = MathOperations.isZero(roots.get(i), equation);
                        if(isZero)
                            rootsLabel.setText(rootsLabel.getText() + roots.get(i) + ", ");
                    }
                    rootsLabel.setText(rootsLabel.getText().substring(0, rootsLabel.getText().length()-2));
                    System.out.println(T2-T1);            
                } else {
                    errorLabel.setText("INVALID EQUATION");
                }
            }
        });
        button.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        buttonPanel.add(button);

        JPanel rootsPanel = new JPanel();
        rootsPanel.setLayout(new BoxLayout(rootsPanel, BoxLayout.X_AXIS));
        rootsLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        rootsPanel.add(rootsLabel);
        rootsPanel.add(Box.createHorizontalGlue());
        
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(30, 30)));
        contentPanel.add(textPanel);
        contentPanel.add(inputPanel);
        contentPanel.add(errorPanel);
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        contentPanel.add(rootsPanel);

        frame.setSize(300, 250);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow = new PolynomialSolver();
                MainWindow.buildGUI();
            }
        });
    }
}
