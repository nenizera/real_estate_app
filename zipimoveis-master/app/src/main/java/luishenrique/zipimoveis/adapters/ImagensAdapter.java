package luishenrique.zipimoveis.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.utils.FirebaseAppUtils;

public class ImagensAdapter extends PagerAdapter {

    private final List<String>    mImages;
    private final LayoutInflater  mInflater;
    private final Context         mContext;
    private final FirebaseAppUtils mFirebaseAppUtils;

    public ImagensAdapter(Context context, List<String> images) {
        mContext  = context;
        mImages   = images;
        mInflater = LayoutInflater.from(context);
        mFirebaseAppUtils = new FirebaseAppUtils();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = mInflater.inflate(R.layout.slide_images, view, false);
        ImageView myImage  = (ImageView) myImageLayout.findViewById(R.id.img_slide);
        String image       = mImages.get(position);

        mFirebaseAppUtils.getFirebaseStorage().getReference().child( image )
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(mContext)
                                .load(uri)
                                .into(myImage);
                    }
                });

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
