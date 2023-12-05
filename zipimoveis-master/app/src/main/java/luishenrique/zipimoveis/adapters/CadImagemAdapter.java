package luishenrique.zipimoveis.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.rest.models.PhotosImovel;

public class CadImagemAdapter extends PagerAdapter {

    private List<PhotosImovel> mImages;
    private LayoutInflater mInflater;
    private Context mContext;
    private ImagensAdapterListener mListener;

    public interface ImagensAdapterListener {
        void onClickImage(int position);

        void onRemoveImage(PhotosImovel photosImovel, int position);
    }

    public CadImagemAdapter(Context context, List<PhotosImovel> images, ImagensAdapterListener listener) {
        mContext = context;
        mImages = images;
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void update(List<PhotosImovel> images) {
        mImages = new ArrayList<>();

        if (images != null) {
            for (PhotosImovel image : images) {
                if (image == null) {
                    mImages.add(null);
                    continue;
                }

                mImages.add(image);
            }
        } else {
            mImages = images;
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = mInflater.inflate(R.layout.slide_images_imovel, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.img_slide_imovel);

        Button btnRemove = (Button) myImageLayout.findViewById(R.id.btn_remove_photo);
        TextView txtFirst = (TextView) myImageLayout.findViewById(R.id.txt_number_first);
        TextView txtLast = (TextView) myImageLayout.findViewById(R.id.txt_number_last);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickImage(position);
            }
        });

        txtLast.setText(String.valueOf(getCount() - 1));
        txtFirst.setText(String.valueOf(position));

        final PhotosImovel photosPet = mImages.get(position);
        if (photosPet != null) {

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRemoveImage(photosPet, position);
                }
            });

            Glide.with(mContext)
                    .load( photosPet.getUri() )
                    .into((ImageView) myImageLayout.findViewById(R.id.img_slide_imovel));

        } else {
            btnRemove.setVisibility(View.GONE);
            myImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_add_a_photo));
        }


        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    public void clearAdapter() {
        mImages.clear();
        mImages.add(null);
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
