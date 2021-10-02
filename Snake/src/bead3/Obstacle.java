/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bead3;

/**
 * Rocks and Food
 * @author Lisznyai Ákos
 */
public class Obstacle {
    
    private int x;
    private int y;
    private final Boolean apple;
    
    /**
     * Creates the rock/food
     * @param px The x position of the obstacle
     * @param py The y position of the obstacle
     * @param a Determines if the obstacle is a rock or food
     */
    public Obstacle(int px, int py, Boolean a)
    {
        this.x = px;
        this.y = py;
        this.apple = a;
    }
    
    public int getX()
    {
        return this.x;
    }
    
    public int getY()
    {
        return this.y;
    }
    
    public Boolean isApple()
    {
        return this.apple;
    }
    
    public void setX(int px)
    {
        this.x = px;
    }
    
    public void setY(int py)
    {
        this.y = py;
    }
}
