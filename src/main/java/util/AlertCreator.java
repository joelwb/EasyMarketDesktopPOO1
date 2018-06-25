/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Toolkit;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;

/**
 *
 * @author joel-
 */
public abstract class AlertCreator {

    public static void criarAlert(Alert.AlertType type, String title, String header, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.initModality(Modality.APPLICATION_MODAL);
        Toolkit.getDefaultToolkit().beep();
        alert.showAndWait();
    }

    public static void exibeExececao(Exception ex) {
        if (ex instanceof IllegalArgumentException) {
            AlertCreator.criarAlert(Alert.AlertType.ERROR, "Erro!", ex.getMessage(), null);
        } else {
            AlertCreator.criarAlert(Alert.AlertType.ERROR, "Erro!", "Erro Interno!", "Procure o suporte para resolver seu problema");
        }
    }

    public static Integer getInt(String title, String header, String msg) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(msg);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                return Integer.parseInt(result.get());
            } catch (NumberFormatException ex) {
                AlertCreator.criarAlert(Alert.AlertType.ERROR, "Valor Inválido", "Valor deve ser numerico", null);
                return null;
            }
        }

        return null;
    }
}
