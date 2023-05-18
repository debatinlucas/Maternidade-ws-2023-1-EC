package br.com.dlweb.maternidade.bebe.conexao;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import br.com.dlweb.maternidade.bebe.Bebe;

public class GetHttpService extends AsyncTask<Void, Void, Bebe> {

    private int id;
    public GetHttpService(int id) {
        this.id = id;
    }

    @Override
    protected Bebe doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();
        try {
            String s_url = "https://maternidadeback-1-l8476236.deta.app/api/bebes/" + this.id;
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
        return  gson.fromJson(resposta.toString(), Bebe.class);
    }
}