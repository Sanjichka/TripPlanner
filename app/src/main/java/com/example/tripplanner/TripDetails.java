package com.example.tripplanner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TripDetails {

    public String userID;
    public String docID;
    public String name;
    public String date;
    public String season;
    public String reason;

}
