package com.example.aceleracion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TextView ejex;
    private TextView ejey;
    private TextView ejez;
    private TextView aceleracion;
    private String a, b, c;
    private float[] gravity = new float[3];
    final float ALPHA =  0.8f;
    private double accelerationCurrentValue;
    private double accelerationPreviousValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ejex = findViewById(R.id.tv_eje_x);
        ejey = findViewById(R.id.tv_eje_y);
        ejez = findViewById(R.id.tv_eje_z);
        aceleracion = findViewById(R.id.tv_acc);
        mSensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager = null;
        mAccelerometer = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values.clone();

        values = highPass(values[0], values[1], values[2]);
        a = values[0]+"";
        b = values[1]+"";
        c = values[2]+"";
        ejex.setText(a);
        ejey.setText(b);
        ejez.setText(c);

        accelerationCurrentValue = Math.sqrt((values[0] * values[0] + values[1] * values[1] + values[2] * values[2]));
        accelerationPreviousValue = accelerationCurrentValue;
        double changeInAcceleration = Math.abs(accelerationCurrentValue - accelerationPreviousValue);
        aceleracion.setText(": " + accelerationCurrentValue);


        //aca ejecutan lo de la suma de cuadrados para captar el desplazamiento
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
//METODO PARA HACER EL FILTRO
    private float[] highPass(float x, float y, float z)
    {
        float[] filteredValues = new float[3];
        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * x;
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * y;
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * z;
        filteredValues[0] = x - gravity[0];
        filteredValues[1] = y - gravity[1];
        filteredValues[2] = z - gravity[2];
        return filteredValues;
    }
//metodo multiplicacion de cuadrados
    //aca deben de poner y meterlo dentro de onchanged
}