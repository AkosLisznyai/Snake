/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bead3;

/**
 * Stores a name and a score
 * @author Lisznyai Ákos
 */
public class Score {
    
    private final String name;
    private final int score;
    
    public Score(String n, int s)
    {
        this.name = n;
        this.score = s;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public int getScore()
    {
        return this.score;
    }
    
}
