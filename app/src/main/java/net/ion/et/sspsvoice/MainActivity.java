package net.ion.et.sspsvoice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GameAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.listview1);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        try {
            ApiHandler apiHandler = new ApiHandler(this);
            apiHandler.execute("/common/gameSchedule?sportsCd=2&matchStateCd=1");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private List<GameEntity> initFakeData() {

        List<GameEntity> gameEntities = new ArrayList<>();

        GameEntity entity = new GameEntity();
        entity.gameName = "[이벤트] 인기 직장 맞대결";
        entity.subGameName = "개구리 : 올챙이 맞대결";
        entity.startTime = "2020-02-01 13:00:00";
        entity.subGameId = 11;

        gameEntities.add(entity);

        return gameEntities;
    }

    public void test(String result) {

        try {
            JSONArray array = new JSONArray(result);
            List<GameEntity> gameEntities = new ArrayList<>();

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = (JSONObject)array.get(i);

                GameEntity entity = new GameEntity();
                entity.parse(object);

                gameEntities.add(entity);

            }

            mAdapter = new GameAdapter(gameEntities);

            RecyclerView recyclerView = findViewById(R.id.listview1);
            recyclerView.setAdapter(mAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
