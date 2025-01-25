package at.edu.c02.ledcontroller;

import org.json.JSONObject;

import java.io.IOException;

public interface ApiService {
    JSONObject getLights() throws IOException;
    JSONObject getLight(int numberLED) throws IOException;
    void setLight(JSONObject requestBody) throws IOException;
}
