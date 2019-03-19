# Choreographer - Android
Animate views easily by chaining animations, run them serially or in parallel.

![](https://github.com/ZachBublil/Choreographer/blob/master/demoGif.gif)

# Download
Download the latest AAR via Gradle:
```
implementation 'zachinio.choreographer:choreographer:1.0.3'
```

# How to use
To run animation serially
```
 Choreographer()
            .addAnimation(EnterAnimation(view, EnterAnimation.Direction.TOP, 560))
            .addAnimation(FadeAnimation(view,0.2f,560))
            .animate()
```
Use "addAnimationAsync" method to add animations asynchronously
```
  Choreographer()
            .addAnimation(EnterAnimation(view, EnterAnimation.Direction.TOP, 560))
            .addAnimation(FadeAnimation(otherView,0.2f,560))
            .addAnimationAsync(ScaleAnimation(otherView, 0.5f, 0.5f, 560))
            .animate()
```

To add delay between animations
```
   Choreographer()
            .addAnimation(EnterAnimation(view, EnterAnimation.Direction.TOP, 560))
            .wait(1900) // mills
            .addAnimation(FadeAnimation(otherView,0.2f,560))
            .addAnimationAsync(ScaleAnimation(otherView, 0.5f, 0.5f, 560))
            .animate()
```

# Customize animations
The library contains some basic animations, if you want to chain a custom animation you can do it by extending from Animation class of the library and perform the animation inside the "animate" method.

# Licensing
```
Copyright 2018 Zach Bublil

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```

