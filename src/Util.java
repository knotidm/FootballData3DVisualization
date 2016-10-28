import http.requests.GetRequest;
import processing.data.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class Util {
    static JSONObject getRequestToJSONObject(String link) {
        GetRequest getRequest = new GetRequest(link);
        getRequest.addHeader("X-Auth-Token", "b95ca7f69f22429d9e82720ea977198e");
        getRequest.send();
        return JSONObject.parse(getRequest.getContent());
    }

    static Team getTeamByCompareStandingTeamName(Competition competition, int index) {
        for (Team team : competition.teams) {
            if (team.name.equals(competition.standings.get(index).teamName))
                return team;
        }
        return null;
    }

    static Integer getInteger(String integerString) {
        if (!integerString.equals("")) {
            return Integer.parseInt(integerString);
        }
        return 0;
    }

    static Date getDate(String dateString) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        if (!dateString.equals(""))
            try {
                return format.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        return null;
    }
}