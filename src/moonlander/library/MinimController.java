package moonlander.library;

import ddf.minim.*;


public class MinimController extends Controller {
    AudioPlayer player;

    public MinimController(AudioPlayer player, int beatsPerMinute, int rowsPerBeat) {
        super((beatsPerMinute / 60.0) * rowsPerBeat);
        this.player = player;
    }

    public void update() {
        if (isPlaying())
            setCurrentRow((player.position() / 1000.0) * rowsPerSecond, false);
    }

    public void pause() {
        super.pause();
        player.pause();
    }

    public void play() {
        super.play();
        player.cue((int)(getCurrentRow() * 1000.0 / rowsPerSecond));
        player.play();
    }
}
