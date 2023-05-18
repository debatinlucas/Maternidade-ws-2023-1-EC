package br.com.dlweb.maternidade.bebe;

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
import br.com.dlweb.maternidade.bebe.conexao.GetAllHttpService;

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
        View v = inflater.inflate(R.layout.bebe_fragment_listar, container, false);

        RecyclerView recyclerViewBebes = v.findViewById(R.id.recyclerViewBebes);


        try {
            LinearLayoutManager manager = new LinearLayoutManager(v.getContext());
            recyclerViewBebes.setLayoutManager(manager);
            recyclerViewBebes.addItemDecoration(new DividerItemDecoration(v.getContext(), LinearLayoutManager.VERTICAL));
            recyclerViewBebes.setHasFixedSize(true);
            Bebe[] bebes = new GetAllHttpService("15", "0").execute().get();
            BebeAdapter adapterBebes = new BebeAdapter(bebes, getActivity());
            recyclerViewBebes.setAdapter(adapterBebes);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return v;
    }
}