# HangmanComplete

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HangmanComplete extends JPanel {

    private String message;
    private WordList wordlist;
    private String word;
    private String guesses;
    private int wrongGuess;
    private boolean gameOver;
    private Display display;
    private ArrayList<JButton> letterButtons = new ArrayList();
    private JButton nextButton;
    private JButton giveUpButton;

    public HangmanComplete() {
        JPanel top = new JPanel();
        ButtonHandler buttonhadler = new ButtonHandler();
        top.setLayout(new GridLayout(2, 13, 3, 3)); //This is our top layout which will consist of alphabets
        for (char ch = 'A'; ch <= 'Z'; ch = (char) (ch + '\u0001')) {
            JButton b = new JButton("" + ch);  //Here we will be adding the alphabet buttons
            b.setPreferredSize(new Dimension(30, 35));// Setting the size of alphabet buttons
            top.add(b);
            b.addActionListener(new ButtonHandler());
            this.letterButtons.add(b);
        }
        display = new Display(); //Here we are just setting up the font and colour
        JPanel bottom = new JPanel(); //This panel will consist of quit button, next button and give up button
        nextButton = new JButton("Next word"); //Initializing the next button
        nextButton.addActionListener(buttonhadler);
        bottom.add(this.nextButton);
        giveUpButton = new JButton("Give up");
        giveUpButton.addActionListener(buttonhadler);
        bottom.add(this.giveUpButton);
        JButton quit = new JButton("Quit");
        quit.addActionListener(buttonhadler);
        bottom.add(quit);
        setLayout(new BorderLayout(3, 3));
        add((Component) top, "North");
        add((Component) this.display, "Center");
        add((Component) bottom, "South");
        setBackground(new Color(100, 0, 0));
        wordlist = new WordList("wordlist.txt");
        startGame();
    }
    
    private boolean wordDone() {
        for (int i = 0; i < this.word.length(); ++i) {
            char ch = this.word.charAt(i);
            if (this.guesses.indexOf(ch) != -1) {
                continue;
            }
            return false;
        }
        return true;
    }    

    private void startGame() {
        gameOver = false;
        guesses = "";
        wrongGuess = 0;
        nextButton.setEnabled(false);
        for (int i = 0; i < letterButtons.size(); ++i) {
            letterButtons.get(i).setEnabled(true);
        }
        giveUpButton.setEnabled(true);
        int index = (int) (Math.random() * (double) this.wordlist.getWordCount());
        word = wordlist.removeWord(index);
        word = word.toUpperCase();
        System.out.println(word);
        message = "The word has " + word.length() + " letters.  Let's play Hangman!";

    }



    private void drawHangman(Graphics g, int level) {
        g.setColor(new Color(90, 30, 0));
        g.fillRect(300, 320, 200, 10);
        g.fillRect(340, 100, 10, 225);
        g.fillRect(340, 120, 100, 7);
        g.drawLine(430, 125, 430, 160);
        g.setColor(new Color(0, 70, 0));
        if (level > 0) {
            g.fillOval(415, 140, 30, 40);
        }
        if (level > 1) {
            g.drawLine(430, 180, 430, 205);
            g.drawLine(430, 205, 430, 245);
        }
        if (level > 2) {
            g.drawLine(395, 225, 430, 200);

        }
        if (level > 3) {
            g.drawLine(430, 200, 465, 225);
        }

        if (level > 4) {
            g.drawLine(390, 290, 430, 245);
        }
        if (level > 5) {
            g.drawLine(430, 245, 470, 290);
        }
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Hangman Game by Sonam Avichal");
        HangmanComplete panel = new HangmanComplete();
        window.setContentPane(panel);
        window.pack();
        window.setResizable(false);
        window.setDefaultCloseOperation(3);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((screen.width - window.getWidth()) / 2, (screen.height - window.getHeight()) / 2);
        window.setVisible(true);
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JButton whichButton = (JButton) evt.getSource(); //Now here we will check as to which button we are handling
            String cmd = evt.getActionCommand(); //Getting the string of that button
            if (cmd.equals("Quit")) {
                System.exit(0);      //If it is quit then we will exit it
            } else if (cmd.equals("Give up")) {  //If its the give up button
                message = "You lose because you gave up!  The word is:  " + word;
                //Now we will reset the game
                giveUpButton.setEnabled(false); //Disabling the give up button
                nextButton.setEnabled(true); //Enabling the next game button
                for (JButton b : letterButtons) {
                    b.setEnabled(false);  //Disabling all the alphabets button
                }
                gameOver = true;
            } else if (cmd.equals("Next word")) {
                startGame();   //Starting a new game
            } else {   //We have pressed an alphabet key, making listener for it
                char guess = cmd.charAt(0);  //Getting the character which was clicked
                guesses = guesses + guess;
                whichButton.setEnabled(false); //We will disable the button that was clicked
                if (wordDone()) { //If the word is complete
                    message = "Congratulations!  You got it!";
                    giveUpButton.setEnabled(false); // Disabling give up button
                    nextButton.setEnabled(true);  //Enabling the next button
                    for (JButton b : letterButtons) {  //Disabling all alphabet buttons
                        b.setEnabled(false);
                    }
                    gameOver = true;
                } else if (word.indexOf(guess) >= 0) { //If the guess exists in the word
                    message = "Yes, " + guess + " is in the word.  Pick your next letter.";
                } else { //Guess is not in the word
                    wrongGuess++;
                    if (wrongGuess == 6) { //If wrongGuess are over
                        message =  "Sorry, you're hung!  The word is:  " + word;
                        giveUpButton.setEnabled(false);
                       nextButton.setEnabled(true);
                        for (JButton b : letterButtons) { //Disabling all alphabet buttons
                            b.setEnabled(false);
                        }
                        gameOver=true;
                    } else {  //If wrongGuess are not over
                        message =  "Sorry, " + guess + " is not in the word.  Pick your next letter.";
                    }
                }
            }
            display.repaint();
        }
    }

    private class Display
            extends JPanel {

        Display() {
            this.setPreferredSize(new Dimension(620, 420));
            this.setBackground(new Color(128, 237, 183));
            this.setFont(new Font("Serif", 1, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ((Graphics2D) g).setStroke(new BasicStroke(3.0f));
            if (message != null) {
                g.setColor(Color.RED);
                g.drawString(message, 30, 40);
            }
            if (gameOver) {
                g.setColor(Color.BLUE);
                g.drawString("Click \"Next word\" to play again.", 30, 70);
            } else {
                g.drawString("Bad Guesses Remaining:  " + (6 - wrongGuess), 30, 70);
            }
            drawHangman(g, wrongGuess);
            g.setColor(Color.DARK_GRAY);
            for (int i = 0; i < word.length(); ++i) {
               
                if (guesses.indexOf(word.charAt(i)) < 0) {
                    g.drawString("*", 25 + i * 50, 395);   //print the * if guess is not in the word
                
                }
                else
                g.drawString("" + word.charAt(i), 25 + i * 50, 395);  //else print the alphabet itself
            }
        }
    }

}
