package zachinio.sample.choreographer.ui

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import zachinio.choreographer.Choreographer
import zachinio.choreographer.animation.*
import zachinio.sample.choreographer.R


class SampleActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Choreographer()
            .addAnimation(EnterAnimation(helloWorld, Direction.TOP, 560))
            .addAnimation(FadeAnimation(childView, 0.2f, 560))
            .addAnimationAsync(ScaleAnimation(childView, 0.5f, 0.5f, 560))
            .addAnimation(BounceAnimation(childView2, 50))
            .runSideEffect(Runnable {
                childView.setBackgroundColor(Color.BLACK)
            })
            .addAnimation(FadeAnimation(childView, 1f, 560))
            .addAnimation(MoveAnimation(helloWorld, 100, null, 500))
            .addAnimation(ProgressImageLoader(childView,500,Direction.BOTTOM))
            .animate()
    }
}