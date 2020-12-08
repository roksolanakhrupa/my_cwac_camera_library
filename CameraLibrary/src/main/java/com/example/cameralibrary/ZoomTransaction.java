package com.example.cameralibrary;

import android.hardware.Camera;
import android.hardware.Camera.OnZoomChangeListener;

/**
 * Class for processing a zoom request. Create an instance
 * of this by calling zoomTo() on a CameraFragment or
 * CameraView.
 */
final public class ZoomTransaction implements OnZoomChangeListener {
    private Camera camera;
    private int level;
    private Runnable onComplete=null;
    private OnZoomChangeListener onChange=null;

    /**
     * Local-only constructor. Please use zoomTo() on a
     * CameraFragment or CameraView to create your own
     * ZoomTransaction instances
     *
     * @param camera
     * @param level
     */
    ZoomTransaction(Camera camera, int level) {
        this.camera=camera;
        this.level=level;
    }

    /**
     * Call this to specify a Runnable to be executed when the
     * zoom operation is complete.
     *
     * @param onComplete
     *          a Runnable
     * @return the ZoomTransaction itself
     */
    public ZoomTransaction onComplete(Runnable onComplete) {
        this.onComplete=onComplete;

        return(this);
    }

    /**
     * Call this to specify an OnZoomChangeListener to be
     * notified of changes in the zoom level. For a smooth
     * zoom, this will be called for each change in the zoom
     * level. For a non-smooth zoom, this will be called once,
     * at the end.
     *
     * @param onChange
     *          the listener for zoom change events
     * @return the ZoomTransaction itself
     */
    public ZoomTransaction onChange(Camera.OnZoomChangeListener onChange) {
        this.onChange=onChange;

        return(this);
    }

    /**
     * Actually performs the zoom. If smooth zooming is
     * supported, the zoom begins but happens asynchronously.
     * If smoooth zooming is not supported, the zoom happens
     * immediately, and your callback objects
     * (OnZoomChangedListener, Runnable) are immediately
     * invoked.
     */
    public void go() {
        Camera.Parameters params=camera.getParameters();

        if (params.isSmoothZoomSupported()) {
            camera.setZoomChangeListener(this);
            camera.startSmoothZoom(level);
        }
        else {
            params.setZoom(level);
            camera.setParameters(params);
            onZoomChange(level, true, camera);
        }
    }

    /**
     * Cancels an outstanding smooth zoom request. Call this
     * after go() and before your onCompleted Runnable is
     * invoked.
     */
    public void cancel() {
        camera.stopSmoothZoom();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.hardware.Camera.OnZoomChangeListener#onZoomChange
     * (int, boolean, android.hardware.Camera)
     */
    @Override
    public void onZoomChange(int zoomValue, boolean stopped, Camera camera) {
        if (onChange != null) {
            onChange.onZoomChange(zoomValue, stopped, camera);
        }

        if (stopped && onComplete != null) {
            onComplete.run();
        }
    }
}
