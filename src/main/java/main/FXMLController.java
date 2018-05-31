package main;

import information.DadosPessoaisController;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import modelo.pessoa.Endereco;
import modelo.pessoa.PessoaFisica;
import modelo.supermercado.Funcionario;

public class FXMLController implements Initializable, MainButtonClickListener {

    @FXML
    private HBox content;
    @FXML
    private Label bemVindo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @Override
    public void save() {
        content.getChildren().clear();
        content.getChildren().add(bemVindo);
    }

    @Override
    public void cancel() {
        content.getChildren().clear();
        content.getChildren().add(bemVindo);
    }
    
    @FXML
    private void openDadosPessoais(ActionEvent event) throws IOException {
        Endereco endereco = new Endereco("Manguinhos", "29871-475", "Serra", Endereco.Estado.ES, 52, "IFES");
        Funcionario funcionario;
        
        
        //esse erro é apenas um teste
        try {
            funcionario = new Funcionario("Gerente", "Vendas", "131.117.850-33", new Date(), PessoaFisica.Genero.M, "joel@gmail.com", "Tesha2", "Joel", endereco);
        } catch (UnsupportedEncodingException | IllegalArgumentException | NoSuchAlgorithmException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (ex instanceof IllegalArgumentException){
                alert.setTitle("Erro!");
                alert.setContentText(ex.getMessage());
            }
            else {
                alert.setContentText("Erro Interno!");
                alert.setContentText("Entre em contato com o suporte!");
            }
            
            alert.showAndWait();
            return;
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DadosPessoais.fxml"));
        
        DadosPessoaisController controller = new DadosPessoaisController(funcionario,this);
        loader.setController(controller);
        Parent root = loader.load();
        
        content.getChildren().clear();
        HBox.setHgrow(root, Priority.ALWAYS);
        content.getChildren().add(root);
    }

    @FXML
    private void cadastrarFuncionario(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DadosPessoais.fxml"));
        
        DadosPessoaisController controller = new DadosPessoaisController(null,this);
        loader.setController(controller);
        Parent root = loader.load();
        
        content.getChildren().clear();
        HBox.setHgrow(root, Priority.ALWAYS);
        content.getChildren().add(root);
    }

    @FXML
    private void cadastrarProduto(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Produto.fxml"));
        
        Parent root = loader.load();
        
        content.getChildren().clear();
        HBox.setHgrow(root, Priority.ALWAYS);
        content.getChildren().add(root);
    }
}
