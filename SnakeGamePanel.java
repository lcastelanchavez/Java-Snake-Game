package hw4;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; 
import java.util.Random; 

public class SnakeGamePanel extends JPanel {
    private final int WIDTH=320,HEIGHT=320; //set dimensions of panel
    //set variables JUMP to move snake, width and height of apple of pixels and what to reduce delay by
    private final int JUMP = 20, RECT_WIDTH = 20, RECT_HEIGHT = 20, REDUCE = 20; 
    //setting delay to 250 ms
    private int delay = 250; 
    
    private char currDir; //determines direction of snake
    private char lastKeyCodePressed; //stores most recent key pressed
    Sequence apple;                 //stores coordinates of apple 
    int applesEaten = 0;            //stores number of apples eaten 
    ArrayList<Sequence> snake;      //stores coordinates of snake 
    private Timer timer;            //timer 
    boolean gameOver = false;       //stores state of game


public SnakeGamePanel() {
    snake = new ArrayList<>(); 
    //use random to get random coordinates within range
    Random r = new Random(); 
    int snakeRandomXCoord = r.nextInt(16) * 20;
    int snakeRandomYCoord = r.nextInt(16) * 20;
    int appleRandomXCoord = r.nextInt(16) * 20;
    int appleRandomYCoord = r.nextInt(16) * 20;

    //generate new apple coordinates if snake coordinates equal the apple 
    while((snakeRandomXCoord == appleRandomXCoord) && (snakeRandomYCoord == appleRandomYCoord))
    {
    
        appleRandomXCoord = r.nextInt(16) * 20;
        appleRandomYCoord = r.nextInt(16) * 20;

    }
    //add coordinates to snake and apple 
    snake.add(new Sequence(snakeRandomXCoord,snakeRandomYCoord));
    apple = new Sequence(appleRandomXCoord, appleRandomYCoord); 
    
    addKeyListener(new DirectionListener());    //store key strokes
    timer = new Timer(delay, new MovingUpdate() );  //start moving 
    setPreferredSize( new Dimension(WIDTH, HEIGHT) );   //set size of frame
    setBackground( Color.blue );    
    timer.start();
    setFocusable(true);
}

private class DirectionListener implements KeyListener {
    @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode())  {
                case KeyEvent.VK_UP:                //if pressed up 
                    //if(lastKeyCodePressed != 'D')
                    //{
                        lastKeyCodePressed = 'U';
                    //} 
                    break;
                case KeyEvent.VK_DOWN:              //if pressed down
                    //if(lastKeyCodePressed != 'U')
                    //{
                        lastKeyCodePressed = 'D';
                    //}
                    break;
                case KeyEvent.VK_LEFT:                  //if pressed left
                    //if(lastKeyCodePressed != 'R')
                    //{
                        lastKeyCodePressed = 'L';
                    //}
                    break;                    
                case KeyEvent.VK_RIGHT:             //if pressed right 
                    //if(lastKeyCodePressed != 'L')
                    //{
                    lastKeyCodePressed = 'R';
                    //}
                    break;
                case KeyEvent.VK_SPACE:
                    //stop timer and reset it 
                    timer.stop();
                    JOptionPane.showMessageDialog(null, "Pause, continue?");    //stops code until close dialoge
                    timer.restart();
                    break; 
            }  
 
        }
        @Override
        public void keyTyped(KeyEvent e) { }
        @Override
        public void keyReleased(KeyEvent e) { }
}

protected void paintComponent(Graphics g)
{
    super.paintComponent(g);
    //paint rectangle with apple coordinates 
    //for loop using ArrayList to paint rectangles
    g.setColor(Color.GREEN);
    g.drawRect(apple.x, apple.y, RECT_WIDTH, RECT_HEIGHT); 
    g.fillRect(apple.x, apple.y, RECT_WIDTH, RECT_HEIGHT); 

    for(int i= 0; i < snake.size(); ++i)
    {
        g.setColor(Color.RED);
        g.drawRect(snake.get(i).x, snake.get(i).y, RECT_WIDTH, RECT_HEIGHT);
        g.fillRect(snake.get(i).x, snake.get(i).y, RECT_WIDTH, RECT_HEIGHT);
    }

    
}

