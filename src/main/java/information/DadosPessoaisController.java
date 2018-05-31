package information;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import main.MainButtonClickListener;
import modelo.pessoa.Endereco;
import modelo.pessoa.Endereco.Estado;
import modelo.supermercado.Funcionario;
import org.controlsfx.control.textfield.CustomPasswordField;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class DadosPessoaisController implements Initializable {

    private Funcionario funcionario;
    private MainButtonClickListener listener;

    @FXML
    private ToggleGroup generoGroup;
    @FXML
    private Button changeSenha;
    @FXML
    private VBox confirmContent;
    @FXML
    private RadioButton masculino;
    @FXML
    private RadioButton feminino;
    @FXML
    private Button cancel;
    @FXML
    private Button save;
    @FXML
    private TextField email;
    @FXML
    private CustomPasswordField senha;
    @FXML
    private CustomPasswordField confirmSenha;
    @FXML
    private TextField nome;
    @FXML
    private TextField cpf;
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
    private ChoiceBox<String> estado;

    public DadosPessoaisController(Funcionario funcionario, MainButtonClickListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener nulo!!");
        }

        this.funcionario = funcionario;
        this.listener = listener;

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
            estado.getItems().add(e.toString());
        }

        if (funcionario == null) {              //é cadastro
            changeSenha.setVisible(false);
            changeSenha.setManaged(false);
            save.setText("Cadastrar");
        } else {                                //não é cadastro
            confirmContent.setVisible(false);
            confirmContent.setManaged(false);
            
            senha.setDisable(true);
            cpf.setDisable(true);
            masculino.setDisable(true);
            feminino.setDisable(true);
            dataNasc.setDisable(true);
            
            inicializaCampos();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        listener.cancel();
    }

    @FXML
    private void save(ActionEvent event) {
        listener.save();
    }

    private void inicializaCampos() {
        email.setText(funcionario.getLogin());
        senha.setText(funcionario.getSenha());

        nome.setText(funcionario.getNome());
        cpf.setText(funcionario.getCpf());
        if (funcionario.getGenero().toChar() == 'M') generoGroup.selectToggle(masculino);
        else generoGroup.selectToggle(feminino);
        dataNasc.setValue(toLocalDate(funcionario.getDataNasc()));

        setor.setText(funcionario.getSetor());
        cargo.setText(funcionario.getCargo());

        Endereco endereco = funcionario.getEndereco();
        cep.setText(endereco.getCep());
        rua.setText(endereco.getRuaAvenida());
        numero.getValueFactory().setValue(endereco.getNumero());
        bairro.setText(endereco.getBairro());
        cidade.setText(endereco.getCidade());
        estado.setValue(endereco.getEstado().toString());
    }
    
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
