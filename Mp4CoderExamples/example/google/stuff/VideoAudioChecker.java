package me.zuichu.mp4coder.example.google.stuff;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import me.zuichu.mp4coder.IsoFile;
import me.zuichu.mp4coder.boxes.iso14496.part12.HandlerBox;

public class VideoAudioChecker {

    public static void main(String[] args) throws IOException {
        String f = VideoAudioChecker.class.getProtectionDomain().getCodeSource().getLocation().getFile() + "/count-video.mp4";
        IsoFile isoFile = new IsoFile(new FileInputStream(f).getChannel());
        System.err.println(getType(isoFile));
    }

    public static TYPE getType(IsoFile isoFile) {

        List<HandlerBox> handlerBoxes =
                isoFile.getBoxes(HandlerBox.class, true);
        for (HandlerBox handlerBox : handlerBoxes) {
            if ("vide".equals(handlerBox.getHandlerType())) {
                return TYPE.VIDEO;
            } else if ("soun".equals(handlerBox.getHandlerType())) {
                return TYPE.AUDIO;
            } else {
                System.err.println("unknown");
            }

        }
        return TYPE.AUDIO;
    }

    private enum TYPE {
        AUDIO,
        VIDEO
    }

}
