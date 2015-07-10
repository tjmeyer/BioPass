/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nex.logic;

import com.nex.biopass.Grouping;
import com.nex.biopass.Capture;
import com.nex.biopass.DBManager;
import static java.lang.Math.abs;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author M
 */
public class Analyzer {
    
    public static Grouping testGroup;
    public static List<Grouping> compareGroup = new ArrayList<>();
    public static boolean valid = false;
    public static double fly = 0;
    public static double hold = 0;
    public static double threshold;
    public static boolean error = false;
    public static String errorMessage = "";
    
    public Analyzer(String username, String message, double thresh) throws SQLException, ClassNotFoundException
    {
        threshold = thresh;
        testGroup = new Grouping(message);
        //pull 10 most recent groups from database based on user id
        DBManager db = DBManager.getInstance();
        String query = "SELECT id FROM user WHERE username = \""+username+"\"";
        int userId = -1;
        ResultSet rs = db.execute(query);
        if(rs.next())
        {
            userId = rs.getInt("id");
        }
        else
        {
            System.out.println("User "+username+" could not be found!");
        }
        
        // log past groupings
        query = "SELECT id, message FROM grouping WHERE user_id = "+userId+
                " ORDER BY ID DESC LIMIT 10";
        rs = db.execute(query);
        while(rs.next())
        {
            System.out.println("Adding group from database to Analyzer");
            compareGroup.add(new Grouping(rs.getString("message")));
        }
    }
   
    public void run()
    {
        List<DescriptiveStatistics> holdStats = new ArrayList<>();
        List<DescriptiveStatistics> flyStats  = new ArrayList<>();
        DescriptiveStatistics holdAnalysis = new DescriptiveStatistics();
        DescriptiveStatistics flyAnalysis  = new DescriptiveStatistics();
      
        System.out.println("Running Analyzer:");
        System.out.println("compareGroup.size() :: "+compareGroup.size());
        System.out.println("compareGroup.get(0).getCaptures().size() :: " + compareGroup.get(0).getCaptures().size());
              
        int currentCapture = 0;        
      
        // this while loop will prepare the compareGroup for analysis
        while(currentCapture < compareGroup.get(0).getCaptures().size())
        {
            System.out.println("currentCapture # = "+currentCapture);
            
            DescriptiveStatistics hold = new DescriptiveStatistics();
            DescriptiveStatistics fly = new DescriptiveStatistics();
            for(int i = 0; i < compareGroup.size(); i++)
            {
                if(compareGroup.get(i).getCaptures().size() > currentCapture)
                {
                    Capture current = compareGroup.get(i).getCaptures().get(currentCapture);

                    // adding captures hold time to hold averages
                    hold.addValue(current.getTime());

                    // we can't calculate fly time if we're indexing the last capture
                    // so we need to stop one short
                    if(compareGroup.get(i).getCaptures().size() > currentCapture + 1)
                    {
                        // fly time = holdTime + totalTime - nextCapture'sTotalTime
                        Capture next = compareGroup.get(i).getCaptures().get(currentCapture + 1);

                        long flyTime = current.getTime() + current.getStart() - next.getStart();

                        // adding capture's fly time to fly time averages
                        fly.addValue(flyTime);
                    }
                }
            }
            
            // push current stat objects onto the stack
            holdStats.add(hold);
            flyStats.add(fly);
            currentCapture++;
        }
        
        // now complare our test grouping to our gathered stats
        for(int j = 0; j < testGroup.getCaptures().size(); j++)
        {
            Capture current = testGroup.getCaptures().get(j);
            // as in hold standard deviation
            double holdDev = 0;
            double flyDev = 0;
            
            double holdTime = current.getTime();

            try
            {
                holdDev = abs(holdTime - holdStats.get(j).getMean()) / 
                        holdStats.get(j).getStandardDeviation();

                if(j + 1 < testGroup.getCaptures().size())
                {
                    double flyTime = current.getTime() + current.getStart() - 
                            testGroup.getCaptures().get(j + 1).getStart();

                    flyDev = abs(flyTime - flyStats.get(j).getMean()) / 
                            flyStats.get(j).getStandardDeviation();
                }
            }
            catch(java.lang.IndexOutOfBoundsException e)
            {
                error = true;
                errorMessage = e.toString();
                System.out.println("ERROR CAUGHT!!!!");
            }
            
            holdAnalysis.addValue(holdDev);
            flyAnalysis.addValue(flyDev);
        }
        System.out.println("Average Hold Deviation: " + holdAnalysis.getMean());
        System.out.println("Average Fly Deviation:  " + flyAnalysis.getMean());
        fly = flyAnalysis.getMean();
        hold = holdAnalysis.getMean();
        valid = holdAnalysis.getMean() < threshold && flyAnalysis.getMean() < threshold;
    }
}