package com.example.facedetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import com.google.android.gms.vision.face.Landmark
import kotlinx.android.synthetic.main.activity_face_landmarks.*

class FaceLandmarks : AppCompatActivity() {
    var bitmap: Bitmap? = null
    var sunglass: Bitmap? = null
    var cap: Bitmap? = null
    var canvas: Canvas? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_landmarks)
        bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.billgates)
        faceImgLandmark.setImageBitmap(bitmap)

        sunglass = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.sunglaes)
        cap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.cap)
        var tempBitmap = Bitmap.createBitmap(bitmap!!.width, bitmap!!.height, Bitmap.Config.RGB_565)
        canvas = Canvas(tempBitmap)
        canvas!!.drawBitmap(bitmap!!, 0F, 0F, null)

        btnLandmarkProcess.setOnClickListener {
            Log.d("TAG", "detectLandmark: "+"Call btnLandmarkProcess")
            var faceDector: FaceDetector = FaceDetector.Builder(applicationContext)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build()
            if (!faceDector.isOperational) {
                Toast.makeText(
                    this,
                    "Face Detector Could Not be set up to your device",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            var frame: Frame = Frame.Builder().setBitmap(bitmap).build()
            var facedata: SparseArray<Face> = faceDector.detect(frame)
            for (i in 0 until facedata.size()) {
                var face: com.google.android.gms.vision.face.Face  = facedata.valueAt(i)
                detectLandmark(face)
            }
        }
    }
    private fun detectLandmark(face: Face) {
        Log.d("TAG", "detectLandmark: "+"Call detectLandmark")
        for (landmark: Landmark in face.landmarks) {
            var cx: Int = landmark.position.x.toInt()
            var cy: Int = landmark.position.y.toInt()
            drawImageView(landmark.type, cx, cy)
        }
    }

    private fun drawImageView(type: Int, cx: Int, cy: Int) {
        Log.d("TAG", "detectLandmark: "+"Call drawImageView")
        if (type == Landmark.NOSE_BASE) {
            var scaleWidth: Int = cap!!.getScaledWidth(canvas)
            var scaleHeight: Int = cap!!.getScaledHeight(canvas)
            canvas!!.drawBitmap(
                cap!!,
                (cx - (scaleWidth / 2)).toFloat(),
                (cy - (scaleHeight * 2)).toFloat(),
                null
            )
            canvas!!.drawBitmap(
                sunglass!!,
                (cx - 500).toFloat(),
                (cy - (scaleHeight) + 120).toFloat(),
                null
            )
        }
    }
}