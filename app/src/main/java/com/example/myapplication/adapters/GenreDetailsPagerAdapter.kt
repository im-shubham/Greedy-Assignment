import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import com.example.myapplication.fragments.TopAlbumsFragment
import com.example.myapplication.fragments.TopArtistsFragment
import com.example.myapplication.fragments.TopTracksFragment



class GenreDetailPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = arrayOf("Top Albums", "Top Tracks", "Top Artists")

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position]
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TopAlbumsFragment()
            1 -> TopTracksFragment()
            2 -> TopArtistsFragment()
            else -> throw IllegalStateException("Invalid tab position")
        }
    }
}


