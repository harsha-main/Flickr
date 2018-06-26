package harsha_main.github.flickr;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import static android.view.View.GONE;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    Context c;
    private Item[] mData;
    private LayoutInflater mInflater;
    private boolean expanded[];
    MyRecyclerViewAdapter(Context context, Item[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        c = context;
        expanded = new boolean[data.length / 2];

    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(c).load("https://farm" + mData[position * 2].farmid + ".staticflickr.com/" + mData[position * 2].serverid + "/" + mData[position * 2].imageid + "_" + mData[position * 2].secret + "_m.jpg")
                .into(holder.image1);
        Glide.with(c).load("https://farm" + mData[position * 2 + 1].farmid + ".staticflickr.com/" + mData[position * 2 + 1].serverid + "/" + mData[position * 2 + 1].imageid + "_" + mData[position * 2 + 1].secret + "_m.jpg")
               .into(holder.image2);


        holder.text.setVisibility(GONE);
        holder.image1.clearColorFilter();
        holder.image2.clearColorFilter();
        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.image2.clearColorFilter();
                expanded[position] = !expanded[position];
                if(!expanded[position])
                    holder.image1.clearColorFilter();
                else
                holder.image1.setColorFilter(Color.argb(100, 0, 0, 255));

                change(holder, position,holder.str1,1);
            }
        });
        holder.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.image1.clearColorFilter();
                expanded[position] = !expanded[position];
                if(!expanded[position])
                    holder.image2.clearColorFilter();
                else
                    holder.image2.setColorFilter(Color.argb(100, 0, 0, 255));
                change(holder, position,holder.str2,2);
            }
        });


    }

    void change(final ViewHolder holder, int position, final String str, int k) {

        holder.text.setText("Loading...");
        if (expanded[position])
            holder.text.setVisibility(View.VISIBLE);
        else
            holder.text.setVisibility(GONE);
        int add=0;
        if(k==2){add=1;}
        StringRequest request = new StringRequest("https://api.flickr.com/services/rest/?&method=flickr.photos.getInfo&api_key=4ef2fe2affcdd6e13218f5ddd0e2500d&photo_id="+mData[position*2+add].imageid, new Response.Listener<String>() {
            public void onResponse(String response) {
                String string="";
                String s[]=response.split("realname");
                if(s.length<2){error();return;}
                String info[]=s[1].split("\"");
                String t[]=response.split("<title>");
                if(t.length<2){error();return;}
                String title=t[1].split("<")[0];
                string+=title;
                if(info.length<4){error();return;}
                string+="\n Name: "+info[1] + "\n Location: "+info[3];
                string += "\nDate taken: "+response.split("taken=")[1].split("\"")[1];
               holder.text.setText(string);
            }
            void error(){
                Toast.makeText(c,"API Error, unable to fetch details",Toast.LENGTH_SHORT).show();
            }
        },

                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(c,"Some Error Occurred, unable to fetch details",Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(c);
        queue.add(request);
    }

    @Override
    public int getItemCount() {
        return mData.length / 2;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image1, image2;
        TextView text;
        String str1,str2;
        ViewHolder(View itemView) {
            super(itemView);
            image2 = itemView.findViewById(R.id.image2);
            text = itemView.findViewById(R.id.text);
            image1 = itemView.findViewById(R.id.image);

        }
    }
}