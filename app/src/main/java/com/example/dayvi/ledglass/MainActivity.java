package com.example.dayvi.ledglass;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import petrov.kristiyan.colorpicker.ColorPicker;

public class MainActivity extends AppCompatActivity {
    String textCaixaText = null;
    private AlertDialog alerta;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int SOLICITA_CONEXAO = 2;
    boolean conexao = false;
    private  static String MAC = null;
    BluetoothDevice mBluetoothDevice = null;
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothSocket mSocket = null;
    private static final UUID mUUID = UUID.fromString("28c36230-ff62-11e6-9598-0800200c9a66"); //foda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton); //button text
        ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton2); //button set color
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.imageButton3); //button effects
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton4); //button equalizer
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //ativando adaptador Bluetooth

        /*Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);*/

        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth não encontrado", Toast.LENGTH_SHORT).show();
        }
        else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "verificar se está conectado, se sim -> enviar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = getLayoutInflater();
                final View view = li.inflate(R.layout.set_text, null);
                view.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        TextView text = (TextView) view.findViewById(R.id.editText);
                        textCaixaText = text.getText().toString();
                        Toast.makeText(MainActivity.this, textCaixaText, Toast.LENGTH_SHORT).show();
                        alerta.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Escreva algo");
                builder.setView(view);
                alerta = builder.create();
                alerta.show();

            }
                //Toast.makeText(getApplicationContext(), "Abre a set text", Toast.LENGTH_SHORT).show();
        });

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ColorPicker colorPicker = new ColorPicker(MainActivity.this);
                colorPicker.setTitle("Selecione a cor");
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        Toast.makeText(getApplicationContext(), Integer.toHexString(color), Toast.LENGTH_SHORT).show();
                        toolbar.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel(){
                        // put code
                    }
                }).show();
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Abre a tela effects", Toast.LENGTH_SHORT).show();
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Abre a tela equalizer", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth ativado", Toast.LENGTH_SHORT).show();
                }
                else {
                Toast.makeText(getApplicationContext(), "Bluetooth não ativado, o app será encerrado", Toast.LENGTH_SHORT).show();
                    finish();
            } break;

            case SOLICITA_CONEXAO:
                if (resultCode == Activity.RESULT_OK) { // putaria da conexão bluetooth
                    MAC = data.getExtras().getString(ListaDispositivos.ENDERECO_MAC);
                    mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(MAC);
                    // Toast.makeText(getApplicationContext(), "MAC: " + MAC, Toast.LENGTH_SHORT).show();

                    try {
                        mSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(mUUID);
                        mSocket.connect();
                        conexao = true;
                        Toast.makeText(getApplicationContext(), "Conectado com: " + MAC, Toast.LENGTH_SHORT).show();
                    } catch (IOException erro) {
                        conexao = false;
                        Toast.makeText(getApplicationContext(), "Falha ao conectar com " + MAC, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Falha ao obter o MAC", Toast.LENGTH_SHORT).show();
                }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (conexao) {
                try {
                    mSocket.close();
                    conexao = false;
                    Toast.makeText(getApplicationContext(), "Bluetooth foi desconectado", Toast.LENGTH_SHORT).show();
                } catch (IOException erro) {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro" + erro, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Intent abreLista = new Intent(MainActivity.this, ListaDispositivos.class);
                startActivityForResult(abreLista, SOLICITA_CONEXAO);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
