/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.search;

import database.supermercado.CompraDAO;
import database.usuarios.ClienteDAO;
import database.usuarios.FuncionarioDAO;
import visao.filter.FiltroController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import modelo.supermercado.Supermercado;
import modelo.usuarios.PessoaFisica;
import visao.filter.FilterComunication;
import visao.filter.item.FilterData;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import visao.details.DadosPessoaisController;
import visao.details.ComprasController;
import modelo.supermercado.Compra;
import modelo.usuarios.Cliente;
import modelo.usuarios.Funcionario;
import modelo.usuarios.PessoaFisica.Genero;
import visao.util.alerts.AlertCreator;
import visao.util.Screen;
import visao.util.TableViewConfigurator;
import util.Util;

/**
 * FXML Controller class
 *
 * @author joel-
 */
public class BuscaPessoaFisicaController implements Initializable, FilterComunication {

    private final PessoaFisicaClass pfc;
    private final Supermercado supermercado;
    private List<PessoaFisica> pfs;
    private Funcionario funcionario;

    @FXML
    private TableView<List<String>> pessoaFisicaTable;
    @FXML
    private Button ordersButton;

    public enum PessoaFisicaClass {
        Cliente("Cliente"), Funcionario("Funcionário");

        private final String classe;

        private PessoaFisicaClass(String classe) {
            this.classe = classe;
        }

        public String getClasse() {
            return classe;
        }
    }

    public BuscaPessoaFisicaController(FiltroController bc, PessoaFisicaClass pfc, Supermercado supermercado, Funcionario funcionario) throws IllegalArgumentException {
        Util.verificaIsObjNull(bc, "FiltroController");
        Util.verificaIsObjNull(pfc, "PessoaFisicaClass pf");
        Util.verificaIsObjNull(supermercado, "Supermercado");
        Util.verificaIsObjNull(funcionario, "Funcionario");

        this.supermercado = supermercado;
        this.pfc = pfc;
        this.funcionario = funcionario;

        List<FilterData> filters = new ArrayList<>();

        filters.add(new FilterData("Nome", "Dados Pessoais do " + pfc.getClasse(), String.class));
        filters.add(new FilterData("CPF", "Dados Pessoais do " + pfc.getClasse(), String.class));
        filters.add(new FilterData("Gênero", "Dados Pessoais do " + pfc.getClasse(), PessoaFisica.Genero.class));
        if (pfc == PessoaFisicaClass.Funcionario) {
            filters.add(new FilterData("Setor", "Profissão", String.class));
            filters.add(new FilterData("Cargo", "Profissão", String.class));
        }

        bc.setFilters(filters);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (pfc == PessoaFisicaClass.Funcionario) {
            ordersButton.setVisible(false);
            ordersButton.setManaged(false);
        }

        TableViewConfigurator.configure(pessoaFisicaTable);
    }

    @FXML
    private void getDetalhes(ActionEvent event) {
        int indxPF = pessoaFisicaTable.getSelectionModel().getSelectedIndex();
        if (indxPF == -1) {
            return;
        }

        PessoaFisica pf = pfs.get(indxPF);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/details/DadosPessoais.fxml"));
        DadosPessoaisController dpc;

        try {
            if (pfc == PessoaFisicaClass.Cliente) {
                dpc = new DadosPessoaisController((Cliente) pf);
            } else {
                dpc = new DadosPessoaisController((Funcionario) pf, funcionario, null, supermercado);
            }

            loader.setController(dpc);

            Screen.openNew(loader);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @FXML
    private void getAllOrders(ActionEvent event) {
        int indxPF = pessoaFisicaTable.getSelectionModel().getSelectedIndex();
        if (indxPF == -1) {
            return;
        }

        Cliente cliente = (Cliente) pfs.get(indxPF);

        try {
            List<Compra> compras = CompraDAO.readHistoricoComprasByCliente(cliente, supermercado);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/details/Compras.fxml"));
            ComprasController cc = new ComprasController(compras);
            loader.setController(cc);

            Screen.openNew(loader);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            AlertCreator.exibeExececao(ex);
        }
    }

    @Override
    public void listenResponse(Map<String, Object> response) {
        String nome = (String) response.get("Nome");
        String cpf = (String) response.get("CPF");
        Genero genero = (Genero) response.get("Gênero");

        try {
            if (pfc == PessoaFisicaClass.Cliente) {
                pfs = new ArrayList<PessoaFisica>(ClienteDAO.readClientesBySupermercado(supermercado, nome, cpf, genero));
            } else {
                String setor = (String) response.get("Setor");
                String cargo = (String) response.get("Cargo");

                pfs = new ArrayList<PessoaFisica>(FuncionarioDAO.readFuncionariosBySupermercado(supermercado, nome, cpf, genero, setor, cargo));
            }

            refreshTable();
        } catch (SQLException | ClassNotFoundException | IllegalArgumentException ex) {
            AlertCreator.exibeExececao(ex);
        }

    }

    private void refreshTable() {
        pessoaFisicaTable.getItems().clear();

        for (PessoaFisica pf : pfs) {
            List<String> row = new ArrayList<>();
            row.add(pf.getNome());
            row.add(pf.getCpf());

            pessoaFisicaTable.getItems().add(row);
        }
    }
}
