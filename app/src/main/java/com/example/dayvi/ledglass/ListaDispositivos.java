package com.example.dayvi.ledglass;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by dayvi on 02/03/2017.
 */

public class ListaDispositivos extends ListActivity {

    static String ENDERECO_MAC = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter <String> arrayBluettoth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set <BluetoothDevice> dispositivosPareados = bluetoothAdapter.getBondedDevices();

        if (dispositivosPareados.size() > 0) {
            for (BluetoothDevice dispositivos : dispositivosPareados) {
                String nomeBt = dispositivos.getName();
                String macBt = dispositivos.getAddress();
                arrayBluettoth.add(nomeBt + "\n" + macBt);
            }
        }
        setListAdapter(arrayBluettoth);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String infoGeral = ((TextView) v).getText().toString();
        //Toast.makeText(getApplicationContext(), "info: " + infoGeral, Toast.LENGTH_LONG).show();

        String enderecoMac = infoGeral.substring(infoGeral.length() - 17);
        //Toast.makeText(getApplicationContext(), "mac: " + enderecoMac, Toast.LENGTH_LONG).show();

        Intent returnMac = new Intent();
        returnMac.putExtra(ENDERECO_MAC, enderecoMac);
        setResult(RESULT_OK, returnMac);
        finish();

    }
}
