package zplayer.gymcode.zplayer.model;

/**
 * Created by quydu on 3/31/2018.
 */

public class ZSong {
    private String Title;
    private String Source;
    private String Performer;
    private int Thumbnail;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }


    public String getPerformer() {
        return Performer;
    }

    public void setPerformer(String performer) {
        Performer = performer;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }
}
