// Generated code from Butter Knife. Do not modify!
package com.example.jayant_sai.moviesnow;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import custom.RichBottomNavigationView;
import custom.StatefulRecyclerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.bottom_navigation = Utils.findRequiredViewAsType(source, R.id.bottom_navigation, "field 'bottom_navigation'", RichBottomNavigationView.class);
    target.mRvMovies = Utils.findRequiredViewAsType(source, R.id.rv_movies, "field 'mRvMovies'", StatefulRecyclerView.class);
    target.mTvErrorMessageDisplay = Utils.findRequiredViewAsType(source, R.id.tv_error_message_display, "field 'mTvErrorMessageDisplay'", TextView.class);
    target.mPbLoadingIndicator = Utils.findRequiredViewAsType(source, R.id.pb_loading_indicator, "field 'mPbLoadingIndicator'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.bottom_navigation = null;
    target.mRvMovies = null;
    target.mTvErrorMessageDisplay = null;
    target.mPbLoadingIndicator = null;
  }
}
