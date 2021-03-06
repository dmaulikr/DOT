package pl.slapps.dot.gui;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import pl.slapps.dot.drawing.Util;
import pl.slapps.dot.gui.fragment.LayoutMainMenu;

/**
 * Created by piotr on 23/02/16.
 */
public class AnimationMainMenu {

    private LayoutMainMenu menu;
    public AnimationHide menuHideAnimation;
    public AnimationShow menuShowAnimation;
    public AnimationHide headerHideAnimation;
    public AnimationHide btnsHideAnimation;
    public AnimationEntrance entranceAnimation;

    public AnimationMainMenu(LayoutMainMenu menu)
    {
        this.menu=menu;

        init();
    }

    public void showMenu()
    {
        if(menu.getLayout().getParent()==null)
            menu.context.gameHolder.addView(menu.getLayout());

        menu.getGame().setRunnig(false);
        menu.enableButtons();


        menuShowAnimation.startAnimation(null, 500);
    }
    public void hideMenu()
    {
        menu.getGame().setDrawing(true);
        menu.disableButtons();
        headerHideAnimation.startAnimation(500);
        btnsHideAnimation.startAnimation(500);
       // menuHideAnimation.startAnimation(500);
    }

    public void setColor(final String colorStart, final String colorEnd)
    {
        Animation colorAnimation = new Animation() {
            public void applyTransformation(float progress, Transformation f) {


                final int color = Color.parseColor(Util.calculateColorsSwitch(colorStart, colorEnd, progress));
                //int c = Color.argb(100, Color.red(color), Color.green(color), Color.blue(color));
                menu.getBackground().setBackgroundColor(color);
            }
        };
        colorAnimation.setDuration(500);
        menu.getBackground().startAnimation(colorAnimation);
    }

    public void init()
    {



        menuHideAnimation = new AnimationHide(menu.getLayoutMenu(), new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                menu.getLayoutMenu().setVisibility(View.GONE);
                menuHideAnimation.clearAnimation();
                entranceAnimation.clearAnimation();
                btnsHideAnimation.clearAnimation();
                headerHideAnimation.clearAnimation();


                new Handler().post(new Runnable() {
                    public void run() {
                        menu.context.removeMainMenu();
                    }
                });
                //menu.context.gameHolder.removeView(menu.getLayout());
                menu.getGame().setRunnig(true);



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        menuShowAnimation = new AnimationShow(menu.getLayoutMenu());
        headerHideAnimation = new AnimationHide(menu.getStartButton(), new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        entranceAnimation = new AnimationEntrance(menu.getLayoutHeader());

        btnsHideAnimation = new AnimationHide(menu.getLayoutBtns(), new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                entranceAnimation.startAnimation(new AnimationShow.OnAnimationListener() {
                    @Override
                    public void onAnimationEnd() {
                        menuHideAnimation.startAnimation(500);

                    }

                    @Override
                    public void onAnimationStart() {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
