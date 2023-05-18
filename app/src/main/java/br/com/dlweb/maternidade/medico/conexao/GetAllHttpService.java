package br.com.dlweb.maternidade.medico.conexao;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import br.com.dlweb.maternidade.medico.Medico;

public class GetAllHttpService extends AsyncTask<Void, Void, Medico[]> {

    private String limit, offset;
    public GetAllHttpService(String limit, String offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    protected Medico[] doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();
        try {
            String s_url = "https://maternidadeback-1-l8476236.deta.app/api/medicos?limit=" + this.limit + "&offset=" + this.offset;
            URL url = new URL(s_url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(false);
            connection.setConnectTimeout(5000);
            connection.connect();

            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                resposta.append(scanner.nextLine());
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Medico[] medicos = null;
        try {
            JSONObject returnValue = new JSONObject(resposta.toString());
            medicos = gson.fromJson(returnValue.getJSONArray("data").toString(), Medico[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return medicos;
    }
}