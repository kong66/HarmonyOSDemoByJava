package com.example.java_demo1;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Component;


public class MyAnimator {
    public  enum AnimationState{
        start,
        stop,
        cancel,
        pause,
        end,
        resume,
    }
    public interface  AnimationStateChange{
        void onStateChange(Animator animator, AnimationState state);
    }
    AnimationStateChange onStateChange;
    AnimatorValue ani;
    Component aniTarget;
    public MyAnimator(Component target, long dur, long delay, int loop, int type,
                       AnimatorValue.ValueUpdateListener update,
                       AnimationStateChange onState) {
        aniTarget = target;
        onStateChange = onState;
        ani = new AnimatorValue();
        ani.setDuration(dur);
        ani.setDelay(delay);
        ani.setLoopedCount(loop);
        ani.setCurveType(type);
        //设置动画过程
        ani.setValueUpdateListener(update);
        ani.setStateChangedListener(new Animator.StateChangedListener() {
            @Override
            public void onStart(Animator animator) {
                if(onStateChange !=null)
                    onStateChange.onStateChange(animator, AnimationState.start);
            }

            @Override
            public void onStop(Animator animator) {
                if(onStateChange !=null)
                    onStateChange.onStateChange(animator, AnimationState.stop);
            }

            @Override
            public void onCancel(Animator animator) {
                if(onStateChange !=null)
                    onStateChange.onStateChange(animator, AnimationState.cancel);
            }

            @Override
            public void onEnd(Animator animator) {
                if(onStateChange !=null)
                    onStateChange.onStateChange(animator, AnimationState.end);
            }

            @Override
            public void onPause(Animator animator) {
                if(onStateChange !=null)
                    onStateChange.onStateChange(animator, AnimationState.pause);
            }

            @Override
            public void onResume(Animator animator) {
                if(onStateChange !=null)
                    onStateChange.onStateChange(animator, AnimationState.resume);
            }
        });
    }

    public  AnimatorValue ani(){
        return  ani;
    }

}


