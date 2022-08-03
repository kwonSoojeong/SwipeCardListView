package com.crystal.cardlistview

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2
import com.crystal.cardlistview.databinding.ActivityMainBinding
import android.util.Log



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cardViewpager.apply {
            offscreenPageLimit = 1
            adapter = CardAdapter(
                mutableListOf(Store("이거 먹어볼래요?", "위치 정보", "맛잇는 음식점"),
                    Store("아주 아주 인기 많은 핫플레이스1", "위치 위치", "이름모를 음식점"),
                    Store("아주 아주 인기 많은 핫플레이스2", "위치 위치", "이름모를 음식점"),
                    Store("아주 아주 인기 많은 핫플레이스3", "위치 위치", "이름모를 음식점"),
                    Store("아주 아주 인기 많은 핫플레이스4", "위치 위치", "이름모를 음식점")
                    )
                )
            setPageTransformer(SliderTransformer())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                var prePosition = -1
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    Log.d("MainActivity", "scroll : position ${position}, offset : ${positionOffset}")
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state){
                        ViewPager2.SCROLL_STATE_IDLE -> {
                            if(prePosition < currentItem){
                                Log.d("MainActivity", "scroll :up")
                            }else if(prePosition > currentItem){
                                Log.d("MainActivity", "scroll : down")
                            }
                            prePosition = currentItem
                        }
                    }
                }
            })
        }
    }
}

class SliderTransformer : ViewPager2.PageTransformer {

    companion object {

        private const val SCALE_MAX = .85f
        private const val SCALE_DEFAULT = 1f
        private const val SCALE_FACTOR = .9f

        private const val ALPHA_FACTOR = .8f
        private const val ALPHA_DEFAULT = 1.0f

        private const val TOP_Y = .0f

        private const val CARD_GAP_DP = 20
    }

    override fun transformPage(page: View, position: Float) {
        val cardView = page.findViewById<CardView>(R.id.card_view)
        val cardViewHeight = cardView.height
        page.apply {
            when {
                position < 0.0f -> {
                    val scale = SCALE_DEFAULT + SCALE_FACTOR * position
                    if (scale < SCALE_MAX) {
                        scaleX = SCALE_MAX
                        scaleY = SCALE_MAX
                    } else {
                        scaleX = scale
                        scaleY = scale
                    }
                    alpha = ALPHA_DEFAULT + position * ALPHA_FACTOR
                    y = TOP_Y
                }
                position < 1.0f && position >= 0.0f -> {
                    scaleX = SCALE_DEFAULT
                    scaleY = SCALE_DEFAULT
                    alpha = ALPHA_DEFAULT
                    y = TOP_Y
                    translationY = position
                }
                else -> {
                    scaleX = SCALE_DEFAULT
                    scaleY = SCALE_DEFAULT
                    alpha = ALPHA_DEFAULT
                    val bottomGap = height - cardViewHeight
                    val cardGap = CARD_GAP_DP.dpToPx(context)

                    translationY = if (bottomGap < cardGap) {
                        position
                    } else {
                        position - bottomGap + cardGap
                    }
                }
            }
        }
    }
}

private fun Int.dpToPx(context: Context?): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context?.resources?.displayMetrics
    ).toInt()
}
