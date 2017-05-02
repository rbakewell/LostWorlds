<%-- 
    Document   : game
    Created on : Apr 20, 2017, 7:16:26 PM
    Author     : russ
--%>
<link rel = "stylesheet"
      type = "text/css"
      href = "StyleSheet.css" />
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ArrayTest.tld" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lost Worlds</title>
    </head>
    <body>
        <table border="1">
            <tr>
                <th>Name</th>
                <th>Character</th>
                <th>Hit Points</th>
                <th></th>
                <th>Opponent</th>
                <th>Opponent Hit Points</th>
                <c:if test="${game == 'done'}">
                <th></th>
                <th align="center"><h2>You</h2></th>
                <th align="center"><h2>Opponent</h2></th>
                </c:if>
            </tr>
            <tr>
                <td align="center">${user}</td>
                <td align="center">${userDisplayName}</td>
                <td align="center">${userHP}</td>
                <td></td>
                <td>${compDisplayName}</td>
                <td>${compHP}</td>
                <c:if test="${game == 'done'}">
                    <td></td>
                    <td align="center"><h2>${userWin}</h2></td>
                    <td align="center"><h2>${compWin}</h2></td>
                </c:if>   
            </tr>


        </table>
                <c:if test="${game == 'done'}">
            <form action="LWservlet" method="post">
            New Game?<input type="submit" name="replay" value="Yes!"/>
            </form></c:if>
        <div class="cen">
            
            <p><font size="5">You are: ${title}<c:if test="${not empty compscore}">  Opponent Scored: ${compscore} </c:if></font></p>
                <p>Opponent tells you: ${message}
                <c:if test="${not empty userscore}">
                    <font size="5">You Scored ${userscore}</font>
                </c:if></p>
        </div>

            <div class="stack">
                <img class="image" src="${sheet}" width="500" height="780" alt="Character Sheet" >
                <img class="image" src="SheetMask/${sheetMask}" width="500" height="780" alt="Sheet Mask" usemap="#Psheetmap">
            </div>
            <div class="nextto">
                
                <img src="${compCH}/${pic}"  height="770">
            </div>
            <map name="Psheetmap">
                <div title="DOWN SWING: Smash"><area shape="rect" coords="264,205,360,218" alt="move" <c:if test="${ fn:contains( userMoves, 24) }"> href="LWservlet?move=24"</c:if>></div>
                <div title="SIE SWING: Strong"><area shape="rect" coords="264,221,360,234" alt="move" <c:if test="${ fn:contains( userMoves, 28) }">href="LWservlet?move=28"</c:if>></div>
                <div title="SIDE SWING: High"><area shape="rect" coords="264,236,360,249" alt="move" <c:if test="${ fn:contains( userMoves, 10) }">href="LWservlet?move=10"</c:if>></div>
                <div title="SIDE SWING: Low"><area shape="rect" coords="264,252,360,265" alt="High" <c:if test="${ fn:contains( userMoves, 2) }">href="LWservlet?move=2"</c:if>></div>
                <div title="THRUST: High"><area shape="rect" coords="264,268,360,280" alt="High" <c:if test="${ fn:contains( userMoves, 32) }">href="LWservlet?move=32"</c:if>></div>
                <div title="THRUST: Low"><area shape="rect" coords="264,283,360,296" alt="High" <c:if test="${ fn:contains( userMoves, 14) }">href="LWservlet?move=14"</c:if>></div>
                <div title="FAKE: High"><area shape="rect" coords="264,300,360,311" alt="High" <c:if test="${ fn:contains( userMoves, 42) }">href="LWservlet?move=42"</c:if>></div>
                <div title="FAKE: Low"><area shape="rect" coords="264,315,360,328" alt="High" <c:if test="${ fn:contains( userMoves, 12) }">href="LWservlet?move=12"</c:if>></div>
                <div title="FAKE: Side Swing"><area shape="rect" coords="264,331,360,343" alt="High" <c:if test="${ fn:contains( userMoves, 22) }">href="LWservlet?move=22"</c:if>></div>
                <div title="FAKE: Thrust"><area shape="rect" coords="264,347,360,359" alt="High" <c:if test="${ fn:contains( userMoves, 38) }">href="LWservlet?move=38"</c:if>></div>
                <div title="PROTECTED ATTACKS: Down Swing"><area shape="rect" coords="264,363,360,374" alt="High" <c:if test="${ fn:contains( userMoves, 44) }">href="LWservlet?move=44"</c:if>></div>
                <div title="PROTECTED ATTACKS: Side Swing"><area shape="rect" coords="264,377,360,390" alt="High" <c:if test="${ fn:contains( userMoves, 48) }">href="LWservlet?move=48"</c:if>></div>
                <div title="PROTECTED ATTACKS: Thrust"><area shape="rect" coords="264,394,360,405" alt="High" <c:if test="${ fn:contains( userMoves, 6) }">href="LWservlet?move=6"</c:if>></div>
                <div title="SPECIAL: Kick"><area shape="rect" coords="264,408,360,421" alt="High" <c:if test="${ fn:contains( userMoves, 34) }">href="LWservlet?move=34"</c:if>></div>
                <div title="SPECIAL: WIld Swing"><area shape="rect" coords="264,424,360,436" alt="High" <c:if test="${ fn:contains( userMoves, 40) }">href="LWservlet?move=40"</c:if>></div>
                <div title="SPECIAL: Dislodge Weapon"><area shape="rect" coords="264,440,360,451" alt="High" <c:if test="${ fn:contains( userMoves, 30) }">href="LWservlet?move=30"</c:if>></div>
                <div title="SPECIAL: Retrieve Weapon"><area shape="rect" coords="264,455,360,467" alt="High" <c:if test="${ fn:contains( userMoves, 46) }">href="LWservlet?move=46"</c:if>></div>
                <div title="SHIELD BLOCK: High"><area shape="rect" coords="264,471,360,483" alt="High" <c:if test="${ fn:contains( userMoves, 26) }">href="LWservlet?move=26"</c:if>></div>
                <div title="SHIELD BLOCK: Low"><area shape="rect" coords="264,486,360,500" alt="High" <c:if test="${ fn:contains( userMoves, 4) }">href="LWservlet?move=4"</c:if>></div>
                <div title="JUMP: Up"><area shape="rect" coords="264,504,360,515" alt="High" <c:if test="${ fn:contains( userMoves, 18) }">href="LWservlet?move=18"</c:if>></div>
                <div title="JUMP: Dodge"><area shape="rect" coords="264,518,360,529" alt="High" <c:if test="${ fn:contains( userMoves, 8) }">href="LWservlet?move=8"</c:if>></div>
                <div title="JUMP: Duck"><area shape="rect" coords="264,532,360,546" alt="High" <c:if test="${ fn:contains( userMoves, 20) }">href="LWservlet?move=20"</c:if>></div>
                <div title="JUMP: Away"><area shape="rect" coords="264,549,360,562" alt="High" <c:if test="${ fn:contains( userMoves, 16) }">href="LWservlet?move=16"</c:if>></div>

                <div title="EXTENDED RANGE: Charge"><area shape="rect" coords="264,620,360,633" alt="High" <c:if test="${ fn:contains( userMoves, 50) }">href="LWservlet?move=50"</c:if>></div>
                <div title="EXTENDED RANGE: Swing High"><area shape="rect" coords="264,636,360,648" alt="High" <c:if test="${ fn:contains( userMoves, 64) }">href="LWservlet?move=64"</c:if>></div>
                <div title="EXTENDED RANGE: Swing Low"><area shape="rect" coords="264,652,360,665" alt="High" <c:if test="${ fn:contains( userMoves, 58) }">href="LWservlet?move=58"</c:if>></div>
                <div title="EXTENDED RANGE: Thrust High"><area shape="rect" coords="264,668,360,681" alt="High" <c:if test="${ fn:contains( userMoves, 54) }">href="LWservlet?move=54"</c:if>></div>
                <div title="EXTENDED RANGE: Thrust Low"><area shape="rect" coords="264,684,360,696" alt="High" <c:if test="${ fn:contains( userMoves, 60) }">href="LWservlet?move=60"</c:if>></div>
                <div title="EXTENDED RANGE: Block & Close"><area shape="rect" coords="264,699,360,713" alt="High" <c:if test="${ fn:contains( userMoves, 56) }">href="LWservlet?move=56"</c:if>></div>
                <div title="EXTENDED RANGE: Dodge"><area shape="rect" coords="264,715,360,727" alt="High" <c:if test="${ fn:contains( userMoves, 52) }">href="LWservlet?move=52"</c:if>></div>
                <div title="EXTENDED RANGE: Jump Back"><area shape="rect" coords="264,730,360,742" alt="High" <c:if test="${ fn:contains( userMoves, 62) }">href="LWservlet?move=62"</c:if>></div>
            </map>
            
        
    </body>
</html>
