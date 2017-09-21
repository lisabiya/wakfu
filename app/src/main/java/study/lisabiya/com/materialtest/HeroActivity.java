package study.lisabiya.com.materialtest;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static study.lisabiya.com.materialtest.R.id.fbt_comment;

/**
 * Created by Administrator on 2017/9/17.
 */

public class HeroActivity extends AppCompatActivity {

    public static final String HERO_NAME="hero_name";
    public static final String HERO_IMAGE_ID="hero_image_id";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_layout);
        Intent intent=getIntent();
        String heroName=intent.getStringExtra(HERO_NAME);
        int heroImageId=intent.getIntExtra(HERO_IMAGE_ID,0);
        Toolbar toolbar= (Toolbar) findViewById(R.id.hero_toolbar);
        CollapsingToolbarLayout collapsingToolbar= (CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar);
        ImageView heroImageView= (ImageView) findViewById(R.id.hero_image_view);
        TextView heroContextText= (TextView) findViewById(R.id.hero_content_text);

        FloatingActionButton comment= (FloatingActionButton) findViewById(fbt_comment);
        comment.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(heroName);
        Glide.with(this).load(heroImageId).into(heroImageView);
        String heroContent=generateHeroContent(heroName);
        heroContextText.setText(heroContent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String generateHeroContent(String heroName){
        StringBuilder heroContent=new StringBuilder();
        heroContent.append(getString(R.string.iop));
        heroContent.append(getString(R.string.cra));
        heroContent.append(getString(R.string.ecaflip));
        heroContent.append(getString(R.string.feca));
        heroContent.append(getString(R.string.enutrof));
        heroContent.append(getString(R.string.steam));
        heroContent.append(getString(R.string.sram));
        heroContent.append(getString(R.string.pandawa));
        return heroContent.toString();
    }

    public void Comment(View view){
        Toast.makeText(this,"暂时不支持评论",Toast.LENGTH_SHORT).show();
    }

}
