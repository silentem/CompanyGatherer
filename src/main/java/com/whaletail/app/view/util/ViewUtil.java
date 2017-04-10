package com.whaletail.app.view.util;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * @author Whaletail
 */
public class ViewUtil {
    public static Alert alert(Alert alert,
                              String title,
                              String header,
                              String content,
                              Scene scene,
                              ButtonType... buttons) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttons);
        alert.initOwner(scene.getWindow());
        return alert;
    }
}