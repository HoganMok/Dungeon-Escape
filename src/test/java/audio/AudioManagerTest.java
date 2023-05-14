package audio;
import org.junit.*;
import static org.junit.Assert.*;

public class AudioManagerTest {

    private AudioManager audioManager;

    @Before
    public void setUpTest(){
        audioManager = new AudioManager();
    }

    @Test
    public void testGetAudio(){
        audioManager.addAudio(AudioManager.AudioType.BOMB, "BOMB.wav");
        Audio audio = audioManager.getAudio(AudioManager.AudioType.BOMB);
        assertNotNull(audio);
    }
    @Test
    public void testMute(){
        audioManager.mute();
        assertTrue(audioManager.getIsMuted());
    }

    @Test
    public void testUnmute(){
        audioManager.unMute();
        assertFalse(audioManager.getIsMuted());
    }

    @Test
    public void testPlayAudioOnce(){
        audioManager.addAudio(AudioManager.AudioType.BOMB, "BOMB.wav");
        audioManager.playAudioOnce(AudioManager.AudioType.BOMB);
        assertTrue(audioManager.getIsPlayingOnce());
    }

    @Test
    public void testPlayAudioOnLoop(){
        audioManager.addAudio(AudioManager.AudioType.BOMB, "BOMB.wav");
        audioManager.playAudioOnLoop(AudioManager.AudioType.BOMB);
        assertTrue(audioManager.getIsPlayingOnLoop());
        audioManager.mute();
        audioManager.playAudioOnLoop(AudioManager.AudioType.BOMB);
        assertFalse(audioManager.getIsPlayingOnLoop());
    }

    @Test
    public void testStopAudio(){
        audioManager.addAudio(AudioManager.AudioType.BOMB, "BOMB.wav");
        audioManager.playAudioOnLoop(AudioManager.AudioType.BOMB);
        audioManager.stopAudio(AudioManager.AudioType.BOMB);
        assertFalse(audioManager.getIsPlayingOnLoop());
    }
}
