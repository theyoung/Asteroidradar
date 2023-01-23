package com.udacity.asteroidradar.view.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.network.FetchState
import com.udacity.asteroidradar.database.entities.PictureOfDayEntity
import com.udacity.asteroidradar.database.model.Asteroid
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.view.AsteroidsAdapter
import timber.log.Timber

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)
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
//        setHasOptionsMenu(true)

        return binding.root
    }

    class ClickListener(private val listener: (item:Asteroid)->Unit) {
        fun click(item:Asteroid) = listener(item)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.main_overflow_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return true
//    }
}
