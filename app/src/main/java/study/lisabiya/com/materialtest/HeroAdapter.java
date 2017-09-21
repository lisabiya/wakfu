package study.lisabiya.com.materialtest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16.
 */

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.ViewHolder> {

    private Context mContext;

    private List<Hero> mHeroList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView heroImage;
        TextView heroName;

        public ViewHolder(View view) {
            super(view);
            cardView=(CardView)view;
            heroImage= (ImageView) view.findViewById(R.id.hero_image);
            heroName= (TextView) view.findViewById(R.id.hero_name);
        }
    }
    public HeroAdapter(List<Hero> heroList){
        mHeroList=heroList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.heros_item,
                parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Hero hero=mHeroList.get(position);
                Intent intent=new Intent(mContext,HeroActivity.class);
                intent.putExtra(HeroActivity.HERO_NAME,hero.getProfessional());
                intent.putExtra(HeroActivity.HERO_IMAGE_ID,hero.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hero hero=mHeroList.get(position);
        holder.heroName.setText(hero.getProfessional());
//        holder.heroImage.setImageResource(hero.getImageId());
        Glide.with(mContext).load(hero.getImageId()).into(holder.heroImage);
    }

    @Override
    public int getItemCount() {
        return mHeroList.size();
    }
}
