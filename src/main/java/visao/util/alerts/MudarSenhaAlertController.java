/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.util.alerts;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class MudarSenhaAlertController implements Initializable {

    @FXML
    private PasswordField senhaAtual;
    @FXML
    private Label erroSenhaAtual;
    @FXML
    private PasswordField senhaNova;
    @FXML
    private Label erroSenhaNova;
    @FXML
    private PasswordField confirm;
    @FXML
    private Label erroConfirm;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        erroSenhaNova.setVisible(false);
        erroConfirm.setVisible(false);
        erroSenhaAtual.setVisible(false);
    }    

    public void setVisibilityErroSenhaAtual(boolean visible){
        erroSenhaAtual.setVisible(visible);
    }
    
    public String getSenhaAtual(){
        return senhaAtual.getText();
    }
    
    public String getSenhaNova(){
        return senhaNova.getText();
    }
    
    //Retorna true se a senha nova estiver igual nos dois campos e tiver 6 ou mais caracteres
    //ou seja se tanto o erroConfirm e erroSenhaNova não estiverem visiveis
    public boolean isSenhaNovaReady(){
        return !erroConfirm.isVisible() && !erroSenhaNova.isVisible();
    }
    
    private void exibeErros(){
        if (senhaNova.getText().length() < 6) erroSenhaNova.setVisible(true);
        else erroSenhaNova.setVisible(false);
        
        if (confirm.getText().equals(senhaNova.getText())) erroConfirm.setVisible(false);
        else erroConfirm.setVisible(true);
    }

    @FXML
    private void onSenhaNovaChange(KeyEvent event) {
        exibeErros();
    }

    @FXML
    private void onConfirmChange(KeyEvent event) {
        exibeErros();
    }
}
