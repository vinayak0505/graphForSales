package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference dbRef;
    LineGraphSeries<DataPoint> series2,series;
    DateCalculator dateCalculator = new DateCalculator();
    DataPoint dataPoint[];

    private Spinner mTimeSpinner; // spinner reference
    private String mTime; // this is to store the data(String) that we got from creating spinner method
    private TextView spinnerText;// this is just for reference you can remove it
    SimpleDateFormat simpleDateFormat;
    String data = null;
    Double value[] = new Double[8];
    Double totalItem = 0.0;
    GraphView graph;
    int loop = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = 0; i<8;i++){
            value[i] = 0.0;
        }
        mTimeSpinner = (Spinner) findViewById(R.id.spinner_for_time);
        spinnerText = (TextView) findViewById(R.id.spinner_text_here);
        dbRef = FirebaseDatabase.getInstance().getReference();
        graph = (GraphView) findViewById(R.id.graph);
        /*
        for(String s : dateCalculator.getThisWeek()) {
            spinnerText.append(s+"\n");
            loop = 0;
            loop++;
        }

         */
        Query mQueryRef = dbRef.child("OrderMaster").orderByChild("orderDate");
        mQueryRef.addValueEventListener(valueEventListener);
        setupSpinner();
    }


    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mTimeSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mTimeSpinner.setOnItemSelectedListener(selectedListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            graph.removeAllSeries();
            for(String s : dateCalculator.getThisWeek()) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.getKey();
                    if(s.equals(snapshot.child(name).child("orderDate").getValue().toString())) {
                        value[loop] += Double.parseDouble(snapshot.child(name)
                                .child("netAmtDue")
                                .getValue().toString());
                        spinnerText.append(s + "    "+value[loop].toString() + "   " + loop + "\n");
                    }
                }
                loop++;
            }
            dataPoint = new DataPoint[] {
                    new DataPoint(0, value[0]),
                    new DataPoint(1, value[1]),
                    new DataPoint(2, value[2]),
                    new DataPoint(3, value[3]),
                    new DataPoint(4, value[4]),
                    new DataPoint(5, value[5]),
                    new DataPoint(6, value[6]),
            };
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoint);
            graph.addSeries(series);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };


    //spinner OnItemSelectedListener
    AdapterView.OnItemSelectedListener selectedListener= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selection = (String) parent.getItemAtPosition(position);
            if (!TextUtils.isEmpty(selection)) {
                mTime = selection;
                //spinnerText.setText(mTime);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}