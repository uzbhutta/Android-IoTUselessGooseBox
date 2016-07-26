package ca.m3dia.iotuselessgoosebox;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeTransform;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import ca.m3dia.iotuselessgoosebox.lib.AnimatorPath;
import ca.m3dia.iotuselessgoosebox.lib.PathEvaluator;
import ca.m3dia.iotuselessgoosebox.lib.PathPoint;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;

/**
 * Created by Umar Bhutta.
 */
public class CustomFragment extends Fragment {
    private static final String TAG = CustomFragment.class.getSimpleName();
    public static String CUSTOM_NAME = "CUSTOM_NAME";
    public static String CUSTOM_INFO = "CUSTOM_INFO";
    public static String CUSTOM_LETTERS = "CUSTOM_LETTERS";

    public static ArrayList<String> name = new ArrayList<>();
    public static ArrayList<String> info = new ArrayList<>();
    public static ArrayList<String> letters = new ArrayList<>();
    private String json = "";

    private ImageButton mFab;
    private FrameLayout mFabContainer;
    private LinearLayout mControlsContainer;
    private RelativeLayout mRelativeLayout;

    private EditText nameEditText;
    Button addButton, cancelButton;
    private Spinner lidSpinner;
    private Spinner lidLedSpinner;
    private Spinner redLedSpinner;
    private Spinner armSpinner;
    private Spinner soundSpinner;
    private TextView toggleCustomTextView;

    ParticleDevice currDevice;

    public final static float SCALE_FACTOR      = 13f;
    public final static int ANIMATION_DURATION  = 300;
    public final static int MINIMUN_X_DISTANCE  = 200;

    private boolean mRevealFlag;
    private float mFabSize;

    private String lidAction;
    private String lidLedAction;
    private String redLedAction;
    private String armAction;
    private String soundAction;

