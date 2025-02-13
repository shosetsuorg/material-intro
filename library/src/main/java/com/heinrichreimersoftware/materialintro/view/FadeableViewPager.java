/*
 * MIT License
 *
 * Copyright (c) 2017 Jan Heinrich Reimer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.heinrichreimersoftware.materialintro.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class FadeableViewPager extends SwipeBlockableViewPager {

    public FadeableViewPager(Context context) {
        super(context);
    }

    public FadeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(new PagerAdapterWrapper(adapter));
    }

    @Override
    public PagerAdapter getAdapter() {
        PagerAdapterWrapper wrapper = (PagerAdapterWrapper) super.getAdapter();
        return wrapper == null ? null : wrapper.getAdapter();
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        super.setOnPageChangeListener(new OnPageChangeListenerWrapper(listener));
    }

    @Override
    public void addOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        super.addOnPageChangeListener(new OnPageChangeListenerWrapper(listener));
    }

    @Override
    public void removeOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        super.removeOnPageChangeListener(new OnPageChangeListenerWrapper(listener));
    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        super.setPageTransformer(reverseDrawingOrder, new PageTransformerWrapper(transformer, getAdapter()));
    }

    private class OnPageChangeListenerWrapper implements OnPageChangeListener {
        private final OnPageChangeListener listener;

        private OnPageChangeListenerWrapper(OnPageChangeListener listener) {
            this.listener = listener;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int count = listener instanceof OnOverscrollPageChangeListener ?
                    FadeableViewPager.super.getAdapter().getCount() : getAdapter().getCount();
            listener.onPageScrolled(Math.min(position, count - 1),
                    position < count ? positionOffset : 0,
                    position < count ? positionOffsetPixels : 0);
        }

        @Override
        public void onPageSelected(int position) {
            int count = listener instanceof OnOverscrollPageChangeListener ?
                    FadeableViewPager.super.getAdapter().getCount() : getAdapter().getCount();
            listener.onPageSelected(Math.min(position, count - 1));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            listener.onPageScrollStateChanged(state);
        }
    }

    private class PagerAdapterWrapper extends PagerAdapter {
        private final PagerAdapter adapter;

        private PagerAdapterWrapper(PagerAdapter adapter) {
            this.adapter = adapter;
            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    notifyDataSetChanged();
                }

                @Override
                public void onInvalidated() {
                    notifyDataSetChanged();
                }
            });
        }

        public PagerAdapter getAdapter() {
            return adapter;
        }

        @Override
        public int getCount() {
            return adapter.getCount() + 1;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object != null && adapter.isViewFromObject(view, object);
        }

        @Override
        public void startUpdate(@NonNull ViewGroup container) {
            adapter.startUpdate(container);
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (position < adapter.getCount())
                return adapter.instantiateItem(container, position);
            return null;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (position < adapter.getCount())
                adapter.destroyItem(container, position, object);
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (position < adapter.getCount())
                adapter.setPrimaryItem(container, position, object);
        }

        @Override
        public void finishUpdate(@NonNull ViewGroup container) {
            adapter.finishUpdate(container);
        }

        @SuppressWarnings("deprecation")
        @Deprecated
        @Override
        public void startUpdate(@NonNull View container) {
            adapter.startUpdate(container);
        }

        @SuppressWarnings("deprecation")
        @Deprecated
        @Override
        public Object instantiateItem(@NonNull View container, int position) {
            if (position < adapter.getCount())
                return adapter.instantiateItem(container, position);
            return null;
        }

        @SuppressWarnings("deprecation")
        @Deprecated
        @Override
        public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
            if (position < adapter.getCount())
                adapter.destroyItem(container, position, object);
        }

        @SuppressWarnings("deprecation")
        @Deprecated
        @Override
        public void setPrimaryItem(@NonNull View container, int position, @NonNull Object object) {
            if (position < adapter.getCount())
                adapter.setPrimaryItem(container, position, object);
        }

        @SuppressWarnings("deprecation")
        @Deprecated
        @Override
        public void finishUpdate(@NonNull View container) {
            adapter.finishUpdate(container);
        }

        @Override
        public Parcelable saveState() {
            return adapter.saveState();
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            adapter.restoreState(state, loader);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            int position = adapter.getItemPosition(object);
            if (position < adapter.getCount()) return position;
            return POSITION_NONE;
        }

        @Override
        public void registerDataSetObserver(@NonNull DataSetObserver observer) {
            adapter.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(@NonNull DataSetObserver observer) {
            adapter.unregisterDataSetObserver(observer);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < adapter.getCount())
                return adapter.getPageTitle(position);
            return null;
        }

        @Override
        public float getPageWidth(int position) {
            if (position < adapter.getCount())
                return adapter.getPageWidth(position);
            return 1f;
        }
    }

    private class PageTransformerWrapper implements PageTransformer {
        private final PageTransformer pageTransformer;
        private final PagerAdapter adapter;

        private PageTransformerWrapper(PageTransformer pageTransformer, PagerAdapter adapter) {
            this.pageTransformer = pageTransformer;
            this.adapter = adapter;
        }

        @Override
        public void transformPage(@NonNull View page, float position) {
            pageTransformer.transformPage(page, Math.min(position, adapter.getCount() - 1));
        }
    }


    public interface OnOverscrollPageChangeListener extends OnPageChangeListener {
    }

    public static class SimpleOnOverscrollPageChangeListener implements OnOverscrollPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
