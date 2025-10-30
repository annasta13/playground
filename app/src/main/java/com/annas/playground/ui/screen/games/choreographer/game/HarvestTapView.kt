package com.annas.playground.ui.screen.games.choreographer.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Choreographer
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import com.annas.playground.R
import com.annas.playground.ui.screen.games.choreographer.game.model.HarvestObject
import com.annas.playground.ui.screen.games.choreographer.game.model.HarvestObject.Type.FFB
import com.annas.playground.ui.screen.games.choreographer.game.model.HarvestObject.Type.POW
import com.annas.playground.ui.screen.games.choreographer.game.model.ScoreEffect
import kotlin.random.Random

@Suppress("MagicNumber", "TooManyFunctions")
class HarvestTapView(context: Context, attrs: AttributeSet? = null) : View(context, attrs),
    Choreographer.FrameCallback {

    private lateinit var score: MutableState<Int>
    private lateinit var live: MutableState<Int>
    fun setState(score: MutableState<Int>, liveState: MutableState<Int>) {
        this.score = score
        this.live = liveState
    }

    private val bottomPadding = 0
    private val dragSpaceHeight = 50f.dpToPx()
    private val bottomGameUiArea = bottomPadding + dragSpaceHeight

    private val toneGen by lazy { ToneGenerator(AudioManager.STREAM_MUSIC, 100) }
    private val fruits = mutableListOf<HarvestObject>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val random = Random(System.currentTimeMillis())
    private val lock = Any()

    private var lastSpawnTime = 0L
    private val baseSpawnInterval = 800L // spawn every 0.8s
    private var lastFrameTime = 0L

    private var running = false

    private var ffbBitmap: Bitmap? = null
    private var cartBitmap: Bitmap? = null
    private var manBitmap: Bitmap? = null
    private var powBitmap: Bitmap? = null
    private var constraintBitmaps = mutableListOf<Bitmap>()

    private lateinit var bitmapPool: BitmapPool
    private val scoreEffects = mutableListOf<ScoreEffect>()
    private var speedMultiplier = 1f
    private var lastSpeedLevel = 0

    private var cartX = 0f
    private var cartY = 0f
    private var cartWidth = 70f.dpToPx()
    private var cartHeight = 54.75f.dpToPx()
    private var isDragging = false
    private var manX = 0f
    private var manY = 0f
    private var manHeight = 68f.dpToPx()
    private var manWidth = 32f.dpToPx()
    private fun Float.dpToPx(): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

    private var dragOffsetX = 0f

    // --- start game when view attached ---
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        bitmapPool = BitmapPool(context)
        getBitmaps()
        lastFrameTime = System.nanoTime()
        Choreographer.getInstance().postFrameCallback(this)
    }

    private fun getBitmaps() {
        if (cartBitmap == null) {
            cartBitmap = bitmapPool.get(R.drawable.ic_wheelbarrow)
            cartWidth = cartBitmap?.width?.toFloat() ?: cartWidth
            cartHeight = cartBitmap?.height?.toFloat() ?: cartWidth
        }

        if (powBitmap == null) {
            val powBmp = bitmapPool.get(R.drawable.ic_pow)
            val powSizePx = 80f.dpToPx().toInt()
            powBitmap = powBmp?.scale(powSizePx, powSizePx)
        }
        if (manBitmap == null) {
            manBitmap = bitmapPool.get(R.drawable.ic_wheelbarrow_man)
            manHeight = manBitmap?.height?.toFloat() ?: manHeight
            manWidth = manBitmap?.width?.toFloat() ?: manWidth
        }
        ffbBitmap = bitmapPool.get(R.drawable.ic_sawit_fruit)
        if (constraintBitmaps.isEmpty()) constraintBitmaps.addAll(bitmapPool.getConstraints())
    }

    // --- stop game when view detached ---
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        running = false
        bitmapPool.clear()
        Choreographer.getInstance().removeFrameCallback(this)
    }

    // --- called every frame (~60 fps) ---
    override fun doFrame(frameTimeNanos: Long) {
        if (!running) return

        val delta = (frameTimeNanos - lastFrameTime) / 1_000_000f // ms
        lastFrameTime = frameTimeNanos

        update(delta)
        invalidate()

        Choreographer.getInstance().postFrameCallback(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT)

        val dragSpaceTop = height - dragSpaceHeight - bottomPadding
        val left = 0f
        val right = width.toFloat()

        canvas.drawRect(
            /* left = */ left,
            /* top = */dragSpaceTop - 2,
            /* right = */right,
            /* bottom = */dragSpaceTop,
            /* paint = */paint.apply { color = Color.GREEN }
        )
        canvas.drawRect(
            /* left = */ left,
            /* top = */dragSpaceTop,
            /* right = */right,
            /* bottom = */(height - bottomPadding).toFloat(),
            /* paint = */paint.apply { color = Color.LTGRAY }
        )

        // Text setup
        paint.apply {
            color = Color.DKGRAY
            textSize = 42f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val dragSpaceCenterY = height - bottomPadding - (dragSpaceHeight / 2f)
        val text = "❮❮❮❮ Geser \uD83D\uDC46 untuk memainkan ❯❯❯❯"

        // Adjust for text baseline: use font metrics to center vertically
        val fm = paint.fontMetrics
        val textBaseline = dragSpaceCenterY - (fm.ascent + fm.descent) / 2f

        canvas.drawText(text, width / 2f, textBaseline, paint)
        synchronized(lock) {
            for (fruit in fruits) runCatching {
                val bmp = fruit.bitmap
                if (!bmp.isRecycled) {
                    canvas.drawBitmap(bmp, fruit.x, fruit.y, null)
                }
            }
        }

        paint.color = Color.BLACK
        paint.textSize = 14f
        cartBitmap?.let { canvas.drawBitmap(it, cartX, cartY, null) }
        manBitmap?.let { canvas.drawBitmap(it, manX, manY, null) }

        for (effect in scoreEffects) {
            val color = if (effect.value > 0) Color.GREEN else Color.RED
            paint.color = color
            paint.alpha = (effect.alpha * 255).toInt()
            paint.textSize = 24f.dpToPx()
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(
                if (effect.value > 0) "+${effect.value}" else effect.value.toString(),
                effect.x,
                effect.y,
                paint
            )
        }
        paint.alpha = 255 // reset alpha

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        manX = (width - width).toFloat()
        cartX = manX + manWidth
        manY = h - manHeight - bottomGameUiArea
        cartY = h - cartHeight - bottomGameUiArea
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (live.value > 0) {
                    isDragging = true
                    dragOffsetX = event.x - manX
                    resumeGame()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isDragging) runCatching {
                    manX =
                        (event.x - dragOffsetX).coerceIn(-manWidth, width - (cartWidth + manWidth))
                    cartX = manX + manWidth
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDragging = false
            }
        }
        return true
    }

    fun pauseGame() {
        running = false
        Choreographer.getInstance().removeFrameCallback(this)
    }

    fun resumeGame() {
        if (!running) {
            running = true
            lastFrameTime = System.nanoTime()
            Choreographer.getInstance().postFrameCallback(this)
        }
    }


    @Suppress("CyclomaticComplexMethod", "LongMethod")
    private fun update(delta: Float) {
        if (!running) return
        synchronized(lock) {
            val now = System.currentTimeMillis()
            val type = HarvestObject.Type.entries.toTypedArray().random()
            val spawnInterval = (baseSpawnInterval / speedMultiplier)
                .toLong()
                .coerceAtLeast(200L)
            if (now - lastSpawnTime > spawnInterval && type != POW) runCatching {
                val bitmap = when (type) {
                    FFB -> ffbBitmap
                    else -> constraintBitmaps.random()
                }
                bitmap?.let {
                    fruits.add(
                        HarvestObject(
                            x = random.nextInt(width - 100).toFloat(),
                            y = -100f,
                            size = 100f,
                            speed = random.nextInt(from = 4, until = 6).toFloat(),
                            type = type,
                            bitmap = it,
                            hitTimer = 0f
                        )
                    )
                }
                lastSpawnTime = now
            }

            val iterator = fruits.iterator()
            while (iterator.hasNext()) runCatching {
                val fruit = iterator.next()
                fruit.y += fruit.speed * speedMultiplier * (delta / 16f)

                // detect collision with cart
                val fruitLeft = fruit.x
                val fruitRight = fruit.x + fruit.size
                val fruitTop = fruit.y
                val fruitBottom = fruit.y + fruit.size
                val fruitCenterX = fruit.x + fruit.size / 2
                val cartTop = cartY


                val manTop = manY
                val cartBottom = cartY + cartHeight
                val manRight = manX + manWidth
                val manBottom = manY + manHeight

                val constraintTolerance =
                    (constraintBitmaps.firstOrNull()?.width?.toFloat() ?: 20f) * 50 / 100
                val cartRight = cartX + cartWidth + constraintTolerance
                val cartLeftTolerance = cartX - constraintTolerance

                val verticalManTolerance = (manBitmap?.width?.toFloat() ?: 50f) * 50 / 100
                val cartAndManCorrectTopArea = if (fruit.x in cartLeftTolerance..cartX) manTop
                else cartTop
                val inCartArea = fruitBottom >= cartAndManCorrectTopArea
                        && fruitCenterX in cartX..cartRight
                        && fruitTop <= cartBottom

                val collidingCart = fruitRight > cartX && fruitLeft < cartRight &&
                        fruitBottom > cartTop && fruitTop < cartBottom
                val collidingMan = fruitRight > manX && fruitLeft < manRight &&
                        fruitBottom > (manTop + verticalManTolerance) && fruitTop < manBottom


                if (inCartArea && fruit.type != HarvestObject.Type.POW) {
                    when (fruit.type) {
                        FFB -> {
                            setAndInflateScore(fruit)
                            iterator.remove()
                        }

                        else -> {
                            setAndInflateScore(fruit)
                            iterator.remove()
                        }
                    }
                } else if (collidingCart) {
                    iterator.remove()
                } else if (collidingMan) {
                    powBitmap?.let {
                        fruit.bitmap = it
                        fruit.type = HarvestObject.Type.POW
                        fruit.speed = 0f // stop falling
                        fruit.hitTimer = 0f // create a short display timer
                        fruit.x = fruitCenterX - (it.width / 2f)
                        fruit.y = fruitBottom - it.height
                        toneGen.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 300)
                        live.value -= 1
                        if (live.value == 0) pauseGame()
                    }
                }

                // Increment timer for POW hits
                if (fruit.type == HarvestObject.Type.POW) {
                    fruit.hitTimer += delta / 1000f // convert ms → s
                    if (fruit.hitTimer > 0.3f) iterator.remove() // show ~0.3s
                } else if (fruitBottom > height - dragSpaceHeight - bottomPadding) {
                    iterator.remove()
                }

                val scoreIterator = scoreEffects.iterator()
                while (scoreIterator.hasNext()) {
                    val effect = scoreIterator.next()
                    effect.y -= 0.5f * (delta / 16f) // move upward
                    effect.alpha -= 0.02f * (delta / 16f) // fade out
                    effect.lifetime += delta / 1000f

                    if (effect.alpha <= 0f || effect.lifetime > 1.2f) {
                        scoreIterator.remove()
                    }
                }
            }
        }
    }

    private fun setAndInflateScore(fruit: HarvestObject) {
        val isCorrect = fruit.type == HarvestObject.Type.FFB
        val scoreValue = if (isCorrect) +10 else -10
        val ringTone = if (isCorrect) ToneGenerator.TONE_PROP_ACK else ToneGenerator.TONE_PROP_NACK
        toneGen.startTone(ringTone, 150)
        score.value += scoreValue
        val textX = cartX + (cartWidth / 2f)
        val textY = cartY - 20f
        val currentLevel = score.value / 100
        if (currentLevel > lastSpeedLevel) {
            lastSpeedLevel = currentLevel
            speedMultiplier += 0.2f
            speedMultiplier = speedMultiplier.coerceAtMost(3f)
        }
        scoreEffects.add(
            ScoreEffect(
                value = scoreValue,
                x = textX,
                y = textY
            )
        )
    }

    fun resetGame() {
        running = false
        fruits.clear()
        scoreEffects.clear()
        score.value = 0
        isDragging = false
        dragOffsetX = 0f
        speedMultiplier = 1f
        lastSpawnTime = 0L
        lastFrameTime = 0L
        lastSpeedLevel = 0
    }

}


class BitmapPool(private val context: Context) {

    private val cache = mutableMapOf<Int, Bitmap>()

    fun get(@DrawableRes resId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, resId)
        val bmp = drawable?.toBitmap()
        return bmp?.run { cache.getOrPut(resId) { this@run } }
    }

    fun getConstraints(): List<Bitmap> {
        val constraintResIds = listOf(
            R.drawable.ic_durian,
            R.drawable.ic_grapes,
            R.drawable.ic_banana,
            R.drawable.ic_pineapple,
            R.drawable.ic_cherries,
            R.drawable.ic_strawberry,
            R.drawable.ic_watermelon,
            R.drawable.ic_tomato,
            R.drawable.ic_orange,
            R.drawable.ic_pear,
            R.drawable.ic_carrot,
        )
        val bitmaps = mutableListOf<Bitmap>()
        constraintResIds.forEach { resId ->
            get(resId)?.let { bitmaps.add(it) }
        }
        return bitmaps
    }

    fun clear() {
        cache.values.forEach { if (!it.isRecycled) it.recycle() }
        cache.clear()
    }
}
