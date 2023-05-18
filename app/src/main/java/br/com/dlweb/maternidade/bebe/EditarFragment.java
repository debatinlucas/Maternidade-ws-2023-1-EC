package br.com.dlweb.maternidade.bebe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.dlweb.maternidade.R;
import br.com.dlweb.maternidade.mae.Mae;
import br.com.dlweb.maternidade.medico.Medico;
import br.com.dlweb.maternidade.bebe.conexao.GetHttpService;
import br.com.dlweb.maternidade.bebe.conexao.DeleteHttpService;
import br.com.dlweb.maternidade.bebe.conexao.PutHttpService;

public class EditarFragment extends Fragment {

    EditText etNome;
    EditText etDataNascimento;
    EditText etPeso;
    EditText etAltura;
    Spinner spMae;
    Spinner spMedico;
    ArrayList<Integer> listMaeId;
    ArrayList<String> listMaeName;
    ArrayList<Integer> listMedicoCrm;
    ArrayList<String> listMedicoName;
    Bebe b;

    public EditarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bebe_fragment_editar, container, false);
        Bundle bundle = getArguments();
        int id_bebe = bundle.getInt("id");

        spMae = v.findViewById(R.id.spinnerMae);
        spMedico = v.findViewById(R.id.spinnerMedico);
        etNome = v.findViewById(R.id.editTextNome);
        etDataNascimento = v.findViewById(R.id.editTextDataNascimento);
        etPeso = v.findViewById(R.id.editTextPeso);
        etAltura = v.findViewById(R.id.editTextAltura);

        listMaeId = new ArrayList<>();
        listMaeName = new ArrayList<>();
        listMedicoCrm = new ArrayList<>();
        listMedicoName = new ArrayList<>();
        try {
            Mae[] maes = new br.com.dlweb.maternidade.mae.conexao.GetAllHttpService("15", "0").execute().get();
            for (Mae mae : maes) {
                listMaeId.add(mae.getId());
                listMaeName.add(mae.getNome());
            }
            ArrayAdapter<String> spMaeArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listMaeName);
            spMae.setAdapter(spMaeArrayAdapter);

            Medico[] medicos = new br.com.dlweb.maternidade.medico.conexao.GetAllHttpService("15", "0").execute().get();
            for (Medico medico : medicos) {
                listMedicoCrm.add(medico.getCrm());
                listMedicoName.add(medico.getNome());
            }

            ArrayAdapter<String> spMedicoArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listMedicoName);
            spMedico.setAdapter(spMedicoArrayAdapter);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        // Obtém os dados do médico que será editado
        try {
            b = new GetHttpService(id_bebe).execute().get();
            etNome.setText(b.getNome());
            etPeso.setText(String.valueOf(b.getPeso()));
            etAltura.setText(String.valueOf(b.getAltura()));
            spMae.setSelection(listMaeId.indexOf(b.getId_mae()));
            spMedico.setSelection(listMedicoCrm.indexOf(b.getCrm_medico()));
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            etDataNascimento.setText(formato.format(Date.valueOf(b.getData_nascimento())));
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(getActivity(), "Erro ao buscar o médico!", Toast.LENGTH_LONG).show();
            Log.d("ListarMedico", "nenhum documento encontrado");
            e.printStackTrace();
        }

        Button btnEditar = v.findViewById(R.id.buttonEditar);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(id_bebe);
            }
        });

        Button btnExcluir = v.findViewById(R.id.buttonExcluir);

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.dialog_excluir_bebe);
                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluir(id_bebe);
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

    private void editar (int id) {
        if (etNome.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome!", Toast.LENGTH_LONG).show();
        } else if (etDataNascimento.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe a data nascimento!", Toast.LENGTH_LONG).show();
        } else if (etPeso.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o peso!", Toast.LENGTH_LONG).show();
        } else if (etAltura.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o altura!", Toast.LENGTH_LONG).show();
        } else {
            b = new Bebe();
            b.setId(id);
            String nomeMae = spMae.getSelectedItem().toString();
            b.setId_mae(listMaeId.get(listMaeName.indexOf(nomeMae)));
            String nomeMedico = spMedico.getSelectedItem().toString();
            b.setCrm_medico(listMedicoCrm.get(listMedicoName.indexOf(nomeMedico)));
            b.setNome(etNome.getText().toString());
            b.setPeso(Float.parseFloat(etPeso.getText().toString()));
            b.setAltura(Integer.parseInt(etAltura.getText().toString()));
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatoSaida = new SimpleDateFormat("yyyy-MM-dd");
            try {
                b.setData_nascimento(formatoSaida.format(formatoEntrada.parse(etDataNascimento.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            String json = gson.toJson(b);
            Log.d("json", json);
            try {
                String retorno = new PutHttpService(id, json).execute().get();
                if (!retorno.equals("false")) {
                    Toast.makeText(getActivity(), "Bebê editado!", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameBebe, new ListarFragment()).commit();
                }
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(getActivity(), "Erro ao editar o bebê!", Toast.LENGTH_LONG).show();
                Log.d("EditarBebe", "mensagem de erro: ", e);
                e.printStackTrace();
            }
        }
    }

    private void excluir (int id) {
        try {
            String retorno = new DeleteHttpService(id).execute().get();
            if (retorno.equals("null")) {
                Toast.makeText(getActivity(), "Bebê excluído!", Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameBebe, new ListarFragment()).commit();
            }
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(getActivity(), "Erro ao excluir o bebê!", Toast.LENGTH_LONG).show();
            Log.d("ExcluirBebe", "mensagem de erro: ", e);
            e.printStackTrace();
        }
    }
}