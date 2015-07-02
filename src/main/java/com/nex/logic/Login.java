/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nex.logic;

import com.nex.biopass.DBManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author M
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String grouping = request.getParameter("group");
        
        System.out.println("--System Login Attempt--");
        System.out.println("Username: "+username);
        System.out.println("Password: "+password);
        System.out.println("Grouping: "+grouping);
  
        String responseMessage = null;
        try
        {
            DBManager db = DBManager.getInstance();
            if(!db.usernameExists(username))
            {
                responseMessage = "Username ("+username+") does not exist.";
            }
            else if(!db.passwordMatch(username, password))
            {
                responseMessage = "Password does not match.";
            }
            else
            {
                //default threshold
                double threshold  = 1.8;
                //build Analyzer and pass responsibility over
                String sql = "SELECT dev FROM user WHERE username = \""+username+"\"";
                ResultSet rs;
                rs = db.execute(sql);
                
                while(rs.next())
                {
                    System.out.println("Retreiving deviation setting");
                    threshold = rs.getDouble("dev");
                }
                
                request.getSession().setAttribute("user", username);
                Analyzer sessionTest = new Analyzer(username, grouping, threshold);
                sessionTest.run();
                
                if(!Analyzer.valid)
                {
                    responseMessage = "Invalid :: Hold: "+Analyzer.hold+" deviations, Fly: "+Analyzer.fly+" deviations.\n";
                }
                else if (Analyzer.error)
                {
                    responseMessage = "Inconsistency error detected in biometric history. "
                            + "It would appear that you can't spell your "
                            + "password correctly six times in a row. Try not. Do… or do not. There is no try.\n"
                            + "Please recreate "+username+"'s account.\n"
                            + "Invalid account has been automatically deleted, your attempt at gunking up my database has failed.";
                    try {
                        //the magic of cascading deletes
                        db.executeUpdate("DELETE FROM user WHERE username = \""+username+"\"");
                    } catch (SQLException ex) {
                        Logger.getLogger(Delete.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    DBManager.getInstance().insertGrouping(username, grouping);
                    request.getSession().setAttribute("user", username);
                    request.getSession().setAttribute("hold", Analyzer.hold);
                    request.getSession().setAttribute("fly", Analyzer.fly);
                    request.getSession().setAttribute("threshold", Analyzer.threshold);
                    request.getSession().setAttribute("history", Analyzer.compareGroup);
                    request.getSession().setAttribute("compare", Analyzer.testGroup);
                    responseMessage = "valid";                    
                }
                
                
            }
        }
        catch (SQLException | ClassNotFoundException ex) 
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            out.print(responseMessage);
        }
        

    
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
