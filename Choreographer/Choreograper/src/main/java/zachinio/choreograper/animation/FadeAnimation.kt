package zachinio.choreograper.animation

import android.animation.Animator
import android.view.View
import io.reactivex.Completable
import java.lang.ref.WeakReference

class FadeAnimation(
    view: View,
    private val alpha:Float,
    private val duration: Long
) : Animation() {

    private val viewWeak = WeakReference(view)

    override fun animate(): Completable {
        return Completable.create {
            viewWeak.get()
                ?.animate()
                ?.setDuration(duration)
                ?.alpha(alpha)
                ?.setListener(object : AnimationListener(){
                    override fun onAnimationEnd(p0: Animator?) {
                        it.onComplete()
                    }
                })
        }
    }
    enum class Direction {
        OUT, IN
    }
}