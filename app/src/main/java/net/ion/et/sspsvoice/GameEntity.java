package net.ion.et.sspsvoice;

import org.json.JSONObject;

public class GameEntity
{
    public int subGameId;
    public String gameName;
    public String subGameName;
    public String startTime;

    public int getSubGameId() {
        return subGameId;
    }

    public void setSubGameId(int subGameId) {
        this.subGameId = subGameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getSubGameName() {
        return subGameName;
    }

    public void setSubGameName(String subGameName) {
        this.subGameName = subGameName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void parse(JSONObject object) throws Exception
    {
        this.gameName = (String)object.get("gameName");
        this.subGameName = "test";
        this.subGameId = 11;
        this.startTime = "2012-02-01 12:30:00";
    }
}
