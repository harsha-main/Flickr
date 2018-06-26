package harsha_main.github.flickr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    MyRecyclerViewAdapter adapter;
    Item data[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StringRequest request = new StringRequest("https://api.flickr.com/services/rest/?&method=flickr.people.getPublicPhotos&api_key=4ef2fe2affcdd6e13218f5ddd0e2500d&user_id=29096781@N02", new Response.Listener<String>() {
            public void onResponse(String response) {
                String res[] = response.split("photo ");
                data = new Item[res.length - 1];
                for (int i = 1; i < res.length; i++) {
                    String str[] = res[i].split(" ");
                    data[i - 1] = new Item(str[4].split("\"")[1], str[3].split("\"")[1], str[0].split("\"")[1], str[2].split("\"")[1]);

                }
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                adapter = new MyRecyclerViewAdapter(MainActivity.this, data);
                recyclerView.setAdapter(adapter);
            }
        },

                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Network Error Occured ", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

}