package br.com.dlweb.maternidade.mae.conexao;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import br.com.dlweb.maternidade.mae.Mae;

public class GetAllHttpService extends AsyncTask<Void, Void, Mae[]> {

    private String limit, offset;
    public GetAllHttpService(String limit, String offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    protected Mae[] doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();
        try {
            String s_url = "https://maternidadeback-1-l8476236.deta.app/api/maes?limit=" + this.limit + "&offset=" + this.offset;
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
        Mae[] maes = null;
        try {
            JSONObject returnValue = new JSONObject(resposta.toString());
            maes = gson.fromJson(returnValue.getJSONArray("data").toString(), Mae[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maes;
    }
}