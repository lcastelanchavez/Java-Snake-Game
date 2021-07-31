package hw4;
import javax.swing.*;
public class SnakeGame {
    public static void main(String args[]){ //run the Snake Game
        JFrame frame = new JFrame("Snake Game");    //create a new Frame
        JPanel panel = new SnakeGamePanel();    //create new SnakeGamePanel game 
        frame.add(panel);   
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);  //make sure cannot resize
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //finish program after closing window
    }
}
