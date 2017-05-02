/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LostServlet;

import Db_con.ConnectionPool;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author russ
 */
public class gameInnards {

    String user, charater, compCH, smove, userRestrictions, compRestrictions;
    int move, compHP, userHP;
    ResultSet res = null;
    HttpSession session;
    Statement statement;
    String sql = "";

    public void whatDidWeGet(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws SQLException {
        session = request.getSession();

        ConnectionPool connectionPool = (ConnectionPool) servletContext.getAttribute("connectionPool");
        Connection connection = connectionPool.getConnection();
        statement = connection.createStatement();

        boolean doScore = false;
        String message = "Do only EXTENDED RANGE next turn.";
        String title = "EXTENDED RANGE BLOCKING";
        String restrictions = "", modtype = null;
        int score = 0, modscore = 0;
        user = (String) session.getAttribute("user");
        charater = (String) session.getAttribute("character");
        userRestrictions = (String) session.getAttribute("userRestrictions");
        compCH = (String) session.getAttribute("compCH");
        compRestrictions = (String) session.getAttribute("compRestrictions");

        String pic = compCH + "_57_BW.jpg";

        smove = request.getParameter("move");

        if (session.getAttribute("compHP") != null) {
            compHP = (int) session.getAttribute("compHP");
        }
        if (smove != null) {
            move = Integer.parseInt(smove);
        }

        if (move > 0 && session.getAttribute("game") != "done") {
            int cm = compMove();
            pic = setPlayMove(cm);

            sql = "select finalPage from bookMoves where moveParchment='" + charater + "' AND page='" + cm + "' AND opponentMove='" + move + "'";
            res = statement.executeQuery(sql);
            res.next();
            String compPg = res.getString("finalPage");

            sql = "select * from bookResults where page='" + compPg + "' and picParchment like '" + charater + "%'";
            res = statement.executeQuery(sql);
            Integer cscore = 0;
            String sscore;
            while (res.next()) {

                title = res.getString("title"); // no need to the other
                message = res.getString("message"); // no need to the other
                session.setAttribute("sscore", res.getString("score")); // really need both
                modscore = res.getInt("modscore"); // really need both
                modtype = res.getString("modtype"); // really need both
                restrictions = res.getString("restrictions"); // really need both
            }
            session.setAttribute("usermod", modscore);
            session.setAttribute("usermodtype", modtype);
            ////////////////////////////////////////////////////
            Integer intscore = null;
            if (session.getAttribute("sscore") != null) {
                doScore = true;
                intscore = Integer.parseInt((String) session.getAttribute("sscore"));
            } else {
                session.setAttribute("compscore", session.getAttribute("sscore"));
            }

            sql = "select modscore from CharacterSheet where charac='" + compCH + "' AND PG='" + cm + "'";
            res = statement.executeQuery(sql);
            res.next();
            modscore = res.getInt("modscore");
            ///////////////////////
//            int speicalmod = 0;
//            if (session.getAttribute("lastcompmod") != null) {
//                if ((int) session.getAttribute("lastcompmod") > 0) {
//                    String swmod = speModtype((String) session.getAttribute("lastcompmodtype"));
//                    sql = "select modscore from CharacterSheet where charac='" + compCH + "' AND PG='" + cm + "'" + swmod;
//                    res = statement.executeQuery(sql);
//
//                    if (!res.next()) {
//                        speicalmod = 0;
//
//                    } else {
//                        speicalmod = (int) session.getAttribute("lastcompmod");
//                    }
//                }
//            }
            if (doScore) {
                session.setAttribute("compscore", (intscore + modscore)); //  + speicalmod
                userHP = (int) session.getAttribute("userHP");
                if ((intscore + modscore) > 0) {
                    userHP = (int) session.getAttribute("userHP");
                    session.setAttribute("userHP", userHP - (intscore + modscore));// + speicalmod 
                }
            }

            session.setAttribute("sscore", null);
            ////////////////////////////////////////////////////
            session.setAttribute("userRestictions", restrictions);
            session.setAttribute("message", message);
            session.setAttribute("title", title);
            setUserRestictions();
        }

        session.setAttribute("move", move);
        session.setAttribute("pic", pic);
        compHP = (int) session.getAttribute("compHP");
        userHP = (int) session.getAttribute("userHP");

        try {
            if (compHP < 1) {
                session.setAttribute("userWin", "WIN");
                session.setAttribute("compWin", "LOST");
                session.setAttribute("game", "done");

            } else if (userHP < 1) {
                session.setAttribute("compWin", "WIN");
                session.setAttribute("userWin", "LOST");
                session.setAttribute("game", "done");
            }
            response.sendRedirect("game.jsp");

        } catch (IOException ex) {
            Logger.getLogger(gameInnards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int compMove() {
        ArrayList cmov = new ArrayList<>();
        Random rand = new Random();
        String cr = compRestictions();
        sql = "select PG from CharacterSheet where charac='" + compCH + "' " + cr;

        try {
            res = statement.executeQuery(sql);
            while (res.next()) {
                cmov.add(res.getInt("PG"));
            }

//            if (statement != null) {
//                statement.close();
//            }

        } catch (SQLException ex) {
            Logger.getLogger(gameInnards.class.getName()).log(Level.SEVERE, null, ex);
        }
        int r = rand.nextInt(cmov.size());
        int outint = (int) cmov.get(r);
        return outint;
    }

    public String compRestictions() {

        String sqlPlus = "";
        String restictions = (String) session.getAttribute("compRestictions");
        if (!(boolean) session.getAttribute("compWeapon") && !"weapon back".equals(restictions)) {
            restictions = "no weapon";
        }
        switch (restictions) {
            case "no weapon":  /// do some thing more
                sqlPlus = "AND (color = 'yellow' OR color = 'green' OR moveTypeB = 'kick') AND moveTypeB = 'wild swing'";
                session.setAttribute("compWeapon", false);
                break;
            case "weapon back":  /// some thing more ??
                sqlPlus = "AND moveTypeA != 'extended'";
                session.setAttribute("compWeapon", true);
                break;
            case "shield smashed":  /// do some thing more 
                session.setAttribute("compShield", false);
                break;
            case "no red":
                sqlPlus = "AND color != 'red' AND moveTypeA != 'extended'";
                break;
            case "no thrusts blue":
                sqlPlus = "AND color != 'blue' AND (moveTypeA != 'thrust' OR moveTypeB != 'thrust') AND moveTypeA != 'extended'";
                break;
            case "only green yellow":
                sqlPlus = "AND (color = 'green' OR color = 'yellow')";
                break;
            case "no side swing":
                sqlPlus = "AND (moveTypeA != 'side swing' OR moveTypeA != 'side swing') AND moveTypeA != 'extended'";
                break;
            case "no red side swing":
                sqlPlus = "AND color != 'red' AND (moveTypeA != 'side swing' OR moveTypeA != 'side swing') AND moveTypeA != 'extended'";
                break;
            case "no red orange":
                sqlPlus = "AND (color != 'red' OR color != 'orange') AND moveTypeA != 'extended'";
                break;
            case "no blue":
                sqlPlus = "AND color != 'blue' AND moveTypeA != 'extended'";
                break;
            case "only yellow":
                sqlPlus = "AND color = 'yellow'";
                break;
            case "no blue yellow":
                sqlPlus = "AND (color != 'blue' OR color != 'yellow') AND moveTypeA != 'extended'";
                break;
            case "skel body point":
                sqlPlus = "AND color != 'orange' AND moveTypeA != 'extended'";
                int hp = (int) session.getAttribute("compHP");
                session.setAttribute("compHP", (hp + 1));
                break;
            case "only brown":
                sqlPlus = "AND color = 'brown'";
                break;
            case "no green yellow":
                sqlPlus = "AND (color != 'green' OR color != 'yellow') AND moveTypeA != 'extended'";
                break;
            case "only jumps":
                sqlPlus = "AND moveTypeA = 'jump'";
                break;
            case "only extended range":
                sqlPlus = "AND moveTypeA = 'extended'";
                break;
            case "no orange":
                sqlPlus = "AND color != 'orange' AND moveTypeA != 'extended'";
                break;
            case "no yellow":
                sqlPlus = "AND color != 'yellow' AND moveTypeA != 'extended'";
                break;
            case "none":
                sqlPlus = "AND moveTypeA != 'extended'";
                break;
        }

        if (!(boolean) session.getAttribute("compShield")) {
            sqlPlus += " moveTypeA != 'shield block' AND moveTypeA != 'protected attacks'";
        }
        if ((boolean) session.getAttribute("compWeapon")) {
            sqlPlus += " AND PG != '46'";
        }
        return sqlPlus;
    }

    public String setPlayMove(int cm) {
        String pic = "", swmod = "";
        int modscore, score = 0, speicalmod = 0;
        boolean doScore = false;
        sql = "select finalPage from bookMoves where moveParchment='" + compCH + "' AND page='" + move + "' AND opponentMove='" + cm + "'";
        try {
            res = statement.executeQuery(sql);
            res.next();
            String pg = res.getString("finalPage");

            sql = "select picParchment, score, modscore, modtype, restrictions from bookResults where page='" + pg + "' and picParchment like '" + compCH + "%'";
            res = statement.executeQuery(sql);
            res.next();
            modscore = res.getInt("modscore"); // really need both
            String modtype = res.getString("modtype"); // really need both
            ////////  special for next turn
            session.setAttribute("lastcompmod", session.getAttribute("modscore"));
            session.setAttribute("lastcompmodtype", session.getAttribute("modscore"));
            session.setAttribute("compmod", modscore);
            session.setAttribute("compmodtype", modtype);
            ///////

            pic = res.getString("picParchment"); //yes
            if (res.getString("score") != null && !res.getString("score").isEmpty()) {
                doScore = true;
                score = res.getInt("score");
            } else {
                session.setAttribute("userscore", res.getString("score"));
            }

            session.setAttribute("compRestictions", res.getString("restrictions"));

            sql = "select modscore from CharacterSheet where charac='" + charater + "' AND PG='" + move + "'";
            res = statement.executeQuery(sql);
            res.next();
            modscore = res.getInt("modscore");
            ///////////////////
//            if (session.getAttribute("usermod") != null) {
//                if ((int) session.getAttribute("usermod") > 0) {
//                    swmod = speModtype((String) session.getAttribute("usermodtype"));
//                    sql = "select modscore from CharacterSheet where charac='" + charater + "' AND PG='" + move + "'" + swmod;
//                    res = statement.executeQuery(sql);
//
//                    if (!res.next()) {
//                        speicalmod = 0;
//
//                    } else {
//                        speicalmod = (int) session.getAttribute("usermod");
//                    }
//                }
//            }
            if (doScore) {
                session.setAttribute("userscore", (score + modscore)); //  + speicalmod
                compHP = (int) session.getAttribute("compHP");
            }
            session.getAttribute("usermodtype");
            if ((score + modscore) > 0) {
                compHP = (int) session.getAttribute("compHP");
                session.setAttribute("compHP", compHP - (score + modscore));  ///   + speicalmod
            }
//            if (statement != null) {
//                statement.close();
//            }

        } catch (SQLException e) {
            System.out.println("err : " + e);
        }
        return pic;
    }

    public void setUserRestictions() {
        ArrayList userMoves = new ArrayList<>();
        String ur = userRestictions();
        String cr = compRestictions();
        sql = "select PG from CharacterSheet where charac='" + charater + "' " + ur;

        try {
            res = statement.executeQuery(sql);
            while (res.next()) {
                userMoves.add(res.getInt("PG"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(gameInnards.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setAttribute("userMoves", userMoves);

    }

    public void setNewGame(HttpServletRequest request, ServletContext servletContext) {
        session = request.getSession();

        ConnectionPool connectionPool = (ConnectionPool) servletContext.getAttribute("connectionPool");
        try {
            Connection connection = connectionPool.getConnection();
            statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(gameInnards.class.getName()).log(Level.SEVERE, null, ex);
        }

        Random rand = new Random();
        int r = rand.nextInt(2);

        String character = (String) session.getAttribute("character");

        if ("man".equals(character)) {
            session.setAttribute("sheet", "man/man_Psheet.jpg");
            compCH = "skel";
            session.setAttribute("compDisplayName", "Skeleton");
            session.setAttribute("userDisplayName", "Man in Chainmail");
        } else {
            session.setAttribute("sheet", "skel/skel_Psheet.jpg");
            compCH = "man";
            session.setAttribute("compDisplayName", "Man in Chainmail");
            session.setAttribute("userDisplayName", "Skeleton");
        }
        String usersql = "select bodyPts from CharacterSheet where charac='" + character + "'";
        String compsql = "select bodyPts from CharacterSheet where charac='" + compCH + "'";

        try {
            res = statement.executeQuery(usersql);
            res.next();
            userHP = res.getInt("bodyPts");

            res = statement.executeQuery(compsql);
            res.next();
            compHP = res.getInt("bodyPts");
//            if (statement != null) {
//                statement.close();
//            }

        } catch (SQLException e) {
            System.out.println("err : " + e);
        }
        session.setAttribute("userHP", userHP);
        session.setAttribute("compCH", compCH);
        session.setAttribute("compHP", compHP);
        session.setAttribute("compRestictions", "only extended range");
        session.setAttribute("sheetMask", "extended_only.png");
        session.setAttribute("userWeapon", true);
        session.setAttribute("userShield", true);
        session.setAttribute("compWeapon", true);
        session.setAttribute("compShield", true);

        session.setAttribute("compscore", null);
        session.setAttribute("userscore", null);
        session.setAttribute("userMoves", null);
        session.setAttribute("compWin", null);
        session.setAttribute("userWin", null);
        session.setAttribute("game", "on");
    }

    public String userRestictions() {

        String sqlPlus = "", sheetMask = "";
        String restictions = (String) session.getAttribute("userRestictions");
        if (!(boolean) session.getAttribute("userWeapon") && !"weapon back".equals(restictions)) {
            restictions = "no weapon";
        }
        switch (restictions) {
            case "no weapon":  /// do some thing more
                sqlPlus = "AND (color = 'yellow' OR color = 'green' OR moveTypeB = 'kick') AND moveTypeB = 'wild swing'";
                session.setAttribute("userWeapon", false);
                break;
            case "weapon back":  /// some thing more ??
                sqlPlus = "AND moveTypeA != 'extended'";
                session.setAttribute("userWeapon", true);
                sheetMask = "no_restrictions.png";
                break;
            case "shield smashed":  /// do some thing more 
                session.setAttribute("userShield", false);
                sheetMask = "no_restrictions.png";
                break;
            case "no red":
                sqlPlus = "AND color != 'red' AND moveTypeA != 'extended'";
                sheetMask = "no_red.png";
                break;
            case "no thrusts blue":
                sqlPlus = "AND color != 'blue' AND (moveTypeA != 'thrust' OR moveTypeB != 'thrust') AND moveTypeA != 'extended'";
                sheetMask = "no_thrusts_blue.png";
                break;
            case "only green yellow":
                sqlPlus = "AND (color = 'green' OR color = 'yellow')";
                sheetMask = "only_green_yellow.png";
                break;
            case "no side swing":
                sqlPlus = "AND (moveTypeA != 'side swing' OR moveTypeA != 'side swing') AND moveTypeA != 'extended'";
                sheetMask = "no_side_swing.png";
                break;
            case "no red side swing":
                sqlPlus = "AND color != 'red' AND (moveTypeA != 'side swing' OR moveTypeA != 'side swing') AND moveTypeA != 'extended'";
                sheetMask = "no_red_side_swing.png";
                break;
            case "no red orange":
                sqlPlus = "AND (color != 'red' OR color != 'orange') AND moveTypeA != 'extended'";
                sheetMask = "no_red_orange.png";
                break;
            case "no blue":
                sqlPlus = "AND color != 'blue' AND moveTypeA != 'extended'";
                sheetMask = "no_blue.png";
                break;
            case "only yellow":
                sqlPlus = "AND color = 'yellow'";
                sheetMask = "only_yellow.png";
                break;
            case "no blue yellow":
                sqlPlus = "AND (color != 'blue' OR color != 'yellow') AND moveTypeA != 'extended'";
                sheetMask = "no_blue_yellow.png";
                break;
            case "skel body point":
                sqlPlus = "AND color != 'orange' AND moveTypeA != 'extended'";
                sheetMask = "no_orange.png";
                int hp = (int) session.getAttribute("userHP");
                session.setAttribute("userHP", (hp + 1));
                break;
            case "only brown":
                sqlPlus = "AND color = 'brown'";
                sheetMask = "only_brown.png";
                break;
            case "no green yellow":
                sqlPlus = "AND (color != 'green' OR color != 'yellow') AND moveTypeA != 'extended'";
                sheetMask = "no_green_yellow.png";
                break;
            case "only jumps":
                sqlPlus = "AND moveTypeA = 'jump'";
                sheetMask = "only_jumps.png";
                break;
            case "only extended range":
                sqlPlus = "AND moveTypeA = 'extended'";
                sheetMask = "extended_only.png";
                break;
            case "no orange":
                sqlPlus = "AND color != 'orange' AND moveTypeA != 'extended'";
                sheetMask = "no_orange.png";
                break;
            case "no yellow":
                sqlPlus = "AND color != 'yellow' AND moveTypeA != 'extended'";
                sheetMask = "no_yellow.png";
                break;
            case "none":
                sqlPlus = "AND moveTypeA != 'extended'";
                sheetMask = "no_restrictions.png";
                break;
        }

        if (!(boolean) session.getAttribute("userShield")) {
            sqlPlus += " moveTypeA != 'shield block' AND moveTypeA != 'protected attacks'";
        }
        if ((boolean) session.getAttribute("userWeapon")) {
            sqlPlus += " AND PG != '46'";
        }

        session.setAttribute("sheetMask", sheetMask);
        return sqlPlus;
    }

    public String speModtype(String modtype) {
        String swmod = "";
        if (modtype != null) {
            switch (modtype) {
                case "any":
                    swmod = "";
                    break;
                case "blue":
                    swmod = " AND color='blue'";
                    break;
                case "down side swing":
                    swmod = " AND (moveTypeA='down swing' OR moveTypeB='down swing' OR moveTypeA='side swing' OR moveTypeB='side swing'";
                    break;
                case "oragne":
                    swmod = " AND color='orange'";
                    break;
            }

        }
        return swmod;
    }

    public void storeResults(HttpServletRequest request, ServletContext servletContext) {
        session = request.getSession();

        ConnectionPool connectionPool = (ConnectionPool) servletContext.getAttribute("connectionPool");
        try {
            Connection connection = connectionPool.getConnection();
            statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(gameInnards.class.getName()).log(Level.SEVERE, null, ex);
        }
        String user, charac;
        int gamesPlayed, gamesWon, gamesLost;
        user = (String) session.getAttribute("user");
        charac = (String) session.getAttribute("character");
        if (session.getAttribute("userWin") == "WIN") {
            gamesWon = 1;
            gamesLost = 0;
        } else {
            gamesWon = 0;
            gamesLost = 1;

        }

        // sql.append("insert into "+table+" values('"+name);
        String hsql = "select user, charac from LWusers where user='"+user+"' and charac='"+charac+"'";

        try {
            res = statement.executeQuery(hsql);
            if (!res.next()) {
                hsql = "insert into LWusers values('" + user + "', '" + charac + "', '1', '" + gamesWon + "', '" + gamesLost + "')";
            } else {
                hsql = "update LWusers set gamesWon = gamesWon + " + gamesWon + ", gamesLost = gamesLost + " + gamesLost + ", gamesPlayed = gamesPlayed +1 where user='" + user + "' and charac='" + charac+"'";
            }
            System.out.println(hsql);
            statement.executeUpdate(hsql);
//            if (statement != null) {
//                statement.close();
//            }

        } catch (SQLException ex) {
            Logger.getLogger(gameInnards.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
