package at.edu.c02.ledcontroller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class handles the actual logic
 */
public class LedControllerImpl implements LedController {
    private final ApiService apiService;

    public LedControllerImpl(ApiService apiService)
    {
        this.apiService = apiService;
    }

    @Override
    public void demo() throws IOException
    {
        // Call `getLights`, the response is a json object in the form `{ "lights": [ { ... }, { ... } ] }`
        JSONObject response = apiService.getLights();
        // get the "lights" array from the response
        JSONArray lights = response.getJSONArray("lights");
        // read the first json object of the lights array
        JSONObject firstLight = lights.getJSONObject(0);
        // read int and string properties of the light
        System.out.println("First light id is: " + firstLight.getInt("id"));
        System.out.println("First light color is: " + firstLight.getString("color"));
    }

    @Override
    public void getGroupLeds(String groupName) throws IOException {
        JSONObject response = apiService.getLights();
        JSONArray lights = response.getJSONArray("lights");
        ArrayList<JSONObject> groupLights = new ArrayList<>();

        lights.forEach(lightObj -> {
            if (lightObj instanceof JSONObject light) {
                JSONObject groupInfo = light.getJSONObject("groupByGroup");
                String lightGroupName = groupInfo.getString("name");
                if (lightGroupName.equals(groupName)) {
                    groupLights.add(light);
                }
            }
        });

        System.out.println("Lights in group '" + groupName + "':");
        groupLights.forEach(light -> System.out.println(light.toString()));
    }

    @Override
    public void getSingleLed(int id) throws IOException {
        JSONObject response = apiService.getLight(id);
        System.out.println(response.toString());
    }

    @Override
    public void turnOnAllLeds() throws IOException {
        int[] ledIds = {2, 10, 11, 12, 13, 14, 15, 16};
        for (int ledId : ledIds) {

            JSONObject requestBody = new JSONObject();
            requestBody.put("id", ledId);
            requestBody.put("color", "#009000");
            requestBody.put("state", true); // LED einschalten

            apiService.setLight(requestBody);
        }

        System.out.println("Alle LEDs wurden eingeschalten.");
    }

    @Override
    public void turnOffAllLeds() throws IOException {
        int[] ledIds = {2, 10, 11, 12, 13, 14, 15, 16};
        for (int ledId : ledIds) {

            JSONObject requestBody = new JSONObject();
            requestBody.put("id", ledId);
            requestBody.put("color", "#000000");
            requestBody.put("state", false); // LED ausschalten

            apiService.setLight(requestBody);
        }

        System.out.println("Alle LEDs wurden ausgeschaltet.");
    }

    @Override
    public void spinningLed(String color, int turns) throws IOException {
        int[] ledIds = {2, 10, 11, 12, 13, 14, 15, 16};

        turnOffAllLeds();
        for (int t = 0; t < turns; t++) {
            for (int i = 0; i < ledIds.length; i++) {
                JSONObject requestBodyOn = new JSONObject();
                requestBodyOn.put("id", ledIds[i]);
                requestBodyOn.put("color", color);
                requestBodyOn.put("state", true);
                apiService.setLight(requestBodyOn);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // LED ausschalten
                JSONObject requestBodyOff = new JSONObject();
                requestBodyOff.put("id", ledIds[i]);
                requestBodyOff.put("color", "#000000");
                requestBodyOff.put("state", false);
                apiService.setLight(requestBodyOff);
            }
        }

        turnOffAllLeds();
        System.out.println("Lauflicht abgeschlossen.");
    }

    @Override
    public void setLed(int id, String color) throws IOException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", id);
        requestBody.put("color", color);
        requestBody.put("state", true); // Automatisch einschalten

        apiService.setLight(requestBody);

        System.out.println("LED " + id + " wurde auf Farbe " + color + " gesetzt und eingeschaltet.");
    }

    @Override
    public void spinningWheel(int steps) throws IOException {
        int[] ledIds = {2, 10, 11, 12, 13, 14, 15, 16};

        JSONObject response = apiService.getLights();
        JSONArray lights = response.getJSONArray("lights");

        JSONArray sortedLights = new JSONArray();
        for (int id : ledIds) {
            for (int i = 0; i < lights.length(); i++) {
                JSONObject light = lights.getJSONObject(i);
                if (light.getInt("id") == id) {
                    sortedLights.put(light);
                    break;
                }
            }
        }

        for (int step = 0; step < steps; step++) {
            String[] colors = new String[ledIds.length];
            boolean[] states = new boolean[ledIds.length];

            for (int i = 0; i < sortedLights.length(); i++) {
                JSONObject light = sortedLights.getJSONObject(i);
                colors[i] = light.getString("color");
                states[i] = light.getBoolean("on");
            }

            String[] shiftedColors = new String[ledIds.length];
            boolean[] shiftedStates = new boolean[ledIds.length];

            for (int i = 0; i < colors.length; i++) {
                int newIndex = (i + 1) % colors.length;
                shiftedColors[newIndex] = colors[i];
                shiftedStates[newIndex] = states[i];
            }

            for (int i = 0; i < ledIds.length; i++) {
                JSONObject requestBody = new JSONObject();
                requestBody.put("id", ledIds[i]);
                requestBody.put("color", shiftedStates[i] ? shiftedColors[i] : "#000000");
                requestBody.put("state", shiftedStates[i]);
                apiService.setLight(requestBody);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        turnOffAllLeds();
        System.out.println("Spinning-Wheel-Effekt abgeschlossen.");
    }



}
