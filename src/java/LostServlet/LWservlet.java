/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LostServlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author russ
 */
public class LWservlet extends HttpServlet {

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
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        
        ServletContext servletContext = getServletContext();
        
        gameInnards game = new gameInnards();

        String user = (String) session.getAttribute("user");
        String character = (String) session.getAttribute("character");
  
        if(request.getParameter("replay") != null){
            game.storeResults(request, servletContext);
            game.setNewGame(request, servletContext);
            
        }

        if (user != null && character != null) {
            game.whatDidWeGet(request ,response, servletContext);
        }

        if (user == null) {
            String ruser = (String) request.getParameter("user");
            
            if (ruser == null) {
                response.sendRedirect("login.jsp");
            } else {
                user = (String) request.getParameter("user");
                session.setAttribute("user", user);
            }
        }

        if (session.getAttribute("game") == null && user != null) {
            session.setAttribute("game", "on");
            response.sendRedirect("select.jsp");
            
        }

        if (request.getParameter("submit") != null) {
            character = request.getParameter("submit");
            session.setAttribute("character", character);
            game.setNewGame(request, servletContext);
            response.sendRedirect("LWservlet");
        }
//response.sendRedirect("LWservlet");
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LWservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LWservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
