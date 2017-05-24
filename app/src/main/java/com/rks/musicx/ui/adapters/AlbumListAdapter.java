package com.rks.musicx.ui.adapters;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rks.musicx.R;
import com.rks.musicx.base.BaseRecyclerViewAdapter;
import com.rks.musicx.data.model.Album;
import com.rks.musicx.data.network.AlbumArtwork;
import com.rks.musicx.interfaces.palette;
import com.rks.musicx.misc.utils.Extras;
import com.rks.musicx.misc.utils.Helper;
import com.rks.musicx.misc.widgets.CircleImageView;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;


/*
 * Created by Coolalien on 6/28/2016.
 */

/*
 * ©2017 Rajneesh Singh
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class AlbumListAdapter extends BaseRecyclerViewAdapter<Album, AlbumListAdapter.AlbumViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private int layoutID;
    private int duration = 300;
    private Interpolator interpolator = new LinearInterpolator();
    private int lastpos = -1;
    private ValueAnimator colorAnimation;

    public AlbumListAdapter(@NonNull Context context) {
        super(context);
    }

    public void setLayoutID(int layout) {
        layoutID = layout;
    }

    @Override
    public AlbumListAdapter.AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false);
        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlbumListAdapter.AlbumViewHolder holder, int position) {
        Album albums = getItem(position);
        if (layoutID == R.layout.item_grid_view || layoutID == R.layout.recent_list) {
            int pos = holder.getAdapterPosition();
            if (lastpos < pos) {
                for (Animator animator : Helper.getAnimator(holder.backgroundColor)) {
                    animator.setDuration(duration).start();
                    animator.setInterpolator(interpolator);
                }
            }
            holder.AlbumName.setText(albums.getAlbumName());
            holder.ArtistName.setText(albums.getArtistName());
            AlbumArtwork albumArtwork = new AlbumArtwork(getContext(), albums.getArtistName(), albums.getAlbumName(), albums.getId(), holder.AlbumArtwork, new palette() {
                @Override
                public void palettework(Palette palette) {
                    final int[] colors = Helper.getAvailableColor(getContext(), palette);
                    holder.backgroundColor.setBackgroundColor(colors[0]);
                    holder.AlbumName.setTextColor(ContextCompat.getColor(getContext(), R.color.text_transparent));
                    holder.ArtistName.setTextColor(ContextCompat.getColor(getContext(), R.color.text_transparent2));
                    animateViews(holder, colors[0]);
                }
            });
            albumArtwork.execute();
            holder.menu.setVisibility(View.GONE);
        }
        if (layoutID == R.layout.item_list_view) {
            holder.AlbumListName.setText(albums.getAlbumName());
            holder.ArtistListName.setText(albums.getArtistName());
            AlbumArtwork albumArtwork = new AlbumArtwork(getContext(), albums.getArtistName(), albums.getAlbumName(), albums.getId(), holder.AlbumListArtwork, new palette() {
                @Override
                public void palettework(Palette palette) {

                }
            });
            albumArtwork.execute();
            if (Extras.getInstance().getDarkTheme() || Extras.getInstance().getBlackTheme()) {
                holder.AlbumListName.setTextColor(Color.WHITE);
                holder.ArtistListName.setTextColor(ContextCompat.getColor(getContext(), R.color.darkthemeTextColor));
            }
        }
    }

    @Override
    public Album getItem(int position) throws ArrayIndexOutOfBoundsException {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return getItem(position).getAlbumName().substring(0, 1);
    }

    public void setFilter(List<Album> albumList) {
        data = new ArrayList<>();
        data.addAll(albumList);
        notifyDataSetChanged();
    }

    private void animateViews(AlbumViewHolder albumViewHolder, int colorBg) {
        colorAnimation = setAnimator(0xffe5e5e5,
                colorBg);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                albumViewHolder.backgroundColor.setBackgroundColor((Integer) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    private ValueAnimator setAnimator(int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(duration);
        return colorAnimation;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView AlbumArtwork;
        private TextView ArtistName, AlbumName, AlbumListName, ArtistListName;
        private LinearLayout backgroundColor;
        private CircleImageView AlbumListArtwork;
        private ImageButton menu;

        @SuppressLint("CutPasteId")
        public AlbumViewHolder(View itemView) {
            super(itemView);

            if (layoutID == R.layout.item_grid_view || layoutID == R.layout.recent_list) {
                AlbumArtwork = (ImageView) itemView.findViewById(R.id.album_artwork);
                AlbumName = (TextView) itemView.findViewById(R.id.album_name);
                ArtistName = (TextView) itemView.findViewById(R.id.artist_name);
                menu = (ImageButton) itemView.findViewById(R.id.menu_button);
                backgroundColor = (LinearLayout) itemView.findViewById(R.id.backgroundColor);
                AlbumArtwork.setOnClickListener(this);
                itemView.setOnClickListener(this);
                itemView.findViewById(R.id.item_view).setOnClickListener(this);
            }
            if (layoutID == R.layout.item_list_view) {
                AlbumListArtwork = (CircleImageView) itemView.findViewById(R.id.album_artwork);
                AlbumListName = (TextView) itemView.findViewById(R.id.listalbumname);
                ArtistListName = (TextView) itemView.findViewById(R.id.listartistname);
                AlbumListArtwork.setOnClickListener(this);
                itemView.findViewById(R.id.item_view).setOnClickListener(this);
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            triggerOnItemClickListener(position, v);
        }
    }

}
