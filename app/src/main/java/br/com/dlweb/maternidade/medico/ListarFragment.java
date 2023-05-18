package br.com.dlweb.maternidade.medico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.ExecutionException;

import br.com.dlweb.maternidade.R;
import br.com.dlweb.maternidade.medico.conexao.GetAllHttpService;

public class ListarFragment extends Fragment {

    public ListarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.medico_fragment_listar, container, false);

        RecyclerView recyclerViewMedicos = v.findViewById(R.id.recyclerViewMedicos);

        try {
            LinearLayoutManager manager = new LinearLayoutManager(v.getContext());
            recyclerViewMedicos.setLayoutManager(manager);
            recyclerViewMedicos.addItemDecoration(new DividerItemDecoration(v.getContext(), LinearLayoutManager.VERTICAL));
            recyclerViewMedicos.setHasFixedSize(true);
            Medico[] medicos = new GetAllHttpService("15", "0").execute().get();
            MedicoAdapter adapterMedicos = new MedicoAdapter(medicos, getActivity());
            recyclerViewMedicos.setAdapter(adapterMedicos);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return v;
    }
}