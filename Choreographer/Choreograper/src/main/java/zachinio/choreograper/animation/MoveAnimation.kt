package zachinio.choreograper.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import io.reactivex.Completable
import java.lang.ref.WeakReference

class MoveAnimation(
        private val viewWeak: WeakReference<View>,
        private val x: Int,
        private val y: Int,
        private val duration: Long) : Animation() {

    override fun animate(): Completable {
        return Completable.create {
            val objectAnimatorX = ObjectAnimator.ofFloat(viewWeak.get(), "x", x.toFloat())
            val objectAnimatorY = ObjectAnimator.ofFloat(viewWeak.get(), "y", y.toFloat())
            val animatorSet = AnimatorSet()
            animatorSet.duration = duration
            animatorSet.removeAllListeners()
            animatorSet.playTogether(objectAnimatorX, objectAnimatorY)
            animatorSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {

                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    it.onComplete()
                }
            })
            animatorSet.start()
        }
    }
}