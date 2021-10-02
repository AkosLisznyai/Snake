/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bead3;

import java.util.ArrayList;

/**
 * The head of the snake
 * @author Lisznyai Ákos
 */
public class Head extends Body {
    
    private String dir;
    private ArrayList<Body> body = new ArrayList<>();
    
    /**
     * Creates the snake
     * @param px The X coordinate of the head of the snake
     * @param py The Y coordinate of the head of the snake
     * @param d The direction the snake is facing towards
     */
    public Head(int px, int py, String d) 
    {
        super(px, py);
        this.dir = d;
        switch(d)
        {
            case "RIGHT":
                this.body.add(new Body(px-10, py));
                break;
            case "DOWN":
                this.body.add(new Body(px, py-10));
                break;
            case "LEFT":
                this.body.add(new Body(px+10, py));
                break;
            case "UP":
                this.body.add(new Body(px, py+10));
                break;
        }
    }
    
    public String getDir()
    {
        return this.dir;
    }
    
    public ArrayList<Body> getBody()
    {
        return this.body;
    }
    
    public void setDir(String d)
    {
        this.dir = d;
    }
    
    /**
     * Adds more body parts to the snake
     * @param px the x coordinate of the body part
     * @param py the y coordinate of the body part
     */
    public void addBody(int px, int py)
    {
        Body temp = new Body(px, py);
        this.body.add(temp);
    }
}
