/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import database.supermercado.SupermercadoDAO;
import database.usuarios.FuncionarioDAO;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.security.auth.login.LoginException;
import modelo.supermercado.Supermercado;
import modelo.usuarios.Funcionario;
import util.AlertCreator;
import util.Screen;

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
        Funcionario funcionario = null;
        Supermercado supermercado = null;

        try {
            funcionario = FuncionarioDAO.SignIn(email.getText(), senha.getText());
            supermercado = SupermercadoDAO.readSupermercadoByFuncionario(funcionario);
        } catch (SQLException | ClassNotFoundException | LoginException | UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            if (ex instanceof LoginException) {
                AlertCreator.criarAlert(Alert.AlertType.WARNING, "Falha ao logar", "Não foi possivel logar", ex.getMessage());
            } else {
                AlertCreator.criarAlert(Alert.AlertType.ERROR, "Erro!", "Erro Interno!", "Procure o suporte para resolver seu problema");
            }

            return;
        }

        FXMLController controller = new FXMLController(supermercado, funcionario);
        loader.setController(controller);

        Screen.openNew(loader);

        Stage loginStage = (Stage) email.getScene().getWindow();
        loginStage.close();
    }

}
