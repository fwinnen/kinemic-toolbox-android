# Kinemic Toolbox
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
        maven {
            url "https://dl.bintray.com/kinemic/maven"
        }
        google()
    }
}
```

Note: This is only needed until jcenter accepts our maven repo.

## build.gradle (module)
```groovy

android {
    compileSdkVersion 26 // make sure to use latest sdk
    ...
}

...

dependencies {

    ...
    
    implementation 'de.kinemic:toolbox:0.9.5'
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

# Support

If you encounter any problems or need help, please don't hesitate to contact us
at [support@kinemic.de](mailto:support@kinemic.de).
