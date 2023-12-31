import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements ActionListener{
    
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; // How big we want the object in this game
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; // how many objects we can fit on the screen. 14,400
    static final int DELAY = 75; // create a delay for the timer
    final int x[] = new int[GAME_UNITS]; // this array holds the all the x-coordinates of the snake body parts including the head
    final int y[] = new int[GAME_UNITS]; // // this array holds the all the y-coordinates of the snake body parts including the head
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R'; // R for right, L for left, U for up, D for down. It's set the initial direction to Right
    boolean running = false;
    Timer timer; // Creating an instance of the Timer class
    Random random; // Creating an instance of the Random class
    
    private BufferedImage backgroundImage;

   GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        loadBackgroundImage(); // Load the background image
        startGame();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("snake.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startGame(){
           newApple(); // create a new apple when the game starts
           running = true;
           timer = new Timer(DELAY, this); // Determine how fast the game is. You put "this" argument because we are using the actionListner interface
           timer.start();
    }
    
    @Override
    public void paintComponent(Graphics g){
           super.paintComponent(g);
           // Draw the background image
           g.drawImage(backgroundImage, 0, 0, null);
           draw(g);
    }

    public void draw(Graphics g) {

        if(running){
            /* 
            // Draw the grid
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                // Set the color for the grid lines. w/o this, you can not see the line cz they are all black
                g.setColor(Color.GRAY);
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);  // Draw vertical lines. g.drawLine(x1,y1, x2,y2) is for 1 is starting coordinates, 2 is for end coordinates
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);  // Draw horizontal lines
            }
            */
                
                g.setColor(Color.red);
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Making a snake
            for(int i = 0; i < bodyParts; i++){
                    if(i == 0){
                        g.setColor(Color.green);
                        g.fillRect(x[i], y[i],UNIT_SIZE, UNIT_SIZE);
                    }
                    else{
                        g.setColor(new Color(45, 180, 0));

                        g.fillRect(x[i], y[i],UNIT_SIZE, UNIT_SIZE);
                    }
            } 
             g.setColor(Color.red);
             g.setFont(new Font("Ink Free", Font.BOLD, 40));
             FontMetrics metrics = getFontMetrics(g.getFont());
             g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());   
        }
        else{
            gameOver(g);
        }
    }

    public void newApple(){
       appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
       appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
       for(int i = bodyParts; i > 0; i --){
        x[i] = x[i-1];
        y[i] = y[i-1];
       }
       switch(direction){
        case 'U': // U for up
            y[0] = y[0] - UNIT_SIZE;
            break;
        case 'D': // U for up
            y[0] = y[0] + UNIT_SIZE;
            break;
        case 'L': // U for up
            x[0] = x[0] - UNIT_SIZE;
            break;
        case 'R': // U for up
            x[0] = x[0] + UNIT_SIZE;
            break;
       }
    }
    public void checkApple(){
          if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
          }
    }
    public void checkCollisions(){
        // this checks if head collides with body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i] && (y[0] == y[i]))){
                running = false;
            }
        }
        // check if head touches left border
        if(x[0] < 0){
            running = false;
        }
        // check if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        // check if head touches top border
        if(y[0] < 0){
            running = false;
        }
        // check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize()); 

        // Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
   
    public class MyKeyAdapter extends KeyAdapter{  // control the snake
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                     if(direction != 'R'){
                        direction = 'L'; // this if statement limits users only 90 degree terns, cz if 180 degrees, the snake body collides itself
                     }
                     break;
            case KeyEvent.VK_RIGHT:
                     if(direction != 'L'){
                        direction = 'R'; 
                     }
                     break;
            case KeyEvent.VK_UP:
                     if(direction != 'D'){
                        direction = 'U'; 
                     }
                     break;
            case KeyEvent.VK_DOWN:
                     if(direction != 'U'){
                        direction = 'D'; 
                     }
                     break;
            }
        }
    }
}
