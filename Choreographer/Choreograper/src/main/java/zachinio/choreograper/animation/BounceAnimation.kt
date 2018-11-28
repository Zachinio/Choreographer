package zachinio.choreograper.animation

import android.view.View
import android.view.animation.AnimationUtils
import io.reactivex.Completable
import zachinio.choreograper.R
import java.lang.ref.WeakReference

class BounceAnimation(view: View, private val duration: Long) : Animation() {

    private val viewWeak = WeakReference(view)

    override fun animate(): Completable {
        return Completable.create {
            val bounceAnimation = AnimationUtils.loadAnimation(viewWeak.get()?.context, R.anim.bounce)
            bounceAnimation.duration = duration
            bounceAnimation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationRepeat(p0: android.view.animation.Animation?) {

                }

                override fun onAnimationEnd(p0: android.view.animation.Animation?) {
                    it.onComplete()
                }

                override fun onAnimationStart(p0: android.view.animation.Animation?) {
                }
            })
            viewWeak.get()?.startAnimation(bounceAnimation)
        }
    }

}