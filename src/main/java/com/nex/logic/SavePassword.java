/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nex.logic;

import com.nex.biopass.DBManager;
import com.nex.biopass.Grouping;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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
@WebServlet(name = "SavePassword", urlPatterns = {"/SavePassword"})
public class SavePassword extends HttpServlet {

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
            throws ServletException, IOException 
    {
        
        int attemptNum = (int) request.getSession().getAttribute("attemptNumber");
        String message = request.getParameter("capture");
        
        if(attemptNum < 6)
        {
            List<Grouping> groups = (List<Grouping>) request.getSession().getAttribute("groups");
            groups.add(new Grouping(message));
            request.getSession().setAttribute("groups", groups);
            attemptNum++;
            request.getSession().setAttribute("attemptNumber", attemptNum);
            request.getRequestDispatcher("gatherPasswords.jsp").forward(request, response);
        }
        else
        {
            String user = (String) request.getSession().getAttribute("user");
            String password = request.getParameter("password");
            List<Grouping> groups = (List<Grouping>) request.getSession().getAttribute("groups");
            
            try {
                DBManager db = DBManager.getInstance();
                String sql = "INSERT INTO user VALUES(null, \'"+user+"\', \'"+password+"\', 1.8)";
                db.executeUpdate(sql);
                
                for (int i = 0; i < groups.size(); i++)
                {
                    groups.get(i).save(user);
                }   
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(SavePassword.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            response.sendRedirect("Logout");
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
