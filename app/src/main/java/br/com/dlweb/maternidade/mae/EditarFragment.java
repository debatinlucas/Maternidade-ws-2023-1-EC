package br.com.dlweb.maternidade.mae;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

import br.com.dlweb.maternidade.R;
import br.com.dlweb.maternidade.mae.conexao.DeleteHttpService;
import br.com.dlweb.maternidade.mae.conexao.GetHttpService;
import br.com.dlweb.maternidade.mae.conexao.PutHttpService;

public class EditarFragment extends Fragment {

    private Mae m;
    private EditText etNome;
    private EditText etCep;
    private EditText etLogradouro;
    private EditText etNumero;
    private EditText etBairro;
    private EditText etCidade;
    private EditText etFixo;
    private EditText etCelular;
    private EditText etComercial;
    private EditText etDataNascimento;

    public EditarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mae_fragment_editar, container, false);
        etNome = v.findViewById(R.id.editTextNome);
        etCep = v.findViewById(R.id.editTextCep);
        etLogradouro = v.findViewById(R.id.editTextLogradouro);
        etNumero = v.findViewById(R.id.editTextNumero);
        etBairro = v.findViewById(R.id.editTextBairro);
        etCidade = v.findViewById(R.id.editTextCidade);
        etFixo = v.findViewById(R.id.editTextFixo);
        etCelular = v.findViewById(R.id.editTextCelular);
        etComercial = v.findViewById(R.id.editTextComercial);
        etDataNascimento = v.findViewById(R.id.editTextDataNascimento);

        // id enviado via parâmetro no ListarFragment
        Bundle b = getArguments();
        int id_mae = b != null ? b.getInt("id") : null;

        Button btnEditar = v.findViewById(R.id.buttonEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(id_mae);
            }
        });

        Button btnExcluir = v.findViewById(R.id.buttonExcluir);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_excluir_mae);
                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        excluir(id_mae);
                    }
                });
                builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Não faz nada
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // Obtém os dados da mãe que será editada
        try {
            m = new GetHttpService(id_mae).execute().get();
            etNome.setText(m.getNome());
            etCep.setText(m.getCep());
            etLogradouro.setText(m.getLogradouro());
            etNumero.setText(String.valueOf(m.getNumero()));
            etBairro.setText(m.getBairro());
            etCidade.setText(m.getCidade());
            etFixo.setText(m.getFixo());
            etCelular.setText(m.getCelular());
            etComercial.setText(m.getComercial());
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            etDataNascimento.setText(formato.format(Date.valueOf(m.getData_nascimento())));
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(getActivity(), "Erro ao buscar a mãe!", Toast.LENGTH_LONG).show();
            Log.d("ListarMae", "nenhum documento encontrado");
            e.printStackTrace();
        }
        return v;
    }

    private void editar (int id) {
        if (etNome.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome!", Toast.LENGTH_LONG).show();
        } else if (etCep.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o CEP!", Toast.LENGTH_LONG).show();
        } else if (etLogradouro.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o logradouro!", Toast.LENGTH_LONG).show();
        } else if (etNumero.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o número do logradouro!", Toast.LENGTH_LONG).show();
        } else if (etBairro.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o bairro!", Toast.LENGTH_LONG).show();
        } else if (etCidade.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe a cidade!", Toast.LENGTH_LONG).show();
        } else if (etCelular.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o número do celular!", Toast.LENGTH_LONG).show();
        } else if (etDataNascimento.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe a data de nascimento!", Toast.LENGTH_LONG).show();
        } else {
            m.setNome(etNome.getText().toString());
            m.setLogradouro(etLogradouro.getText().toString());
            m.setCep(etCep.getText().toString());
            m.setNumero(Integer.parseInt(etNumero.getText().toString()));
            m.setBairro(etBairro.getText().toString());
            m.setCidade(etCidade.getText().toString());
            m.setFixo(etFixo.getText().toString());
            m.setCelular(etCelular.getText().toString());
            m.setComercial(etComercial.getText().toString());
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatoSaida = new SimpleDateFormat("yyyy-MM-dd");
            try {
                m.setData_nascimento(formatoSaida.format(formatoEntrada.parse(etDataNascimento.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            String json = gson.toJson(m);
            Log.d("json", json);
            try {
                String retorno = new PutHttpService(id, json).execute().get();
                if (!retorno.equals("false")) {
                    Toast.makeText(getActivity(), "Mãe atualizada!", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMae, new ListarFragment()).commit();
                }
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(getActivity(), "Erro ao editar a mãe!", Toast.LENGTH_LONG).show();
                Log.d("EditarMae", "mensagem de erro: ", e);
                e.printStackTrace();
            }
        }
    }

    private void excluir(int id) {
        try {
            String retorno = new DeleteHttpService(id).execute().get();
            if (retorno.equals("null")) {
                Toast.makeText(getActivity(), "Mãe excluída!", Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMae, new ListarFragment()).commit();
            }
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(getActivity(), "Erro ao excluir a mãe!", Toast.LENGTH_LONG).show();
            Log.d("ExcluirMae", "mensagem de erro: ", e);
            e.printStackTrace();
        }
    }
}