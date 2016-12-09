package Util;

import Model.Competition;
import Model.Standing;
import Model.Team;
import com.sun.istack.internal.NotNull;
import http.requests.GetRequest;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        ArrayList<Standing> standings  = new ArrayList(competition.getStandings());
        for (Team team : competition.getTeams()) {
            if (team.getName().equals(standings.get(index).getTeamName()))
                resultTeam = team;
        }
        return resultTeam;
    }

    @NotNull
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

    public static String bigDecimalToString(BigDecimal bigDecimal) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return numberFormat.format(bigDecimal);
    }

    public static void onFrontOfPeasyCam(PApplet pApplet, PeasyCam peasyCam) {
        pApplet.rotateX(peasyCam.getRotations()[0]);
        pApplet.rotateY(peasyCam.getRotations()[1]);
        pApplet.rotateZ(peasyCam.getRotations()[2]);
    }
}