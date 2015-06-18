/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nex.biopass;

/**
 *
 * @author M
 */
public class Capture {
    
    private final int ascii;
    private final int time;
    private final long start;

    Capture(int newAscii, int newTime, long newStart)
    {
        //
        this.ascii = newAscii;
        this.time  = newTime;
        this.start = newStart;
    }
    
    public int getAscii()
    {
        return this.ascii;
    }
    
    public int getTime()
    {
        return this.time;
    }
    
    public long getStart()
    {
        return this.start;
    }
}
