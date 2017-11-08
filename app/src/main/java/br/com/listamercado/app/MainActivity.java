package br.com.listamercado.app;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    EditText txtProduto;
    ProdutoAdapter adapter;
    View.OnClickListener clickCK = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CheckBox ck = (CheckBox) view;
            int pos = (int) ck.getTag();

            Produto prodSelecioaado = adapter.getItem(pos);
            Produto prodBanco = Produto.findById(Produto.class, prodSelecioaado.getId());

            if (ck.isChecked()){
                prodBanco.setAtivo(true);
                prodSelecioaado.setAtivo(true);
                prodBanco.save();
            }else{
                prodBanco.setAtivo(false);
                prodSelecioaado.setAtivo(false);
                prodBanco.save();
            }

            adapter.notifyDataSetChanged();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        txtProduto = (EditText)findViewById(R.id.txtProduto);

        //Fazendo o select
        List<Produto> lstProdutos = Produto.listAll(Produto.class);

        //Cria adapter
        adapter = new ProdutoAdapter(this, lstProdutos);

        //Liga o adapter a lista
        listView.setAdapter(adapter);
    }

    public void InserirItem(View view) {
        String produto = txtProduto.getText().toString();

        if (produto.isEmpty()){
            txtProduto.setError("Insira um valor");
            return;
        }

        Produto p = new Produto(produto, false);

        //Adicionando no banco
        p.save();

        //Limpar Caixinha
        txtProduto.setText(null);

        //Adicionando o produto na lista
        adapter.add(p);
    }

    //Adapter Class
    private class ProdutoAdapter extends ArrayAdapter<Produto>{

        public ProdutoAdapter(Context context, List<Produto> itens) {

            super(context, 0, itens);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View v = convertView;

            if(v == null){
                v = LayoutInflater.from(getContext()).inflate(R.layout.item_lista, null);
            }

            Produto item = getItem(position);

            TextView txtItemProduto = v.findViewById(R.id.txtItem);
            CheckBox ckItemChecado = v.findViewById(R.id.ckItemProduto);

            txtItemProduto.setText(item.getNome());
            ckItemChecado.setChecked(item.isAtivo());

            //Guarda a posição do item em uma tag'''''''''''''''''''''''''''''''''''
            ckItemChecado.setTag(position);

            ckItemChecado.setOnClickListener(clickCK);

            return v;
        }
    }
}