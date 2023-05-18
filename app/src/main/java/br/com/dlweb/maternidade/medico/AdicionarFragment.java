package br.com.dlweb.maternidade.medico;

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
import br.com.dlweb.maternidade.medico.conexao.PostHttpService;

public class AdicionarFragment extends Fragment {

    EditText etNome;
    EditText etEspecialidade;
    EditText etCelular;

    public AdicionarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.medico_fragment_adicionar, container, false);

        etNome = v.findViewById(R.id.editTextNome);
        etEspecialidade = v.findViewById(R.id.editTextEspecialidade);
        etCelular = v.findViewById(R.id.editTextCelular);

        Button btnSalvar = v.findViewById(R.id.buttonAdicionar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionar();
            }
        });

        return v;
    }

    private void adicionar () {
        if (etNome.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome!", Toast.LENGTH_LONG).show();
        } else if (etEspecialidade.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe a especialidade!", Toast.LENGTH_LONG).show();
        } else if (etCelular.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o celular!", Toast.LENGTH_LONG).show();
        } else {
            Medico m = new Medico();
            m.setNome(etNome.getText().toString());
            m.setEspecialidade(etEspecialidade.getText().toString());
            m.setCelular(etCelular.getText().toString());
            Gson gson = new Gson();
            String json = gson.toJson(m);
            try {
                String retorno = new PostHttpService(json).execute().get();
                if (!retorno.equals("false")) {
                    Toast.makeText(getActivity(), "Médico salvo!", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMedico, new ListarFragment()).commit();
                }
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(getActivity(), "Erro ao salvar o médico!", Toast.LENGTH_LONG).show();
                Log.d("AdicionarMedico", "mensagem de erro: ", e);
                e.printStackTrace();
            }
        }
    }
}