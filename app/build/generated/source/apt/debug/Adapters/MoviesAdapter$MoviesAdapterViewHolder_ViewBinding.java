// Generated code from Butter Knife. Do not modify!
package Adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.jayant_sai.moviesnow.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MoviesAdapter$MoviesAdapterViewHolder_ViewBinding implements Unbinder {
  private MoviesAdapter.MoviesAdapterViewHolder target;

  @UiThread
  public MoviesAdapter$MoviesAdapterViewHolder_ViewBinding(MoviesAdapter.MoviesAdapterViewHolder target,
      View source) {
    this.target = target;

    target.mMoviesItem = Utils.findRequiredViewAsType(source, R.id.movies_item, "field 'mMoviesItem'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MoviesAdapter.MoviesAdapterViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mMoviesItem = null;
  }
}
