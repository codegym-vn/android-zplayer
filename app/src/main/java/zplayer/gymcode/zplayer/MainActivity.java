package zplayer.gymcode.zplayer;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import zplayer.gymcode.zplayer.model.ZSong;

public class MainActivity extends Activity {
    SongManager songManager;

    MediaPlayer m;
    private List<ZSong> songs;
    private int index = 0;
    private TextView tvCurrent;
    private TextView tvRemaining;
    private ImageView ivPlay;
    private View ivNext;
    private View ivPre;
    private TextView tvTitle;
    private TextView tvPerformer;
    private ImageView ivThumbnail;


    private final int interval = 1000;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {

        }
    };
    private CountDownTimer countDown;
    private SeekBar seekBar;
    private long currentUntilFinished;
    private long songDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCurrent = findViewById(R.id.tvCurrent);
        tvRemaining = findViewById(R.id.tvRemaining);
        tvTitle = findViewById(R.id.tvTitle);
        tvPerformer = findViewById(R.id.tvPerformer);
        ivThumbnail = findViewById(R.id.ivThumbnail);
        seekBar = findViewById(R.id.seekBar);
        ivPlay = findViewById(R.id.ivPlay);
        ivNext = findViewById(R.id.ivNext);
        ivPre = findViewById(R.id.ivPre);

        songManager = new SongManager();
        songManager.loadSongs();
        songs = songManager.getSongs();
        updateViewBySong(songs.get(index));

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCurrent.setVisibility(View.VISIBLE);
                tvRemaining.setVisibility(View.VISIBLE);
                if (m == null) {
                    playMusicBySong(songs.get(index));
                    return;
                }
                if (m.isPlaying()) {
                    ivPlay.setImageResource(R.drawable.play);
                    pauseMusic();
                } else {
                    ivPlay.setImageResource(R.drawable.pause);
                    startMusic();
                }
            }
        });
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index < songs.size() - 1) {
                    index++;
                } else {
                    index = 0;
                }
                playMusicBySong(songs.get(index));
            }
        });
        ivPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index > 0) {
                    index--;
                } else {
                    index = songs.size() - 1;
                }
                playMusicBySong(songs.get(index));
            }
        });
    }

    /**
     * Cập nhật giao diện theo thông tin bản nhac
     * @param song bản nhạc
     */
    private void updateViewBySong(ZSong song) {
        //Cập nhật thông tin trên view
        tvTitle.setText(song.getTitle());
        tvPerformer.setText(song.getPerformer());
        ivThumbnail.setImageResource(song.getThumbnail());
        try {
            //Lấy thông tin thời gian bản nhạc
            AssetFileDescriptor descriptor = getAssets().openFd(song.getSource());
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            String duration =
                    metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            tvRemaining.setText(getTimeFormatByLong(Long.parseLong(duration)));
            tvCurrent.setText("00:00");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Khởi tạo bộ đếm countdown để thực hiện việc cập nhật thời gian phát và còn lại của bài hát
     * @param millisInFuture
     */
    private void startCountDownTimer(long millisInFuture) {
        countDown = new CountDownTimer(millisInFuture, 1000) {

            public void onTick(long millisUntilFinished) {
                currentUntilFinished = millisUntilFinished;
                tvRemaining.setText(getTimeFormatByLong(millisUntilFinished));
                tvCurrent.setText(getTimeFormatByLong((songDuration - millisUntilFinished)));
                seekBar.setProgress((int) (songDuration - millisUntilFinished));

            }

            public void onFinish() {
            }

        }.start();
    }

    /**
     * Hàm chơi bản nhạc
     * @param song bản nhạc cần chơi
     */
    public void playMusicBySong(ZSong song) {
        try {
            updateViewBySong(song);
            if (m == null) {
                m = new MediaPlayer();
            }
            stopMusic();
            AssetFileDescriptor descriptor = getAssets().openFd(song.getSource());
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            m.prepare();
            m.setLooping(true);
            m.start();
            String duration =
                    metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            songDuration = Long.parseLong(duration);
            seekBar.setMax((int) songDuration);
            if (countDown != null) {
                countDown.cancel();
            }
            startCountDownTimer(songDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hàm dừng chơi nhạc
     */
    private void stopMusic() {
        if (m.isPlaying()) {
            m.stop();
            m.release();
            m = new MediaPlayer();
        }
    }

    /**
     * Hàm bắt đầu chơi nhạc lại
     */
    private void startMusic() {
        m.start();
        startCountDownTimer(currentUntilFinished);
    }

    /**
     * Hàm tạm dừng chơi nhạc
     */
    private void pauseMusic() {
        m.pause();
        countDown.cancel();
    }



    /**
     * Hàm trả về định dạng time format theo phút:giây
     * @param millis millis cần format
     * @return chuỗi đã được format
     */
    private String getTimeFormatByLong(long millis) {
        String out;
        long seconds = (millis % 60000) / 1000;
        long minutes = (millis / 60000);
        out = String.format("%02d:%02d", minutes, seconds);
        return out;
    }

}
