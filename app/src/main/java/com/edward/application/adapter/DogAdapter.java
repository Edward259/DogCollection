package com.edward.application.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.edward.application.R;
import com.edward.application.action.ObservableCollect;
import com.edward.application.common.Constants;
import com.edward.application.databinding.DogItemBinding;
import com.edward.application.db.DogModel;
import com.edward.application.eventbus.ShowLargeImageEvent;
import com.edward.application.utils.FileUtils;
import com.edward.application.utils.ImageUtils;
import com.edward.application.utils.RxViewUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.ViewHolder<DogItemBinding>> {
    private List<DogModel> dogModels = new ArrayList<>();

    @NonNull
    @Override
    public DogAdapter.ViewHolder<DogItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DogAdapter.ViewHolder<DogItemBinding> holder, int position) {
        DogItemBinding binding = holder.getBinding();
        DogModel dogModel = dogModels.get(position);
        DogItemModel viewModel = new DogItemModel(dogModel);
        binding.setModel(viewModel);
        RxViewUtils.setOnDoubleClickListener(binding.img, view -> {
            ObservableCollect.instance().updateCover(dogModel)
                    .doOnSubscribe(disposable -> viewModel.isUpdating.set(true))
                    .doFinally(() -> viewModel.isUpdating.set(false))
                    .subscribe(model -> DogItemModel.setCover(binding.img, viewModel));
        }, v -> EventBus.getDefault().post(new ShowLargeImageEvent(viewModel)));

    }

    @Override
    public int getItemCount() {
        return dogModels.size();
    }

    public void setDogModels(List<DogModel> dogModels) {
        this.dogModels = dogModels;
        notifyDataSetChanged();
    }

    public void addDogModel(DogModel model) {
        this.dogModels.add(model);
        notifyItemInserted(dogModels.size() - 1);
    }

    public static class DogItemModel extends BaseObservable {
        private DogModel bean;
        public ObservableField<String> imgPath = new ObservableField<>();
        public ObservableField<String> breed = new ObservableField<>();
        public ObservableBoolean isUpdating = new ObservableBoolean(false);
        public ObservableBoolean isCollected = new ObservableBoolean(false) {
            @Override
            public void set(boolean value) {
                super.set(value);
                bean.setCollected(value);
                ObservableCollect.instance().saveDogModel(bean);
            }
        };

        public DogItemModel(DogModel bean) {
            this.bean = bean;
            imgPath.set(bean.getCoverPath());
            breed.set(bean.getBreed());
            isCollected.set(bean.isCollected());
        }

        @BindingAdapter("setCover")
        public static void setCover(ImageView imageView, DogItemModel model) {
            String path = FileUtils.fileExist(ImageUtils.getCoverCachePath(model.bean)) ?
                    ImageUtils.getCoverCachePath(model.bean) :
                    model.imgPath.get();
            Log.e(Constants.LOG_TAG, "setCover: path: " + path);
            Glide.with(imageView.getContext())
                    .load(path)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.dog)
                    .error(R.drawable.failed)
                    .apply(new RequestOptions().transforms(new CenterCrop(),
                            new RoundedCorners(getDimensionPixelSize(imageView.getContext()))))
                    .into(imageView);
        }

        public void switchCheck(View view) {
            if (!(view instanceof CheckBox)) {
                return;
            }
            isCollected.set(((CheckBox) view).isChecked());
        }

        public void clickImg(View view) {
            EventBus.getDefault().post(new ShowLargeImageEvent(this));
        }

        private static int getDimensionPixelSize(Context context) {
            return context.getResources().getDimensionPixelSize(R.dimen.bg_dog_item_radius);
        }
    }

    public static class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

        T binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public T getBinding() {
            return binding;
        }
    }
}
