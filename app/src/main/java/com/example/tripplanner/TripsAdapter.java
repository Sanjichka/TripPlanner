package com.example.tripplanner;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;
    String userID;
    String name;
    String numTrips;
    public static final String TAG = "TAG";
    ArrayList<String> tripNamesArray;
    ArrayList<String> tripDatesArray;
    ArrayList<String> userIDArray;
    ArrayList<String> docIDArray;
    ArrayList<String> tripSeasonArray;
    ArrayList<String> tripReasonArray;

    public class TripsViewHolder extends RecyclerView.ViewHolder {
        public TextView myName;
        public TextView myDate;
        public TextView myReason;
        public TextView mySeason;
        public Button delete_btn;
        public Button open_btn;

        public ImageView Pic;
        public TripsViewHolder(View itemView) {
            super(itemView);
            myName = (TextView) itemView.findViewById(R.id.rowTripName);
            myDate = (TextView) itemView.findViewById(R.id.rowTripDate);
            myReason = (TextView) itemView.findViewById(R.id.rowTripReason);
            mySeason = (TextView) itemView.findViewById(R.id.rowTripSeason);
            delete_btn = (Button) itemView.findViewById(R.id.btnDelete);
            open_btn = (Button) itemView.findViewById(R.id.btnOpen);

            Pic = (ImageView) itemView.findViewById(R.id.picture);
        }
    }

    public TripsAdapter(Context context, ArrayList<String> userIDArray, ArrayList<String> docIDArray, ArrayList<String> tripNamesArray, ArrayList<String> tripDatesArray, ArrayList<String> tripSeasonArray, ArrayList<String> tripReasonArray, String name, String numTrips) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.tripDatesArray = tripDatesArray;
        this.tripReasonArray = tripReasonArray;
        this.tripNamesArray = tripNamesArray;
        this.tripSeasonArray = tripSeasonArray;
        this.docIDArray = docIDArray;
        this.userIDArray = userIDArray;
        this.name = name;
        this.numTrips = numTrips;
    }

    @NonNull
    @Override
    public TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.row_trips_list, parent, false);
        return new TripsViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        if(tripSeasonArray.get(position).equals("spring")) {
            viewHolder.Pic.setImageResource(R.drawable.spring);
        }
        else if(tripSeasonArray.get(position).equals("summer")) {
            viewHolder.Pic.setImageResource(R.drawable.summer);
        }
        else if(tripSeasonArray.get(position).equals("autumn")) {
            viewHolder.Pic.setImageResource(R.drawable.autumn);
        }
        else if(tripSeasonArray.get(position).equals("winter")) {
            viewHolder.Pic.setImageResource(R.drawable.winter);
        }

        viewHolder.myName.setText("Trip Name: " + tripNamesArray.get(position));
        viewHolder.mySeason.setText("Trip Season: " + tripSeasonArray.get(position));
        viewHolder.myReason.setText("Trip Reason: " + tripReasonArray.get(position));



        //countdown
        Calendar c = Calendar.getInstance();
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate, endDate;
        try {
            startDate = sdf.parse(today);
            endDate = sdf.parse(tripDatesArray.get(position));
            String b="";
            for(int i=0; i<tripDatesArray.get(position).length();i++) {
                if((i==0 && tripDatesArray.get(position).charAt(i)=='0') || (i==3 && tripDatesArray.get(position).charAt(i)=='0'))
                    continue;
                if(i!=(tripDatesArray.get(position).length()-4) && i!=(tripDatesArray.get(position).length()-3))
                    b += tripDatesArray.get(position).charAt(i);
            }
            long diff = endDate.getTime() - startDate.getTime();
            if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)==1) {
                viewHolder.myDate.setText("Trip Date: " + b + " | " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " day left");
            }
            else if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)==0) {
                viewHolder.myDate.setText("Trip Date: " + tripDatesArray.get(position) + " | Today!");
            }
            else if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)<0) {
                viewHolder.myDate.setText("Trip Date: Done " + Math.abs(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) + " days ago");
            }
            else {
                viewHolder.myDate.setText("Trip Date: " + b + " | in " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " days");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        root = db.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = userIDArray.get(position);


        viewHolder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, DetailsVolPerReqActivity.class);
//                mContext.startActivity(intent);

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete it?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        root.child("TripDetails-"+userID).child(docIDArray.get(position)).removeValue();

                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();

            }
        });


        viewHolder.open_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FilesListActivity.class);
                intent.putExtra("nameTrip", tripNamesArray.get(position));
                intent.putExtra("name", name);
                intent.putExtra("numTrips", numTrips);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return tripNamesArray.size();
    }


}


