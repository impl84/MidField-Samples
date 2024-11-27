
package grpc.performer;

import java.util.List;

import com.midfield_system.grpc.v1.AudioFormat;
import com.midfield_system.grpc.v1.DeviceInfo;
import com.midfield_system.grpc.v1.Format;
import com.midfield_system.grpc.v1.Format.FormatTypeCase;
import com.midfield_system.grpc.v1.StreamFormat;
import com.midfield_system.grpc.v1.VideoFormat;
import com.midfield_system.grpc.v1.WmvFormat;

public class Debug
{
    public static void printDeviceInfo(DeviceInfo devInf)
    {
        System.out.println(devInf.getDeviceName());
        int idx = devInf.getDefaultFormatIndex();
        System.out.println("Preferred Format Index: " + idx);
        System.out.println(devInf.getFormats(idx));
    }
    
    public static void printFormatsList(List<Format> list)
    {
        for (var format : list) {
            FormatTypeCase type = format.getFormatTypeCase();
            switch (type) {
            case STREAM_FORMAT:
                StreamFormat streamFormat = format.getStreamFormat();
                System.out.println(streamFormat);
                break;
            case AUDIO_FORMAT:
                AudioFormat audioFormat = format.getAudioFormat();
                System.out.println(audioFormat);
                break;
            case VIDEO_FORMAT:
                VideoFormat videoFormat = format.getVideoFormat();
                System.out.println(videoFormat);
                break;
            case WMV_FORMAT:
                WmvFormat wmvFormat = format.getWmvFormat();
                System.out.println(wmvFormat);
                break;
            case FORMATTYPE_NOT_SET:
                System.out.println("FORMATTYPE_NOT_SET");
                break;
            }
        }
    }
}
