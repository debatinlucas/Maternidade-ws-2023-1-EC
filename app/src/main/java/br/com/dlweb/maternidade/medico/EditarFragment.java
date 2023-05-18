package br.com.dlweb.maternidade.medico;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

import br.com.dlweb.maternidade.R;
import br.com.dlweb.maternidade.medico.conexao.DeleteHttpService;
import br.com.dlweb.maternidade.medico.conexao.PutHttpService;
import br.com.dlweb.maternidade.medico.conexao.GetHttpService;

public class EditarFragment extends Fragment {

    EditText etNome;
    EditText etEspecialidade;
    EditText etCelular;
    Medico m;

    public EditarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.medico_fragment_editar, container, false);
        Bundle b = getArguments();
        int crm_medico = b != null ? b.getInt("crm") : null;

        etNome = v.findViewById(R.id.editTextNome);
        etEspecialidade = v.findViewById(R.id.editTextEspecialidade);
        etCelular = v.findViewById(R.id.editTextCelular);

        // Obtém os dados do médico que será editado
        try {
            m = new GetHttpService(crm_medico).execute().get();
            etNome.setText(m.getNome());
            etEspecialidade.setText(m.getEspecialidade());
            etCelular.setText(m.getCelular());
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(getActivity(), "Erro ao buscar o médico!", Toast.LENGTH_LONG).show();
            Log.d("ListarMedico", "nenhum documento encontrado");
            e.printStackTrace();
        }


        Button btnEditar = v.findViewById(R.id.buttonEditar);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(crm_medico);
            }
        });

        Button btnExcluir = v.findViewById(R.id.buttonExcluir);

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.dialog_excluir_medico);
                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluir(crm_medico);
                    }
                });
                builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Não faz nada
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return v;
    }

    private void editar (int crm) {
        if (etNome.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome!", Toast.LENGTH_LONG).show();
        } else if (etEspecialidade.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe a especialidade!", Toast.LENGTH_LONG).show();
        } else if (etCelular.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o celular!", Toast.LENGTH_LONG).show();
        } else {
            m = new Medico();
            m.setCrm(crm);
            m.setNome(etNome.getText().toString());
            m.setEspecialidade(etEspecialidade.getText().toString());
            m.setCelular(etCelular.getText().toString());
            Gson gson = new Gson();
            String json = gson.toJson(m);
            Log.d("json", json);
            try {
                String retorno = new PutHttpService(crm, json).execute().get();
                if (!retorno.equals("false")) {
                    Toast.makeText(getActivity(), "Médico editado!", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMedico, new ListarFragment()).commit();
                }
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(getActivity(), "Erro ao editar o médico!", Toast.LENGTH_LONG).show();
                Log.d("EditarMedico", "mensagem de erro: ", e);
                e.printStackTrace();
            }
        }
    }

    private void excluir (int id) {
        try {
            String retorno = new DeleteHttpService(id).execute().get();
            if (retorno.equals("null")) {
                Toast.makeText(getActivity(), "Médico excluído!", Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMedico, new ListarFragment()).commit();
            }
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(getActivity(), "Erro ao excluir o médico!", Toast.LENGTH_LONG).show();
            Log.d("ExcluirMedico", "mensagem de erro: ", e);
            e.printStackTrace();
        }
    }
}