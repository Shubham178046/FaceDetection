package com.example.facedetection

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import android.widget.Toast
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var bitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.billgates)
        faceImg.setImageBitmap(bitmap)
        var paint: Paint = Paint()
        paint.strokeWidth = 5F
        paint.setColor(Color.RED)
        paint.style = Paint.Style.STROKE
        var tempBitmap = Bitmap.createBitmap(bitmap!!.width, bitmap!!.height, Bitmap.Config.RGB_565)
        var canvas: Canvas = Canvas(tempBitmap)
        canvas.drawBitmap(bitmap!!, 0F, 0F, null)

        btnProcess.setOnClickListener {
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
                var face: Face = facedata.valueAt(i)
                var x1: Float = face.position.x
                var y1: Float = face.position.y
                var x2: Float = x1 + face.width
                var y2: Float = y1 + face.height
                var rectF: RectF = RectF(x1, y1, x2, y2)
                canvas.drawRoundRect(rectF, 2F, 2F, paint)
            }
            faceImg.setImageDrawable(BitmapDrawable(resources, tempBitmap))
        }
    }
}