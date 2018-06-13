/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.supermercado.Supermercado;
import modelo.usuarios.Endereco;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class LoginController implements Initializable {

    @FXML
    private TextField email;
    @FXML
    private PasswordField senha;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void login(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        
        //TODO Usar funcões dos DAOs paga logar e pegar o supermercado onde o funcionario trabalha
        Endereco endereco = new Endereco("SANTA LUCIA", "29056-925", "Vitória", Endereco.Estado.ES, 565, "RUA DAS PALMEIRAS");
        Supermercado supermercado = new Supermercado(1, -18.5382, -54.4525, "vitória 03", "35.868.768/0001-66", "CARREFOUR", endereco);
        
        FXMLController controller = new FXMLController(supermercado, null);
        loader.setController(controller);
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        Stage stage = new Stage();
        stage.setTitle("EasyMarket Desktop");
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
        
        Stage loginStage = (Stage) email.getScene().getWindow();
        loginStage.close();
    }
    
}
