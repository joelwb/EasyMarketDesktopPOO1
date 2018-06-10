package main;

import model_info.DadosPessoaisController;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import modelo.usuarios.Endereco;
import modelo.usuarios.Funcionario;
import modelo.usuarios.PessoaFisica;
import search.BuscaController;
import search.BuscaFornecedorController;
import search.BuscaLoteController;
import search.BuscaPessoaFisicaController;
import search.BuscaProdutoController;

public class FXMLController implements Initializable, MainButtonClickListener {

    private List<Parent> screenStack;

    @FXML
    private HBox content;
    @FXML
    private Label bemVindo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        screenStack = new ArrayList<>();
    }

    @Override
    public void save() {
        content.getChildren().clear();
        Parent root = screenStack.get(screenStack.size() - 1);
        content.getChildren().add(root);
        screenStack.remove(root);
    }

    @Override
    public void cancel() {
        content.getChildren().clear();
        Parent root = screenStack.get(screenStack.size() - 1);
        content.getChildren().add(root);
        screenStack.remove(root);
    }

    private void addScreen(FXMLLoader loader) throws IOException {
        Parent root = loader.load();

        screenStack.add((Parent) content.getChildren().get(0));
        content.getChildren().clear();
        HBox.setHgrow(root, Priority.ALWAYS);
        content.getChildren().add(root);

    }

    @FXML
    private void openDadosPessoais(ActionEvent event) throws IOException {
        Endereco endereco = new Endereco("Manguinhos", "29871-475", "Serra", Endereco.Estado.ES, 52, "IFES");
        Funcionario funcionario;

        //esse erro é apenas um teste
        try {
            funcionario = new Funcionario("Gerente", "Vendas", "131.117.850-33", new Date(), PessoaFisica.Genero.M, "joel@gmail.com", "Tesha2", "Joel", endereco);
        } catch (UnsupportedEncodingException | IllegalArgumentException | NoSuchAlgorithmException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (ex instanceof IllegalArgumentException) {
                alert.setTitle("Erro!");
                alert.setContentText(ex.getMessage());
            } else {
                alert.setContentText("Erro Interno!");
                alert.setContentText("Entre em contato com o suporte!");
            }

            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DadosPessoais.fxml"));

        DadosPessoaisController controller = new DadosPessoaisController(funcionario, this);
        loader.setController(controller);
        addScreen(loader);
    }

    @FXML
    private void cadastrarFuncionario(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DadosPessoais.fxml"));

        DadosPessoaisController controller = new DadosPessoaisController(null, this);
        loader.setController(controller);
        addScreen(loader);
    }

    @FXML
    private void cadastrarProduto(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Produto.fxml"));
        addScreen(loader);
    }

    @FXML
    private void cadastrarLote(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Lote.fxml"));
        addScreen(loader);
    }

    @FXML
    private void buscarFornecedores(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Busca.fxml"));
        BuscaController bc = new BuscaController(this);
        loader.setController(bc);
        addScreen(loader);
        
        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/BuscaFornecedor.fxml"));
        BuscaFornecedorController bfc = new BuscaFornecedorController(bc);
        bc.setFilterComunication(bfc);
        
        subLoader.setController(bfc);
        Parent subView = subLoader.load();
        bc.setContent(subView);
    }

    @FXML
    private void buscarProdutos(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Busca.fxml"));
        BuscaController bc = new BuscaController(this);
        loader.setController(bc);
        addScreen(loader);
        
        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/BuscaProduto.fxml"));
        BuscaProdutoController bpc = new BuscaProdutoController(bc);
        bc.setFilterComunication(bpc);
        
        subLoader.setController(bpc);
        Parent subView = subLoader.load();
        bc.setContent(subView);
    }

    @FXML
    private void buscaLote(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Busca.fxml"));
        BuscaController bc = new BuscaController(this);
        loader.setController(bc);
        addScreen(loader);
        
        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/BuscaLote.fxml"));
        BuscaLoteController blc = new BuscaLoteController(bc);
        bc.setFilterComunication(blc);
        
        subLoader.setController(blc);
        Parent subView = subLoader.load();
        bc.setContent(subView);
    }

    @FXML
    private void buscaCliente(ActionEvent event) throws IOException {
        buscaPessoaFisica(BuscaPessoaFisicaController.PessoaFisicaClass.Cliente);
    }

    @FXML
    private void buscaFuncionario(ActionEvent event) throws IOException {
        buscaPessoaFisica(BuscaPessoaFisicaController.PessoaFisicaClass.Funcionario);
    }
    
    private void buscaPessoaFisica(BuscaPessoaFisicaController.PessoaFisicaClass pf) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Busca.fxml"));
        BuscaController bc = new BuscaController(this);
        loader.setController(bc);
        addScreen(loader);
        
        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/BuscaPessoaFisica.fxml"));
        BuscaPessoaFisicaController bpfc = new BuscaPessoaFisicaController(bc,pf);
        bc.setFilterComunication(bpfc);
        
        subLoader.setController(bpfc);
        Parent subView = subLoader.load();
        bc.setContent(subView);
    }
}
