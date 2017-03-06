package com.example.dayvi.ledglass;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    String textCaixaText = "Esse texto vai ser mostrado no óculos";
    int efeitoTexto = 1;
    private AlertDialog alerta;
    private int velocidade = 50, brilho = 75;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int SOLICITA_CONEXAO = 2;
    boolean conexao = false;
    private  static String MAC = null;
    BluetoothDevice mBluetoothDevice = null;
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothSocket mSocket = null;
    private static final UUID mUUID = UUID.fromString("28c36230-ff62-11e6-9598-0800200c9a66"); //foda
    int cor = Color.GRAY;

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
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

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
                String msg = null;
                if (!mBluetoothAdapter.isEnabled()) {
                    msg = "Bluetooth não ativado!";
                }
                else if (conexao) {
                    msg = "Enviando!";
                }
                else {
                    msg = "Falha ao enviar!";
                }
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = getLayoutInflater();
                final View view = li.inflate(R.layout.set_text, null);
                final TextView text = (TextView) view.findViewById(R.id.editText);

                text.setHint(textCaixaText);

                view.findViewById(R.id.img1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "imagem 1", Toast.LENGTH_SHORT).show();
                        efeitoTexto = 1;
                    }
                });

                view.findViewById(R.id.img2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "imagem 1", Toast.LENGTH_SHORT).show();
                        efeitoTexto = 2;
                    }
                });

                view.findViewById(R.id.img3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "imagem 1", Toast.LENGTH_SHORT).show();
                        efeitoTexto = 3;
                    }
                });

                view.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        alerta.dismiss();
                        setColorPicker(toolbar, fab);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //builder.setTitle("Escreva algo");
                builder.setView(view);
                alerta = builder.create();
                alerta.show();

            }
        });

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorPicker(toolbar, fab);
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Abre a tela effects", Toast.LENGTH_SHORT).show();

                LayoutInflater li3 = getLayoutInflater();
                final View view3 = li3.inflate(R.layout.effects, null);

                view3.findViewById(R.id.img1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "imagem 1", Toast.LENGTH_SHORT).show();
                        alerta.dismiss();
                        setColorPicker(toolbar, fab);
                    }
                });

                view3.findViewById(R.id.img2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "imagem 2", Toast.LENGTH_SHORT).show();
                        alerta.dismiss();
                    }
                });

                view3.findViewById(R.id.img3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "imagem 3", Toast.LENGTH_SHORT).show();
                        alerta.dismiss();
                    }
                });

                view3.findViewById(R.id.img4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "imagem 4", Toast.LENGTH_SHORT).show();
                        alerta.dismiss();
                    }
                });

                view3.findViewById(R.id.img5).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "imagem 5", Toast.LENGTH_SHORT).show();
                        alerta.dismiss();
                    }
                });

                AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
                //builder.setTitle("Escreva algo");
                builder3.setView(view3);
                alerta = builder3.create();
                alerta.show();

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
                //else {
                //Toast.makeText(getApplicationContext(), "Bluetooth não ativado, o app será encerrado", Toast.LENGTH_SHORT).show();
                   // finish();
            //}
                break;

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
            break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

        if (id == R.id.preferences_settings) {
            //preferencias
        }

        return super.onOptionsItemSelected(item);
    }

    public void setColorPicker(final Toolbar toolbar, final FloatingActionButton fab) {

        LayoutInflater li = getLayoutInflater();
        final View view2 = li.inflate(R.layout.velocidade_brilho_telinha, null);
        final SeekBar seekBrilho = (SeekBar) view2.findViewById(R.id.seekBar1);
        final SeekBar seekVelocidade = (SeekBar) view2.findViewById(R.id.seekBar);
        final ColorPicker corSelecionada = (ColorPicker) view2.findViewById(R.id.picker);
        final SaturationBar saturationBar = (SaturationBar) view2.findViewById(R.id.saturationbar);

        corSelecionada.addSaturationBar(saturationBar);

        corSelecionada.setOldCenterColor(cor);
        corSelecionada.setColor(cor);

        seekBrilho.setProgress(brilho);
        seekVelocidade.setProgress(velocidade);

        view2.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                velocidade = seekVelocidade.getProgress();
                brilho = seekBrilho.getProgress();
                cor = corSelecionada.getColor();
                //toolbar.setBackgroundColor(cor);
                fab.setBackgroundTintList(ColorStateList.valueOf(cor));
                alerta.dismiss();
            }
        });

        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
        //builder.setTitle("Escreva algo");
        builder2.setView(view2);
        alerta = builder2.create();
        alerta.show();

    }

}
