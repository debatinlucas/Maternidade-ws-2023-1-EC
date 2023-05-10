package br.com.dlweb.maternidade.mae;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.ExecutionException;

import br.com.dlweb.maternidade.R;
import br.com.dlweb.maternidade.mae.conexao.GetAllHttpService;

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
        View v = inflater.inflate(R.layout.mae_fragment_listar, container, false);

        RecyclerView recyclerViewMaes = v.findViewById(R.id.recyclerViewMaes);
        try {
            LinearLayoutManager manager = new LinearLayoutManager(v.getContext());
            recyclerViewMaes.setLayoutManager(manager);
            recyclerViewMaes.addItemDecoration(new DividerItemDecoration(v.getContext(), LinearLayoutManager.VERTICAL));
            recyclerViewMaes.setHasFixedSize(true);
            Mae[] maes = new GetAllHttpService("15", "0").execute().get();
            MaeAdapter adapterMaes = new MaeAdapter(maes, getActivity());
            recyclerViewMaes.setAdapter(adapterMaes);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return v;
    }
}