package main;

import model.details.DadosPessoaisController;
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
import model.details.LoteController;
import model.details.ProdutoController;
import modelo.supermercado.Supermercado;
import modelo.usuarios.Funcionario;
import report.RelatorioClienteController;
import report.RelatorioMeioPagController;
import report.RelatorioProdutoController;
import filter.FiltroController;
import java.sql.SQLException;
import javafx.animation.FadeTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.util.Duration;
import org.controlsfx.control.BreadCrumbBar;
import search.BuscaFornecedorController;
import search.BuscaLoteController;
import search.BuscaPessoaFisicaController;
import search.BuscaProdutoController;

//TODO organizar os fxmls e trocar os paths deles na classe
public class FXMLController implements Initializable, MainScreenListener {

    private List<Parent> screenStack;
    private final Supermercado market;
    private final Funcionario funcionario;

    @FXML
    private HBox content;
    @FXML
    private BreadCrumbBar<String> navBar;
    @FXML
    private ScrollPane navBarConteiner;

    public FXMLController(Supermercado market, Funcionario funcionario) {
        this.market = market;
        this.funcionario = funcionario;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        screenStack = new ArrayList<>();

        navBarConteiner.setVisible(false);
        navBarConteiner.setManaged(false);
    }

    private void addScreen(FXMLLoader loader, String screenName) throws IOException {
        Parent root = loader.load();

        screenStack.add((Parent) content.getChildren().get(0));
        content.getChildren().clear();
        HBox.setHgrow(root, Priority.ALWAYS);
        addFade(root);
        content.getChildren().add(root);

        TreeItem<String> screenCrumb = new TreeItem<>(screenName);

        if (navBar.getSelectedCrumb() != null) {
            navBar.getSelectedCrumb().getChildren().add(screenCrumb);
        }

        navBarConteiner.setVisible(true);
        navBarConteiner.setManaged(true);

        navBar.setSelectedCrumb(screenCrumb);
    }

    @Override
    public void pullScreen() {
        content.getChildren().clear();
        Parent root = screenStack.get(screenStack.size() - 1);
        addFade(root);
        content.getChildren().add(root);
        screenStack.remove(root);

        TreeItem<String> lastCrumb = navBar.getSelectedCrumb();
        navBar.setSelectedCrumb(lastCrumb.getParent());

        if (navBar.getSelectedCrumb() == null) {
            navBarConteiner.setVisible(false);
            navBarConteiner.setManaged(false);
        } else {
            lastCrumb.getParent().getChildren().remove(lastCrumb);
        }

    }

