# Kinemic Toolbox
[ ![Download](https://api.bintray.com/packages/kinemic/maven/toolbox/images/download.svg?version=0.9.5) ](https://bintray.com/kinemic/maven/toolbox/0.9.5/link)

A Collection of classes, templates and utilities for Android, that help you interact with the [Kinemic Publisher](https://kinemic.de/evalpackage.html).

The easiest way to get started is to use our [Android Example](https://github.com/kinemic/kinemic-example-android) as a template for a new application.
If you want to integrate gesture interaction into your existing application, just follow the instructions below.

# Getting Started
To use the toolbox, you need to add our maven repository in your root projects build.gradle
and add this module as a dependency to your modules build.gradle.
## build.gradle (root)
```groovy
allprojects {
    repositories {
        jcenter()
        // This is the direct bintray repo, which can be ahead of jcenter sometimes
        // maven {
        //     url "https://dl.bintray.com/kinemic/maven"
        // }
        google()
    }
}
```

Note: This was only needed until jcenter accepted our maven repo.

## build.gradle (module)
```groovy

android {
    compileSdkVersion 26 // make sure to use latest sdk
    ...
}

...

dependencies {

    ...
    
    implementation 'de.kinemic:toolbox:0.9.6'
}

...

```

If you use a different sdk version, you have to exclude the support library which gets pulled in by this library.
```groovy

depenencies {
    ...
    compile('de.kinemic:toolbox:0.9.5', {
        exclude group: 'com.android.support', module: 'appcompat-v7'
        exclude group: 'com.android.support.constraint', module: 'constraint-layout'
    })
}

...

```

# Usage

To make use the gesture events (from the Kinemic Publisher running on the same device),
make your Activity extend our `GestureActivity` or `AdvancedGestureActivity` and override 
its `handleEvent(event)` method.

## Gestures
```java
import de.kinemic.toolbox.AdvancedGestureActivity;
import de.kinemic.toolbox.event.PublisherEvent;

MainActivity extends AdvancedGestureActivity {
    
    ...
    
    @Override
    protected void handleEvent(final PublisherEvent event) throws JSONException {
        switch(event.type) {
            case Gesture:
                PublisherEvent.Gesture gesture = event.asGesture();
                Log.d("Events", "Gesture: " + gesture.name);
                break;
        }
    }
}
```

## Airmouse

With the airmouse feature enabled, the publisher emits `MouseEvent`s.
These are always relative to a reference orientation.
This reference can be reset by calling `requestOrientationReset()`.
For best results, we recommend that you call this method right before you 
want to start using the `MouseEvent`s, when the user holds the hand still for some seconds.

## Layout Navigation

Version 0.9.6 adds first experimental support for android layout navigation. See [Android Example - LayoutActivity](https://github.com/kinemic/kinemic-example-android/blob/master/app/src/main/java/de/kinemic/example/gesturereceiver/LayoutActivity.java)
for a sample implementation.

To use the layout navigation, have your activity extend `de.kinemic.toolbox.GestureFocusActivity`.
You can specify a view subtree to be controllable via gesture by overriding `getRootGestureView()`. 
One view in this subtree will be focused and the focus can be propagated using gestures.
Swipe Up, Down, Left and Right propagate the focus in the respective direction.
Rotate RL performs a click on the currently selected item and Rotate LR performs a back button press.

With this structure it should be possible to extend a given Layout with gesture control.

# Development

To build and publish a new version change the libraryVersion in build.gradle and execute
```bash
./gradlew install
./gradlew bintrayUpload
```
This will build and upload a new artifact onto bintray.
From the web interface you can push it to jcenter. 


# Support

If you encounter any problems or need help, please don't hesitate to contact us
at [support@kinemic.de](mailto:support@kinemic.de).
