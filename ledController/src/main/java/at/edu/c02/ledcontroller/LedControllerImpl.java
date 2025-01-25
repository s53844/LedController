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
}
