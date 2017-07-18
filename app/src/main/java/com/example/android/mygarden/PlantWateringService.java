package com.example.android.mygarden;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.android.mygarden.provider.PlantContract;
import com.example.android.mygarden.utils.PlantUtils;


public class PlantWateringService extends IntentService {

    public static final String ACTION_WATER_PLANT = "com.example.android.mygarden.action.water_plants";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public PlantWateringService() {
        super("PlantWaterService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            String action = intent.getAction();
            if(ACTION_WATER_PLANT.equals(action)){
                handleWateringPlant();
            }

        }
    }

    public static void startWateringService(Context context) {
        Intent intent = new Intent(context, PlantWateringService.class);

        intent.setAction(ACTION_WATER_PLANT);

        context.startService(intent);
    }

    public void handleWateringPlant(){
        Uri PLANT_WATER_URI = PlantContract.PlantEntry.CONTENT_URI.buildUpon().appendPath(PlantContract.PATH_PLANTS).build();

        ContentValues contentValues = new ContentValues();

        long timeNow = System.currentTimeMillis();

        contentValues.put(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME, timeNow);

        getContentResolver().update(PLANT_WATER_URI,
                contentValues,
                PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME + ">?",
                new String[]{String.valueOf(timeNow - PlantUtils.MAX_AGE_WITHOUT_WATER)} );
    }
}
