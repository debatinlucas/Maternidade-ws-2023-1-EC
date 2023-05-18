package br.com.dlweb.maternidade.bebe.conexao;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import br.com.dlweb.maternidade.bebe.Bebe;

public class GetAllHttpService extends AsyncTask<Void, Void, Bebe[]> {

    private String limit, offset;
    public GetAllHttpService(String limit, String offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    protected Bebe[] doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();
        try {
            String s_url = "https://maternidadeback-1-l8476236.deta.app/api/bebes?limit=" + this.limit + "&offset=" + this.offset;
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
        Bebe[] bebes = null;
        try {
            JSONObject returnValue = new JSONObject(resposta.toString());
            bebes = gson.fromJson(returnValue.getJSONArray("data").toString(), Bebe[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bebes;
    }
}