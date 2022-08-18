
package performer;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.DeviceInfo;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.util.Log;
import com.midfield_system.api.viewer.VideoCanvas;

import util.ConsolePrinter;
import util.ConsoleReader;
import util.SimpleViewer;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: DeviceToRenderer
 * 
 * Date Modified: 2022.06.09
 *
 */
public class DeviceToRenderer
{
// =============================================================================
// CLASS METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static void main(String[] args)
    {
        // �R���\�[������̕������͂����� ConsoleReader �̃C���X�^���X���擾����D
        ConsoleReader reader = ConsoleReader.getInstance();
        
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsNode         mfs  = null;
        StreamPerformer pfmr = null;
        
        try {
            // MidField System �����������C�N������D
            mfs = MfsNode.initialize();
            mfs.activate();
            
            // �r�f�I�ƃI�[�f�B�I�̓��̓f�o�C�X��񃊃X�g���擾���C
            // ���p������̓f�o�C�X��I������D
            // �i�����ł͍ŏ��̓��̓f�o�C�X��I������D�j
            DeviceInfoManager devInfMgr = DeviceInfoManager.getInstance();
            DeviceInfo        vidDev    = devInfMgr.getInputVideoDeviceInfoList().get(0);
            DeviceInfo        audDev    = devInfMgr.getInputAudioDeviceInfoList().get(0);
            
            // SegmentIo �̓��͂���̓f�o�C�X�Ƃ��č\������D
            SegmentIo segIo = new SegmentIo();
            segIo.configureInputDevice(vidDev, audDev);
            
            // SegmentIo �̏o�͂��f�t�H���g�����_���Ƃ��č\������D
            segIo.configureDefaultRenderer();
            
            // ���C�u�\�[�X�I�v�V������L���ɂ���D
            segIo.setLiveSource(true);
            
            // SegmentIo �����Ƃ� StreamPerformer �𐶐�����D
            pfmr = StreamPerformer.newInstance(segIo);
            
            // StreamPerformer ���� VideoCanvas ���擾����D
            VideoCanvas vidCvs = pfmr.getVideoCanvas();
            
            // StreamPerformer ���� VideoCanvas ���擾���CSimpleViewer �ɒǉ�����D
            SwingUtilities.invokeAndWait(() -> {
                new SimpleViewer("Device to Renderer", vidCvs);
            });
            // ���o�͏������J�n����D
            pfmr.open();
            pfmr.start();
            
            // Enter�L�[�̓��͂�҂D
            System.out.printf("> Enter �L�[�̓��͂�҂��܂��D");
            reader.readLine();
            
            // ���o�͏������I������D
            pfmr.stop();
            pfmr.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // StreamPerformer �̏������I������D
            if (pfmr != null) {
                pfmr.delete();
            }
            // MidField System ���I������D
            if (mfs != null) {
                mfs.shutdown();
            }
            // ConsoleReader ���������D
            reader.release();
        }
    }
}