/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bead3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * The engine of the game.
 * @author Lisznyai Ákos
 */
public class Snake extends JPanel implements ActionListener {

    private final Image apple = new ImageIcon("src/bead3/img/apple.png").getImage();
    private final Image rock = new ImageIcon("src/bead3/img/rock.png").getImage();
    private final Image body = new ImageIcon("src/bead3/img/body.png").getImage();
    private final Image head = new ImageIcon("src/bead3/img/head.png").getImage();
    
    private final int G_WIDTH = 300;
    private final int G_HEIGHT = 300;
    private final int O_SIZE = 10;
    private final int DELAY = 140;
    private final int R_POS = 29;
    
    private ArrayList<Obstacle> rocks = new ArrayList<>();
    private Obstacle food;
    private Head snake;
    private Timer timer;
    private int score;
    private String name;
    private Boolean game;
    private JLabel timelabel;
    private JLabel scorelabel;
    private Timer milis;
    private long start;
    private final Leaderboard lb;
    
    /**
     * Starts the game.
     * @throws SQLException 
     */
    public Snake() throws SQLException
    {
        this.lb = new Leaderboard();
        initField();
    }
    
    public Leaderboard getLeaderboard()
    {
        return this.lb;
    }
    
    /**
     * Determines the number of rocks
     * @return the number of rocks
     */
    private int randRocks()
    {
        return (int)(Math.random()*6); 
    }
    
    /**
     * Creates a random coordinate
     * @return A random coordinate
     */
    private int randPos()
    {
        return ((int)(Math.random()*R_POS))*O_SIZE;
    }
    
    /**
     * Determines a random direction
     * @return the direction
     */
    private String randDir()
    {
        int num = (int)(Math.random()*4);
        switch(num)
        {
            case 1:
                return "RIGHT";
            case 2:
                return "DOWN";
            case 3:
                return "LEFT";
            case 4:
                return "UP";
        }
        return "RIGHT";
    }
    
