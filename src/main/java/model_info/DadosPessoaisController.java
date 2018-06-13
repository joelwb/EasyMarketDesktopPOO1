package model_info;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import database.usuarios.FuncionarioDAO;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import main.MainButtonClickListener;
import modelo.supermercado.Supermercado;
import modelo.usuarios.Cliente;
import modelo.usuarios.Endereco;
import modelo.usuarios.Endereco.Estado;
import modelo.usuarios.Funcionario;
import modelo.usuarios.PessoaFisica;
import org.controlsfx.control.textfield.CustomPasswordField;
import static util.ConversorDataObjs.toDate;
import static util.ConversorDataObjs.toLocalDate;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class DadosPessoaisController implements Initializable {
    private Supermercado supermercado;
    private Funcionario funcionario;
    private Cliente cliente;
    private MainButtonClickListener listener;

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

    public DadosPessoaisController(Funcionario funcionario, MainButtonClickListener listener, Supermercado supermercado) {
        this.funcionario = funcionario;
        this.supermercado = supermercado;
        this.listener = listener;
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
        masculino.setToggleGroup(generoGroup);
        feminino.setToggleGroup(generoGroup);
        numero.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5000, 0));

        for (Estado e : Estado.values()) {
            estado.getItems().add(e);
        }

        if (funcionario == null && cliente == null) {   //é cadastro de funcionario
            senhaConteiner.setVisible(false);
            senhaConteiner.setManaged(false);
            save.setText("Cadastrar");
        } else {
            senha.setDisable(true);
            cpf.setDisable(true);
            masculino.setDisable(true);
            feminino.setDisable(true);
            dataNasc.setDisable(true);
            cancel.setText("Voltar");

            if (funcionario != null) {                  //é consulta de fucionario
                setor.setText(funcionario.getSetor());
                cargo.setText(funcionario.getCargo());

                inicializaCamposPessoaFisica(funcionario);
            } else {                                     //é consulta de cliente
                inicializaCamposPessoaFisica(cliente);
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        listener.cancel();
    }

    @FXML
    private void save(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (funcionario == null && cliente == null){    //é cadastro de funcionario
            String email = this.email.getText();
            String senha = this.cpf.getText();
            String nome = this.nome.getText();
            String cpf = this.cpf.getText();
            RadioButton generoRB = (RadioButton) generoGroup.getSelectedToggle();
            
            PessoaFisica.Genero genero = null;
            if (generoRB == masculino){
                genero = PessoaFisica.Genero.M;
            }else {
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
            
            Endereco endereco = new Endereco(bairro, cep, cidade, estado, numero, rua);
            
            try{
                Funcionario novoFuncionario = new Funcionario(cargo, setor, cpf, dataNasc, genero, email, senha, nome, endereco);
                FuncionarioDAO.create(novoFuncionario, supermercado);
            }catch (IllegalArgumentException | NoSuchAlgorithmException | UnsupportedEncodingException ex){
                //TODO criar alert;
                return;
            }
            
        }
        
        listener.save();
    }

    @FXML
    private void addContato(ActionEvent event) {
    }

    @FXML
    private void editContato(ActionEvent event) {
    }

    @FXML
    private void removeContato(ActionEvent event) {
    }

    @FXML
    private void changeSenha(ActionEvent event) {
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

}
