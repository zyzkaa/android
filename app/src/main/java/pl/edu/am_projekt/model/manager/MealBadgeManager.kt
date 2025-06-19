import android.view.View
import androidx.annotation.OptIn
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import java.lang.ref.WeakReference

object MealBadgeManager {
    private var badgeDrawable: BadgeDrawable? = null
    private var fabRef: WeakReference<View>? = null

    @OptIn(ExperimentalBadgeUtils::class)
    fun attachBadge(fab: View) {
        fabRef = WeakReference(fab)
        val context = fab.context

        if (badgeDrawable == null) {
            badgeDrawable = BadgeDrawable.create(context)
        }

        BadgeUtils.attachBadgeDrawable(badgeDrawable!!, fab)
    }

    fun setMealCount(count: Int) {
        badgeDrawable?.apply {
            number = count
            isVisible = count > 0
        }
    }

    fun increment() {
        val current = badgeDrawable?.number ?: 0
        setMealCount(current + 1)
    }
    fun decrement() {
        val current = badgeDrawable?.number ?: 0
        setMealCount(current - 1)
    }


    fun clear() {
        badgeDrawable?.clearNumber()
        badgeDrawable?.isVisible = false
    }

    @OptIn(ExperimentalBadgeUtils::class)
    fun detach() {
        fabRef?.get()?.let {
            BadgeUtils.detachBadgeDrawable(badgeDrawable!!, it)
        }
        fabRef?.clear()
        fabRef = null
        badgeDrawable = null
    }
}
