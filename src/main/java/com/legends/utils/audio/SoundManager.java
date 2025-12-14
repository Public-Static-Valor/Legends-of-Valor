package com.legends.utils.audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages all sound effects for the Legends games.
 * Uses Java's built-in javax.sound.sampled API for audio playback.
 * All sounds are played asynchronously to avoid blocking the game.
 */
public class SoundManager {
    private static SoundManager instance;
    private Map<SoundType, Clip> soundClips;
    private boolean soundEnabled;
    private float volume;

    /**
     * Enum defining all available sound types in the game.
     */
    public enum SoundType {
        GAME_START("sounds/game_start.wav"),
        ATTACK("sounds/attack.wav"),
        DAMAGE_TAKEN("sounds/damage.wav"),
        POTION_USE("sounds/potion.wav"),
        SPELL_CAST("sounds/spell.wav"),
        MARKET_ENTER("sounds/market.wav"),
        BUY_ITEM("sounds/buy.wav"),
        SELL_ITEM("sounds/sell.wav"),
        VICTORY("sounds/victory.wav"),
        DEFEAT("sounds/defeat.wav"),
        LEVEL_UP("sounds/levelup.wav"),
        EQUIP_ITEM("sounds/equip.wav"),
        MONSTER_DEATH("sounds/monster_death.wav"),
        HERO_DEATH("sounds/hero_death.wav"),
        TELEPORT("sounds/teleport.wav"),
        RECALL("sounds/recall.wav"),
        MOVE("sounds/move.wav"),
        DODGE("sounds/dodge.wav");

        private final String filePath;

        SoundType(String filePath) {
            this.filePath = filePath;
        }

        public String getFilePath() {
            return filePath;
        }
    }

    /**
     * Private constructor for singleton pattern.
     */
    private SoundManager() {
        this.soundClips = new HashMap<>();
        this.soundEnabled = true;
        this.volume = 0.7f; // Default volume at 70%
        loadSounds();
    }

    /**
     * Gets the singleton instance of SoundManager.
     *
     * @return The SoundManager instance.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Loads all sound files into memory.
     */
    private void loadSounds() {
        for (SoundType soundType : SoundType.values()) {
            try {
                loadSound(soundType);
            } catch (Exception e) {
                // Sound file not found or couldn't be loaded - continue without this sound
                System.err.println("Warning: Could not load sound: " + soundType.getFilePath());
            }
        }
    }

    /**
     * Loads a specific sound file.
     *
     * @param soundType The type of sound to load.
     * @throws Exception If the sound file cannot be loaded.
     */
    private void loadSound(SoundType soundType) throws Exception {
        InputStream audioSrc = getClass().getResourceAsStream("/" + soundType.getFilePath());

        if (audioSrc == null) {
            // Try loading from file system if resource not found
            return;
        }

        try {
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            
            // Get the format of the loaded sound
            AudioFormat baseFormat = audioStream.getFormat();
            
            // Create a standard format that is widely supported (PCM_SIGNED, 16-bit)
            AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false
            );
            
            // Get a stream in the decoded format
            AudioInputStream decodedStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
            
            // Get a clip that supports the decoded format
            DataLine.Info info = new DataLine.Info(Clip.class, decodedFormat);
            Clip clip = (Clip) AudioSystem.getLine(info);
            
            clip.open(decodedStream);
            setVolume(clip, volume);
            soundClips.put(soundType, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | IllegalArgumentException e) {
            throw new Exception("Failed to load sound: " + soundType.getFilePath(), e);
        }
    }

    /**
     * Plays a sound effect.
     *
     * @param soundType The type of sound to play.
     */
    public void playSound(SoundType soundType) {
        if (!soundEnabled) {
            return;
        }

        Clip clip = soundClips.get(soundType);
        if (clip != null) {
            // Play sound in a separate thread to avoid blocking
            new Thread(() -> {
                try {
                    synchronized (clip) {
                        if (clip.isRunning()) {
                            clip.stop();
                        }
                        clip.setFramePosition(0);
                        clip.start();
                    }
                } catch (Exception e) {
                    // Silently handle sound playback errors
                }
            }).start();
        }
    }

