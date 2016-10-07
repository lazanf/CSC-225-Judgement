/**********************************************************************
 * @author Travis R. Dewitt
 * @version 0.8
 * Date: June 15, 2015
 * <p>
 * Title: Midi
 * Description: Construct a Midi file sound object.
 * This is merely some framework and is currently unused in the given game, Judgement.
 * <p>
 * This class should work, however is it untested.
 * <p>
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 *********************************************************************/
package axohEngine2.sound;

import javax.sound.midi.*;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MidiSequence {
    //primary midi sequencer object
    private Sequencer sequencer;

    //Make sequence a read-only property
    private Sequence song;
    //Filename property - read only
    private String filename;
    //looping property for continuous playback
    private boolean looping = false;
    //Repeat sound multiple times
    private int repeat = 0;

    //Constructor
    public MidiSequence() {
        try {
            //Start sequencer
            sequencer = MidiSystem.getSequencer();
        } catch (MidiUnavailableException ignored) {
        }
    }

    //Constructor that accepts a filename
    public MidiSequence(String midiFile) {
        try {
          /*  //load the midi file in to the sequencer
            filename = midifile;
            song = MidiSystem.getSequence(getURL(filename));
            sequencer.setSequence(song);
            sequencer.open();*/
        	sequencer = MidiSystem.getSequencer();
        	sequencer.open();
        	InputStream is = new BufferedInputStream(new FileInputStream(new File(midiFile)));
        	sequencer.setSequence(is);
        	sequencer.start();
        } catch (InvalidMidiDataException | MidiUnavailableException | IOException ignored) {
        }
    }

    public Sequence getSong() {
        return song;
    }

    public String getFilename() {
        return filename;
    }

    public boolean getLooping() {
        return looping;
    }

    public void setLooping(boolean _looping) {
        looping = _looping;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int _repeat) {
        repeat = _repeat;
    }

    //Is the sequence ready?
    public boolean isLoaded() {
        return sequencer.isOpen();
    }

    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        } catch (Exception ignored) {
        }
        return url;
    }

    //Play the midi sequence
    public void play() {
        if (!sequencer.isOpen()) return;
        if (looping) {
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
        } else {
            sequencer.setLoopCount(repeat);
            sequencer.start();
        }
    }

    public void stop() {
        sequencer.stop();
    }
}