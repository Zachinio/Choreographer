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
                direction: Direction,
                animationType: AnimationType,
                duration: Long
        ): Animation {
            return when (animationType) {
                AnimationType.ENTER -> EnterAnimation(
                        viewWeak,
                        direction,
                        animationType,
                        duration
                )
                AnimationType.SCALE -> ScaleAnimation(
                        viewWeak,
                        direction,
                        animationType,
                        duration
                )
                AnimationType.FADE -> FadeAnimation(
                        viewWeak,
                        direction,
                        animationType,
                        duration
                )
                Animation.AnimationType.MOVE -> TODO()
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

    enum class Direction {
        TOP, RIGHT, BOTTOM, LEFT, UP, DOWN, OUT, IN
    }

    enum class AnimationType {
        ENTER, SCALE, FADE,MOVE
    }
}