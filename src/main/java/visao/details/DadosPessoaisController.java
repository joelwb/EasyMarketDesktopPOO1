package visao.details;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import visao.util.alerts.AddContatoAlertController;
import visao.util.alerts.MudarSenhaAlertController;
import br.com.parg.viacep.ViaCEP;
import br.com.parg.viacep.ViaCEPEvents;
import br.com.parg.viacep.ViaCEPException;
import database.usuarios.ContatoDAO;
import database.usuarios.FuncionarioDAO;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.stage.Stage;
import modelo.supermercado.Supermercado;
import modelo.usuarios.Cliente;
import modelo.usuarios.Contato;
import modelo.usuarios.Endereco;
import modelo.usuarios.Endereco.Estado;
import modelo.usuarios.Funcionario;
import modelo.usuarios.PessoaFisica;
import org.controlsfx.control.textfield.CustomPasswordField;
import visao.util.alerts.AlertCreator;
import static visao.util.DateObjConversor.toDate;
import static visao.util.DateObjConversor.toLocalDate;
import visao.util.TableViewConfigurator;
import util.Util;
import visao.main.MainScreenListener;
import modelo.usuarios.Contato.Tipo;
import org.json.JSONException;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class DadosPessoaisController implements Initializable, ViaCEPEvents {

    private Supermercado supermercado;
    private Funcionario funcionario;
    private Cliente cliente;
    private MainScreenListener listener;
    private boolean isPerfil;
    private List<Contato> contatos;

    private List<Contato> addedContatos;
    private List<Contato> editedContatos;
    private List<Contato> removedContatos;

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
    private Button apagar;
    @FXML
    private Button searchCepButton;
    @FXML
    private VBox senhaConteiner;
    @FXML
    private TitledPane secaoTrab;
    @FXML
    private ToolBar toolBar;

    public DadosPessoaisController(Funcionario funcAcessado, Funcionario funcLogado, MainScreenListener listener, Supermercado supermercado) throws IllegalArgumentException, SQLException, ClassNotFoundException {
        Util.verificaIsObjNull(funcLogado, "Funcionario logado");
        Util.verificaIsObjNull(supermercado, "Supermercado");

        this.funcionario = funcAcessado;
        this.supermercado = supermercado;
        this.listener = listener;
        if (funcAcessado != null && funcAcessado.getId() == funcLogado.getId()) {
            isPerfil = true;
        }

        if (funcAcessado != null) {
            contatos = ContatoDAO.readContatosByPessoa(funcAcessado);
        }
    }

    public DadosPessoaisController(Cliente cliente) throws IllegalArgumentException, SQLException, ClassNotFoundException {
        Util.verificaIsObjNull(cliente, "Cliente");
        this.cliente = cliente;
        contatos = ContatoDAO.readContatosByPessoa(cliente);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (listener == null) {         //Com certeza é consulta, ou de funcionario ou cliente
            cancel.setVisible(false);
            cancel.setManaged(false);

            save.setVisible(false);
            save.setManaged(false);

            if (isPerfil) {
                desabilitaEdicaoDados(); // é o próprio funcionario consultando seu perfil
            }
        }

        TableViewConfigurator.configure(contatosTable);
        masculino.setToggleGroup(generoGroup);
        feminino.setToggleGroup(generoGroup);
        numero.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5000, 0));

        estado.getItems().addAll(Arrays.asList(Estado.values()));

        if (funcionario == null && cliente == null) {   //é cadastro de funcionario
            senhaConteiner.setVisible(false);
            senhaConteiner.setManaged(false);
            apagar.setVisible(false);
            apagar.setManaged(false);
            save.setText("Cadastrar");

            addedContatos = new ArrayList<>();
            editedContatos = new ArrayList<>();
            removedContatos = new ArrayList<>();
        } else {

            for (Contato contato : contatos) {
                List<String> row = new ArrayList<>();
                row.add(contato.getDescricao());
                row.add(contato.getTipo().toString());

                contatosTable.getItems().add(row);
            }

            if (funcionario != null) {                  //é consulta ou perfil de fucionario
                addedContatos = new ArrayList<>();
                editedContatos = new ArrayList<>();
                removedContatos = new ArrayList<>();

                setor.setText(funcionario.getSetor());
                cargo.setText(funcionario.getCargo());

                inicializaCamposPessoaFisica(funcionario);

                if (!isPerfil) {
                    desabilitaEdicaoDados();            //é Consulta de funcionario
                } else {
                    email.setDisable(true);
                    setor.setDisable(true);
                    cargo.setDisable(true);

                    apagar.setVisible(false);
                    apagar.setManaged(false);
                }

            } else {                                    //é consulta de cliente
                apagar.setVisible(false);
                apagar.setManaged(false);

                secaoTrab.setVisible(false);
                secaoTrab.setManaged(false);
                desabilitaEdicaoDados();
                inicializaCamposPessoaFisica(cliente);
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        if (!AlertCreator.getConfirmacao()) {
            return;
        }

        listener.pullScreen();
    }

    @FXML
    private void save(ActionEvent event) {
        if (!AlertCreator.getConfirmacao()) {
            return;
        }

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

        if (cep.split("-").length != 2) {
            cep = cep.substring(0, 5) + "-" + cep.substring(5);
        }

        try {
            Endereco endereco = new Endereco(bairro, cep, cidade, estado, numero, rua);

            if (!isPerfil) {    //é cadastro de Funcionario
                Funcionario novoFuncionario = new Funcionario(cargo, setor, cpf, dataNasc, genero, email, senha, nome, endereco);
                int id = FuncionarioDAO.create(novoFuncionario, supermercado);

                funcionario = new Funcionario(cargo, setor, cpf, dataNasc, genero, email, senha, id, nome, endereco);
            } else {            //é atualização dos dados
                funcionario.setCargo(cargo);
                funcionario.setSetor(setor);
                funcionario.setEndereco(endereco);
                funcionario.setNome(nome);
                funcionario.setGenero(genero);
                funcionario.setDataNasc(dataNasc);
                funcionario.setCpf(cpf);
                funcionario.setSenha(this.senha.getText());

                FuncionarioDAO.update(funcionario);
            }
        } catch (UnsupportedEncodingException | ClassNotFoundException | IllegalArgumentException | NoSuchAlgorithmException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("23505")) {
                AlertCreator.criarAlert(Alert.AlertType.WARNING, "Email repetido", "Já existem uma conta com mesmo email", null);
            } else {
                AlertCreator.exibeExececao(ex);
            }
            return;
        }

        try {
            for (Contato c : addedContatos) {
                ContatoDAO.create(c, funcionario);
            }

            for (Contato c : editedContatos) {
                ContatoDAO.update(c);
            }

            for (Contato c : removedContatos) {
                ContatoDAO.delete(c.getId());
            }
        } catch (ClassNotFoundException | SQLException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        //TODO Atualizar os contatos
        AlertCreator.criarAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Dados foram salvos", null);
        listener.pullScreen();
    }

    private int getIndxOfContato(List<Contato> contatos, Contato c) {
        for (int i = 0; i < contatos.size(); i++) {
            Contato cont = contatos.get(i);
            if (cont.getDescricao().equals(c.getDescricao()) && cont.getTipo() == c.getTipo()) {
                return i;
            }
        }

        return -1;
    }

    @FXML
    private void addContato(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alert/AddContatoAlert.fxml"));
        DialogPane dg = (DialogPane) loader.load();
        final AddContatoAlertController acac = (AddContatoAlertController) loader.getController();

        Alert alert = getAlertContato("Adicionar Contato!", dg, acac);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            addedContatos.add(new Contato(acac.getContato(), acac.getTipo()));

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
        Tipo tipo = null;
        for (Tipo t : Tipo.values()) {
            if (t.toString().equals(row.get(1))) {
                acac.setTipo(t);
                tipo = t;
                break;
            }
        }

        Contato editedContato = new Contato(row.get(0), tipo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            int indx = getIndxOfContato(addedContatos, editedContato);
            if (indx != -1) {
                addedContatos.remove(indx);
                addedContatos.add(new Contato(acac.getContato(), acac.getTipo()));
            } else {
                Contato c = contatos.get(getIndxOfContato(contatos, editedContato));
                editedContatos.remove(c);

                c.setDescricao(acac.getContato());
                c.setTipo(acac.getTipo());

                editedContatos.add(c);
            }

            row.set(0, acac.getContato());
            row.add(1, acac.getTipo().toString());
            contatosTable.refresh();
        }
    }

    //TODO Analizar como vai fazer para controlar os contatos
    @FXML
    private void removeContato(ActionEvent event) {
        List<String> row = contatosTable.getSelectionModel().getSelectedItem();

        Tipo tipo = null;
        for (Tipo t : Tipo.values()) {
            if (row.get(1).equals(t.toString())) {
                tipo = t;
                break;
            }
        }

        Contato removedContato = new Contato(row.get(0), tipo);

        int indx = getIndxOfContato(addedContatos, removedContato);
        if (indx != -1) {
            addedContatos.remove(indx);
        } else if ((indx = getIndxOfContato(editedContatos, removedContato)) != -1) {
            editedContatos.remove(indx);
        }

        if (contatos != null && (indx = getIndxOfContato(contatos, removedContato)) != -1) {
            Contato c = contatos.get(indx);
            removedContatos.add(c);
        }

        contatosTable.getItems().remove(row);
    }

    @FXML
    private void apagar(ActionEvent event) {
        if (!AlertCreator.getConfirmacao()) {
            return;
        }

        try {
            FuncionarioDAO.delete(funcionario.getId());
        } catch (SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
            return;
        }

        AlertCreator.criarAlert(Alert.AlertType.INFORMATION, "Sucesso", "Funcionário apagado com sucesso!", null);

        ((Stage) email.getScene().getWindow()).close();
    }

    @FXML
    private void searchCep(ActionEvent event) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ViaCEP viaCEP = new ViaCEP(cep.getText(), DadosPessoaisController.this);
                } catch (ViaCEPException | JSONException ex) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (ex instanceof ViaCEPException) {
                                AlertCreator.criarAlert(Alert.AlertType.WARNING, "Erro!", "Error ao buscar endereço!", "Não foi possível encontrar o CEP");
                            } else {
                                AlertCreator.exibeExececao(ex);
                            }
                        }
                    });
                }
            }
        });

        t.start();
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
                    if (!Util.criptografar(msac.getSenhaAtual()).equals(funcionario.getSenha())) {
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
            String novaSenha = msac.getSenhaNova();
            try {
                senha.setText(Util.criptografar(novaSenha));
            } catch (IllegalArgumentException | NoSuchAlgorithmException | UnsupportedEncodingException ex) {
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
        searchCepButton.setDisable(true);

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

    @Override
    public void onCEPSuccess(final ViaCEP viacep) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                rua.setText(new String(viacep.getLogradouro().getBytes(), Charset.forName("UTF-8")));
                bairro.setText(new String(viacep.getBairro().getBytes(), Charset.forName("UTF-8")));
                cidade.setText(new String(viacep.getLocalidade().getBytes(), Charset.forName("UTF-8")));

                for (Estado e : Estado.values()) {
                    if (e.toString().equals(viacep.getUf())) {
                        estado.setValue(e);
                    }
                }
            }
        });
    }

    @Override
    public void onCEPError(String string) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                rua.setText("");
                bairro.setText("");
                cidade.setText("");
                estado.setValue(null);
            }
        });
    }
}
