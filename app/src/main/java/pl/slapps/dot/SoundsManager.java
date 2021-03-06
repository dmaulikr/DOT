package pl.slapps.dot;

import android.content.Context;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.util.Log;

import pl.slapps.dot.model.Sounds;

/**
 * Created by piotr on 17.11.15.
 */
public class SoundsManager {


    private String TAG = SoundsManager.class.getName();

    private Uri moveSound;
    private Uri crashSound;
    private Uri crashSoundTwo;
    private Uri soundFinish;
    private Uri backgroundSound;


    private AsyncPlayer asyncPlayer;
    private AsyncPlayer asyncPlayerPress;
    private AsyncPlayer asyncPlayerCrash;
    private AsyncPlayer asyncPlayerCrashTwo;

    private AsyncPlayer asyncPlayerBackground;
    private Context context;

    public static String DEFAULT_PRESS = "click2";
    public static String DEFAULT_CRASH = "spacebib";
    public static String DEFAULT_FINISH = "finish";
    public static String DEFAULT_COIN = "coin";


    private String key = "cache/";

    private Sounds sounds;
    private boolean crashFlag;
    private boolean mute;

    public boolean getMute()
    {
        return mute;
    }
    public void mute(boolean mute) {
        if(!mute)
            stopMusic();
        this.mute = mute;
    }
    public void toggleMute()
    {
        mute(!mute);
    }

    public static AsyncPlayer getPlayer(String tag) {
        return new AsyncPlayer(tag);
    }

    public void stopMusic() {
        asyncPlayer.stop();
        asyncPlayerBackground.stop();
        asyncPlayerCrash.stop();
        asyncPlayerCrashTwo.stop();

        asyncPlayerPress.stop();
    }

    public Uri parseSound(String filename) {
        if (!filename.contains(key))
            return Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + filename);
        else
            return Uri.parse(filename);
    }

    public void configure(Sounds sounds) {
        this.sounds = sounds;

        if (!sounds.soundBackground.equals(""))

            if (sounds.soundBackground.contains(key))
                backgroundSound = Uri.parse(sounds.soundBackground);
            else
                backgroundSound = parseSound(sounds.soundBackground);
        else
            backgroundSound = null;

        if (!sounds.soundPress.equals("")) {

            if (sounds.soundPress.contains(key))
                moveSound = Uri.parse(sounds.soundPress);
            else
                moveSound = parseSound(sounds.soundPress);
        } else
            moveSound = parseSound(DEFAULT_PRESS);

        if (!sounds.soundCrash.equals("")) {
            if (sounds.soundCrash.contains(key))
                crashSound = Uri.parse(sounds.soundCrash);
            else
                crashSound = parseSound(sounds.soundCrash);
        } else
            crashSound = parseSound(DEFAULT_CRASH);

        if (!sounds.soundCrashTwo.equals("")) {
            if (sounds.soundCrashTwo.contains(key))
                crashSoundTwo = Uri.parse(sounds.soundCrashTwo);
            else
                crashSoundTwo = parseSound(sounds.soundCrashTwo);
        } else
            crashSound = parseSound(DEFAULT_CRASH);

        if (!sounds.soundFinish.equals("")) {
            if (sounds.soundFinish.contains(key))
                soundFinish = Uri.parse(sounds.soundFinish);
            else
                soundFinish = parseSound(sounds.soundFinish);

        } else
            soundFinish = parseSound(DEFAULT_FINISH);


    }

    public SoundsManager(Context context) {
        this.context = context;
        this.sounds = new Sounds();
        moveSound = Uri.parse("android.resource://" + context.getPackageName() + "/raw/click2");
        crashSound = Uri.parse("android.resource://" + context.getPackageName() + "/raw/spacebib");
        crashSoundTwo = Uri.parse("android.resource://" + context.getPackageName() + "/raw/spacebib");

        soundFinish = Uri.parse("android.resource://" + context.getPackageName() + "/raw/finish");

        asyncPlayer = new AsyncPlayer("action");
        asyncPlayerBackground = new AsyncPlayer("background");
        asyncPlayerPress = new AsyncPlayer("press");
        asyncPlayerCrash = new AsyncPlayer("crash");
        asyncPlayerCrashTwo = new AsyncPlayer("crashTwo");


    }

    public void stopBackgroundPlayer() {
        asyncPlayerBackground.stop();
    }

    private void playOverlapped(Uri uri) {
        getPlayer(uri.toString()).play(context, uri, false, AudioManager.STREAM_MUSIC);
    }

    public void playFinishSound() {
        if(mute)
            return;
        asyncPlayer.play(context, soundFinish, false, AudioManager.STREAM_MUSIC);
        //  mediaPlayerMove.start();
    }

    public void playMoveSound() {
        if(mute)
            return;
        if (!sounds.overlap)
            asyncPlayerPress.play(context, moveSound, false, AudioManager.STREAM_MUSIC);
        else
            playOverlapped(moveSound);
        //  mediaPlayerMove.start();

    }

    public void playCoinSound() {
        if(mute)
            return;

        playOverlapped(parseSound(DEFAULT_COIN));
        //  mediaPlayerMove.start();

    }

    public void playCrashSound() {
        if(mute)
            return;


        if (crashFlag) {
            asyncPlayerCrash.play(context, crashSound, false, AudioManager.STREAM_MUSIC);

        } else {
            asyncPlayerCrashTwo.play(context, crashSoundTwo, false, AudioManager.STREAM_MUSIC);

        }
        crashFlag = !crashFlag;


        //  mediaPlayerCrash.start();
    }


    public void playBackgroundSound() {
        if(mute)
            return;
        asyncPlayerBackground.stop();
        if (backgroundSound == null)
            return;


        asyncPlayerBackground.play(context, backgroundSound, true, AudioManager.STREAM_MUSIC);

        //  mediaPlayerCrash.start();
    }

    public void playRawFile(String filename) {
        if(mute)
            return;
        Uri custom = parseSound(filename);
        if (!sounds.overlap)
            asyncPlayer.play(context, custom, false, AudioManager.STREAM_MUSIC);
        else
            playOverlapped(custom);

    }

    public void playUri(Uri uri) {
        if(mute)
            return;
        asyncPlayer.play(context, uri, false, AudioManager.STREAM_MUSIC);


    }

}
