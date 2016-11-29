package Util;

import Model.Competition;
import Model.Team;
import com.sun.istack.internal.NotNull;
import http.requests.GetRequest;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
    public static JSONObject getRequestToJSONObject(String link) {
        GetRequest getRequest = new GetRequest(link);
        getRequest.addHeader("X-Auth-Token", "b95ca7f69f22429d9e82720ea977198e");
        getRequest.send();
        return JSONObject.parse(getRequest.getContent());
    }
    @NotNull
    public static Team getTeamByCompareTeamName(Competition competition, int index) {
        Team resultTeam = new Team();
        for (Team team : competition.teams) {
            if (team.name.equals(competition.standings.get(index).teamName))
                resultTeam = team;
        }
        return resultTeam;
    }
    public static BigDecimal getBigDecimal(String integerString) {
        if (!integerString.equals("")) {
            return new BigDecimal(integerString);
        }
        return new BigDecimal(0);
    }
    public static Date getDate(String dateString) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        if (!dateString.equals(""))
            try {
                return format.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        return null;
    }
    public static void onFrontOfPeasyCam(PApplet pApplet, PeasyCam peasyCam){
        pApplet.rotateX(peasyCam.getRotations()[0]);
        pApplet.rotateY(peasyCam.getRotations()[1]);
        pApplet.rotateZ(peasyCam.getRotations()[2]);
    }
}