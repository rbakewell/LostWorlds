/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Make_db;

import Db_con.MyConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author russ
 */
public class Make_skel_tables {

    public static void main(String args[]) {

        boolean remote = true;
        String fullFilePath = "";
        ArrayList fcon = new ArrayList();
        
        String[] up = {"UserName", "Password"};

        Connection con = MyConnection.getConnection(remote, up);
        String createBookMoves, createBookResults, createCharacterSheet, createLWusers;
        
        createBookMoves = "create table bookMoves (moveParchment varchar(10), page int, opponentMove int, finalPage int)";
        createBookResults = "create table bookResults (picParchment varchar(15), page int, title varchar(35), message varchar(90), score int, modscore int, modtype varchar(20), restrictions varchar(25))";
        createCharacterSheet = "create table CharacterSheet (charac varchar(10), height int, bodyPts int, moveTypeA varchar(20), moveTypeB varchar(20), color varchar(10), PG int, EXT int, modscore int)";
        createLWusers = "create table LWusers (user text, charac text, gamesPlayed int, gamesWon int, gamesLost int)";
        
        //String[] files = {"ManMoves.txt", "SkelMoves.txt", "ManPic.txt", "SkelPic.txt", "ManSheet.txt", "skelSheet.txt"};
        String[] files = {"ManPic.txt", "SkelPic.txt", "ManSheet.txt", "skelSheet.txt"};

        Statement stmt;

        if (con == null) {
            System.out.println("Unable to create connection");
            return;
        }

        try {
            stmt = con.createStatement();
            //stmt.executeUpdate(createBookMoves);
            stmt.executeUpdate(createBookResults);
            //stmt.executeUpdate(createCharacterSheet);
            //stmt.executeUpdate(createLWusers);

            
            String current = new java.io.File( "." ).getCanonicalPath();
            for (String file : files) {

                String pathSeparator = System.getProperty("file.separator");
                
                System.out.println(current);
                fullFilePath = "web/WEB-INF/" + pathSeparator + file;
                
                String name = "";
                String table = "";
                Scanner linReader = new Scanner(new File(fullFilePath));
                switch (file){
                    case "SkelMoves.txt":
                    case "ManMoves.txt":
                        
                        if(file == "SkelMoves.txt"){
                            name = "skel', '";
                        } else {
                            name = "man', '";
                        }
                        table = "bookMoves";
                        System.out.println("Table "+table);
                        break;
                    case "ManPic.txt":
                    case "SkelPic.txt":
                        table = "bookResults";
                          System.out.println("Table "+table);
                        break;
                    case "ManSheet.txt":
                    case "skelSheet.txt":
                        table = "CharacterSheet";
                        System.out.println("Table "+table);
                        break;
                }

                while (linReader.hasNext()) {
                    String line = linReader.nextLine();
                    String[] linebits = line.split(", ");
                    StringBuilder sql = new StringBuilder();
                    sql.append("insert into "+table+" values('"+name);
                            
                    for(String bit : linebits){
                        sql.append(bit);
                        sql.append("', '");
                        //System.out.println(sql);
                        if(bit.equals("null")){
                            //System.out.println("in loop");
                            int despo = sql.length() -9;
                            sql.deleteCharAt(despo);
                            despo = sql.length() -4;
                            sql.deleteCharAt(despo);
                        }
                        //System.out.println(sql);
                    }
                    sql.setLength(sql.length() -3);
                    sql.append(")");
                    
                    System.out.println(sql);
                    stmt.executeUpdate(sql.toString());
                    //System.out.println(sql);
                }
                linReader.close();

            }

            stmt.close();
            con.close();

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Make_skel_tables.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Make_skel_tables.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
