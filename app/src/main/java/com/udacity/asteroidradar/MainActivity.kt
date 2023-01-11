package com.udacity.asteroidradar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.udacity.asteroidradar.api.network.NasaNetwork
import com.udacity.asteroidradar.api.network.PictureOfDayNetwork
import com.udacity.asteroidradar.database.entities.PictureOfDayEntity
import com.udacity.asteroidradar.main.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.d("Timber Logging Start")
        // 1. activity 기준 today viewmodel을 만든다
        //  - today
        //
//        viewModel.today.observe(this, Observer {
//            Timber.d("Today Data Observed = " + it.toString())
//        })
        // 2. today Database, DAO, Repository를 만든다
        //  - add today
        //
        // TODO 2. today data를 fragment에 표시한다
        //TODO 3. today Repository Action과 Retrofit을 연결한다
        //   - added today
        //   - internet이 없으면 캐쉬 데이터를 보여줄 수 있게 해야한다

        //TODO 4. today viewmodel을 observing 한다.
        //   - 피카소를 등록한다
        //   - 피카소와 이미지 뷰를 연동한다

        //TODO 5. list Database, DAO, Repository를 만든다
        //   - DB에 오늘 이후 데이터가 있는지 확인한다.
        //   - 없으면 오늘 부터 7일 후까지 DB에 저장해 놓는다
        //   - 오늘 이전 데이터는 삭제 한다

        //TODO 7. work manager를  등록하고 list를 1일 단위로 갱신하게 한다
        //   - 오늘 이전 데이터는 삭제한다

        //TODO 8. talkback 모드를 모든 화면에 반영한다. text, image 등
    }
}
