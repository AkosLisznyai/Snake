/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bead3;

/**
 *  The body of the snake
 * @author Lsiznyai Ákos
 */
public class Body {
    
    protected int x;
    protected int y;
    
    /**
     * Creates the body of the snake
     * @param px The x coordinate of the body 
     * @param py The y coordinate of the body
     */
    public Body(int px, int py)
    {
        this.x = px;
        this.y = py;
    }
    
    public int getX()
    {
        return this.x;
    }
    
    public int getY()
    {
        return this.y;
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
