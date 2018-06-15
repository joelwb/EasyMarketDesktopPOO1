/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.MainApp;

/**
 *
 * @author joel-
 */
public abstract class Screen {
    public static Stage openNew(FXMLLoader loader) throws IOException{
        Scene scene = new Scene((Parent)loader.load());
        
        Stage stage = new Stage();
        stage.setTitle("EasyMarket Desktop");
        stage.setScene(scene);
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/imagens/icone.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
        
        return stage;
    } 
}
