package com.example.localizacao;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap map;

    private LocationManager locationManager;

    private LatLng localizacaoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Aqui verificamos a permissão para utilizar o gps
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Aqui verificamos a permissão para utilizar o gps
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                // Neste momento iniciamos o mapa chamando o metodo onMapReady
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

            } else {
                //Caso não tivermos permissão, iremos perguntar para o usuário se ele deseja permitir
                mostrarMensagem("É necessário permitir o uso de sua localização");

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 0);

            }
        } else {
            //Caso não tivermos permissão, iremos perguntar para o usuário se ele deseja permitir
            mostrarMensagem("É necessário permitir o uso de sua localização");

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 0);
        }

    }

    /**
     * Método responsável por preparar o mapa
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        map = googleMap;

        // Aqui habilitamos os controles de ZOOM no mapa
        map.getUiSettings().setZoomControlsEnabled(true);

        // Aqui habilitamos para visualizarmos a posição atual, se não o mapa não terá o ponto azul, ou seja, nossa posição atual
        map.setMyLocationEnabled(true);

        //Verificamos as permissões do GPS novamente
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Aqui recebemos a ultima localizacao obtida do usuário pela INTERNET
            Location localizacaoViaInternet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            // Aqui recebemos a ultima localizacao obtida do usuário pelo GPS
            Location localizacaoViaGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            
            //Verificamos se foi obtido a localização via INTERNET
            if (localizacaoViaInternet != null) {

                //Obtendo latitude e longitude
                double latitude = localizacaoViaInternet.getLatitude();
                double longitude = localizacaoViaInternet.getLongitude();
                localizacaoAtual = new LatLng(latitude, longitude);

            }
            //Verificamos se foi obtido a localização via GPS
            else if (localizacaoViaGPS != null) {

                //Obtendo latitude e longitude
                double latitude = localizacaoViaGPS.getLatitude();
                double longitude = localizacaoViaGPS.getLongitude();
                localizacaoAtual = new LatLng(latitude, longitude);

            }


            //Aqui marcamos o local do usuário com um Ponteiro
            map.addMarker(new MarkerOptions().position(localizacaoAtual).title("Sua Posicação!"));

            //Aqui movimentamos a tela para a posição atual do usuário com uma animação de ZOOM
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(localizacaoAtual, 17.5f));

        }
    }

    /**
     *  Metodo para mostrar uma mensagem ao usuário
     * @param msg
     */
    public void mostrarMensagem(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        toast.show();
    }

}
