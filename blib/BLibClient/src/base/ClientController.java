
package base;
import java.io.*;

import base.*;
import entities.Message;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class controls the client side
 *
 * @deprecated Will be removed soon due to being completely pointless
 */
public class ClientController {
    public Message sendToServer(Message msg) {
        return ClientApplication.getInstance().sendMessageToServer(msg);
    }
}