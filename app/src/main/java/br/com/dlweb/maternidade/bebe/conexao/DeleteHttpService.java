package br.com.dlweb.maternidade.bebe.conexao;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DeleteHttpService extends AsyncTask<Void, Void, String> {

    private int id;
    public DeleteHttpService(int id) {
        this.id = id;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String jsonDeResposta = "false";
        try {
            String s_url = "https://maternidadeback-1-l8476236.deta.app/api/bebes/" + this.id;
            URL url = new URL(s_url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(false);
            connection.setConnectTimeout(5000);
            connection.connect();

            jsonDeResposta = new Scanner(connection.getInputStream()).next();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonDeResposta;
    }
}