    private void addFade(Parent root) {
        FadeTransition ft = new FadeTransition(Duration.seconds(1), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    @FXML
    private void openDadosPessoais(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/model/details/DadosPessoais.fxml"));

        DadosPessoaisController controller = new DadosPessoaisController(funcionario, funcionario, this, market);
        loader.setController(controller);
        addScreen(loader, "Dados Pessoais");
    }

    @FXML
    private void cadastrarFuncionario(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/model/details/DadosPessoais.fxml"));

        DadosPessoaisController controller = new DadosPessoaisController(null, funcionario, this, market);
        loader.setController(controller);
        addScreen(loader, "Cadastro de Funcionário");
    }

    @FXML
    private void cadastrarProduto(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/model/details/Produto.fxml"));
        ProdutoController pc = new ProdutoController(null, this, market);
        loader.setController(pc);

        addScreen(loader, "Cadastro de Produto");
    }

    @FXML
    private void cadastrarLote(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/model/details/Lote.fxml"));
        LoteController lc = new LoteController(null, this, market);
        loader.setController(lc);

        addScreen(loader, "Cadastro de Lote");
    }

    private FiltroController getNewFiltroController(String screenName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/filter/Filtro.fxml"));
        FiltroController fc = new FiltroController(this);
        loader.setController(fc);
        addScreen(loader, screenName);

        return fc;
    }

    @FXML
    private void buscarFornecedores(ActionEvent event) throws IOException {
        FiltroController fc = getNewFiltroController("Busca de Fornecedores");

        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/search/BuscaFornecedor.fxml"));
        BuscaFornecedorController bfc = new BuscaFornecedorController(fc, market);
        fc.setFilterComunication(bfc);

        subLoader.setController(bfc);
        Parent subView = subLoader.load();
        fc.setContent(subView);
    }

    @FXML
    private void buscarProdutos(ActionEvent event) throws IOException {
        FiltroController fc = getNewFiltroController("Busca de Produtos");

        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/search/BuscaProduto.fxml"));
        BuscaProdutoController bpc = new BuscaProdutoController(fc, market);
        fc.setFilterComunication(bpc);

        subLoader.setController(bpc);
        Parent subView = subLoader.load();
        fc.setContent(subView);
    }

    @FXML
    private void buscarLotes(ActionEvent event) throws IOException {
        FiltroController fc = getNewFiltroController("Busca de Lotes");

        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/search/BuscaLote.fxml"));
        BuscaLoteController blc = new BuscaLoteController(fc, market);
        fc.setFilterComunication(blc);

        subLoader.setController(blc);
        Parent subView = subLoader.load();
        fc.setContent(subView);
    }

    @FXML
    private void buscarClientes(ActionEvent event) throws IOException {
        buscaPessoasFisicas(BuscaPessoaFisicaController.PessoaFisicaClass.Cliente, "Busca de Clientes");
    }

    @FXML
    private void buscarFuncionarios(ActionEvent event) throws IOException {
        buscaPessoasFisicas(BuscaPessoaFisicaController.PessoaFisicaClass.Funcionario, "Busca de Funcionários");
    }

    private void buscaPessoasFisicas(BuscaPessoaFisicaController.PessoaFisicaClass pf, String screenName) throws IOException {
        FiltroController fc = getNewFiltroController(screenName);

        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/search/BuscaPessoaFisica.fxml"));
        BuscaPessoaFisicaController bpfc = new BuscaPessoaFisicaController(fc, pf, market, funcionario);
        fc.setFilterComunication(bpfc);

        subLoader.setController(bpfc);
        Parent subView = subLoader.load();
        fc.setContent(subView);
    }

    @FXML
    private void showClientesMaisConsumistas(ActionEvent event) throws IOException {
        showRelatorioCliente(RelatorioClienteController.TipoRelatorio.MAIS_CONSUMISTAS, "Clientes mais consumistas");
    }

    @FXML
    private void showMediaConsumoClientes(ActionEvent event) throws IOException {
        showRelatorioCliente(RelatorioClienteController.TipoRelatorio.MEDIA_CONSUMO, "Média de  Consumo/Cliente");
    }

    private void showRelatorioCliente(RelatorioClienteController.TipoRelatorio tr, String screenName) throws IOException {
        FiltroController fc = getNewFiltroController(screenName);

        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/report/RelatorioCliente.fxml"));
        RelatorioClienteController rcc = new RelatorioClienteController(fc, tr, market);
        fc.setFilterComunication(rcc);

        subLoader.setController(rcc);
        Parent subView = subLoader.load();
        fc.setContent(subView);
    }

    @FXML
    private void showProdMaisVend(ActionEvent event) throws IOException {
        FiltroController fc = getNewFiltroController("Produto mais vendidos");

        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/report/RelatorioProduto.fxml"));
        RelatorioProdutoController rpc = new RelatorioProdutoController(fc, market);
        fc.setFilterComunication(rpc);

        subLoader.setController(rpc);
        Parent subView = subLoader.load();
        fc.setContent(subView);
    }

    @FXML
    private void showPagMaisUsado(ActionEvent event) throws IOException {
        showRelatorioMeioPag(RelatorioMeioPagController.TipoRelatorio.MAIS_UTILIZADO, "Meio Pag. mais usados");
    }

    @FXML
    private void showPagMaisRentavel(ActionEvent event) throws IOException {
        showRelatorioMeioPag(RelatorioMeioPagController.TipoRelatorio.MAIS_RENTAVEL, "Meio Pag. mais rentáveis");
    }

    private void showRelatorioMeioPag(RelatorioMeioPagController.TipoRelatorio tr, String screeName) throws IOException {
        FiltroController fc = getNewFiltroController(screeName);

        FXMLLoader subLoader = new FXMLLoader(getClass().getResource("/fxml/report/RelatorioMeioPag.fxml"));
        RelatorioMeioPagController rmpc = new RelatorioMeioPagController(fc, tr, market);
        fc.setFilterComunication(rmpc);

        subLoader.setController(rmpc);
        Parent subView = subLoader.load();
        fc.setContent(subView);
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main/Login.fxml"));
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
