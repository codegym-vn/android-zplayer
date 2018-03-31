package zplayer.gymcode.zplayer;

import java.util.ArrayList;
import java.util.List;

import zplayer.gymcode.zplayer.model.ZSong;

/**
 * Created by quydu on 3/31/2018.
 */

public class SongManager {
    private List<ZSong> songs;

    public SongManager() {
        songs = new ArrayList<>();
    }

    public List<ZSong> getSongs() {
        return songs;
    }

    public void loadSongs() {
        ZSong song = new ZSong();
        song.setPerformer("Trịnh Công sơn");
        song.setTitle("Như tiếng thở dài");
        song.setSource("Songs/Nhu Tieng Tho Dai - Trinh Cong Son.mp3");
        song.setThumbnail(R.drawable.tcs);
        songs.add(song);
        song = new ZSong();
        song.setPerformer("Trịnh Công sơn");
        song.setTitle("Tôi ơi đừng tuyệt vọng");
        song.setSource("Songs/Toi Oi Dung Tuyet Vong - Trinh Cong Son.mp3");
        song.setThumbnail(R.drawable.tcs2);
        songs.add(song);
    }
}
