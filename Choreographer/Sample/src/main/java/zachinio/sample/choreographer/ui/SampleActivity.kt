package zachinio.sample.choreographer.ui

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import zachinio.choreograper.Choreographer
import zachinio.choreograper.animation.Animation
import zachinio.sample.choreographer.R


class SampleActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Choreographer.get()
                .addAnimation(helloWorld, Animation.Direction.TOP, Animation.AnimationType.ENTER, 560)
                .addAnimation(childView, Animation.Direction.DOWN, Animation.AnimationType.SCALE, 560)
                .wait(3000)
                .addAnimation(childView2, Animation.Direction.IN, Animation.AnimationType.FADE, 560)
                .animate()
    }
}