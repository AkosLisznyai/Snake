/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bead3;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Lisznyai Ákos
 */
public class SnakeGUI {
    
    private JFrame window;
    private Snake game;
    
    /**
     * Creates the window for the game.
     * @throws SQLException 
     */
    public SnakeGUI() throws SQLException
    {
        this.window = new JFrame("Snake");
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.game = new Snake();
        this.window.getContentPane().add(this.game, BorderLayout.CENTER);
        this.window.getContentPane().add(this.game.getTimer(), BorderLayout.SOUTH);
        this.window.getContentPane().add(this.game.getScore(), BorderLayout.NORTH);
        
        JMenuBar menuMain = new JMenuBar();
        this.window.setJMenuBar(menuMain);
        JMenuItem menuNew = new JMenuItem("New Game");
        menuMain.add(menuNew);
        menuNew.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent a)
            {
                window.getContentPane().remove(game);
                window.getContentPane().remove(game.getTimer());
                window.getContentPane().remove(game.getScore());
                try {
                    game = new Snake();
                } catch (SQLException ex) {
                    Logger.getLogger(SnakeGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                window.getContentPane().add(game, BorderLayout.CENTER);
                window.getContentPane().add(game.getTimer(), BorderLayout.SOUTH);
                window.getContentPane().add(game.getScore(), BorderLayout.NORTH);
                window.pack();
                window.repaint();
                game.requestFocusInWindow();
            }
            
        });
        
        JMenuItem hs = new JMenuItem("Highscore");
        menuMain.add(hs);
        hs.addActionListener(new ActionListener()
        {
           @Override
           public void actionPerformed(ActionEvent a)
           {
               JOptionPane.showMessageDialog(window, game.getLeaderboard().topString(), "Ranglista", JOptionPane.PLAIN_MESSAGE);
           }
        });
        
        JMenuItem exit = new JMenuItem("Exit");
        menuMain.add(exit);
        exit.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent a) 
            {
                System.exit(0);
            }
        });
        
        this.window.setResizable(false);
        this.window.pack();
        this.window.setVisible(true);
    }
    
}
