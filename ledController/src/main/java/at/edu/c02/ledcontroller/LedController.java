package at.edu.c02.ledcontroller;

import java.io.IOException;

public interface LedController {
    void demo() throws IOException;
    void getGroupLeds(String groupName) throws IOException;
    void getSingleLed(int id) throws IOException;;
    void turnOffAllLeds() throws IOException;

    void spinningLed(String color, int turns) throws IOException;

    void setLed(int id, String color) throws IOException;


}
