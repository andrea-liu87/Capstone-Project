package com.andreasgift.petto.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.andreasgift.petto.MainActivity;
import com.andreasgift.petto.R;
import com.andreasgift.petto.data.PetContract;
import com.andreasgift.petto.data.PetProviderHelper;

public class FragmentAttributes extends Fragment implements View.OnClickListener {
    private int progressHungerBar = -1;
    private int progressCleanlinessBar= -1;
    private int progresssHappinessBar = -1;
    private final int maxValue = 10;

    private ImageButton hungerButton;
    private ImageButton cleanButton;
    private ImageButton playButton;

    private ProgressBar hunger;
    private ProgressBar clean;
    private ProgressBar happiness;

    //Mandatory empty constructor
    public FragmentAttributes(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attributes, container, false);
        hunger = view.findViewById(R.id.hunger_bar);
        clean = view.findViewById(R.id.cleanliness_bar);
        happiness = view.findViewById(R.id.happiness_bar);

        hungerButton = view.findViewById(R.id.hunger_button);
        cleanButton = view.findViewById(R.id.clean_button);
        playButton = view.findViewById(R.id.play_button);

        hungerButton.setOnClickListener(this);
        cleanButton.setOnClickListener(this);
        playButton.setOnClickListener(this);

        if (progressHungerBar > -1){
            hunger.setProgress(progressHungerBar);
            clean.setProgress(progressCleanlinessBar);
            happiness.setProgress(progresssHappinessBar);
        }
        checkStatusProgressBar();
        return view;
    }

    public void setAttributes(int hunger, int cleanliness, int happiness){
        progressHungerBar = hunger;
        progressCleanlinessBar = cleanliness;
        progresssHappinessBar = happiness;
    }

    private void checkStatusProgressBar (){
        String statusBar = "";
        if (progressHungerBar == 0 || progressCleanlinessBar == 0 || progresssHappinessBar == 0){
            Toast.makeText(getContext(), "RIP Choco. Your pet has died", Toast.LENGTH_LONG).show();
            PetProviderHelper.deleteAllPet(getContext().getContentResolver());
            return;
        }
        if (progressHungerBar < 3){
            statusBar = "Hunger status is critical";
        }
        if (progressCleanlinessBar < 3){
            statusBar = "Cleanliness status is critical";
        }
        if (progresssHappinessBar < 3){
            statusBar = "Happiness status is critical";
        }
        Toast.makeText(getContext(),statusBar, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hunger_button :
                int updateValue = PetProviderHelper.updateAttributes(getContext().getContentResolver(), PetContract.HUNGER_IND);
                if (updateValue > 0){
                    MainActivity.showHappyCat();
                }
                if (progressHungerBar < maxValue){
                    progressHungerBar += 1;
                    hunger.setProgress(progressHungerBar);}
                break;
            case R.id.clean_button :
                int updateValue2 = PetProviderHelper.updateAttributes(getContext().getContentResolver(), PetContract.CLEANLINESS_IND);
                if (updateValue2 > 0){
                    MainActivity.showHappyCat();
                }
                if (progressCleanlinessBar < maxValue){
                    progressCleanlinessBar += 1;
                    clean.setProgress(progressCleanlinessBar);}
                break;
            case R.id.play_button :
                int updateValue3 = PetProviderHelper.updateAttributes(getContext().getContentResolver(), PetContract.HAPPINESS_IND);
                if (updateValue3 > 0){
                    MainActivity.showHappyCat();
                }
                if (progresssHappinessBar < maxValue){
                    progresssHappinessBar += 1;
                    happiness.setProgress(progresssHappinessBar);}
                break;
        }
    }
}
