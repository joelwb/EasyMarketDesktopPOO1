package model.details;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import alerts.AddContatoAlertController;
import alerts.MudarSenhaAlertController;
import database.usuarios.FuncionarioDAO;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import modelo.supermercado.Supermercado;
import modelo.usuarios.Cliente;
import modelo.usuarios.Contato;
import modelo.usuarios.Endereco;
import modelo.usuarios.Endereco.Estado;
import modelo.usuarios.Funcionario;
import modelo.usuarios.PessoaFisica;
import org.controlsfx.control.textfield.CustomPasswordField;
import util.AlertCreator;
import static util.DateObjConversor.toDate;
import static util.DateObjConversor.toLocalDate;
import util.TableViewConfigurator;
import util.Util;
import main.MainScreenListener;

/**
 * FXML Controller class
 *
 * @author joel-
 */
//TODO testar com ano de nascimenot menor que 1970;
public class DadosPessoaisController implements Initializable {

    private Supermercado supermercado;
    private Funcionario funcionario;
    private Cliente cliente;
    private MainScreenListener listener;
    private boolean isPerfil;

    @FXML
    private TextField email;
    @FXML
    private CustomPasswordField senha;
    @FXML
    private TextField nome;
    @FXML
    private TextField cpf;
    @FXML
    private RadioButton masculino;
    @FXML
    private ToggleGroup generoGroup;
    @FXML
    private RadioButton feminino;
    @FXML
    private DatePicker dataNasc;
    @FXML
    private TextField setor;
    @FXML
    private TextField cargo;
    @FXML
    private TextField cep;
    @FXML
    private TextField rua;
    @FXML
    private Spinner<Integer> numero;
    @FXML
    private TextField bairro;
    @FXML
    private TextField cidade;
    @FXML
    private ChoiceBox<Estado> estado;
    @FXML
    private Button addContato;
    @FXML
    private Button editContato;
    @FXML
    private Button removeContato;
    @FXML
    private TableView<List<String>> contatosTable;
    @FXML
    private Button cancel;
    @FXML
    private Button save;
    @FXML
    private VBox senhaConteiner;
    @FXML
    private TitledPane secaoTrab;
    @FXML
    private ToolBar toolBar;

    public DadosPessoaisController(Funcionario funcAcessado, Funcionario funcLogado, MainScreenListener listener, Supermercado supermercado) throws IllegalArgumentException {
        Util.verificaIsObjNull(funcLogado, "Funcionario logado");
        Util.verificaIsObjNull(supermercado, "Supermercado");

        this.funcionario = funcAcessado;
        this.supermercado = supermercado;
        this.listener = listener;
        if (funcLogado == funcAcessado) {
            isPerfil = true;
        }

    }

