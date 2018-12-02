package zachinio.choreograper.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import io.reactivex.Completable
import java.lang.ref.WeakReference


class FadeAnimation(
    view: View,
    private val alpha: Float,
    private val duration: Long
) : Animation() {

    private val viewWeak = WeakReference(view)

    override fun animate(): Completable {
        return Completable.create {
            val alphaAnimation = ObjectAnimator.ofFloat(viewWeak.get(), "alpha", alpha)
            val animatorSet = AnimatorSet()
            animatorSet.duration = duration
            animatorSet.playTogether(alphaAnimation)
            animatorSet.addListener(object : AnimationListener(){
                override fun onAnimationEnd(p0: Animator?) {
                    it.onComplete()
                }
            })
            animatorSet.start()
        }
    }
}