private class MovingUpdate implements ActionListener{
    @Override
        public void actionPerformed(ActionEvent e)
        {
            //based on lastKeyCode pressed, update snake direction
            gameOver = isGameOver(); 
            if(!gameOver)
            {
                switch(lastKeyCodePressed)  {   //update currDir to new keystroke 
                    case 'U':
                        if(currDir != 'D')  //make sure previous is not opposite direction
                        {
                            currDir = 'U';
                        } 
                        break;
                    case 'D':
                        if(currDir != 'U')  //make sure previous is not opposite direction
                        {
                            currDir = 'D';
                        }
                        break;
                    case 'L':
                        if(currDir != 'R')  //make sure previous is not opposite direction
                        {
                            currDir = 'L';
                        }
                        break;                    
                    case 'R':
                        if(currDir != 'L')  //make sure previous is not opposite direction
                        {
                            currDir = 'R';
                        }
                        break;
                }  
                //store old tail to add new Sequence object in case encounter new apple
                Sequence tail = new Sequence(snake.get(snake.size()-1).x, snake.get(snake.size()-1).y);
                //go through the rest of the snake body and move it
                for(int i = snake.size()-1; i > 0; --i)
                {
                    (snake.get(i)).x = (snake.get(i-1)).x; 
                    (snake.get(i)).y = (snake.get(i-1)).y; 
                }

                switch(currDir) //change the head coordinates
                {
                case 'U':
                    (snake.get(0)).y -= JUMP; 
                    break;
                case 'D':
                    (snake.get(0)).y += JUMP;
                    break;
                case 'L':
                    (snake.get(0)).x -= JUMP;
                    break;
                case 'R':
                    (snake.get(0)).x += JUMP;
                    break;
                }

                //if the head moves onto an apple, add one to tail of snake
                //and call nextApple method to generate a new apple 
                //and add one to Sequence
                if((snake.get(0).x == apple.x) && (snake.get(0).y == apple.y))
                {
                    snake.add(tail);   //add new Sequence to make snake longer
                    nextApple(); 
                }


                repaint();  //call on repaint method to draw updated snake and apple 
            }
            //if game is over, else call dialogue newgame
            else {

                newGame();
            }
        }

 }

private class Sequence {
    public int x, y; 

    Sequence(int x, int y) //create sequence object constructor
    {
        if(x< 0 || x > 300 || y < 0 || y >300 )
        {
            System.out.println("Entered bad coordinates hoe");
        }
        this.y = y; //set x and y coordinates
        this.x = x; 
    }
}

private boolean isGameOver()
{
    //check if snake head hits border panel to end game 
    if(snake.get(0).x < 0 || snake.get(0).x > 300 || snake.get(0).y > 300 || snake.get(0).y < 0)
    {
        return true; 
    }
    //check if snake hits itself to end game
    for(int i = 1; i < snake.size(); i++)   //check if snake head hits body 
    {
        if((snake.get(0).x == snake.get(i).x) && (snake.get(0).y == snake.get(i).y))    
        {
            return true; 
        }
    }
    //otherwise return false 
    return false; 

}

private void nextApple()
{
    System.out.println("entering nextApple");
    Random r = new Random(); 
    int appleRandomXCoord = r.nextInt(16) * 20;
    int appleRandomYCoord = r.nextInt(16) * 20;
  

    //loop through snake body body coordinates
    //start at front and check again everytime generate new coordinate
    for(int i = 0; i < snake.size(); i++) {

        if((appleRandomXCoord == snake.get(i).x) && (appleRandomYCoord == snake.get(i).y))
        {
            appleRandomXCoord = r.nextInt(16) * 20;
            appleRandomYCoord = r.nextInt(16) * 20;
            i = -1;          //reset i to check entire snake body with new coordinates again 
        }
       
    }
    //add new apple to board 
    apple = new Sequence(appleRandomXCoord, appleRandomYCoord);


    ++applesEaten; //increase number of apples eaten 

    if(applesEaten %4 == 0) 
    {
        if(delay > 50)
        {
            delay -= REDUCE; 
            //System.out.println(delay); 
            timer.setDelay(delay);
        }
    }

}
//creates newGame
private void newGame()
{
    timer.stop();   //stop timer 
    String[] arr = {"New Game"}; //gives button options
    //shows dialogue
    JOptionPane.showOptionDialog(null, "New Game?", "New Game", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, arr, arr[0]); 

    //resets all of the variables and generates new snake and apple positions
    snake = new ArrayList<> (); 
    Random r = new Random(); 
    int snakeRandomXCoord = r.nextInt(16) * 20;
    int snakeRandomYCoord = r.nextInt(16) * 20;
    int appleRandomXCoord = r.nextInt(16) * 20;
    int appleRandomYCoord = r.nextInt(16) * 20;
    while((snakeRandomXCoord == appleRandomXCoord) && (snakeRandomYCoord == appleRandomYCoord))
    {
    
        appleRandomXCoord = r.nextInt(16) * 20;
        appleRandomYCoord = r.nextInt(16) * 20;

    }
    
    snake.add(new Sequence(snakeRandomXCoord,snakeRandomYCoord));
    apple = new Sequence(appleRandomXCoord, appleRandomYCoord); 
    applesEaten = 0; 
    currDir = 'h' ;                 //set to something random so does not move at start
    lastKeyCodePressed = 'k'; 
    delay = 250;            
    timer.setDelay(delay);          //reset timer and start it again 
    timer.restart();

}

} 




