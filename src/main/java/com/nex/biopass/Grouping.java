/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nex.biopass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author M
 */
public class Grouping {
    private List<Capture> captures = new ArrayList<>();
    private String originalMessage;
    
    public Grouping(String message)
    {
        this.originalMessage = message;
        //int ascii, int time, float start
        //65:{23523452345345:222} etc.
        String[] captureParts = message.split(" ");
        for(int i = 0; i < captureParts.length; i++)
        {
            String[] asciiPart;
            asciiPart = captureParts[i].split(":");
            String[] startTimePart;
            startTimePart = asciiPart[1].split("-");
            System.out.println("Creating Capture ("+asciiPart[0]+
                    ", "+startTimePart[1]+", "+startTimePart[0]+")");
            Capture newCapture = new Capture(Integer.valueOf(asciiPart[0]), 
                    Integer.valueOf(startTimePart[1]), 
                    Long.valueOf(startTimePart[0]));
            this.captures.add(newCapture);
        }
    }
    
    public List<Capture> getCaptures()
    {
        return this.captures;
    }
    
    public void save(String user) throws SQLException, ClassNotFoundException
    {
        DBManager db = DBManager.getInstance();
        db.insertGrouping(user, this.originalMessage);
    }
}
