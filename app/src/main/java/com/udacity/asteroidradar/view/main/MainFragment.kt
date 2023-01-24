package com.udacity.asteroidradar.view.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.network.FetchState
import com.udacity.asteroidradar.database.entities.PictureOfDayEntity
import com.udacity.asteroidradar.database.model.Asteroid
import com.udacity.asteroidradar.database.model.repository.AsteroidsFilter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.view.AsteroidsAdapter

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application,
            MutableLiveData(AsteroidsFilter.WEEK)))[MainViewModel::class.java]
    }
    lateinit var today : LiveData<PictureOfDayEntity>
    private var retry = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.fetchState.observe(viewLifecycleOwner, Observer{
            when(it){
                FetchState.NORMAL -> ""
                FetchState.BAD_REQUEST -> Toast.makeText(context,"Bad Request",Toast.LENGTH_SHORT).show()
                FetchState.INTERNET_DISCONNECT -> Toast.makeText(context,"Internet Disconnected",Toast.LENGTH_SHORT).show()
                FetchState.TIMEOUT -> {
                    if(retry < 4) {
                        Toast.makeText(context,"Http Timeout Retry : " + retry,Toast.LENGTH_SHORT).show()
                        retry++
                        viewModel.reloadAsteroidList();
                    } else {
                        Toast.makeText(context,"Network error and No data, Restart the App",Toast.LENGTH_SHORT).show()
                    }
                }
                else -> ""
            }
        })
        val adapter = AsteroidsAdapter(ClickListener {
//            Toast.makeText(context, it.toString() + " clicked!!", Toast.LENGTH_SHORT).show()
            this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        viewModel.asteroids.observe(viewLifecycleOwner, Observer{
            adapter.asteroids = it ?: listOf()
            if(0 < it.size) binding.statusLoadingWheel.isGone = true;
        })
        adapter.asteroids = viewModel.asteroids.value ?: listOf()
        binding.asteroidRecycler.adapter = adapter


        //TODO 11. 옵션메뉴 삭제 및 재 작성
        setupMenu()
        
        return binding.root
    }

    private fun setupMenu() {
        
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.show_week_menu -> viewModel.updateFilter(AsteroidsFilter.WEEK)
                    R.id.show_today_menu -> viewModel.updateFilter(AsteroidsFilter.TODAY)
                    R.id.show_all_menu -> viewModel.updateFilter(AsteroidsFilter.ALL)
                }
                return true;
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    class ClickListener(private val listener: (item:Asteroid)->Unit) {
        fun click(item:Asteroid) = listener(item)
    }


}
