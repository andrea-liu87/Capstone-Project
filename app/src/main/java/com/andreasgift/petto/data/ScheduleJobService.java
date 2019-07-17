package com.andreasgift.petto.data;

import com.andreasgift.petto.data.PetContract;
import com.andreasgift.petto.data.PetProviderHelper;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class ScheduleJobService extends JobService {

    private Thread backgroundTask;
    @Override
    public boolean onStartJob(JobParameters job) {
        backgroundTask = new Thread(new Runnable() {
            @Override
            public void run() {
                if (job.getTag().equals(PetContract.PROVIDER_NAME)){
                    int attributes = job.getExtras().getInt(PetContract.ATTRIBUTES_KEY);
                    if (attributes > -1 && attributes <= 2) {
                        PetProviderHelper.reduceAttributes(getContentResolver(), attributes);
                    }
                }
            }
        });
        backgroundTask.start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (backgroundTask != null){
            backgroundTask.stop();
        }
        return true;
    }
}
