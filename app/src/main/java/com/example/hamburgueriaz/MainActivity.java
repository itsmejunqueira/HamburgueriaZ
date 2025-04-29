package com.example.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int quantidade = 0;
    private TextView quantidadeTextView;
    private EditText nomeClienteEditText;
    private CheckBox baconCheckBox, queijoCheckBox, onionRingsCheckBox;
    private TextView precoTotalTextView;
    private TextView resumoPedidoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quantidadeTextView = findViewById(R.id.quantidade_escolhida);
        Button adicionarButton = findViewById(R.id.adicionar_quantidade);
        Button subtrairButton = findViewById(R.id.subtrair_quantidade);
        nomeClienteEditText = findViewById(R.id.nome_cliente);
        baconCheckBox = findViewById(R.id.adicional_bacon);
        queijoCheckBox = findViewById(R.id.adicional_queijo);
        onionRingsCheckBox = findViewById(R.id.adicional_onionrings);
        precoTotalTextView = findViewById(R.id.preco_total);
        resumoPedidoTextView = findViewById(R.id.resumopedido);
        Button enviarPedidoButton = findViewById(R.id.enviar_pedido);

        configurarEventosCheckBox();
        atualizarQuantidadeTextView();
        atualizarPrecoTotal();

        adicionarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarQuantidade();
            }
        });

        subtrairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtrairQuantidade();
            }
        });

        enviarPedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPedido();
            }
        });
    }

    private void adicionarQuantidade() {
        quantidade++;
        atualizarQuantidadeTextView();
        atualizarPrecoTotal();
    }

    private void subtrairQuantidade() {
        if (quantidade > 0) {
            quantidade--;
            atualizarQuantidadeTextView();
            atualizarPrecoTotal();
        }
    }

    private void atualizarQuantidadeTextView() {
        quantidadeTextView.setText(String.valueOf(quantidade));
    }

    private void configurarEventosCheckBox() {
        baconCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarPrecoTotal());
        queijoCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarPrecoTotal());
        onionRingsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarPrecoTotal());
    }

    private void atualizarPrecoTotal() {
        boolean temBacon = baconCheckBox.isChecked();
        boolean temQueijo = queijoCheckBox.isChecked();
        boolean temOnionRings = onionRingsCheckBox.isChecked();

        double precoTotal = calcularPrecoTotal(temBacon, temQueijo, temOnionRings);
        precoTotalTextView.setText("Preço Total: R$ " + String.format("%.2f", precoTotal));
    }

    private double calcularPrecoTotal(boolean temBacon, boolean temQueijo, boolean temOnionRings) {
        double precoBase = 20.0;
        double precoAdicionalBacon = temBacon ? 2.0 : 0.0;
        double precoAdicionalQueijo = temQueijo ? 2.0 : 0.0;
        double precoAdicionalOnionRings = temOnionRings ? 3.0 : 0.0;

        return (precoBase + precoAdicionalBacon + precoAdicionalQueijo + precoAdicionalOnionRings) * quantidade;
    }

    private void enviarPedido() {
        String nomeCliente = nomeClienteEditText.getText().toString();
        boolean temBacon = baconCheckBox.isChecked();
        boolean temQueijo = queijoCheckBox.isChecked();
        boolean temOnionRings = onionRingsCheckBox.isChecked();

        double precoTotal = calcularPrecoTotal(temBacon, temQueijo, temOnionRings);

        String resumo = "Nome do cliente: " + nomeCliente + "\n" +
                "Tem Bacon? " + (temBacon ? "Sim" : "Não") + "\n" +
                "Tem Queijo? " + (temQueijo ? "Sim" : "Não") + "\n" +
                "Tem Onion Rings? " + (temOnionRings ? "Sim" : "Não") + "\n" +
                "Quantidade: " + quantidade + "\n" +
                "Preço final: R$ " + String.format("%.2f", precoTotal);

        resumoPedidoTextView.setText(resumo);
        precoTotalTextView.setText("Preço Total: R$ " + String.format("%.2f", precoTotal));

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"estudanteads2025@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nomeCliente);
        intent.putExtra(Intent.EXTRA_TEXT, resumo);
        try {
            startActivity(Intent.createChooser(intent, "Enviar e-mail"));
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }
}