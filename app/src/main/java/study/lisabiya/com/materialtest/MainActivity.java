package study.lisabiya.com.materialtest;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.Item;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE =1 ;
    private DrawerLayout mDrawerLayout;
    private List<Uri> mSelected;
    private ImageView user_image;
    private View headerView;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    private Hero[] heroes={new Hero("伊欧普",R.drawable.iop)
            ,new Hero("凯拉",R.drawable.cra)
            ,new Hero("萨迪达",R.drawable.sadida)
            ,new Hero("斯拉姆",R.drawable.sram)
            ,new Hero("埃努卓",R.drawable.enutrof)
            ,new Hero("菲卡",R.drawable.feca)
            ,new Hero("潘达旺",R.drawable.pandawa)
            ,new Hero("欧撒莫达",R.drawable.osamoda)
            ,new Hero("安妮丽莎",R.drawable.eniripsa)
            ,new Hero("萨满行者",R.drawable.masqueraiders)
            ,new Hero("蒸汽行者",R.drawable.steam)
            ,new Hero("艾卡菲利",R.drawable.ecaflip)
            ,new Hero("械勒",R.drawable.xelor)
            ,new Hero("盗贼",R.drawable.ruguerush)
            ,new Hero("狂战士",R.drawable.sacrier)
            ,new Hero("光能法师",R.drawable.huppermage)
            ,new Hero("亚兰洛普",R.drawable.eliotrope)
                            };
    private List<Hero> heroList=new ArrayList<>();
    private HeroAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_layout);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
        FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab);
        //获取sharedPreferences对象
        mSharedPreferences=getSharedPreferences("background",MODE_PRIVATE);
        editor=mSharedPreferences.edit();
        //悬浮键交互
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"删除了数据",Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this,"数据删除",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
        //显示滑动菜单
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        //菜单
        navigationView.setCheckedItem(R.id.nav_call);
            //获取headerLayout组件
        headerView = navigationView.getHeaderView(0);
        user_image= (ImageView) headerView.findViewById(R.id.icon_image);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                        permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.
                        PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    startMatisse();
                }
            }
        });
            //item点击特效
        navigationView.setNavigationItemSelectedListener(new NavigationView.
                OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    mDrawerLayout.closeDrawers();
                return true;

            }
        });
            //item监听
        navigationView.getMenu().findItem(R.id.nav_call).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this,"打电话",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //加载英雄介绍页面
        initHeroes();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new HeroAdapter(heroList);
        recyclerView.setAdapter(adapter);
        //下拉刷新
        swipeRefresh= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout
                .OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshHeroes();
            }
        });
        //设置背景图
        if(mSharedPreferences.getString("backImage","")!=""&&
        mSharedPreferences.getString("backImage","")!=null){
            try {
                loadDrawable();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //初始化英雄列表
    private void initHeroes(){
        heroList.clear();
        for(int i=0;i<17;i++){
            heroList.add(heroes[i]);
        }
    }
    //启动知乎相册
    private void startMatisse(){
        Matisse.from(MainActivity.this)
                .choose(MimeType.of(MimeType.GIF,MimeType.JPEG,MimeType.PNG))//选择mime的类型
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
    }
    //授权处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.
                        PERMISSION_GRANTED){
                    startMatisse();
                }else {
                    Toast.makeText(MainActivity.this,"需要授权才可访问相册",
                            Toast.LENGTH_SHORT).show();
                }
        }
    }
    //自定义用户页面的背景以及保存数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            user_image.setImageResource(R.drawable.cra);
            Uri uri=mSelected.get(0);
            //保存图片
            Glide.with(this).load(uri).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    saveDrawable(resource);
                }
            });
            //设置头像
            Glide.with(this).load(uri).into(user_image);
            //虚幻背景
            Glide.with(this).load(uri).
                    bitmapTransform(new BlurTransformation(this,14,3))
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        headerView.setBackground(resource);
                        }
                    });
        }

    }
    //加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    //菜单监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                Toast.makeText(this,"你点击的了备份",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"你点击的了删除",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this,"你点击的了设置",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
    //刷新英雄列表
    private void refreshHeroes(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initHeroes();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    //使用sharedPreferences保存和读取图片
    //保存
    private  void saveDrawable(Bitmap bitmap){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,bos);
        String imageBase64=new String(Base64.encodeToString(bos.toByteArray(),
                Base64.DEFAULT));
        editor.putString("backImage",imageBase64);
        editor.commit();
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //读取
    private  void loadDrawable() throws IOException {
        String temp=mSharedPreferences.getString("backImage","");
        ByteArrayInputStream bis=new ByteArrayInputStream(Base64.
                decode(temp.getBytes(),Base64.DEFAULT));
        Drawable drawable=Drawable.createFromStream(bis,"image");
            user_image.setImageDrawable(drawable);
        Glide.with(this).load(Base64.decode(temp.getBytes(),Base64.DEFAULT)).
                bitmapTransform(new BlurTransformation(this,50,1))
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        headerView.setBackground(resource);
                    }
                });
        bis.close();
    }

}
