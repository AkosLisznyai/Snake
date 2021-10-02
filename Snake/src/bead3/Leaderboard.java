/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bead3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Connects to the database to access the scores.
 * @author Lisznyai Ákos
 */
public class Leaderboard {
    private ArrayList<Score> scores = new ArrayList<>();
    Connection connection;
    PreparedStatement insertStatement;
    
    /**
     * creates the connection
     * @throws SQLException 
     */
    public Leaderboard() throws SQLException
    {
        String dbURL = "jdbc:derby://localhost:1527/snake;";
        connection = DriverManager.getConnection(dbURL);
        String insertQuery = "INSERT INTO SCORES (ID, NAME, POINTS) VALUES (?, ?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
        setScores();
    }
    
    /**
     * Copies the scores from the database
     * @throws SQLException 
     */
    private void setScores() throws SQLException
    {
        String query = "SELECT * FROM SCORES";
        Statement smt = connection.createStatement();
        ResultSet res = smt.executeQuery(query);
        while(res.next())
        {
            String s = res.getString(2);
            int i = res.getInt(3);
            this.scores.add(new Score(s,i));
        }
        sortScore();
    }
    
    /**
     * Sorts the scores in descending order(by score)
     */
    private void sortScore()
    {
        Collections.sort(this.scores, new Comparator<Score>()
        {
            @Override
            public int compare(Score t, Score t1)
            {
                return t1.getScore() - t.getScore();
            }
        });
    }
    
    /**
     * Inserts a new score into the database;
     * @param n
     * @param s
     * @throws SQLException 
     */
    public void insertScore(String n, int s) throws SQLException
    {
        int i = this.scores.size()+1;
        insertStatement.setInt(1, i);
        insertStatement.setString(2, n);
        insertStatement.setInt(3, s);
        insertStatement.executeUpdate();
        this.scores.add(new Score(n,s));
        sortScore();
    }
    
    /**
     * Turns the top 10 scores into a string
     * @return 
     */
    public String topString()
    {
        int count = 1;
        String top = "";
        if(this.scores.size() <= 10)
        {
            for(Score s : this.scores)
            {
                String temp = top;
                top = temp + count + ". " + s.getName() + ": " + s.getScore() + System.lineSeparator();
                count++;
            }
        }
        else
        {
            while(count <= 10)
            {
                String temp = top;
                top = temp + count + ". " + this.scores.get(count-1).getName() + ": " + this.scores.get(count-1).getScore() + System.lineSeparator();
                count++;
            }
        }
        return top;
    }
}