    /**
     * Plays a sound effect and waits for it to complete.
     *
     * @param soundType The type of sound to play.
     */
    public void playSoundAndWait(SoundType soundType) {
        if (!soundEnabled) {
            return;
        }

        Clip clip = soundClips.get(soundType);
        if (clip != null) {
            try {
                synchronized (clip) {
                    if (clip.isRunning()) {
                        clip.stop();
                    }
                    clip.setFramePosition(0);
                    clip.start();
                }

                // Wait for the clip to finish
                while (clip.isRunning()) {
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                // Silently handle sound playback errors
            }
        }
    }

    /**
     * Stops all currently playing sounds.
     */
    public void stopAllSounds() {
        for (Clip clip : soundClips.values()) {
            if (clip != null) {
                synchronized (clip) {
                    if (clip.isRunning()) {
                        clip.stop();
                    }
                }
            }
        }
    }

    /**
     * Sets the volume for a specific clip.
     *
     * @param clip   The clip to adjust.
     * @param volume The volume level (0.0 to 1.0).
     */
    private void setVolume(Clip clip, float volume) {
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        } catch (IllegalArgumentException e) {
            // Volume control not supported for this clip
        }
    }

    /**
     * Sets the master volume for all sounds.
     *
     * @param volume The volume level (0.0 to 1.0).
     */
    public void setMasterVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        for (Clip clip : soundClips.values()) {
            if (clip != null) {
                setVolume(clip, this.volume);
            }
        }
    }

    /**
     * Enables or disables sound effects.
     *
     * @param enabled True to enable sounds, false to disable.
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) {
            stopAllSounds();
        }
    }

    /**
     * Checks if sound is enabled.
     *
     * @return True if sound is enabled, false otherwise.
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * Gets the current master volume.
     *
     * @return The volume level (0.0 to 1.0).
     */
    public float getMasterVolume() {
        return volume;
    }

    /**
     * Releases all sound resources.
     * Should be called when the game is closing.
     */
    public void cleanup() {
        stopAllSounds();
        for (Clip clip : soundClips.values()) {
            if (clip != null) {
                clip.close();
            }
        }
        soundClips.clear();
    }

    /**
     * Convenience method to play attack sound.
     */
    public void playAttackSound() {
        playSound(SoundType.ATTACK);
    }

    /**
     * Convenience method to play damage sound.
     */
    public void playDamageSound() {
        playSound(SoundType.DAMAGE_TAKEN);
    }

    /**
     * Convenience method to play potion sound.
     */
    public void playPotionSound() {
        playSound(SoundType.POTION_USE);
    }

    /**
     * Convenience method to play spell sound.
     */
    public void playSpellSound() {
        playSound(SoundType.SPELL_CAST);
    }

    /**
     * Convenience method to play market sound.
     */
    public void playMarketSound() {
        playSound(SoundType.MARKET_ENTER);
    }

    /**
     * Convenience method to play buy sound.
     */
    public void playBuySound() {
        playSound(SoundType.BUY_ITEM);
    }

    /**
     * Convenience method to play sell sound.
     */
    public void playSellSound() {
        playSound(SoundType.SELL_ITEM);
    }

    /**
     * Convenience method to play victory sound.
     */
    public void playVictorySound() {
        playSound(SoundType.VICTORY);
    }

    /**
     * Convenience method to play defeat sound.
     */
    public void playDefeatSound() {
        playSound(SoundType.DEFEAT);
    }

    /**
     * Convenience method to play level up sound.
     */
    public void playLevelUpSound() {
        playSound(SoundType.LEVEL_UP);
    }

    /**
     * Convenience method to play equip sound.
     */
    public void playEquipSound() {
        playSound(SoundType.EQUIP_ITEM);
    }

    /**
     * Convenience method to play monster death sound.
     */
    public void playMonsterDeathSound() {
        playSound(SoundType.MONSTER_DEATH);
    }

    /**
     * Convenience method to play hero death sound.
     */
    public void playHeroDeathSound() {
        playSound(SoundType.HERO_DEATH);
    }

    /**
     * Convenience method to play teleport sound.
     */
    public void playTeleportSound() {
        playSound(SoundType.TELEPORT);
    }

    /**
     * Convenience method to play recall sound.
     */
    public void playRecallSound() {
        playSound(SoundType.RECALL);
    }

    /**
     * Convenience method to play move sound.
     */
    public void playMoveSound() {
        playSound(SoundType.MOVE);
    }

    /**
     * Convenience method to play dodge sound.
     */
    public void playDodgeSound() {
        playSound(SoundType.DODGE);
    }

    /**
     * Convenience method to play game start sound.
     */
    public void playGameStartSound() {
        playSound(SoundType.GAME_START);
    }
}