    private String customName;
    private String customInfo;
    private String customLetters;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);



        mFabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);
        bindViews(view);
        setupList(view);

        ParticleDeviceSetupLibrary.init(getContext().getApplicationContext(), MainActivity.class);

        return view;
    }

    private void setupList(View view) {
        //setup recycler view
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //setup adapter
        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity());
        //attach adapter to recycler view
        recyclerView.setAdapter(customListAdapter);

        //set LayoutManager for recyclerView. Use vertical list
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //attach layout manager to recyclerView
        recyclerView.setLayoutManager(layoutManager);
    }

    private void bindViews(final View view) {
        mFab = (ImageButton) view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onFabPressed(v);
            }
        });

        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        mFabContainer = (FrameLayout) view.findViewById(R.id.fab_container);
        mControlsContainer = (LinearLayout) view.findViewById(R.id.add_custom_container);
        mFabContainer.bringToFront();
        toggleCustomTextView = (TextView) view.findViewById(R.id.toggleCustomTextView);
        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        addButton = (Button) view.findViewById(R.id.addButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        lidSpinner = (Spinner) view.findViewById(R.id.lidSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> lidAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.lid_spinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        lidAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the lidAdapter to the spinner
        lidSpinner.setAdapter(lidAdapter);

        lidLedSpinner = (Spinner) view.findViewById(R.id.lidLedSpinner);
        ArrayAdapter<CharSequence> lidLedAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.lid_led_spinner, android.R.layout.simple_spinner_item);
        lidLedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lidLedSpinner.setAdapter(lidLedAdapter);

        redLedSpinner = (Spinner) view.findViewById(R.id.redLedSpinner);
        ArrayAdapter<CharSequence> redLedAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.red_led_spinner, android.R.layout.simple_spinner_item);
        redLedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        redLedSpinner.setAdapter(redLedAdapter);

        armSpinner = (Spinner) view.findViewById(R.id.armSpinner);
        ArrayAdapter<CharSequence> armAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.arm_spinner, android.R.layout.simple_spinner_item);
        armAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        armSpinner.setAdapter(armAdapter);

        soundSpinner = (Spinner) view.findViewById(R.id.soundSpinner);
        ArrayAdapter<CharSequence> soundAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sound_spinner, android.R.layout.simple_spinner_item);
        soundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundSpinner.setAdapter(soundAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonPressed(view);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelButtonPressed(view);
            }
        });

        toggleCustomTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToggleCustomPressed();
            }
        });

    }

    private void onToggleCustomPressed() {
        for (int i = 0; i < name.size(); i++) {
            Log.d(TAG, i + ". " + name.get(i));
        }

        for (int i = 0; i < info.size(); i++) {
            Log.d(TAG, i + ". " + info.get(i));
        }

        //Create json from letters ArrayList
        json = "{\"type\":1, \"data\":[";

        for(String member : letters) {
            json += "\"" + member + "\",";
        }

        json = json.substring(0, json.length() - 1);
        json += "]}";

        new Thread() {
            @Override
            public void run() {
                // Make the Particle call here

                ArrayList<String> jsonList = new ArrayList<>();
                ArrayList<String> toggleType = new ArrayList<>();
                jsonList.add(json);
                jsonList.add("CUSTOM");

                try {
                    ParticleCloudSDK.getCloud().logIn("umar.bhutta@hotmail.com", "560588123rocks");
                    currDevice = ParticleCloudSDK.getCloud().getDevice("1e003d001747343337363432");

                    int resultCode = currDevice.callFunction("toggleType", toggleType);
                    currDevice.callFunction("jsonParser", jsonList);

                    //capture resultCode from particle function to toast to user
                    if (resultCode == 1) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(), "Custom Mode - action list updated.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(), "Failed to enable Custom Mode and update action list.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (ParticleCloudException | ParticleDevice.FunctionDoesNotExistException | IOException e) {
                    e.printStackTrace();
                }
                jsonList.clear();
                toggleType.clear();
            }
        }.start();
    }

    private void onCancelButtonPressed(final View view) {
        reverseAnimation(view);
    }

    private void reverseAnimation(final View view) {
        ViewGroup transitionRoot = mRelativeLayout;

        Scene originalScene = Scene.getSceneForLayout(transitionRoot, R.layout.fragment_custom, view.getContext());

        originalScene.setEnterAction(new Runnable() {
            @Override
            public void run() {
                bindViews(view);
                mFab.setImageDrawable(null);
                setupList(view);

                mRevealFlag = false;
            }
        });

        TransitionManager.go(originalScene, new ChangeTransform());

        //TODO: Animate this in
        Drawable plus = getResources().getDrawable(R.drawable.ic_add_black_24dp);
        mFab.setImageDrawable(plus);
    }

    private void onAddButtonPressed(final View view) {
        mFab.setImageDrawable(null);

        //reset buffer
        customLetters = "";

        lidAction = lidSpinner.getSelectedItem().toString();
        lidLedAction = lidLedSpinner.getSelectedItem().toString();
        redLedAction = redLedSpinner.getSelectedItem().toString();
        armAction = armSpinner.getSelectedItem().toString();
        soundAction = soundSpinner.getSelectedItem().toString();

        customName = nameEditText.getText().toString();
        customInfo = lidAction + ", " + lidLedAction + ", " + redLedAction + ", " +
                armAction + ", " + soundAction;

        name.add(customName);
        info.add(customInfo);

        switch(lidAction) {
            case "Normal":
                customLetters = "A";
                break;
            case "Fast":
                customLetters = "B";
                break;
            case "Slow":
                customLetters = "C";
                break;
            case "Shake":
                customLetters = "D";
                break;
            default:
                customLetters = "A";
                break;
        }

        switch(lidLedAction) {
            case "On":
                customLetters += "A";
                break;
            case "Delayed On":
                customLetters += "B";
                break;
            case "Off":
                customLetters += "C";
                break;
            case "Flicker":
                customLetters += "D";
                break;
            default:
                customLetters += "C";
                break;
        }

        switch(redLedAction) {
            case "On":
                customLetters += "A";
                break;
            case "Delayed On":
                customLetters += "B";
                break;
            case "Off":
                customLetters += "C";
                break;
            case "Flicker":
                customLetters += "D";
                break;
            default:
                customLetters += "C";
                break;
        }

        switch(armAction) {
            case "Normal":
                customLetters += "A";
                break;
            case "Fast":
                customLetters += "B";
                break;
            case "Slow":
                customLetters += "C";
                break;
            case "Shake":
                customLetters += "D";
                break;
            default:
                customLetters += "A";
                break;
        }

        switch(soundAction) {
            case "On":
                customLetters += "A";
                break;
            case "Off":
                customLetters += "B";
                break;
            default:
                customLetters += "B";
                break;
        }

        letters.add(customLetters);

        reverseAnimation(view);
    }


    public void onFabPressed(View view) {
        mFab.setImageDrawable(null);
        final float startX = mFab.getX();

        AnimatorPath path = new AnimatorPath();
        path.moveTo(0, 0);
        path.curveTo(-200, 200, -400, 100, -600, 50);

        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "fabLoc",
                new PathEvaluator(), path.getPoints().toArray());

        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(ANIMATION_DURATION);
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (Math.abs(startX - mFab.getX()) > MINIMUN_X_DISTANCE) {
                    if (!mRevealFlag) {
                        mFabContainer.setY(mFabContainer.getY() + mFabSize - 17);


                        mFab.animate()
                                .scaleXBy(SCALE_FACTOR)
                                .scaleYBy(SCALE_FACTOR)
                                .setListener(mEndRevealListener)
                                .setDuration(ANIMATION_DURATION);



                        mRevealFlag = true;
                    }
                }
            }
        });
    }

    private AnimatorListenerAdapter mEndRevealListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mFab.setVisibility(View.INVISIBLE);

            mFabContainer.setBackgroundColor(getResources()
                    .getColor(R.color.brand_accent));


            mControlsContainer.setPadding(0, -400, 0, 0);

            for (int i = 0; i < mControlsContainer.getChildCount(); i++) {
                View v = mControlsContainer.getChildAt(i);
                ViewPropertyAnimator animator = v.animate()
                        .scaleX(1).scaleY(1)
                        .setDuration(ANIMATION_DURATION);

                animator.setStartDelay(i * 50);
                animator.start();
            }
        }
    };

    /**
     * We need this setter to translate between the information the animator
     * produces (a new "PathPoint" describing the current animated location)
     * and the information that the button requires (an xy location). The
     * setter will be called by the ObjectAnimator given the 'fabLoc'
     * property string.
     */
    public void setFabLoc(PathPoint newLoc) {
        mFab.setTranslationX(newLoc.mX);

        if (mRevealFlag)
            mFab.setTranslationY(newLoc.mY - (mFabSize / 2));
        else
            mFab.setTranslationY(newLoc.mY);
    }
}
