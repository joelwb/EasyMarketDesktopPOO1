/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.util.alerts;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import modelo.usuarios.Contato;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class AddContatoAlertController implements Initializable {
    @FXML
    private TextField contato;
    @FXML
    private ComboBox<Contato.Tipo> tipo;
    @FXML
    private Label erro;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (Contato.Tipo t : Contato.Tipo.values()) {
            tipo.getItems().add(t);
        }

        tipo.setValue(Contato.Tipo.CELULAR);
        tipo.valueProperty().addListener(new ChangeListener<Contato.Tipo>() {
            @Override
            public void changed(ObservableValue<? extends Contato.Tipo> observable, Contato.Tipo oldValue, Contato.Tipo newValue) {
                if (newValue != Contato.Tipo.EMAIL) {
                    erro.setVisible(false);
                }
            }
        });

        erro.setVisible(false);
    }

    public String getContato() {
        return contato.getText();
    }

    public Contato.Tipo getTipo() {
        return tipo.getValue();
    }
    
    @FXML
    private void contatoChange(KeyEvent event) {
        if (tipo.getValue() == Contato.Tipo.EMAIL && !Util.isLoginValido(contato.getText()) ) {
            erro.setVisible(true);
        } else {
            erro.setVisible(false);
        }
    }
    
    public void setContato(String contato){
        this.contato.setText(contato);
    }
    
    public void setTipo(Contato.Tipo tipo){
        this.tipo.setValue(tipo);
    }

}