    public DadosPessoaisController(Cliente cliente) throws IllegalArgumentException {
        Util.verificaIsObjNull(cliente, "Cliente");
        this.cliente = cliente;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (listener == null) {         //Com certeza é consulta, ou de funcionario ou cliente
            toolBar.setVisible(false);
            toolBar.setManaged(false);
        }

        TableViewConfigurator.configure(contatosTable);
        masculino.setToggleGroup(generoGroup);
        feminino.setToggleGroup(generoGroup);
        numero.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5000, 0));

        estado.getItems().addAll(Arrays.asList(Estado.values()));

        if (funcionario == null && cliente == null) {   //é cadastro de funcionario
            senhaConteiner.setVisible(false);
            senhaConteiner.setManaged(false);
            save.setText("Cadastrar");
        } else {
            if (funcionario != null) {                  //é consulta ou perfil de fucionario
                setor.setText(funcionario.getSetor());
                cargo.setText(funcionario.getCargo());

                inicializaCamposPessoaFisica(funcionario);

                if (!isPerfil) {
                    desabilitaEdicaoDados();            //é Consulta de funcionario
                } else {
                    setor.setDisable(true);
                    cargo.setDisable(true);
                }

            } else {                                    //é consulta de cliente
                secaoTrab.setVisible(false);
                secaoTrab.setManaged(false);
                desabilitaEdicaoDados();
                inicializaCamposPessoaFisica(cliente);
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        listener.pullScreen();
    }

    @FXML
    private void save(ActionEvent event) {
        String email = this.email.getText();
        String senha = this.cpf.getText();
        String nome = this.nome.getText();
        String cpf = this.cpf.getText();
        RadioButton generoRB = (RadioButton) generoGroup.getSelectedToggle();

        PessoaFisica.Genero genero = null;
        if (generoRB == masculino) {
            genero = PessoaFisica.Genero.M;
        } else if (generoRB == feminino) {
            genero = PessoaFisica.Genero.F;
        }

        Date dataNasc = toDate(this.dataNasc.getValue());
        String setor = this.setor.getText();
        String cargo = this.cargo.getText();

        String cep = this.cep.getText();
        String rua = this.rua.getText();
        int numero = this.numero.getValue();
        String bairro = this.bairro.getText();
        String cidade = this.cidade.getText();
        Estado estado = this.estado.getValue();

        try {
            if (!isPerfil) {    //é cadastro de Funcionario
                Endereco endereco = new Endereco(bairro, cep, cidade, estado, numero, rua);
                Funcionario novoFuncionario = new Funcionario(cargo, setor, cpf, dataNasc, genero, email, senha, nome, endereco);
                FuncionarioDAO.create(novoFuncionario, supermercado);
            } else {             //é atualização dos dados
                //TODO usar fucionarioDAO.update();
            }
        } catch (UnsupportedEncodingException | ClassNotFoundException | IllegalArgumentException | NoSuchAlgorithmException | SQLException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        AlertCreator.criarAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Dados foram salvos", null);
        listener.pullScreen();
    }

    @FXML
    private void addContato(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alert/AddContatoAlert.fxml"));
        DialogPane dg = (DialogPane) loader.load();
        final AddContatoAlertController acac = (AddContatoAlertController) loader.getController();

        Alert alert = getAlertContato("Adicionar Contato!", dg, acac);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            List<String> row = new ArrayList<>();
            row.add(acac.getContato());
            row.add(acac.getTipo().toString());

            contatosTable.getItems().add(row);
        }
    }

    @FXML
    private void editContato(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alert/AddContatoAlert.fxml"));
        DialogPane dg = (DialogPane) loader.load();
        final AddContatoAlertController acac = (AddContatoAlertController) loader.getController();

        Alert alert = getAlertContato("Editar Contato!", dg, acac);

        List<String> row = contatosTable.getSelectionModel().getSelectedItem();
        acac.setContato(row.get(0));
        for (Contato.Tipo tipo : Contato.Tipo.values()) {
            if (tipo.toString().equals(row.get(1))) {
                acac.setTipo(tipo);
                break;
            }
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            row.set(0, acac.getContato());
            row.add(1, acac.getTipo().toString());
            contatosTable.refresh();
        }
    }

    //TODO Analizar como vai fazer para controlar os contatos
    @FXML
    private void removeContato(ActionEvent event) {
    }

    @FXML
    private void changeSenha(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Trocando de Senha!");
        alert.setHeaderText("Informe os dados:");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alert/MudarSenhaAlert.fxml"));
        DialogPane dg = (DialogPane) loader.load();
        final MudarSenhaAlertController msac = loader.getController();

        alert.setDialogPane(dg);
        alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button ok = (Button) dg.lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    //TODO usar na versão criptografada do BD
                    //if (!Util.criptografar(msac.getSenhaAtual()).equals(funcionario.getSenha())) {
                    if (!msac.getSenhaAtual().equals(funcionario.getSenha())) {
                        Toolkit.getDefaultToolkit().beep();
                        msac.setVisibilityErroSenhaAtual(true);
                        event.consume();
                    } else if (!msac.isSenhaNovaReady()) {
                        Toolkit.getDefaultToolkit().beep();
                        event.consume();
                    }
                } catch (Exception ex) {
                    AlertCreator.exibeExececao(ex);
                }
            }
        });

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            String senhaNova = msac.getSenhaNova();
            try {
                funcionario.setSenha(senhaNova);
                senha.setText(senhaNova);
            } catch (IllegalArgumentException ex) {
                AlertCreator.exibeExececao(ex);
            }
        }
    }

    private void desabilitaEdicaoDados() {
        cpf.setDisable(true);
        masculino.setDisable(true);
        feminino.setDisable(true);
        dataNasc.setDisable(true);
        cancel.setText("Voltar");
        nome.setDisable(true);
        email.setDisable(true);
        cep.setDisable(true);
        numero.setDisable(true);
        rua.setDisable(true);
        cidade.setDisable(true);
        bairro.setDisable(true);
        estado.setDisable(true);
        setor.setDisable(true);
        cargo.setDisable(true);

        addContato.setVisible(false);
        addContato.setManaged(false);
        editContato.setVisible(false);
        editContato.setManaged(false);
        removeContato.setVisible(false);
        removeContato.setManaged(false);
        senhaConteiner.setVisible(false);
        senhaConteiner.setManaged(false);
        save.setVisible(false);
        save.setManaged(false);
    }

    private void inicializaCamposPessoaFisica(PessoaFisica pf) {
        email.setText(pf.getLogin());
        senha.setText(pf.getSenha());

        nome.setText(pf.getNome());
        cpf.setText(pf.getCpf());
        if (pf.getGenero().toChar() == 'M') {
            generoGroup.selectToggle(masculino);
        } else {
            generoGroup.selectToggle(feminino);
        }
        dataNasc.setValue(toLocalDate(pf.getDataNasc()));

        Endereco endereco = pf.getEndereco();
        cep.setText(endereco.getCep());
        rua.setText(endereco.getRuaAvenida());
        numero.getValueFactory().setValue(endereco.getNumero());
        bairro.setText(endereco.getBairro());
        cidade.setText(endereco.getCidade());
        estado.setValue(endereco.getEstado());
    }

    private Alert getAlertContato(String title, DialogPane dg, final AddContatoAlertController acac) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Informe os dados:");

        alert.setDialogPane(dg);
        alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button ok = (Button) dg.lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (acac.getTipo() == Contato.Tipo.EMAIL && !Util.isLoginValido(acac.getContato())) {
                    Toolkit.getDefaultToolkit().beep();
                    event.consume();
                }
            }
        });

        return alert;
    }
}
