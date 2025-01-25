package at.edu.c02.ledcontroller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


public class LedControllerTest {
    /**
     * This test is just here to check if tests get executed. Feel free to delete it when adding your own tests.
     * Take a look at the stack calculator tests again if you are unsure where to start.
     */
    @Test
    public void dummyTest() {
        assertEquals(1, 1);
    }

    @Test
    public void testGetGroupLeds() throws IOException {
        JSONObject light1 = new JSONObject()
                .put("id", 1)
                .put("color", "#fff")
                .put("on", true)
                .put("groupByGroup", new JSONObject().put("name", "A"));
        JSONObject light2 = new JSONObject()
                .put("id", 2)
                .put("color", "#000")
                .put("on", false)
                .put("groupByGroup", new JSONObject().put("name", "B"));
        JSONObject light3 = new JSONObject()
                .put("id", 3)
                .put("color", "#ff0")
                .put("on", true)
                .put("groupByGroup", new JSONObject().put("name", "A"));

        JSONArray lightsArray = new JSONArray().put(light1).put(light2).put(light3);
        JSONObject apiResponse = new JSONObject().put("lights", lightsArray);

        ApiServiceImpl apiServiceMock = mock(ApiServiceImpl.class);
        when(apiServiceMock.getLights()).thenReturn(apiResponse);

        LedController ledController = new LedControllerImpl(apiServiceMock);
        ledController.getGroupLeds("A");

        verify(apiServiceMock, times(1)).getLights();
    }

    @Test
    public void testTurnOffAllLeds() throws IOException {
        ApiService apiServiceMock = mock(ApiService.class);

        LedControllerImpl ledController = new LedControllerImpl(apiServiceMock);

        ledController.turnOffAllLeds();

        verify(apiServiceMock, times(8)).setLight(any(JSONObject.class));
    }

    @Test
    public void testSpinningLed() throws IOException {
        ApiService apiServiceMock = mock(ApiService.class);

        LedControllerImpl ledController = new LedControllerImpl(apiServiceMock);

        ledController.spinningLed("#ff0000", 2);

        verify(apiServiceMock, atLeast(16)).setLight(any(JSONObject.class));
    }

    @Test
    public void endToEndSetLed() throws IOException {
        ApiServiceImpl apiService = new ApiServiceImpl();
        LedControllerImpl ledController = new LedControllerImpl(apiService);

        ledController.setLed(13, "#ff0000");

        JSONObject light = apiService.getLight(13).getJSONArray("lights").getJSONObject(0);

        assertEquals("#ff0000", light.getString("color"));
    }
}
