package zachinio.choreograper.animation

import android.graphics.Point
import android.view.View
import android.view.ViewTreeObserver
import io.reactivex.Completable
import io.reactivex.subjects.SingleSubject
import java.lang.ref.WeakReference

abstract class Animation {

    internal val viewPositionSingle: SingleSubject<Point> = SingleSubject.create()
    internal var async = false
    internal var wait: Long? = null

    abstract fun animate(): Completable

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