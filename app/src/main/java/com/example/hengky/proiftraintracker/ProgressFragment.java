package com.example.hengky.proiftraintracker;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;


public class ProgressFragment extends Fragment implements LocationListener {

    private FragmentListener listener;
    MapsActivity context;
    TextView txt, selanjutnya, akhir;
    ChooseDestination dataStasiun;
    String stasiunSelanjutnya;
    String stasiunAkhir;
    //ChooseDestination dataLongitudeLatitude;
    int idxAwal = dataStasiun.indexStasiunAwal;
    int idxAkhir = dataStasiun.indexStasiunAkhir;
    int currIdx;
    double curLatitude, curLongitude; //lokasi dimana user berada
    double lat1, lat2, latLast ;
    double lng1, lng2, lngLast;
    TextView finalEstimation;
    TextView nextEstimation;
    //----------------------------------uji coba------------------------------
    String [] lokasiSeluruhnya = new String[]{"Platinum Kost","Hotel Harris Ciumbuleuit", "Pertamina gas station 34-40111", "Yellow Truck"};

    double [] testLatitude = new double []{-6.878156, -6.880751, -6.887657 };
    double [] testLongitude = new double []{107.606692, 107.604180, 107.602003};

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.on_progress_trip, container, false);
        this.txt = view.findViewById(R.id.velocity);
        this.selanjutnya = view.findViewById(R.id.stasiunSelanjutnya);
        this.akhir = view.findViewById(R.id.stasiunAkhir);
        this.finalEstimation =  view.findViewById(R.id.finalStationEstimation);
        this.nextEstimation = view.findViewById(R.id.nextStationEstimation);

        //this.currIdx = (idxAwal > idxAkhir)? (idxAwal -1) : (idxAwal+1);
        this.currIdx = 1;
        //dataStasiun = new ChooseDestination();
        stasiunSelanjutnya = lokasiSeluruhnya[currIdx];
        stasiunAkhir = lokasiSeluruhnya[lokasiSeluruhnya.length-1];

        selanjutnya.setText("Stasiun Selanjutnya: "+stasiunSelanjutnya);
        akhir.setText("Stasiun Akhir: "+stasiunAkhir);
        LocationManager locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        this.context.setNotification();
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        this.onLocationChanged(null);

        lat1=testLatitude[0];
        lng1=testLongitude[0];
        lat2 = testLatitude[currIdx];
        lng2 = testLongitude[currIdx];
        String lokasi2=lokasiSeluruhnya[currIdx];
        Log.d("test lokasi selanjutnya", lokasi2+ ": "+ String.valueOf(lat2)+" , "+String.valueOf(lng2));

        latLast = testLatitude[testLatitude.length-1];
        lngLast = testLongitude[testLongitude.length-1];
        String lokasi3=lokasiSeluruhnya[testLongitude.length-1];
        Log.d("test lokasi akhir", lokasi3+ ": "+ String.valueOf(latLast)+" , "+String.valueOf(lngLast));

        double totalDisRes = this.calculateDistance(lat1, lng1 , latLast,lngLast );
        double nextDisRes = this.calculateDistance(lat1, lng1, lat2, lng2);
        double estimasiStasiunSelanjutnya, estimasiStasiunAkhir;

        estimasiStasiunSelanjutnya = nextDisRes / 25;
        estimasiStasiunAkhir = totalDisRes / 25;

        nextEstimation.setText(String.format("Stasiun selanjutnya : %.2f", estimasiStasiunSelanjutnya)+" jam");
        finalEstimation.setText(String.format("Stasiun akhir: %.2f", estimasiStasiunAkhir)+" jam");

        return view;
    }

    public static ProgressFragment newInstance(String title){
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (MapsActivity) context;
        if (context instanceof FragmentListener) {
            listener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location==null){
            txt.setText("0.0 km/h");
        }
        else{
            float nCurrentSpeed = (float) (location.getSpeed() * 3.6);

            txt.setText(String.format("%.0f km/h", nCurrentSpeed));

            curLatitude = location.getLatitude();
            curLongitude = location.getLongitude();

            lat2 = testLatitude[currIdx];
            lng2 = testLongitude[currIdx];
            String lokasi2=lokasiSeluruhnya[currIdx];
            Log.d("test lokasi selanjutnya", lokasi2+ ": "+ String.valueOf(lat2)+" , "+String.valueOf(lng2));

            latLast = testLatitude[testLatitude.length-1];
            lngLast = testLongitude[testLongitude.length-1];
            String lokasi3=lokasiSeluruhnya[testLongitude.length-1];
            Log.d("test lokasi akhir", lokasi3+ ": "+ String.valueOf(latLast)+" , "+String.valueOf(lngLast));

            double totalDisRes = this.calculateDistance(curLatitude, curLongitude , latLast,lngLast );
            double nextDisRes = this.calculateDistance(curLatitude, curLongitude, lat2, lng2);
            double estimasiStasiunSelanjutnya, estimasiStasiunAkhir;
            if(nCurrentSpeed >= 25){
                estimasiStasiunSelanjutnya = nextDisRes / nCurrentSpeed;
                estimasiStasiunAkhir = totalDisRes / nCurrentSpeed;
            }
            else{
                estimasiStasiunSelanjutnya = nextDisRes / 25;
                estimasiStasiunAkhir = totalDisRes / 25;
            }

            nextEstimation.setText(String.format("Stasiun selanjutnya : %.2f", estimasiStasiunSelanjutnya)+" jam");
            finalEstimation.setText(String.format("Stasiun akhir : %.2f", estimasiStasiunAkhir)+" jam");


            //untuk memberi notifikasi saat sudah dekat stasiun

            if(calculateDistance(lat2,lng2 , curLatitude, curLongitude) < 0.1) { // if distance < 0.1 miles we take locations as equal
                setNotifSaatDekatStasiun(lokasiSeluruhnya[currIdx]);
                if(currIdx<2){
                    currIdx++;
                }
                else if(currIdx>dataStasiun.indexStasiunAkhir){
                    currIdx--;
                }
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



    public void setNotifSaatDekatStasiun(String stasiun){
        Intent intent =new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(this.getContext(), 0, intent, 0);
        Notification noti = new Notification.Builder(this.getContext())
                .setTicker("Train Tracker")
                .setContentTitle("Train Tracker")
                .setContentText("Sebentar lagi anda akan tiba di :" + stasiun)
                .setSmallIcon(R.drawable.train_icon)
                .setContentIntent(pIntent).getNotification();
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, noti);
        Vibrator v = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            v.vibrate(500);
        }
    }

}
