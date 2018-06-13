/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Toolkit;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

/**
 *
 * @author joel-
 */
public abstract class AlertCreator {
    public static void criarAlert(Alert.AlertType type, String title, String header, String msg){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.initModality(Modality.APPLICATION_MODAL);
        Toolkit.getDefaultToolkit().beep();
        alert.showAndWait();
    }
}
