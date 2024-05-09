
package trial.performer;

import java.util.List;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.DeviceInfo;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsException;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.util.Log;
import com.midfield_system.api.viewer.VideoCanvas;

import util.ConsolePrinter;
import util.SimpleViewer;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.08.28
 *
 */
public class DeviceToRenderer
{
    public static void main(String[] args)
    {
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsNode         mfs  = null;
        StreamPerformer pfmr = null;
        
        try {
            // ��MidField System �����������C�N������D
            mfs = MfsNode.initialize();
            mfs.activate();
            
            // ���r�f�I�ƃI�[�f�B�I�f�o�C�X�œ��͂��\������D
            // �E���̓f�o�C�X��I�����Đݒ肷��D
            DeviceInfoManager devInfMgr = DeviceInfoManager.getInstance();
            
            List<DeviceInfo> lsVidDev = devInfMgr.getVideoInputDeviceInfoList();
            if (lsVidDev.size() <= 0) {
                System.out.println("�����p�\�ȃr�f�I���̓f�o�C�X������܂���D");
                return;
            }
            List<DeviceInfo> lsAudDev = devInfMgr.getAudioInputDeviceInfoList();
            if (lsAudDev.size() <= 0) {
                System.out.println("�����p�\�ȃI�[�f�B�I���̓f�o�C�X������܂���D");
                return;
            }
            SegmentIo segIo = new SegmentIo();
            segIo.configureInputDevice(lsVidDev.get(0), lsAudDev.get(0));
            
            // ���o�͂������_���Ƃ��č\������D
            segIo.configureRenderer();
            
            // ��StreamPerformer �𐶐����C���̓����ɂ��� VideoCanvas �����o���D
            //   ���̌�CVideoCanvas ��\�����邽�߂� SimpleViewer �𐶐����ĕ\������D
            pfmr = StreamPerformer.newInstance(segIo);
            VideoCanvas vidCvs = pfmr.getVideoCanvas();
            SwingUtilities.invokeAndWait(() -> {
                new SimpleViewer("Device to Renderer", vidCvs);
            });
            // ��StreamPerformer �̓��o�͏������J�n����D
            pfmr.open();
            pfmr.start();
            
            // ��StreamPerformer �̓��o�͏������I������D
            System.out.print("> Enter �L�[�̓��͂�҂��܂��D");
            System.in.read();
            
            pfmr.stop();
            pfmr.close();
        }
        catch (MfsException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ��StreamPerformer �̑S�Ă̏������I������D
            if (pfmr != null) {
                pfmr.delete();
            }
            // ��MidField System ���I������D
            if (mfs != null) {
                mfs.shutdown();
            }
        }
    }
}