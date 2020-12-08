package com.example.cameralibrary;



import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaRecorder;


public interface CameraHost extends Camera.AutoFocusCallback {

     enum RecordingHint {
        STILL_ONLY, VIDEO_ONLY, ANY, NONE
    }

     enum FailureReason {
        NO_CAMERAS_REPORTED(1), UNKNOWN(2);

        int value;

        private FailureReason(int value) {
            this.value=value;
        }
    }

    /**
     * Implement this to configure the Camera.Parameters just
     * prior to taking a photo.
     *
     * @param parameters
     *          the Camera.Parameters to be modified
     * @return the Camera.Parameters that was passed in
     */
    Camera.Parameters adjustPictureParameters(PictureTransaction xact, Camera.Parameters parameters);

    /**
     * Implement this to configure the Camera.Parameters for
     * the purposes of the preview. Note that you will have
     * another chance to configure the Camera.Parameters for a
     * specific photo via adjustPictureParameters().
     *
     * @param parameters
     *          the Camera.Parameters to be modified
     * @return the Camera.Parameters that was passed in
     */
    Camera.Parameters adjustPreviewParameters(Camera.Parameters parameters);

    /**
     * This will be called by the library to let you know that
     * auto-focus is available for your use, so you can update
     * your UI accordingly.
     */
    void autoFocusAvailable();

    /**
     * This will be called by the library to let you know that
     * auto-focus is not available for your use, so you can
     * update your UI accordingly.
     */
    void autoFocusUnavailable();

    /**
     * This will be called by the library to give you a chance
     * to configure the audio of the MediaRecorder, just prior
     * to beginning to record a video. Please ONLY configure
     * audio here.
     *
     * @param cameraId
     *          the camera that will be used for recording
     * @param recorder
     *          the MediaRecorder to be configured
     */
    void configureRecorderAudio(int cameraId, MediaRecorder recorder);

    /**
     * This will be called by the library to give you a chance
     * to configure the output of the MediaRecorder, just
     * prior to beginning to record a video. Please ONLY
     * configure output here.
     *
     * @param cameraId
     *          the camera that will be used for recording
     * @param recorder
     *          the MediaRecorder to be configured
     */
    void configureRecorderOutput(int cameraId, MediaRecorder recorder);

    /**
     * This will be called by the library to give you a chance
     * to configure the profile of the MediaRecorder, just
     * prior to beginning to record a video. Please ONLY
     * configure profile here.
     *
     * @param cameraId
     *          the camera that will be used for recording
     * @param recorder
     *          the MediaRecorder to be configured
     */
    void configureRecorderProfile(int cameraId, MediaRecorder recorder);

    /**
     * @return the ID of the camera that you want to use for
     *         previews and picture/video taking with the
     *         associated CameraView instance
     */
    int getCameraId();

    /**
     * @return the DeviceProfile to use for custom-tailoring
     *         the behavior of CameraView, to overcome
     *         device-specific idiosyncrasies
     */
    DeviceProfile getDeviceProfile();

    /**
     * Called to allow you to be able to indicate what size
     * photo should be taken.
     *
     * @param parameters
     *          the current camera parameters
     * @return the size of photo to take (note: must be a
     *         supported size!)
     */
    Camera.Size getPictureSize(PictureTransaction xact, Camera.Parameters parameters);

    /**
     * Called to allow you to indicate what size preview
     * should be used
     *
     * @param displayOrientation
     *          orientation of the display in degrees
     * @param width
     *          width of the available preview space
     * @param height
     *          height of the available preview space
     * @param parameters
     *          the current camera parameters
     * @return the size of the preview to use (note: must be a
     *         supported preview size!)
     */
    Camera.Size getPreviewSize(int displayOrientation, int width,
                               int height, Camera.Parameters parameters);

    /**
     * Same as getPreviewSize(), but called when we anticipate
     * taking videos, as some devices may work better with
     * lower-resolution previews, to reduce CPU load
     *
     * @param displayOrientation
     *          orientation of the display in degrees
     * @param width
     *          width of the available preview space
     * @param height
     *          height of the available preview space
     * @param parameters
     *          the current camera parameters
     * @param deviceHint
     *          the size that the device itself thinks should
     *          be used for video, which sometimes is
     *          ridiculously low
     * @return the size of the preview to use (note: must be a
     *         supported preview size!)
     */
    Camera.Size getPreferredPreviewSizeForVideo(int displayOrientation,
                                                int width,
                                                int height,
                                                Camera.Parameters parameters,
                                                Camera.Size deviceHint);

    /**
     * @return the Camera.ShutterCallback to be used with the
     *         camera, for sound effects and such
     */
    Camera.ShutterCallback getShutterCallback();

    /**
     * Called when something blows up in CameraView, to allow
     * you to alert the user as you see fit
     *
     * @param e
     *          an Exception indicating what went wrong
     */
    void handleException(Exception e);

    /**
     * @return true if you want the saved output to be
     *         mirrored when using the front-facing camera,
     *         false to leave it alone
     */
    boolean mirrorFFC();

    /**
     * Called when a picture has been taken. This will be
     * called on a background thread.
     *
     * @param bitmap
     *          Bitmap of the picture
     */
    void saveImage(PictureTransaction xact, Bitmap bitmap);

    /**
     * Called when a picture has been taken. This will be
     * called on a background thread.
     *
     * passes image orientation
     *
     * @param image
     *          byte array of the picture data (e.g., JPEG)
     */
    void saveImage(PictureTransaction xact, byte[] image, int imageOrientation);

    /**
     * @return true if you want the camera to keep the preview
     *         disabled after taking a picture (e.g., you want
     *         to present the picture to the user for editing
     *         or processing), false if you want preview to be
     *         re-enabled (e.g., you want the user to be able
     *         to take another picture right away)
     */
    boolean useSingleShotMode();

    /**
     * @return a RecordingHint value indicating what you
     *         intend to use the camera for
     */
    RecordingHint getRecordingHint();

    /**
     * Called when we failed to open the camera for one reason
     * or another, so you can let the user know
     *
     * @param reason
     *          a FailureReason indicating what went wrong
     */
    void onCameraFail(FailureReason reason);

    boolean useFullBleedPreview();

    float maxPictureCleanupHeapUsage();
}

