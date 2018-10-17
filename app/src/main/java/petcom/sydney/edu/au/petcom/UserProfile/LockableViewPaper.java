package petcom.sydney.edu.au.petcom.UserProfile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class LockableViewPaper extends ViewPager {

    private boolean swipeable;

    public LockableViewPaper(@NonNull Context context) {
        super(context);
    }

    public LockableViewPaper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.swipeable = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.swipeable) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.swipeable) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public void setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
    }
}
