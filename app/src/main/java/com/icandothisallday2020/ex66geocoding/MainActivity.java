package com.icandothisallday2020.ex66geocoding;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText etAddress,etLat,etLog;
    double lat,lat2,log,log2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAddress=findViewById(R.id.et_address);
        etLat=findViewById(R.id.et_lat);
        etLog=findViewById(R.id.et_log);
    }

    public void clickBtn(View view) {
        //주소->좌표(GeoCoding)
        String address=etAddress.getText().toString();

        //GeoCoding 작업 수행 객체 생성
        Geocoder geocoder=new Geocoder(this, Locale.KOREA);
        try {
            List<Address> addresses=geocoder.getFromLocationName(address,3);
            //최대 3개의 결과를 줌-maxResult:3
            //결과를 보여주는 다이얼로그
            StringBuffer buffer=new StringBuffer();
            for( Address t:addresses) buffer.append(t.getLatitude()+", "+t.getLongitude()+"\n");

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage(buffer.toString()).setPositiveButton("OK",null).create().show();

            //지도앱으로 보여주기위해 좌표값 멤버변수에 대입
            lat= addresses.get(0).getLatitude();
            log= addresses.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickBtn2(View view) {
        //좌표->주소(역지오 코딩)
        lat2=Double.parseDouble(etLat.getText().toString());
        log2=Double.parseDouble(etLog.getText().toString());

        Geocoder geocoder=new Geocoder(this,Locale.KOREA);

        try {
            List<Address> addresses=geocoder.getFromLocation(lat2,log2,3);
            StringBuffer buffer=new StringBuffer();
            for(Address t: addresses) {
                buffer.append(t.getCountryName()+"\n"+t.getPostalCode()+"\n"+t.getAddressLine(0)+"\n");//주소 1
                buffer.append(t.getAddressLine(1)+"\n");//주소2:없으면 null
                buffer.append(t.getAddressLine(2)+"\n");//주소2:없으면 null
                buffer.append("-------------------------\n");
            }
            new AlertDialog.Builder(this).setMessage(buffer.toString()).setPositiveButton("OK",null).create().show();
        } catch (IOException e) { e.printStackTrace();}
    }

    public void clickMap(View view) {
        //지도앱 실행
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri= Uri.parse("geo:"+lat+","+log+"?q="+lat+","+log);
        intent.setData(uri);
        startActivity(intent);//디바이스에 설치된 지도앱 실행
    }

    public void clickShow(View view) {
        Uri uri=Uri.parse("geo:"+lat2+","+log2+"?q="+lat2+","+log2+"&z=10");
        //z:zoom z=1:대한민국 전체 지도 1~최대25까지(지역에따라 최대값다름)
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);//action,data 줄여쓰기
        startActivity(intent);
    }
}
