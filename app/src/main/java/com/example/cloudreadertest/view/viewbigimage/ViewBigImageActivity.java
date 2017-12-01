package com.example.cloudreadertest.view.viewbigimage;

import android.Manifest;
import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cloudreadertest.R;
import com.example.cloudreadertest.app.Constants;
import com.example.cloudreadertest.databinding.ActivityViewBigImageBinding;
import com.example.cloudreadertest.databinding.ViewpagerVeryImageBinding;
import com.example.cloudreadertest.utils.DebugUtil;
import com.example.cloudreadertest.utils.ImageLoadUtils;
import com.example.cloudreadertest.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewBigImageActivity extends FragmentActivity {
    private ActivityViewBigImageBinding mBinding;
    private int selet;//1,头像不显示页码，2大图显示页码
    private int position;//第几张  接收穿过来当前选择的图片的页码
    private ArrayList<String> imageLists;//图片源
    private boolean isLocal;
    private boolean isApp;//是否是本应用中的图片
    private int imageID;//图片在本应用中的ID
    private int mPage;//viewpage当前页数
    /**
     * 需要进行检测的权限数组
     */
    protected String[] permissionList = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_big_image);
        getView();
    }

    private void getView() {
        mBinding.tvSaveHvp.setOnClickListener(onClickListener);
        mBinding.tvLeft.setOnClickListener(onClickListener);
        mBinding.tvRight.setOnClickListener(onClickListener);
        //读取传过来的值
        Bundle bundle = getIntent().getExtras();
        selet = bundle.getInt("selet");//1,头像不显示页码，2大图显示页码
        position = bundle.getInt("position");//第几张
        imageLists = bundle.getStringArrayList("imageuri");
        isLocal = bundle.getBoolean("isLocal", false);
        //是否是本应用中的图片
        isApp = bundle.getBoolean("isApp", false);
        //图片在本应用中的ID
        imageID = bundle.getInt("id", 0);
        //拿到数据，设置适配器
        if (isApp) {
            MyPagerAdapter adapter = new MyPagerAdapter();
            mBinding.hvpViewBigImage.setAdapter(adapter);
            mBinding.hvpViewBigImage.setEnabled(false);
        } else {
            viewPagerAdapter = new ViewPagerAdapter();
            mBinding.hvpViewBigImage.setAdapter(viewPagerAdapter);
            mBinding.hvpViewBigImage.setCurrentItem(position);
            mPage = position;
            mBinding.hvpViewBigImage.setOnPageChangeListener(onPageChangeListener);
            mBinding.hvpViewBigImage.setEnabled(false);
            if (selet == 2) {
                mBinding.tvPageHvp.setText(position + 1 + "/" + imageLists.size());
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_save_hvp:
                    PermissionGen.needPermission(ViewBigImageActivity.this, Constants.WRITE_EXTERNAL_STORAGE, permissionList);
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    @PermissionSuccess(requestCode = Constants.WRITE_EXTERNAL_STORAGE)
    public void requestSdcardSuccess() {
        saveImageToLocal();
    }

    @PermissionFail(requestCode = Constants.WRITE_EXTERNAL_STORAGE)
    public void requestSdcardFailed() {
//        mDialog = new AlertDialog.Builder(ViewBigImageActivity.this)
//                .setTitle("权限拒绝")
//                .setMessage("您拒绝了权限，图片保存失败")
//                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        ShowAppSetDetails.showInstalledAppDetails(ViewBigImageActivity.this, "com.example.cloudreadertest");
//                        DebugUtil.debug("开启权限设置");
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setCancelable(true)
//                .create();
//        mDialog.show();
        ToastUtil.showToast("您拒绝了权限，图片保存失败");
    }

    /**
     * 保存图片到本地
     */
    private void saveImageToLocal() {
        ToastUtil.showToast("开始下载图片");
        if (isApp) {//本地图片
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageID);
            ImageLoadUtils.saveImageToGallery(ViewBigImageActivity.this, bitmap);
        } else {//网络图片
            final BitmapFactory.Options options = new BitmapFactory.Options();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取图片路径
                    final String imageUrl = getImagePath(imageLists.get(mPage));
                    //主线程更新UI
                    ViewBigImageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (imageUrl != null) {
                                Bitmap bitmap = BitmapFactory.decodeFile(imageUrl, options);
                                ImageLoadUtils.saveImageToGallery(ViewBigImageActivity.this, bitmap);
                            }
                        }
                    });
                }
            }).start();
        }
    }

    /**
     * Glide获取图片缓存路径
     *
     * @param imageUrl
     * @return
     */
    private String getImagePath(String imageUrl) {
        String path = null;
        FutureTarget<File> futureTarget = Glide.with(ViewBigImageActivity.this)
                .load(imageUrl).downloadOnly(500, 500);
        try {
            File cacheFile = futureTarget.get();
            path = cacheFile.getAbsolutePath();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return path;
    }

    ViewPagerAdapter viewPagerAdapter;

    /**
     * 如果是本应用图片的适配器
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //ViewDataBinding dataBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.viewpager_very_image, null, false);
            ViewpagerVeryImageBinding binding = DataBindingUtil.setContentView(getParent(), R.layout.viewpager_very_image);
            View view = binding.getRoot();
            if (imageID != 0) {
                binding.pvZoomImage.setImageResource(imageID);
            }
            binding.pvZoomImage.setOnPhotoTapListener(onPhotoTapListener);
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 图片点击事件
     */
    private PhotoViewAttacher.OnPhotoTapListener onPhotoTapListener = new PhotoViewAttacher.OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View view, float x, float y) {
            finish();
        }

        @Override
        public void onOutsidePhotoTap() {

        }
    };

    /**
     * viewpager
     */
    private class ViewPagerAdapter extends PagerAdapter {

        LayoutInflater inflater;

        public ViewPagerAdapter() {
            inflater = getLayoutInflater();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.viewpager_very_image, container, false);
            final PhotoView photoView = view.findViewById(R.id.pv_zoom_image);
            final ProgressBar progressBar = view.findViewById(R.id.pb_loading);
            // 保存网络图片的路径
            String adapter_image_entity = (String) getItem(position);

            String imageUrl;
            if (isLocal) {
                imageUrl = "file://" + adapter_image_entity;
                mBinding.tvSaveHvp.setVisibility(View.GONE);
            } else {
                imageUrl = adapter_image_entity;
            }
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setClickable(false);
            Glide.with(container.getContext())
                    .load(imageUrl)
                    .crossFade(700)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            DebugUtil.toast(container.getContext(), "资源加载异常");
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        //图是否加载完成
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            int height = photoView.getHeight();
                            int windowHeight = getWindowManager().getDefaultDisplay().getHeight();
                            if (height > windowHeight) {
                                photoView.setScaleType(ImageView.ScaleType.CENTER);
                            } else {
                                photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                            return false;
                        }
                    })
                    .into(photoView);
            photoView.setOnPhotoTapListener(onPhotoTapListener);
            container.addView(view, 0);
            return view;
        }

        @Override
        public int getCount() {
            return (imageLists == null || imageLists.size() == 0) ? 0 : imageLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        Object getItem(int position) {
            return imageLists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * viewpage的滑动监听
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //当页面改变后，重新设置一遍页码
            mPage = position;
            mBinding.tvPageHvp.setText(position + 1 + "/" + imageLists.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private AlertDialog mDialog;

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        DebugUtil.debug("requestCode:" + requestCode);
//        if (requestCode == 0) {
//            if (PermissionUtils.checkSelfResult(grantResults)) {
//                // Permission Granted
//                DebugUtil.debug("-----Permission Granted");
//                saveImageToLocal();
//            } else {
//                // Permission Denied
//                DebugUtil.debug("Permission Denied");
//                mDialog = new AlertDialog.Builder(ViewBigImageActivity.this)
//                        .setTitle("友好提醒")
//                        .setMessage("您已拒绝权限,请开启！")
//                        .setPositiveButton("开启", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                                ShowAppSetDetails.showInstalledAppDetails(ViewBigImageActivity.this, "com.example.cloudreadertest");
//                                DebugUtil.debug("开启权限设置");
//                                saveImageToLocal();
//                            }
//                        })
//                        .setCancelable(true)
//                        .create();
//                mDialog.show();
//            }
//            PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
//        }
//    }
}
