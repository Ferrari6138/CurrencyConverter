package com.example.currencyconverter2;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ConversorController {
    @FXML private TextField valorInput;
    @FXML private ComboBox<String> moedaOrigem;
    @FXML private ComboBox<String> moedaDestino;
    @FXML private Label resultadoLabel;

    @FXML
    public void initialize() {
        // Popular combobox com moedas
        moedaOrigem.getItems().addAll("USD", "BRL", "EUR", "GBP", "JPY");
        moedaDestino.getItems().addAll("USD", "BRL", "EUR", "GBP", "JPY");
    }

    @FXML
    private void converterMoeda() {
        try {
            String de = moedaOrigem.getValue();
            String para = moedaDestino.getValue();
            double valor = Double.parseDouble(valorInput.getText());

            double taxa = getTaxaCambio(de, para);
            double resultado = valor * taxa;

            resultadoLabel.setText(String.format("%.2f %s = %.2f %s",
                    valor, de, resultado, para));
        } catch (Exception e) {
            resultadoLabel.setText("Erro na conversão");
            e.printStackTrace();
        }
    }

    private double getTaxaCambio(String de, String para) throws Exception {
        // Usando a API Frankfurter (gratuita)
        String urlStr = "https://api.frankfurter.app/latest?from=" + de + "&to=" + para;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Erro na API: " + responseCode);
        }

        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        JSONObject json = new JSONObject(response.toString());
        return json.getJSONObject("rates").getDouble(para);
    }
}