package ca.m3dia.iotuselessgoosebox;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import ca.m3dia.iotuselessgoosebox.lib.AnimatorPath;
import ca.m3dia.iotuselessgoosebox.lib.PathEvaluator;
import ca.m3dia.iotuselessgoosebox.lib.PathPoint;

/**
 * Created by umarb_000 on 2016-07-20.
 */
public class CustomFragment extends Fragment {
    private View mFab;
    private FrameLayout mFabContainer;
    private LinearLayout mControlsContainer;

    public final static float SCALE_FACTOR      = 13f;
    public final static int ANIMATION_DURATION  = 300;
    public final static int MINIMUN_X_DISTANCE  = 200;

    private boolean mRevealFlag;
    private float mFabSize;

    private EditText nameEditText;
    private Spinner lidSpinner;
    private Spinner lidLedSpinner;
    private Spinner redLedSpinner;
    private Spinner armSpinner;
    private Spinner soundSpinner;
    Button addButton, cancelButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);

        mFabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);
        bindViews(view);

        //setup recycler view
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //setup adapter
        CustomListAdapter customListAdapter = new CustomListAdapter();
        //attach adapter to recycler view
        recyclerView.setAdapter(customListAdapter);

        //set LayoutManager for recyclerView. Use vertical list
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //attach layout manager to recyclerView
        recyclerView.setLayoutManager(layoutManager);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverseFabAnimation();
            }
        });

        return view;
    }

    private void bindViews(View view) {
        mFab = view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onFabPressed(v);
            }
        });

        mFabContainer = (FrameLayout) view.findViewById(R.id.fab_container);
        mControlsContainer = (LinearLayout) view.findViewById(R.id.add_custom_container);
        mFabContainer.bringToFront();

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
    }

    private void reverseFabAnimation() {
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
                                .scaleXBy(0)
                                .scaleYBy(0)
                                .setListener(mEndRevealListener)
                                .setDuration(ANIMATION_DURATION);


                        mFab.setVisibility(View.VISIBLE);

                        mFabContainer.setBackgroundColor(getResources()
                                .getColor(R.color.blue_white_text));


                        mControlsContainer.setPadding(0, -400, 0, 0);

                        for (int i = 0; i < mControlsContainer.getChildCount(); i++) {
                            View v = mControlsContainer.getChildAt(i);
                            ViewPropertyAnimator animator = v.animate()
                                    .scaleX(0).scaleY(0)
                                    .setDuration(ANIMATION_DURATION);

                            animator.setStartDelay(i * 50);
                            animator.start();
                        }

                    }
                }
            }
        });
    }

    public void onFabPressed(View view) {
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
