/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.main;

import database.supermercado.SupermercadoDAO;
import database.usuarios.FuncionarioDAO;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.security.auth.login.LoginException;
import modelo.supermercado.Supermercado;
import modelo.usuarios.Funcionario;
import visao.util.alerts.AlertCreator;
import visao.util.Screen;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class LoginController implements Initializable, EventHandler<KeyEvent> {

    @FXML
    private TextField email;
    @FXML
    private PasswordField senha;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        email.setOnKeyPressed(this);
        senha.setOnKeyPressed(this);
    }

    @FXML
    private void login(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main/Scene.fxml"));
        Funcionario funcionario;
        Supermercado supermercado;

        try {
            funcionario = FuncionarioDAO.SignIn(email.getText(), senha.getText());
            supermercado = SupermercadoDAO.readSupermercadoByFuncionario(funcionario);
        } catch (IllegalArgumentException | SQLException | ClassNotFoundException | LoginException | UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            if (ex instanceof LoginException || ex instanceof IllegalArgumentException) {
                AlertCreator.criarAlert(Alert.AlertType.WARNING, "Falha ao logar", "Não foi possivel logar", ex.getMessage());
            } else {
                AlertCreator.criarAlert(Alert.AlertType.ERROR, "Erro!", "Erro Interno!", "Procure o suporte para resolver seu problema");
            }

            return;
        }

        FXMLController controller = new FXMLController(supermercado, funcionario);
        loader.setController(controller);

        try {
            Screen.openNew(loader);
        } catch (IOException ex) {
            AlertCreator.exibeExececao(ex);
        }

        Stage loginStage = (Stage) email.getScene().getWindow();
        loginStage.close();
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) login(null);
    }

}
