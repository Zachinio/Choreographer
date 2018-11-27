package zachinio.choreograper.animation

import android.graphics.Point
import android.view.View
import android.view.ViewTreeObserver
import io.reactivex.Completable
import io.reactivex.subjects.SingleSubject
import zachinio.choreograper.Choreographer
import java.lang.ref.WeakReference

abstract class Animation {

    internal val viewPositionSingle: SingleSubject<Point> = SingleSubject.create()
    var async = false
    var wait: Long? = null

    companion object {
        fun create(
            viewWeak: WeakReference<View>,
            direction: Choreographer.Direction,
            animationType: Choreographer.AnimationType,
            duration: Long
        ): Animation {
            return when (animationType) {
                Choreographer.AnimationType.ENTER -> EnterAnimation(
                    viewWeak,
                    direction,
                    animationType,
                    duration
                )
                Choreographer.AnimationType.SCALE -> ScaleAnimation(
                    viewWeak,
                    direction,
                    animationType,
                    duration
                )
                Choreographer.AnimationType.FADE -> FadeAnimation(
                    viewWeak,
                    direction,
                    animationType,
                    duration
                )
            }
        }
    }

    internal abstract fun animate(): Completable

    internal fun getViewPosition(viewWeak: WeakReference<View>) {
        viewWeak.get()?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewWeak.get()?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                viewPositionSingle.onSuccess(Point(viewWeak.get()?.x?.toInt()!!, viewWeak.get()?.y?.toInt()!!))
            }
        })
    }
}