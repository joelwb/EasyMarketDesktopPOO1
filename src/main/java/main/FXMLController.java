package main;

import model_info.DadosPessoaisController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model_info.LoteController;
import model_info.ProdutoController;
import modelo.supermercado.Supermercado;
import modelo.usuarios.Funcionario;
import report.RelatorioClienteController;
import report.RelatorioMeioPagController;
import report.RelatorioProdutoController;
import filter.FiltroController;
import search.BuscaFornecedorController;
import search.BuscaLoteController;
import search.BuscaPessoaFisicaController;
import search.BuscaProdutoController;

public class FXMLController implements Initializable, MainButtonClickListener {

    private List<Parent> screenStack;
    private final Supermercado market;
    private final Funcionario funcionario;

    @FXML
    private HBox content;

    public FXMLController(Supermercado market, Funcionario funcionario) {
        this.market = market;
        this.funcionario = funcionario;
    }

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
        ProdutoController pc = new ProdutoController(null, this);
        loader.setController(pc);
        
        addScreen(loader);
    }

    @FXML
    private void cadastrarLote(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Lote.fxml"));
        LoteController lc = new LoteController(null, this);
        loader.setController(lc);
        
        addScreen(loader);
    }
    
    private FiltroController getNewBuscaController() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Filtro.fxml"));
        FiltroController bc = new FiltroController(this);
        loader.setController(bc);
        addScreen(loader);
        
        return bc;
    }

    @FXML
    private void buscarFornecedores(ActionEvent event) throws IOException {
        FiltroController bc = getNewBuscaController();
        
        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/BuscaFornecedor.fxml"));
        BuscaFornecedorController bfc = new BuscaFornecedorController(bc, market);
        bc.setFilterComunication(bfc);
        
        subLoader.setController(bfc);
        Parent subView = subLoader.load();
        bc.setContent(subView);
    }

    @FXML
    private void buscarProdutos(ActionEvent event) throws IOException {
        FiltroController bc = getNewBuscaController();
        
        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/BuscaProduto.fxml"));
        BuscaProdutoController bpc = new BuscaProdutoController(bc, market);
        bc.setFilterComunication(bpc);
        
        subLoader.setController(bpc);
        Parent subView = subLoader.load();
        bc.setContent(subView);
    }

    @FXML
    private void buscarLotes(ActionEvent event) throws IOException {
        FiltroController bc = getNewBuscaController();
        
        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/BuscaLote.fxml"));
        BuscaLoteController blc = new BuscaLoteController(bc, market);
        bc.setFilterComunication(blc);
        
        subLoader.setController(blc);
        Parent subView = subLoader.load();
        bc.setContent(subView);
    }

    @FXML
    private void buscarClientes(ActionEvent event) throws IOException {
        buscaPessoasFisicas(BuscaPessoaFisicaController.PessoaFisicaClass.Cliente);
    }

    @FXML
    private void buscarFuncionarios(ActionEvent event) throws IOException {
        buscaPessoasFisicas(BuscaPessoaFisicaController.PessoaFisicaClass.Funcionario);
    }
    
    private void buscaPessoasFisicas(BuscaPessoaFisicaController.PessoaFisicaClass pf) throws IOException{
        FiltroController bc = getNewBuscaController();
        
        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/BuscaPessoaFisica.fxml"));
        BuscaPessoaFisicaController bpfc = new BuscaPessoaFisicaController(bc,pf, market);
        bc.setFilterComunication(bpfc);
        
        subLoader.setController(bpfc);
        Parent subView = subLoader.load();
        bc.setContent(subView);
    }

    @FXML
    private void showClientesMaisConsumistas(ActionEvent event) throws IOException {
        showRelatorioCliente(RelatorioClienteController.TipoRelatorio.MAIS_CONSUMISTAS);
    }

    @FXML
    private void showMediaConsumoClientes(ActionEvent event) throws IOException {
        showRelatorioCliente(RelatorioClienteController.TipoRelatorio.MEDIA_CONSUMO);
    }
    
    private void showRelatorioCliente(RelatorioClienteController.TipoRelatorio tr) throws IOException{
        FiltroController bc = getNewBuscaController();
        
        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/RelatorioCliente.fxml"));
        RelatorioClienteController rcc = new RelatorioClienteController(bc,tr, market);
        bc.setFilterComunication(rcc);
        
        subLoader.setController(rcc);
        Parent subView = subLoader.load();
        bc.setContent(subView);
    }

    @FXML
    private void showProdMaisVend(ActionEvent event) throws IOException {
        FiltroController bc = getNewBuscaController();
        
        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/RelatorioProduto.fxml"));
        RelatorioProdutoController rpc = new RelatorioProdutoController(bc, market);
        bc.setFilterComunication(rpc);
        
        subLoader.setController(rpc);
        Parent subView = subLoader.load();
        bc.setContent(subView);
    }

    @FXML
    private void showPagMaisUsado(ActionEvent event) throws IOException {
        showRelatorioMeioPag(RelatorioMeioPagController.TipoRelatorio.MAIS_UTILIZADO);
    }

    @FXML
    private void showPagMaisRentavel(ActionEvent event) throws IOException {
        showRelatorioMeioPag(RelatorioMeioPagController.TipoRelatorio.MAIS_RENTAVEL);
    }
    
    private void showRelatorioMeioPag(RelatorioMeioPagController.TipoRelatorio tr) throws IOException{
        FiltroController bc = getNewBuscaController();
        
        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/RelatorioMeioPag.fxml"));
        RelatorioMeioPagController rmpc = new RelatorioMeioPagController(bc,tr, market);
        bc.setFilterComunication(rmpc);
        
        subLoader.setController(rmpc);
        Parent subView = subLoader.load();
        bc.setContent(subView);
    }
    
    @FXML
    private void logout(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("EasyMarket Desktop");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        
        Stage thisStage = (Stage) content.getScene().getWindow();
        thisStage.close();
    }
}