    /**
     * Determines the position of the rocks and food
     * @param n the amount of rocks, or 1 if food
     * @param isApple wether it's food or rocks
     */
    private void generateObstacle(int n, Boolean isApple)
    {
        Boolean collide;
        int randX;
        int randY;
        int succCount;
        for(int i = 0; i<n; i++)
        {
            collide = true;
            succCount = 0;
            while(collide)
            {
                succCount = 0;
                randX = randPos();
                randY = randPos();
                if((randX >= this.snake.getX()+3*O_SIZE && randY >= this.snake.getY()+3*O_SIZE) || (randX <= this.snake.getX()-3*O_SIZE && randY <= this.snake.getY()-3*O_SIZE))
                {
                    succCount++;
                }
                
                if((randX >= this.food.getX()+3*O_SIZE && randY >= this.food.getY()+3*O_SIZE) || (randY <= this.food.getY()-3*O_SIZE && randX <= this.food.getX()-3*O_SIZE))
                {
                    succCount++;
                }
                
                for(Body b : this.snake.getBody())
                {
                    if(randX != b.getX() && randY != b.getY())
                    {
                        succCount++;
                    }
                }
                
                for(Obstacle r : this.rocks)
                {
                    if(randX != r.getX() && randY != r.getY())
                    {
                        succCount++;
                    }
                }
                
                if(succCount == 2+this.snake.getBody().size()+this.rocks.size())
                {
                    if(isApple)
                    {
                        this.food.setX(randX);
                        this.food.setY(randY);
                    }
                    else
                    {
                        this.rocks.add(new Obstacle(randX, randY, false));
                    }
                    collide = false;
                }
            }
        }
    }
    
    
    /**
     * Creates the field
     */
    private void initField()
    {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(G_WIDTH, G_HEIGHT));
        this.timelabel = new JLabel(" ");
        this.scorelabel = new JLabel(this.score + " points");
        this.timelabel.setHorizontalAlignment(JLabel.RIGHT);
        this.scorelabel.setHorizontalAlignment(JLabel.RIGHT);
        
        
        this.milis = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timelabel.setText(elapsedTime() + " ms");
            }
        });
        this.start = System.currentTimeMillis();
        this.milis.start();
        
        initGame();
    }
    
    /**
     * determines the time since the game started
     * @return The time in milliseconds
     */
    private long elapsedTime()
    {
        return System.currentTimeMillis() - this.start;
    }
    
    public JLabel getTimer()
    {
        return this.timelabel;
    }
    
    public JLabel getScore()
    {
        return this.scorelabel;
    }
    
    /**
     * Sets up the game board
     */
    private void initGame()
    {
        this.game = true;
        this.score = 0;
        this.name = "";
        String dir = randDir();
        this.snake = new Head(150, 150, dir);
        this.food = new Obstacle(0, 0, true);
        
        generateObstacle(1, true);
        generateObstacle(randRocks(), false);
        
        this.timer = new Timer(DELAY, this);
        this.timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        try {
            drawGame(g);
        } catch (SQLException ex) {
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Draws the graphics
     * @param g
     * @throws SQLException 
     */
    private void drawGame(Graphics g) throws SQLException
    {
        if(game)
        {
            g.drawImage(this.apple,this.food.getX(), this.food.getY(),this);
            g.drawImage(this.head, this.snake.getX(), this.snake.getY(), this);
            for(Body b : this.snake.getBody())
            {
                g.drawImage(this.body, b.getX(), b.getY(), this);
            }
            
            for(Obstacle r : this.rocks)
            {
                g.drawImage(this.rock, r.getX(), r.getY(), this);
            }
            
            Toolkit.getDefaultToolkit().sync();
        }
        else
        {
            gameOver(g);
        }
    }
    
    /**
     * The game over screen
     * @param g
     * @throws SQLException 
     */
    private void gameOver(Graphics g) throws SQLException
    {
        String msg = "Score: " + this.score;
        Font font = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics met = getFontMetrics(font);
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (G_WIDTH - met.stringWidth(msg)) / 2, G_HEIGHT / 2);
        
        try
        {
            this.name = (String)JOptionPane.showInputDialog(this, "Enter your name to register your score:");
        }
        catch(NullPointerException e)
        {
            
        }
        if(!this.name.equals("") && this.name != null)
        {
            this.lb.insertScore(this.name, this.score);
        }
    }
    
    /**
     * Moves the body of the snake
     * @param px the x position of the head of the snake before it moved
     * @param py the y position of the head of the snake before it moved
     */
    private void moveBody(int px, int py)
    {
        int posX = px;
        int posY = py;
        int tempX;
        int tempY;
        for(Body b : this.snake.getBody())
        {
            tempX = b.getX();
            tempY = b.getY();
            b.setX(posX);
            b.setY(posY);
            posX = tempX;
            posY = tempY;
        }
    }
    
    /**
     * Determines what happens when the snake collides with something
     * @param px The x position of the last body part before it moved
     * @param py The y position of the last body part before it moved
     */
    private void checkCollision(int px, int py)
    {
        if(this.snake.getX() == this.food.getX() && this.snake.getY() == this.food.getY())
        {
            this.snake.addBody(px, py);
            generateObstacle(1, true);
            this.score++;
            this.scorelabel.setText(this.score + " points");
        }
        
        if(this.snake.getX() < 0 || this.snake.getX() >= G_WIDTH || this.snake.getY() < 0 || this.snake.getY() >= G_HEIGHT)
        {
            this.game = false;
        }
        
        for(Body b : this.snake.getBody())
        {
            if(this.snake.getX() == b.getX() && this.snake.getY() == b.getY())
            {
                this.game = false;
            }
        }
        
        for(Obstacle r : this.rocks)
        {
            if(this.snake.getX() == r.getX() && this.snake.getY() == r.getY())
            {
                this.game = false;
            }
        }
        
        if(!this.game)
        {
            this.timer.stop();
            this.milis.stop();
        }
    }
    
    /**
     * Moves the snake in a direction
     */
    private void move()
    {
        int lastX = this.snake.getBody().get(this.snake.getBody().size()-1).getX();
        int lastY = this.snake.getBody().get(this.snake.getBody().size()-1).getY();
        
        int headX = this.snake.getX();
        int headY = this.snake.getY();
        
        int mov;
        switch(this.snake.getDir())
        {
            case "RIGHT":
                mov = this.snake.getX() + this.O_SIZE;
                this.snake.setX(mov);
                break;
            case "LEFT":
                mov = this.snake.getX() - this.O_SIZE;
                this.snake.setX(mov);
                break;
            case "UP":
                mov = this.snake.getY() - this.O_SIZE;
                this.snake.setY(mov);
                break;
            case "DOWN":
                mov = this.snake.getY() + this.O_SIZE;
                this.snake.setY(mov);
                break;
        }
        moveBody(headX, headY);
        checkCollision(lastX, lastY);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(game){move();}
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        /**
        * The movement keys
        * @param e 
        */
        @Override
        public void keyPressed(KeyEvent e) 
        {
            int key = e.getKeyCode();
            
            switch(key)
            {
                case KeyEvent.VK_W:
                    if(!snake.getDir().equals("DOWN"))
                    {
                        snake.setDir("UP");
                    }
                    break;
                case KeyEvent.VK_S:
                    if(!snake.getDir().equals("UP"))
                    {
                        snake.setDir("DOWN");
                    }
                    break;
                case KeyEvent.VK_A:
                    if(!snake.getDir().equals("RIGHT"))
                    {
                        snake.setDir("LEFT");
                    }
                    break;
                case KeyEvent.VK_D:
                    if(!snake.getDir().equals("LEFT"))
                    {
                        snake.setDir("RIGHT");
                    }
                    break;
            }
        }

    }
    
    
}
