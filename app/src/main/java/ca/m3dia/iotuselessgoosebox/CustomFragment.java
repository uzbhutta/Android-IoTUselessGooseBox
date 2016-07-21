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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
        mControlsContainer = (LinearLayout) view.findViewById(R.id.media_controls_container);
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
                        mFabContainer.setY(mFabContainer.getY() + mFabSize / 2);

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
